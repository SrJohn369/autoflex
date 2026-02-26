package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.ProductionSuggestionItemDTO;
import com.projeto.autoflex.dto.ProductionSuggestionResponseDTO;
import com.projeto.autoflex.entity.Product;
import com.projeto.autoflex.entity.ProductMaterial;
import com.projeto.autoflex.entity.RawMaterial;
import com.projeto.autoflex.repository.ProductRepository;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Suggests which products (and quantities) can be produced with current stock.
 * Prioritizes products by value (highest first) since a raw material can be
 * used in multiple products.
 */
@Service
public class ProductionSuggestionService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductionSuggestionService(ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Transactional(readOnly = true)
    public ProductionSuggestionResponseDTO getProductionSuggestion() {
        List<Product> products = productRepository.findAllWithMaterialsOrderByValueDesc();
        List<RawMaterial> allMaterials = rawMaterialRepository.findAll();

        Map<Long, BigDecimal> stock = new HashMap<>();
        for (RawMaterial rm : allMaterials) {
            stock.put(rm.getId(), rm.getQuantityInStock() != null ? rm.getQuantityInStock() : BigDecimal.ZERO);
        }

        ProductionSuggestionResponseDTO response = new ProductionSuggestionResponseDTO();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            if (product.getMaterials() == null || product.getMaterials().isEmpty())
                continue;

            BigDecimal maxQty = null;
            for (ProductMaterial pm : product.getMaterials()) {
                Long rmId = pm.getRawMaterial().getId();
                BigDecimal available = stock.getOrDefault(rmId, BigDecimal.ZERO);
                BigDecimal required = pm.getQuantityRequired();
                if (required == null || required.compareTo(BigDecimal.ZERO) <= 0)
                    continue;
                BigDecimal qty = available.divide(required, 10, RoundingMode.DOWN);
                if (maxQty == null || qty.compareTo(maxQty) < 0) {
                    maxQty = qty;
                }
            }

            if (maxQty == null || maxQty.compareTo(BigDecimal.ZERO) <= 0)
                continue;

            BigDecimal quantityProducible = maxQty.setScale(0, RoundingMode.DOWN);
            if (quantityProducible.compareTo(BigDecimal.ZERO) <= 0)
                continue;

            for (ProductMaterial pm : product.getMaterials()) {
                Long rmId = pm.getRawMaterial().getId();
                BigDecimal used = pm.getQuantityRequired().multiply(quantityProducible);
                stock.put(rmId, stock.get(rmId).subtract(used));
            }

            ProductionSuggestionItemDTO item = new ProductionSuggestionItemDTO();
            item.setProductId(product.getId());
            item.setProductCode(product.getCode());
            item.setProductName(product.getName());
            item.setProductValue(product.getValue());
            item.setQuantityProducible(quantityProducible);
            item.setTotalValue(product.getValue().multiply(quantityProducible));
            response.getItems().add(item);
            totalValue = totalValue.add(item.getTotalValue());
        }

        response.setTotalValue(totalValue);
        return response;
    }
}

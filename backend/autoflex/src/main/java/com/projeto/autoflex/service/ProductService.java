package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.ProductDTO;
import com.projeto.autoflex.dto.ProductMaterialDTO;
import com.projeto.autoflex.entity.Product;
import com.projeto.autoflex.entity.ProductMaterial;
import com.projeto.autoflex.entity.RawMaterial;
import com.projeto.autoflex.repository.ProductRepository;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductService(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAllWithMaterialsOrderByValueDesc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toDTO(product);
    }

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        if (productRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Produto já cadastrado com o código: " + dto.getCode());
        }
        Product product = toEntity(dto);
        product = productRepository.save(product);
        saveMaterials(product, dto.getMaterials());
        product = productRepository.findById(product.getId()).orElseThrow();
        return toDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        if (productRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new RuntimeException("Produto já cadastrado com o código: " + dto.getCode());
        }
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setValue(dto.getValue());
        product.getMaterials().clear();
        productRepository.flush();
        saveMaterials(product, dto.getMaterials());
        product = productRepository.findById(id).orElseThrow();
        return toDTO(product);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        productRepository.deleteById(id);
    }

    private void saveMaterials(Product product, List<ProductMaterialDTO> materialDTOs) {
        if (materialDTOs == null || materialDTOs.isEmpty()) return;
        for (ProductMaterialDTO mdto : materialDTOs) {
            if (mdto.getRawMaterialId() == null || mdto.getQuantityRequired() == null) continue;
            RawMaterial raw = rawMaterialRepository.findById(mdto.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Matéria-prima não encontrada"));
            ProductMaterial pm = new ProductMaterial();
            pm.setProduct(product);
            pm.setRawMaterial(raw);
            pm.setQuantityRequired(mdto.getQuantityRequired());
            product.getMaterials().add(pm);
        }
        productRepository.saveAndFlush(product);
    }

    private Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setCode(dto.getCode());
        p.setName(dto.getName());
        p.setValue(dto.getValue());
        return p;
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setValue(product.getValue());
        List<ProductMaterialDTO> materials = new ArrayList<>();
        for (ProductMaterial pm : product.getMaterials()) {
            ProductMaterialDTO mdto = new ProductMaterialDTO();
            mdto.setId(pm.getId());
            mdto.setRawMaterialId(pm.getRawMaterial().getId());
            mdto.setRawMaterialCode(pm.getRawMaterial().getCode());
            mdto.setRawMaterialName(pm.getRawMaterial().getName());
            mdto.setQuantityRequired(pm.getQuantityRequired());
            materials.add(mdto);
        }
        dto.setMaterials(materials);
        return dto;
    }
}

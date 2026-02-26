package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.ProductionSuggestionItemDTO;
import com.projeto.autoflex.dto.ProductionSuggestionResponseDTO;
import com.projeto.autoflex.entity.Product;
import com.projeto.autoflex.entity.ProductMaterial;
import com.projeto.autoflex.entity.RawMaterial;
import com.projeto.autoflex.repository.ProductRepository;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionSuggestionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionSuggestionService productionSuggestionService;

    private Product productA;
    private Product productB;
    private RawMaterial rawMaterial1;
    private RawMaterial rawMaterial2;

    @BeforeEach
    void setUp() {
        rawMaterial1 = new RawMaterial();
        rawMaterial1.setId(1L);
        rawMaterial1.setCode("RM01");
        rawMaterial1.setName("Screw");
        rawMaterial1.setQuantityInStock(new BigDecimal("100")); // We have 100 screws

        rawMaterial2 = new RawMaterial();
        rawMaterial2.setId(2L);
        rawMaterial2.setCode("RM02");
        rawMaterial2.setName("Wood");
        rawMaterial2.setQuantityInStock(new BigDecimal("50")); // We have 50 wood pieces

        productA = new Product();
        productA.setId(1L);
        productA.setCode("P01");
        productA.setName("Table");
        productA.setValue(new BigDecimal("200.00")); // Higher value
        productA.setMaterials(new ArrayList<>());

        // Table needs 10 screws and 5 wood pieces
        ProductMaterial pmA1 = new ProductMaterial();
        pmA1.setRawMaterial(rawMaterial1);
        pmA1.setQuantityRequired(new BigDecimal("10"));
        ProductMaterial pmA2 = new ProductMaterial();
        pmA2.setRawMaterial(rawMaterial2);
        pmA2.setQuantityRequired(new BigDecimal("5"));
        productA.getMaterials().add(pmA1);
        productA.getMaterials().add(pmA2);

        productB = new Product();
        productB.setId(2L);
        productB.setCode("P02");
        productB.setName("Chair");
        productB.setValue(new BigDecimal("50.00")); // Lower value
        productB.setMaterials(new ArrayList<>());

        // Chair needs 4 screws and 2 wood pieces
        ProductMaterial pmB1 = new ProductMaterial();
        pmB1.setRawMaterial(rawMaterial1);
        pmB1.setQuantityRequired(new BigDecimal("4"));
        ProductMaterial pmB2 = new ProductMaterial();
        pmB2.setRawMaterial(rawMaterial2);
        pmB2.setQuantityRequired(new BigDecimal("2"));
        productB.getMaterials().add(pmB1);
        productB.getMaterials().add(pmB2);
    }

    @Test
    void getProductionSuggestion_ShouldSuggestBasedOnHighestValue() {
        // Table (value 200), Chair (value 50). Ordered desc by value.
        when(productRepository.findAllWithMaterialsOrderByValueDesc()).thenReturn(List.of(productA, productB));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial1, rawMaterial2));

        // We have 100 screws and 50 wood. 
        // Table needs 10 screws, 5 wood. Producible tables = min(100/10, 50/5) = min(10, 10) = 10 tables.
        // Making 10 tables consumes 100 screws and 50 woods. 
        // Remaining: 0 screws, 0 woods.
        // Producible Chairs = 0.

        ProductionSuggestionResponseDTO response = productionSuggestionService.getProductionSuggestion();

        assertNotNull(response);
        assertEquals(1, response.getItems().size()); // Only tables should be suggested due to running out of stock
        
        ProductionSuggestionItemDTO tableItem = response.getItems().get(0);
        assertEquals("P01", tableItem.getProductCode());
        assertEquals(0, new BigDecimal("10").compareTo(tableItem.getQuantityProducible()));
        assertEquals(0, new BigDecimal("2000.00").compareTo(tableItem.getTotalValue()));

        assertEquals(0, new BigDecimal("2000.00").compareTo(response.getTotalValue()));

        verify(productRepository, times(1)).findAllWithMaterialsOrderByValueDesc();
        verify(rawMaterialRepository, times(1)).findAll();
    }

    @Test
    void getProductionSuggestion_ShouldLeaveRemaindersForLowerValueProducts() {
        // We have 108 screws and 54 wood limit.
        // Table needs 10 screws, 5 wood. Producible tables = 10. Leftovers = 8 screws,
        // 4 wood.
        // Chair needs 4 screws, 2 wood. Producible chairs with leftovers = min(8/4,
        // 4/2) = 2.
        rawMaterial1.setQuantityInStock(new BigDecimal("108"));
        rawMaterial2.setQuantityInStock(new BigDecimal("54"));

        when(productRepository.findAllWithMaterialsOrderByValueDesc()).thenReturn(List.of(productA, productB));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial1, rawMaterial2));

        ProductionSuggestionResponseDTO response = productionSuggestionService.getProductionSuggestion();

        assertNotNull(response);
        assertEquals(2, response.getItems().size()); // Suggests tables and then remainder on chairs

        ProductionSuggestionItemDTO tableItem = response.getItems().get(0);
        assertEquals("P01", tableItem.getProductCode());
        assertEquals(0, new BigDecimal("10").compareTo(tableItem.getQuantityProducible()));
        assertEquals(0, new BigDecimal("2000.00").compareTo(tableItem.getTotalValue())); // 10 * 200 = 2000

        ProductionSuggestionItemDTO chairItem = response.getItems().get(1);
        assertEquals("P02", chairItem.getProductCode());
        assertEquals(0, new BigDecimal("2").compareTo(chairItem.getQuantityProducible()));
        assertEquals(0, new BigDecimal("100.00").compareTo(chairItem.getTotalValue())); // 2 * 50 = 100

        assertEquals(0, new BigDecimal("2100.00").compareTo(response.getTotalValue()));
    }

    @Test
    void getProductionSuggestion_ShouldIgnoreProductsWithMissingMaterials() {
        Product productC = new Product();
        productC.setId(3L);
        productC.setCode("P03");
        productC.setName("Ghost Item");
        productC.setValue(new BigDecimal("5000.00"));
        // Empty materials array
        productC.setMaterials(new ArrayList<>());

        when(productRepository.findAllWithMaterialsOrderByValueDesc()).thenReturn(List.of(productC, productA));
        when(rawMaterialRepository.findAll()).thenReturn(List.of(rawMaterial1, rawMaterial2));

        ProductionSuggestionResponseDTO response = productionSuggestionService.getProductionSuggestion();

        assertNotNull(response);
        // Should completely ignore product C since it has no materials listed
        assertEquals(1, response.getItems().size());
        assertEquals("P01", response.getItems().get(0).getProductCode());
    }
}

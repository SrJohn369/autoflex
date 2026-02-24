package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.ProductDTO;
import com.projeto.autoflex.entity.Product;
import com.projeto.autoflex.repository.ProductRepository;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setCode("PRD001");
        product.setName("Bicycle");
        product.setValue(new BigDecimal("1500.00"));

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setCode("PRD001");
        productDTO.setName("Bicycle");
        productDTO.setValue(new BigDecimal("1500.00"));
        productDTO.setMaterials(new ArrayList<>());
    }

    @Test
    void findById_ShouldReturnDto_WhenIdExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getCode(), result.getCode());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.findById(2L));
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    void create_ShouldReturnDto_WhenCodeDoesNotExist() {
        when(productRepository.existsByCode("PRD001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.create(productDTO);

        assertNotNull(result);
        assertEquals("PRD001", result.getCode());
        verify(productRepository, times(1)).existsByCode("PRD001");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void create_ShouldThrowException_WhenCodeAlreadyExists() {
        when(productRepository.existsByCode("PRD001")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.create(productDTO));
        assertEquals("Produto já cadastrado com o código: PRD001", exception.getMessage());
        verify(productRepository, times(1)).existsByCode("PRD001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void update_ShouldReturnUpdatedDto_WhenValid() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByCodeAndIdNot("PRD001", 1L)).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product)); // for the reload step

        ProductDTO updatedDto = new ProductDTO();
        updatedDto.setCode("PRD001");
        updatedDto.setName("Mountain Bike");
        updatedDto.setValue(new BigDecimal("2000.00"));
        updatedDto.setMaterials(new ArrayList<>());

        ProductDTO result = productService.update(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Mountain Bike", result.getName());
        verify(productRepository, times(2)).findById(1L);
        verify(productRepository, times(1)).existsByCodeAndIdNot("PRD001", 1L);
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.update(1L, productDTO));
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).existsByCodeAndIdNot(anyString(), anyLong());
    }

    @Test
    void deleteById_ShouldDelete_WhenIdExists() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteById(1L));
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenIdDoesNotExist() {
        when(productRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteById(1L));
        assertEquals("Produto não encontrado", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}

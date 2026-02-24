package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.RawMaterialDTO;
import com.projeto.autoflex.entity.RawMaterial;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    private RawMaterial rawMaterial;
    private RawMaterialDTO rawMaterialDTO;

    @BeforeEach
    void setUp() {
        rawMaterial = new RawMaterial();
        rawMaterial.setId(1L);
        rawMaterial.setCode("RM001");
        rawMaterial.setName("Aluminum");
        rawMaterial.setQuantityInStock(new BigDecimal("100.50"));

        rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setId(1L);
        rawMaterialDTO.setCode("RM001");
        rawMaterialDTO.setName("Aluminum");
        rawMaterialDTO.setQuantityInStock(new BigDecimal("100.50"));
    }

    @Test
    void findById_ShouldReturnDto_WhenIdExists() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));

        RawMaterialDTO result = rawMaterialService.findById(1L);

        assertNotNull(result);
        assertEquals(rawMaterial.getId(), result.getId());
        assertEquals(rawMaterial.getCode(), result.getCode());
        verify(rawMaterialRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(rawMaterialRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rawMaterialService.findById(2L));
        assertEquals("Matéria-prima não encontrada", exception.getMessage());
        verify(rawMaterialRepository, times(1)).findById(2L);
    }

    @Test
    void create_ShouldReturnDto_WhenCodeDoesNotExist() {
        when(rawMaterialRepository.existsByCode("RM001")).thenReturn(false);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        RawMaterialDTO result = rawMaterialService.create(rawMaterialDTO);

        assertNotNull(result);
        assertEquals("RM001", result.getCode());
        verify(rawMaterialRepository, times(1)).existsByCode("RM001");
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void create_ShouldThrowException_WhenCodeAlreadyExists() {
        when(rawMaterialRepository.existsByCode("RM001")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rawMaterialService.create(rawMaterialDTO));
        assertEquals("Matéria-prima já cadastrada com o código: RM001", exception.getMessage());
        verify(rawMaterialRepository, times(1)).existsByCode("RM001");
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void update_ShouldReturnUpdatedDto_WhenValid() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.existsByCodeAndIdNot("RM001", 1L)).thenReturn(false);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        RawMaterialDTO updatedDto = new RawMaterialDTO();
        updatedDto.setCode("RM001");
        updatedDto.setName("Aluminum Updated");
        updatedDto.setQuantityInStock(new BigDecimal("150.00"));

        RawMaterialDTO result = rawMaterialService.update(1L, updatedDto);

        assertNotNull(result);
        assertEquals("RM001", result.getCode());
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).existsByCodeAndIdNot("RM001", 1L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rawMaterialService.update(1L, rawMaterialDTO));
        assertEquals("Matéria-prima não encontrada", exception.getMessage());
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, never()).existsByCodeAndIdNot(anyString(), anyLong());
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void deleteById_ShouldDelete_WhenIdExists() {
        when(rawMaterialRepository.existsById(1L)).thenReturn(true);
        doNothing().when(rawMaterialRepository).deleteById(1L);

        assertDoesNotThrow(() -> rawMaterialService.deleteById(1L));
        verify(rawMaterialRepository, times(1)).existsById(1L);
        verify(rawMaterialRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenIdDoesNotExist() {
        when(rawMaterialRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rawMaterialService.deleteById(1L));
        assertEquals("Matéria-prima não encontrada", exception.getMessage());
        verify(rawMaterialRepository, times(1)).existsById(1L);
        verify(rawMaterialRepository, never()).deleteById(anyLong());
    }
}

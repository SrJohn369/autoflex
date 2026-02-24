package com.projeto.autoflex.service;

import com.projeto.autoflex.dto.RawMaterialDTO;
import com.projeto.autoflex.entity.RawMaterial;
import com.projeto.autoflex.repository.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Transactional(readOnly = true)
    public List<RawMaterialDTO> findAll() {
        return rawMaterialRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RawMaterialDTO findById(Long id) {
        RawMaterial raw = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria-prima não encontrada"));
        return toDTO(raw);
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        if (rawMaterialRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Matéria-prima já cadastrada com o código: " + dto.getCode());
        }
        RawMaterial raw = toEntity(dto);
        raw = rawMaterialRepository.save(raw);
        return toDTO(raw);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial raw = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matéria-prima não encontrada"));
        if (rawMaterialRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new RuntimeException("Matéria-prima já cadastrada com o código: " + dto.getCode());
        }
        raw.setCode(dto.getCode());
        raw.setName(dto.getName());
        raw.setQuantityInStock(dto.getQuantityInStock());
        raw = rawMaterialRepository.save(raw);
        return toDTO(raw);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new RuntimeException("Matéria-prima não encontrada");
        }
        rawMaterialRepository.deleteById(id);
    }

    private RawMaterial toEntity(RawMaterialDTO dto) {
        RawMaterial r = new RawMaterial();
        r.setId(dto.getId());
        r.setCode(dto.getCode());
        r.setName(dto.getName());
        r.setQuantityInStock(dto.getQuantityInStock());
        return r;
    }

    private RawMaterialDTO toDTO(RawMaterial raw) {
        RawMaterialDTO dto = new RawMaterialDTO();
        dto.setId(raw.getId());
        dto.setCode(raw.getCode());
        dto.setName(raw.getName());
        dto.setQuantityInStock(raw.getQuantityInStock());
        return dto;
    }
}

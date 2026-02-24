package com.projeto.autoflex.controller;

import com.projeto.autoflex.dto.RawMaterialDTO;
import com.projeto.autoflex.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @GetMapping
    public List<RawMaterialDTO> findAll() {
        return rawMaterialService.findAll();
    }

    @GetMapping("/{id}")
    public RawMaterialDTO findById(@PathVariable Long id) {
        return rawMaterialService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterialDTO create(@Valid @RequestBody RawMaterialDTO dto) {
        return rawMaterialService.create(dto);
    }

    @PutMapping("/{id}")
    public RawMaterialDTO update(@PathVariable Long id, @Valid @RequestBody RawMaterialDTO dto) {
        return rawMaterialService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rawMaterialService.deleteById(id);
    }
}

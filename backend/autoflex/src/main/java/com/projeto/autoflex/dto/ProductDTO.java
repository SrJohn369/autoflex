package com.projeto.autoflex.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal value;

    @Valid
    private List<ProductMaterialDTO> materials = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public List<ProductMaterialDTO> getMaterials() {
        return materials;
    }

    public void setMaterials(List<ProductMaterialDTO> materials) {
        this.materials = materials != null ? materials : new ArrayList<>();
    }
}

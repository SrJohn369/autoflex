package com.projeto.autoflex.dto;

import java.math.BigDecimal;

public class ProductionSuggestionItemDTO {

    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal productValue;
    private BigDecimal quantityProducible;
    private BigDecimal totalValue;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductValue() {
        return productValue;
    }

    public void setProductValue(BigDecimal productValue) {
        this.productValue = productValue;
    }

    public BigDecimal getQuantityProducible() {
        return quantityProducible;
    }

    public void setQuantityProducible(BigDecimal quantityProducible) {
        this.quantityProducible = quantityProducible;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}

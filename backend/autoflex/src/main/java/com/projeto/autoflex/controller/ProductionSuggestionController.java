package com.projeto.autoflex.controller;

import com.projeto.autoflex.dto.ProductionSuggestionResponseDTO;
import com.projeto.autoflex.service.ProductionSuggestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production-suggestion")
public class ProductionSuggestionController {

    private final ProductionSuggestionService productionSuggestionService;

    public ProductionSuggestionController(ProductionSuggestionService productionSuggestionService) {
        this.productionSuggestionService = productionSuggestionService;
    }

    @GetMapping
    public ProductionSuggestionResponseDTO getProductionSuggestion() {
        return productionSuggestionService.getProductionSuggestion();
    }
}

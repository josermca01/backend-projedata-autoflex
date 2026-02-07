package com.inventory.controller;

import com.inventory.dto.ProductionSuggestionDTO;
import com.inventory.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductionController {
    
    private final ProductionService productionService;
    
    @GetMapping("/suggestion")
    public ResponseEntity<ProductionSuggestionDTO> getProductionSuggestion() {
        return ResponseEntity.ok(productionService.calculateProductionSuggestion());
    }
}

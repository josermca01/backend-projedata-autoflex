package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionDTO {
    
    private List<ProductionItemDTO> items;
    private BigDecimal totalValue;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductionItemDTO {
        private Long productId;
        private String productName;
        private BigDecimal productValue;
        private Integer quantityToProduce;
        private BigDecimal totalValue;
    }
}

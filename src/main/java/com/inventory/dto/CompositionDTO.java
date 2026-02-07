package com.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositionDTO {
    
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;
    
    private String rawMaterialName;
    
    @NotNull(message = "Quantity required is required")
    @Positive(message = "Quantity required must be positive")
    private BigDecimal quantityRequired;
}

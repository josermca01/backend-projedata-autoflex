package com.inventory.service;

import com.inventory.dto.ProductionSuggestionDTO;
import com.inventory.model.Product;
import com.inventory.model.ProductComposition;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionService {
    
    private final ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public ProductionSuggestionDTO calculateProductionSuggestion() {
        List<Product> products = productRepository.findAllOrderByValueDesc();
        Map<Long, BigDecimal> availableStock = new HashMap<>();
        List<ProductionSuggestionDTO.ProductionItemDTO> productionItems = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.ZERO;
        
        // Initialize available stock
        for (Product product : products) {
            for (ProductComposition composition : product.getCompositions()) {
                availableStock.putIfAbsent(
                    composition.getRawMaterial().getId(),
                    composition.getRawMaterial().getStockQuantity()
                );
            }
        }
        
        // Calculate production for each product (ordered by value DESC)
        for (Product product : products) {
            if (product.getCompositions().isEmpty()) {
                continue;
            }
            
            // Calculate maximum quantity that can be produced with available stock
            Integer maxQuantity = calculateMaxProductionQuantity(product, availableStock);
            
            if (maxQuantity > 0) {
                // Deduct used materials from available stock
                for (ProductComposition composition : product.getCompositions()) {
                    Long materialId = composition.getRawMaterial().getId();
                    BigDecimal used = composition.getQuantityRequired()
                            .multiply(BigDecimal.valueOf(maxQuantity));
                    availableStock.put(materialId, availableStock.get(materialId).subtract(used));
                }
                
                // Add to production suggestion
                BigDecimal itemTotalValue = product.getValue().multiply(BigDecimal.valueOf(maxQuantity));
                totalValue = totalValue.add(itemTotalValue);
                
                ProductionSuggestionDTO.ProductionItemDTO item = 
                    new ProductionSuggestionDTO.ProductionItemDTO(
                        product.getId(),
                        product.getName(),
                        product.getValue(),
                        maxQuantity,
                        itemTotalValue
                    );
                productionItems.add(item);
            }
        }
        
        return new ProductionSuggestionDTO(productionItems, totalValue);
    }
    
    private Integer calculateMaxProductionQuantity(Product product, Map<Long, BigDecimal> availableStock) {
        Integer maxQuantity = Integer.MAX_VALUE;
        
        for (ProductComposition composition : product.getCompositions()) {
            Long materialId = composition.getRawMaterial().getId();
            BigDecimal available = availableStock.get(materialId);
            BigDecimal required = composition.getQuantityRequired();
            
            if (required.compareTo(BigDecimal.ZERO) > 0) {
                int possibleQuantity = available.divide(required, 0, RoundingMode.DOWN).intValue();
                maxQuantity = Math.min(maxQuantity, possibleQuantity);
            } else {
                maxQuantity = 0;
                break;
            }
        }
        
        return maxQuantity == Integer.MAX_VALUE ? 0 : maxQuantity;
    }
}

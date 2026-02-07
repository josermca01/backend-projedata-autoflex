package com.inventory.repository;

import com.inventory.model.ProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCompositionRepository extends JpaRepository<ProductComposition, Long> {
    
    List<ProductComposition> findByProductId(Long productId);
    
    void deleteByProductId(Long productId);
    
    @Query("SELECT pc FROM ProductComposition pc JOIN FETCH pc.rawMaterial WHERE pc.product.id = :productId")
    List<ProductComposition> findByProductIdWithRawMaterial(Long productId);
}

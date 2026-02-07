package com.inventory.repository;

import com.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.compositions c LEFT JOIN FETCH c.rawMaterial WHERE p.id = :id")
    Optional<Product> findByIdWithCompositions(Long id);
    
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.compositions ORDER BY p.value DESC")
    List<Product> findAllOrderByValueDesc();
}

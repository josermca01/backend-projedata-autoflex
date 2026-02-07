package com.inventory.service;

import com.inventory.dto.CompositionDTO;
import com.inventory.dto.ProductDTO;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Product;
import com.inventory.model.ProductComposition;
import com.inventory.model.RawMaterial;
import com.inventory.repository.ProductCompositionRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductCompositionRepository compositionRepository;
    private final RawMaterialRepository rawMaterialRepository;
    
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findByIdWithCompositions(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }
    
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setValue(productDTO.getValue());
        
        Product savedProduct = productRepository.save(product);
        if (productDTO.getCompositions() != null && !productDTO.getCompositions().isEmpty()) {
            product.setCompositions(updateCompositions(product, productDTO.getCompositions()));
        }
        
        
        
        return convertToDTO(productRepository.findByIdWithCompositions(savedProduct.getId()).get());
    }
    
    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        product.setName(productDTO.getName());
        product.setValue(productDTO.getValue());
        
        productRepository.save(product);
        
        if (productDTO.getCompositions() != null) {
            compositionRepository.deleteByProductId(id);
            updateCompositions(product, productDTO.getCompositions());
        }
        
        return convertToDTO(productRepository.findByIdWithCompositions(id).get());
    }
    
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    private List<ProductComposition> updateCompositions(Product product, List<CompositionDTO> compositionDTOs) {
        List<ProductComposition> compositionList = new ArrayList<>();
        for (CompositionDTO dto : compositionDTOs) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + dto.getRawMaterialId()));
            
            ProductComposition composition = new ProductComposition();
            composition.setProduct(product);
            composition.setRawMaterial(rawMaterial);
            composition.setQuantityRequired(dto.getQuantityRequired());
            compositionList.add(composition);
            compositionRepository.save(composition);
        }
        return compositionList;
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setValue(product.getValue());
        
        if (product.getCompositions() != null && !product.getCompositions().isEmpty()) {
            List<CompositionDTO> compositions = product.getCompositions().stream()
                    .map(this::convertCompositionToDTO)
                    .collect(Collectors.toList());
            dto.setCompositions(compositions);
        }
        
        return dto;
    }
    
    private CompositionDTO convertCompositionToDTO(ProductComposition composition) {
        CompositionDTO dto = new CompositionDTO();
        dto.setId(composition.getId());
        dto.setProductId(composition.getProduct().getId());
        dto.setRawMaterialId(composition.getRawMaterial().getId());
        dto.setRawMaterialName(composition.getRawMaterial().getName());
        dto.setQuantityRequired(composition.getQuantityRequired());
        return dto;
    }
}

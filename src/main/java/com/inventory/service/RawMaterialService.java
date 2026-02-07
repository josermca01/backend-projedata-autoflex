package com.inventory.service;

import com.inventory.dto.RawMaterialDTO;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.RawMaterial;
import com.inventory.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialService {
    
    private final RawMaterialRepository rawMaterialRepository;
    
    @Transactional(readOnly = true)
    public List<RawMaterialDTO> findAll() {
        return rawMaterialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RawMaterialDTO findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
        return convertToDTO(rawMaterial);
    }
    
    @Transactional
    public RawMaterialDTO create(RawMaterialDTO rawMaterialDTO) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName(rawMaterialDTO.getName());
        rawMaterial.setStockQuantity(rawMaterialDTO.getStockQuantity());
        
        RawMaterial savedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return convertToDTO(savedRawMaterial);
    }
    
    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO rawMaterialDTO) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw material not found with id: " + id));
        
        rawMaterial.setName(rawMaterialDTO.getName());
        rawMaterial.setStockQuantity(rawMaterialDTO.getStockQuantity());
        
        RawMaterial updatedRawMaterial = rawMaterialRepository.save(rawMaterial);
        return convertToDTO(updatedRawMaterial);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!rawMaterialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Raw material not found with id: " + id);
        }
        rawMaterialRepository.deleteById(id);
    }
    
    private RawMaterialDTO convertToDTO(RawMaterial rawMaterial) {
        RawMaterialDTO dto = new RawMaterialDTO();
        dto.setId(rawMaterial.getId());
        dto.setName(rawMaterial.getName());
        dto.setStockQuantity(rawMaterial.getStockQuantity());
        return dto;
    }
}

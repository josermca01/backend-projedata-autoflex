package com.inventory.controller;

import com.inventory.dto.RawMaterialDTO;
import com.inventory.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RawMaterialController {
    
    private final RawMaterialService rawMaterialService;
    
    @GetMapping
    public ResponseEntity<List<RawMaterialDTO>> getAllRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> getRawMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<RawMaterialDTO> createRawMaterial(@Valid @RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.create(rawMaterialDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> updateRawMaterial(
            @PathVariable Long id,
            @Valid @RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.ok(rawMaterialService.update(id, rawMaterialDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

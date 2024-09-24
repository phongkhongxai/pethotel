package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.services.PetTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet-types")
public class PetTypeController {
    @Autowired
    private PetTypeService petTypeService;

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPetType(@Valid @RequestBody PetTypeDto petTypeDto) {
        PetTypeDto pt = petTypeService.savePetType(petTypeDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPetType(){
        List<PetTypeDto> petTypeDtos = petTypeService.getAllPetTypes();
        return new ResponseEntity<>(petTypeDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetTypeById(@PathVariable("id") Long id) {
        PetTypeDto petTypeDto = petTypeService.getPetTypeById(id);
        return new ResponseEntity<>(petTypeDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePetType(@PathVariable("id") Long id, @Valid @RequestBody PetTypeDto petTypeDto) {
        PetTypeDto bt1 = petTypeService.updatePetType(id, petTypeDto);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePetType(@PathVariable("id") Long id) {
        String msg = petTypeService.deletePetType(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }
}

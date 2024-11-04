package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.entity.ImageFile;
import com.bumble.pethotel.models.entity.Pet;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.services.PetService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
    @Autowired
    private PetService petService;
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPet(@Valid @ModelAttribute PetDto petDto) {
        PetDto pt = petService.savePet(petDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @GetMapping
    public PetsResponese getAllPets(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return petService.getAllPet(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/users/{userId}")
    public PetsResponese getPetsByUser(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                       @PathVariable("userId") Long userId){
        return petService.getPetByUserId(userId,pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable("id") Long id) {
        PetDto petDto = petService.getPetById(id);
        return new ResponseEntity<>(petDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePet(@PathVariable("id") Long id, @Valid @ModelAttribute PetUpdated petUpdated) {
        PetDto bt1 = petService.updatePet(id, petUpdated);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/images/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id, @RequestParam("files") List<MultipartFile> files) {
        String msg = petService.uploadImagePet(id, files);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<?> getImageShop(@PathVariable("id") Long id) {
        Set<ImageFile> images = petService.getImagePet(id);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        String msg = petService.deletePet(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}

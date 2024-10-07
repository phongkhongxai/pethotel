package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.CareServiceDto;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.requestModel.CareServiceUpdated;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.responseModel.CareServicesResponse;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.services.CareServicesService;
import com.bumble.pethotel.services.PetService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
public class CareServicesController {
    @Autowired
    private CareServicesService careServicesService;

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody CareServiceDto careServiceDto) {
        CareServiceDto pt = careServicesService.saveCareService(careServiceDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @GetMapping
    public CareServicesResponse getAllServices(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                           @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return careServicesService.getAllCareService(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/shops/{shopId}")
    public CareServicesResponse getServicesOfShop(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                       @PathVariable("shopId") Long shopId){
        return careServicesService.getCareServiceByShop(shopId,pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/shops/health/{shopId}/")
    public CareServicesResponse getServiceByShopAndTypeHealth(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                                              @PathVariable("shopId") Long shopId){
        return careServicesService.getCareServiceByShopAndType(shopId, "health",pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/shops/spa/{shopId}/")
    public CareServicesResponse getServiceByShopAndTypeSpa(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                                              @PathVariable("shopId") Long shopId){
        return careServicesService.getCareServiceByShopAndType(shopId, "spa",pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") Long id) {
        CareServiceDto careServiceDto = careServicesService.getCareServiceById(id);
        return new ResponseEntity<>(careServiceDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable("id") Long id, @Valid @RequestBody CareServiceUpdated careServiceUpdated) {
        CareServiceDto bt1 = careServicesService.updateCareService(id, careServiceUpdated);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") Long id) {
        String msg = careServicesService.deleteCareService(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}

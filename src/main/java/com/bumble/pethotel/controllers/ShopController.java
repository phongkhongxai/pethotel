package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ShopDto;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.requestModel.ShopUpdated;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ShopsResponse;
import com.bumble.pethotel.services.PetService;
import com.bumble.pethotel.services.ShopService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admins")
    public ResponseEntity<?> createShop(@Valid @RequestBody ShopDto shopDto) {
        ShopDto pt = shopService.saveShop(shopDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping
    public ResponseEntity<?> createShopByOwner(@Valid @RequestBody ShopDto shopDto) {
        ShopDto pt = shopService.createShop(shopDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/verify/{id}")
    public ResponseEntity<?> verifyShop(@PathVariable("id") Long id) {
        String msg = shopService.verifyShop(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/not-verify")
    public ShopsResponse getAllShopsNotVerified(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return shopService.getAllShopNotVerify(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping
    public ShopsResponse getAllShops(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return shopService.getAllShop(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/users/{userId}")
    public ShopsResponse getShopsByUser(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                       @PathVariable("userId") Long userId){
        return shopService.getShopByUserId(userId,pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopById(@PathVariable("id") Long id) {
        ShopDto shopDto = shopService.getShopById(id);
        return new ResponseEntity<>(shopDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShop(@PathVariable("id") Long id, @Valid @RequestBody ShopUpdated shopUpdated) {
        ShopDto bt1 = shopService.updateShop(id, shopUpdated);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable("id") Long id) {
        String msg = shopService.deleteShop(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}

package com.bumble.pethotel.controllers;

import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ReviewDto;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ReviewsResponse;
import com.bumble.pethotel.services.PetService;
import com.bumble.pethotel.services.ReviewService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto pt = reviewService.saveReview(reviewDto);
        return new ResponseEntity<>(pt, HttpStatus.CREATED);
    }
    @GetMapping
    public ReviewsResponse getAllReviews(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return reviewService.getAllReviews(pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/shops/{shopId}")
    public ReviewsResponse getReviewsByShop(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                       @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
                                       @PathVariable("shopId") Long shopId){
        return reviewService.getReviewsByShop(shopId,pageNo, pageSize, sortBy, sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable("id") Long id) {
        ReviewDto reviewDto = reviewService.getReviewById(id);
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }
}

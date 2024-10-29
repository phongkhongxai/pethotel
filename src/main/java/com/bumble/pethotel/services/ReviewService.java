package com.bumble.pethotel.services;

import com.bumble.pethotel.models.entity.ImageFile;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ReviewDto;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.requestModel.ReviewUpdateRequest;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ReviewsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ReviewService {
    ReviewDto saveReview(ReviewDto reviewDto);
    ReviewDto getReviewById(Long id);
    ReviewsResponse getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewsResponse getReviewsByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir);
    //ReviewDto updateReview(Long id, ReviewUpdateRequest reviewUpdateRequest);
    //String uploadImagePet(Long id, List<MultipartFile> files);
    //Set<ImageFile> getImagePet(Long id);

    //String deletePet(Long id);
}

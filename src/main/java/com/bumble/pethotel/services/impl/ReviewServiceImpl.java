package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ReviewDto;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ReviewsResponse;
import com.bumble.pethotel.repositories.*;
import com.bumble.pethotel.services.CloudinaryService;
import com.bumble.pethotel.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Override
    public ReviewDto saveReview(ReviewDto reviewDto) {
        Shop shop = shopRepository.findById(reviewDto.getShopId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Shop not found."));
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Shop not found."));
        Optional<Booking> booking = bookingRepository.findLatestByUserAndShop(user, shop.getId());
        if (booking.isEmpty()){
            throw new PetApiException(HttpStatus.BAD_REQUEST, "User do not have any booking of shop, so this active be denied.");
        }
        Optional<Review> review = reviewRepository.findByUserAndShopAndIsDeleteFalse(user, shop);
        if (review.isEmpty()){
            throw new PetApiException(HttpStatus.BAD_REQUEST, "This user have already reviewed this shop.");
        }
        Review review1 = modelMapper.map(reviewDto, Review.class);
        return modelMapper.map(reviewRepository.save(review1), ReviewDto.class);
    }

    @Override
    public ReviewDto getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Review not found with id: "+ id);

        }
        return modelMapper.map(review.get(),ReviewDto.class);
    }

    @Override
    public ReviewsResponse getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Review> reviews = reviewRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Review> listOfReviews = reviews.getContent();

        List<ReviewDto> content = listOfReviews.stream().map(bt -> modelMapper.map(bt, ReviewDto.class)).collect(Collectors.toList());

        ReviewsResponse templatesResponse = new ReviewsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(reviews.getNumber());
        templatesResponse.setPageSize(reviews.getSize());
        templatesResponse.setTotalElements(reviews.getTotalElements());
        templatesResponse.setTotalPages(reviews.getTotalPages());
        templatesResponse.setLast(reviews.isLast());

        return templatesResponse;
    }

    @Override
    public ReviewsResponse getReviewsByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"Shop not found."));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Review> reviews = reviewRepository.findByShopAndIsDeleteFalse(shop,pageable);

        // get content for page object
        List<Review> listOfReviews = reviews.getContent();

        List<ReviewDto> content = listOfReviews.stream().map(bt -> modelMapper.map(bt, ReviewDto.class)).collect(Collectors.toList());

        ReviewsResponse templatesResponse = new ReviewsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(reviews.getNumber());
        templatesResponse.setPageSize(reviews.getSize());
        templatesResponse.setTotalElements(reviews.getTotalElements());
        templatesResponse.setTotalPages(reviews.getTotalPages());
        templatesResponse.setLast(reviews.isLast());

        return templatesResponse;
    }
}

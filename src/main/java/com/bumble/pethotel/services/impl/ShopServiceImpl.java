package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.ImageFile;
import com.bumble.pethotel.models.entity.Pet;
import com.bumble.pethotel.models.entity.Shop;
import com.bumble.pethotel.models.entity.User;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.ImageFileDto;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ShopDto;
import com.bumble.pethotel.models.payload.requestModel.ShopUpdated;
import com.bumble.pethotel.models.payload.responseModel.ShopsResponse;
import com.bumble.pethotel.repositories.ShopRepository;
import com.bumble.pethotel.repositories.UserRepository;
import com.bumble.pethotel.services.CloudinaryService;
import com.bumble.pethotel.services.ShopService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailVerificationService emailVerificationService;
    @Override
    public ShopDto saveShop(ShopDto shopDto) {
        Optional<User> user = userRepository.findById(shopDto.getUserId());
        if (user.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "User not found with id: "+ shopDto.getUserId());
        }
        Shop shop = modelMapper.map(shopDto, Shop.class);
        shop.setShopVerified(true);
        shop.setDelete(false);
        return modelMapper.map(shopRepository.save(shop), ShopDto.class);
    }

    @Override
    public ShopDto createShop(ShopDto shopDto) {
        Optional<User> user = userRepository.findById(shopDto.getUserId());
        if (user.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "User not found with id: "+ shopDto.getUserId());
        }
        Shop shop = modelMapper.map(shopDto, Shop.class);
        shop.setShopVerified(false);
        shop.setDelete(false);
        return modelMapper.map(shopRepository.save(shop), ShopDto.class);
    }

    @Override
    public ShopDto getShopById(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if(shopOptional.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: "+ id);

        }
        Shop shop = shopOptional.get();

        // Chuyển đổi Shop thành ShopDto
        ShopDto shopDto = modelMapper.map(shop, ShopDto.class);

        // Chuyển đổi Set<ImageFile> thành Set<ImageFileDto>
        Set<ImageFileDto> imageFileDtos = shop.getImageFile().stream()
                .map(imageFile -> new ImageFileDto(
                        imageFile.getId(),
                        imageFile.getUrl(),
                        imageFile.getCreatedAt().toString() // Hoặc định dạng ngày tháng khác nếu cần
                ))
                .collect(Collectors.toSet());

        // Gán imageFileDtos vào shopDto
        shopDto.setImageFiles(imageFileDtos);

        return shopDto;
    }

    @Override
    public ShopsResponse getAllShop(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Shop> shops = shopRepository.findAllNotDeletedAndVerified(pageable);

        // get content for page object
        List<Shop> listOfShops = shops.getContent();

        List<ShopDto> content = listOfShops.stream().map(shop -> {
            ShopDto shopDto = modelMapper.map(shop, ShopDto.class);

            // Chuyển đổi Set<ImageFile> sang Set<ImageFileDto>
            Set<ImageFileDto> imageFileDtos = shop.getImageFile().stream()
                    .map(imageFile -> new ImageFileDto(
                            imageFile.getId(),
                            imageFile.getUrl(),
                            imageFile.getCreatedAt().toString()  // Hoặc định dạng khác nếu cần
                    ))
                    .collect(Collectors.toSet());

            // Gán imageFileDtos vào shopDto
            shopDto.setImageFiles(imageFileDtos);
            return shopDto;
        }).collect(Collectors.toList());
        ShopsResponse templatesResponse = new ShopsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(shops.getNumber());
        templatesResponse.setPageSize(shops.getSize());
        templatesResponse.setTotalElements(shops.getTotalElements());
        templatesResponse.setTotalPages(shops.getTotalPages());
        templatesResponse.setLast(shops.isLast());

        return templatesResponse;
    }

    @Override
    public ShopsResponse getAllShopNotVerify(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Shop> shops = shopRepository.findAllNotDeletedAndNotVerified(pageable);

        // get content for page object
        List<Shop> listOfShops = shops.getContent();

        List<ShopDto> content = listOfShops.stream().map(bt -> modelMapper.map(bt, ShopDto.class)).collect(Collectors.toList());

        ShopsResponse templatesResponse = new ShopsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(shops.getNumber());
        templatesResponse.setPageSize(shops.getSize());
        templatesResponse.setTotalElements(shops.getTotalElements());
        templatesResponse.setTotalPages(shops.getTotalPages());
        templatesResponse.setLast(shops.isLast());

        return templatesResponse;
    }

    @Override
    @Transactional
    public String verifyShop(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: " + id);
        }
        if (shopOptional.get().isShopVerified()) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Shop have been verified.");
        }
        Shop shop = shopOptional.get();
        shop.setShopVerified(true);
        emailVerificationService.sendEmailNotify(shop);
        shopRepository.save(shop);
        return "Verified successfully";
    }

    @Override
    public ShopsResponse getShopByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"User not found with id: "+ userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Shop> shops = shopRepository.findByUserAndIsDeleteFalseAndShopVerifiedTrue(user,pageable);

        // get content for page object
        List<Shop> listOfShops = shops.getContent();

        List<ShopDto> content = listOfShops.stream().map(shop -> {
            ShopDto shopDto = modelMapper.map(shop, ShopDto.class);

            // Chuyển đổi Set<ImageFile> sang Set<ImageFileDto>
            Set<ImageFileDto> imageFileDtos = shop.getImageFile().stream()
                    .map(imageFile -> new ImageFileDto(
                            imageFile.getId(),
                            imageFile.getUrl(),
                            imageFile.getCreatedAt().toString()  // Hoặc định dạng khác nếu cần
                    ))
                    .collect(Collectors.toSet());

            shopDto.setImageFiles(imageFileDtos);
            return shopDto;
        }).collect(Collectors.toList());
        ShopsResponse templatesResponse = new ShopsResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(shops.getNumber());
        templatesResponse.setPageSize(shops.getSize());
        templatesResponse.setTotalElements(shops.getTotalElements());
        templatesResponse.setTotalPages(shops.getTotalPages());
        templatesResponse.setLast(shops.isLast());

        return templatesResponse;
    }

    @Override
    public ShopDto updateShop(Long id, ShopUpdated shopUpdated) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: " + id);
        }

        // Retrieve the shop entity
        Shop shop = shopOptional.get();

        // Update fields if present in the shopUpdated object
        shop.setName(shopUpdated.getName() != null ? shopUpdated.getName() : shop.getName());
        shop.setAddress(shopUpdated.getAddress() != null ? shopUpdated.getAddress() : shop.getAddress());
        shop.setPhone(shopUpdated.getPhone() != null ? shopUpdated.getPhone() : shop.getPhone());
        shop.setDescription(shopUpdated.getDescription() != null ? shopUpdated.getDescription() : shop.getDescription());
        shop.setBankName(shopUpdated.getBankName() != null ? shopUpdated.getBankName() : shop.getBankName());
        shop.setAccountNumber(shopUpdated.getAccountNumber() != null ? shopUpdated.getAccountNumber() : shop.getAccountNumber());
        if (shopUpdated.getFiles() != null && !shopUpdated.getFiles().isEmpty()) {
            List<String> uploadedUrls = cloudinaryService.uploadFiles(shopUpdated.getFiles(), "shops/" + id);

            // Save the image URLs to the shop entity
            Set<ImageFile> imageFiles = new HashSet<>();
            for (String url : uploadedUrls) {
                if (!"default".equals(url)) {  // Skip default URLs if any
                    ImageFile imageFile = ImageFile.builder()
                            .url(url)
                            .shop(shop)
                            .createdAt(LocalDateTime.now())
                            .build();
                    imageFiles.add(imageFile);
                }
            }

            // Add new images to the existing set of images for the shop
            shop.getImageFile().addAll(imageFiles);
        }
        Shop updatedShop = shopRepository.save(shop);
        return modelMapper.map(updatedShop, ShopDto.class);
    }

    @Override
    public String deleteShop(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: " + id);
        }

        // Retrieve the shop entity
        Shop shop = shopOptional.get();
        shop.setDelete(true);
        shopRepository.save(shop);
        return "Deleted successfully";
    }

    @Override
    public String uploadImageShop(Long id, List<MultipartFile> files) {
        // Check if the shop exists
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: " + id);
        }
        Shop shop = shopOptional.get();

        // Upload the files to Cloudinary and get the URLs
        List<String> uploadedUrls = cloudinaryService.uploadFiles(files, "shops/" + id);

        // Save the image URLs to the shop entity
        Set<ImageFile> imageFiles = new HashSet<>();
        for (String url : uploadedUrls) {
            if (!"default".equals(url)) {
                ImageFile imageFile = ImageFile.builder()
                        .url(url)
                        .shop(shop)
                        .createdAt(LocalDateTime.now())
                        .build();
                imageFiles.add(imageFile);
            }
        }

        shop.getImageFile().addAll(imageFiles);
        shopRepository.save(shop);

        return "Successfully uploaded " + uploadedUrls.size() + " image(s) for shop with id: " + id;
    }

    @Override
    public Set<ImageFile> getImageShop(Long id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);
        if (shopOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: " + id);
        }
        Shop shop = shopOptional.get();
        return shop.getImageFile();
    }

}

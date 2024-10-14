package com.bumble.pethotel.services;

import com.bumble.pethotel.models.entity.ImageFile;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ShopDto;
import com.bumble.pethotel.models.payload.requestModel.ShopUpdated;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ShopsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ShopService {
    ShopDto saveShop(ShopDto shopDto);
    ShopDto createShop(ShopDto shopDto);
    ShopDto getShopById(Long id);
    ShopsResponse getAllShop(int pageNo, int pageSize, String sortBy, String sortDir);
    ShopsResponse getAllShopNotVerify(int pageNo, int pageSize, String sortBy, String sortDir);
    String verifyShop(Long id);

    ShopsResponse getShopByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    ShopDto updateShop(Long id, ShopUpdated shopUpdated);
    String deleteShop(Long id);
    String uploadImageShop(Long id, List<MultipartFile> files);
    Set<ImageFile> getImageShop(Long id);

}

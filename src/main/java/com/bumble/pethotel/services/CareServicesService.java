package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.CareServiceDto;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.requestModel.CareServiceUpdated;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.responseModel.CareServicesResponse;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;

public interface CareServicesService {
    CareServiceDto saveCareService(CareServiceDto petDto);
    CareServiceDto getCareServiceById(Long id);
    CareServicesResponse getAllCareService(int pageNo, int pageSize, String sortBy, String sortDir);
    CareServicesResponse getCareServiceByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir);
    CareServicesResponse getCareServiceByShopAndType(Long shopId,String type, int pageNo, int pageSize, String sortBy, String sortDir);

    CareServiceDto updateCareService(Long id, CareServiceUpdated careServiceUpdated);
    String deleteCareService(Long id);
}

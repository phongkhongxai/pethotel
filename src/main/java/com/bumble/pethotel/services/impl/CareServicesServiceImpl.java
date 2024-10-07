package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.CareServiceDto;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.requestModel.CareServiceUpdated;
import com.bumble.pethotel.models.payload.responseModel.CareServicesResponse;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.repositories.CareServiceRepository;
import com.bumble.pethotel.repositories.ShopRepository;
import com.bumble.pethotel.services.CareServicesService;
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
public class CareServicesServiceImpl implements CareServicesService {
    @Autowired
    private CareServiceRepository careServiceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShopRepository shopRepository;
    @Override
    public CareServiceDto saveCareService(CareServiceDto careServiceDto) {
        Optional<Shop> shop = shopRepository.findById(careServiceDto.getShopId());
        if (shop.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: "+ careServiceDto.getShopId());
        }
        CareService careService = modelMapper.map(careServiceDto, CareService.class);
        careService.setDelete(false);
        return modelMapper.map(careServiceRepository.save(careService), CareServiceDto.class);
    }

    @Override
    public CareServiceDto getCareServiceById(Long id) {
        Optional<CareService> careService = careServiceRepository.findById(id);
        if(careService.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Care Service not found with id: "+ id);

        }
        return modelMapper.map(careService.get(),CareServiceDto.class);
    }

    @Override
    public CareServicesResponse getAllCareService(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CareService> careServices = careServiceRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<CareService> listOfServices = careServices.getContent();

        List<CareServiceDto> content = listOfServices.stream().map(bt -> modelMapper.map(bt, CareServiceDto.class)).collect(Collectors.toList());

        CareServicesResponse templatesResponse = new CareServicesResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(careServices.getNumber());
        templatesResponse.setPageSize(careServices.getSize());
        templatesResponse.setTotalElements(careServices.getTotalElements());
        templatesResponse.setTotalPages(careServices.getTotalPages());
        templatesResponse.setLast(careServices.isLast());

        return templatesResponse;
    }

    @Override
    public CareServicesResponse getCareServiceByShop(Long shopId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: "+ shopId);
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CareService> careServices = careServiceRepository.findByShopAndIsDeleteFalse(shop.get(),pageable);

        // get content for page object
        List<CareService> listOfServices = careServices.getContent();

        List<CareServiceDto> content = listOfServices.stream().map(bt -> modelMapper.map(bt, CareServiceDto.class)).collect(Collectors.toList());

        CareServicesResponse templatesResponse = new CareServicesResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(careServices.getNumber());
        templatesResponse.setPageSize(careServices.getSize());
        templatesResponse.setTotalElements(careServices.getTotalElements());
        templatesResponse.setTotalPages(careServices.getTotalPages());
        templatesResponse.setLast(careServices.isLast());

        return templatesResponse;
    }

    @Override
    public CareServicesResponse getCareServiceByShopAndType(Long shopId, String type, int pageNo, int pageSize, String sortBy, String sortDir) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Shop not found with id: "+ shopId);
        }
        if (!type.equalsIgnoreCase("spa") && !type.equalsIgnoreCase("health")) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Invalid care service type. Only 'spa' and 'health' are allowed.");
        }
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CareService> careServices = careServiceRepository.findByShopAndTypeAndIsDeleteFalse(shop.get(),type,pageable);

        // get content for page object
        List<CareService> listOfServices = careServices.getContent();

        List<CareServiceDto> content = listOfServices.stream().map(bt -> modelMapper.map(bt, CareServiceDto.class)).collect(Collectors.toList());

        CareServicesResponse templatesResponse = new CareServicesResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(careServices.getNumber());
        templatesResponse.setPageSize(careServices.getSize());
        templatesResponse.setTotalElements(careServices.getTotalElements());
        templatesResponse.setTotalPages(careServices.getTotalPages());
        templatesResponse.setLast(careServices.isLast());

        return templatesResponse;
    }

    @Override
    public CareServiceDto updateCareService(Long id, CareServiceUpdated careServiceUpdated) {
        Optional<CareService> careServiceOptional = careServiceRepository.findById(id);
        if (careServiceOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Care Service not found with id: " + id);
        }
        CareService careService = careServiceOptional.get();
        careService.setName(careServiceUpdated.getName() != null ? careServiceUpdated.getName() : careService.getName());
        careService.setPrice(careServiceUpdated.getPrice() > 0 ? careServiceUpdated.getPrice() : careService.getPrice());
        careService.setDescription(careServiceUpdated.getDescription() != null ? careServiceUpdated.getDescription() : careService.getDescription());
        careService.setStatus(careServiceUpdated.getStatus() != null ? careServiceUpdated.getStatus() : careService.getStatus());
        CareService updatedCareService = careServiceRepository.save(careService);
        return modelMapper.map(updatedCareService, CareServiceDto.class);
    }

    @Override
    public String deleteCareService(Long id) {
        Optional<CareService> careService = careServiceRepository.findById(id);
        if (careService.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Care Service not found with id: " + id);
        }
        CareService careService1 = careService.get();
        careService1.setDelete(true);
        careServiceRepository.save(careService1);
        return "Deleted successfully";
    }
}

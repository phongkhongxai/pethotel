package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;

import java.util.List;

public interface PetService {
    PetDto savePet(PetDto petDto);
    PetDto getPetById(Long id);
    PetsResponese getAllPet(int pageNo, int pageSize, String sortBy, String sortDir);
    PetsResponese getPetByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    PetDto updatePet(Long id, PetUpdated petUpdated);
    String deletePet(Long id);
}

package com.bumble.pethotel.services;

import com.bumble.pethotel.models.payload.dto.PetTypeDto;

import java.util.List;

public interface PetTypeService {
    PetTypeDto savePetType(PetTypeDto petTypeDto);
    PetTypeDto getPetTypeById(Long id);
    List<PetTypeDto> getAllPetTypes();
    PetTypeDto updatePetType(Long id, PetTypeDto petTypeDto);
    String deletePetType(Long id);

}

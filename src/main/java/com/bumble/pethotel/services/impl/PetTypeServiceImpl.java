package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.PetType;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.PetTypeDto;
import com.bumble.pethotel.repositories.PetTypeRepository;
import com.bumble.pethotel.services.PetTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetTypeServiceImpl implements PetTypeService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PetTypeRepository petTypeRepository;
    @Override
    public PetTypeDto savePetType(PetTypeDto petTypeDto) {
        PetType petType =modelMapper.map(petTypeDto, PetType.class);
        petType.setDelete(false);
        return modelMapper.map(petTypeRepository.save(petType), PetTypeDto.class);
    }

    @Override
    public PetTypeDto getPetTypeById(Long id) {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if(petType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Pet Type not found with ID: "+id);
        }
        return modelMapper.map(petType.get(),PetTypeDto.class);
    }

    @Override
    public List<PetTypeDto> getAllPetTypes() {
        return petTypeRepository.findAllNotDeleted().stream().map(bt -> modelMapper.map(bt, PetTypeDto.class)).toList();
    }

    @Override
    public PetTypeDto updatePetType(Long id, PetTypeDto petTypeDto) {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if(petType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Pet Type not found with ID: "+id);
        }
        PetType existingPetType = petType.get();
        existingPetType.setName(petTypeDto.getName() != null ? petTypeDto.getName() : existingPetType.getName());
        PetType updatedPetType = petTypeRepository.save(existingPetType);
        return modelMapper.map(updatedPetType, PetTypeDto.class);
    }

    @Override
    public String deletePetType(Long id) {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if(petType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND,"Pet Type not found with ID: "+id);
        }
        PetType existingPetType = petType.get();
        existingPetType.setDelete(true);
        petTypeRepository.save(existingPetType);
        return "Deleted successfully.";
    }
}

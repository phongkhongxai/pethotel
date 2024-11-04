package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.*;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.ImageFileDto;
import com.bumble.pethotel.models.payload.dto.PetDto;
import com.bumble.pethotel.models.payload.dto.ShopDto;
import com.bumble.pethotel.models.payload.requestModel.PetUpdated;
import com.bumble.pethotel.models.payload.responseModel.PetsResponese;
import com.bumble.pethotel.models.payload.responseModel.ShopsResponse;
import com.bumble.pethotel.repositories.PetRepository;
import com.bumble.pethotel.repositories.PetTypeRepository;
import com.bumble.pethotel.repositories.UserRepository;
import com.bumble.pethotel.services.CloudinaryService;
import com.bumble.pethotel.services.PetService;
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
public class PetServiceImpl implements PetService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public PetDto savePet(PetDto petDto) {
        Optional<PetType> petType = petTypeRepository.findById(petDto.getPetTypeId());
        Optional<User> user = userRepository.findById(petDto.getUserId());
        if (petType.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet Type not found with id: "+ petDto.getPetTypeId());
        }
        if (user.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "User not found with id: "+ petDto.getUserId());
        }
        Pet pet0 = modelMapper.map(petDto, Pet.class);
        pet0.setDelete(false);
        Pet pet = petRepository.save(pet0);
        if (petDto.getFiles() != null && !petDto.getFiles().isEmpty()) {
            List<String> uploadedUrls = cloudinaryService.uploadFiles(petDto.getFiles(), "pets/" + pet.getId());

            // Save the image URLs to the shop entity
            Set<ImageFile> imageFiles = new HashSet<>();
            for (String url : uploadedUrls) {
                if (!"default".equals(url)) {
                    ImageFile imageFile = ImageFile.builder()
                            .url(url)
                            .pet(pet)
                            .createdAt(LocalDateTime.now())
                            .build();
                    imageFiles.add(imageFile);
                }
            }
            if (pet.getImageFile() == null) {
                pet.setImageFile(new HashSet<>());
            }

            pet.getImageFile().addAll(imageFiles);
        }
        return modelMapper.map(petRepository.save(pet), PetDto.class);
    }

    @Override
    public PetDto getPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if(pet.isEmpty()){
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet not found with id: "+ id);

        }
        PetDto petDto = modelMapper.map(pet.get(), PetDto.class);

        // Chuyển đổi Set<ImageFile> thành Set<ImageFileDto>
        Set<ImageFileDto> imageFileDtos = pet.get().getImageFile().stream()
                .map(imageFile -> new ImageFileDto(
                        imageFile.getId(),
                        imageFile.getUrl(),
                        imageFile.getCreatedAt().toString() // Hoặc định dạng ngày tháng khác nếu cần
                ))
                .collect(Collectors.toSet());

        // Gán imageFileDtos vào shopDto
        petDto.setImageFile(imageFileDtos);
        return modelMapper.map(pet.get(),PetDto.class);
    }

    @Override
    public PetsResponese getAllPet(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Pet> pets = petRepository.findAllNotDeleted(pageable);

        // get content for page object
        List<Pet> listOfPets = pets.getContent();

        List<PetDto> content = listOfPets.stream().map(bt -> modelMapper.map(bt, PetDto.class)).collect(Collectors.toList());

        PetsResponese templatesResponse = new PetsResponese();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(pets.getNumber());
        templatesResponse.setPageSize(pets.getSize());
        templatesResponse.setTotalElements(pets.getTotalElements());
        templatesResponse.setTotalPages(pets.getTotalPages());
        templatesResponse.setLast(pets.isLast());

        return templatesResponse;

    }

    @Override
    public PetsResponese getPetByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND,"User not found with id: "+ userId));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Pet> pets = petRepository.findByUserAndIsDeleteFalse(user,pageable);

        // get content for page object
        List<Pet> listOfPets = pets.getContent();

        List<PetDto> content = listOfPets.stream().map(bt -> modelMapper.map(bt, PetDto.class)).collect(Collectors.toList());

        PetsResponese templatesResponse = new PetsResponese();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(pets.getNumber());
        templatesResponse.setPageSize(pets.getSize());
        templatesResponse.setTotalElements(pets.getTotalElements());
        templatesResponse.setTotalPages(pets.getTotalPages());
        templatesResponse.setLast(pets.isLast());

        return templatesResponse;
    }

    @Override
    public PetDto updatePet(Long id, PetUpdated petUpdated) {
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet not found with id: " + id);
        }

        Pet pet = petOptional.get();
        pet.setName(petUpdated.getName() != null ? petUpdated.getName() : pet.getName());
        pet.setAge(petUpdated.getAge() > 0 ? petUpdated.getAge() : pet.getAge());
        pet.setBreed(petUpdated.getBreed() != null ? petUpdated.getBreed() : pet.getBreed());
        pet.setColor(petUpdated.getColor() != null ? petUpdated.getColor() : pet.getColor());
        pet.setWeight(petUpdated.getWeight() > 0 ? petUpdated.getWeight() : pet.getWeight());
        pet.setGender(petUpdated.getGender() != null ? petUpdated.getGender() : pet.getGender());
        if (petUpdated.getPetTypeId() != null) {
            PetType petType = petTypeRepository.findById(petUpdated.getPetTypeId())
                    .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "PetType not found with id: " + petUpdated.getPetTypeId()));
            pet.setPetType(petType);
        }
        if (petUpdated.getFiles() != null && !petUpdated.getFiles().isEmpty()) {
            List<String> uploadedUrls = cloudinaryService.uploadFiles(petUpdated.getFiles(), "pets/" + id);

            // Save the image URLs to the shop entity
            Set<ImageFile> imageFiles = new HashSet<>();
            for (String url : uploadedUrls) {
                if (!"default".equals(url)) {
                    ImageFile imageFile = ImageFile.builder()
                            .url(url)
                            .pet(pet)
                            .createdAt(LocalDateTime.now())
                            .build();
                    imageFiles.add(imageFile);
                }
            }

            pet.getImageFile().addAll(imageFiles);
        }
        Pet updatedPet = petRepository.save(pet);
        return modelMapper.map(updatedPet, PetDto.class);
    }

    @Override
    public String uploadImagePet(Long id, List<MultipartFile> files) {
        // Check if the shop exists
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet not found with id: " + id);
        }
        Pet pet1 = pet.get();

        List<String> uploadedUrls = cloudinaryService.uploadFiles(files, "pets/" + id);

        // Save the image URLs to the shop entity
        Set<ImageFile> imageFiles = new HashSet<>();
        for (String url : uploadedUrls) {
            if (!"default".equals(url)) {
                ImageFile imageFile = ImageFile.builder()
                        .url(url)
                        .pet(pet1)
                        .createdAt(LocalDateTime.now())
                        .build();
                imageFiles.add(imageFile);
            }
        }

        pet1.getImageFile().addAll(imageFiles);
        petRepository.save(pet1);

        return "Successfully uploaded " + uploadedUrls.size() + " image(s) for pet with id: " + id;
    }

    @Override
    public Set<ImageFile> getImagePet(Long id) {
        Optional<Pet> pet1 = petRepository.findById(id);
        if (pet1.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet not found with id: " + id);
        }
        Pet pet = pet1.get();
        return pet.getImageFile();
    }

    @Override
    public String deletePet(Long id) {
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isEmpty()) {
            throw new PetApiException(HttpStatus.NOT_FOUND, "Pet not found with id: " + id);
        }
        Pet pet = petOptional.get();
        pet.setDelete(true);
        petRepository.save(pet);
        return "Deleted successfully";
    }
}

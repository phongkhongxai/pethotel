package com.bumble.pethotel.services.impl;


import com.bumble.pethotel.models.entity.Role;
import com.bumble.pethotel.models.entity.User;
import com.bumble.pethotel.models.exception.PetApiException;
import com.bumble.pethotel.models.payload.dto.SignupDto;
import com.bumble.pethotel.models.payload.dto.UserDto;
import com.bumble.pethotel.models.payload.requestModel.UserUpdatedRequest;
import com.bumble.pethotel.models.payload.responseModel.UsersResponse;
import com.bumble.pethotel.repositories.RoleRepository;
import com.bumble.pethotel.repositories.UserRepository;
import com.bumble.pethotel.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDto saveAdminUser(SignupDto signupDto) {
        // add check if username already exists
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
        }
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        user.setRole(userRole);
        user.setAvatarUrl("default");


        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto saveOwnerUser(SignupDto signupDto) {
        // add check if username already exists
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
        }
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = modelMapper.map(signupDto, User.class);

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Role userRole = roleRepository.findByRoleName("ROLE_OWNER")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        user.setRole(userRole);
        user.setAvatarUrl("default");

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserUpdatedRequest userUpdatedRequest) {
        // Find the existing user
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new PetApiException(HttpStatus.BAD_REQUEST, "User not found"));

        if (!existingUser.getUsername().equals(userUpdatedRequest.getUsername()) &&
                userRepository.existsByUsername(userUpdatedRequest.getUsername())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Username is already in use!");
        }

        if (!existingUser.getEmail().equals(userUpdatedRequest.getEmail()) &&
                userRepository.existsByEmail(userUpdatedRequest.getEmail())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Email is already in use!");
        }

        if (userUpdatedRequest.getFullName() != null) {
            existingUser.setFullName(userUpdatedRequest.getFullName());
        }
        if (userUpdatedRequest.getEmail() != null) {
            existingUser.setEmail(userUpdatedRequest.getEmail());
        }
        if (userUpdatedRequest.getDob() != null) {
            existingUser.setDob(userUpdatedRequest.getDob());
        }
        if (userUpdatedRequest.getAddress() != null) {
            existingUser.setAddress(userUpdatedRequest.getAddress());
        }
        if (userUpdatedRequest.getGender() != null) {
            existingUser.setGender(userUpdatedRequest.getGender());
        }
        if (userUpdatedRequest.getPhone() != null) {
            existingUser.setPhone(userUpdatedRequest.getPhone());
        }
        if (userUpdatedRequest.getUsername() != null) {
            existingUser.setUsername(userUpdatedRequest.getUsername());
        }
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto changePassword(Long id, String oldPassword, String newPassword) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new PetApiException(HttpStatus.BAD_REQUEST, "User not found"));
        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }
        if (!passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            throw new PetApiException(HttpStatus.BAD_REQUEST, "New password must different from old password.");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getProfileUserById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new PetApiException(HttpStatus.BAD_REQUEST, "User not found"));
        return modelMapper.map(existingUser, UserDto.class);
    }

    @Override
    public String deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new PetApiException(HttpStatus.BAD_REQUEST, "User not found"));
        existingUser.setDelete(true);
        userRepository.save(existingUser);
        return "Deleted user successfully";
    }

    @Override
    public UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findAllNotDeleted(pageable);

        List<User> userList = users.getContent();

        List<UserDto> content = userList.stream().map(bt -> modelMapper.map(bt, UserDto.class)).collect(Collectors.toList());

        UsersResponse templatesResponse = new UsersResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(users.getNumber());
        templatesResponse.setPageSize(users.getSize());
        templatesResponse.setTotalElements(users.getTotalElements());
        templatesResponse.setTotalPages(users.getTotalPages());
        templatesResponse.setLast(users.isLast());

        return templatesResponse;
    }

    @Override
    public UsersResponse getAllOwnerUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Role userRole = roleRepository.findByRoleName("ROLE_OWNER")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findByRoleAndIsDeleteFalse( userRole, pageable);

        List<User> userList = users.getContent();

        List<UserDto> content = userList.stream().map(bt -> modelMapper.map(bt, UserDto.class)).collect(Collectors.toList());

        UsersResponse templatesResponse = new UsersResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(users.getNumber());
        templatesResponse.setPageSize(users.getSize());
        templatesResponse.setTotalElements(users.getTotalElements());
        templatesResponse.setTotalPages(users.getTotalPages());
        templatesResponse.setLast(users.isLast());

        return templatesResponse;
    }

    @Override
    public UsersResponse getAllAdminUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Role userRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findByRoleAndIsDeleteFalse( userRole, pageable);

        List<User> userList = users.getContent();

        List<UserDto> content = userList.stream().map(bt -> modelMapper.map(bt, UserDto.class)).collect(Collectors.toList());

        UsersResponse templatesResponse = new UsersResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(users.getNumber());
        templatesResponse.setPageSize(users.getSize());
        templatesResponse.setTotalElements(users.getTotalElements());
        templatesResponse.setTotalPages(users.getTotalPages());
        templatesResponse.setLast(users.isLast());

        return templatesResponse;
    }

    @Override
    public UsersResponse getAllCustomerUser(int pageNo, int pageSize, String sortBy, String sortDir) {
        Role userRole = roleRepository.findByRoleName("ROLE_CUSTOMER")
                .orElseThrow(() -> new PetApiException(HttpStatus.NOT_FOUND, "User Role not found."));
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findByRoleAndIsDeleteFalse( userRole, pageable);

        List<User> userList = users.getContent();

        List<UserDto> content = userList.stream().map(bt -> modelMapper.map(bt, UserDto.class)).collect(Collectors.toList());

        UsersResponse templatesResponse = new UsersResponse();
        templatesResponse.setContent(content);
        templatesResponse.setPageNo(users.getNumber());
        templatesResponse.setPageSize(users.getSize());
        templatesResponse.setTotalElements(users.getTotalElements());
        templatesResponse.setTotalPages(users.getTotalPages());
        templatesResponse.setLast(users.isLast());

        return templatesResponse;
    }

    @Override
    public UserDto activatePremium(Long userId, int months) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PetApiException(HttpStatus.BAD_REQUEST, "User not found"));

        user.setPremium(true);
        user.setPremiumExpiryDate(LocalDateTime.now().plusMonths(months));
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Scheduled(cron = "0 0 23 * * ?") // Chạy vào 23:00 mỗi ngày
    public void checkAndUpdatePremiumStatusForAllUsers() {
        List<User> premiumUsers = userRepository.findByIsPremiumTrue(); // Giả sử bạn có method này để lấy user premium

        for (User user : premiumUsers) {
            checkAndUpdatePremiumStatus(user);
        }
    }

    public void checkAndUpdatePremiumStatus(User user) {
        if (user.isPremium() && user.getPremiumExpiryDate()!=null && user.getPremiumExpiryDate().isBefore(LocalDateTime.now())) {
            user.setPremium(false);
            userRepository.save(user);
        }
    }
}

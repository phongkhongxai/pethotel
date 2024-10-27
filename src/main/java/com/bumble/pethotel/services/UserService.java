package com.bumble.pethotel.services;


import com.bumble.pethotel.models.payload.dto.SignupDto;
import com.bumble.pethotel.models.payload.dto.UserDto;
import com.bumble.pethotel.models.payload.requestModel.UserUpdatedRequest;
import com.bumble.pethotel.models.payload.responseModel.UsersResponse;

public interface UserService {

    UserDto saveAdminUser(SignupDto signupDto);
    UserDto saveOwnerUser(SignupDto signupDto);

    UserDto updateUser(Long id, UserUpdatedRequest signupDto);
    UserDto changePassword(Long id, String oldPassword, String newPassword);
    UserDto getProfileUserById(Long id);

    String deleteUser(Long id);
    UsersResponse getAllUser(int pageNo, int pageSize, String sortBy, String sortDir);

    UsersResponse getAllOwnerUser(int pageNo, int pageSize, String sortBy, String sortDir);
    UsersResponse getAllAdminUser(int pageNo, int pageSize, String sortBy, String sortDir);
    UsersResponse getAllCustomerUser(int pageNo, int pageSize, String sortBy, String sortDir);

    UserDto activatePremium(Long userId, int months);



}

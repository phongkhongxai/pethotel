package com.bumble.pethotel.controllers;


import com.bumble.pethotel.models.payload.dto.SignupDto;
import com.bumble.pethotel.models.payload.dto.UserDto;
import com.bumble.pethotel.models.payload.requestModel.UserUpdatedRequest;
import com.bumble.pethotel.models.payload.responseModel.UsersResponse;
import com.bumble.pethotel.services.UserService;
import com.bumble.pethotel.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){ this.userService=userService;}

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admins")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody SignupDto signupDto) {
        UserDto bt = userService.saveAdminUser(signupDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/owners")
    public ResponseEntity<?> createOwnerUser(@Valid @RequestBody SignupDto signupDto) {
        UserDto bt = userService.saveOwnerUser(signupDto);
        return new ResponseEntity<>(bt, HttpStatus.CREATED);
    }

    @GetMapping
    public UsersResponse getAllUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                     @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return userService.getAllUser(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/admins")
    public UsersResponse getAllAdminUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
            return userService.getAllAdminUser(pageNo, pageSize, sortBy, sortDir);

    }
    @GetMapping("/owners")
    public UsersResponse getAllOwnerUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
            return userService.getAllOwnerUser(pageNo, pageSize, sortBy, sortDir);

    }

    @GetMapping("/customers")
    public UsersResponse getAllCustomerUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
            return userService.getAllCustomerUser(pageNo, pageSize, sortBy, sortDir);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        UserDto userDto = userService.getProfileUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,@Valid @RequestBody UserUpdatedRequest bt) {
        UserDto bt1 = userService.updateUser(id, bt);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @PutMapping("/active-premium/{id}/")
    public ResponseEntity<?> updateUserPremium(@PathVariable("id") Long id,@RequestParam int months) {
        UserDto bt1 = userService.activatePremium(id, months);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        String msg = userService.deleteUser(id);
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_OWNER')")
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long id, @RequestParam String oldPassword, @RequestParam String newPassword){
        UserDto bt1 = userService.changePassword(id, oldPassword, newPassword);
        return new ResponseEntity<>(bt1, HttpStatus.OK);
    }




}

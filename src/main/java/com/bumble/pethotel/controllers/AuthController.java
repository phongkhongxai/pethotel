package com.bumble.pethotel.controllers;


import com.bumble.pethotel.models.payload.dto.LoginDto;
import com.bumble.pethotel.models.payload.dto.SignupDto;
import com.bumble.pethotel.models.payload.responseModel.AuthenticationResponse;
import com.bumble.pethotel.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/no-auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDto loginDto){
        AuthenticationResponse token = authService.login(loginDto);
        return  ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupDto signupDto){
        String response = authService.signup(signupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmailCode(@RequestParam("email") String email,
                                                  @RequestParam("code") String code) {
        String response = authService.verifyEmailCode(email, code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendVerificationCode(@RequestParam("email") String email) {
         String response = authService.resendVerificationCode(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
         String response = authService.forgotPassword(email);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        String response = authService.resetPassword(token, newPassword);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}

package com.bumble.pethotel.services;


import com.bumble.pethotel.models.payload.dto.LoginDto;
import com.bumble.pethotel.models.payload.dto.SignupDto;
import com.bumble.pethotel.models.payload.requestModel.NewPasswordRequest;
import com.bumble.pethotel.models.payload.responseModel.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    AuthenticationResponse login(LoginDto loginDto);
    String signup(SignupDto signupDto);
    String signupOwner(SignupDto signupDto);
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    String verifyEmailCode(String email, String code);
    String resendVerificationCode(String email);
    String forgotPassword(String email);
    String resetPassword(NewPasswordRequest newPasswordRequest);
}

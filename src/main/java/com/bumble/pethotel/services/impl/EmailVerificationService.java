package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.models.entity.Shop;
import com.bumble.pethotel.models.entity.User;
import com.bumble.pethotel.repositories.UserRepository;
import com.bumble.pethotel.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailVerificationService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

     @Async
    public void sendVerificationCode(User user) {
        String code = CodeGenerator.generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(10)); // Code valid for 10 minutes

        // Send email with the verification code
        sendEmailVerify(user.getEmail(), code);
    }


    private void sendEmailVerify(String recipientEmail, String verificationCode) {
        String subject = "Email Verification Code";
        String message = "Your verification code is: " + verificationCode;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("apehome8386@gmail.com");
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    @Async
    public void sendPasswordResetToken(User user) {
        String token = CodeGenerator.generateVerificationCode();
        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(15)); // Token valid for 15 minutes
        userRepository.save(user);

        // Send email with the reset token
        sendEmailReset(user.getEmail(), token);
    }


    private void sendEmailReset(String recipientEmail, String resetToken) {
        String subject = "Password Reset Request";
        String message = "To reset your password, use the following token: " + resetToken;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("apehome8386@gmail.com");
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    @Async
    public void sendEmailNotify(Shop shop) {
        String subject = "Shop Verification Notification";
        String message = "Dear " + shop.getUser().getFullName() + ",\n\n"
                + "We are pleased to inform you that your shop, " + shop.getName() + ", has been successfully verified.\n"
                + "You can now manage your services and rooms.\n\n"
                + "Best regards,\n"
                + "Apehome Team";

        sendEmail(shop.getUser().getEmail(), subject, message);
    }

    private void sendEmail(String recipientEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("apehome8386@gmail.com");
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}

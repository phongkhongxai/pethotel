package com.bumble.pethotel.models.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus
public class PetApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public PetApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public PetApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}

package com.example.productservice.exceptionhandle;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
//    String message;
}

package com.service.category.dtos;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomMessage {

    private String message;

    private boolean success;

    private HttpStatus status;
}

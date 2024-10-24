package com.example.app.application.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse {

    @NonNull
    private final String error;

    @NonNull
    private final String message;

    private List<ValidationErrorResponse> fieldErrors;

    private LocalDateTime timestamp = LocalDateTime.now();
}

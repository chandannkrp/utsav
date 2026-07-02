package dev.utsav.api.error;

import java.time.LocalDateTime;

public record ApiError(
        String code,
        String message,
        int status,
        LocalDateTime timestamp
) {

    public static ApiError of(String code, String message, int status){
        return new ApiError(code, message, status, LocalDateTime.now());
    }
}

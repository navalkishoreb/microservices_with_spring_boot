package edu.navalkishoreb.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response"
)
public class ErrorResponseDto {

    @Schema(description = "API path where error occurred", example = "/api/create")
    private String apiPath;

    @Schema(description = "HTTP status code of the error", example = "500")
    private HttpStatus errorCode;

    @Schema(description = "Error message", example = "Internal Server Error")
    private String errorMessage;

    @Schema(description = "Time when error occurred", example = "2021-09-01T10:20:30")
    private LocalDateTime errorTime;
}

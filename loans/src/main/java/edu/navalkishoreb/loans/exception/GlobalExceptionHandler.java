package edu.navalkishoreb.loans.exception;

import java.time.LocalDateTime;
import java.util.Optional;

import edu.navalkishoreb.loans.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LoanAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleLoanAlreadyExistsException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .apiPath(webRequest.getDescription(false))
                .build();
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .apiPath(webRequest.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .apiPath(webRequest.getDescription(false))
                .build();
        return ResponseEntity.internalServerError().body(errorResponseDto);
    }
}

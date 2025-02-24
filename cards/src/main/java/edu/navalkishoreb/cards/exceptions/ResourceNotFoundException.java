package edu.navalkishoreb.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("Resource %s with %s %s not found", resourceName, field, value));
    }
}

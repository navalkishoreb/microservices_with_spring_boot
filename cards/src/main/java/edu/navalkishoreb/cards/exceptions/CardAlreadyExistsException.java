package edu.navalkishoreb.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CardAlreadyExistsException extends RuntimeException {

        public CardAlreadyExistsException(String message) {
            super(message);
        }
}

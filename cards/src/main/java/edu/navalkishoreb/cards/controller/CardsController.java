package edu.navalkishoreb.cards.controller;

import edu.navalkishoreb.cards.dto.CardsDto;
import edu.navalkishoreb.cards.dto.ResponseDto;
import edu.navalkishoreb.cards.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CardsController {

    private ICardsService iCardsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.builder().statusCode("200").statusMsg("Cards created successfully").build());
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        CardsDto cards = iCardsService.fetchCard(mobileNumber);
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCard(@Valid @RequestBody CardsDto cardsDto) {
        if (iCardsService.updateCard(cardsDto)) {
            return ResponseEntity.ok((ResponseDto.builder().statusCode("200").statusMsg("Cards updated successfully").build()));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResponseDto.builder().statusCode("417").statusMsg("Cards update failed").build());
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        if (iCardsService.deleteCard(mobileNumber)) {
            return ResponseEntity.ok(ResponseDto.builder().statusCode("200").statusMsg("Cards deleted successfully").build());
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResponseDto.builder().statusCode("417").statusMsg("Cards delete failed").build());
        }

    }

}

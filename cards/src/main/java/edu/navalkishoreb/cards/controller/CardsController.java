package edu.navalkishoreb.cards.controller;

import edu.navalkishoreb.cards.dto.CardsContactInfoDto;
import edu.navalkishoreb.cards.dto.CardsDto;
import edu.navalkishoreb.cards.dto.ErrorResponseDto;
import edu.navalkishoreb.cards.dto.ResponseDto;
import edu.navalkishoreb.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

@Tag(
        name = "CRUD REST APIs for Cards",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE card details"
)
@Validated
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CardsController {

    private final ICardsService iCardsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

    public CardsController(ICardsService iCardsService) {
        this.iCardsService = iCardsService;
    }

    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "HTTP Status CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP Status Internal Server Error",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.builder().statusCode(getStatusCode(HttpStatus.CREATED)).statusMsg("Cards created successfully").build());
    }

    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        CardsDto cards = iCardsService.fetchCard(mobileNumber);
        return ResponseEntity.ok(cards);
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update card details based on a card number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCard(@Valid @RequestBody CardsDto cardsDto) {
        if (iCardsService.updateCard(cardsDto)) {
            return ResponseEntity.ok(
                    (ResponseDto.builder().statusCode(getStatusCode(HttpStatus.OK)).statusMsg("Cards updated successfully").build()));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResponseDto.builder().statusCode(getStatusCode(HttpStatus.EXPECTATION_FAILED)).statusMsg("Cards update failed").build());
        }
    }

    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCard(
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        if (iCardsService.deleteCard(mobileNumber)) {
            return ResponseEntity.ok(ResponseDto.builder().statusCode(getStatusCode(HttpStatus.OK)).statusMsg("Cards deleted successfully").build());
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResponseDto.builder().statusCode(getStatusCode(HttpStatus.EXPECTATION_FAILED)).statusMsg("Cards delete failed").build());
        }

    }

    private String getStatusCode(HttpStatus status) {
        return String.valueOf(status.value());
    }

    @Operation(
            summary = "Get build information",
            description = "REST API to get Accounts Service build information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.ok().body(buildVersion);
    }

    @Operation(
            summary = "Get Java information",
            description = "REST API to get Cards Service java information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.ok().body(environment.getProperty("JAVA_HOME"));
    }


    @Operation(
            summary = "Get Contact information",
            description = "REST API to get Cards Service Contact information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo(){
        return ResponseEntity.ok().body(cardsContactInfoDto);
    }
}

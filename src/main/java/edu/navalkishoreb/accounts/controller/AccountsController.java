package edu.navalkishoreb.accounts.controller;

import edu.navalkishoreb.accounts.constants.AccountsConstants;
import edu.navalkishoreb.accounts.dto.CustomerDto;
import edu.navalkishoreb.accounts.dto.ResponseDto;
import edu.navalkishoreb.accounts.service.IAccountService;
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

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class AccountsController {

    private IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(
            @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits") @RequestParam String mobileNumber) {
        CustomerDto customerDto = accountService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.ok().body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountService.updateAccounts(customerDto);
        if (isUpdated) {
            return ResponseEntity.ok()
                    .body(ResponseDto.builder().statusCode(AccountsConstants.STATUS_200).statusMsg(AccountsConstants.MESSAGE_200).build());
        } else {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.builder().statusCode(AccountsConstants.STATUS_500).statusMsg(AccountsConstants.MESSAGE_500).build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(
            @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits") @RequestParam String mobileNumber) {
        boolean isDeleted = accountService.deleteAccounts(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.ok().body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.builder().statusCode(AccountsConstants.STATUS_500).statusMsg(AccountsConstants.MESSAGE_500).build());
        }

    }
}

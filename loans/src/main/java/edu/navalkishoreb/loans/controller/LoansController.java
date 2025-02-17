package edu.navalkishoreb.loans.controller;

import edu.navalkishoreb.loans.dto.LoansDto;
import edu.navalkishoreb.loans.dto.ResponseDto;
import edu.navalkishoreb.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class LoansController {

    private ILoansService iLoanService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam String mobileNumber) {
        iLoanService.createLoan(mobileNumber);
        return ResponseEntity.ok(ResponseDto.builder().statusCode("200").statusMsg("Success").build());
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(@RequestParam String mobileNumber) {
        LoansDto loansDto = iLoanService.fetchLoan(mobileNumber);
        return ResponseEntity.ok(loansDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoan(@RequestBody LoansDto loansDto) {
        if (iLoanService.updateLoan(loansDto)) {
            return ResponseEntity.ok(ResponseDto.builder().statusCode("200").statusMsg("Success").build());
        } else {
            return ResponseEntity.unprocessableEntity().body(ResponseDto.builder().statusCode("422").statusMsg("Unprocessable Entity").build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoan(@RequestParam String mobileNumber) {
        if (iLoanService.deleteLoan(mobileNumber)) {
            return ResponseEntity.ok(ResponseDto.builder().statusCode("200").statusMsg("Success").build());
        } else {
            return ResponseEntity.unprocessableEntity().body(ResponseDto.builder().statusCode("422").statusMsg("Unprocessable Entity").build());
        }
    }
}

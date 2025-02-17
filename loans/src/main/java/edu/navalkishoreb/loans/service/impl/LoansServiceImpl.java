package edu.navalkishoreb.loans.service.impl;

import java.util.Optional;
import java.util.Random;

import edu.navalkishoreb.loans.constants.LoansConstants;
import edu.navalkishoreb.loans.dto.LoansDto;
import edu.navalkishoreb.loans.entity.Loans;
import edu.navalkishoreb.loans.exception.LoanAlreadyExistsException;
import edu.navalkishoreb.loans.exception.ResourceNotFoundException;
import edu.navalkishoreb.loans.mapper.LoansMapper;
import edu.navalkishoreb.loans.repository.LoansRepository;
import edu.navalkishoreb.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> loans = loansRepository.findByMobileNumber(mobileNumber);
        loans.ifPresent(it -> {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
        });
        loansRepository.save(createNewLoan(mobileNumber));
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loans", "mobileNumber", mobileNumber));
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /**
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        String loanNumber = loansDto.getLoanNumber();
        Loans loans = loansRepository. findByLoanNumber(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loans", "loanNumber", loanNumber));
        Loans updatedLoans = LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(updatedLoans);
        return true;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loans", "mobileNumber", mobileNumber));
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }
}

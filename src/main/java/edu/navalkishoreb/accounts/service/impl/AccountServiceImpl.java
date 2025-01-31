package edu.navalkishoreb.accounts.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import edu.navalkishoreb.accounts.constants.AccountsConstants;
import edu.navalkishoreb.accounts.dto.AccountsDto;
import edu.navalkishoreb.accounts.dto.CustomerDto;
import edu.navalkishoreb.accounts.entity.Accounts;
import edu.navalkishoreb.accounts.entity.Customer;
import edu.navalkishoreb.accounts.exception.CustomerAlreadyExistsException;
import edu.navalkishoreb.accounts.exception.ResourceNotFoundException;
import edu.navalkishoreb.accounts.mapper.AccountsMapper;
import edu.navalkishoreb.accounts.mapper.CustomerMapper;
import edu.navalkishoreb.accounts.repository.AccountsRepository;
import edu.navalkishoreb.accounts.repository.CustomerRepository;
import edu.navalkishoreb.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * Creates a new account for the given customer.
     *
     * @param customerDto the data transfer object containing customer details
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        customerRepository.findByMobileNumber(customer.getMobileNumber()).ifPresent(c -> {
            throw new CustomerAlreadyExistsException(String.format("Customer already exists with mobile number %s", customer.getMobileNumber()));
        });
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));

    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccounts = new Accounts();
        newAccounts.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        newAccounts.setAccountNumber(randomAccountNumber);
        newAccounts.setAccountType(AccountsConstants.SAVINGS);
        newAccounts.setBranchAddress(AccountsConstants.ADDRESS);
        newAccounts.setCreatedBy("Anonymous");
        newAccounts.setCreatedAt(LocalDateTime.now());
        return newAccounts;

    }

    /**
     * Fetches the account details for the given mobile number.
     *
     * @param mobileNumber the mobile number of the customer
     * @return the data transfer object containing account details
     */
    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.toAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }
}

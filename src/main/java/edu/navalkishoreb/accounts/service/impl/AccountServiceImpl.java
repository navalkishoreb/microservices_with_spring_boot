package edu.navalkishoreb.accounts.service.impl;

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

    /**
     * Updates the account details for the given customer.
     *
     * @param customerDto the data transfer object containing customer details
     * @return true if the account details are updated successfully, false otherwise
     */
    @Override
    public boolean updateAccounts(CustomerDto customerDto) {
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto == null) {
            return false;
        }
        Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "accountId", accountsDto.getAccountNumber().toString()));
        AccountsMapper.toAccounts(accountsDto, accounts);
        accounts = accountsRepository.save(accounts);
        Long customerId = accounts.getCustomerId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId.toString()));
        CustomerMapper.mapToCustomer(customerDto, customer);
        customerRepository.save(customer);
        return true;
    }

    /**
     * Deletes the account associated with the given mobile number.
     *
     * @param mobileNumber the mobile number of the customer
     * @return true if the account is deleted successfully, false otherwise
     */
    @Override
    public boolean deleteAccounts(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Long customerId = customer.getCustomerId();
        accountsRepository.deleteByCustomerId(customerId);
        customerRepository.delete(customer);
        return true;
    }
}

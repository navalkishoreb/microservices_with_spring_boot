package edu.navalkishoreb.accounts.service;

import edu.navalkishoreb.accounts.dto.CustomerDto;

public interface IAccountService {

    /**
     * Creates a new account for the given customer.
     *
     * @param customerDto the data transfer object containing customer details
     */
    void createAccount(CustomerDto customerDto);

    /**
     * Fetches the account details for the given mobile number.
     *
     * @param mobileNumber the mobile number of the customer
     * @return the data transfer object containing account details
     */
    CustomerDto fetchAccountDetails(String mobileNumber);
}

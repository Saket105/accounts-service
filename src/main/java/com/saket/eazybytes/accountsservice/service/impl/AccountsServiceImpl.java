package com.saket.eazybytes.accountsservice.service.impl;

import com.saket.eazybytes.accountsservice.dto.CustomerDto;
import com.saket.eazybytes.accountsservice.exception.CustomerAlreadyExistsException;
import com.saket.eazybytes.accountsservice.mapper.CustomerMapper;
import com.saket.eazybytes.accountsservice.model.Accounts;
import com.saket.eazybytes.accountsservice.model.Customer;
import com.saket.eazybytes.accountsservice.repository.AccountsRepository;
import com.saket.eazybytes.accountsservice.repository.CustomerRepository;
import com.saket.eazybytes.accountsservice.service.IAccountsService;
import com.saket.eazybytes.accountsservice.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccounts(savedCustomer));
    }
    private Accounts createNewAccounts(Customer customer){
        Accounts newAcc = new Accounts();
        newAcc.setCustomerId(customer.getCustomerId());
        long randAccNumber = 1000000000L+new Random().nextInt(900000000);
        newAcc.setAccountNumber(randAccNumber);
        newAcc.setAccountType(Constants.SAVINGS);
        newAcc.setBranchAddress(Constants.ADDRESS);
        newAcc.setCreatedAt(LocalDateTime.now());
        newAcc.setCreatedBy("Anonymous");
        return newAcc;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        return null;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        return false;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        return false;
    }
}

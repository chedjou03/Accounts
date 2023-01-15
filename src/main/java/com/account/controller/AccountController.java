package com.account.controller;

import com.account.config.AccountsServiceConfig;
import com.account.model.*;
import com.account.repository.AccountRepository;
import com.account.service.client.CardsFeignClient;
import com.account.service.client.LoansFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountsServiceConfig accountsServiceConfig;

    @Autowired
    LoansFeignClient loansFeignClient;

    @Autowired
    CardsFeignClient cardsFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping("/myAccount")
    public Account getAccountDetail(@RequestHeader("ChedjouBank-correlation-id") String correlationId,@RequestBody Customer customer){
        logger.debug("ChedjouBank-correlation-id : {}. ", correlationId);
        Account account = accountRepository.findByCustomerId(customer.getCustomerId());
        if(account!=null){
            return account;
        }else {
            return null;
        }
    }

    @GetMapping("/accounts/properties")
    public String getAccountsProperties() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(), accountsServiceConfig.getActiveBranches(), accountsServiceConfig.getMailDetails());
        String jsonStr = objectMapper.writeValueAsString(properties);
        return jsonStr;
    }

    @PostMapping("/myCustomerDetails")
    @CircuitBreaker(name = "customerDetailsCircuitBreaker",fallbackMethod = "myDefaultCustomerDetailFallBack")
    public CustomerDetails myCustomerDetails(@RequestHeader("ChedjouBank-correlation-id") String correlationId, @RequestBody Customer customer) {
        logger.info("myCustomerDetails method has started");
        logger.debug("ChedjouBank-correlation-id : {}. ", correlationId);
        Account accounts = accountRepository.findByCustomerId(customer.getCustomerId());
        List<Loan> loans = loansFeignClient.getLoansDetail(correlationId, customer);
        List<Card> cards = cardsFeignClient.getCardDetails(correlationId,customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);

        logger.info("myCustomerDetails method is ending");
        return customerDetails;

    }
    
    private CustomerDetails myDefaultCustomerDetailFallBack(@RequestHeader("ChedjouBank-correlation-id") String correlationId,Customer customer,Throwable t){
        logger.info("myDefaultCustomerDetailFallBack method has started");
        logger.debug("ChedjouBank-correlation-id : {}. ", correlationId);
        CustomerDetails customerDetails = new CustomerDetails();
        Account accounts = accountRepository.findByCustomerId(customer.getCustomerId());
        List<Loan> loans = loansFeignClient.getLoansDetail(correlationId,customer);
        customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);

        logger.info("myDefaultCustomerDetailFallBack method is ending");
        return customerDetails;
    }
}

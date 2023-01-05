package com.account.controller;

import com.account.config.AccountsServiceConfig;
import com.account.model.Account;
import com.account.model.Customer;
import com.account.model.Properties;
import com.account.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountsServiceConfig accountsServiceConfig;

    @PostMapping("/myAccount")
    public Account getAccountDetail(@RequestBody Customer customer){
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

}

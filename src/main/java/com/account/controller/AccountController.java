package com.account.controller;

import com.account.model.Account;
import com.account.model.Customer;
import com.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/myAccount")
    public Account getAccountDetail(@RequestBody Customer customer){
        Account account = accountRepository.findByCustomerId(customer.getCustomerId());
        if(account!=null){
            return account;
        }else {
            return null;
        }

    }
}

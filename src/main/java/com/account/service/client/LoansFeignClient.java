package com.account.service.client;


import com.account.model.Customer;
import com.account.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@FeignClient("loans")
public interface LoansFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "myLoan", consumes = "application/json")
    ArrayList<Loan> getLoansDetail(@RequestHeader("ChedjouBank-correlation-id") String correlationId, @RequestBody Customer customer);
}

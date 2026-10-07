package com.ninjabank.account.controller;

import com.ninjabank.account.dto.BalanceRequest;
import com.ninjabank.account.entity.Account;
import com.ninjabank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return accountService.getAccount(accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(
            @PathVariable String accountNumber,
            @RequestBody BalanceRequest request) {
        return accountService.deposit(accountNumber, request.getAmount());
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(
            @PathVariable String accountNumber,
            @RequestBody BalanceRequest request) {
        return accountService.withdraw(accountNumber, request.getAmount());
    }
}
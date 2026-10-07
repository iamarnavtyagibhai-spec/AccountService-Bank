package com.ninjabank.account.service;

import com.ninjabank.account.entity.Account;
import com.ninjabank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(String accountNumber) {

        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));
    }

    @Override
    public Account deposit(
            String accountNumber,
            BigDecimal amount) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (!"ACTIVE".equals(account.getAccountStatus().name())) {
            throw new RuntimeException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        account.setBalance(
                account.getBalance().add(amount)
        );

        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(
            String accountNumber,
            BigDecimal amount) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (!"ACTIVE".equals(account.getAccountStatus().name())) {
            throw new RuntimeException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(amount)
        );

        return accountRepository.save(account);
    }
}
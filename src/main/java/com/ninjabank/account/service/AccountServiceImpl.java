package com.ninjabank.account.service;

import com.ninjabank.account.entity.Account;
import com.ninjabank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
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

    @Override
    @Transactional
    public Account transfer(
            String fromAccountNumber,
            String toAccountNumber,
            BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (fromAccountNumber != null && fromAccountNumber.equalsIgnoreCase(toAccountNumber)) {
            throw new RuntimeException("Sender and receiver accounts cannot be the same");
        }

        Account sender = accountRepository
                .findByAccountNumber(fromAccountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Sender account not found"));

        Account receiver = accountRepository
                .findByAccountNumber(toAccountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Receiver account not found"));

        if (!"ACTIVE".equals(sender.getAccountStatus().name())) {
            throw new RuntimeException("Sender account is not active");
        }

        if (!"ACTIVE".equals(receiver.getAccountStatus().name())) {
            throw new RuntimeException("Receiver account is not active");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(
                sender.getBalance().subtract(amount)
        );

        receiver.setBalance(
                receiver.getBalance().add(amount)
        );

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return sender;
    }
}

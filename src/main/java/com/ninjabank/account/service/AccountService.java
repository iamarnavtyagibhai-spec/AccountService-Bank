package com.ninjabank.account.service;

import com.ninjabank.account.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account getAccount(String accountNumber);

    Account deposit(String accountNumber, BigDecimal amount);

    Account withdraw(String accountNumber, BigDecimal amount);

    Account transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);
}

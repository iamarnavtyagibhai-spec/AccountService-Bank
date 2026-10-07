package com.ninjabank.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceTransferRequest {

    private String fromAccountNumber;

    private String toAccountNumber;

    private BigDecimal amount;
}

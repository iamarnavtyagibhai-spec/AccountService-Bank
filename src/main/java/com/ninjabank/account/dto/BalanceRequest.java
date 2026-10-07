package com.ninjabank.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceRequest {

    private BigDecimal amount;
}
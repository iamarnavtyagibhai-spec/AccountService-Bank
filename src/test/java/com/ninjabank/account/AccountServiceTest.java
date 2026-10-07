package com.ninjabank.account;

import com.ninjabank.account.entity.Account;
import com.ninjabank.account.enums.AccountStatus;
import com.ninjabank.account.repository.AccountRepository;
import com.ninjabank.account.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account activeAccount;

    @BeforeEach
    void setUp() {
        activeAccount = Account.builder()
                .id(1L)
                .accountNumber("ACC12345")
                .accountName("Ninja User")
                .balance(new BigDecimal("1000.00"))
                .accountType("SAVINGS")
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Test 1: Get Account Successfully")
    void testGetAccount_Success() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        Account result = accountService.getAccount("ACC12345");

        assertNotNull(result);
        assertEquals("ACC12345", result.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber("ACC12345");
    }

    @Test
    @DisplayName("Test 2: Get Account Not Found Throws Exception")
    void testGetAccount_NotFound() {
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.getAccount("INVALID"));

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test 3: Deposit Successfully")
    void testDeposit_Success() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.deposit("ACC12345", new BigDecimal("500.00"));

        assertNotNull(result);
        assertEquals(new BigDecimal("1500.00"), result.getBalance());
        verify(accountRepository, times(1)).save(activeAccount);
    }

    @Test
    @DisplayName("Test 4: Deposit with Zero or Negative Amount Throws Exception")
    void testDeposit_NegativeOrZeroAmount() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        RuntimeException exZero = assertThrows(RuntimeException.class, () ->
                accountService.deposit("ACC12345", BigDecimal.ZERO));
        assertEquals("Amount must be greater than zero", exZero.getMessage());

        RuntimeException exNegative = assertThrows(RuntimeException.class, () ->
                accountService.deposit("ACC12345", new BigDecimal("-100.00")));
        assertEquals("Amount must be greater than zero", exNegative.getMessage());
    }

    @Test
    @DisplayName("Test 5: Deposit on Inactive/Blocked Account Throws Exception")
    void testDeposit_InactiveAccount() {
        activeAccount.setAccountStatus(AccountStatus.BLOCKED);
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.deposit("ACC12345", new BigDecimal("500.00")));

        assertEquals("Account is not active", exception.getMessage());
    }

    @Test
    @DisplayName("Test 6: Withdraw Successfully")
    void testWithdraw_Success() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.withdraw("ACC12345", new BigDecimal("300.00"));

        assertNotNull(result);
        assertEquals(new BigDecimal("700.00"), result.getBalance());
        verify(accountRepository, times(1)).save(activeAccount);
    }

    @Test
    @DisplayName("Test 7: Withdraw Exceeding Balance Throws Exception")
    void testWithdraw_InsufficientBalance() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.withdraw("ACC12345", new BigDecimal("1500.00")));

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    @DisplayName("Test 8: Withdraw Zero or Negative Amount Throws Exception")
    void testWithdraw_NegativeOrZeroAmount() {
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        RuntimeException exZero = assertThrows(RuntimeException.class, () ->
                accountService.withdraw("ACC12345", BigDecimal.ZERO));
        assertEquals("Amount must be greater than zero", exZero.getMessage());
    }

    @Test
    @DisplayName("Test 9: Withdraw on Closed Account Throws Exception")
    void testWithdraw_ClosedAccount() {
        activeAccount.setAccountStatus(AccountStatus.CLOSED);
        when(accountRepository.findByAccountNumber("ACC12345")).thenReturn(Optional.of(activeAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.withdraw("ACC12345", new BigDecimal("100.00")));

        assertEquals("Account is not active", exception.getMessage());
    }
}

package com.ninjabank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjabank.account.dto.BalanceRequest;
import com.ninjabank.account.entity.Account;
import com.ninjabank.account.enums.AccountStatus;
import com.ninjabank.account.repository.AccountRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private String validJwtToken;
    private String testAccountNumber;

    @BeforeEach
    void setUp() {
        testAccountNumber = "ACC_TEST_" + System.currentTimeMillis();

        Account account = Account.builder()
                .accountNumber(testAccountNumber)
                .accountName("Integration Test User")
                .balance(new BigDecimal("5000.00"))
                .accountType("SAVINGS")
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();

        accountRepository.save(account);

        validJwtToken = "Bearer " + Jwts.builder()
                .setSubject("testuser@ninjabank.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    @Test
    @DisplayName("API Test 1: Unauthenticated request should be 403 Forbidden")
    void testEndpoint_Unauthenticated_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/accounts/" + testAccountNumber))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("API Test 2: Authenticated GET /accounts/{accountNumber} should return 200 OK")
    void testGetAccount_Authenticated_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/accounts/" + testAccountNumber)
                        .header("Authorization", validJwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(testAccountNumber))
                .andExpect(jsonPath("$.balance").value(5000.00))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    @DisplayName("API Test 3: Authenticated POST /accounts/{accountNumber}/deposit should update balance")
    void testDeposit_Authenticated_ShouldUpdateBalance() throws Exception {
        BalanceRequest request = new BalanceRequest();
        request.setAmount(new BigDecimal("1500.00"));

        mockMvc.perform(post("/accounts/" + testAccountNumber + "/deposit")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(6500.00));
    }

    @Test
    @DisplayName("API Test 4: Authenticated POST /accounts/{accountNumber}/withdraw should deduct balance")
    void testWithdraw_Authenticated_ShouldDeductBalance() throws Exception {
        BalanceRequest request = new BalanceRequest();
        request.setAmount(new BigDecimal("2000.00"));

        mockMvc.perform(post("/accounts/" + testAccountNumber + "/withdraw")
                        .header("Authorization", validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(3000.00));
    }
}

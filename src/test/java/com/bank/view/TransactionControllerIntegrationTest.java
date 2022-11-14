package com.bank.view;

import com.bank.App;
import com.bank.controller.request.TransactionRequest;
import com.bank.model.Account;
import com.bank.model.TransactionType;
import com.bank.repo.AccountRepository;
import com.bank.repo.TransactionRepository;
import com.bank.repo.TransactionTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        transactionRepository.deleteAll();
        transactionTypeRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        transactionRepository.deleteAll();
        transactionTypeRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToCreditAmountInAccount() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(100));
        transactionTypeRepository.save(new TransactionType("CREDIT"));

        mockMvc.perform(post("/transaction/credit").with(httpBasic(userAccount.getId(),password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldThrowErrorWhenAmountToBeCreditedIsLessThanOne() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(0));
        transactionTypeRepository.save(new TransactionType("CREDIT"));

        mockMvc.perform(post("/transaction/credit").with(httpBasic(userAccount.getId(),password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }
}

package com.bank.view;

import com.bank.App;
import com.bank.controller.request.TransactionRequest;
import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.constants.TransactionType;
import com.bank.repo.AccountRepository;
import com.bank.repo.TransactionRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;
    private String email;
    private String password;
    private Account account;

    @BeforeEach
    void beforeEach() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        email = "abc@example.com";
        password = "Password@234";
        account = new Account(email, bCryptPasswordEncoder.encode(password));
        accountRepository.save(account);
    }

    @AfterEach
    void afterEach() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToCreditAmountInAccount() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(100));

        mockMvc.perform(post("/transaction/credit").with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldThrowErrorWhenAmountToBeCreditedIsLessThanOne() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(0));

        mockMvc.perform(post("/transaction/credit").with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeAbleToDebitAmountInAccount() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(100));

        mockMvc.perform(post("/transaction/debit").with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldThrowErrorWhenAmountToBeDebitedIsLessThanOne() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(0));

        mockMvc.perform(post("/transaction/debit").with(httpBasic(email, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAbleToGetAccountTransactionHistory() throws Exception {
        BigDecimal amount = new BigDecimal(100);
        Transaction firstTransaction = transactionRepository.save(new Transaction(account, new Date(), TransactionType.CREDIT, amount));
        Transaction secondTransaction = transactionRepository.save(new Transaction(account, new Date(), TransactionType.DEBIT, amount));
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(firstTransaction, secondTransaction));
        ArrayList<TransactionResponse> transactionResponses = new ArrayList<>();
        TransactionResponse transactionResponse = new TransactionResponse();
        for (Transaction transaction : transactions) {
            transactionResponses.add(transactionResponse.getTransactionResponse(transaction));
        }
        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(account.getId(), account.getEmail(), account.getAvail_bal(), transactionResponses);

        mockMvc.perform(get("/transaction").with(httpBasic(email, password)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionHistoryResponse)));

    }
}

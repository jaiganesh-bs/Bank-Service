package com.bank.view;

import com.bank.App;
import com.bank.controller.request.TransactionRequest;
import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.model.Account;
import com.bank.model.Transaction;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        mockMvc.perform(post("/transaction/credit").with(httpBasic(userAccount.getId(), password))
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

        mockMvc.perform(post("/transaction/credit").with(httpBasic(userAccount.getId(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeAbleToDebitAmountInAccount() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(100));
        transactionTypeRepository.save(new TransactionType("DEBIT"));

        mockMvc.perform(post("/transaction/debit").with(httpBasic(userAccount.getId(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldThrowErrorWhenAmountToBeDebitedIsLessThanOne() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(0));
        transactionTypeRepository.save(new TransactionType("DEBIT"));

        mockMvc.perform(post("/transaction/debit").with(httpBasic(userAccount.getId(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAbleToGetAccountTransactionHistory() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        TransactionType debit = transactionTypeRepository.save(new TransactionType("DEBIT"));
        TransactionType credit = transactionTypeRepository.save(new TransactionType("CREDIT"));

        BigDecimal amount = new BigDecimal(100);
        Transaction firstTransaction = transactionRepository.save(new Transaction(userAccount, new Date(), credit, amount, amount));
        Transaction secondTransaction = transactionRepository.save(new Transaction(userAccount, new Date(), debit, amount, account.getAvail_bal()));
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(firstTransaction, secondTransaction));
        ArrayList<TransactionResponse> transactionResponse = new ArrayList<TransactionResponse>();
        for (Transaction transaction : transactions) {
            transactionResponse.add(new TransactionResponse(transaction.getId(), transaction.getDate(), transaction.getTransactionType().getName(), transaction.getAmount(), transaction.getBalance()));
        }
        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(account.getId(), account.getName(), transactionResponse, account.getAvail_bal());

        mockMvc.perform(get("/transaction").with(httpBasic(account.getId(), password)))
                .andExpect(status().isOk())
                .andExpect(content().json(transactionHistoryResponse.toString()));

    }
}

package com.bank.view;

import com.bank.App;
import com.bank.controller.request.CreateAccountRequest;
import com.bank.controller.response.SummaryResponse;
import com.bank.model.Account;
import com.bank.repo.AccountRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void before() {
        accountRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToCreateAccountWhenNameAndPasswordIsGiven() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(name, password);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldBeAbleToGetAccountSummaryWhenValidAccountIdIsGiven() throws Exception {
        String name = "Jaiganesh";
        String password = "Password@234";
        Account account = new Account(name, bCryptPasswordEncoder.encode(password));
        Account userAccount = accountRepository.save(account);
        SummaryResponse summaryResponse = new SummaryResponse(userAccount.getId(), userAccount.getName(), userAccount.getAvail_bal());

        mockMvc.perform(get("/account/summary").with(httpBasic(userAccount.getId(), password)))
                .andExpect(status().isOk())
                .andExpect(content().json(summaryResponse.toString()));
    }
}

package com.bank.service;

import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.constants.TransactionType;
import com.bank.repo.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;


    public void credit(String email, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        if (isInValidAmount(amount)) throw new InvalidAmountException();
        Account account = accountService.credit(email, amount);
        Transaction credit = new Transaction(account, getCurrentDate(), TransactionType.CREDIT, amount);
        transactionRepository.save(credit);
    }


    private boolean isInValidAmount(BigDecimal amount) {
        int valid = 1;
        return amount.signum() != valid;
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public void debit(String accountId, BigDecimal amount) throws InvalidAmountException, AccountNotFoundException {
        if (isInValidAmount(amount)) throw new InvalidAmountException();
        Account account = accountService.debit(accountId, amount);
        Transaction debit = new Transaction(account, getCurrentDate(), TransactionType.DEBIT, amount);
        transactionRepository.save(debit);
    }

    public List<Transaction> getHistory(String email) {
        return transactionRepository.findByAccountEmail(email);
    }

    public TransactionHistoryResponse getAccountStatement(String email) throws AccountNotFoundException {
        List<Transaction> transactions = getHistory(email);
        Account account = accountService.getAccount(email);
        List<TransactionResponse> transactionResponses = getTransactionResponses(transactions);
        return new TransactionHistoryResponse(account.getId(), account.getEmail(), account.getAvail_bal(), transactionResponses);
    }

    private List<TransactionResponse> getTransactionResponses(List<Transaction> transactions) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        TransactionResponse transactionResponse = new TransactionResponse();
        for (Transaction transaction : transactions) {
            transactionResponses.add(transactionResponse.getTransactionResponse(transaction));
        }
        return transactionResponses;
    }
}

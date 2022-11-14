package com.bank.service;

import com.bank.controller.response.TransactionHistoryResponse;
import com.bank.controller.response.TransactionResponse;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repo.TransactionRepository;
import com.bank.repo.TransactionTypeRepository;
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
    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    public Transaction credit(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        if(isValidAmount(amount)) throw new InvalidAmountException();
        Account account = accountService.credit(accountId, amount);
        Transaction credit = new Transaction(account, getToday(), transactionTypeRepository.findByName("CREDIT"), amount, account.getAvail_bal());
        return transactionRepository.save(credit);
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount.signum() != 1;
    }

    private Date getToday() {
        return new Date();
    }

    public Transaction debit(String accountId, BigDecimal amount) throws InvalidAmountException, AccountNotFoundException {
        if(isValidAmount(amount)) throw new InvalidAmountException();
        Account account = accountService.debit(accountId, amount);
        Transaction debit = new Transaction(account, getToday(), transactionTypeRepository.findByName("DEBIT"), amount, account.getAvail_bal());
        return transactionRepository.save(debit);
    }

    public List<Transaction> getHistory(String accountId) {
        List<Transaction> transactions = transactionRepository.findByAccount_id(accountId);
        return  transactions;
    }

    public TransactionHistoryResponse getAccountStatement(String id) throws AccountNotFoundException {
        List<Transaction> transactions = getHistory(id);
        Account account = accountService.getAccount(id);
        List<TransactionResponse> transactionResponse = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionResponse.add(new TransactionResponse(transaction.getId(),transaction.getDate(), transaction.getTransactionType().getName(), transaction.getAmount(), transaction.getBalance()));
        }
        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse(account.getId(), account.getName(), transactionResponse, account.getAvail_bal());


        return transactionHistoryResponse;
    }
}

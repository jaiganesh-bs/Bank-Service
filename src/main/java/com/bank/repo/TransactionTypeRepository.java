package com.bank.repo;

import com.bank.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
    TransactionType findByName(String credit);
}

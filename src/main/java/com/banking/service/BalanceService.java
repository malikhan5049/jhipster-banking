package com.banking.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.domain.BankAccount;
import com.banking.domain.Operation;
import com.banking.repository.BankAccountRepository;
import com.banking.repository.OperationRepository;

@Service
@Transactional
public class BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceService.class);

    @Inject
    private OperationRepository operationRepository;
    @Inject
    private BankAccountRepository bankAccountRepository;

    public void add(final Operation operation) {
        operationRepository.save(operation);
        BankAccount bankAccount = bankAccountRepository.getOne(operation.getBankAccount().getId());
        bankAccount.setBalance(bankAccount.getBalance().add(operation.getAmount()));

    }

}

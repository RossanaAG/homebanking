package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.TransactionDTO;
import com.ap.homebanking.enums.TransactionType;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber
    )
    {
        if (amount == 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        } else if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("Origin and Destination Accounts are equals", HttpStatus.FORBIDDEN);
        }

        Account originAccount = accountRepository.findByNumber(fromAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(toAccountNumber);
        Client client = clientRepository.findByEmail(authentication.getName());

        if (originAccount == null){
            return new ResponseEntity<>("Origin Account Not Exist", HttpStatus.FORBIDDEN);
        }

        if (destinationAccount == null){
            return new ResponseEntity<>("destination Account Not Exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("The Client is not the owner of Origin Account", HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("Origin Account without enough balance", HttpStatus.FORBIDDEN);
        }


        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount,description, LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description, LocalDateTime.now());
        double originBalance = originAccount.getBalance() - amount;
        double destinationBalance = (destinationAccount.getBalance() + amount);
        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);
        originAccount.setBalance(originBalance);
        destinationAccount.setBalance(destinationBalance);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
    }

}
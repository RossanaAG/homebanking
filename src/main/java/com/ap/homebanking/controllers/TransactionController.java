package com.ap.homebanking.controllers;

import com.ap.homebanking.enums.TransactionType;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import com.ap.homebanking.services.TransactionService;
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
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber
    )
    {
        if (amount == 0)
            return new ResponseEntity<>("Transaction Amount is Zero", HttpStatus.FORBIDDEN);


        if (description.isBlank())
            return new ResponseEntity<>("Transaction Description is blank", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.isBlank())
            return new ResponseEntity<>("Origin Account is blank", HttpStatus.FORBIDDEN);

        if (toAccountNumber.isBlank())
            return new ResponseEntity<>("Destination Account is blank", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.equals(toAccountNumber))
            return new ResponseEntity<>("Origin and Destination Accounts are equals", HttpStatus.FORBIDDEN);

        Account originAccount = accountService.findByNumber(fromAccountNumber);
        Account destinationAccount = accountService.findByNumber(toAccountNumber);
        Client client = clientService.findByEmail(authentication.getName());

        if (originAccount == null)
            return new ResponseEntity<>("Origin Account Does Not Exist", HttpStatus.FORBIDDEN);

        if (!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("The Client " + client.getEmail()
                    + " is Not the Owner of Origin Account", HttpStatus.FORBIDDEN);
        }

        if (destinationAccount == null)
            return new ResponseEntity<>("We couldn't find the destination account.", HttpStatus.FORBIDDEN);

        if (originAccount.getBalance() < amount)
            return new ResponseEntity<>("Insufficient balance in the origin account.", HttpStatus.FORBIDDEN);


        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount,description + " [" + originAccount.getNumber() + "]", LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description + " [" + destinationAccount.getNumber() + "]", LocalDateTime.now());
        double originBalance = originAccount.getBalance() - amount;
        double destinationBalance = (destinationAccount.getBalance() + amount);
        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);
        originAccount.setBalance(originBalance);
        destinationAccount.setBalance(destinationBalance);
        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinationAccount);
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
    }

}
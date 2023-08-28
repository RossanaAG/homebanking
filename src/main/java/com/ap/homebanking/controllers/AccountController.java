package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.tools.tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);
        if(account != null) return new AccountDTO(account);
        return null;
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentAccounts(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    // Crea una nueva cuenta para el cliente autenticado
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());

        // Crea una nueva cuenta para el cliente si el cliente tiene menos de 3 cuentas
        if (client.getAccounts().size() < 3) {
            String number = tools.generateAccountNumber(accountRepository.findAll());
            Account account= new Account(number, LocalDate.now(), 0, client);

            accountRepository.save(account);
            return new ResponseEntity<>("Client's account has been successfully added " + account.getClient().getEmail() , HttpStatus.CREATED);

        }
        return new ResponseEntity<>("We've hit the limit for the number of accounts allowed.", HttpStatus.FORBIDDEN);
    }

    @GetMapping("clients/current/accounts/{id}")
    public AccountDTO getCurrentAccount(Authentication authentication, @PathVariable Long id){

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        if ((account != null) && (client.getAccounts().contains(account))){
            return new AccountDTO(account);
        }
        return null;
    }

}
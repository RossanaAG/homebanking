package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface AccountService {


    List<Account> findAllAccounts();

    List<AccountDTO> convertToAccountsDTO(List<Account> accounts);

    Account findById(Long Id);

    Account findByNumber(String number);

    boolean existsByNumber(String number);

    boolean existsByClientAndNumber(Client client, String number);

    void saveAccount(Account account);

}
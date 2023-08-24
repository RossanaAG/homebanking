package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;

    public ClientDTO(Client client) {
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        accounts = client.getAccounts()
                .stream().map(AccountDTO::new)
                .collect(Collectors.toSet());
        loans = client.getLoans()
                .stream().map(ClientLoanDTO::new)
                .collect(Collectors.toList());
        cards = client.getCards()
                .stream().map(CardDTO::new)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public List<ClientLoanDTO> getLoans() {
        return loans;
    }
    public List<CardDTO> getCards() {
        return cards;
    }
}
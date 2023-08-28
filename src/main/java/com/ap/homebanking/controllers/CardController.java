package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.enums.CardColor;
import com.ap.homebanking.enums.CardType;
import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
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
@RequestMapping("api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCurrentCards(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType
    )
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        int creditCardsCount = 0, debitCardsCount = 0;

        for(Card card:client.getCards()){
            if(card.getType().equals(CardType.CREDIT)) creditCardsCount++;
            else debitCardsCount++;
        }

        if ((cardType == CardType.CREDIT && creditCardsCount < 3) || (cardType == CardType.DEBIT && debitCardsCount < 3)){
            String cardHolder = client.getFirstName() + " " + client.getLastName();
            String number = tools.generateCardNumber(cardRepository.findAll());
            short cvv = (short)(100 + Math.random() * 899);
            Card card = new Card(cardHolder, cardType, cardColor, number, cvv, LocalDate.now(),LocalDate.now().plusYears(5));
            client.addCard(card);
            cardRepository.save(card);
            clientRepository.save(client);
            return new ResponseEntity<>("Client's card has been successfully added " + card.getCardHolder(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>("We've hit the maximum number of cards of the same type allowed", HttpStatus.FORBIDDEN);
    }

}

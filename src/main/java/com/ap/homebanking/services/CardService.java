package com.ap.homebanking.services;

import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.enums.CardColor;
import com.ap.homebanking.enums.CardType;
import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;

import java.util.List;

public interface CardService {

    List<Card> findAllCards();

    Card findById(Long id);

    List<CardDTO> convertToCardDTO(List<Card> cards);

    Long countByClientAndType(Client client, CardType cardType);

    boolean existsByClientAndTypeAndColor( Client client,CardType cardType, CardColor cardColor);

    boolean existsByNumber(String number);

    void saveCard(Card card);
}
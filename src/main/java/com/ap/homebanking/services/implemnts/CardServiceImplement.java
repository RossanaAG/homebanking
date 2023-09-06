package com.ap.homebanking.services.implemnts;

import com.ap.homebanking.dtos.CardDTO;
import com.ap.homebanking.enums.CardColor;
import com.ap.homebanking.enums.CardType;
import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public List<CardDTO> convertToCardDTO(List<Card> cards) {
        return cards.stream().map(CardDTO :: new).collect(Collectors.toList());
    }

    @Override
    public Long countByClientAndType(Client client, CardType cardType) {
        return cardRepository.countByClientAndType(client, cardType);
    }

    @Override
    public boolean existsByClientAndTypeAndColor(Client client, CardType cardType, CardColor cardColor) {
        return cardRepository.existsByClientAndTypeAndColor(client, cardType, cardColor);
    }

    @Override
    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}

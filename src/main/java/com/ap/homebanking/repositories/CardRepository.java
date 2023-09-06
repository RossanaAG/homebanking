package com.ap.homebanking.repositories;

import com.ap.homebanking.enums.CardColor;
import com.ap.homebanking.enums.CardType;
import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByClientAndTypeAndColor(Client client, CardType type, CardColor color);

    Long countByClientAndType(Client client,CardType type);


}
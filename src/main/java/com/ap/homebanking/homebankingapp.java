package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class homebankingapp {

    public static void main(String[] args) {
        SpringApplication.run(homebankingapp.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
        return (args -> {
            Client client_1 = new Client("Melba","Morel","melba@mindhub.com");
            clientRepository.save(client_1);

            Account account_1 = new Account(client_1,"VIN001", LocalDateTime.now(),5000 );
            Account account_2 = new Account(client_1,"VIN002", LocalDateTime.now().plusDays(1),7500 );
            accountRepository.save(account_1);
            accountRepository.save(account_2);
        });
    }
}
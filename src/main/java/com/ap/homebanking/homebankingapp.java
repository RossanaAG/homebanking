package com.ap.homebanking;

import com.ap.homebanking.enums.CardColor;
import com.ap.homebanking.enums.CardType;
import com.ap.homebanking.enums.TransactionType;
import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class homebankingapp {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(homebankingapp.class, args);
    }
    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
        return (args) -> {
            //Creación de clientes
            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
            Client client2 = new Client("Leo","Messi","leo.messi@mindhub.com", passwordEncoder.encode("18122022"));

            //Creo Administrador
            Client admin = new Client("Admin", "Arg", "administrador@mindhub.com", passwordEncoder.encode("Admin1234"));

            //Se guarda clientes
            clientRepository.save(admin);
            clientRepository.save(client1);
            clientRepository.save(client2);

            // Creación de cuentas
            Account account1 = new Account("VIN-11234567", LocalDate.now().minusDays(2),5000);
            Account account2 = new Account("VIN-77654321", LocalDate.now().minusDays(1),7500);


            // Agregar cuenta al cliente
            client1.addAccount(account1);
            client1.addAccount(account2);


            // Se guarda cuenta
            accountRepository.save(account1);
            accountRepository.save(account2);


            //Transacciones
            Transaction transaction1 = new Transaction(TransactionType.CREDIT,17000,"Salario", LocalDateTime.now());
            Transaction transaction2 = new Transaction(TransactionType.DEBIT,-2000,"Cuota Tarjeta", LocalDateTime.now());
            Transaction transaction3 = new Transaction(TransactionType.CREDIT,4000,"Venta", LocalDateTime.now());
            Transaction transaction4 = new Transaction(TransactionType.DEBIT,-1000,"Refacciones", LocalDateTime.now());


            // Agregando transacciones a la cuenta
            account1.addTransaction(transaction1);
            account1.addTransaction(transaction2);
            account1.addTransaction(transaction3);
            account1.addTransaction(transaction4);

            //Guardado de Transacciones
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);




            // Crear préstamos
            Loan loan1 = new Loan("Hipotecario",500000, List.of(12,24,36,48,60));
            Loan loan2 = new Loan("Personal",100000, List.of(6,12,24));
            Loan loan3 = new Loan("Automotriz",300000, List.of(6,12,24,36));

            // Guardar préstamos
            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            //Crear Préstamos CLiente
            ClientLoan clientLoan1 = new ClientLoan(400000,60);
            ClientLoan clientLoan2 = new ClientLoan(50000,12);
            ClientLoan clientLoan3 = new ClientLoan(100000,24);
            ClientLoan clientLoan4 = new ClientLoan(200000,36);

            // Añadir cliente a préstamos
            client1.addLoan(clientLoan1);
            client1.addLoan(clientLoan2);
            client2.addLoan(clientLoan3);
            client2.addLoan(clientLoan4);

            //Añadir cliente a préstamos de cliente
            loan1.addClient(clientLoan1);
            loan2.addClient(clientLoan2);
            loan2.addClient(clientLoan3);
            loan3.addClient(clientLoan4);

            // Guardar préstamo de cliente
            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);


            //Para crear las tarjetas para CLiente, datos a tener en cuenta:
            //Nombre del Titular, Nro de Tarjeta, Fecha de Inicio de Validez, Fecha de Vencimiento, CVV de 3 dígitos
            String name1 = client1.getFirstName() + " " + client1.getLastName();
            Card card1 = new Card(name1, CardType.DEBIT, CardColor.GOLD, "1111-2222-3333-4444", (short)123, LocalDate.now(), LocalDate.now().plusYears(5));
            Card card2 = new Card(name1, CardType.CREDIT, CardColor.TITANIUM, "9999-8888-7777-6666", (short)456, LocalDate.now(), LocalDate.now().plusYears(5));
            String name2 = client2.getFirstName() + " " + client2.getLastName();
            Card card3 = new Card(name2, CardType.CREDIT, CardColor.SILVER, "33551-1030-2022-118", (short)789, LocalDate.now().minusYears(1), LocalDate.now().plusYears(4));

            // Agregamos Tarjeta a Cliente
            client1.addCard(card1);
            client1.addCard(card2);
            client2.addCard(card3);

            // Guardamos Tarjeta en su repositorio
            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);


        };
    }
}

//No olvidar que se crea se agrega y se guarda cada transacción realizada





//Tampoco creer que Leo necesita un préstamo xD

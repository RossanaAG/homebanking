package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class homebankingapp {

    public static void main(String[] args) {

        SpringApplication.run(homebankingapp.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository) {
        return (args) -> {
            //Creación de clientes
            Client client1 = new Client("Melba", "Morel","melba@maindhub.com");
            Client client2 = new Client("Leo","Messi","leo.messi@mindhub.com");

            //Se guarda clientes
            clientRepository.save(client1);
            clientRepository.save(client2);

            // Creación de cuentas
            Account account1 = new Account("VIN001", LocalDate.now().minusDays(2),5000);
            Account account2 = new Account("VIN002", LocalDate.now().minusDays(1),7500);
            Account account3 = new Account("VIN003", LocalDate.now().minusDays(3),8000);


            // Agregar cuenta al cliente
            client1.addAccount(account1);
            client1.addAccount(account2);
            client2.addAccount(account3);

            // Se guarda cuenta
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);

            //Transacciones
            Transaction transaction1 = new Transaction(TransactionType.CREDIT,1700,"Intereses", LocalDateTime.now());
            Transaction transaction2 = new Transaction(TransactionType.DEBIT,-2250,"Servicio de Internet", LocalDateTime.now());

            // Agregando transacciones a la cuenta
            account1.addTransaction(transaction1);
            account1.addTransaction(transaction2);

            //Guardado de Transacciones
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);

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


        };
    }
}

//No olvidar que se crea se agrega y se guarda cada transacción realizada





//Tampoco creer que Leo necesita un préstamo xD

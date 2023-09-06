package com.ap.homebanking.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {

    static public String generateAccountNumber()
    {
        // Se crea un número de cuenta aleatorio en formato "VIN-XXXXXXXX"
        return "VIN-" + String.format("%08d", 11111111 + (int)(Math.random() * 88888888));
    }

    static public String generateCardNumber()
    {
        String[] cardNumber = new String[4];
        StringBuilder number = new StringBuilder();
        for(int i=0;i<4;i++){
            // Se generan cuatro partes del número de tarjeta, cada una con cuatro dígitos
            cardNumber[i] = String.format("%04d", 1000 + (int)(Math.random() * 8999));
            number.append(cardNumber[i]);
            if (i<=2) number.append("-");
        }
        return number.toString();
    }

    static private final  Pattern EMAIL_PATTERN = Pattern.compile(
            //Verifica direcciones de correo electrónico válidas (nota oara mi: ver apuntes)
            "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{2,6}$"
    );

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}

/*
public class tools {
    //Genera un número de cuenta aleatorio que no existe en la lista de cuentas
   static public String generateAccountNumber(List<Account> accounts)
    {
        String number;
        boolean check;

        do {
            check=true;

            // Genera un número de cuenta aleatorio en el formato "VIN-xxxxxxxx"
            number = "VIN-" + String.format("%08d", 11111111 + (int)(Math.random() * 88888888));

            // Comprueba si el número de cuenta generado ya existe en la lista de cuentas
            for(Account account:accounts)
            {
                if (account.getNumber().equals(number)) {
                    check = false;
                    break;
                }
            }
        } while(!check);

        return number;
    }

    // Genera un número de tarjeta aleatorio que no existe en la lista de tarjetas
    static public String generateCardNumber(List<Card> cards)
    {
        String[] cardNumber = new String[4];
        String number = "";
        boolean check;
        do {
            check=true;
            // Genera cuatro bloques de números de 4 dígitos cada uno y los enlaza con -
            for(int i=0;i<4;i++){
                cardNumber[i] = String.format("%04d", 1000 + (int)(Math.random() * 8999));
                number += cardNumber[i];
                if (i<=2) number += "-";
            }
            // Comprueba si el número de tarjeta generado ya existe en la lista de tarjetas
            for(Card card:cards)
            {
                if (card.getNumber().equals(number)) {
                    check = false;
                    break;
                }
            }
        } while(!check);

        return number;
    }
}*/

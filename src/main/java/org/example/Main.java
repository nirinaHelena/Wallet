package org.example;

import org.example.DAO.AccountDAO;
import org.example.DAO.CurrencyDAO;
import org.example.DAO.TransactionDAO;
import org.example.Service.BalanceCalculator;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        {
            CurrencyDAO currencyDAO = new CurrencyDAO();
            AccountDAO accountDAO = new AccountDAO();
            TransactionDAO transactionDAO = new TransactionDAO();
            /**
             // Création de quelques devises
             Currency ariary = new Currency(null,"Ariary", "MGA");
             Currency usd = new Currency(null,"Dollar", "USD");
             */

            // voir la liste de currency
            Currency MGA = new Currency(1, "Ariary", "MGA");
            Currency USD = new Currency(2, "Dollar", "USD");
            System.out.println("=".repeat(30));
            System.out.println("enregistrer un currency");
            /**
             // Enregistrement des devises dans la base de données
             currencyDAO.saveAll(List.of(ariary, usd));
             */
            System.out.println("=".repeat(30));
            // find all currency
            System.out.println("find all currency");
            System.out.println(currencyDAO.findAll());
            System.out.println("=".repeat(30));
            System.out.println("créer des comptes");
            //enregistrer quelques comptes
            Account account1 = new Account("courant", 2, "banque");
            Account account2 = new Account("courant", 1, "banque");

            // Enregistrement des comptes dans la base de données
            accountDAO.saveAll(List.of(account1, account2));

            System.out.println("=".repeat(30));

            // afficher tous les comptes enregistrer
            System.out.println("find all account");
            System.out.println(accountDAO.findAll());
            System.out.println("=".repeat(30));


            // Création de quelques transactions
            System.out.println("=".repeat(30));
            System.out.println("save transaction");

            Transaction transaction1 = new Transaction(4, "vente",
                    100.0, null, "credit", 1);

            Transaction transaction2 = new Transaction(4, "achat",
                    5.0, null, "debit", 1);
            // Enregistrement des transactions dans la base de données
            System.out.println("save transaction 1");
            transactionDAO.save(transaction1);
            transactionDAO.save(transaction2);

            System.out.println("=".repeat(30));
            List <Account> allAccounts = accountDAO.findAll();
            // Affichage du solde actuel de chaque compte
            System.out.println("\nSolde actuel de chaque compte : ");
            for (Account account : allAccounts) {
                double currentBalance = accountDAO.currentBalance(account1.getAccountId());
                System.out.println("Solde de " + account.getAccountName() + ": " + currentBalance);
            }

            System.out.println("=".repeat(30));

            // Affichage des transactions de chaque compte
            System.out.println("Transactions de chaque compte : ");
            for (Account account : allAccounts) {
                List<Transaction> transactions = transactionDAO.findAllAtDate(account.getAccountId());
                System.out.println("Transactions de " + account.getAccountName() + ": ");
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            }
            System.out.println("=".repeat(30));
            System.out.println("la balance de account1");
        }

            System.out.println("=".repeat(30));
        BalanceCalculator balanceCalculator = new BalanceCalculator();

        // Exemple d'utilisation de la fonction calculateBalance
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-12-31T23:59:59");

        double totalBalance = balanceCalculator.calculateBalance(5, startDate, endDate);
        System.out.println("Total Balance: " + totalBalance);

        // Exemple d'utilisation de la fonction calculateCategoryAmounts
        Map<String, Double> categoryAmounts = balanceCalculator.calculateCategoryAmounts(5, startDate, endDate);
        System.out.println("Restaurant Amount: " + categoryAmounts.get("restaurant"));
        System.out.println("Salaire Amount: " + categoryAmounts.get("salaire"));
    }
}


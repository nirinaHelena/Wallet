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
        CurrencyDAO currencyDAO = new CurrencyDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        // Création de quelques devises
        Currency ariary = new Currency(null,"Ariary", "MGA");
        Currency usd = new Currency(null,"Dollar", "USD");

        // Enregistrement des devises dans la base de données
        currencyDAO.saveAll(List.of(ariary, usd));

        // Création de quelques comptes
        Account account1 = new Account("Compte 1", ariary, "banque");
        Account account2 = new Account("Compte 2", usd, "banque");

        // Enregistrement des comptes dans la base de données
        accountDAO.saveAll(List.of(account1, account2));

        // Création de quelques transactions
        Transaction transaction1 = new Transaction(account1.getAccountId(), "Achat", 100.0, LocalDateTime.now(), "debit");
        Transaction transaction2 = new Transaction(account2.getAccountId(), "Vente", 50.0, LocalDateTime.now(), "credit");

        // Enregistrement des transactions dans la base de données
        transactionDAO.save(transaction1, account1, 10);
        transactionDAO.save(transaction2, account2, 8.5);

        // Affichage de tous les comptes
        List<Account> allAccounts = accountDAO.findAll();
        System.out.println("Liste de tous les comptes : ");
        for (Account account : allAccounts) {
            System.out.println(account);
        }

        // Affichage du solde actuel de chaque compte
        System.out.println("\nSolde actuel de chaque compte : ");
        for (Account account : allAccounts) {
            double currentBalance = accountDAO.currentBalance(account.getAccountId());
            System.out.println("Solde de " + account.getAccountName() + ": " + currentBalance);
        }

        // Affichage des transactions de chaque compte
        System.out.println("\nTransactions de chaque compte : ");
        for (Account account : allAccounts) {
            List<Transaction> transactions = transactionDAO.findAll(account.getAccountId());
            System.out.println("Transactions de " + account.getAccountName() + ": ");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }

        BalanceCalculator balanceCalculator = new BalanceCalculator();

        // Exemple d'utilisation de la fonction calculateBalance
        UUID accountId = UUID.fromString("your_account_id");
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-12-31T23:59:59");

        double totalBalance = balanceCalculator.calculateBalance(accountId, startDate, endDate);
        System.out.println("Total Balance: " + totalBalance);

        // Exemple d'utilisation de la fonction calculateCategoryAmounts
        Map<String, Double> categoryAmounts = balanceCalculator.calculateCategoryAmounts(accountId, startDate, endDate);
        System.out.println("Restaurant Amount: " + categoryAmounts.get("restaurant"));
        System.out.println("Salaire Amount: " + categoryAmounts.get("salaire"));
    }
    }
}

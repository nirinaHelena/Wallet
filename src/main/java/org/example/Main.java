package org.example;

import org.example.DAO.AccountDAO;
import org.example.DAO.CurrencyDAO;
import org.example.DAO.TransactionDAO;
import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
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
        Transaction transaction1 = new Transaction(4, "vente", 100.0, null, "credit");
        Transaction transaction2 = new Transaction(4, "achat", 5.0, null, "debit");
        // Enregistrement des transactions dans la base de données
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
        System.out.println("\nTransactions de chaque compte : ");
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
}

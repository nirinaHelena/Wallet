package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AccountDAO implements DAOInterface<Account>{
    private final DatabaseConnection connection;
    private final AmountDAO amountDAO;
    private final TransactionDAO transactionDAO;

    public AccountDAO() {
        this.connection = new DatabaseConnection();
        this.amountDAO = new AmountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // Trouve tous les comptes sans leurs montants et transactions associées


    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<>();


        String sql = "SELECT * FROM account;";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                accountList.add(new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("account_name"),
                        resultSet.getInt("account_currency"),
                        resultSet.getString("account_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }


    // Enregistre une liste de comptes avec nom, devise et type
    public List<Account> saveAll(List<Account> toSave) {
        List<Account> currencyList = new ArrayList<>();

        for (Account account : toSave) {
            Account saveAccount = save(account);
            if (saveAccount != null) {
                currencyList.add(saveAccount);
            }
        }
        return currencyList;
    }

    @Override
    public Account save(Account toSave) {
        String sql = "INSERT INTO account (account_name, account_currency, account_type) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, toSave.getAccountName());
            preparedStatement.setInt(2, toSave.getCurrency());
            preparedStatement.setString(3, toSave.getAccountType());

            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Crée un nouvel objet Account avec l'ID généré
                        Account savedAccount = new Account(
                                toSave.getAccountName(),
                                toSave.getCurrency(),
                                toSave.getAccountType()
                        );

                        return savedAccount;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Retourne null en cas d'échec de l'enregistrement
    }


    // Affiche le solde actuel
    public double currentBalance(int accountId) {
        double amount = 0;

        String sql = "SELECT amount FROM amount WHERE account_id = ? ORDER BY datetime DESC LIMIT 1;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    amount = resultSet.getDouble("amount");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    // Affiche un compte avec tous les paramètres
    public List<Account> findAccount(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        List<Account> accountList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    accountList.add(new Account(
                            resultSet.getInt("account_id"),
                            resultSet.getString("account_name"),
                            amountDAO.findLastAmount(accountId),
                            transactionDAO.findAllAtDate(accountId),
                            resultSet.getInt("account_currency"),
                            resultSet.getString("account_type")
                    ));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    // Affiche le solde à une date donnée
    public double balanceAtADate(UUID accountId, LocalDateTime dateTime) {
        double amount = 0;

        String sql = "SELECT amount FROM amount WHERE account_id = ? AND datetime <= ? ORDER BY datetime DESC LIMIT 1;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, Timestamp.valueOf(dateTime));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    amount = resultSet.getDouble("amount");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }


    // Affiche le solde d'un compte entre deux dates
    public double balanceHistory(UUID accountId, LocalDateTime startDate, LocalDateTime endDate) {
        double amount = 0;
        String sql = "SELECT amount FROM amount WHERE account_id = ? AND datetime BETWEEN ? AND ?;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, Timestamp.valueOf(startDate));
            preparedStatement.setObject(3, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    amount += resultSet.getDouble("amount");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public <ExchangeRateCalculationMethod> double currentBalance(int accountId, LocalDateTime date, ExchangeRateCalculationMethod method) {
        double exchangeRate;

        if (method.equals(method)) {
            exchangeRate = amountDAO.weightedAverageExchange(accountId, date);
        } else if (method.equals(method)) {
            exchangeRate = amountDAO.getMinimumExchangeRate(accountId, date);
        } else if (method.equals(method)) {
            exchangeRate = amountDAO.getMaximumExchangeRate(accountId, date);
        } else if (method.equals(method)) {
            exchangeRate = amountDAO.getMedianExchangeRate(accountId, date);
        } else {
            throw new IllegalArgumentException("Invalid ExchangeRateCalculationMethod");
        }

        // Obtenez le solde actuel de l'account, ajusté en fonction du taux de change
        double currentBalance = amountDAO.findLastAmount(accountId).getAmount();
        double adjustedBalance = currentBalance * exchangeRate;

        return adjustedBalance;
    }


}

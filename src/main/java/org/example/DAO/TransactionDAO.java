package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Amount;
import org.example.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TransactionDAO {

    private AmountDAO amountDAO;
    private AccountDAO accountDAO;
    private DatabaseConnection connection;

    public TransactionDAO() {
        this.connection = new DatabaseConnection();
    }

    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transaction ;";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                transactionList.add(new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("account_id"),
                        resultSet.getString("transaction_label"),
                        resultSet.getDouble("transaction_amount"),
                        resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                        resultSet.getString("transaction_type"),
                        resultSet.getInt("transaction_category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    // find all transactions of an account
    public List<Transaction> findAllAtDate(int accountId) {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transaction where account_id = ? ;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactionList.add(new Transaction(
                            resultSet.getInt("transaction_id"),
                            resultSet.getInt("account_id"),
                            resultSet.getString("transaction_label"),
                            resultSet.getDouble("transaction_amount"),
                            resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                            resultSet.getString("transaction_type"),
                            resultSet.getInt("transaction_category")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }
    public String  save(Transaction toSave) {
        String sql = "INSERT INTO transaction (account_id, transaction_label, transaction_amount, transaction_type, category_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, toSave.getAccountId());
            preparedStatement.setString(2, toSave.getTransactionLabel());
            preparedStatement.setDouble(3, toSave.getAmount());
            preparedStatement.setString(4, toSave.getTransactionType());
            preparedStatement.setInt(5, toSave.getCategory());
            // amount * exchange rate



            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                if (Objects.equals(toSave.getTransactionType(), "debit")){
                    // TODO: check account exchangeRate
                    Double debitAmount= amountDAO.findLastAmount(toSave.getAccountId()).getAmount()
                            - toSave.getAmount();

                    Amount newAmount = new Amount(toSave.getAccountId(),debitAmount,
                            null);
                    amountDAO.save(newAmount);
                    return "Vous avez retirer avec succès votre compte de : "+ toSave.getAmount()
                            +"votre nouveau solde est de : " + amountDAO.findLastAmount(toSave.getAccountId())
                            + " merci de votre confiance";
                }
                if (Objects.equals(toSave.getTransactionType(), "credit")){
                    // TODO: check account exchangeRate
                    Double creditAmount= amountDAO.findLastAmount(toSave.getAccountId()).getAmount()
                            + toSave.getAmount();

                    Amount newAmount = new Amount(toSave.getAccountId(),creditAmount,
                            null);
                    amountDAO.save(newAmount);
                    return "Votre nouveau solde est de : " + amountDAO.findLastAmount(toSave.getAccountId())
                            + " merci de votre confiance";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Transaction> findAllAtDate(UUID accountId, LocalDateTime date) {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE account_id = ? AND DATE(transaction_date_hour) = ?;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, date.toLocalDate());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactionList.add(new Transaction(
                            resultSet.getInt("transaction_id"),
                            resultSet.getInt("account_id"),
                            resultSet.getString("transaction_label"),
                            resultSet.getDouble("transaction_amount"),
                            resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                            resultSet.getString("transaction_type"),
                            resultSet.getInt("transaction_category")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }
    // Retourne toutes les transactions dans la plage de dates donnée
    public List<Transaction> findAll(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE account_id = ? AND transaction_date_hour BETWEEN ? AND ?;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, startDate);
            preparedStatement.setObject(3, endDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactionList.add(new Transaction(
                            resultSet.getInt("transaction_id"),
                            resultSet.getInt("account_id"),
                            resultSet.getString("transaction_label"),
                            resultSet.getDouble("transaction_amount"),
                            resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                            resultSet.getString("transaction_type"),
                            resultSet.getInt("transaction_category")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }
}

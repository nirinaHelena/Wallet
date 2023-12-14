package org.example.DAO;

import org.example.model.Account;
import org.example.model.Amount;
import org.example.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDAO {

    private AmountDAO amountDAO;
    private AccountDAO accountDAO;
    private Connection connection;

    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM \"transaction\" ;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                transactionList.add(new Transaction(
                        (UUID) resultSet.getObject("transaction_id"),
                        (UUID) resultSet.getObject("account_id"),
                        resultSet.getString("transaction_label"),
                        resultSet.getDouble("transaction_amount"),
                        resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                        resultSet.getString("transaction_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    // find all transactions of an account
    public List<Transaction> findAll(UUID accountId) {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM \"transaction\" where account_id = ? ;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    transactionList.add(new Transaction(
                            (UUID) resultSet.getObject("transaction_id"),
                            (UUID) resultSet.getObject("account_id"),
                            resultSet.getString("transaction_label"),
                            resultSet.getDouble("transaction_amount"),
                            resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                            resultSet.getString("transaction_type")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    public Account save(Transaction toSave, Account account, double exchangeRate) {
        String sql = "INSERT INTO \"transaction\" (account_id, transaction_label, transaction_amount, transaction_type, exchange_rate) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, account.getAccountId());
            preparedStatement.setString(2, toSave.getTransactionLabel());
            preparedStatement.setDouble(3, toSave.getAmount());
            preparedStatement.setString(4, toSave.getTransactionType());
            preparedStatement.setDouble(5, toSave.getAmount() * exchangeRate);



            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        UUID generatedTransactionId = UUID.fromString(generatedKeys.getString(1));
                        return account;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

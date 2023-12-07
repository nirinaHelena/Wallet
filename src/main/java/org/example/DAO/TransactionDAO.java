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
    private Connection connection;

    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM \"transaction\" ; " ;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                transactionList.add(new Transaction(
                        (UUID) resultSet.getObject("transaction_id"),
                        (UUID) resultSet.getObject("account_id"),
                        resultSet.getString("transaction_label"),
                        resultSet.getDouble("transaction_amount"),
                        resultSet.getTimestamp("transaction_date_hour").toLocalDateTime(),
                        resultSet.getString("transaction_type")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return transactionList;
    }

    // parameter of account: id, accountName, currency, accountType
    public Account save(Transaction toSave, Account account) {
        /**
         * check the type of the transaction
         * check the type of the account
         */
        String sql = "INSERT INTO \"transaction\" (account_id, transaction_label," +
                "transaction_amount, transaction_type) value" +
                "(?, ?, ?, ?)";

        // for transaction type debit
        if (toSave.getTransactionType() == "debit") {
            // if the account type is bank
            if (account.getAccountType() == "banque") {

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setObject(1, account.getAccountId());
                    preparedStatement.setString(2, toSave.getTransactionLabel());
                    preparedStatement.setDouble(3, toSave.getAmount());
                    preparedStatement.setString(4, toSave.getTransactionType());

                    int rowAdded = preparedStatement.executeUpdate();
                    Amount newAmount;
                    newAmount = new Amount(account.getAmount().getAmount() - toSave.getAmount());
                    amountDAO.save(newAmount, account.getAccountId());

                    if (rowAdded > 0) {
                        // TODO: get the last amount of an account
                        // all attribut of account
                        return account;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // else
            else {
                if (account.getAmount().getAmount() - toSave.getAmount() > 0) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setObject(1, account.getAccountId());
                        preparedStatement.setString(2, toSave.getTransactionLabel());
                        preparedStatement.setDouble(3, toSave.getAmount());
                        preparedStatement.setString(4, toSave.getTransactionType());

                        int rowAdded = preparedStatement.executeUpdate();
                        Amount newAmount;
                        newAmount = new Amount(account.getAmount().getAmount() - toSave.getAmount());
                        amountDAO.save(newAmount, account.getAccountId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return account;
                } else {
                    return null;
                }
            }
        }
        if (toSave.getTransactionType() == "credit") {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, account.getAccountId());
                preparedStatement.setString(2, toSave.getTransactionLabel());
                preparedStatement.setDouble(3, toSave.getAmount());
                preparedStatement.setString(4, toSave.getTransactionType());

                int rowAdded = preparedStatement.executeUpdate();
                Amount newAmount;
                newAmount = new Amount(account.getAmount().getAmount() + toSave.getAmount());
                amountDAO.save(newAmount, account.getAccountId());

                if (rowAdded > 0) {
                    // TODO: get the last amount of an account
                    // all attribut of account
                    return account;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;


        // TODO: create a function to transfer money between two account
    }
}

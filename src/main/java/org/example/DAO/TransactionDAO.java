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

    public Account save(Transaction toSave, Account account) {
        /**
         * check the type of the transaction
         * check the type of the account
         */
        Amount amount = new Amount(toSave.getAmount());
        // for transaction type debit
        if (toSave.getTransactionType() == "debit"){
            if (account.getAccountType() == "banque"){
                String sql = "INSERT INTO \"transaction\" (account_id, transaction_label," +
                        "transaction_amount, transaction_type) value" +
                        "(?, ?, ?, ?)";
                try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setObject(1, account.getAccountId());
                    preparedStatement.setString(2, toSave.getTransactionLabel());
                    preparedStatement.setDouble(3, toSave.getAmount());
                    preparedStatement.setString(4, toSave.getTransactionType());

                    int rowAdded = preparedStatement.executeUpdate();

                    if (rowAdded > 0){
                        // TODO: get the last amount of an account
                        return account;
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public List<Transaction> saveAll(List<Transaction> toSave) {
        return null;
    }
    // TODO: join a transaction with an account
    // TODO: create function to create a transaction, return value: the account
    // condition: if it's a bank, a can have negatif value
    // else you can't
    public Account transaction(Amount amount, Account account){
        // check what is the type of the account
        // if it's a bank, you can have negatif value
        if (account.getAccountType() == "banque"){

        }
    }

    // TODO: create a function to transfer money between two account

}

package org.example.DAO;

import org.example.model.Account;
import org.example.model.Amount;
import org.example.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AmountDAO {
    private Connection connection;

    // find all amount
    public List<Amount> findAll() {
        List<Amount> amountList = new ArrayList<>();

        String sql= "SELECT FROM * amount";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                amountList.add(new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amountList;
    }

    // amount link to an account
    public Amount save(Amount toSave, UUID accountId) {

        String sql= "INSERT INTO amount (account_id, amount) value(?, ?);";

        try (PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, accountId);
            preparedStatement.setDouble(2, toSave.getAmount());

            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0){
                return toSave;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // TODO: find how to create a couple <amount, account>
    public List<Amount> saveAll(List<Amount> toSave) {
        return  null;
    }
    // TODO: create function to get the last amount
    // TODO: create function to get the account's amount at a given date
    // TODO: create function to get the account's amount now

    // TODO: work with the last amount
}

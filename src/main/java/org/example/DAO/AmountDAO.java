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
    public List<Amount> saveAll(List<Amount> toSave, UUID accountId) {
        String sql = "INSERT INTO amount (account_id, amount, datetime) VALUES (?, ?, ?);";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Amount amount : toSave) {
                preparedStatement.setObject(1, accountId);
                preparedStatement.setDouble(2, amount.getAmount());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(amount.getDateTime()));
    
                preparedStatement.addBatch();
            }
    
            int[] rowsAdded = preparedStatement.executeBatch();
            return (rowsAdded.length == toSave.size()) ? toSave : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Amount findLastAmount(UUID accountId) {
        Amount lastAmount = null;
        String sql = "SELECT * FROM amount WHERE account_id = ? ORDER BY datetime DESC LIMIT 1;";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastAmount = new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastAmount;
    }
    
    public Amount findAmountAtDate(UUID accountId, LocalDateTime dateTime) {
        Amount amount = null;
        String sql = "SELECT * FROM amount WHERE account_id = ? AND datetime <= ? ORDER BY datetime DESC LIMIT 1;";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dateTime));
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                amount = new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }
    
    public Amount findCurrentAmount(UUID accountId) {
        return findLastAmount(accountId);
    }
    
}

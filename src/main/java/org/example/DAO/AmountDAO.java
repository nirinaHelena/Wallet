package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Amount;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AmountDAO {
    private DatabaseConnection connection;

    public AmountDAO() {
        this.connection = new DatabaseConnection();
    }

    // find all amount
    public List<Amount> findAll() {
        List<Amount> amountList = new ArrayList<>();

        String sql= "SELECT FROM * amount";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                amountList.add(new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime(),
                        resultSet.getDouble("exchangeRate")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amountList;
    }

public Amount save(Amount toSave, UUID accountId) {
    String sql = "INSERT INTO amount (account_id, amount, datetime, currency_id, exchangeRate) VALUES (?, ?, ?, ?);";
    try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
        preparedStatement.setObject(1, accountId);
        preparedStatement.setDouble(2, toSave.getAmount());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(toSave.getDateTime()));
        preparedStatement.setObject(4, toSave.getExchangeRate());
        int rowAdded = preparedStatement.executeUpdate();
        if (rowAdded > 0) {
            return toSave;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public List<Amount> saveAll(List<Amount> toSave, UUID accountId) {
        String sql = "INSERT INTO amount (account_id, amount, datetime) VALUES (?, ?, ?);";
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            for (Amount amount : toSave) {
                preparedStatement.setObject(1, accountId);
                preparedStatement.setDouble(2, amount.getAmount());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(amount.getDateTime()));
                preparedStatement.setDouble(4,amount.getExchangeRate());
    
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
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastAmount = new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime(),
                        resultSet.getDouble("exchangeRate")
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
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dateTime));
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                amount = new Amount(
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime(),
                        resultSet.getDouble("exchangeRate")
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

    public double weightedAverageExchange(UUID accountId, LocalDateTime date) {
        String sql = "SELECT AVG(exchange_rate) AS weighted_average " +
                "FROM amount " +
                "WHERE account_id = ? AND DATE(datetime) = ?;";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("weighted_average");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Valeur par défaut si quelque chose ne fonctionne pas
    }
}    

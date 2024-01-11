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

        String sql= "SELECT * FROM amount";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                amountList.add(new Amount(
                        resultSet.getInt("account_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amountList;
    }

public Amount save(Amount toSave) {

    String sql = "INSERT INTO amount (account_id, amount, datetime) VALUES (?, ?, ?);";

    try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
        preparedStatement.setObject(1, toSave.getAccountId());
        preparedStatement.setDouble(2, toSave.getAmount());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

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
    
                preparedStatement.addBatch();
            }
    
            int[] rowsAdded = preparedStatement.executeBatch();
            return (rowsAdded.length == toSave.size()) ? toSave : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Amount findLastAmount(int accountId) {
        Amount lastAmount = null;
        String sql = "SELECT * FROM amount WHERE account_id = ? ORDER BY datetime DESC LIMIT 1;";
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lastAmount = new Amount(
                        resultSet.getInt("account_id"),
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
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dateTime));
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                amount = new Amount(
                        resultSet.getInt("account_id"),
                        resultSet.getDouble("amount,"),
                        resultSet.getTimestamp("datetime").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }
    
    public Amount findCurrentAmount(int accountId) {

        return findLastAmount(accountId);
    }

    public double weightedAverageExchange(int accountId, LocalDateTime date) {
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
        return 0.0; // Valeur par défaut si quelque chose ne fonctionne pas
    }

    public double getMinimumExchangeRate(int accountId, LocalDateTime date) {
        String sql = "SELECT MIN(exchange_rate) FROM amount WHERE account_id = ? AND datetime::DATE = ?;";
        return getExchangeRate(accountId, date, sql);
    }

    public double getMaximumExchangeRate(int accountId, LocalDateTime date) {
        String sql = "SELECT MAX(exchange_rate) FROM amount WHERE account_id = ? AND datetime::DATE = ?;";
        return getExchangeRate(accountId, date, sql);
    }

    public double getMedianExchangeRate(int accountId, LocalDateTime date) {
        String sql = "SELECT percentile_cont(0.5) WITHIN GROUP (ORDER BY exchange_rate) " +
                "FROM amount WHERE account_id = ? AND datetime::DATE = ?;";
        return getExchangeRate(accountId, date, sql);
    }
    private double getExchangeRate(int accountId, LocalDateTime date, String sql) {
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {
            preparedStatement.setObject(1, accountId);
            preparedStatement.setObject(2, Timestamp.valueOf(date));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Remplacez cette valeur par une valeur par défaut ou une gestion d'erreur appropriée
    }
}    

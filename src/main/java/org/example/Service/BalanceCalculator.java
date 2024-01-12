package org.example.Service;

import org.example.DAO.TransactionDAO;
import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class BalanceCalculator {
    private final DatabaseConnection connection;
    private TransactionDAO transactionDAO;

    public BalanceCalculator() {
        this.connection = new DatabaseConnection();
    }

    // Fonction pour calculer la somme des entrées et sorties d'argent entre une plage de dates donnée
    public double calculateBalance(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COALESCE(SUM(CASE WHEN t.transaction_type = 'credit' THEN t.transaction_amount ELSE -t.transaction_amount END), 0) " +
                "FROM transaction t " +
                "WHERE t.account_id = ? " +
                "AND t.transaction_date_hour BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Fonction pour calculer la somme des montants de chaque catégorie entre une plage de dates donnée
    public Map<String, Double> calculateCategoryAmounts(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Double> categoryAmounts = new HashMap<>();
        String sql = "SELECT " +
                "COALESCE(SUM(CASE WHEN tc.category_name = 'restaurant' THEN t.transaction_amount ELSE 0 END), 0) AS restaurant, " +
                "COALESCE(SUM(CASE WHEN tc.category_name = 'salaire' THEN t.transaction_amount ELSE 0 END), 0) AS salaire " +
                "FROM transaction t " +
                "LEFT JOIN transaction_category tc ON t.category_id = tc.category_id " +
                "WHERE t.account_id = ? " +
                "AND t.transaction_date_hour BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql)) {

            preparedStatement.setObject(1, accountId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryAmounts.put("restaurant", resultSet.getDouble("restaurant"));
                    categoryAmounts.put("salaire", resultSet.getDouble("salaire"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryAmounts;
    }
    
    // Calcule la somme des entrées et sorties d'argent entre la plage de dates donnée
    public double getSumOfAmountsBetweenDates(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionDAO.findAll(accountId, startDate, endDate);

        double totalAmount = 0.0;

        for (Transaction transaction : transactions) {
            if ("debit".equals(transaction.getTransactionType())) {
                totalAmount -= transaction.getAmount();
            } else if ("credit".equals(transaction.getTransactionType())) {
                totalAmount += transaction.getAmount();
            }
        }
    
        return totalAmount;
    }

    // Calcule la somme des montants de chaque catégorie entre la plage de dates donnée
public Map<String, Double> getSumOfAmountsByCategoryBetweenDates(int accountId, LocalDateTime startDate, LocalDateTime endDate) {
    List<Transaction> transactions = transactionDAO.findAll(accountId, startDate, endDate);

    Map<String, Double> categoryAmounts = new HashMap<>();
    for (Transaction transaction : transactions) {
            String transactionType = transaction.getTransactionType();
            double totalAmount = categoryAmounts.getOrDefault(transactionType, 0.0);

            if ("debit".equals(transactionType)) {
                totalAmount -= transaction.getAmount();
            } else if ("credit".equals(transactionType)) {
                totalAmount += transaction.getAmount();
            }

            categoryAmounts.put(transactionType, totalAmount);
        }
    Map<String, Double> sortedCategoryAmounts = new TreeMap<>(categoryAmounts);

    return sortedCategoryAmounts;
    
}

}


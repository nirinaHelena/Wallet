package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyDAO implements DAOInterface<Currency> {
    private final DatabaseConnection connection;

    public CurrencyDAO() {
        this.connection = new DatabaseConnection();
    }

    public List<Currency> findAll() {
        List<Currency> currencyList = new ArrayList<>();

        String sql = "SELECT * FROM currency;";

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                currencyList.add(new Currency(
                        (UUID) resultSet.getObject("currency_id"),
                        resultSet.getString("currency_name"),
                        resultSet.getString("currency_code")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyList;
    }

    public Currency save(Currency toSave) {
        String sql = "INSERT INTO currency (currency_name, currency_code) VALUES (?, ?);";

        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, toSave.getCurrencyName());
            preparedStatement.setString(2, toSave.getCurrencyCode());

            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Currency> saveAll(List<Currency> toSave) {
        List<Currency> currencyList = new ArrayList<>();

        for (Currency currency : toSave) {
            Currency saveCurrency = save(currency);
            if (saveCurrency != null) {
                currencyList.add(saveCurrency);
            }
        }
        return currencyList;
    }
}

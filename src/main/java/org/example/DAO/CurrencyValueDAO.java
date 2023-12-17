package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.CurrencyValue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyValueDAO  {
    private DatabaseConnection connection;

    public List<CurrencyValue> findAll() {
        List<CurrencyValue> currencyValueList = new ArrayList<>();


        String sql = "SELECT * FROM currency_value ; " ;

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                currencyValueList.add(new CurrencyValue(
                        resultSet.getInt("currency_id"),
                        resultSet.getInt("currency_id_source"),
                        resultSet.getInt("currency_id_destination"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("effect_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyValueList;
    }
    public List<CurrencyValue> findLastCurrencyValue() {
        List<CurrencyValue> currencyValueList = new ArrayList<>();


        String sql = "SELECT * FROM currency_value WHERE account_id = ? ORDER BY datetime DESC LIMIT 1 ; " ;

        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                currencyValueList.add(new CurrencyValue(
                        resultSet.getInt("currency_id"),
                        resultSet.getInt("currency_id_source"),
                        resultSet.getInt("currency_id_destination"),
                        resultSet.getDouble("value"),
                        resultSet.getTimestamp("effect_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyValueList;
    }


    public CurrencyValue save(CurrencyValue toSave) {

        String sql = "INSERT INTO currency_value (currency_id_source, currency_id_destination, value) VALUES (?, ?, ?);";


        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, toSave.getCurrencyIdSource());
            preparedStatement.setObject(2, toSave.getCurrencyIdDestination());
            preparedStatement.setDouble(3, toSave.getValue());

            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0) {
                return toSave;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CurrencyValue> saveAll(List<CurrencyValue> toSave) {
        List<CurrencyValue> currencyList = new ArrayList<>();

        for (CurrencyValue currencyValue : toSave) {
            CurrencyValue saveCurrencyValue = save(currencyValue);
            if (saveCurrencyValue != null) {
                currencyList.add(saveCurrencyValue);
            }
        }
        return currencyList;
    }
}


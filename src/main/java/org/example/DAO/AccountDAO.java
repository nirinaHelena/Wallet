package org.example.DAO;

import org.example.model.Account;
import org.example.model.Currency;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO implements DAOInterface<Account>{
    private Connection connection;

    // find all account without their amount and transaction
    @Override
    public List<Account> findAll() {

        List<Account> accountList = new ArrayList<>();

        String sql= "SELECT FROM * account";

        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                accountList.add(new Account(
                        (UUID) resultSet.getObject("account_id"),
                        resultSet.getString("account_name"),
                        (Currency) resultSet.getObject("account_currency"),
                        resultSet.getString("account_type")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return accountList;
    }

    // save an account with only name currency and type
    @Override
    public Account save(Account toSave) {

        String sql = "INSERT INTO account(account_name, account_currency, account_type) values (?, ?);";

        // TODO: change currency to int when insert into database
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, toSave.getAccountName());
            preparedStatement.setObject(2, toSave.getCurrency());
            preparedStatement.setString(3, toSave.getAccountType());

            int rowAdded = preparedStatement.executeUpdate();
            if (rowAdded > 0){
                return toSave;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // save a list account with name, currency, type
    @Override
    public List<Account> saveAll(List<Account> toSave) {

        List<Account> accountList = new ArrayList<>();

        for (Account account: toSave){
            Account saveAccount = save(account);
            if (saveAccount != null){
                accountList.add(saveAccount);
            }
        }
        return accountList;
    }

    // show amount today
    public double currentBalance(UUID accountId){
        double amount = 0;
        String sql = "SELECT amount\n" +
                "FROM amount\n" +
                "WHERE account_id = "+ accountId+"\n" +
                "ORDER BY datetime DESC\n" +
                "LIMIT 1;" ;
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                amount = resultSet.getDouble("amount");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amount;
    }

    // show amount at a date
    public double balanceAtADate(UUID accountId, LocalDateTime dateTime){
        double amount = 0;
        String sql = "SELECT COALESCE(SUM(amount), 0) AS current_balance\n" +
                "FROM amount\n" +
                "WHERE \n" +
                "    account_id = " + accountId + " AND\n" +
                "    datetime <= "+ dateTime +"" +
                "ORDER BY datetime DESC\n" +
                "LIMIT 1;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                amount = resultSet.getDouble("amount");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amount;
    }

    // show amount of an account between a date
    public double balanceHistory(UUID accountId, LocalDateTime startDate, LocalDateTime endDate){
        double amount = 0 ;
        String sql = "SELECT *\n" +
                "FROM amount\n" +
                "WHERE \n" +
                "    account_id = "+ accountId +" AND\n" +
                "    datetime BETWEEN "+ startDate +" AND "+ endDate +";" ;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                amount = resultSet.getDouble("amount");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amount;
    }
}

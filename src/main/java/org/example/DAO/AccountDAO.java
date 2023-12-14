package org.example.DAO;

import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;
import org.example.model.Currency;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO{
    private final DatabaseConnection connection;
    private AmountDAO amountDAO;
    private TransactionDAO transactionDAO;

    public AccountDAO() {
        this.connection = new DatabaseConnection(); // Initialize the connection object
    }
    // find all account without their amount and transaction
    public List<Account> findAll() {

        List<Account> accountList = new ArrayList<>();

        String sql= "SELECT * FROM account ;";

        try (Statement statement = connection.getConnection().createStatement();
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
    // save a list account with name, currency, type
    public List<Account> saveAll(List<Account> toSave) {
        String sql = "INSERT INTO account (account_name, account_currency, account_type) VALUES (?, ?, ?);";
    
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Account account : toSave) {
                preparedStatement.setString(1, account.getAccountName());
                preparedStatement.setObject(2, account.getCurrency());
                preparedStatement.setString(3, account.getAccountType());
    
                preparedStatement.addBatch();
            }
    
            int[] rowsAdded = preparedStatement.executeBatch();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
    
            List<Account> savedAccounts = new ArrayList<>();
            for (int i = 0; i < rowsAdded.length; i++) {
                if (rowsAdded[i] > 0 && generatedKeys.next()) {
                    Account savedAccount = new Account(
                            UUID.fromString(generatedKeys.getString(1)),
                            toSave.get(i).getAccountName(),
                            toSave.get(i).getCurrency(),
                            toSave.get(i).getAccountType()
                    );
                    savedAccounts.add(savedAccount);
                }
            }
            return savedAccounts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    // show amount today
    public double currentBalance(UUID accountId){
        double amount = 0;
        String sql = "SELECT amount\n" +
                "FROM amount\n" +
                "WHERE account_id = "+ accountId+"\n" +
                "ORDER BY datetime DESC\n" +
                "LIMIT 1;" ;
        try (Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                amount = resultSet.getDouble("amount");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amount;
    }

    // show an account with all parameter
    public List<Account> findAccount(UUID accountId){
        String sql = "SELECT * FROM account where account_id = "+ accountId + ";" ;
        List<Account> accountList = new ArrayList<>();
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()){
                accountList.add(new Account)(
                        (UUID) resultSet.getObject("account_id"),
                        resultSet.getString("account_name"),
                        amountDAO.findLastAmount(accountId),
                        transactionDAO.findAll(accountId),
                        (Currency) resultSet.getObject("account_currency"),
                        resultSet.getString("account_type")
                )
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return accountList;
    }

    // show amount at a date
    public double balanceAtADate(UUID accountId, LocalDateTime dateTime){
        double amount = 0;
        String sql = "SELECT amount \n" +
                "FROM amount\n" +
                "WHERE \n" +
                "    account_id = " + accountId + " AND\n" +
                "    datetime <= "+ dateTime +"" +
                "ORDER BY datetime DESC\n" +
                "LIMIT 1;";
        try (Statement statement = connection.getConnection().createStatement();
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
        String sql = "SELECT * \n" +
                "FROM amount\n" +
                "WHERE \n" +
                "    account_id = "+ accountId +" AND\n" +
                "    datetime BETWEEN "+ startDate +" AND "+ endDate +";" ;
        try (Statement statement = connection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                amount = resultSet.getDouble("amount");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return amount;
    }

    @Override
    public Account save(Account toSave) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}

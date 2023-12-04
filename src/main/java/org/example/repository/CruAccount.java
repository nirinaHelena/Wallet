package org.example.repository;

import org.example.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CruAccount implements CrudOperationInterface<Account>{
    private Connection connection;
    @Override
    public List<Account> findAll() {
        List<Account> accountList= new ArrayList<>();
        String sql= "select * from account;";

        try (PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                accountList.add(new Account(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  accountList;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        List<Account> accountList= new ArrayList<>();
        for (Account account: toSave){
            Account savedAccount= save(account);
            if (savedAccount!= null){
                accountList.add(savedAccount);
            }
        }
        return accountList;
    }

    @Override
    public Account save(Account toSave) {
        String sql= "insert into account (username, email) values (?, ?) ;" ;
        try (PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1, toSave.getName());
            preparedStatement.setString(2,toSave.getEmail());

            int affected= preparedStatement.executeUpdate();
            if (affected>0){
                return toSave
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

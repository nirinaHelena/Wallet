package test.Logger;

import org.example.DAO.AccountDAO;
import org.example.databaseConfiguration.DatabaseConnection;
import org.example.model.Account;

public class AccountTest {
    DatabaseConnection databaseConnection;
    public static void main(String[] args) {
        System.out.println("test account dao");
        AccountDAO accountDAO = new AccountDAO();
        System.out.println("List of account: "+ accountDAO.findAll());
    }
}

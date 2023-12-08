package org.example.DAO;

import org.example.model.Account;
import org.example.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class TransferDAO {
    private Connection connection;
    private TransactionDAO transactionDAO;
    public String save(Account senderAccount, Account receiverAccount, double amount, String label){
        String sql = "INSERT INTO transfer (sender_account_id, receiver_account_id," +
                "transfer amount) value (?,?, ?) ; " ;
        /**
         * check if senderAccount'ammount > amount
         * check if account'currency are the same
         */
        if (senderAccount.getAmount().getAmount() > amount){
            if (senderAccount.getCurrency() == receiverAccount.getCurrency()){
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                    preparedStatement.setObject(1,senderAccount.getAccountId());
                    preparedStatement.setObject(2, receiverAccount.getAccountId());
                    preparedStatement.setDouble(3, amount);

                    int rowAdded = preparedStatement.executeUpdate();

                    if (rowAdded > 0){
                        // debit senderAccount
                        Transaction sendTransaction = new Transaction(label, amount, "debit");
                        transactionDAO.save(sendTransaction, senderAccount);
                        // credit receiverAccount
                        Transaction receiveTransaction = new Transaction(label, amount, "credit");
                        transactionDAO.save(receiveTransaction, receiverAccount);
                        return "Transfer success";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {

            }
        }else{
            return "Sorry, you don't have enough money for the transaction." ;
        }
        return null;
    }
}

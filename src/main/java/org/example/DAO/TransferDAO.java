package org.example.DAO;

import org.example.model.Account;
import org.example.model.CurrencyValue;
import org.example.model.Transaction;
import org.example.model.Transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class TransferDAO {
    private Connection connection;
    private TransactionDAO transactionDAO;
    private AmountDAO amountDAO;
    private AccountDAO accountDAO;
    private CurrencyValueDAO currencyValueDAO;
    public String save(Transfer transfer, int transactionCategory){
        String sql = "INSERT INTO transfer_history (sender_account_id, receiver_account_id," +
                "transfer amount) value (?,?, ?) ; " ;
        /**
         * check if senderAccount'ammount > amount
         * check if account'currency are the same
         */
        Account senderAccount= (Account) accountDAO.findAccount(transfer.getSenderAccountId());
        Account receiverAccount= (Account) accountDAO.findAccount(transfer.getSenderAccountId());
        Double senderAmount= amountDAO.findLastAmount(transfer.getSenderAccountId()).getAmount();
        Double receiverAmount= amountDAO.findLastAmount(transfer.getReceiverAccountId()).getAmount();

        if (senderAmount> transfer.getTransferAmount().getAmount()){
            if (senderAccount.getCurrency()== receiverAccount.getCurrency()){
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                    preparedStatement.setObject(1,senderAccount.getAccountId());
                    preparedStatement.setObject(2, receiverAccount.getAccountId());
                    preparedStatement.setDouble(3, transfer.getTransferAmount().getAmount());

                    int rowAdded = preparedStatement.executeUpdate();

                    if (rowAdded > 0){
                        // debit senderAccount
                        Transaction debitTransaction = new Transaction( senderAccount.getAccountId(),
                                null, transfer.getTransferAmount().getAmount(),
                                null, "debit", transactionCategory);

                        Transaction creditTransaction = new Transaction(receiverAccount.getAccountId(),
                                null, transfer.getTransferAmount().getAmount(),
                                null, "credit", transactionCategory);

                        transactionDAO.save(debitTransaction);
                        // credit receiverAccount
                        transactionDAO.save(creditTransaction);
                        return "Transfer success";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }if (senderAccount.getCurrency() != receiverAccount.getCurrency()){
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

                    preparedStatement.setObject(1,senderAccount.getAccountId());
                    preparedStatement.setObject(2, receiverAccount.getAccountId());
                    preparedStatement.setDouble(3, transfer.getTransferAmount().getAmount());

                    int rowAdded = preparedStatement.executeUpdate();

                    if (rowAdded > 0){
                        // debit senderAccount
                        Transaction debitTransaction = new Transaction( senderAccount.getAccountId(),
                                null, transfer.getTransferAmount().getAmount(),
                                null, "debit", transactionCategory);

                        CurrencyValue  lastCurrencyValue = (CurrencyValue) currencyValueDAO.findLastCurrencyValue();
                        Double creditAmount = transfer.getTransferAmount().getAmount() * lastCurrencyValue.getValue();

                        Transaction creditTransaction = new Transaction(receiverAccount.getAccountId(),
                                null, creditAmount,
                                null, "credit", transactionCategory);

                        transactionDAO.save(debitTransaction);
                        // credit receiverAccount
                        transactionDAO.save(creditTransaction);
                        return "Transfer success";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        }else{
            return "Sorry, you don't have enough money for the transaction." ;
        }
        return null;
    }
}
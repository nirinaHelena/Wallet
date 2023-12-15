package test.Logger;

import org.example.DAO.CurrencyDAO;
import org.example.model.Currency;

public class CurrencyTest {
    public static void main(String[] args) {
        System.out.println("Test currency dao");
        CurrencyDAO currencyDAO= new CurrencyDAO();
        System.out.println("find all currency"+ currencyDAO.findAll());

        Currency euro= new Currency(null,"Euro", "EUR");

        System.out.println("save a currency"+ currencyDAO.save(euro));
    }
}

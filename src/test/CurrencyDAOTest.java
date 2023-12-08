import org.example.DAO.CurrencyDAO;
import org.example.model.Currency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyDAOTest {

    private Connection connection;
    private CurrencyDAO currencyDAO;

    @BeforeEach
    void setUp() {
        // Initialisez la connexion à la base de données (vous devrez peut-être ajuster l'URL, le nom d'utilisateur et le mot de passe)
        String jdbcUrl = "jdbc:mysql://localhost:3306/votre_base_de_donnees";
        String username = "votre_nom_utilisateur";
        String password = "votre_mot_de_passe";

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            currencyDAO = new CurrencyDAO();
            currencyDAO.setConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        // Fermez la connexion après chaque test
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFindAll() {
        // Assurez-vous d'avoir des données dans la table currency pour ce test
        List<Currency> currencies = currencyDAO.findAll();
        assertNotNull(currencies);
        // Ajoutez des assertions supplémentaires en fonction de vos données
    }

    @Test
    void testSave() {
        // Créez une nouvelle devise pour le test
        Currency currencyToSave = new Currency("Dollar", "USD");

        // Enregistrez la devise
        Currency savedCurrency = currencyDAO.save(currencyToSave);

        // Assurez-vous que la devise enregistrée n'est pas nulle et a un ID généré
        assertNotNull(savedCurrency);
        assertNotNull(savedCurrency.getCurrencyId());

        // Ajoutez des assertions supplémentaires en fonction de vos besoins
    }

    @Test
    void testSaveAll() {
        // Créez une liste de devises pour le test
        List<Currency> currenciesToSave = List.of(
                new Currency("Euro", "EUR"),
                new Currency("Yen", "JPY")
        );

        // Enregistrez la liste de devises
        List<Currency> savedCurrencies = currencyDAO.saveAll(currenciesToSave);

        // Assurez-vous que la liste de devises enregistrées n'est pas nulle et contient des devises avec des ID générés
        assertNotNull(savedCurrencies);
        for (Currency savedCurrency : savedCurrencies) {
            assertNotNull(savedCurrency.getCurrencyId());
        }
    }
}

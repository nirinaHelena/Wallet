import org.junit.jupiter.api.*;
import org.example.DAO.AccountDAO;
import org.example.model.Account;
import org.example.model.Currency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
    
class AccountDAOTest {

    private static Connection connection;
    private AccountDAO accountDAO;
    private Account testAccount;
    private Currency testCurrency;

    @BeforeAll
    static void setUpBeforeClass() throws SQLException {
        // Initialiser la connexion à la base de données (peut-être utiliser une base de données de test)
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_database", "username", "password");
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {
        // Fermer la connexion après les tests
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    void setUp() {
        // Initialiser les objets nécessaires pour les tests
        accountDAO = new AccountDAO(connection);
        testCurrency = new Currency(UUID.randomUUID(), "Euro", "EUR");
        testAccount = new Account(UUID.randomUUID(), "Test Account", testCurrency, "Bank");
    }

    @AfterEach
    void tearDown() {
        // Nettoyer les ressources après chaque test si nécessaire
    }

    @Test
    void testSaveAccount() {
        // Enregistrer un compte dans la base de données
        Account savedAccount = accountDAO.save(testAccount);

        // Vérifier si le compte enregistré est le même que celui que nous avons sauvegardé
        assertNotNull(savedAccount);
        assertEquals(testAccount.getAccountId(), savedAccount.getAccountId());
        assertEquals(testAccount.getAccountName(), savedAccount.getAccountName());
        assertEquals(testAccount.getCurrency(), savedAccount.getCurrency());
        assertEquals(testAccount.getAccountType(), savedAccount.getAccountType());
    }

    @Test
    void testFindAllAccounts() {
        // Récupérer tous les comptes de la base de données
        List<Account> accountList = accountDAO.findAll();

        // Vérifier si la liste n'est pas nulle
        assertNotNull(accountList);
    }

    @Test
    void testCurrentBalance() {
        // Insérer des données de test dans la base de données si nécessaire

        // Récupérer le solde actuel pour un compte spécifique
        double currentBalance = accountDAO.currentBalance(testAccount.getAccountId());

        // Vérifier si le solde actuel est correct (selon les données de test)
        assertEquals(0.0, currentBalance);
    }
}

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AmountDAOTest {

    private static JdbcConnectionPool connectionPool;
    private AmountDAO amountDAO;
    private Connection connection;
    private Amount testAmount;
    private UUID testAccountId;

    @BeforeAll
    static void setUpBeforeClass() {
        // Initialiser la connexion à la base de données H2 en mémoire
        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "username", "password");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // Fermer la connexion après les tests
        connectionPool.dispose();
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Initialiser la connexion et le DAO avant chaque test
        connection = connectionPool.getConnection();
        amountDAO = new AmountDAO(connection);
        testAmount = new Amount(100.0);
        testAccountId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Fermer la connexion après chaque test
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testSaveAmount() {
        // Enregistrer un montant dans la base de données
        Amount savedAmount = amountDAO.save(testAmount, testAccountId);

        // Vérifier si le montant enregistré est le même que celui que nous avons sauvegardé
        assertNotNull(savedAmount);
        assertEquals(testAmount.getAmount(), savedAmount.getAmount(), 0.001); // Utiliser une tolérance pour les comparaisons de nombres à virgule flottante
        assertEquals(testAmount.getDateTime(), savedAmount.getDateTime());
    }

    @Test
    void testFindAllAmounts() {
        // Insérer des données de test dans la base de données si nécessaire

        // Récupérer tous les montants de la base de données
        List<Amount> amountList = amountDAO.findAll();

        // Vérifier si la liste n'est pas nulle
        assertNotNull(amountList);
    }
}

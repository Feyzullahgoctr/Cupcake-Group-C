package app.persistence;

import app.services.BestilService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseTestBefore {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "cupcake";

    protected static ConnectionPool connectionPool;
    protected static AdminMapper adminMapper;
    protected static BestilMapper bestilMapper;
    protected static BetalMapper betalMapper;
    protected static UserMapper userMapper;

    // Kører kun en gang før alle test
    @BeforeAll
    static void setUpClass() {
        connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
        adminMapper = new AdminMapper(connectionPool);
        bestilMapper = new BestilMapper(connectionPool);
        betalMapper = new BetalMapper(connectionPool);
        userMapper = new UserMapper(connectionPool);
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("delete from test.orderlines");
            stmt.execute("delete from test.orders");
            stmt.execute("delete from test.bottoms");
            stmt.execute("delete from test.toppings");
            stmt.execute("delete from test.users");

            stmt.execute("ALTER SEQUENCE test.orderlines_orderline_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.orders_order_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.bottoms_bottom_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.toppings_topping_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE test.users_user_id_seq RESTART WITH 1");

            stmt.execute("""
                INSERT INTO test.users (email, password, admin, balance) VALUES
                ('admin@test.dk',  '1234', true,  0),
                ('kunde1@test.dk', '1234', false, 100),
                ('kunde2@test.dk', '1234', false, 400)
                """);

            stmt.execute("""
                INSERT INTO test.bottoms (bottom_name, price) VALUES
                ('Chocolate', 5),
                ('Vanilla', 5),
                ('Nutmeg', 5)
                """);

            stmt.execute("""
                INSERT INTO test.toppings (topping_name, price) VALUES
                ('Chocolate', 5),
                ('Blueberry', 5),
                ('Raspberry', 5)
                """);

            stmt.execute("""
                INSERT INTO test.orders (user_id, total_price, status) VALUES
                (2, 40, 'unpaid'),
                (3, 10, 'Paid')
                """);

            stmt.execute("""
                INSERT INTO test.orderlines (order_id, bottom_id, topping_id, amount, price) VALUES
                (1, 1, 2, 2, 20),
                (2, 2, 1, 1, 10)
                """);

        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

}

package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(UserMapper.class);

    public UserMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public User login(String email, String password) throws DatabaseException {
        User user = getUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new DatabaseException("Forkert email eller adgangskode");
        }
        return user;
    }

    public User getUserByEmail(String email) throws DatabaseException {
        String query = "SELECT user_id, email, password, admin, balance FROM users WHERE email = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int userId = rs.getInt("user_id");
                String dbPassword = rs.getString("password");
                boolean isAdmin = rs.getBoolean("admin");
                double balance = rs.getDouble("balance");

                return new User(userId, email, dbPassword, isAdmin, balance);
            }

        } catch (SQLException e) {
            logger.error("Fejl ved opslag på email: {}", email, e);
            throw new DatabaseException("Der skete en fejl ved login", e.getMessage());
        }

    }

    public void createUser(String email, String password) throws DatabaseException {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DatabaseException("Emailen findes allerede");
            }
            logger.error("Fejl ved oprettelse af bruger: {}", email, e);
            throw new DatabaseException("Der skete en fejl ved oprettelse", e.getMessage());
        }
    }
    public double getUserBalanceByEmail(String email)  {
        String query = "SELECT balance FROM users WHERE email = ?";
        double balance = 0;
        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return balance;
                }
                balance = rs.getDouble("balance");

                return balance;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateUserBalance(String email, double amount) throws DatabaseException {

        String query = "UPDATE users SET balance = ? WHERE email = ? ";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            double newBalance=getUserBalanceByEmail(email)+amount;

            ps.setDouble(1, newBalance);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DatabaseException("Emailen findes allerede");
            }
            logger.error("Fejl ved oprettelse af bruger: {}", email, e);
            throw new DatabaseException("Der skete en fejl ved oprettelse", e.getMessage());
        }
    }
}

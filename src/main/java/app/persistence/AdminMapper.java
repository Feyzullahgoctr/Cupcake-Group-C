package app.persistence;

import app.dto.AdminOrderDTO;
import app.dto.OrderlineDTO;
import app.entities.Order;
import app.entities.Orderline;
import app.entities.Status;
import app.entities.User;
import app.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(AdminMapper.class);

    public AdminMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }


    public List<AdminOrderDTO> getAllOrders() throws DatabaseException {
        List<AdminOrderDTO> orders = new ArrayList<>();
        String query = """
                SELECT o.order_id, u.email, o.create_date, o.status, o.total_price
                FROM orders o
                JOIN users u ON o.user_id = u.user_id
                ORDER BY o.create_date DESC, o.order_id DESC
                """;

        try (Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
            while((rs.next())){
                int orderId = rs.getInt("order_id");
                String email = rs.getString("email");
                LocalDateTime orderDate = rs.getTimestamp("create_date").toLocalDateTime();
                Status status = Status.valueOf(rs.getString("status"));
                double totalPrice = rs.getDouble("total_price");

                List<OrderlineDTO> orderlines = getOrderLines(orderId);
                orders.add(new AdminOrderDTO(orderId, email, orderDate, status, totalPrice, orderlines));
            }
        } catch (SQLException e){
            logger.error("Fejl ved hentning af alle ordrer", e);
            throw new DatabaseException("Kunne ikke hente ordrer", e.getMessage());
        }
        return orders;
    }

    public List<OrderlineDTO> getOrderLines(int orderId) throws DatabaseException {
        List<OrderlineDTO> orderlines = new ArrayList<>();
        String query = """
                SELECT ol.orderline_id, b.bottom_id, t.topping_id, ol.amount, ol.price,
                       b.bottom_name, t.topping_name
                FROM orderlines ol
                JOIN bottoms b ON ol.bottom_id = b.bottom_id
                JOIN toppings t ON ol.topping_id = t.topping_id
                WHERE ol.order_id = ?
                """;

        try (Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orderlines.add(new OrderlineDTO(
                            rs.getInt("orderline_id"),
                            rs.getInt("bottom_id"),
                            rs.getInt("topping_id"),
                            rs.getInt("amount"),
                            rs.getDouble("price"),
                            rs.getString("bottom_name"),
                            rs.getString("topping_name")
                    ));
                }
            }
        } catch (SQLException e){
            logger.error("Fejl ved hentning af ordrelinjer for ordre: {}", orderId, e);
            throw new DatabaseException("Kunne ikke hente ordrelinjer", e.getMessage());
        }
        return orderlines;
    }


    public List<User> getAllCostumers() throws DatabaseException {
        List<User> customers = new ArrayList<>();
        String query = "SELECT user_id, email, password, admin, balance FROM users WHERE admin = false ORDER BY user_id";

        try(Connection conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()){
                int userId = rs.getInt("user_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isAdmin = rs.getBoolean("admin");
                double balance = rs.getDouble("balance");

                customers.add(new User(userId, email, password, isAdmin, balance));
            }
        } catch (SQLException e){
            logger.error("Fejl ved hentning af kunder", e);
            throw new DatabaseException("Kunne ikke hente kunder", e.getMessage());
        }

        return customers;

    }


    public User getUserById(int userId) throws DatabaseException {
        String query = "SELECT user_id, email, password, admin, balance FROM users WHERE user_id = ?";

        try (Connection conn = connectionPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isAdmin = rs.getBoolean("admin");
                double balance = rs.getDouble("balance");

                return new User(userId, email, password, isAdmin, balance);
            }
        } catch (SQLException e){
            logger.error("Fejl ved hentning af kunde: {}",userId);
            throw new DatabaseException("Kunne ikke hente kunden", e.getMessage());
        }
    }

    public List<Order> getOrdersByUserId(int userId) throws DatabaseException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT order_id, user_id, create_date, status, total_price FROM orders WHERE user_id = ? ORDER BY create_date DESC";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    LocalDateTime orderDate = rs.getTimestamp("create_date").toLocalDateTime();
                    Status status = Status.valueOf(rs.getString("status"));
                    double totalPrice = rs.getDouble("total_price");

                    orders.add(new Order(orderId, userId, orderDate, status, totalPrice));
                }
            }

        } catch (SQLException e) {
            logger.error("Fejl ved hentning af ordrer for kunde: {}", userId, e);
            throw new DatabaseException("Kunne ikke hente ordrer", e.getMessage());
        }
        return orders;
    }


}

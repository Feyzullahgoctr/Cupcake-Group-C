package app.persistence;

import app.dto.OrderlineDTO;
import app.entities.*;
import app.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BestilMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(BestilMapper.class);

    public BestilMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public List<Bottom> getBottoms() throws DatabaseException {
        String query = "select * from bottoms";
        List<Bottom> bottoms = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int bottom_id = rs.getInt("bottom_id");
                    String name = rs.getString("bottom_name");
                    double price = rs.getDouble("price");

                    bottoms.add(new Bottom(bottom_id, name, price));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette produkt findes ikke.");
        }
        return bottoms;
    }

    public List<Topping> getToppings() throws DatabaseException {
        String query = "select * from toppings";
        List<Topping> toppings = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int topping_id = rs.getInt("topping_id");
                    String name = rs.getString("topping_name");
                    double price = rs.getDouble("price");

                    toppings.add(new Topping(topping_id, name, price));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette produkt findes ikke.");
        }
        return toppings;
    }


    public int createOrder(int user_id, double price) throws DatabaseException
    {
        String sql = "insert into orders (user_id, create_date, total_price, status) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, user_id);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDouble(3, price);
            ps.setString(4, Status.unpaid.name());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                keys.next();
                return keys.getInt(1);
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Could not create order in the database");
        }
    }

    public int createOrderline(int order_id, int bottom_id,int topping_id, double amount, double price) throws DatabaseException
    {
        String sql = "insert into orderlines (order_id,bottom_id,topping_id, amount, price) values (?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, order_id);
            ps.setInt(2, bottom_id);
            ps.setInt(3, topping_id);
            ps.setDouble(4, amount);
            ps.setDouble(5, price);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                keys.next();
                return keys.getInt(1);
            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Could not create orderline in the database");
        }
    }


    public void updateOrderPrice(int order_id , double total_price) throws DatabaseException
    {
        String sql = "update orders set total_price = ?  where order_id = ?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setDouble(1, total_price);
                prepareStatement.setInt(2, order_id);

                prepareStatement.executeUpdate();

            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke opdater total price i order");
        }
    }


    public List<Orderline> getOrderline() throws DatabaseException {
        String query = "select * from orderlines";
        List<Orderline> orderlines = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int orderline_id = rs.getInt("orderline_id");
                    int order_id = rs.getInt("order_id");
                    int bottom_id = rs.getInt("bottom_id");
                    int topping_id = rs.getInt("topping_id");
                    int amount = rs.getInt("amount");
                    double price = rs.getDouble("price");

                    orderlines.add(new Orderline(orderline_id,order_id,bottom_id,topping_id,amount,price));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Dette orderlines findes ikke.");
        }
        return orderlines;
    }


    public void deleteOrderlines(int order_id) throws DatabaseException
    {
        String sql = "delete from orderlines WHERE order_id = ?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setInt(1, order_id);

                prepareStatement.executeUpdate();

            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke slette order");
        }
    }
    public void deleteOrder(int order_id) throws DatabaseException {
        String query = "delete from orders WHERE order_id = ?";


        try(Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
                prepareStatement.setInt(1, order_id);

                prepareStatement.executeUpdate();
            }
        }
            catch (SQLException e)
            {
                logger.error(e.getMessage(), e);
                throw new DatabaseException("Kunne ikke slette order");
            }
        }


    public List<OrderlineDTO> getOrderlineByOrderid(int order_id)
            throws DatabaseException {

        String query = """
            
                SELECT ol.orderline_id,
                       ol.order_id,
                   b.bottom_id,
                   t.topping_id,
                   ol.amount,
                   ol.price,
                   b.bottom_name,
                   t.topping_name
            FROM orderlines ol
                     JOIN bottoms b ON ol.bottom_id = b.bottom_id
                     JOIN toppings t ON ol.topping_id = t.topping_id
            WHERE ol.order_id = ?
            """;

        List<OrderlineDTO> orderlines = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setInt(1, order_id);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    orderlines.add(new OrderlineDTO(
                            rs.getInt("orderline_id"),
                            rs.getInt("order_id"),
                            rs.getInt("bottom_id"),
                            rs.getInt("topping_id"),
                            rs.getInt("amount"),
                            rs.getDouble("price"),
                            rs.getString("bottom_name"),
                            rs.getString("topping_name")
                    ));
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke hente orderlines.");
        }

        return orderlines;
    }
    public List<Order> getOrdersByUserId(int user_id) throws DatabaseException {
    String query = "SELECT * FROM orders WHERE user_id = ? ORDER BY create_date ";

            List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setInt(1, user_id);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    orders.add(new Order(
                            rs.getInt("order_id"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("create_date").toLocalDateTime(),
                            Status.valueOf(rs.getString("status")),
                            rs.getDouble("total_price")
                    ));
                }
            }        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke hente orderlines.");
        }

        return orders;
    }
}

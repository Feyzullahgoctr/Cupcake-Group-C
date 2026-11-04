package app.persistence;

import app.entities.Bottom;
import app.entities.Status;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
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

}

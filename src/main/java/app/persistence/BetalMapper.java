package app.persistence;

import app.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BetalMapper {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(BetalMapper.class);

    public BetalMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void updateOrderStatus(int order_id, String status) throws DatabaseException
    {
        String sql = "update orders set status = ?  where order_id = ?";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql))
            {
                prepareStatement.setString(1, status);
                prepareStatement.setInt(2, order_id);

                prepareStatement.executeUpdate();

            }
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new DatabaseException("Kunne ikke opdater status i order");
        }
    }


}

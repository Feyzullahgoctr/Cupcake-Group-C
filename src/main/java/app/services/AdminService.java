package app.services;

import app.dto.AdminOrderDTO;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;

import java.util.List;

public class AdminService {

    private ConnectionPool connectionPool;
    private AdminMapper adminMapper;

    public AdminService(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.adminMapper = new AdminMapper(connectionPool);
    }

    public List<User> getAllCostumers() throws DatabaseException {
        return adminMapper.getAllCostumers();
    }

    public User getCustomer(int userId) throws DatabaseException {
        User customer = adminMapper.getUserById(userId);
        if (customer == null) {
            throw new IllegalArgumentException("Kunden findes ikke");
        }
        return customer;

    }

    public List<Order> getOrdersForCustomer(int userId) throws DatabaseException {
        return adminMapper.getOrdersByUserId(userId);
    }

    public List<AdminOrderDTO> getAllOrders() throws DatabaseException {
        return adminMapper.getAllOrders();
    }


}

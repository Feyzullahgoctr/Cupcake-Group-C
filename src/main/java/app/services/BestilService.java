package app.services;

import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import app.persistence.BestilMapper;
import app.persistence.ConnectionPool;

import java.util.List;

public class BestilService {

    private ConnectionPool connectionPool;
    private BestilMapper bestilMapper;

    public BestilService(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.bestilMapper = new BestilMapper(connectionPool);
    }

    public List<Bottom> getBottoms() throws DatabaseException {
        return bestilMapper.getBottoms();
    }

    public List<Topping> getToppings() throws DatabaseException {
        return bestilMapper.getToppings();
    }

    public int createOrder(int user_id, double price) throws DatabaseException {
        return bestilMapper.createOrder(user_id,price);
    }

    public int createOrderline(int order_id, int bottom_id,int topping_id, double amount, double price) throws DatabaseException {
        return bestilMapper.createOrderline(order_id, bottom_id,topping_id, amount, price);
    }


}

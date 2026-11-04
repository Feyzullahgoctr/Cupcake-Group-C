package app.services;

import app.dto.OrderlineDTO;
import app.entities.*;
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

    public void updateOrderPrice(int order_id , double total_price) throws DatabaseException {
        bestilMapper.updateOrderPrice(order_id, total_price);
    }

    public List<Orderline> getAllOrderlines() throws DatabaseException {
        return bestilMapper.getOrderline();
    }

    public List<OrderlineDTO> getOrderlineByOrderid(int order_id) throws DatabaseException {
        return bestilMapper.getOrderlineByOrderid(order_id);
    }

    public double getOrderlineTotalPrice(int order_id) throws DatabaseException {

        List<OrderlineDTO> orderlineByOrderid = getOrderlineByOrderid(order_id);

        double total = 0;

        for (OrderlineDTO orderline : orderlineByOrderid) {
            total += orderline.getPrice();
        }

        return total;
    }

    public int getOrderlineTotalAmount(int order_id) throws DatabaseException {

        List<OrderlineDTO> orderlineByOrderid = getOrderlineByOrderid(order_id);

        int total = 0;

        for (OrderlineDTO orderline : orderlineByOrderid) {
            total += orderline.getAmount();
        }

        return total;
    }

    public void deleteOrderlines(int order_id) throws DatabaseException {
        bestilMapper.deleteOrderlines(order_id);
    }

    public Bottom getBottom(int bottom_id) throws DatabaseException {
        List<Bottom> bottoms = bestilMapper.getBottoms();
        for (Bottom bottom : bottoms) {
            if (bottom.getBottom_id() == bottom_id) {
                return bottom;
            }
        }
        return null;
    }

    public Topping getTopping(int topping_id) throws DatabaseException {
        List<Topping> toppings = bestilMapper.getToppings();
        for (Topping topping : toppings) {
            if (topping.getTopping_id() == topping_id) {
                return topping;
            }
        }
        return null;
    }


}

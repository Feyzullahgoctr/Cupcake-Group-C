package app.services;

import app.exceptions.DatabaseException;
import app.persistence.BetalMapper;
import app.persistence.ConnectionPool;

public class BetalService {

    private ConnectionPool connectionPool;
    private BetalMapper betalMapper;

    public BetalService(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.betalMapper = new BetalMapper(connectionPool);
    }

    public void updateOrderStatus(int order_id, String status) throws DatabaseException {
        betalMapper.updateOrderStatus(order_id, status);
    }

    public void updateUserBalance(String email, double totalprice) throws DatabaseException {
        betalMapper.updateUserBalance(email, totalprice);
    }


}

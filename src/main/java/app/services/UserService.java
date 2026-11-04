package app.services;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;

public class UserService {
   private ConnectionPool connectionPool;
   private UserMapper userMapper;

   public UserService(ConnectionPool connectionPool){
       this.connectionPool = connectionPool;
       this.userMapper = new UserMapper(connectionPool);
   }

   public User login(String email, String password) throws DatabaseException {
       if (email == null || email.isBlank()){
           throw new IllegalArgumentException("Email må ikke være tom");
       }
       if (password == null || password.isBlank()){
           throw new IllegalArgumentException("Password må ikke være tomt");
       }
       return userMapper.login(email, password);
   }

    public void register(String email, String password, String password2) throws DatabaseException {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email må ikke være tom");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password må ikke være tomt");
        }
        if (!password.equals(password2)) {
            throw new IllegalArgumentException("De to adgangskoder er ikke ens");
        }
        userMapper.createUser(email, password);
    }

    public void updateUserBalance(String email, double amount) throws DatabaseException {
        userMapper.updateUserBalance(email, amount);
    }
    public void getUserBalanceByEmail(String email) throws DatabaseException {
        userMapper.getUserBalanceByEmail(email);
    }
}

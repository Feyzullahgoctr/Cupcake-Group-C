package app.controllers;

import app.dto.OrderlineDTO;
import app.entities.Status;
import app.entities.Topping;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.BestilService;
import app.services.BetalService;
import app.services.UserService;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BetalController {

    private ConnectionPool connectionPool;
    private BetalService betalService;
    private BestilService bestilService;
    private UserService userService;

    public BetalController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.betalService = new BetalService(connectionPool);
        this.bestilService = new BestilService(connectionPool);
        this.userService = new UserService(connectionPool);
    }

    public void setRoutes(JavalinConfig app) {
        app.routes.post("/betal", ctx -> pay(ctx));
        app.routes.post("/betal/bekraeft", ctx -> paymentConfirmation(ctx));

    }

    private void pay(Context ctx) throws DatabaseException {

        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        int order_id = Integer.parseInt(ctx.formParam("order_id"));

        List<OrderlineDTO> orderItems = bestilService.getOrderlineByOrderid(order_id);
        double totalPrice = bestilService.getOrderlineTotalPrice(order_id);


        ctx.attribute("orderItems", orderItems);
        ctx.attribute("totalPrice", totalPrice);
        ctx.attribute("order_id", order_id);
        ctx.render("/betaling.html");

    }

    private void paymentConfirmation(Context ctx) throws DatabaseException {

        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        int order_id = Integer.parseInt(ctx.formParam("order_id"));
        double totalPrice = bestilService.getOrderlineTotalPrice(order_id);
        User userbalance = userService.getUserByEmail(user.getEmail());

        if (totalPrice > userbalance.getBalance()) {
            ctx.attribute("msg", "utilstrækkelig saldo");
            pay(ctx);
        } else {
            betalService.updateOrderStatus(order_id, String.valueOf(Status.Paid));
            betalService.updateUserBalance(userbalance.getEmail(), userbalance.getBalance() - totalPrice);
            User opdateret = userService.getUserByEmail(user.getEmail());
            ctx.sessionAttribute("order_id", null);
            ctx.sessionAttribute("user", opdateret);
            ctx.attribute("user", opdateret);
            ctx.render("/betalingcomplete.html");
        }


    }





}

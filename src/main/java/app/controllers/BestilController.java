package app.controllers;

import app.dto.OrderlineDTO;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.BestilService;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class BestilController {


    private ConnectionPool connectionPool;
    private BestilService bestilService;

    public BestilController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.bestilService = new BestilService(connectionPool);
    }

    public void setRoutes(JavalinConfig app) {
        app.routes.get("/bestil", ctx -> orderShow(ctx));
        app.routes.post("/bestil", ctx -> addToCart(ctx));
        app.routes.post("/bestil/redigere", ctx -> editOrder(ctx));

    }

    private void orderShow(Context ctx) throws DatabaseException {

        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        if (user != null) {

            List<Bottom> bottoms = bestilService.getBottoms();
            List<Topping> toppings = bestilService.getToppings();

            ctx.attribute("bottoms", bottoms);
            ctx.attribute("toppings", toppings);

            Integer orderId = ctx.sessionAttribute("order_id");

            if (orderId != null) {

                List<OrderlineDTO> orderItems =
                        bestilService.getOrderlineByOrderid(orderId);

                int totalAmount = bestilService.getOrderlineTotalAmount(orderId);
                double totalPrice = bestilService.getOrderlineTotalPrice(orderId);

                ctx.attribute("orderItems", orderItems);
                ctx.attribute("totalAmount", totalAmount);
                ctx.attribute("totalPrice", totalPrice);

                ctx.attribute("editing", orderItems.isEmpty());

            } else {

                ctx.attribute("orderItems", null);
                ctx.attribute("totalAmount", 0);
                ctx.attribute("totalPrice", 0.0);
                ctx.attribute("editing", true);
            }

            ctx.render("/bestil.html");

        } else {
            ctx.redirect("/login");
        }
    }

    private void addToCart(Context ctx) throws DatabaseException {

        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        if (user != null) {

            // Den henter de tilføjede ordrer fra HTML.
            List<String> bundIds = ctx.formParams("bund");
            List<String> toppingIds = ctx.formParams("topping");
            List<String> antalListe = ctx.formParams("antal");


            // Hvis det ikke er blevet tilføjet før, vil oversigtssektionen være tom.
            Integer order_id = ctx.sessionAttribute("order_id");

            if (order_id == null) {
                order_id = bestilService.createOrder(user.getUser_id(), 0);
                ctx.sessionAttribute("order_id", order_id);
            } else {
                bestilService.deleteOrderlines(order_id);
            }


            // Den registrerer ordren i databasen.
            for (int i = 0; i < bundIds.size(); i++) {

                int bundId = Integer.parseInt(bundIds.get(i));
                int toppingId = Integer.parseInt(toppingIds.get(i));
                int antal = Integer.parseInt(antalListe.get(i));

                double bundPrice = bestilService.getBottom(bundId).getPrice();
                double toppingPrice = bestilService.getTopping(toppingId).getPrice();

                double totalprice = (bundPrice + toppingPrice) * antal;

                bestilService.createOrderline(
                        order_id, bundId, toppingId, antal, totalprice);
            }

            // Opdaterer den samlede pris.
            double totalPrice = bestilService.getOrderlineTotalPrice(order_id);
            bestilService.updateOrderPrice(order_id, totalPrice);

            // For at vise eksisterende ordrer til venstre, hvis der er nogen.
            List<OrderlineDTO> orderItems =
                    bestilService.getOrderlineByOrderid(order_id);

            int totalAmount = bestilService.getOrderlineTotalAmount(order_id);

            ctx.attribute("bottoms", bestilService.getBottoms());
            ctx.attribute("toppings", bestilService.getToppings());
            ctx.attribute("orderItems", orderItems);
            ctx.attribute("totalPrice", totalPrice);
            ctx.attribute("totalAmount", totalAmount);
            ctx.attribute("editing", false);

            ctx.render("/bestil.html");
        } else {
            ctx.redirect("/login");
        }


    }

    private void editOrder(Context ctx) throws DatabaseException {

        User user = ctx.sessionAttribute("user");
        ctx.attribute("user", user);

        Integer orderId = ctx.sessionAttribute("order_id");

        if (orderId != null) {
            List<OrderlineDTO> orderItems =
                    bestilService.getOrderlineByOrderid(orderId);

            ctx.attribute("orderItems", orderItems);
            ctx.attribute("totalAmount",
                    bestilService.getOrderlineTotalAmount(orderId));
            ctx.attribute("totalPrice",
                    bestilService.getOrderlineTotalPrice(orderId));

        } else {
            ctx.attribute("orderItems", new ArrayList<>());
            ctx.attribute("totalAmount", 0);
            ctx.attribute("totalPrice", 0.0);
        }

        ctx.attribute("bottoms", bestilService.getBottoms());
        ctx.attribute("toppings", bestilService.getToppings());
        ctx.attribute("editing", true);

        ctx.render("/bestil.html");
    }
    
}

package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.AdminService;
import app.services.BestilService;
import app.services.UserService;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {

    private ConnectionPool connectionPool;
    private AdminService adminService;
    private BestilService bestilService;

    public AdminController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.adminService = new AdminService(connectionPool);
        this.bestilService = new BestilService(connectionPool);
    }

    public void setRoutes(JavalinConfig app){
        app.routes.get("/admin/kunder", ctx -> showCustomers(ctx));
        app.routes.get("/admin/kunder/{id}", ctx -> showCustomer(ctx));

        app.routes.get("/admin", ctx -> ctx.redirect("/admin/ordrer"));
        app.routes.get("/admin/ordrer", ctx -> showOrders(ctx));

        app.routes.post("/admin/ordrer/slet", ctx -> deleteOrder(ctx)); 
    }

    private void deleteOrder(@NotNull Context ctx) throws DatabaseException {
        if (!isAdmin(ctx)) {
            ctx.redirect("/login");
            return;
        }
        try {
            int orderId = Integer.parseInt(ctx.formParam("order_id"));
            bestilService.deleteOrderlines(orderId);
            bestilService.deleteOrder(orderId);
            ctx.redirect("/admin/ordrer");
        } catch (DatabaseException e){
            ctx.result(e.getMessage());
        }
    }

    private void showOrders(@NotNull Context ctx) throws DatabaseException {
        if (!isAdmin(ctx)) {
            ctx.redirect("/login");
            return;
        }
        try{
            Map<String, Object> model = new HashMap<>();
            model.put("user", ctx.sessionAttribute("user"));
            model.put("orders", adminService.getAllOrders());
            ctx.render("adminordre", model);
        } catch (DatabaseException e){
            ctx.result(e.getMessage());
        }
    }


    public void showCustomer(@NotNull Context ctx) throws DatabaseException {
        if (!isAdmin(ctx)) {
            ctx.redirect("/login");
            return;
        }

            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                User customer = adminService.getCustomer(id);
                List<Order> orders = adminService.getOrdersForCustomer(id);

                Map<String, Object> model = new HashMap<>();
                model.put("user", ctx.sessionAttribute("user"));
                model.put("customer", customer);
                model.put("orders", orders);
                ctx.render("kundedetaljer", model);
            } catch (IllegalArgumentException e) {
                ctx.redirect("/admin/kunder");
            } catch (DatabaseException e){
                ctx.result(e.getMessage());
            }

    }

    public void showCustomers(@NotNull Context ctx) throws DatabaseException {
        if (!isAdmin(ctx)){
            ctx.redirect("/login");
            return;
        }
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("user", ctx.sessionAttribute("user"));
            model.put("customers", adminService.getAllCostumers());
            ctx.render("adminkunde", model);
        } catch (DatabaseException e){
            ctx.result(e.getMessage());
        }
    }

    private boolean isAdmin(Context ctx){
        User user = ctx.sessionAttribute("user");
        return user != null && user.isAdmin();
    }

}

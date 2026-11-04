package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.UserService;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    private ConnectionPool connectionPool;
    private UserService userService;

    public UserController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.userService = new UserService(connectionPool);
    }

    public void setRoutes(JavalinConfig app) {
        app.routes.get("/login", ctx -> showLogin(ctx));
        app.routes.post("/login", ctx -> login(ctx));
        app.routes.get("/logout", ctx -> logout(ctx));
        app.routes.post("/updateBalance", ctx -> updateBalance(ctx));

        app.routes.get("/register", ctx -> ctx.render("register"));
        app.routes.post("/register", ctx -> register(ctx));

        app.routes.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            User user = ctx.sessionAttribute("user");
            model.put("user", user);
            ctx.render("/index", model);
        });



    }

    private void login(@NotNull Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = userService.login(email, password);
            ctx.sessionAttribute("user", user);

            if (user.isAdmin()) {
                ctx.redirect("admin/ordrer");
            } else {
                ctx.redirect("/");
            }

        } catch (DatabaseException | IllegalArgumentException e) {
            showLoginError(ctx, e.getMessage(), email);
        }

    }
    private void logout(Context ctx) {
        ctx.sessionAttribute("user", null);
        ctx.redirect("/");
    }

    private void showLogin(@NotNull Context ctx) {
        Map<String, Object> model = new HashMap<>();
        ctx.render("login");
    }

    private void showLoginError(Context ctx, String errorMessage, String email){
        Map<String, Object> model = new HashMap<>();
        model.put("error", errorMessage);
        model.put("email", email);
        ctx.render("login.html", model);
    }

    private void register(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String password2 = ctx.formParam("password2");

        try {
            userService.register(email, password, password2);
            ctx.redirect("/login");   // oprettet, så gå til login
        } catch (DatabaseException | IllegalArgumentException e) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", e.getMessage());
            model.put("email", email);
            ctx.render("register.html", model);
        }
    }
    private void updateBalance(Context ctx) {
        User user = ctx.sessionAttribute("user");
        String amountStr = ctx.formParam("updateBalanceForm");
        double amount = Double.parseDouble(amountStr);
        try {

        if (amount == 0) {
            userService.updateUserBalance(user.getEmail(), (user.getBalance()*-1));
            user.setBalance(0);
        } else {
            userService.updateUserBalance(user.getEmail(), amount);
            user.setBalance(user.getBalance() + amount);
        }
        ctx.sessionAttribute("user", user);
        ctx.redirect("/");
        } catch (DatabaseException e) {
            showLoginError(ctx, e.getMessage(), user.getEmail());
        }


    }
}

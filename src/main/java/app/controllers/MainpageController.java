package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.config.JavalinConfig;

public class MainpageController {

    private ConnectionPool connectionPool;

    public MainpageController(ConnectionPool connectionPool) {


    }

    public void setRoutes(JavalinConfig config) {
        config.routes.get("/", ctx -> ctx.render("/index.html"));
        config.routes.get("/bestil", ctx -> ctx.render("/bestil.html"));
    }


}

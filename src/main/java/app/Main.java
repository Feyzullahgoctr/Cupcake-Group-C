package app;

import app.configuration.ThymeleafConfig;
import app.controllers.BestilController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        Javalin.create(config -> {

            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));

            UserController userController = new UserController(connectionPool);
            userController.setRoutes(config);

            BestilController bestilController = new BestilController(connectionPool);
            bestilController.setRoutes(config);


        }).start(7070);

    }


}

package app.controllers;

import app.entities.Bottom;
import app.entities.Topping;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.BestilService;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;

import java.util.List;

public class BestilController {


    private ConnectionPool connectionPool;
    private BestilService bestilService;

    public BestilController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
        this.bestilService = new BestilService(connectionPool);
    }

    public void setRoutes(JavalinConfig app) {
        app.routes.get("/bestil", ctx -> bestilapprove(ctx));

    }
    
    private void bestilapprove(Context ctx) throws DatabaseException {

        List<Bottom> bottoms = bestilService.getBottoms();
        List<Topping> toppings = bestilService.getToppings();

        ctx.attribute("bottoms", bottoms);
        ctx.attribute("toppings", toppings);
        ctx.render("/bestil.html");

    }
    
}

package com.mishas.stuff.mta;

import com.mishas.stuff.mta.controller.AccountController;
import com.mishas.stuff.common.controller.ExceptionHandlerController;
import com.mishas.stuff.mta.service.impl.AccountService;


import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.port;

public class MoneyTransferApi {


    public static void main (String... args) {

        // Start embedded server at this port
        port(8080);

        // controller
        AccountController accountController = new AccountController(new AccountService());
        ExceptionHandlerController exceptionHandlerControler = new ExceptionHandlerController();

        internalServerError((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 500 handling\"}";
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });

    }
}

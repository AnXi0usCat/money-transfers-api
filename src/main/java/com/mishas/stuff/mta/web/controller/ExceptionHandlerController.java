package com.mishas.stuff.mta.web.controller;

import com.google.gson.Gson;
import com.mishas.stuff.common.interfaces.IController;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.common.utils.exceptions.MyFundsTransferException;
import com.mishas.stuff.common.utils.exceptions.MyInputValidationException;
import com.mishas.stuff.common.utils.exceptions.MyMissingResourceException;
import com.mishas.stuff.common.utils.exceptions.MyPersistenceException;

import static spark.Spark.*;

public class ExceptionHandlerController implements IController {

    public ExceptionHandlerController() {
        super();
        setupEndpoints();
    }

    @Override
    public void setupEndpoints() {

        // 400 series
        notFound((request, response) -> {
            response.status(404);
            response.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });

        // 500 series
        internalServerError((request, response) -> {
            response.status(500);
            response.type("application/json");
            return "{\"message\":\"Custom 500 handling\"}";
        });

        // custom exceptions mappings

        exception(MyPersistenceException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(mapException(exception)));
        });

        exception(MyMissingResourceException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(mapException(exception)));
        });

        exception(MyFundsTransferException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(mapException(exception)));
        });

        exception(MyInputValidationException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(mapException(exception)));
        });

    }

    // utils: get the message from our custom exceptions

    private StandardResponse mapException(Throwable ex) {
        final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = ex.getClass().getSimpleName();
        return new StandardResponse(StatusResponse.ERROR, message, devMessage);
    }
}

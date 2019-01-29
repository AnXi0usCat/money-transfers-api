package com.mishas.stuff.common.controller;

import com.google.gson.Gson;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.common.utils.exception.MyPersistenceException;

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

        exception(MyPersistenceException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application/json");
            response.body(new Gson().toJson(mapException(exception)));
        });

    }

    // get the message from out custom exceptions
    private StandardResponse mapException(Throwable ex) {
        final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = ex.getClass().getSimpleName();
        return new StandardResponse(StatusResponse.ERROR, message, devMessage);
    }
}

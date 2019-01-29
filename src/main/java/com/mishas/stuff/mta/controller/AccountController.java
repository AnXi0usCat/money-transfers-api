package com.mishas.stuff.mta.controller;

import com.google.gson.Gson;

import com.mishas.stuff.common.controller.IController;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.service.IAccountService;
import org.eclipse.jetty.http.HttpStatus;

import static spark.Spark.*;


public class AccountController implements IController {

    private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
        setupEndpoints();
    }

    public void setupEndpoints() {

        get("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);

            // construct a response object
            return new Gson().toJson(
                        new StandardResponse(StatusResponse.SUCCESS,
                                new Gson().toJsonTree(accountService.get(
                                         Integer.parseInt(request.params(":id"))
                                ))
                        )
                );
        });

        post("/api/v1/accounts", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);

            Account resource = new Gson().fromJson(request.body(), Account.class);
            accountService.create(resource);
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS,   "CREATED", null));
        });

        put("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            // your callback code
            response.type("application/json");
            Account toEdit = new Gson().fromJson(request.body(), Account.class);
            Account editedAccount = accountService.update(
                    Integer.parseInt(request.params(":id")), toEdit);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            new Gson().toJsonTree(editedAccount)
                    )
            );
        });

        delete("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            // your callback code
            response.type("application/json");
            accountService.delete(
                    Integer.parseInt(request.params(":id"))
            );
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, "DELETED", null));
        });

    }
}

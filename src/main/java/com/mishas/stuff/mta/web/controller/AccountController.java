package com.mishas.stuff.mta.web.controller;

import com.google.gson.Gson;

import com.mishas.stuff.common.interfaces.IController;
import com.mishas.stuff.common.utils.InputValidator;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.mta.service.IAccountService;
import com.mishas.stuff.mta.web.dto.AccountDto;
import org.eclipse.jetty.http.HttpStatus;

import java.io.Serializable;

import static spark.Spark.*;


public class AccountController implements IController {

    private IAccountService accountService;
    private InputValidator<AccountDto> inputValidator = new InputValidator(AccountDto.class);
    private static final String LOCATION_PATH = "/api/v1/accounts/";

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
        setupEndpoints();
    }

    public void setupEndpoints() {

        get("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);

            long inputParam = inputValidator.validatePathParam(request.params(":id"));

            // construct a response object
            return new Gson().toJson(
                        new StandardResponse(
                                StatusResponse.SUCCESS,
                                new Gson().toJsonTree(accountService.get(inputParam))
                        )
            );
        });

        post("/api/v1/accounts", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);

            AccountDto resource = inputValidator.validatePayload(request.body());
            inputValidator.isPayloadAValidDto(resource);
            // set location header
            Serializable locationKey = accountService.create(resource);
            response.header("Location", LOCATION_PATH + locationKey);
            // create response payload
            return new Gson().toJson(
                    new StandardResponse(
                            StatusResponse.SUCCESS,
                            "CREATED",
                            null)
            );
        });

        put("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);

            long inputParam = inputValidator.validatePathParam(request.params(":id"));
            AccountDto toEdit = inputValidator.validatePayload(request.body());
            inputValidator.isPayloadAValidDto(toEdit);

            AccountDto editedAccount = accountService.update(inputParam, toEdit);


            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            new Gson().toJsonTree(editedAccount)
                    )
            );
        });

        delete("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);

            long inputParam = inputValidator.validatePathParam(request.params(":id"));
            accountService.delete(inputParam);

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS, "DELETED", null));
        });

        head("/api/v1/accounts/:id", (request, response) -> {
            long param = inputValidator.validatePathParam(request.params(":id"));
            int status = accountService.accountExists(param) ? HttpStatus.OK_200 : HttpStatus.NOT_FOUND_404;
            response.status(status);
            return response;
        });

    }
}

package com.mishas.stuff.mta.controller;

import com.mishas.stuff.common.controller.IController;
import com.mishas.stuff.common.utils.JsonValidator;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.common.utils.exception.MyPayloadValidationException;
import com.mishas.stuff.mta.persistence.model.Account;
import com.mishas.stuff.mta.service.IAccountService;
import org.eclipse.jetty.http.HttpStatus;

import static spark.Spark.*;


public class AccountController implements IController {

    private IAccountService accountService;
    private JsonValidator jsonValidator;

    public AccountController(final IAccountService accountService, final JsonValidator jsonValidator) {
        this.accountService = accountService;
        this.jsonValidator = jsonValidator;
        setupEndpoints();
    }

    public void setupEndpoints() {

        get("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);
            Account resource;
            try {
                resource = accountService.get(Long.parseLong(request.params(":id")));
            } catch (NumberFormatException e) {
                throw new MyPayloadValidationException(e);
            }
            // construct a response object
            return jsonValidator.dataToJson(new StandardResponse(StatusResponse.SUCCESS, resource));
        });

        post("/api/v1/accounts", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);

            Account resource = (Account)jsonValidator.dataFromJson(request.body());
            accountService.create(resource);

            return jsonValidator.dataToJson(new StandardResponse(
                    StatusResponse.SUCCESS,
                    "CREATED",
                    null)
            );

        });

        put("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            // parse int here
            Long clientId;

            try {
                clientId = Long.parseLong(request.params(":id"));
            } catch (NumberFormatException e) {
                throw new MyPayloadValidationException(e);
            }
            Account editedAccount = accountService.update(
                    clientId,
                    (Account)jsonValidator.dataFromJson(request.body())
            );
            return jsonValidator.dataToJson(
                    new StandardResponse(
                            StatusResponse.SUCCESS,
                            editedAccount)
            );
        });

        delete("/api/v1/accounts/:id", (request, response) -> {
            response.type("application/json");
            try {
                accountService.delete(Long.parseLong(request.params(":id")));
            } catch (NumberFormatException e) {
                throw new MyPayloadValidationException(e);
            }

            return jsonValidator.dataToJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            "DELETED",
                            null)
            );
        });

    }
}

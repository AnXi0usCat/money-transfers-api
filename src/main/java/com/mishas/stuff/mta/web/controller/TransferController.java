package com.mishas.stuff.mta.web.controller;

import com.google.gson.Gson;
import com.mishas.stuff.common.interfaces.IController;
import com.mishas.stuff.common.utils.InputValidator;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.mta.service.ITransferService;
import com.mishas.stuff.mta.web.dto.TransferDto;
import org.eclipse.jetty.http.HttpStatus;

import java.io.Serializable;

import static spark.Spark.*;

public class TransferController  implements IController {

    private ITransferService transferService;
    private InputValidator<TransferDto> inputValidator = new InputValidator(TransferDto.class);
    private static final String LOCATION_PATH = "/api/v1/transfers/";

    public TransferController(ITransferService transferService) {
        this.transferService = transferService;
        setupEndpoints();
    }

    @Override
    public void setupEndpoints() {

        get("/api/v1/transfers/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);

            long inputParam = inputValidator.validatePathParam(request.params(":id"));

            // construct a response object
            return new Gson().toJson(
                    new StandardResponse(
                            StatusResponse.SUCCESS,
                            new Gson().toJsonTree(transferService.get(inputParam))
                    )
            );
        });

        post("/api/v1/transfers", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);
            TransferDto resource = inputValidator.validatePayload(request.body());
            inputValidator.isPayloadAValidDto(resource);
            // set location header
            Serializable locationKey = transferService.create(resource);
            response.header("Location", LOCATION_PATH + locationKey);
            // create response payload
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS,   "CREATED", null));
        });

        head("/api/v1/transfers/:id", (request, response) -> {
            long param = inputValidator.validatePathParam(request.params(":id"));
            int status = transferService.transferExists(param) ? HttpStatus.OK_200 : HttpStatus.NOT_FOUND_404;
            response.status(status);
            return response;
        });

    }
}

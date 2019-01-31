package com.mishas.stuff.mta.web.controller;

import com.google.gson.Gson;
import com.mishas.stuff.common.interfaces.IController;
import com.mishas.stuff.common.utils.StandardResponse;
import com.mishas.stuff.common.utils.StatusResponse;
import com.mishas.stuff.mta.persistence.model.Transfer;
import com.mishas.stuff.mta.service.ITransferService;
import org.eclipse.jetty.http.HttpStatus;

import static spark.Spark.*;

public class TransferController  implements IController {

    private ITransferService transferService;

    public TransferController(ITransferService transferService) {
        this.transferService = transferService;
        setupEndpoints();
    }

    @Override
    public void setupEndpoints() {

        get("/api/v1/transfers/:id", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.OK_200);
            Transfer transfer = transferService.get(Integer.parseInt(request.params(":id")));

            // construct a response object
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            new Gson().toJsonTree(transferService.get(
                                    Integer.parseInt(request.params(":id"))
                            ))
                    )
            );
        });

        post("/api/v1/transfers", (request, response) -> {
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);
            Transfer resource = new Gson().fromJson(request.body(), Transfer.class);
            transferService.create(resource);
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS,   "CREATED", null));
        });

    }
}

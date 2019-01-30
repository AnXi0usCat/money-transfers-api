package com.mishas.stuff.mta;

import com.mishas.stuff.mta.web.controller.AccountController;
import com.mishas.stuff.mta.web.controller.ExceptionHandlerController;
import com.mishas.stuff.mta.web.controller.TransferController;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.service.impl.TransferService;


import static spark.Spark.internalServerError;
import static spark.Spark.notFound;
import static spark.Spark.port;

public class MoneyTransferApi {


    public static void main (String... args) {

        // Start embedded server at this port
        port(8080);

        // controller
        AccountController accountController = new AccountController(new AccountService());
        TransferController transferController = new TransferController(new TransferService());
        ExceptionHandlerController exceptionHandlerControler = new ExceptionHandlerController();

    }
}

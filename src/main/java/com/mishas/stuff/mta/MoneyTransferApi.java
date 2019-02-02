package com.mishas.stuff.mta;

import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.dao.TransferRepository;

import com.mishas.stuff.mta.web.controller.AccountController;
import com.mishas.stuff.mta.web.controller.ExceptionHandlerController;
import com.mishas.stuff.mta.web.controller.TransferController;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.service.impl.TransferService;
import static spark.Spark.port;

public class MoneyTransferApi {

    public static void main (String... args) {

        // declare our services
        final  TransferRepository transferRepository = new TransferRepository();
        final  AccountRepository accountRepository = new AccountRepository();
        final  AccountService accountservice = new AccountService(accountRepository);
        final  TransferService transferService = new TransferService(transferRepository, accountservice);

        // Start embedded server at this por
        port(8080);


        // account controller
        AccountController accountController = new AccountController(accountservice);
        // transfer controller
        TransferController transferController = new TransferController(transferService);
        // exceptions handler
        ExceptionHandlerController exceptionHandlerControler = new ExceptionHandlerController();

    }
}

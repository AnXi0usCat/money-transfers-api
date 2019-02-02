package com.mishas.stuff.endtoend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.persistence.dao.TransferRepository;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.service.impl.TransferService;
import com.mishas.stuff.mta.web.controller.AccountController;
import com.mishas.stuff.mta.web.controller.ExceptionHandlerController;
import com.mishas.stuff.mta.web.controller.TransferController;
import com.mishas.stuff.mta.web.dto.AccountDto;
import com.mishas.stuff.mta.web.dto.TransferDto;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TransferControllerTest extends TestHttpClient {

    private static TransferController transferController;
    private static TransferService transferService;
    private static TransferRepository transferRepository;
    private static AccountService accountService;
    private static AccountRepository accountRepository;
    // we need this because it handles exceptions
    private static ExceptionHandlerController exceptionHandlerController;

    @Before
    public void before() {

        accountRepository = new AccountRepository();
        transferRepository = new TransferRepository();
        accountService = new AccountService(accountRepository);
        transferService = new TransferService(transferRepository, accountService);
        transferController = new TransferController(transferService);
        exceptionHandlerController = new ExceptionHandlerController();
        exceptionHandlerController.setupEndpoints();
        transferController.setupEndpoints();
        Spark.awaitInitialization();
    }

    @Test
    public void asdf() {}
    
    // helper methods

    private TransferDto convertPayloadToObject(HttpResponse response) throws IOException {
        JsonObject accountDtoJson = null;
        JsonElement element = super.convertPayloadToJsonElement(response);
        if (element.isJsonObject()) {
            accountDtoJson = element.getAsJsonObject().get("data").getAsJsonObject();
            System.out.println(accountDtoJson);
        }
        return new Gson().fromJson(accountDtoJson, TransferDto.class);
    }
}

package com.mishas.stuff.endtoend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.mta.persistence.dao.AccountRepository;
import com.mishas.stuff.mta.service.impl.AccountService;
import com.mishas.stuff.mta.web.controller.AccountController;
import com.mishas.stuff.mta.web.controller.ExceptionHandlerController;
import com.mishas.stuff.mta.web.dto.AccountDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


import static org.junit.Assert.assertEquals;


public class AccountControllerTest extends TestHttpClient {

    private static AccountController accountController;
    private static AccountService accountService;
    private static AccountRepository accountRepository;
    // we need this because it handles errors
    private static ExceptionHandlerController exceptionHandlerController;

    @Before
    public void before() {
        accountRepository = new AccountRepository();
        accountService = new AccountService(accountRepository);
        accountController = new AccountController(accountService);
        exceptionHandlerController = new ExceptionHandlerController();
        exceptionHandlerController.setupEndpoints();
        accountController.setupEndpoints();
        Spark.awaitInitialization();
    }

    /*
        Get account that doesn't exist, should get 200 OK and empty "data" in payload
     */
    @Test
    public void testGetMethodWhenAccountDoesntExist_return200OK() throws Exception {
        URI uri = builder.setPath("/accounts/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }

    /*
        Get Account with a malformed URL, , should get 400 BAD REQUEST and corresponding message
     */
    @Test
    public void testGetMethodWhenAccountwithMalformedUrl_return400BadRequest() throws Exception {
        URI uri = builder.setPath("/accounts/xcwefrrvcdfvdfvdfvfv").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        var errorBody = convertPayloadToMessage(response);
        assertEquals(errorBody.get("message"),  "java.lang.NumberFormatException: For input string: \"xcwefrrvcdfvdfvdfvfv\"");
        assertEquals(errorBody.get("devMessage"),  "MyInputValidationException");
    }

    /*
        Get account that does exist, should get the 200 OK and the json payload
     */
    @Test
    public void testGetMethodWhenAccountExists_receivePayload() throws Exception {
        // create a new account
        URI uri = builder.setPath("/accounts").build();
        AccountDto newAccount = new AccountDto(1L, "EUR", new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);

        // get the account back
        uri = builder.setPath("/accounts/1").build();
        HttpGet requestGet = new HttpGet(uri);
        response = httpClient.execute(requestGet);
        statusCode = response.getStatusLine().getStatusCode();


        AccountDto result = convertPayloadToObject(response);
        assertEquals(200, statusCode);
        assertEquals(newAccount.getId(), result.getId());
        assertEquals(newAccount.getCurrency(), result.getCurrency());
        assertEquals(newAccount.getBalance().compareTo(result.getBalance().setScale(4, RoundingMode.HALF_EVEN)), 0);
    }

    /*
         Create account with a mandatory field missing, should get 400 BAD REQUEST and corresponding message
     */
    @Test
    public void testPostAccountWithCurrencyNull_return400BadRquest() throws Exception{
        // create a new account
        URI uri = builder.setPath("/accounts").build();
        AccountDto newAccount = new AccountDto( null, new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        var errorBody = convertPayloadToMessage(response);
        assertEquals(errorBody.get("message"),  "Not a valid input Payload: AccountDto{id=null, currency='null', balance=100}");
        assertEquals(errorBody.get("devMessage"),  "MyInputValidationException");
    }

    /*
        Create account with malformed payload, should get 400 BAD REQUEST and corresponding message
     */
    @Test
    public void testPostAccountWithBadPayload_return400BadRequest() throws Exception{
        URI uri = builder.setPath("/accounts").build();

        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(new StringEntity("{ cats: dogs, all hell broke loose"));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        var errorBody = convertPayloadToMessage(response);
        assertEquals( "MyInputValidationException", errorBody.get("devMessage"));
    }

    /*
        Create accoun, should return 201 CREATED, with message CREATED
     */
    @Test
    public void testPostAccount_return201Created() throws Exception{
        // create a new account
        URI uri = builder.setPath("/accounts").build();
        AccountDto newAccount = new AccountDto( "EUR", new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        assertEquals("CREATED", element.getAsJsonObject().get("message").getAsString());

    }

    // helper methods

    private StringEntity convertObjectToPayload(IValidDto resource) throws UnsupportedEncodingException {
        String jsonInString = new Gson().toJson(resource);
        return new StringEntity(jsonInString);
    }

    private JsonElement convertPayloadToJsonElement (HttpResponse response) throws IOException{
        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser parser = new JsonParser();
        return parser.parse(jsonString);
    }

    private AccountDto convertPayloadToObject(HttpResponse response) throws IOException {
        JsonObject accountDtoJson = null;
        JsonElement element = convertPayloadToJsonElement(response);
        if (element.isJsonObject()) {
            accountDtoJson = element.getAsJsonObject().get("data").getAsJsonObject();
        }
        return new Gson().fromJson(accountDtoJson, AccountDto.class);
    }

    private Map<String, String> convertPayloadToMessage(HttpResponse response) throws IOException {
        JsonElement element = convertPayloadToJsonElement(response);
        var resultMap = new HashMap<String, String>();
        if (element.isJsonObject()) {
            resultMap.put("message", element.getAsJsonObject().get("message").getAsString());
            resultMap.put("devMessage", element.getAsJsonObject().get("devMessage").getAsString());
        }
        return resultMap;
    }
}

package com.mishas.stuff.endtoend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.*;
import spark.Spark;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class EndToEndApiTest extends TestHttpClient {

    private static TransferController transferController;
    private static TransferService transferService;
    private static TransferRepository transferRepository;
    private static AccountService accountService;
    private static AccountRepository accountRepository;
    // we need this because it handles exceptions
    private static ExceptionHandlerController exceptionHandlerController;
    // we need this to create accounts
    private static AccountController accountController;



    @Before
    public void before() {
        accountRepository = new AccountRepository();
        transferRepository = new TransferRepository();
        accountService = new AccountService(accountRepository);
        transferService = new TransferService(transferRepository, accountService);
        transferController = new TransferController(transferService);
        exceptionHandlerController = new ExceptionHandlerController();
        accountController = new AccountController(accountService);
        exceptionHandlerController.setupEndpoints();
        transferController.setupEndpoints();
        Spark.awaitInitialization();
    }

    /*
        get transfer that doesn't exist, get a 200 OK
     */
    @Test
    public void testGetMethodWhenTransferDoesntExist_return200OK() throws Exception {
        URI uri = builder.setPath("/api/v1/transfers/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }

    /*
        create a balance transfer
        - make sure the transfer was created successfully: status 201 CREATED
        - make that account balance was transferred successfully: src 0, dst 100
        - make sure that transfer object exists in the database
     */
    @Test
    public void testPostMethodWhenTransferIsSuccessfull_AccountBalanceShouldChange() throws Exception {
        // create two new accountsa
        AccountDto srcAccount = new AccountDto("EUR", new BigDecimal(100));
        AccountDto dstAccount = new AccountDto("EUR", new BigDecimal(0));
        String[] locations = createSrcAndDstAccounts(srcAccount, dstAccount);
        String srcLocation = locations[0];
        String dstLocation = locations[1];

        TransferDto transferDto = new TransferDto(
                Long.parseLong(srcLocation.substring(srcLocation.length()-1)),
                Long.parseLong(dstLocation.substring(dstLocation.length()-1)),
                "EUR",
                new BigDecimal(100)
        );

        // create a new balance transfer
        URI uri = builder.setPath("/api/v1/transfers").build();
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(transferDto));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String transferLocation = response.getFirstHeader("Location").getValue();

        assertEquals(201, statusCode);   // make sure transfer was successful

        // get back the accounts, make sure that balance on
        // Src account is 0 and on the Dst Account is 100
        uri = builder.setPath(srcLocation).build();
        HttpGet request = new HttpGet(uri);
        response = httpClient.execute(request);
        AccountDto accountDtoSrcAfterTransfer = convertPayloadToObject(response);

        uri = builder.setPath(dstLocation).build();
        request = new HttpGet(uri);
        response = httpClient.execute(request);
        AccountDto accountDtoDstAfterTransfer = convertPayloadToObject(response);

        // src account balance is 0
        assertEquals(0, accountDtoSrcAfterTransfer.getBalance().doubleValue(), 0.001);
        // dst account balance
        assertEquals(100, accountDtoDstAfterTransfer.getBalance().doubleValue(), 0.001);

        // get the transfer object from the database, make sure it exists
        uri = builder.setPath(transferLocation).build();
        request = new HttpGet(uri);
        response = httpClient.execute(request);
        statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        assertTrue(convertPayloadToTransferObject(response) != null); // get the transfer object from the database, make sure it exists

    }

    /*
        create source and destination accounts and returns location list
     */
    public String[] createSrcAndDstAccounts(AccountDto srcAccount, AccountDto dstAccount) throws Exception {
        String[] locationList = new String[2];
        URI uri = builder.setPath("/api/v1/accounts").build();
        // create a source account
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(srcAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);
        locationList[0] = response.getFirstHeader("Location").getValue();

        // create destination
        requestPost.setEntity(convertObjectToPayload(dstAccount));
        response = httpClient.execute(requestPost);
        locationList[1] = response.getFirstHeader("Location").getValue();
        return locationList;
    }

    /*
    Get account that doesn't exist, should get 200 OK and empty "data" in payload
 */
    @Test
    public void testGetMethodWhenAccountDoesntExist_return200OK() throws Exception {
        URI uri = builder.setPath("/api/v1/accounts/1").build();
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
        URI uri = builder.setPath("/api/v1/accounts/xcwefrrvcdfvdfvdfvfv").build();
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
        URI uri = builder.setPath("/api/v1/accounts").build();
        AccountDto newAccount = new AccountDto( "EUR", new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);

        // get the account back
        String location = response.getFirstHeader("Location").getValue();

        uri = builder.setPath(location).build();
        HttpGet requestGet = new HttpGet(uri);
        response = httpClient.execute(requestGet);
        statusCode = response.getStatusLine().getStatusCode();

        AccountDto result = convertPayloadToObject(response);
        assertEquals(200, statusCode);
        assertEquals(location.substring(location.length()-1 ), result.getId().toString());
        assertEquals(newAccount.getCurrency(),  result.getCurrency());
        assertEquals(newAccount.getBalance().compareTo(result.getBalance().setScale(4, RoundingMode.HALF_EVEN)), 0);
    }

    /*
         Create account with a mandatory field missing, should get 400 BAD REQUEST and corresponding message
     */
    @Test
    public void testPostAccountWithCurrencyNull_return400BadRquest() throws Exception{
        // create a new account
        URI uri = builder.setPath("/api/v1/accounts").build();
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
        URI uri = builder.setPath("/api/v1/accounts").build();

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
        Create account, should return 201 CREATED, with message CREATED
     */
    @Test
    public void testPostAccount_return201Created() throws Exception{
        // create a new account
        URI uri = builder.setPath("/api/v1/accounts").build();
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

    /*
        Update  account that doesn't exist, should get 200 OK and empty "data" in payload
     */
    @Test
    public void testPutMethodWhenAccountDoesntExist_return200OK() throws Exception {
        AccountDto updateAccount = new AccountDto( "EUR", new BigDecimal(100));
        URI uri = builder.setPath("/api/v1/accounts/1").build();
        HttpPut request = new HttpPut(uri);
        request.setEntity(convertObjectToPayload(updateAccount));
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }


    /*
     Update  account, should get 200 OK and updated account
     */
    @Test
    public void testPutMethodWhenAccountExist_returnPayload() throws Exception {
        // create a new account
        URI uri = builder.setPath("/api/v1/accounts").build();
        AccountDto newAccount = new AccountDto(1L, "EUR", new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);

        // update accounts payload (+ 100 EUR)
        AccountDto updateAccount = new AccountDto( 1L, "EUR", new BigDecimal(200));
        uri = builder.setPath("/api/v1/accounts/1").build();
        HttpPut request = new HttpPut(uri);
        request.setEntity(convertObjectToPayload(updateAccount));
        response = httpClient.execute(request);
        statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        // the updated balance is 200
        assertEquals(200, updateAccount.getBalance().doubleValue(), 0.001);
    }

    /*
        deleting nonexistent account returns 200 OK and empty payload
     */
    @Test
    public void testDeleteMethodWhenAccountDoesntExist_return200OK() throws Exception {
        URI uri = builder.setPath("/api/v1/accounts/1").build();
        HttpDelete request = new HttpDelete(uri);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }

    /*
        deleting an account returns 200 OK and message DELETED
     */
    @Test
    public void testDeleteMethodWhenAccountExists_returns200OKAndMessage() throws Exception {
        // create a new account
        URI uri = builder.setPath("/api/v1/accounts").build();
        AccountDto newAccount = new AccountDto( "EUR", new BigDecimal(100));
        HttpPost requestPost = new HttpPost(uri);
        requestPost.setHeader("Content-type", "application/json");
        requestPost.setEntity(convertObjectToPayload(newAccount));
        HttpResponse response = httpClient.execute(requestPost);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(201, statusCode);

        uri = builder.setPath("/api/v1/accounts/1").build();
        HttpDelete request = new HttpDelete(uri);
        response = httpClient.execute(request);
        statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        assertEquals("DELETED", element.getAsJsonObject().get("message").getAsString());
    }

    // helper methods
    private TransferDto convertPayloadToTransferObject(HttpResponse response) throws IOException {
        JsonObject accountDtoJson = null;
        JsonElement element = super.convertPayloadToJsonElement(response);
        if (element.isJsonObject()) {
            accountDtoJson = element.getAsJsonObject().get("data").getAsJsonObject();
        }
        return new Gson().fromJson(accountDtoJson, TransferDto.class);
    }

}

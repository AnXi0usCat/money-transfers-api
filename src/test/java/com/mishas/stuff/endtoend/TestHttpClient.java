package com.mishas.stuff.endtoend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mishas.stuff.common.interfaces.IValidDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Spark;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public abstract class TestHttpClient {

    static HttpClient httpClient;
    static PoolingHttpClientConnectionManager httpClientConnectionManager;
    URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8080");

    @BeforeClass
    public static void beforeClass() {
        httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setDefaultMaxPerRoute(100);
        httpClientConnectionManager.setMaxTotal(200);
        httpClient = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
        Spark.port(8080);
    }


    @AfterClass
    public static void closeClient() throws Exception {
        Spark.stop();
        HttpClientUtils.closeQuietly(httpClient);
    }

     StringEntity convertObjectToPayload(IValidDto resource) throws UnsupportedEncodingException {
        String jsonInString = new Gson().toJson(resource);
        return new StringEntity(jsonInString);
    }

     JsonElement convertPayloadToJsonElement (HttpResponse response) throws IOException {
        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser parser = new JsonParser();
        return parser.parse(jsonString);
    }

     Map<String, String> convertPayloadToMessage(HttpResponse response) throws IOException {
        JsonElement element = convertPayloadToJsonElement(response);
        var resultMap = new HashMap<String, String>();
        if (element.isJsonObject()) {
            resultMap.put("message", element.getAsJsonObject().get("message").getAsString());
            resultMap.put("devMessage", element.getAsJsonObject().get("devMessage").getAsString());
        }
        return resultMap;
    }
}

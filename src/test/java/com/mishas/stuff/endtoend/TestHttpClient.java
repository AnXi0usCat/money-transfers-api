package com.mishas.stuff.endtoend;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Spark;


public class TestHttpClient {

    static HttpClient httpClient;
    static PoolingHttpClientConnectionManager httpClientConnectionManager;
    URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8080/api/v1/");

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
}

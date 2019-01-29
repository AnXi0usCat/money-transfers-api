package com.mishas.stuff.common.utils;

import com.google.gson.JsonElement;

public class StandardResponse {

    private StatusResponse status;
    private String message;
    private String devMessage;
    private JsonElement data;

    public StandardResponse(StatusResponse status) {
        this.status = status;
    }
    public StandardResponse(StatusResponse status, String message, String devMessage) {
        this.status = status;
        this.message = message;
        this.devMessage = devMessage;
    }
    public StandardResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    // getters and setters
}
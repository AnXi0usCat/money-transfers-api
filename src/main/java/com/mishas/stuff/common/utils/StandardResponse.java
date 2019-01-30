package com.mishas.stuff.common.utils;

import com.mishas.stuff.common.persistence.IEntity;


public class StandardResponse {

    private StatusResponse status;
    private String message;
    private String devMessage;
    private IEntity data;

    public StandardResponse(StatusResponse status) {
        this.status = status;
    }
    public StandardResponse(StatusResponse status, String message, String devMessage) {
        this.status = status;
        this.message = message;
        this.devMessage = devMessage;
    }
    public StandardResponse(StatusResponse status, IEntity data) {
        this.status = status;
        this.data = data;
    }

    // getters and setters
}
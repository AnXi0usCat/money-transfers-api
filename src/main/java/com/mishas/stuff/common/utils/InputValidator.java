package com.mishas.stuff.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mishas.stuff.common.interfaces.IValidDto;
import com.mishas.stuff.common.utils.exceptions.MyInputValidationException;

public class InputValidator<T extends IValidDto> {

    private Class<T> clazz;

    public InputValidator(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * @param input string version of the numeric ID
     * @return Long id
     */
    public long validatePathParam(Object input) {
        long inputParam;
        try {
            inputParam = Long.parseLong((String) input);
        } catch (NumberFormatException | NullPointerException ex) {
            throw new MyInputValidationException(ex);
        }
        return inputParam;
    }

    /**
     * @param input Json String (Hopefully)
     * @param <T> Object which implements IValidDto interface
     * @return instance of Object which implements IValidDto
     */
    public <T extends IValidDto> T validatePayload(Object input) {
        T resource;
        try {
            resource = (T) new Gson().fromJson((String)input, clazz);
        } catch (JsonParseException je) {
            throw new MyInputValidationException(je);
        }
        return resource;
    }

    /**
     * @param resource instance of Object which implements IValidDto
     */
    public <T extends IValidDto> void isPayloadAValidDto(T resource) {
        if (!resource.isValid()) {
            throw new MyInputValidationException("Not a valid input Payload: " + resource.toString());
        }
    }
}

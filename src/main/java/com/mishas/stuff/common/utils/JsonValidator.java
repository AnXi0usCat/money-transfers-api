package com.mishas.stuff.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mishas.stuff.common.persistence.IValidDto;
import com.mishas.stuff.common.utils.exception.MyPayloadValidationException;
import com.mishas.stuff.mta.persistence.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

public class JsonValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonValidator.class);

    public  String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
           // mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            //StringWriter sw = new StringWriter();
            //mapper.writeValue(sw, data);

            return mapper.writeValueAsString(data);
            //return sw.toString();
        } catch (IOException e){
            LOGGER.error("Could not convert to Json: " + e.getLocalizedMessage());
            throw new MyPayloadValidationException(e);
        }
    }

    public  <T extends IValidDto> T dataFromJson(String json) {
        T unmarshallerObject;
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            unmarshallerObject = (T) mapper.readValue(json, Account.class);
            if (!unmarshallerObject.isValid()) {
                LOGGER.error("Invalid json payload: " + unmarshallerObject.toString());
                throw new MyPayloadValidationException("Invalid json payload: " + unmarshallerObject.toString());
            }

         } catch (IOException e) {
            LOGGER.error("Could not convert from Json: " + e.getLocalizedMessage());
            throw new MyPayloadValidationException(e);
        }
        return unmarshallerObject;
    }
}

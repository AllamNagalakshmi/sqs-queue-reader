package com.example.demo.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import java.util.Set;

@Service
public class ValidatorService {

    @Autowired
    JsonSchema bonusPointSchema;

    public Set<ValidationMessage> validateJson(String json) {
        return bonusPointSchema.validate(json, InputFormat.JSON);
    }

}

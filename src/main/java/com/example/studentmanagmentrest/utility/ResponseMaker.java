package com.example.studentmanagmentrest.utility;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMaker {
    static final   Gson gson = new Gson();

    public static ResponseEntity<String> makeCannot(String response) {
        if ("cannot".equals(response.split("\\s+")[2])) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }

    public static ResponseEntity<String> makeSuccess(String response) {
        if ("successfully".equals(response.trim().split("\\s+")[2])) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> makeSaved(String response) {
        if ("saved".equals(response.split("\\s+")[2])) {
            return new ResponseEntity<>(gson.toJson(response), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }
}

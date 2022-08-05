package com.example.studentmanagmentrest.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseMaker {

    public static ResponseEntity<String> makeCannot(String response) {
        if ("cannot".equals(response.split("\\s+")[2])) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<String> makeSuccess(String response) {
        if ("successfully".equals(response.trim().split("\\s+")[2])) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> makeSaved(String response) {
        if ("saved".equals(response.split("\\s+")[2])) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

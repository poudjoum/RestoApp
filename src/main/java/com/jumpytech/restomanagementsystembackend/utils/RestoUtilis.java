package com.jumpytech.restomanagementsystembackend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestoUtilis {
    private  RestoUtilis(){

    }
    public static ResponseEntity<String> getResponseEnity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}

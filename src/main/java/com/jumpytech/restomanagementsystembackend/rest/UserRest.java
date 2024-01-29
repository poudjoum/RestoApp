package com.jumpytech.restomanagementsystembackend.rest;

import com.jumpytech.restomanagementsystembackend.wrapper.UserWrapper;
import org.apache.catalina.LifecycleState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")

public interface UserRest {
    @PostMapping(path="/signup")
    ResponseEntity<String> signUp(@RequestBody() Map<String,String> requestMap );
    @PostMapping(path="/login")
     ResponseEntity<String>login(@RequestBody()Map<String,String>requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<UserWrapper>>getAllUser();
    @PostMapping(path = "/update")
    ResponseEntity<String>update(@RequestBody(required = true) Map<String,String>requestMap);

}

package com.jumpytech.restomanagementsystembackend.rest;

import com.jumpytech.restomanagementsystembackend.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping("/add")
    ResponseEntity<String>addNewCategory(@RequestBody(required = true) Map<String,String>requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String>updateCategory(@RequestBody(required = true)Map<String,String>requestMap);
}

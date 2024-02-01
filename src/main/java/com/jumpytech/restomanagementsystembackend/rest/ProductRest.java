package com.jumpytech.restomanagementsystembackend.rest;

import com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {
@RequestMapping(path = "/add")
    ResponseEntity<String> addNewProduct(@RequestBody(required = true) Map<String,String> requestMap);
    @GetMapping(path = "/get")
    ResponseEntity<List<ProductWrapper>>getAllProduct();

    @PostMapping(path = "/update")
    ResponseEntity<String>updateProduct(@RequestBody(required=true)Map<String,String> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String>deleteProduct(@PathVariable Integer id);

    @PostMapping(path = "/updateStatus")
    ResponseEntity<String>updateStatus(@RequestBody Map<String,String> requestMap);

    @GetMapping(path = "/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>>getByCategory(@PathVariable Integer id);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);
}

package com.jumpytech.restomanagementsystembackend.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWrapper {
    Integer id;
    String name;
    String description;
    Integer price;
    String status;
    Integer categoryId;
    String categoryName;

    public ProductWrapper(Integer id,String name){
        super();
        this.id=id;
        this.name=name;
    }
    public ProductWrapper(Integer id, String name, String description, Integer price){
        this.id=id;
        this.name=name;
        this.description=description;
        this.price=price;
    }
}

package com.jumpytech.restomanagementsystembackend.dao;

import com.jumpytech.restomanagementsystembackend.POJO.Product;
import com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product,Integer> {

    @Query(value = "select new com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p ")
    List<ProductWrapper> getAllProduct();

    @Transactional
    @Modifying
    @Query(value = "update Product p set p.status=:status where p.id=:id")
   Integer updateProductStatus(@Param("status")String status, @Param("id") Integer id);
    @Query(value ="select new com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    @Query("select new com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id ")

    ProductWrapper getProductById(@Param("id") Integer id);
}

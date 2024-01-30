package com.jumpytech.restomanagementsystembackend.dao;

import com.jumpytech.restomanagementsystembackend.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {

    List<Category> getAllCategory();
}

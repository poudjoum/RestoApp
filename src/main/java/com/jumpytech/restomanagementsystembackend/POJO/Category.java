package com.jumpytech.restomanagementsystembackend.POJO;

import jdk.jfr.Name;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
@NamedQuery(name = "Category.getAllCategory",query = "Select c from Category c")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "Category")

public class Category implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;


}

package com.jumpytech.restomanagementsystembackend.POJO;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private  String name;
    @Column(name="contactName")
    private String contactNumber;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="status")
    private String status;
    @Column(name="role")
    private String role;


}

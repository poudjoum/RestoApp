package com.jumpytech.restomanagementsystembackend.dao;

import com.jumpytech.restomanagementsystembackend.POJO.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface BillDao extends JpaRepository<Bill,Integer> {
    @Query(value = "select b from Bill b order by b.id desc")
    List<Bill> getBills();
    @Query(value = "Select b from Bill b where b.createdBy=:userName")
    List<Bill> getBillByUserName(@Param(value = "userName") String currentUser);
}

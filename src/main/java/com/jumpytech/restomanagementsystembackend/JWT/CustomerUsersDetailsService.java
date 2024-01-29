package com.jumpytech.restomanagementsystembackend.JWT;

import com.jumpytech.restomanagementsystembackend.dao.UserDao;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Service
@Slf4j

public class CustomerUsersDetailsService implements UserDetailsService {
    @Getter
    private  com.jumpytech.restomanagementsystembackend.POJO.User userDetail;
    private final UserDao userDao;

    public CustomerUsersDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside loadUserByUsername {}",username);
        userDetail= userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not Found");
    }
}

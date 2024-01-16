package com.jumpytech.restomanagementsystembackend.serviceImpl;

import com.jumpytech.restomanagementsystembackend.POJO.User;
import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.dao.UserDao;
import com.jumpytech.restomanagementsystembackend.service.UserService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

private UserDao userDao;


    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try {
            if (validateSignupMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return RestoUtilis.getResponseEnity("Successfully Registered in Database", HttpStatus.OK);
                } else {
                    return RestoUtilis.getResponseEnity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtilis.getResponseEnity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    private boolean validateSignupMap(Map<String,String> requestMap){
       if (requestMap.containsKey("name")&&requestMap.containsKey("contactNumber")&&requestMap.containsKey("email")&&requestMap.containsKey("password")){
           return true;
       }
       return  false;
    }
    private User getUserFromMap(Map<String,String>requestMap){
        User user= new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}

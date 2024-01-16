package com.jumpytech.restomanagementsystembackend.restImpl;

import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.rest.UserRest;
import com.jumpytech.restomanagementsystembackend.service.UserService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Data
@AllArgsConstructor
public class UserRestImpl implements UserRest {
    private UserService userService;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signup(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

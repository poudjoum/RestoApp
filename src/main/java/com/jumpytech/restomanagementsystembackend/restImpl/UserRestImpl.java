package com.jumpytech.restomanagementsystembackend.restImpl;

import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.rest.UserRest;
import com.jumpytech.restomanagementsystembackend.service.UserService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import com.jumpytech.restomanagementsystembackend.wrapper.UserWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
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


	@Override
	public ResponseEntity<String>login(Map<String, String>requestMap) {
		
		try {
			return userService.login(requestMap);
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		 return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            return userService.getAllUser();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
                return userService.update(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

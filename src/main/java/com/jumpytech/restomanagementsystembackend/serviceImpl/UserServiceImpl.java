package com.jumpytech.restomanagementsystembackend.serviceImpl;

import com.jumpytech.restomanagementsystembackend.JWT.CustomerUsersDetailsService;
import com.jumpytech.restomanagementsystembackend.JWT.JwtFilter;
import com.jumpytech.restomanagementsystembackend.JWT.JwtUtil;
import com.jumpytech.restomanagementsystembackend.POJO.User;
import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.dao.UserDao;
import com.jumpytech.restomanagementsystembackend.service.UserService;
import com.jumpytech.restomanagementsystembackend.utils.EmailUtils;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import com.jumpytech.restomanagementsystembackend.wrapper.UserWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j

public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    AuthenticationManager authenticationManager;
    CustomerUsersDetailsService cust;
    JwtUtil jwtUtil;
    JwtFilter jwtFilter;
    final
    EmailUtils emailUtils;


    public UserServiceImpl(UserDao userDao, AuthenticationManager authenticationManager, CustomerUsersDetailsService cust, JwtUtil jwtUtil, JwtFilter jwtFilter, EmailUtils emailUtils) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.cust = cust;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.emailUtils = emailUtils;
    }
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
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password");
    }
    private User getUserFromMap(Map<String,String>requestMap){
        User user= new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("admin");
        return user;
    }
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside Login");
		try {
			Authentication auth=authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
			if(auth.isAuthenticated()) {
				if(cust.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(cust.getUserDetail().getEmail(),
                            cust.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for Admin approval." + "\"}", HttpStatus.BAD_REQUEST);
				}
				
			}
			
		}catch(Exception ex) {
			log.error("{}",ex);
		}
		return new ResponseEntity<>("{\"message\":\"" + "Bad credentials." + "\"}", HttpStatus.BAD_REQUEST);
	}
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);

            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
               Optional<User> optional=userDao.findById(Integer.parseInt(requestMap.get("id")));
               sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
               if(!optional.isEmpty()){
                   userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                   return RestoUtilis.getResponseEnity("User status updates successfully",HttpStatus.OK);

               }else{
                   return RestoUtilis.getResponseEnity("User id doesn't exist ",HttpStatus.OK);
               }

            }else {
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private void sendMailToAllAdmin(String status, String user ,List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","USER:-"+user+"\n is Approved by \n ADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);

        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disable","USER:-"+user+"\n is Disable by \n ADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

}

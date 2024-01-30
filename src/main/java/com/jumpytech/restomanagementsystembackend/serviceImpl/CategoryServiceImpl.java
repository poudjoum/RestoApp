package com.jumpytech.restomanagementsystembackend.serviceImpl;

import com.google.common.base.Strings;
import com.jumpytech.restomanagementsystembackend.JWT.JwtFilter;
import com.jumpytech.restomanagementsystembackend.POJO.Category;
import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.dao.CategoryDao;
import com.jumpytech.restomanagementsystembackend.service.CategoryService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private CategoryDao categoryDao;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
              if(validateCategoryMap(requestMap,false)){
                  categoryDao.save(getCategoryFromMap(requestMap,false));
                  return RestoUtilis.getResponseEnity("Category Added Successfully!! ",HttpStatus.OK);

                }
            }else{
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String>requestMap, Boolean isAdd){
        Category category=new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));

        }
        category.setName(requestMap.get("name"));
        return category;
    }
    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if(!Strings.isNullOrEmpty(filterValue)&&filterValue.equalsIgnoreCase("true")){
                log.info("Inside First ");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
            }
            return  new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestMap,true)){
                   Optional optional= categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                   if(!optional.isEmpty()){
                       categoryDao.save(getCategoryFromMap(requestMap,true));
                       return RestoUtilis.getResponseEnity("Category Updated Successfully! ",HttpStatus.OK);
                   }else{
                       RestoUtilis.getResponseEnity("Category id does not exist",HttpStatus.OK);
                   }
                }
                return RestoUtilis.getResponseEnity(RestoConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else {
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

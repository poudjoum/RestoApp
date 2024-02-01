package com.jumpytech.restomanagementsystembackend.serviceImpl;

import com.jumpytech.restomanagementsystembackend.JWT.JwtFilter;
import com.jumpytech.restomanagementsystembackend.POJO.Category;
import com.jumpytech.restomanagementsystembackend.POJO.Product;
import com.jumpytech.restomanagementsystembackend.constants.RestoConstants;
import com.jumpytech.restomanagementsystembackend.dao.ProductDao;
import com.jumpytech.restomanagementsystembackend.service.ProductService;
import com.jumpytech.restomanagementsystembackend.utils.RestoUtilis;
import com.jumpytech.restomanagementsystembackend.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductFromMap(requestMap,false)){
                    productDao.save(getProductFromMap(requestMap,false));
                    return RestoUtilis.getResponseEnity("Product Added Successfully !",HttpStatus.OK);
                }
                return  RestoUtilis.getResponseEnity(RestoConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else{
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return  new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductFromMap(requestMap,true)){
                    Optional<Product> optional=productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        Product product=getProductFromMap(requestMap,true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return RestoUtilis.getResponseEnity("Product Updated successfully",HttpStatus.OK);
                    }else {
                        return RestoUtilis.getResponseEnity("Product id does not Exist",HttpStatus.OK);
                    }

                }else{
                    return RestoUtilis.getResponseEnity(RestoConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }

            }
            return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                Optional optional=productDao.findById(id);
                if(!optional.isEmpty()){
                    productDao.deleteById(id);
                    return RestoUtilis.getResponseEnity("Product deleted Successfully!",HttpStatus.OK);
                }else {
                    return RestoUtilis.getResponseEnity("Product id does not Exist.",HttpStatus.OK);
                }


            }else {
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional optional=productDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    return RestoUtilis.getResponseEnity("Product status updated Successfully!",HttpStatus.OK);
                }else {
                    return RestoUtilis.getResponseEnity("Product id does not Exist.",HttpStatus.OK);
                }

            }else {
                return RestoUtilis.getResponseEnity(RestoConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestoUtilis.getResponseEnity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try{
            return  new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category=new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product=new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateProductFromMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id")&&validateId){
                return true;
            } else if (!validateId) {
                return true;

            }

        }
        return false;
    }
}

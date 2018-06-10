package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.controller.backend.ProductManageController;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {


    @Autowired
    ProductMapper productMapper;
    public ServerResponse saveOrUpdateProduct(Product product){
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String [] subImageArray = product.getSubImages().split(",");
                product.setMainImage(subImageArray[0]);
            }
            if(product.getId()!=null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccess("更新产品成功");
                }else {
                    return ServerResponse.createByErrorMessage("更新产品失败");
                }
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createBySuccess("新增产品失败");
            }


        }
        return ServerResponse.createByErrorMessage("更新或者新增产品传入的参数错误");
    }

    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        if(productId==null|| status==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }
}

package com.fubuki.warship.service.impl;

import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.ProductMapper;
import com.fubuki.warship.model.pojo.Category;
import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.request.AddProductReq;
import com.fubuki.warship.model.request.UpdateProductReq;
import com.fubuki.warship.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void add(AddProductReq addProductReq) {
        Product product=new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new WarshipException(WarshipExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product product=new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        if (updateProductReq.getName() != null) {
            Product productOld = productMapper.selectByName(product.getName());
            //名称相同而ID不同，则否决修改
            if (productOld != null && !productOld.getId().equals(product.getId())) {
                throw new WarshipException(WarshipExceptionEnum.NAME_EXISTED);
            }
        }
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void delete(Long id) {
        Product productOld=productMapper.selectByPrimaryKey(id);
        if(productOld==null){
            throw new WarshipException(WarshipExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Long[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids,sellStatus);

    }
}

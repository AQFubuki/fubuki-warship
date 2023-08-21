package com.fubuki.warship.service.impl;

import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.ProductMapper;
import com.fubuki.warship.model.pojo.Category;
import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.query.ProductListQuery;
import com.fubuki.warship.model.request.AddProductReq;
import com.fubuki.warship.model.request.ProductListReq;
import com.fubuki.warship.model.request.UpdateProductReq;
import com.fubuki.warship.model.vo.CategoryVO;
import com.fubuki.warship.service.CategoryService;
import com.fubuki.warship.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, CategoryService categoryService) {
        this.productMapper = productMapper;
        this.categoryService = categoryService;
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

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "category_id, status, id");
        List<Category> categoryList = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    public Product detail(Long id) {
        Product product= productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理 %关键词%
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword())
                    .append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，
        // 还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService
                    .listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Long> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ORDER_ENUM.contains(orderBy)) {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }
    //将categoryVOList中树状结构的id全都存放到list中
    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Long> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }
}

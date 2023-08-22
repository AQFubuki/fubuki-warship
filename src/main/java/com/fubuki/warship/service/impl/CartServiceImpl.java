package com.fubuki.warship.service.impl;

import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.CartMapper;
import com.fubuki.warship.model.dao.ProductMapper;
import com.fubuki.warship.model.pojo.Cart;
import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.vo.CartVO;
import com.fubuki.warship.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
@Autowired
    public CartServiceImpl(CartMapper cartMapper, ProductMapper productMapper) {
        this.cartMapper = cartMapper;
    this.productMapper = productMapper;
}

    @Override
    public List<CartVO> list(Long userId) {
//        List<Cart> cartList = cartMapper.selectByUserId(userId);
//        ArrayList<CartVO>cartVOArrayList=new ArrayList<>();
//        for (Cart cart : cartList) {
//            CartVO cartVO=new CartVO();
//            BeanUtils.copyProperties(cart, cartVO);
//
//            //查询商品信息
//            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
//            cartVO.setTotalPrice(cart.getQuantity() * product.getPrice());
//            cartVO.setPrice(product.getPrice());
//            cartVO.setProductName(product.getName());
//            cartVO.setProductImage(product.getImage());
//            cartVOArrayList.add(cartVO);
//        }
        List<CartVO> cartVOList = cartMapper.selectCartVOList(userId);
        //计算总价=单价*数量
        for (CartVO cartVO : cartVOList) {
            cartVO.setTotalPrice(cartVO.getPrice()*cartVO.getQuantity());
        }
        return cartVOList;
    }

    @Override
    public List<CartVO> add(Long userId, Long productId, Integer count) {
        validProduct(productId, count);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.SELECTED);
            cartMapper.insertSelective(cart);
        } else {
            //这个商品已经在购物车里了，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
           // cartNew.setSelected(Constant.Cart.SELECTED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> update(Long userId, Long productId, Integer count) {
        validProduct(productId, count);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，更新失败
            throw new WarshipException(WarshipExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则更改数量
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());

            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Long userId, Long productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，删除失败
            throw new WarshipException(WarshipExceptionEnum.DELETE_FAILED);
        } else {
            //这个商品已经在购物车里了，则删除

            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> select(Long userId, Long productId, Integer selected) {
        validProduct(productId, 0);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，更新失败
            throw new WarshipException(WarshipExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则更改选中状态
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());

            cartNew.setSelected(selected);

            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectAll(Long userId, Integer selected) {
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        for (Cart cart : cartList) {
            this.select(userId,cart.getProductId(),selected);
//            Cart cartNew = new Cart();
//            cartNew.setId(cart.getId());
//            cartNew.setProductId(cart.getProductId());
//            cartNew.setUserId(cart.getUserId());
//
//            cartNew.setSelected(selected);
//
//            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }
    @Override
    public List<CartVO> selectOrNot(Long userId, Long productId, Integer selected) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //这个商品之前不在购物车里，无法选择/不选中
            throw new WarshipException(WarshipExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则可以选中/不选中
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return this.list(userId);
    }
    @Override
    public List<CartVO> selectAllOrNot(Long userId, Integer selected) {
        //改变选中状态
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }

    private void validProduct(Long productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new WarshipException(WarshipExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if (count > product.getStock()) {
            throw new WarshipException(WarshipExceptionEnum.NOT_ENOUGH);
        }
    }
}

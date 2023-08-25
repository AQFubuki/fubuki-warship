package com.fubuki.warship.service.impl;

import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.filter.UserFilter;
import com.fubuki.warship.model.dao.CartMapper;
import com.fubuki.warship.model.dao.OrderItemMapper;
import com.fubuki.warship.model.dao.OrderMapper;
import com.fubuki.warship.model.dao.ProductMapper;
import com.fubuki.warship.model.pojo.Order;
import com.fubuki.warship.model.pojo.OrderItem;
import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.request.CreateOrderReq;
import com.fubuki.warship.model.vo.CartVO;
import com.fubuki.warship.model.vo.OrderItemVO;
import com.fubuki.warship.model.vo.OrderVO;
import com.fubuki.warship.service.CartService;
import com.fubuki.warship.service.OrderService;
import com.fubuki.warship.util.OrderCodeFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final CartMapper cartMapper;
    private final CartService cartService;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, ProductMapper productMapper, CartMapper cartMapper, CartService cartService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.cartMapper = cartMapper;
        this.cartService = cartService;
    }

    //数据库事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq) {

        //拿到用户ID
        Long userId = UserFilter.currentUser.getId();

        //从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();

        for (CartVO cartVO : cartVOList) {
            if (cartVO.getSelected().equals(Constant.Cart.SELECTED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        //如果购物车已勾选的为空，报错
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new WarshipException(WarshipExceptionEnum.CART_EMPTY);
        }
        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        //把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        //扣库存
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new WarshipException(WarshipExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //把购物车中的已勾选商品删除
        cleanCart(cartVOList);
        //生成订单
        Order order = new Order();
        //生成订单号，有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);

        order.setTotalPrice(totalPrice(orderItemList));

        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        //插入到Order表
        orderMapper.insertSelective(order);

        //循环保存每个商品到order_item表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return orderNo;
    }

    @Override
    public OrderVO detail(String orderNo) {
        //Long userId = UserFilter.currentUser.getId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            //订单号不存在
            throw new WarshipException(WarshipExceptionEnum.NO_ORDER);
        }
        //订单存在，判断所属
        Long userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new WarshipException(WarshipExceptionEnum.NOT_YOUR_ORDER);
        }
        OrderVO orderVO=getOrderVO(order);
        return orderVO;
    }
    public OrderVO getOrderVO(Order order){
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        List<OrderItemVO> orderItemVOList =
                orderItemMapper.selectOrderItemVOList(order.getOrderNo());
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(
                Constant.OrderStatusEnum.codeOf(order.getOrderStatus()).getValue()
        );
        return orderVO;
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Long userId = UserFilter.currentUser.getId();
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVO>orderVOList=new LinkedList<>();
        for (Order order : orderList) {
            OrderVO detail = getOrderVO(order);
            orderVOList.add(detail);
        }
        PageInfo pageInfo = new PageInfo(orderVOList);
        return pageInfo;
    }

    private Long totalPrice(List<OrderItem> orderItemList) {
        Long totalPrice = 0L;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
//            cartService.delete(UserFilter.currentUser.getId()
//                    , cartVO.getProductId());
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new LinkedList<>();
        for (CartVO cartVO : cartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    //判断商品是否存在、上下架状态、库存
    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new WarshipException(WarshipExceptionEnum.NOT_SALE);
            }
            //判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new WarshipException(WarshipExceptionEnum.NOT_ENOUGH);
            }
        }
    }
}

package com.xiaoyan.service.impl;

import com.xiaoyan.dto.OrderDTO;
import com.xiaoyan.enums.OrderStatusEnum;
import com.xiaoyan.enums.PayStatusEnum;
import com.xiaoyan.enums.ResultEnum;
import com.xiaoyan.exception.OrderException;
import com.xiaoyan.model.OrderDetail;
import com.xiaoyan.model.OrderMaster;
import com.xiaoyan.product.client.ProductClient;
import com.xiaoyan.product.common.DecreaseStockInput;
import com.xiaoyan.product.common.ProductInfoOutput;
import com.xiaoyan.repository.OrderDetailRepository;
import com.xiaoyan.repository.OrderMasterRepository;
import com.xiaoyan.service.OrderService;
import com.xiaoyan.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductClient productClient;

    /**
     * 创建订单
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO dto) {
        //主订单号
        String orderId = KeyUtil.genUniqueKey();
        //1 查询商品信息（调用商品服务）
        List<String> productIdList = dto.getOrderDetailList().stream().map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);
        // 2 计算总价
        BigDecimal orderAmout = new BigDecimal(0);
        for (OrderDetail orderDetail : dto.getOrderDetailList()) {
            for (ProductInfoOutput productInfoOutput : productInfoList) {
                //单价*数量
                if (orderDetail.getProductId().equals(productInfoOutput.getProductId())) {
                    orderAmout = productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout);
                    BeanUtils.copyProperties(productInfoOutput, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    orderDetail.setCreateTime(new Date());
                    orderDetail.setUpdateTime(new Date());
                    orderDetailRepository.save(orderDetail);
                }
            }

        }
        //3 扣库存（调用商品服务）
        List<DecreaseStockInput> decreaseStockInputList = dto.getOrderDetailList().stream().map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        //4 订单入库
        OrderMaster orderMaster = new OrderMaster();
        dto.setOrderId(orderId);
        BeanUtils.copyProperties(dto, orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setCreateTime(new Date());
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        return dto;
    }

    @PostMapping("/finish")
    @Transactional
    public OrderDTO finish(String orderId) {
        //1. 先查询订单
        Optional<OrderMaster> masterOptional = orderMasterRepository.findById(orderId);
        if (!masterOptional.isPresent()) {
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2. 判断订单状态
        OrderMaster orderMaster = masterOptional.get();
        if (OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()) {
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //3. 修改订单装填为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHEN.getCode());
        orderMasterRepository.save(orderMaster);

        // 查询子订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}

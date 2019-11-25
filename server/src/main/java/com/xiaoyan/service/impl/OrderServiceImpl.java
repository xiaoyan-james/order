package com.xiaoyan.service.impl;

import com.xiaoyan.dto.OrderDTO;
import com.xiaoyan.enums.OrderStatusEnum;
import com.xiaoyan.enums.PayStatusEnum;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO dto) {
        //主订单号
        String orderId=KeyUtil.genUniqueKey();
        //TODO 查询商品信息（调用商品服务）
        List<String> productIdList=dto.getOrderDetailList().stream().map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList=productClient.listForOrder(productIdList);
        // TODO 计算总价
        BigDecimal orderAmout=new BigDecimal(0);
        for(OrderDetail orderDetail:dto.getOrderDetailList()){
            for (ProductInfoOutput productInfoOutput:productInfoList){
                //单价*数量
                if(orderDetail.getProductId().equals(productInfoOutput.getProductId())){
                    orderAmout=productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout);
                    BeanUtils.copyProperties(productInfoOutput,orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    orderDetailRepository.save(orderDetail);
                }
            }

        }
        //TODO 扣库存（调用商品服务）
        List<DecreaseStockInput> decreaseStockInputList=dto.getOrderDetailList().stream().map(e->new  DecreaseStockInput(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster=new OrderMaster();
        dto.setOrderId(orderId);
        BeanUtils.copyProperties(dto,orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return dto;
    }
}

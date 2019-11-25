package com.xiaoyan.repository;

import com.xiaoyan.OrderApplicationTests;
import com.xiaoyan.enums.OrderStatusEnum;
import com.xiaoyan.enums.PayStatusEnum;
import com.xiaoyan.model.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class OrderDetailRepositoryTest extends OrderApplicationTests {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public  void save(){
        OrderMaster master=new OrderMaster();
        master.setOrderId("order-2");
        master.setBuyerAddress("望京东");
        master.setBuyerName("小严");
        master.setBuyerOpenid("pppp");
        master.setBuyerPhone("13499998888");
        master.setOrderAmount(new BigDecimal(2.5));
        master.setOrderStatus(OrderStatusEnum.NEW.getCode());
        master.setPayStatus(PayStatusEnum.WAIT.getCode());
        master.setUpdateTime(new Date());

        OrderMaster obj=orderMasterRepository.save(master);
        Assert.assertTrue(obj!=null);

    }

}
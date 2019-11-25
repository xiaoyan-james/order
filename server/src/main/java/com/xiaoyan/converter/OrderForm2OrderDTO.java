package com.xiaoyan.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyan.dto.OrderDTO;
import com.xiaoyan.enums.ResultEnum;
import com.xiaoyan.exception.OrderException;
import com.xiaoyan.form.OrderForm;
import com.xiaoyan.model.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDTO {

    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();

        OrderDTO dto = new OrderDTO();
        dto.setBuyerName(orderForm.getName());
        dto.setBuyerPhone(orderForm.getPhone());
        dto.setBuyerAddress(orderForm.getAddress());
        dto.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();

        try {
            orderDetailList=gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        }catch (Exception e){
            log.error("【json转换】错误，String={}",orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        dto.setOrderDetailList(orderDetailList);
        return dto;
    }
}

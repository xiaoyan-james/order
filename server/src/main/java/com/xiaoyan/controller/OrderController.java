package com.xiaoyan.controller;

import com.xiaoyan.converter.OrderForm2OrderDTO;
import com.xiaoyan.dto.OrderDTO;
import com.xiaoyan.enums.ResultEnum;
import com.xiaoyan.exception.OrderException;
import com.xiaoyan.form.OrderForm;
import com.xiaoyan.service.OrderService;
import com.xiaoyan.utils.ResultVoUtil;
import com.xiaoyan.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 1.参数校验
     * 2.查询商品信息（调用商品服务）
     * 3.计算总价
     * 4.扣库存（调用商品服务）
     * 5.订单入库
     */
    @PostMapping("/create")
    @CrossOrigin()
    public ResultVo<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确，orderForm={}", orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        //orderForm -> orderDTO
        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】 购物车信息为空");
            throw new OrderException(ResultEnum.CART_EMPTY);
        }
        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());
        return ResultVoUtil.success(map);
    }

    @PostMapping("/finish")
    public ResultVo<OrderDTO> finish(@RequestParam("orderId") String orderId) {
        return ResultVoUtil.success(orderService.finish(orderId));
    }
}

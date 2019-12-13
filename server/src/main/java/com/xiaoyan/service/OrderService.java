package com.xiaoyan.service;

import com.xiaoyan.dto.OrderDTO;

/**
 *
 */
public interface OrderService {
    OrderDTO create(OrderDTO dto);

    OrderDTO finish(String orderId);
}

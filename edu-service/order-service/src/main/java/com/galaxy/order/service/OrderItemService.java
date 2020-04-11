package com.galaxy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.galaxy.order.entity.OrderItem;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
public interface OrderItemService extends IService<OrderItem> {

    String payOrder(String orderid);

}

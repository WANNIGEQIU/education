package com.galaxy.order.service.impl;


import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galaxy.order.config.AliPayTemplate;
import com.galaxy.order.entity.OrderItem;
import com.galaxy.order.entity.vo.PayVo;
import com.galaxy.order.mapper.OrderItemMapper;
import com.galaxy.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

        @Autowired
        private AliPayTemplate aliPayTemplate;

    @Override
    public String payOrder(String orderid) {


        OrderItem orderItem = this.baseMapper.selectOne(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderNo, orderid));

        PayVo payVo = new PayVo().setOut_trade_no(orderid)
                .setTotal_amount(orderItem.getCoursePrice().toString())
                .setSubject(orderItem.getCourseName());
        try {
            String pay = this.aliPayTemplate.pay(payVo);
            return pay;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

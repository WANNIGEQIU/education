package com.galaxy.order.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.galaxy.common.bean.PageVo;
import com.galaxy.order.entity.MtOrder;
import com.galaxy.order.entity.dto.OrderBean;
import com.galaxy.order.entity.vo.PayAsyncVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
public interface OrderService extends IService<MtOrder> {

    String newOrder(OrderBean orderBean, HttpServletRequest request) ;

    Integer paySuccess(PayAsyncVo payAsyncVo);

   PageVo queryOrder(Page<MtOrder> objectPage, OrderBean bean);

    Boolean removeOrder(String orderId);

    Map getMyOrder(Page<MtOrder> p,String id);

    void closeOrder(String orderNo);

    Integer queryOrderNum(String day);
}

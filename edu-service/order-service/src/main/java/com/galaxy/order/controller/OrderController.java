package com.galaxy.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.LocalDateTimeUtils;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.order.entity.MtOrder;
import com.galaxy.order.entity.OrderItem;
import com.galaxy.order.entity.dto.OrderBean;
import com.galaxy.order.entity.vo.PayAsyncVo;
import com.galaxy.order.service.OrderItemService;
import com.galaxy.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
@RestController
@RequestMapping("/order")
public class OrderController {




    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService itemService;


    @PostMapping("/create")
    public ResultCommon newOrder(@RequestBody OrderBean orderBean, HttpServletRequest request)  {


        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt==null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }

            orderBean.setUserId(jwt.getId());
            orderBean.setUserName(jwt.getUsername());

        String order = this.orderService.newOrder(orderBean,request);


        return ResultCommon.resultOk(order);
    }


    @PostMapping("/pay")
    public ResultCommon paySuccess(PayAsyncVo payAsyncVo){

        Integer integer = this.orderService.paySuccess(payAsyncVo);
        return ResultCommon.resultOk(integer);
    }



    @PostMapping("/query/{page}/{limit}")
    public ResultCommon queryOrder(@PathVariable Long page,@PathVariable Long limit,
    @RequestBody(required = false) OrderBean bean) {
        Page<MtOrder> objectPage = new Page<>(page, limit);
        PageVo pageVo = this.orderService.queryOrder(objectPage, bean);

        return ResultCommon.resultOk(pageVo);


    }


    @DeleteMapping("/remove/{orderId}")
    public ResultCommon  removeOrder(@PathVariable String orderId){

        Boolean b = this.orderService.removeOrder(orderId);

        return ResultCommon.resultOk(b);
    }


    /**
     * 我的订单
     * @param
     * @return
     */
    @GetMapping("/myorder/{page}/{limit}")
    public ResultCommon getMyOrder(@PathVariable Long page,
                                   @PathVariable Long limit,HttpServletRequest request) {

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt==null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }

        if (page ==null || limit ==null) {
            page = 1L;
            limit = 10L;
        }
        Page<MtOrder> p = new Page<>(page, limit);
        Map map =  this.orderService.getMyOrder(p,jwt.getId());
        return ResultCommon.resultOk(map);

    }


    @PutMapping("/close")
    public ResultCommon closeOrder(String orderNo){

        this.orderService.closeOrder(orderNo);
        return ResultCommon.resultOk();
    }

    @PutMapping("/admin/close")
    public ResultCommon closeOrderAdmin(String orderNo){

        this.orderService.closeOrder(orderNo);
        return ResultCommon.resultOk();
    }

    /**
     * 支付成功后提醒
     */
     @PostMapping("/success")
    public ResultCommon getPayStatus(@RequestParam("out_trade_no") String orderNo){

         OrderItem orderItem = this.itemService.getOne(new LambdaQueryWrapper<OrderItem>()
         .eq(OrderItem::getOrderNo,orderNo));

         return ResultCommon.resultOk(orderItem);



     }

    /**
     * 继续支付订单
     */
    @PostMapping("/pay2")
    public ResultCommon payOrder(@RequestParam("id") String orderid, HttpServletRequest request){

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null) {
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }

        String s = this.itemService.payOrder(orderid);
        return ResultCommon.resultOk(s);


    }



    @GetMapping("/queryOrderNum/{day}")
    public Integer queryOrderNum(@PathVariable String day) {
        if (StringUtils.isEmpty(day)) {
            day = LocalDateTimeUtils.formatOther("yyyy-MM-dd");
        }
        Integer integer = this.orderService.queryOrderNum(day);

        return integer;

    }

}


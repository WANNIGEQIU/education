package com.galaxy.order.service.impl;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.constants.OrderConstant;
import com.galaxy.common.enums.OrderEnum;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.LocalDateTimeUtils;
import com.galaxy.order.config.AliPayTemplate;
import com.galaxy.order.entity.MtOrder;
import com.galaxy.order.entity.OrderItem;
import com.galaxy.order.entity.dto.OrderBean;
import com.galaxy.order.entity.vo.OrderVo;
import com.galaxy.order.entity.vo.PayAsyncVo;
import com.galaxy.order.entity.vo.PayVo;
import com.galaxy.order.feign.CourseFeign;
import com.galaxy.order.feign.MemberFeign;
import com.galaxy.order.mapper.OrderItemMapper;
import com.galaxy.order.mapper.OrderMapper;
import com.galaxy.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, MtOrder> implements OrderService {

    @Autowired
    private CourseFeign courseFeign;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private OrderItemMapper itemMapper;

    @Autowired
    private AliPayTemplate aliPayTemplate;

    @Autowired
    private MemberFeign userFeignClient;

    /**
     * 提交订单
     * @param
     * @return
     */
    @Override
    public String newOrder(OrderBean bean, HttpServletRequest request)  {

        /**
         * 订单详细
         */
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuNum(1);                //课程数量 默认1
        orderItem.setLecturerId(bean.getLecturerId());      // 讲师id
        orderItem.setLecturerName(bean.getLecturerName());      //讲师姓名
        orderItem.setCourseId(bean.getCourseId());              //课程id
        orderItem.setCourseName(bean.getCourseName());        //课程名
        orderItem.setCourseImg(bean.getCover());                //课程图片路径
        orderItem.setCoursePrice(bean.getPrice());              //课程价格
        orderItem.setCategoryId(bean.getCategoryId());          // 二级类别
        orderItem.setCategoryPid(bean.getCategoryPid());        //一级类别
        Double d = Double.valueOf(orderItem.getCoursePrice().toString());       //积分
        orderItem.setGift(d/10);


        MtOrder order = new MtOrder();
        order.setId(IdWorker.getTimeId().substring(0,17));
        order.setUserId(bean.getUserId());                //下单用户id
        order.setUsername(bean.getUserName());            //下单用户名
        order.setSourceType(bean.getSourceType());        //订单来源 web
        order.setStatus(OrderConstant.ORDER_DRAFT);       //订单状态 待付款
        order.setPayType(bean.getPayType());                //支付类型
        order.setTotalAmount(orderItem.getCoursePrice());   //订单金额
        order.setPayAmount(orderItem.getCoursePrice());     // 付款金额
        order.setPoints(orderItem.getGift());               // 积分
        orderItem.setOrderNo(order.getId());                //订单号

        //生成订单与明细
        int insert = this.baseMapper.insert(order);
        if (insert ==0) {
            log.info("订单信息: {}", JSON.toJSONString(order));
            throw new MyException(ResultEnum.ERROR.getCode(), "创建订单失败请稍后再试");
        }

        this.itemMapper.insert(orderItem);
        PayVo payVo = new PayVo();
        payVo.setOut_trade_no(orderItem.getOrderNo());
        payVo.setTotal_amount(order.getPayAmount().toString());
        payVo.setSubject(orderItem.getCourseName());

        try {
            String form = this.aliPayTemplate.pay(payVo);
            return form;
//            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().write(form);//直接将完整的表单html输出到页面
//            response.getWriter().flush();
//            response.getWriter().close();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }







    @Override
    public Integer paySuccess(PayAsyncVo payAsyncVo) {
        Map<String, Object> hashMap = new HashMap<>();
        if ("TRADE_SUCCESS".equals(payAsyncVo.getTrade_status())) {
            String zfbNo = payAsyncVo.getTrade_no(); // 支付宝交易号
            String orderNo = payAsyncVo.getOut_trade_no(); // 订单号
            String patTime = payAsyncVo.getGmt_payment(); // 交易付款时间
            String receipt_amount = payAsyncVo.getReceipt_amount(); // 商家实收金额
            String pay_amount = payAsyncVo.getBuyer_pay_amount(); // 用户实际付款金额
            LocalDateTime time = LocalDateTimeUtils.string2parse(patTime, "yyyy-MM-dd HH:mm:ss");

            MtOrder order = this.baseMapper.selectById(orderNo);
            if (order == null)
                return null;
            double points = order.getPoints();
            /**
             * 更新订单
             */
            MtOrder mtOrder = new MtOrder();
            mtOrder.setZfbNo(zfbNo);
            mtOrder.setPayTime(time);
            mtOrder.setStatus(OrderConstant.ORDER_FINISH);
            this.baseMapper.update(mtOrder, new LambdaQueryWrapper<MtOrder>().eq(MtOrder::getId, orderNo));

            /**
             * 增加用户积分
             */
           this.userFeignClient.UpdateIntegral(order.getUserId(),String.valueOf(points));
            log.info("用户名:{},增加积分{}",order.getUsername(),points);

            /**
             * 添加用户学习课程
             */
            OrderItem orderItem = this.itemMapper.selectOne(new LambdaQueryWrapper<OrderItem>()
                    .eq(OrderItem::getOrderNo, order.getId()));
            String courseId = orderItem.getCourseId();
            String username = order.getUsername();
            this.courseFeign.addUn(courseId,username);


            /**
             * 更新课程购买数量
             */

            this.rabbitTemplate.convertAndSend("ORDER", "order", courseId);
            log.info("发送mq 课程id: {}",courseId);


        }

        return 1;
    }

    @Override
    public PageVo queryOrder(Page<MtOrder> objectPage, OrderBean bean) {
        IPage<OrderVo> voPage = this.orderMapper.queryOrder(objectPage, bean);
        List<OrderVo> orderVos = voPage.getRecords();


        orderVos.forEach(item->{
             switch (item.getOrderStatus()){
                 case 1:
                    item.setOrderStatusStr(OrderEnum.ORDER_FINISH.getMsg());
                     break;
                 case 2:
                     item.setOrderStatusStr(OrderEnum.ORDER_CLOSE.getMsg());
                     break;
                 case 0:
                     item.setOrderStatusStr(OrderEnum.ORDER_DRAFT.getMsg());
             }
             switch (item.getPayType()){
                 case 1:
                     item.setPayTypeStr(OrderEnum.PAY_TYPE_ZFB.getMsg());
                     break;
                 case 2:
                     item.setPayTypeStr(OrderEnum.PAY_TYPE_WX.getMsg());
             }

             switch (item.getSourceType()){
                 case 0:
                     item.setSourceStr(OrderEnum.ORDER_SOURCE_web.getMsg());
                     break;
                 case 1:
                     item.setSourceStr(OrderEnum.ORDER_source_app.getMsg());
                     break;
             }

        });





        return new PageVo(voPage);
    }

    @Override
    public Boolean removeOrder(String orderId) {

        int i = this.baseMapper.deleteById(orderId);
        return i>0;
    }

    @Override
    public Map getMyOrder(Page<MtOrder> p, String id) {
        Map<String, Object> hashMap = new HashMap<>();
        List<OrderVo> list = new ArrayList<>();
        Page<MtOrder> orderPage = this.baseMapper.selectPage(p,
                new LambdaQueryWrapper<MtOrder>().eq(MtOrder::getUserId, id));
        List<MtOrder> records = orderPage.getRecords();
        if (CollectionUtils.isEmpty(records))
            return hashMap;
        records.forEach(record->{
            OrderVo orderVo = new OrderVo();
            OrderItem orderItem = this.itemMapper.selectOne(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNo, record.getId()));
            BeanUtils.copyProperties(record,orderVo);
            String s = LocalDateTimeUtils.format2string(record.getEduCreate());
            orderVo.setEduCreate(s);
            orderVo.setOrderStatus(record.getStatus());
            orderVo.setOrderItem(orderItem);
            list.add(orderVo);

        });
        long total = orderPage.getTotal();

        hashMap.put("list",list);
        hashMap.put("total",total);
        return hashMap;

    }

    @Override
    public void closeOrder(String orderNo) {

        MtOrder order = this.baseMapper.selectById(orderNo);

        if (order.getStatus() == OrderConstant.ORDER_DRAFT){
            order.setStatus(OrderConstant.ORDER_CLOSE);
            this.baseMapper.update(order,
                    new LambdaQueryWrapper<MtOrder>().eq(MtOrder::getId,orderNo)
            );
        }



    }

    @Override
    public Integer queryOrderNum(String day) {

        Integer integer = this.orderMapper.queryOrderNum(day);
        return integer == null ? -1:integer;
    }


}

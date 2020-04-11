package com.galaxy.order.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class PayAsyncVo implements Serializable{

    /**
     * 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
     */
    private String gmt_create;

    private String charset;
    /**
     * 交易付款时间
     */
    private String gmt_payment;
    private String notify_time;
    private String subject;
    private String sign;
    private String buyer_id;//支付者的id
    private String body;//订单的信息
    private String invoice_amount;//支付金额
    private String version;
    private String notify_id;//通知id
    /**
     * 支付金额信息
     */
    private String fund_bill_list;
    private String notify_type;//通知类型； trade_status_sync
    private String out_trade_no;//订单号
    private String total_amount;//支付的总额
    private String trade_status;//交易状态  TRADE_SUCCESS 支付成功
    private String trade_no;//支付宝交易号
    private String auth_app_id;//
    private String receipt_amount;//商家收到的款
    private String point_amount;//
    private String app_id;//应用id
    /**
     *
     * 用户在交易中支付的金额，单位为元，精确到小数点后2位
     */
    private String buyer_pay_amount;//最终支付的金额
    private String sign_type;//签名类型
    private String seller_id;//商家的id

}

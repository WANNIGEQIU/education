package com.galaxy.order.mapper;
;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.order.entity.MtOrder;
import com.galaxy.order.entity.dto.OrderBean;
import com.galaxy.order.entity.vo.OrderVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
public interface OrderMapper extends BaseMapper<MtOrder> {

    IPage<OrderVo> queryOrder(Page<MtOrder> objectPage, OrderBean bean);

    Integer queryOrderNum(String day);
}

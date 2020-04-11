package com.galaxy.gateway.filter;

import com.galaxy.gateway.ResultObject;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthFilter extends ZuulFilter {

    private static String[] urls = null;

    static {
        //TODO 网关拦截路径 从数据库获取
        urls = new String[]{"/api/edu-order/order/new",
                "/api/ddu-course/course/web/getcourse",
                "/api/edu-course/course/web/details",
                "/api/edu-course/comment/save",
                "/api/edu-member/user/memberinfo",
                "/api/edu-order/order/myorder",
                 "/api/edu-course/course/web/mycourse",
                "/api/edu-order/order/close",
                "/api/edu-order/order/remove",
                "/api/edu-order/order/pay",
                "/api/edu-member/user/avatar"
        }; // 	需要过滤的请求
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("url:" + request.getRequestURI());

        ctx.getResponse().setContentType("text/html;charset=utf-8"); // 中文乱码
        for (String url : urls) {
            if (request.getRequestURI().contains(url)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object run() throws ZuulException {
        ResultObject resultObject = null;
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("token");
        ctx.getResponse().setContentType("text/html;charset=utf-8"); // 中文乱码

        if (request.getMethod().equals("OPTIONS")) {
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            return null;
        }

        // 未登录
        if (StringUtils.isBlank(token)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.OK.value());
            ctx.addZuulResponseHeader("Access-Control-Allow-Origin", "*");
            resultObject = new ResultObject(ResultEnum.MEMBER_NOT_LOGIN);
            ctx.setResponseBody(com.alibaba.fastjson.JSONObject.toJSONString(resultObject));
            return null;

        }

        // token过期
        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null) {
            ctx.setSendZuulResponse(false); // 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(HttpStatus.OK.value());
            ctx.addZuulResponseHeader("Access-Control-Allow-Origin", "*");
            resultObject = new ResultObject(ResultEnum.AUTH_ERROR_USER);
            ctx.setResponseBody(com.alibaba.fastjson.JSONObject.toJSONString(resultObject));
            return null;
        }


        return null;
    }
}

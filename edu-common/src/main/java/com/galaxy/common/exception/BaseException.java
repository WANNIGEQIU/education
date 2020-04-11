package com.galaxy.common.exception;



import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.ResultCommon;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BaseException {

    //全局异常
    @ExceptionHandler(Exception.class)
    public ResultCommon baseExceptionHandler(Exception e){
        e.printStackTrace();
        log.error("系统异常: {}",e.getMessage());
        return ResultCommon.exception().codeAndMsg(ResultEnum.EXCEPTION_SERVER);
    }
     // by zero
    @ExceptionHandler(ArithmeticException.class)
    public ResultCommon ArithmeticException(ArithmeticException e){
        e.printStackTrace();
        log.error("除零异常: {}",e.getMessage());
        return ResultCommon.exception().codeAndMsg(ResultEnum.ZERO_EXCEPTION);

    }

    // redis 连接异常
    @ExceptionHandler({RedisConnectionException.class, PoolException.class})
    public ResultCommon redisConnect(RedisConnectionException e){
        e.printStackTrace();
        log.error("Redis连接异常");
        return ResultCommon.resultFail().codeAndMsg(ResultEnum.REDIS_CONNECTION_EXCEPTION);
    }

    //自定义异常
    @ExceptionHandler(MyException.class)
    public ResultCommon myException(MyException e){
        e.printStackTrace();
        log.error("自定义异常: {}",e.getMessage());
        return ResultCommon.exception().codeAndMsg(e.getCode(),e.getMessage());

    }
}

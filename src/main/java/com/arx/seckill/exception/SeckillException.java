package com.arx.seckill.exception;

/**
 *  秒杀相关业务异常（运行期异常）
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

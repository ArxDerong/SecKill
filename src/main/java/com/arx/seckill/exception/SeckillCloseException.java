package com.arx.seckill.exception;

/**
 *  秒杀关闭异常（运行期异常）
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}

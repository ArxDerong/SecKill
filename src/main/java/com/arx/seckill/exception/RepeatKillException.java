package com.arx.seckill.exception;

/**
 *  重复秒杀异常（运行期异常）
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}

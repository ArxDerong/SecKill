package com.arx.seckill.service;

import com.arx.seckill.dto.Exposer;
import com.arx.seckill.dto.SeckillExecution;
import com.arx.seckill.entity.Seckill;
import com.arx.seckill.exception.RepeatKillException;
import com.arx.seckill.exception.SeckillCloseException;
import com.arx.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”角度设计接口
 * 三个方面：方法定义粒度,参数，返回类型(return 类型/异常)
 * <p>
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     *  @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}

package com.arx.seckill.dao;

import com.arx.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Arx888 on 2016/7/5.
 * derong218@qq.com
 */
public interface SuccessKilledDao {
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
    SuccessKilled queryById(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}

package com.arx.seckill.service.impl;

import com.arx.seckill.dao.SeckillDao;
import com.arx.seckill.dao.SuccessKilledDao;
import com.arx.seckill.dto.Exposer;
import com.arx.seckill.dto.SeckillExecution;
import com.arx.seckill.entity.Seckill;
import com.arx.seckill.entity.SuccessKilled;
import com.arx.seckill.enums.SeckillStateEnum;
import com.arx.seckill.exception.RepeatKillException;
import com.arx.seckill.exception.SeckillCloseException;
import com.arx.seckill.exception.SeckillException;
import com.arx.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
//@Component @Sevice @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值
    private final String salt = "sdfldjfhldjf*&%#!!@#%&HGJHGSGFhkkkfb((l,-=-8KJGKJG";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 100);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.queryById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        long now = new Date().getTime();

        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        if (now < startTime) {
            return new Exposer(false, seckillId, now, startTime, endTime);
        } else if (now > endTime) {
            return new Exposer(false, seckillId);
        }

        String md5 = getMd5(seckill.getSeckillId());
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {
        String base = salt + seckillId;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
    @Transactional
    /**
     * 使用注解控制事务的优点
     * 1：开发团队达成一致约定，明确标注实物方法的编程风格
     * 2：保证食物方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到实物方法外部。
     * 3：不是所有的方法都需要事务，如只有一条修改操作、只读操作不需要事务控制。
     *
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("数据被篡改");
        }
        try {
            //购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount < 1) {//没有插入数据，重复秒杀
                throw new RepeatKillException("重复秒杀");
            } else {
                Date killTime = new Date();
                //减库存
                int success = seckillDao.reduceNumber(seckillId, killTime);
                if (success < 1) {//没有减库存，秒杀关闭
                    throw new SeckillCloseException("秒杀关闭");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryById(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (RepeatKillException e) {
            throw e;
        } catch (SeckillCloseException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("秒杀内部异常:" + e.getMessage());
        }
    }
}

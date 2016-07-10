package com.arx.seckill.service.impl;

import com.arx.seckill.dao.SeckillDao;
import com.arx.seckill.dto.Exposer;
import com.arx.seckill.dto.SeckillExecution;
import com.arx.seckill.entity.Seckill;
import com.arx.seckill.exception.RepeatKillException;
import com.arx.seckill.exception.SeckillCloseException;
import com.arx.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Arx on 2016/7/10.
 * Email:derong218@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(SeckillServiceTest.class);
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);
    }

    @Test
    public void getById() throws Exception {
        Seckill byId = seckillService.getById(1000);
        logger.info("Seckill={}",byId);
    }

    @Test
    public void exportSeckillUrl() throws Exception {

        Exposer exposer = seckillService.exportSeckillUrl(1000);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void executeSeckill() throws Exception {


        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(1000, 13556278888L, "43f57d615c3a7d9d8cd9fb82daffc206");
            logger.info("seckillExecution={}",seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    }

}
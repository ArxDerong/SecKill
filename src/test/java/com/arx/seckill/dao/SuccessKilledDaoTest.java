package com.arx.seckill.dao;

import com.arx.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Arx888 on 2016/7/6.
 * derong218@qq.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        int i = successKilledDao.insertSuccessKilled(1000L, 13556888888L);
        System.out.println("insert:"+i);
    }

    @Test
    public void queryById() throws Exception {

        SuccessKilled successKilled = successKilledDao.queryById(1000, 13556888888L);
        System.out.println(successKilled);
    }

}
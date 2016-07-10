--数据库初始化脚本
--创建数据库
CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
--使用数据库
USE seckill;
--创建秒杀库存表
CREATE TABLE seckill (
  `seckill_id`  BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT '商品库存id',
  `name`        VARCHAR(120) NOT NULL
  COMMENT '商品名称',
  `number`      INT          NOT NULL
  COMMENT '库存数量',
  `start_time`  TIMESTAMP    NOT NULL
  COMMENT '秒杀开启时间',
  `end_time`    TIMESTAMP    NOT NULL
  COMMENT '秒杀结束时间',
  `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
) ENGINE =InnoDB AUTO_INCREMENT =1000 DEFAULT CHARSET =utf8 COMMENT ='秒杀库存表';

  --初始化数据
insert into seckill(name,number,start_time,end_time)
values
('1000秒杀iPhone6s',100,'2016-7-3 00:00:00','2016-7-4 00:00:00'),
('500秒杀iPhone6s',200,'2016-7-3 00:00:00','2016-7-4 00:00:00'),
('300秒杀iPhone6s',300,'2016-7-3 00:00:00','2016-7-4 00:00:00'),
('200秒杀iPhone6s',400,'2016-7-3 00:00:00','2016-7-4 00:00:00');

--秒杀成功明细
--用户登录认证相关信息
create table success_killed(
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品ID',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态表示:-1:无效 0 成功 1 已付款 2 已发货',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone),/*联合主键:*/
  key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
--mysql -uroot -p2822384
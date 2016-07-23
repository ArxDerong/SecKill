//存放主要交互逻辑js代码
//javascript 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        excution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    handleSeckill: function (seckillId, node) {
        //处理秒杀逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button> ')

        //获取秒杀地址
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //返回数据成功
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer) {
                    if (exposer['exposed']) {//暴露秒杀地址，显示秒杀按钮
                        var md5 = exposer['md5'];
                        var killUrl = seckill.URL.excution(seckillId, md5);
                        
                        $('#killBtn').one('click', function () {
                            //执行秒杀请求
                            //1:先禁用按钮
                            $(this).addClass('disabled');
                            //2:发送秒杀请求，执行秒杀
                            $.post(killUrl, {}, function (result) {
                                if (result && result['success']) {
                                    var killReuslt = result['data'];
                                    var state = killReuslt['state'];
                                    var stateInfo = killReuslt['stateInfo'];
                                    //3:显示秒杀结果
                                    node.html('<span class="label label-success">'+stateInfo+'</span>');
                                }
                            });
                        });
                        node.show();

                    } else {//秒杀未开始,重新执行倒计时
                        var now = exposer['now'];
                        var start = exposer['start'];
                        var end = exposer['end'];
                        seckill.countDown(seckillId, now, start, end);
                    }
                } else {
                    console.error('result:' + result);
                }
            } else {
                console.error('result:' + result);
            }

        });
    },
    validatePhone: function (phone) {
        return phone && phone.length == 11 && !isNaN(phone);
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime) {//秒杀结束
            seckillBox.html('秒杀结束!');
        } else if (startTime > nowTime) {//未开始
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时:%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {//秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            if (!seckill.validatePhone(killPhone)) {
                //绑定phone
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        // 电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger" >手机号错误</label>').show(300);
                    }
                });
            }
            //已经登录
            //倒计时交互

            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            })
        }

    }
}
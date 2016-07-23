package com.arx.seckill.web;

import com.arx.seckill.dto.Exposer;
import com.arx.seckill.dto.SeckillExecution;
import com.arx.seckill.dto.SeckillResult;
import com.arx.seckill.entity.Seckill;
import com.arx.seckill.enums.SeckillStateEnum;
import com.arx.seckill.exception.RepeatKillException;
import com.arx.seckill.exception.SeckillCloseException;
import com.arx.seckill.exception.SeckillException;
import com.arx.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Arx888 on 2016/7/18.
 * derong218@qq.com
 */
@Controller//@Service @Component
@RequestMapping("/seckill")
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表
        List<Seckill> list = seckillService.getSeckillList();

        model.addAttribute("list", list);
        //list.jsp +model=ModelAndView
        return "list";//WEB-INF/jsp/list.jsp;

    }

    @RequestMapping(value = "/{seckillId}/detail")
    public String detail(
            @PathVariable(value = "seckillId") Long seckillId,
            Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax  json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(
            @PathVariable(value = "seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);

            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execution(
            @PathVariable(value = "seckillId") Long seckillId,
            @PathVariable(value = "md5") String md5,
            @CookieValue(value = "killPhone", required = false) Long phone
    ) {
        if(phone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;

        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        }  catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
        catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
        catch (SeckillException e) {
            logger.error(e.getMessage(), e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }

        return result;
    }

    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date = new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }

}

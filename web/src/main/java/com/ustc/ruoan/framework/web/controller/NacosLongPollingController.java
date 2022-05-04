package com.ustc.ruoan.framework.web.controller;

import com.ustc.ruoan.framework.web.entity.Result;
import com.ustc.ruoan.framework.web.service.inter.NacosLongPollingService;
import com.ustc.ruoan.framework.web.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本思路是通过Servlet3.0后提供的异步处理能力，把请求的任务添加至队列中。
 * <p>
 * 在有数据发生变更时,从队列中取出相应请求,然后响应请求,负责拉取数据的接口通过延时任务完成超时处理
 * <p>
 * 如果等到设定的超时时间还没有数据变更时,就主动推送超时信息完成响应
 *
 * @author ruoan
 * @date 2022/5/4 4:39 下午
 */
@RestController
@RequestMapping("/nacos")
public class NacosLongPollingController extends HttpServlet {

    @Autowired
    private NacosLongPollingService nacosLongPollingService;

    @GetMapping("/pull")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String dataId = req.getParameter("dataId");
        if (StringUtils.isEmpty(dataId)) {
            throw new IllegalArgumentException("请求参数异常,dataId能为空");
        }
        nacosLongPollingService.doGet(dataId, req, resp);
    }

    /**
     * 为了在浏览器中演示,我这里先用Get请求,dataId可以区分不同应用的请求
     */
    @GetMapping("/push")
    public Result push(@RequestParam("dataId") String dataId, @RequestParam("data") String data) {
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(data)) {
            throw new IllegalArgumentException("请求参数异常, dataId和data均不能为空");
        }
        nacosLongPollingService.push(dataId, data);
        return ResultUtil.success();
    }
}
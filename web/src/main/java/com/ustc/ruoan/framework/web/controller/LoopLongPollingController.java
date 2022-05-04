package com.ustc.ruoan.framework.web.controller;

import com.ustc.ruoan.framework.web.entity.Result;
import com.ustc.ruoan.framework.web.service.LoopLongPollingServiceImpl;
import com.ustc.ruoan.framework.web.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruoan
 * @date 2022/5/4 3:53 下午
 */
@RestController
@RequestMapping("/loop")
public class LoopLongPollingController {

    @Autowired
    private LoopLongPollingServiceImpl loopLongPollingService;

    /**
     * 从服务端拉取被变更的数据
     */
    @GetMapping("/pull")
    public Result<String> pull() {
        String result = loopLongPollingService.pull();
        return ResultUtil.success(result);
    }

    /**
     * 向服务端推送变更的数据
     */
    @GetMapping("/push")
    public Result<String> push(@RequestParam("data") String data) {
        String result = loopLongPollingService.push(data);
        return ResultUtil.success(result);
    }
}
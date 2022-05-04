package com.ustc.ruoan.framework.web.controller;

import com.ustc.ruoan.framework.web.entity.Result;
import com.ustc.ruoan.framework.web.service.inter.LockLongPollingService;
import com.ustc.ruoan.framework.web.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruoan
 * @date 2022/5/4 4:33 下午
 */
@RestController
@RequestMapping("/lock")
public class LockLongPollingController {

    @Autowired
    private LockLongPollingService lockLongPollingService;

    @RequestMapping("/pull")
    public Result pull() {
        String result = lockLongPollingService.pull();
        return ResultUtil.success(result);
    }

    @RequestMapping("/push")
    public Result push(@RequestParam("data") String data) {
        String result = lockLongPollingService.push(data);
        return ResultUtil.success(result);
    }
}
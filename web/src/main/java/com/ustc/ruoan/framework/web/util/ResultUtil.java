package com.ustc.ruoan.framework.web.util;

import com.ustc.ruoan.framework.web.entity.Result;

/**
 * @author ruoan
 * @date 2022/5/4 3:54 下午
 */
@SuppressWarnings({"rawuse", "rawtypes"})
public class ResultUtil {
    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setSuccess(true);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setData(data);
        return result;
    }
}
package com.ustc.ruoan.framework.web.entity;

import lombok.Data;

/**
 * @author ruoan
 * @date 2022/5/4 3:53 下午
 */
@Data
public class Result<T> {
    private T data;
    private Integer code;
    private Boolean success;
}

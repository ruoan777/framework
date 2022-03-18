package com.ustc.ruoan.framework.web.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author ruoan
 */
@Getter
@Setter
public class Person {

    @Value("${ustc.ruoan.context.work}")
    private String name;

    private int age;

    private String nickName;

    public Person() {
        System.out.println("==========================");
        System.out.println(this.name);
    }
}

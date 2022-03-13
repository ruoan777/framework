package com.ustc.ruoan.framework.web.spring;

import lombok.NonNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ruoan
 */
public class ContextCondition implements Condition {

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        return false;
    }
}

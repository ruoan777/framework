package com.ustc.ruoan.framework.web.spring;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author ruoan
 */
@Component
public class ContextCondition implements Condition {

    @Value("${ustc.ruoan.context.work}")
    private String contextWork;

    @Override
    public boolean matches(@NonNull ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        return Objects.equals(contextWork, "1");
    }
}

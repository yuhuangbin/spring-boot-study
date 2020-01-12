package com.yuhb.springbootstudy.annotation;

import org.springframework.context.ApplicationEvent;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConsumerListener {

    Class<? extends ApplicationEvent> value();
}

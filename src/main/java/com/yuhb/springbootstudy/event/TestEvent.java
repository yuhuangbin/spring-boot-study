package com.yuhb.springbootstudy.event;

import org.springframework.context.ApplicationEvent;

/**
 * Description:
 * author: yu.hb
 * Date: 2020-01-12
 */
public class TestEvent extends ApplicationEvent {
    private String message;

    public TestEvent(String name) {
        super(new Object());
        this.message = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.yuhb.springbootstudy.config;

import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Description:
 * author: yu.hb
 * Date: 2020-01-12
 */
public class EventContext {
    private static List<Consumer<ApplicationEvent>> listener = new ArrayList<>();

    public static void register(Consumer consumer) {
        listener.add(consumer);
    }

    public static void publish(ApplicationEvent event) {
        listener.forEach(consumer -> consumer.accept(event));
    }
}

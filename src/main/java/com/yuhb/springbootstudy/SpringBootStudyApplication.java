package com.yuhb.springbootstudy;

import com.yuhb.springbootstudy.annotation.ConsumerListener;
import com.yuhb.springbootstudy.config.EventContext;
import com.yuhb.springbootstudy.event.TestEvent;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;

@SpringBootApplication
public class SpringBootStudyApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudyApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 发布事件
		EventContext.publish(new TestEvent("hello World, 余黄彬"));
	}

	/**
	 * 监听事件
	 */
	@ConsumerListener(value = ApplicationEvent.class)
	public void testEventListener(TestEvent event) {
		System.out.println("事件到达，内容： " + event.getMessage());
	}
}

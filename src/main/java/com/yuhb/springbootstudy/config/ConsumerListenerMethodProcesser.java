package com.yuhb.springbootstudy.config;

import com.yuhb.springbootstudy.annotation.ConsumerListener;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description:
 * author: yu.hb
 * Date: 2020-01-12
 */
@Configuration
public class ConsumerListenerMethodProcesser implements SmartInitializingSingleton, ApplicationContextAware, BeanFactoryPostProcessor{

    private ConfigurableApplicationContext applicationContext;

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void afterSingletonsInstantiated() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            if (!ScopedProxyUtils.isScopedTarget(beanName)) {
                Class<?> type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
                if (type != null) {
                    process(beanName,type);
                }
            }
        }
    }

    private void process(String beanName, Class<?> beanType) {
        if (!isSpringContainerClass(beanType)) {
            Map<Method, ConsumerListener> methodConsumerListenerMap = MethodIntrospector.selectMethods(beanType,
                    (MethodIntrospector.MetadataLookup<ConsumerListener>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, ConsumerListener.class));
            methodConsumerListenerMap.forEach((method, consumerListener) -> this.registerListener(method, consumerListener, beanName));
        }
    }

    private void registerListener(Method method, ConsumerListener consumerListener, String beanName) {
        Method methodToUse = AopUtils.selectInvocableMethod(method, applicationContext.getType(beanName));
        EventContext.register(event -> {
            if (consumerListener.value().isAssignableFrom(event.getClass())) {
                ReflectionUtils.invokeMethod(methodToUse, applicationContext.getBean(beanName), event);
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        this.beanFactory = configurableListableBeanFactory;
    }

    private static boolean isSpringContainerClass(Class<?> clazz) {
        return (clazz.getName().startsWith("org.springframework.") &&
                !AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
    }
}

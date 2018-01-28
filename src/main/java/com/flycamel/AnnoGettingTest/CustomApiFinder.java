package com.flycamel.AnnoGettingTest;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class CustomApiFinder implements ApplicationContextAware {
    private Map<String, Function<String, String>> functionMap = new HashMap<>();
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @PostConstruct
    public void getAnnotatedClass() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(CustomApi.class));

        for (BeanDefinition beanDefinition : provider.findCandidateComponents("com.flycamel.AnnoGettingTest")) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                CustomApi customApi = clazz.getAnnotation(CustomApi.class);
                System.out.printf("Found class: %s, with value: %s\n", clazz.getSimpleName(), customApi.apiValue());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> annotatedBeans = context.getBeansWithAnnotation(CustomApi.class);

        for (Map.Entry<String, Object> entry : annotatedBeans.entrySet()) {
            System.out.printf("Found bean, key: %s, object: %s\n", entry.getKey(), entry.getValue());

            Method[] methods = AopUtils.getTargetClass(entry.getValue()).getDeclaredMethods();

            for (Method method : methods) {
                System.out.println("Method : " + method);
                if (method.isAnnotationPresent(CustomApi.class)) {
                    CustomApi customApi = method.getAnnotation(CustomApi.class);
                    System.out.println("CustomApi annotated... apiValue : " + customApi.apiValue());
                    //functionMap.put(customApi.apiValue(),)
                }
            }
        }
    }
}

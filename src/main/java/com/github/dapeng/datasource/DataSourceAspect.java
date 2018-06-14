package com.github.dapeng.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author with struy.
 * Create by 2018/6/14 18:06
 * email :yq1724555319@gmail.com
 * 要在事务切面之前切
 */
@Aspect
@Component
@Order(-1)
public class DataSourceAspect {
    private static Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);


    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     *
     * @param point
     * @throws Exception
     */
    @Before(value = "@annotation(dataSource)")
    public void intercept(JoinPoint point, DataSource dataSource) throws Exception {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 默认使用目标类型的注解，如果没有则使用其实现接口的注解
        for (Class<?> clazz : target.getInterfaces()) {
            resolveDataSource(clazz, signature.getMethod());
        }
        resolveDataSource(target, signature.getMethod());
    }

    @After(value = "@annotation(dataSource)")
    public void reset(DataSource dataSource) {
        DynamicDataSourceHolder.clearDataSources();
        LOGGER.info(">>>>>>>>>reSet to default dataSource");
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     *
     * @param clazz
     * @param method
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
                LOGGER.info(">>>>>>>>>Switch dataSource to [{}],class::[{}]", source.value(), clazz.getName());
            }
            // 方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
                LOGGER.info(">>>>>>>>>Switch dataSource to [{}],method::[ {} ]", source.value(), clazz.getName()+"."+method.getName());
            }
        } catch (Exception e) {
            LOGGER.info("Switch dataSource error::{}", e);
        }
    }
}

package com.vijayrc.simple.controller;

import com.vijayrc.simple.meta.WebClass;
import com.vijayrc.simple.meta.WebMethod;
import org.apache.log4j.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.lang.StringUtils.remove;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

@Repository
public class AllControllers implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {
    private static Logger log = Logger.getLogger(AllControllers.class);
    private Map<String, MyMethod> methods = new TreeMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (!clazz.isAnnotationPresent(WebClass.class)) return bean;
        for (Method method : clazz.getMethods())
            if (method.isAnnotationPresent(WebMethod.class))
                methods.put(remove(getKey(clazz, method), "/"), new MyMethod(bean, method));
        return bean;
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        methods.keySet().forEach(log::info);
    }
    public void act(Request request, Response response) throws Exception {
        try {
            String url = request.getPath().toString();
            String path = remove(url, "/");
            log.debug("serving " + url);
            for (String key : methods.keySet()) {
                if (!path.contains(key)) continue;
                addHeaders(response);
                MyMethod myMethod = methods.get(key);
                myMethod.method.invoke(myMethod.bean, request, response);
                return;
            }
            showError(response, new Exception("Page not found, please check the url"));
        } catch (Exception e) {
            log.error(getFullStackTrace(e));
            showError(response, e);
        }
    }
    private String getKey(Class<?> clazz, Method method) {
        return clazz.getAnnotation(WebClass.class).value() + method.getAnnotation(WebMethod.class).value();
    }
    private void showError(Response response, Exception exception) throws Exception {
        new ErrorController().showError(response, exception);
    }
    private void addHeaders(Response response) {
        long time = System.currentTimeMillis();
        response.setValue("Server", "SupportGuy");
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
        response.setValue("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setValue("Cache-Control", "post-check=0, pre-check=0");
        response.setValue("Pragma", "no-cache");
    }
    private class MyMethod {
        private Object bean;
        private Method method;

        private MyMethod(Object bean, Method method) {
            this.bean = bean;
            this.method = method;
        }
    }
}

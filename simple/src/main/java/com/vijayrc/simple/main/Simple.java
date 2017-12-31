package com.vijayrc.simple.main;

import com.vijayrc.simple.web.MyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Simple {
    public static void main(String[] args) throws Exception {
        run();
    }
    public static void run() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        final MyServer myServer = (MyServer) context.getBean("myServer");
        myServer.start();
    }
}

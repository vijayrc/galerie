package com.vijayrc.simple.web;

import org.apache.log4j.Logger;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
@Scope("singleton")
public class MyServer {
    private static Logger log = Logger.getLogger(MyServer.class);
    private Integer port;
    private Server server;
    private MyContainer myContainer;

    @Autowired
    public MyServer(MyContainer myContainer, @Value("#{config['server.port']}") Integer port) {
        this.myContainer = myContainer;
        this.port = port;
    }
    public void start() throws Exception {
        server = new ContainerServer(myContainer,100);
        Connection connection = new SocketConnection(server);
        connection.connect(new InetSocketAddress(port));
        log.info("simple server started, ready to process request...");
    }
    public void stop() throws Exception {
        server.stop();
        log.info("simple server stopped");
    }
}

package com.pvsoul.demo;

import com.pvsoul.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author
 * @description 服务端socket
 * @date 2019/7/30 14:57
 */
@Service
@Slf4j
public class SocketServer {

    //    @Value("${port}")
    private Integer port;
    private boolean started;
    private ServerSocket serverSocket;
    //使用多线程，需要线程池，防止并发过高时创建过多线程耗尽资源
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void start() {
        start(8090);
    }

    @Autowired
    private TestService testService;

    private void start(Integer port) {
        try {
            serverSocket = new ServerSocket(port == null ? this.port : port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("端口异常信息", e);
            System.exit(0);
        }
        while (started) {
            try {
                Socket socket = serverSocket.accept();
                DeviceConnector deviceConnector = new DeviceConnector(socket, testService);
                //接收线程返回结果
                Future future = threadPool.submit(deviceConnector);
                log.info(future.isDone() + "--------");
            } catch (IOException e) {
                log.error("接收线程异常：",e);
            }
        }
    }

}
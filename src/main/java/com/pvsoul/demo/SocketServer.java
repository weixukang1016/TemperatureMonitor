package com.pvsoul.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SocketServer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //    @Value("${port}")
    private Integer port;
    private boolean started;
    private ServerSocket serverSocket;
    //使用多线程，需要线程池，防止并发过高时创建过多线程耗尽资源
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void start() {
        start(8090);
    }

    private void start(Integer port) {
        try {
            serverSocket = new ServerSocket(port == null ? this.port : port);
            started = true;
            logger.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            logger.error("端口异常信息", e);
            System.exit(0);
        }
        while (started) {
            try {
                Socket socket = serverSocket.accept();
                Runnable runnable = () -> {
                    try {
                        //接收客户端数据
                        StringBuilder xmlString = onMessage(socket);
                        //处理逻辑：xmlStringToEsb为处理结果
                        //返回给客户端
                        sendMessage(socket, "Hello Client");
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                //接收线程返回结果
                Future future = threadPool.submit(runnable);
                logger.info(future.isDone() + "--------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private StringBuilder onMessage(Socket socket) {
        byte[] bytes = new byte[1024];
        int len;
        try {
            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, "UTF-8"));
                outputStream.write(bytes);
            }
            //此处，需要关闭服务器的输出流，但不能使用inputStream.close().
            socket.shutdownInput();
            return sb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void sendMessage(Socket socket, String message) {
        try {
            //向客户端返回数据
            OutputStream outputStream = socket.getOutputStream();
            //首先需要计算得知消息的长度
            byte[] sendBytes = message.getBytes("UTF-8");
            //然后将消息的长度优先发送出去
            outputStream.write(sendBytes.length >> 8);
            outputStream.write(sendBytes.length);
            //然后将消息再次发送出去
            outputStream.write(sendBytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.lingdong.netty.sokect.nettyclient;

import com.lingdong.netty.sokect.nettyserver.MsgModel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientServer {

    private String address = "192.168.1.55";
    private Integer port = 1514;

    private NioEventLoopGroup worker = new NioEventLoopGroup();

    public static Channel channel;

    private Bootstrap bootstrap;

    public static void main(String[] args) {
        ClientServer clientServer = new ClientServer();
        clientServer.start();
        System.out.println("客户端启动成功");
        clientServer.sendData();
    }

    private void sendData() {
        Scanner sc= new Scanner(System.in);
        for (int i = 0; i < 20; i++) {
            if(channel != null && channel.isActive()){
                System.err.println("请输入要发送的消息");
                String nextLine = sc.nextLine();
                MsgModel model = new MsgModel();
                model.setT(1);
                model.setB(nextLine);
                channel.writeAndFlush(model);
            }
        }
    }

    private void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 5)); // 设置客户端的机制为 5 秒触发一次ping
//                        pipeline.addLast(new NettyClientHandler());   // 当前客户端的控制器
                    }
                });
        doConnect();
    }

    /**
     * 连接服务端 and 重连
     */
    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture connect = bootstrap.connect(address, port);
        //实现监听通道连接的方法
        connect.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    channel = channelFuture.channel();
                    System.out.println("连接成功");
                } else {
                    System.out.println("每隔2s重连....");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            doConnect();
                        }
                    }, 2, TimeUnit.SECONDS);
                }
            }
        });
    }

}
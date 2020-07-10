package com.lingdong.netty.sokect.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {

    @Autowired
    private NettyServiceHandler nettyServiceHandler;

    @Value("${netty.server.user.port}")
    private Integer serverPort;

    public void start() {
        // 处理连接的线程池
        EventLoopGroup acceptGroup = new NioEventLoopGroup(1);
        // 处理数据的线程池
        EventLoopGroup readGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(acceptGroup, readGroup);
            sb.channel(NioServerSocketChannel.class);
            sb.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 职责链定义（请求交给谁去处理）
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 职责链
                    ChannelPipeline pipeline = ch.pipeline();
                    // 心跳参数设置
                    pipeline.addLast(new IdleStateHandler(5, 0, 0));
                    // 打印数据
                    pipeline.addLast(nettyServiceHandler);
                }
            });
            // 绑定端口
            ChannelFuture cf = sb.bind(serverPort).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acceptGroup.shutdownGracefully();
            readGroup.shutdownGracefully();
        }
    }
}

package com.lingdong.netty;

import com.lingdong.netty.sokect.nettyserver.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NettyDsApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(NettyDsApplication.class, args);

        new Thread(() -> ctx.getBean(NettyServer.class).start()).start();

    }

}

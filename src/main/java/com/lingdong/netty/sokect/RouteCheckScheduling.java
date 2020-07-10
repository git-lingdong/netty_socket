package com.lingdong.netty.netty;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 路由巡检
 */
@Component
@EnableScheduling
public class RouteCheckScheduling {

    @Scheduled(cron = "0/5 * * * * ?")
//    @Scheduled(cron = "0 0/15 * * * ?")
    public void getDynamicRouteInfo() {


    }

}

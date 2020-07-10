package com.lingdong.netty.sokect.nettyserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * user Socket Handler
 *
 * @author: gouwei
 * @date: 2020-07-10 11:03
 */
@Component
@ChannelHandler.Sharable
public class NettyServiceHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServiceHandler.class);

    private AttributeKey<String> signs = ChannelGroupsManager.signs;

    /**
     * 传输信息：f&4875
     * 心跳信息：heartbeat*t:989854211241
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        Channel client = ctx.channel();
        byte[] receiveMsgBytes = new byte[msg.readableBytes()];
        msg.readBytes(receiveMsgBytes);
        String receive = new String(receiveMsgBytes);
        MsgModel msgModel = JSONObject.toJavaObject(JSON.parseObject(receive), MsgModel.class);
        if (msgModel.getT() == 1) {
            String[] dataArr = msgModel.getB().toString().split("&");
            if (client.attr(signs).get() == null) {
                if (dataArr.length > 0 && "f".equals(dataArr[0])) {
                    client.attr(AttributeKey.valueOf(dataArr[1]));
                    ChannelGroupsManager.addClinet(client, Long.parseLong(dataArr[1]));
                }
            }
        }

        String[] dataArr = receive.split("&");
        if (receive.contains("heartbeat*t")) {
            logger.info("接收到客户端心跳数据 : {}", receive);
        } else {
            if (client.attr(signs).get() == null) {
                if (dataArr.length > 0 && "f".equals(dataArr[0])) {
                    client.attr(AttributeKey.valueOf(dataArr[1]));
                    ChannelGroupsManager.addClinet(client, Long.parseLong(dataArr[1]));
                }
            }
        }
    }
}

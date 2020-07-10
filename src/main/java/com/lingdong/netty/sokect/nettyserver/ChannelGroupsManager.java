package com.lingdong.netty.sokect.nettyserver;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * netty_socket服务端配置
 *
 * @author: gouwei
 * @date: 2020-07-10 10:51
 */
public class ChannelGroupsManager {


    /**
     * 用户和连接对应关系
     * key:user_id,value:channel
     */
    public static final ConcurrentHashMap<Long, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 连接标识
     */
    public static final AttributeKey<String> signs = AttributeKey.valueOf("r_id");

    /**
     * 所有连接
     */
    public static final ChannelGroup online_clents = new DefaultChannelGroup("online_user_clents", GlobalEventExecutor.INSTANCE);

    /**
     * 添加连接,建立连接关系
     */
    public static void addClinet(Channel channel, Long id) {
        channel.attr(AttributeKey.valueOf("r_id")).set(id);
        channelMap.put(id, channel);
        online_clents.add(channel);
    }

    public static void addClinet(Channel channel) {
        online_clents.add(channel);
    }

    /**
     * 移除连接
     */
    public static boolean remove(Channel channel, Long id) {
        channel.attr(AttributeKey.valueOf("r_id")).set(id);
        channelMap.remove(id);
        return online_clents.remove(channel);
    }


    /**
     * 判断一个通道是否有用户在使用
     * 可做信息转发时判断该通道是否合法
     *
     * @param channel
     * @return
     */
    public boolean hasClent(Channel channel) {
        AttributeKey<String> key = AttributeKey.valueOf("user");
        //netty移除了这个map的remove方法,这里的判断谨慎一点
        return (channel.hasAttr(key) || channel.attr(key).get() != null);
    }

    /**
     * 根据用户id获取该用户的通道
     *
     * @author: gouwei
     * @date: 2020-07-10 11:37
     * @param: [userId]
     * @return: io.netty.channel.Channel
     * @desc:
     */
    public Channel getChannelByUserId(Long userId) {
        return this.channelMap.get(userId);
    }

    /**
     * 判断一个用户是否在线
     *
     * @param userId
     * @return
     */
    public Boolean online(Long userId) {
        return this.channelMap.containsKey(userId) && this.channelMap.get(userId) != null;
    }


    public static ChannelGroupFuture broadcast(Object msg) {
        return online_clents.writeAndFlush(msg);
    }

    public static ChannelGroupFuture broadcast(Object msg, ChannelMatcher matcher) {
        return online_clents.writeAndFlush(msg, matcher);
    }

    /**
     * 刷新
     */
    public static ChannelGroup flush() {
        return online_clents.flush();
    }

    /**
     * 是否包含连接
     */
    public static boolean contains(Channel channel) {
        return online_clents.contains(channel);
    }

    /**
     * 当前在线人数
     */
    public static int size() {
        return online_clents.size();
    }

}

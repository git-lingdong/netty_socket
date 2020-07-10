package com.lingdong.netty.sokect.nettyserver;

import java.io.Serializable;

/**
 * @author: gouwei
 * @Date: 2020-07-10/15:59
 */
public class MsgModel implements Serializable {
    /**
     * 类型
     */
    private Integer t;
    /**
     * 内容
     */
    private Object b;

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Object getB() {
        return b;
    }

    public void setB(Object b) {
        this.b = b;
    }
}

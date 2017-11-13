package com.sctjsj.gasdrawer.event;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.sctjsj.gasdrawer.entity.javaBean.Message;
import com.sctjsj.gasdrawer.entity.javaBean.MessageBean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/6/5.
 */

public class MessageEvent implements Serializable{
   private MessageBean message;

    public MessageEvent(MessageBean message) {
        this.message = message;
    }

    public MessageBean getMessageBean() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "message=" + message +
                '}';
    }
}

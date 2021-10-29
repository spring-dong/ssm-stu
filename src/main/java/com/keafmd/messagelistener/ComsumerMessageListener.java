package com.keafmd.messagelistener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @ Author     ：scw
 * @ Date       ：Created in 下午 3:06 2018/7/15 0015
 * @ Description：消息的监听者，用于处理消息
 * @ Modified By：
 * @Version: $version$
 */
public class ComsumerMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("接受到消息：" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}


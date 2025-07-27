package com.atguigu.mqtt.handler;

import com.atguigu.mqtt.service.TbLampService;
import com.atguigu.mqtt.service.TbLampStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

//消息处理器
@Component
public class ReceiverMessageHandler implements MessageHandler {

    @Autowired
    private TbLampService tbLampService ;

    @Autowired
    private TbLampStatusService tbLampStatusService ;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String payload = message.getPayload().toString();
        MessageHeaders headers = message.getHeaders();
        String topicName = headers.get("mqtt_receivedTopic").toString();
        if("atguigu/iot/lamp/line".equals(topicName)) {
            tbLampService.updateLampOnlineStatus(payload) ;
        }else if("atguigu/iot/lamp/device/status".equals(topicName)) {
            tbLampStatusService.saveDeviceStatus(payload) ;
        }
    }

}

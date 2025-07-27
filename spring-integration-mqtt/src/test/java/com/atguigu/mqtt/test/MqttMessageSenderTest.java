package com.atguigu.mqtt.test;

import com.atguigu.mqtt.MqttClientApplication;
import com.atguigu.mqtt.service.MqttMessageSender;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MqttClientApplication.class)
public class MqttMessageSenderTest {

    @Autowired
    private MqttMessageSender mqttMessageSender ;

    @Test
    public void sendToMsg() {
        mqttMessageSender.sendMsg("java/t" , "hello mqtt spring boot...");
    }

    //订阅主题
    @Test
    public void subscribe() throws MqttException {
        MqttClient connect = connect();
        connect.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {       // 链接丢失
                System.out.println("connectionLost....");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {        // 接收消息
                System.out.println("topic ----> " + topic);
                byte[] payload = message.getPayload();
                System.out.println("msg ----> " + new String(payload));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {        // 消息完全传送完毕以后
                System.out.println("deliveryComplete...");
            }
        });
        //订阅主题
        connect.subscribe("java/b" , 2);
        while (true);
    }
    //发送消息
    @Test
    public void sendMsg() throws MqttException {
        MqttClient connect = connect();
        //发送消息
        MqttMessage message=new MqttMessage();
        message.setQos(2);
        message.setPayload("Hello word".getBytes());
        connect.publish("java/t",message);
        connect.disconnect();
        connect.close();

    }
    @Test
    void contextLoads() throws MqttException {
        connect();
        while (true){

        }
    }

    private static MqttClient connect() throws MqttException {
        // 定义链接相关参数
        String serverURI = "tcp://127.0.0.1:1883" ;
        String clientId = "paho_client_123" ;

        // 创建一个MqttClient对象
        // public MqttClient(String serverURI, String clientId, MqttClientPersistence persistence)
        MqttClient mqttClient = new MqttClient(serverURI , clientId , new MemoryPersistence()) ;

        // 创建MqttConnectOptions对象
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions() ;
        mqttConnectOptions.setUserName("zhangsan");
        mqttConnectOptions.setPassword("123456".toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttClient.connect(mqttConnectOptions);
        return mqttClient;
    }

}

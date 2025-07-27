<script setup>
import {ref} from "vue";
import mqtt from 'mqtt'

// 消息质量取值数组
const qosList = [0, 1, 2];

// 定义链接参数的对象
const connectionInfo = ref({
  protocol: 'ws' ,
  host: '192.168.1.80' ,
  port: '8083' ,
  clientId: 'emqx_vue_client_' + Math.random().toString().substring(2 , 8) ,
  username: 'zhangsan' ,
  password: '123456'
})

// 定义建立链接的事件处理函数
const clientInitData = ref({
  connected: false
})
const client = ref({})
const createConnection = () => {
    const { protocol, host, port, ...options } = connectionInfo.value
    const connectionUrl = `${protocol}://${host}:${port}/mqtt`
    client.value = mqtt.connect(connectionUrl , options)
    clientInitData.value.connected = true
    console.info("链接建立成功了...")
}

// 端口链接的事件处理函数
const closeConnection = () => {
  client.value.end(false , () => {
    clientInitData.value.connected = false ;
    console.info("链接断开了...")
  })
}

// 订阅主题所需要的js对象
const subscriptionInfo = ref({
  topic: '',
  qos: 0
})
const receivedMessages = ref({})
const subscriptionInitData = ref({
  subscription: false
})

// 订阅主题事件处理函数
const subscriptionTopicHandler = () => {
  const { topic , qos } = subscriptionInfo.value
  client.value.subscribe(topic , { qos }, (error , res) => {
      if(error) {
        console.info("主题订阅失败了...")
        return  ;
      }

      subscriptionInitData.value.subscription = true
      // 给链接对象注册一个接收消息的事件
      client.value.on('message' , (topic , message) => {
        receivedMessages.value = topic + "---->" + message
      })

  })
}

// 取消订阅的事件处理函数
const unSubscriptionTopicHandler = () => {
  const { topic , qos } = subscriptionInfo.value
  client.value.unsubscribe(topic , { qos } , (error , res) => {
    if(error) {
      console.info("主题取消订阅失败了...")
      return ;
    }
    subscriptionInitData.value.subscription = false ;

  })
}

// 发送消息所需要js对象
const publishInfo = ref({
  topic: '',
  qos: 0 ,
  payload: ''
})

// 发送消息的事件处理函数
const doPublish = () => {
  const { topic , qos ,payload } = publishInfo.value
  client.value.publish(topic , payload , { qos } , (error , res) => {
      if(error) {
        console.info("发送消息失败了...")
        return ;
      }
      console.info("发送消息成功了...")
  })
}

</script>

<template>
  <div class="mqtt-demo">
    <el-card>
      <h1>配置信息</h1>
      <el-form label-position="top" >
        <el-row :gutter="20">

          <el-col :span="8">
            <el-form-item prop="protocol" label="选择协议">
              <el-select v-model="connectionInfo.protocol">
                <el-option label="ws://" value="ws"></el-option>
                <el-option label="wss://" value="wss"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item prop="host" label="主机地址">
              <el-input v-model="connectionInfo.host"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item prop="port" label="端口号">
              <el-input type="number" v-model="connectionInfo.port" placeholder="8083/8084"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item prop="clientId" label="客户端ID">
              <el-input v-model="connectionInfo.clientId" > </el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item prop="username" label="用户名">
              <el-input v-model="connectionInfo.username" ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item prop="password" label="密码">
              <el-input v-model="connectionInfo.password"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-button type="primary" :disabled="clientInitData.connected" @click="createConnection">建立连接</el-button>
            <el-button type="danger" :disabled="!clientInitData.connected" @click="closeConnection">断开连接</el-button>
          </el-col>

        </el-row>
      </el-form>
    </el-card>


    <el-card>
      <h1>订阅主题</h1>
      <el-form label-position="top" >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="topic" label="Topic">
              <el-input v-model="subscriptionInfo.topic"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="qos" label="QoS">
              <el-select v-model="subscriptionInfo.qos">
                <el-option
                    v-for="qos in qosList"
                    :key="qos"
                    :label="qos"
                    :value="qos"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-button  type="primary" class="sub-btn" :disabled="subscriptionInitData.subscription" @click="subscriptionTopicHandler">订阅主题</el-button>
            <el-button type="primary" class="sub-btn" :disabled="!subscriptionInitData.subscription"  @click="unSubscriptionTopicHandler">取消订阅</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card>
      <h1>发布消息</h1>
      <el-form label-position="top" >
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item prop="topic" label="Topic">
              <el-input v-model="publishInfo.topic"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="payload" label="Payload">
              <el-input v-model="publishInfo.payload"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="qos" label="QoS">
              <el-select v-model="publishInfo.qos">
                <el-option
                    v-for="qos in qosList"
                    :key="qos"
                    :label="qos"
                    :value="qos"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-col :span="24" class="text-right" >
        <el-button type="primary" @click="doPublish">发布消息</el-button>
      </el-col>
    </el-card>

    <el-card>
      <h1>接收到的消息</h1>
      <el-col :span="24">
        <el-input v-model="receivedMessages"
            type="textarea"
            :rows="3"
            readonly
        ></el-input>
      </el-col>
    </el-card>
  </div>
</template>

<style>
.mqtt-demo {
  max-width: 1200px;
  margin: 32px auto 0 auto;
}

h1 {
  font-size: 16px;
  margin-top: 0;
}

.el-card {
  margin-bottom: 32px;
}
.el-card__body {
  padding: 24px;
}

.el-select {
  width: 100%;
}

.text-right {
  text-align: right;
}

.sub-btn {
  margin-top: 30px;
}
</style>
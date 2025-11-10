package com.skypapaya.firerescuemanagesystem.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skypapaya.firerescuemanagesystem.DAO.EnvironmentDAO;
import com.skypapaya.firerescuemanagesystem.DAO.VitalSignsDAO;
import com.skypapaya.firerescuemanagesystem.DO.EnvironmentDO;
import com.skypapaya.firerescuemanagesystem.DO.VitalSignsDO;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class HuaweiCloudMQTT {
    private EnvironmentDAO environmentDAO;
    private VitalSignsDAO vitalSignsDAO;
    private String data;

    public HuaweiCloudMQTT(VitalSignsDAO vitalSignsDAO, EnvironmentDAO environmentDAO) {
        this.vitalSignsDAO = vitalSignsDAO;
        this.environmentDAO = environmentDAO;
    }



    public void getInf() {

        String broker = "ssl://5e1d9e2307.st1.iotda-device.cn-north-4.myhuaweicloud.com:8883";
        String clientId = "65feba432ccc1a58387dee47_java_server_02_0_0_2024071905";
        String topic = "$devices/java_server_02/user/get_post";  // 替换为你要发布和订阅的主题
        //String content = "Message from MqttPublishSample";
        int qos = 2;
        String username = "65feba432ccc1a58387dee47_java_server_02";
        String password = "476b330a901c119c9ca15c3cd8eeb0cac9cd60b9f974b998df3310a3e32687a2";

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setKeepAliveInterval(60);
            connOpts.setConnectionTimeout(30);

            //System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            //System.out.println("Connected");

            // 订阅主题
            client.subscribe(topic, qos);
            client.setCallback(new MqttCallback() {


                @Override
                public void connectionLost(Throwable throwable) {

                }

                //消息内容
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    try {
                        // 解析消息
                        data = new String(message.getPayload());

                        // 打印收到的数据
                        System.out.println("Received data: " + data);

                        // 尝试将字符串解析为 JSON 对象
                        JSONObject jsonObject = JSON.parseObject(data);
                        JSONArray servicesArray = jsonObject.getJSONArray("services");

                        if (servicesArray != null && !servicesArray.isEmpty()) {
                            JSONObject serviceObject = servicesArray.getJSONObject(0);
                            JSONObject propertiesObject = serviceObject.getJSONObject("properties");

                            // 获取各个属性的值并处理
                            //环境信息数据
                            float smoke = propertiesObject.getFloatValue("smoke");
                            float co = propertiesObject.getFloatValue("CO");
                            float fire = propertiesObject.getFloatValue("FIRE");
                            float temperature = propertiesObject.getFloatValue("temperature");
                            float humidity = propertiesObject.getFloatValue("Humidity");
                            //生命体征数据
                            float breathRate = propertiesObject.getFloatValue("BreathRate");

                            Integer heartRate = propertiesObject.getInteger("HeartRate");
                            float signalStrength = propertiesObject.getFloatValue("SignalStrength");


                            Integer active = propertiesObject.getInteger("active");
                            Integer distance = propertiesObject.getInteger("distance");
                            Integer exist = propertiesObject.getInteger("exist");
                            Integer life = propertiesObject.getInteger("life");
                            Integer people = propertiesObject.getInteger("people");

                            System.out.println(life);

                            //这里有问题！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！  device现在是写死的，mqtt不会返回设备名字
                            // 插入数据库
                            EnvironmentDO environmentDO = new EnvironmentDO(co, fire, humidity, 0, smoke, temperature,"133");
                            environmentDAO.insertEnvironmentDO(environmentDO);
                            VitalSignsDO vitalSignsDO = new VitalSignsDO( breathRate,heartRate,  signalStrength,  active,  distance,  exist,  life,  people);
                            vitalSignsDAO.insertVitalSignsDO(vitalSignsDO);

                            System.out.println("Environment data inserted successfully.");
                        } else {
                            System.out.println("No services found in the JSON data.");
                        }
                    } catch (ClassCastException e) {
                        System.out.println("ClassCastException caught: " + e.getMessage());
                        //e.printStackTrace();  // 打印堆栈信息
                    } catch (Exception e) {
                        System.out.println("Exception caught: " + e.getMessage());
                        //e.printStackTrace();  // 打印堆栈信息
                    }
                }


                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                   // System.out.println("Delivery complete. " + token.isComplete());
                }
            });

            // 发布消息
            /*
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            //System.out.println("Publishing message: " + content);
            client.publish(topic, message);
            //System.out.println("Message published");

             */
            // 保持连接以接收消息
            Thread.sleep(60000000);//保持连接1分钟
            client.disconnect();
            //System.out.println("Disconnected");
            System.exit(0);

        } catch (MqttException | InterruptedException me) {
            //System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
    public void saveInf(){

        /*
        try{
            JSONObject jsonObject = JSON.parseObject(data);
            JSONArray servicesArray = jsonObject.getJSONArray("services");

            float breathingRate = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("BreathingRate").getString("value"));
            Integer heartRate = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("HeartRate").getString("value"));
            float signalStrength = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("SignalStrength").getString("value"));
            Integer active = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("active").getString("value"));
            Integer distance = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("distance").getString("value"));
            Integer exist = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("exist").getString("value"));
            Integer life = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("life").getString("value"));
            Integer people = Integer.parseInt(jsonObject.getJSONObject("checkFailedData").getJSONObject("people").getString("value"));

            VitalSignsDO vitalSignsDO = new VitalSignsDO(breathingRate, heartRate, signalStrength, active, distance, exist, life, people);
            vitalSignsDAO.insertVitalSignsDO(vitalSignsDO);

            float co = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("CO").getString("value"));
            float fire = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("FIRE").getString("value"));
            float humidity = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("Humidity").getString("value"));
            float risk = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("Risk").getString("value"));
            float smoke = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("smoke").getString("value"));
            float temperature = Float.parseFloat(jsonObject.getJSONObject("checkFailedData").getJSONObject("temperature").getString("value"));

            System.out.println(co);
            EnvironmentDO environmentDO = new EnvironmentDO(co, fire, humidity, risk, smoke, temperature);
            environmentDAO.insertEnvironmentDO(environmentDO);
        }catch (Exception e){
            e.printStackTrace();
        }


         */
        getInf();
        //System.out.println(data);

        JSONObject jsonObject = JSON.parseObject(data);
        JSONArray servicesArray = jsonObject.getJSONArray("services");

        if (servicesArray != null && !servicesArray.isEmpty()) {
            JSONObject serviceObject = servicesArray.getJSONObject(0);
            JSONObject propertiesObject = serviceObject.getJSONObject("properties");

            System.out.println(data);
            // 获取各个属性的值
            float smoke = propertiesObject.getFloatValue("smoke");
            float co = propertiesObject.getFloatValue("CO");
            float fire = propertiesObject.getFloatValue("FIRE");
            int people = propertiesObject.getIntValue("people");
            float temperature = propertiesObject.getFloatValue("temperature");
            float humidity = propertiesObject.getFloatValue("Humidity");

            // 输出结果
            System.out.println("Smoke: " + smoke);
            System.out.println("CO: " + co);
            System.out.println("FIRE: " + fire);
            System.out.println("People: " + people);
            System.out.println("Temperature: " + temperature);
            System.out.println("Humidity: " + humidity);
        } else {
            System.out.println("No services found in the JSON data.");
        }


    }


}

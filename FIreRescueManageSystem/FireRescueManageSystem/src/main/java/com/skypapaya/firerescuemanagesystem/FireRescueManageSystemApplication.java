package com.skypapaya.firerescuemanagesystem;
import com.skypapaya.firerescuemanagesystem.DAO.EnvironmentDAO;
import com.skypapaya.firerescuemanagesystem.DAO.MessageDAO;
import com.skypapaya.firerescuemanagesystem.DAO.UserDAO;
import com.skypapaya.firerescuemanagesystem.DAO.VitalSignsDAO;
import com.skypapaya.firerescuemanagesystem.mqtt.HuaweiCloudMQTT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication

public class FireRescueManageSystemApplication implements ApplicationRunner {

    @Autowired
    UserDAO userDAO;
    @Autowired
    EnvironmentDAO environmentDAO;
    @Autowired
    VitalSignsDAO vitalSignsDAO;

    @Autowired
    MessageDAO messageDAO; //注入MessageDAO

    public static void main(String[] args) {
        SpringApplication.run(FireRescueManageSystemApplication.class, args);
        System.out.print("start");

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        MqttPostPropertyMessageListener mqttPostPropertyMessageListener = new MqttPostPropertyMessageListener(vitalSignsDAO,environmentDAO);
//        mqttPostPropertyMessageListener.getMessage();
        HuaweiCloudMQTT huaweiCloudMQTT = new HuaweiCloudMQTT(vitalSignsDAO,environmentDAO);
        //huaweiCloudMQTT.saveInf();
        huaweiCloudMQTT.getInf();
    }
}

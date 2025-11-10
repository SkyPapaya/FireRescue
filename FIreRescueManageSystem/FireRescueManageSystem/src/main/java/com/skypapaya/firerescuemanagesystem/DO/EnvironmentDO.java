package com.skypapaya.firerescuemanagesystem.DO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnvironmentDO {

    Long id;
    float co;
    float fire;
    float humidity;
    float risk;
    float smoke;
    float temperature;
    String device_name;
    EnvironmentDO environmentDO;
    LocalDateTime createTime;
    LocalDateTime modifyTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCo() {
        return co;
    }

    public void setCo(float co) {
        this.co = co;
    }

    public float getFire() {
        return fire;
    }

    public void setFire(float fire) {
        this.fire = fire;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }

    public float getSmoke() {
        return smoke;
    }

    public void setSmoke(float smoke) {
        this.smoke = smoke;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public EnvironmentDO getEnvironmentDO() {
        return environmentDO;
    }

    public void setEnvironmentDO(EnvironmentDO environmentDO) {
        this.environmentDO = environmentDO;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public EnvironmentDO(float co, float fire, float humidity, float risk, float smoke, float temperature,String device_name) {
        this.co = co;
        this.fire = fire;
        this.humidity = humidity;
        this.risk = risk;
        this.smoke = smoke;
        this.temperature = temperature;
        this.device_name = device_name;
    }

    public EnvironmentDO() {

    }
    
}

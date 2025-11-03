package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceDO {

    private Long id;
    private String name;
    private String location;
    private String status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    // Lombok 的 @Data 会自动生成 getters, setters, toString, equals, 和 hashCode
}
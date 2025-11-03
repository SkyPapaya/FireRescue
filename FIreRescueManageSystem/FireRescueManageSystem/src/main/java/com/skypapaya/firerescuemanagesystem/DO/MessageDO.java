package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDO {
    private Long id;
    private String title;
    private LocalDateTime messageTime;
    private Integer status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
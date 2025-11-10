package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDO {

    private Long id;
    private String title;

    // 对应数据库的 message_time
    private LocalDateTime messageTime;

    // 对应 TINYINT, 0=未读, 1=已读, 2=回收站
    private Integer status;

    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
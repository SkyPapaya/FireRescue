package com.skypapaya.firerescuemanagesystem.DO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoDO {
    private Long id;
    private String title;
    private Boolean status;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}

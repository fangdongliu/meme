package cn.fdongl.meme.task.entity;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class TaskShort {

    Integer taskId;
    Date startDate;
    Date endDate;
    Integer weekCount;
    Integer aUserId;
    String aDescription;
    String aColor;
    String aIcon;
    String bDescription;
    String bColor;
    String bIcon;
}

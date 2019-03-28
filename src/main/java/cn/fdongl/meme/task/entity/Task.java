package cn.fdongl.meme.task.entity;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class Task {
    Integer aHp;
    Integer bHp;
    Integer rewardType;
    String rewardParam;
    Integer status;
    Integer taskId;
    Integer aUserId;
    Date startDate;
    Date endDate;
    Integer weekCount;
    Integer bUserId;
    Integer aStartTime;
    String aDescription;
    String aColor;
    String aIcon;
    Integer bStartTime;
    String bDescription;
    String bColor;
    String bIcon;
    Integer aCount;
    Integer aRCount;
    Integer bCount;
    Integer bRCount;
    String aRepeatWeek;
    String bRepeatWeek;
}

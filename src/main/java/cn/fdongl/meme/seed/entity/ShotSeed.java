package cn.fdongl.meme.seed.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class ShotSeed {

    Date finishTime;
    String taskLevel;
    String iconColor;
    String taskIconFilename;

}

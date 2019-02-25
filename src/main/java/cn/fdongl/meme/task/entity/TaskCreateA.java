package cn.fdongl.meme.task.entity;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TaskCreateA {

    Integer aUserId;

    Integer hp;

    String endDate;

    Integer pairId;

    @NotNull
    String startDate;

    @NotNull
    @Min(1)
    Integer weekCount;

    @NotNull
    @Min(0L)
    @Max(23L)
    Integer startTime;
    @NotNull
    @Size(max=48)
    String description;
    @NotNull
    @Size(max=48)
    String color;
    @NotNull
    @Size(max=48)
    String icon;
    @NotNull
    @Size(max = 7,min = 7)
    String repeatWeek;
    @NotNull
    Integer rewardType;

    @NotNull
    @Size(max = 196)
    String rewardParam;

}

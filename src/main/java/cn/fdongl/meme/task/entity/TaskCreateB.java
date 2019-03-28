package cn.fdongl.meme.task.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TaskCreateB {

    Integer hp;

    Integer userId;

    @NotNull
    @Range(max = 22,min = 0)
    Integer startTime;
    @NotNull
    @Length(max=48)
    String description;
    @NotNull
    @Length(max=48)
    String color;
    @NotNull
    @Length(max=48)
    String icon;
    @NotNull
    @Length(max = 7,min = 7)
    String repeatWeek;

    Integer pairId;

}

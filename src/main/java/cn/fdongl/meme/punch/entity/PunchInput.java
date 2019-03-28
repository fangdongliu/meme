package cn.fdongl.meme.punch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunchInput {

    Integer pairId;
    Integer growthValue;
    Integer taskId;
    String image;
    String description;
    String createDate;
    Integer createUser;
    String taskLevel;

}

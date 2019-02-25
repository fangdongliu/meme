package cn.fdongl.meme.punch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Punch {

    Integer taskId;
    String image;
    String description;
    String createDate;
    Integer createUser;
    Integer flag;

}

package cn.fdongl.meme.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFile {

    byte[] data;
    String filename;
    String type;
    Integer createUser;

}

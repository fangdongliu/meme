package cn.fdongl.meme.pair.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class UserPair implements Serializable {

    Integer aUserId;
    String aNickname;
    String aAvatar;
    Integer aGrowth;
    Integer aPunchCount;
    Integer aRPunchCount;
    String bNickname;
    String bAvatar;
    Integer bGrowth;
    Integer bPunchCount;
    Integer bRPunchCount;
    Timestamp createDate;
    Timestamp endDate;

}

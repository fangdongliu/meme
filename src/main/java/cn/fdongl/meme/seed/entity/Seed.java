package cn.fdongl.meme.seed.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Seed {
    Integer rank;
    String name;
    String description;
    String iconFilename;
    List<ShotSeed>seeds;

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}

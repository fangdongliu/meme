package cn.fdongl.meme.seed.mapper;

import cn.fdongl.meme.seed.entity.Flower;
import cn.fdongl.meme.seed.entity.Seed;
import cn.fdongl.meme.seed.entity.SeedStatus;
import cn.fdongl.meme.seed.entity.ShotSeed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface SeedMapper {

    @Select("SELECT seed.finish_time AS 'finishTime',\n" +
            "\t\t seed.task_level AS 'taskLevel',\n" +
            "\t\t task.a_icon AS 'taskIconFilename',\n" +
            "\t\t task.a_color AS 'iconColor'\n" +
            "\t\t FROM seed \n" +
            "\t\t INNER JOIN task\n" +
            "\t\t ON seed.task_id=task.task_id AND seed.pair_id=task.pair_id\n" +
            "\t\t WHERE seed.`status`=1 AND seed.pair_id=#{param1} AND seed.seed_id=#{param2}")
        List<ShotSeed> taskFinish(Integer pairId, Integer seedId);

    @Select("SELECT `seed_id` AS 'seedId'" +
            "`name` AS 'name',\n" +
            "\t\t `description` AS 'description',\n" +
            "\t\t `icon_filename` AS 'iconFilename'\n" +
            "\t\t FROM flower WHERE `seed_id`=#{param1}")
    Seed flower(Integer seedId);

    @Select("SELECT `name` AS 'name',\n" +
            " \t\t `description` AS 'description',\n" +
            " \t\t `icon_filename` AS 'iconFilename'\n" +
            " \t\t FROM flower ")
    List<Flower>flowerAll();

    @Select("SELECT COUNT(`seed_id`=#{param1} AND `status`=1 OR NULL) AS 'rank' from seed")
    Integer rank(Integer seedId);

    @Select("SELECT seed_id from seed where pair_id=#{param1}")
    List<Integer> unlock(Integer pairId);

    @Select("SELECT `task_level` AS 'taskLevel',\n" +
            "\t\t`seed_growth` AS 'growth',\n" +
            "\t\t`seed_need` AS 'need' FROM seed WHERE `task_id`=#{param1}")
    public SeedStatus status(Integer taskId);
}

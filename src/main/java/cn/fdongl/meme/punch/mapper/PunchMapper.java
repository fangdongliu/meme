package cn.fdongl.meme.punch.mapper;

import cn.fdongl.meme.punch.entity.Image;
import cn.fdongl.meme.punch.entity.Punch;
import cn.fdongl.meme.punch.entity.PunchInput;
import cn.fdongl.meme.seed.entity.SeedStatus;
import cn.fdongl.meme.task.entity.CreateFile;
import org.apache.ibatis.annotations.*;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Mapper
public interface PunchMapper {


    @Select("select a_punchCount from pair where pair_id=#{param1}")
    Integer getAPunchCount(Integer pairId);

    @Select("select b_punchCount from pair where pair_id=#{param1}")
    Integer getBPunchCount(Integer pairId);

    @Select("SELECT `image`,\n" +
            "\tdescription,\n" +
            "\tcreate_date as createDate,\n" +
            "\tcreate_user as createUser,\n" +
            "\tflag\n" +
            "\tFROM punch\n" +
            "\tWHERE task_id=#{param1}")
    public List<Punch>list(Integer taskId);

    @Select("SELECT punch_id FROM punch WHERE task_id = #{param3} AND create_user = #{param1} AND (create_date = #{param2})")
    Integer status(Integer userId, String date, Integer taskId);

    @Select("insert into `image`(filename,`type`,create_user,create_date,data)values(#{filename},#{type},#{createUser},now(),#{data});" +
            "SELECT @@IDENTITY;")
    public Integer createFile(CreateFile param);

    @Select("select data from `image` where filename = #{param1} limit 1")
    public InputStream getImage(String filename);

    @Insert("INSERT INTO punch(task_id,create_user,create_date,description,`image`) VALUES(#{taskId},#{createUser},#{createDate},#{description},#{image});" +
            "update task set a_count = a_count+1 where task_id = #{taskId};" +
            "update pair set a_punchCount=a_punchCount+1,a_growth=a_growth+#{growthValue} where pair_id=#{pairId}"
           )
    public Integer punchA(PunchInput punch);

    @Update("UPDATE seed SET `seed_growth`=`seed_growth`+1,`task_level`=#{taskLevel} WHERE task_id=#{taskId}")
    public Integer punchAseed(PunchInput punchInput);

    @Insert("INSERT INTO punch(task_id,create_user,create_date,description,`image`) VALUES(#{taskId},#{createUser},#{createDate},#{description},#{image});" +
            "update task set b_count = b_count+1 where task_id = #{taskId};" +
            "update pair set b_punchCount=b_punchCount+1,b_growth=b_growth+#{growthValue} where pair_id=#{pairId}")
    public Integer punchB(PunchInput punch);

    @Update("UPDATE seed SET `seed_growth`=`seed_growth`+1,`task_level`=#{taskLevel} WHERE task_id=#{taskId}")
    public Integer punchBseed(PunchInput punchInput);

    @Insert("INSERT INTO punch(flag,task_id,create_user,create_date,description,`image`) VALUES(1,#{taskId},#{createUser},#{createDate},#{description},#{image});" +
            "update task set a_rCount = a_rCount+1 where task_id = #{taskId};" +
            "update pair set a_rPunchCount=a_rPunchCount+1,a_growth=a_growth+#{growthValue} where pair_id=#{pairId}")
    public Integer supplyA(PunchInput punch);

    @Update("UPDATE seed SET `seed_growth`=`seed_growth`+1,`task_level`=#{taskLevel} WHERE task_id=#{taskId}")
    public  Integer supplyAseed(PunchInput punchInput);

    @Insert("INSERT INTO punch(flag,task_id,create_user,create_date,description,`image`) VALUES(1,#{taskId},#{createUser},#{createDate},#{description},#{image});" +
            "update task set b_rCount = b_rCount+1 where task_id = #{taskId};" +
            "update pair set b_rPunchCount=b_rPunchCount+1,b_growth=b_growth+#{growthValue} where pair_id=#{pairId}")
    public Integer supplyB(PunchInput punch);

    @Update("UPDATE seed SET `seed_growth`=`seed_growth`+1,`task_level`=#{taskLevel} WHERE task_id=#{taskId}")
    public  Integer supplyBseed(PunchInput punchInput);

    @Select("SELECT `task_level` AS 'taskLevel',\n" +
            "\t\t`seed_growth` AS 'growth',\n" +
            "\t\t`seed_need` AS 'need' FROM seed WHERE `task_id`=#{param1}")
    public SeedStatus getSeedStatus(Integer taskId);

    @Update("UPDATE seed SET `status`=1,finish_time=#{param2} WHERE task_id=#{param1}")
    public Integer finish(Integer taskId,String date);
}

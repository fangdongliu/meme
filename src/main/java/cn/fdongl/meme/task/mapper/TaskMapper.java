package cn.fdongl.meme.task.mapper;

import cn.fdongl.meme.punch.entity.Punch;
import cn.fdongl.meme.punch.entity.PunchStatus;
import cn.fdongl.meme.task.entity.Task;
import cn.fdongl.meme.task.entity.TaskCreateA;
import cn.fdongl.meme.task.entity.TaskCreateB;
import cn.fdongl.meme.task.entity.TaskShort;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Select("SELECT task_id FROM task WHERE pair_id = #{param1} AND ((`status`=-1 and create_date=CURRENT_DATE()) or (`status`=0 and end_date >= now())) limit 1")
    Integer getCurrentTaskId(Integer pairId);

    @Select("SELECT \n" +
            "\ttask_id AS taskId,\n" +
            "\ta_id AS aUserId,\n" +
            "\ta_description AS aDescription,\n" +
            "\ta_color AS aColor,\n" +
            "\ta_icon AS aIcon,\n" +
            "\tb_description AS bDescription,\n" +
            "\tb_color AS bColor,\n" +
            "\tb_icon AS bIcon,\n" +
            "\tstart_date AS startDate,\n" +
            "\tend_date AS endDate,\n" +
            "\tweek_count AS weekCount\n" +
            "FROM task\n" +
            "WHERE pair_id=#{param1} AND (`status`=0 or `status`=1)\n" +
            "ORDER BY start_date")
    List<TaskShort>list(Integer pairId);



    @Select("SELECT \n" +
            "\treward_type AS rewardType,\n" +
            "\treward_param AS rewardParam,\n" +
            "\t`status` AS `status`,\n" +
            "\ta_count AS aCount,\n" +
            "\ta_rCount AS aRCount,\n" +
            "\ta_repeatWeek AS aRepeatWeek,\n" +
            "\tb_repeatWeek AS bRepeatWeek,\n" +
            "\tb_count AS bCount,\n" +
            "\tb_rCount AS bRCount,\n" +
            "\ttask_id AS taskId,\n" +
            "\ta_id AS aUserId,\n" +
            "\tb_id AS bUserId,\n" +
            "\tstart_time AS aStartTime,\n" +
            "\ta_description AS aDescription,\n" +
            "\ta_color AS aColor,\n" +
            "\ta_icon AS aIcon,\n" +
            "\tb_start_time AS bStartTime,\n" +
            "\tb_description AS bDescription,\n" +
            "\tb_color AS bColor,\n" +
            "\tb_icon AS bIcon,\n" +
            "\tstart_date AS startDate,\n"+
            "\tend_date AS endDate,\n" +
            "\tweek_count AS weekCount\n" +
            "FROM task\n" +
            "WHERE task_id=#{param1}")
    Task info(Integer taskId);

    @Select("SELECT \n" +
            "\treward_type AS rewardType,\n" +
            "\treward_param AS rewardParam,\n" +
            "\t`status` AS `status`,\n" +
            "\ta_count AS aCount,\n" +
            "\ta_rCount AS aRCount,\n" +
            "\ta_hp as aHp," +
            "\tb_hp as bHp," +
            "\tb_count AS bCount,\n" +
            "\tb_rCount AS bRCount,\n" +
            "\ttask_id AS taskId,\n" +
            "\ta_repeatWeek AS aRepeatWeek,\n" +
            "\tb_repeatWeek AS bRepeatWeek,\n" +
            "\ta_id AS aUserId,\n" +
            "\tb_id AS bUserId,\n" +
            "\tstart_time AS aStartTime,\n" +
            "\ta_description AS aDescription,\n" +
            "\ta_color AS aColor,\n" +
            "\ta_icon AS aIcon,\n" +
            "\tb_start_time AS bStartTime,\n" +
            "\tb_description AS bDescription,\n" +
            "\tb_color AS bColor,\n" +
            "\tb_icon AS bIcon,\n" +
            "\tstart_date AS startDate,\n" +
            "\tweek_count AS weekCount\n" +
            "FROM task\n" +
            "WHERE pair_id = #{param1} AND ((`status`=-1 and create_date=CURRENT_DATE()) or (`status`=0 and end_date >= now()))")
    Task info2(Integer pairId);




    @Insert("INSERT INTO task(\n" +
            "\tpair_id,\n" +
            "\ta_id,\n" +
            "\treward_type,\n" +
            "\treward_param,\n" +
            "\ta_description,\n" +
            "\ta_color,\n" +
            "\ta_icon,\n" +
            "\ta_repeatWeek,\n" +
            "\tweek_count,\n" +
            "\tstart_time,\n" +
            "\tstart_date,\n" +
            "\tcreate_date,\n"+
            "\tend_date,\n"+
            "\ta_hp)values\n" +
            "\t(#{pairId},#{aUserId},#{rewardType}  ,#{rewardParam},#{description},#{color},#{icon},#{repeatWeek},\n" +
            "\t#{weekCount},#{startTime},#{startDate},CURRENT_DATE(),#{endDate},#{hp})")
    Integer createA(TaskCreateA param);

    @Insert("INSERT INTO seed(pair_id,seed_id,seed_need)values(#{param1.pairId},#{param1.rewardParam},#{param1.weekCount}*#{param2})")
    Integer createSeedA(TaskCreateA param,Integer n);

    @Update("UPDATE seed SET seed_need=seed_need+(SELECT week_count from task where task_id=#{param3} limit 1)*#{param2}\n" +
            "WHERE task_id=#{param3}")
    Integer createSeedB(TaskCreateB param,Integer n,Integer taskId);

    @Update("UPDATE task SET\n" +
            "\tb_description=#{description},\n" +
            "\tb_color=#{color},\n" +
            "\tb_icon=#{icon},\n" +
            "\tb_id=#{userId},\n" +
            "\tb_repeatWeek=#{repeatWeek},\n" +
            "\tb_start_time=#{startTime},\n" +
            "\tb_hp=(#{hp}*week_count/20)," +
            "\tstatus=0 \n" +
            "\tWHERE task.pair_id = #{pairId} AND task.`status` = -1 and create_date=CURRENT_DATE()")
    Integer createB(TaskCreateB param);

    @Update("UPDATE task SET `status` = (1-`status`) WHERE pair_id = #{param1} AND ((`status`=-1 and create_date=CURRENT_DATE()) or (`status`=0 and end_date >= now()))")
    Integer end(Integer pairId);

}

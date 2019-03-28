package cn.fdongl.meme.pair.mapper;

import cn.fdongl.meme.pair.entity.ShortPair;
import cn.fdongl.meme.pair.entity.UserPair;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PairMapper {

    @Select("select pair_id from pair where `status` = 0 and (a_id = #{param1} or b_id = #{param1}) limit 1")
    Integer getUserPair(Integer userId);

    @Select("delete from pair where (a_id = #{param1}) and `status` = -1;" +
            "insert into pair(a_id,b_id,`status`,create_date) values(#{param1},null,-1,now());" +
            "select @@IDENTITY;")
    Integer start(Integer userId);

    @Delete("delete from pair where (a_id = #{param1}) and `status` = -1;")
    Integer deletePairs(Integer userId);

    @Update("update pair set b_id = #{param2},`status` = 0 where pair_id = #{param1} and a_id != #{param2} and `status` = -1;"+
            "INSERT INTO seed (`pair_id`) VALUES (#{param1});" +
            "INSERT INTO medal (`pair_id`) VALUES (#{param1});" )
    Integer accept(Integer pairId,Integer userId);

    @Update("update pair set `status`=1,end_date=now() where `status` = 0 and (a_id=#{param2} or b_id=#{param2})")
    Integer end(Integer userId);

    @Update("update pair set `status` = `status` + 4 where pair_id = #{param1} and a_id=#{param2} and (`status` = 1 or `status` =3)")
    Integer deleteA(Integer pairId,Integer userId);

    @Update("update pair set `status` = `status` + 2 where pair_id = #{param1} and b_id=#{param2} and (`status` = 1 or `status` =5)")
    Integer deleteB(Integer pairId,Integer userId);

    @Select("SELECT A.nickname AS aNickname," +
            "A.avatar AS aAvatar," +
            "B.nickname AS bNickname," +
            "B.avatar AS bAvatar," +
            "C.a_id AS aUserId," +
            "C.a_growth AS aGrowth," +
            "C.b_growth AS bGrowth," +
            "C.a_punchCount AS aPunchCount," +
            "C.b_punchCount AS bPunchCount," +
            "C.a_rPunchCount AS aRPunchCount," +
            "C.b_rPunchCount AS bRPunchCount," +
            "C.create_date AS createDate," +
            "C.end_date AS endDate " +
            "from pair C left join user A ON C.a_id = A.user_id left JOIN user B ON C.b_id = B.user_id WHERE C.pair_id = #{param1}")
    UserPair info(Integer userId);

    @Select("SELECT user.nickname," +
            "T.pair_id as pairId FROM user RIGHT JOIN (SELECT pair_id,(case when a_id = #{param1} then b_id ELSE a_id End)AS a FROM pair WHERE " +
            "(pair.a_id=#{param1} AND (pair.`status`=1 OR pair.`status`=3)) OR " +
            "(pair.b_id=#{param1} AND (pair.`status`=1 OR pair.`status`=5)))T ON user.user_id = T.a")
    List<ShortPair>list(Integer userId);


}

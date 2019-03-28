package cn.fdongl.meme.authority.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginMapper {

    @Select("select pair_id from pair where `status` = 0 and (a_id = #{param1} or b_id = #{param1}) limit 1")
    Integer getUserPair(Integer userId);

    @Select("insert into user(open_id,avatar,nickname,create_date) values(#{param1},#{param2},#{param3},now());" +
            "select @@IDENTITY;")
    Integer insertUser(String openid,String avatar,String nickname);

    @Select("select user_id from user where open_id = #{param1} limit 1")
    Integer getUser(String openid);

    @Update("update user set avatar=#{param2},nickname=#{param3} where user_id=#{param1}")
    Integer updateUser(Integer userId,String avatar,String nickname);


}

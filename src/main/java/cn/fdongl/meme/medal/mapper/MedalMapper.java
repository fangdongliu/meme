package cn.fdongl.meme.medal.mapper;

import cn.fdongl.meme.medal.entity.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MedalMapper {

    @Select("SELECT `first_1` AS 'firstOne',\n" +
            "\t\t`first_1_date` AS 'firstOneDate',\n" +
            "\t\t`first_2` AS 'firstTwo',\n" +
            "\t\t`first_2_date` AS 'firstTwoDate',\n" +
            "\t\t`first_3` AS 'firstThree',\n" +
            "\t\t`first_3_date` AS 'firstThreeDate',\n" +
            "\t\t`sum_1` AS 'sumOne',\n" +
            "\t\t`sum_1_date` AS 'sumOneDate',\n" +
            "\t\t`sum_2` AS 'sumTwo',\n" +
            "\t\t`sum_2_date` AS 'sumTwoDate',\n" +
            "\t\t`sum_3` AS 'sumThree',\n" +
            "\t\t`sum_3_date` AS 'sumThreeDate',\n" +
            "\t\t`seed_1` AS 'seedOne',\n" +
            "\t\t`seed_1_date` AS 'seedOneDate',\n" +
            "\t\t`seed_2` AS 'seedTwo',\n" +
            "\t\t`seed_2_date` AS 'seedTwoDate',\n" +
            "\t\t`seed_3` AS 'seedThree',\n" +
            "\t\t`seed_3_date` AS 'seedThreeDate'\n" +
            "\t\tFROM medal WHERE pair_id=#{param1}")
     Info info(Integer pairId);
}

package cn.fdongl.meme.controller;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Mapper
public interface Maps {

    @Select("SELECT now();")
    Timestamp now();

    @Select("SELECT now();")
    Time no();

    @Select("SELECT now();")
    Date d();
}

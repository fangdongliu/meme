package cn.fdongl.meme.task.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.task.entity.Task;
import cn.fdongl.meme.task.entity.TaskCreateA;
import cn.fdongl.meme.task.entity.TaskCreateB;
import cn.fdongl.meme.task.mapper.TaskMapper;
import cn.fdongl.meme.tool.ErrorDefiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskMapper taskMapper;

    @PostMapping("list")
    public Object list(
            HttpServletRequest request,
            Integer pairId
    ) throws AuthenticationException {
        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        if(pairId == null){
            pairId = loginStatus.getPairId();
        }

        return taskMapper.list(pairId);
    }

    @PostMapping("info")
    public Object info(
            HttpServletRequest request,
            Integer taskId
    ) throws Exception {
        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        if(taskId == null){
            if(loginStatus.getPairId()==null){
                throw new Exception();
            }
            try {
                return taskMapper.info2(loginStatus.getPairId());
            }
          catch (Exception e){
                e.printStackTrace();
          }
        }

        return taskMapper.info(taskId);
    }



    @PostMapping("end")
    public Object end(
            HttpServletRequest request
    ) throws Exception {

        LoginStatus loginStatus =LoginStatus.fromRequestAndCheckPair(request);

        try {
            Integer n = taskMapper.end(loginStatus.getPairId());
            if(n<=0){
                throw new Exception();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    @PostMapping("createA")
    public Object createA(
            HttpServletRequest request,
            @Valid TaskCreateA param
            )throws Exception{

        LoginStatus loginStatus = LoginStatus.fromRequestAndCheckPair(request);
       // LoginStatus loginStatus = new LoginStatus(6,15,"");
        param.setAUserId(loginStatus.getUserId());

        Integer taskId = taskMapper.getCurrentTaskId(loginStatus.getPairId());

        if(taskId!=null){
            throw new Exception();
        }

        Integer n = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        Date d = format.parse(param.getStartDate());

        Date now = Calendar.getInstance().getTime();

        if(d.getTime()<=now.getTime()){
            throw new Exception("开始日期需大于今天");
        }

        d.setTime(d.getTime()+param.getWeekCount()*7*24*60*60*1000);

        param.setEndDate(format.format(d));

        String s = param.getRepeatWeek();

        for(int i=0;i<7;i++){
            char c = s.charAt(i);
            if(c=='1'){
                n++;
            }
            else if(c!='0'){
                throw new Exception();
            }
        }

        param.setHp((param.getWeekCount()*n)/20);

        param.setPairId(loginStatus.getPairId());

        try {
            n = taskMapper.createA(param);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(n<=0){
            throw new Exception();
        }

        return null;

    }

    @PostMapping("createB")
    public Object createB(
            HttpServletRequest request,
            @Valid TaskCreateB param
            )throws Exception{

        LoginStatus loginStatus = LoginStatus.fromRequestAndCheckPair(request);

        Integer t = 0;

        String s = param.getRepeatWeek();

        for(int i=0;i<7;i++){
            char c = s.charAt(i);
            if(c=='1'){
                t++;
            }
            else if(c!='0'){
                throw new Exception();
            }
        }

        param.setHp(t);

        param.setPairId(loginStatus.getPairId());

        param.setUserId(loginStatus.getUserId());
        Integer n = null;
        n = taskMapper.createB(param);


        if(n<=0){
            throw new Exception();
        }

        return null;

    }


}
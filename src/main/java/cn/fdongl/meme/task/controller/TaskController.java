package cn.fdongl.meme.task.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.pair.mapper.PairMapper;
import cn.fdongl.meme.seed.mapper.SeedMapper;
import cn.fdongl.meme.task.entity.Task;
import cn.fdongl.meme.task.entity.TaskCreateA;
import cn.fdongl.meme.task.entity.TaskCreateB;
import cn.fdongl.meme.task.mapper.TaskMapper;
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

    @Autowired
    PairMapper pairMapper;

    @PostMapping("list")
    public Object list(
        LoginStatus loginStatus,
        Integer pairId
    )  {

        if(pairId == null){
            pairId = loginStatus.getPairId();
        }

        return taskMapper.list(pairId);
    }

    @PostMapping("info")
    public Object info(
            LoginStatus loginStatus,
            Integer taskId
    ) throws Exception {
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
            LoginStatus loginStatus
    ) throws Exception {

        loginStatus.checkPair();

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
            LoginStatus loginStatus,
            @Valid TaskCreateA param
            )throws Exception{
        loginStatus.checkPair();
        param.setAUserId(loginStatus.getUserId());

        Integer taskId = taskMapper.getCurrentTaskId(loginStatus.getPairId());

        if(taskId!=null){
            throw new Exception();
        }

        Integer n = 0,m=0;

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
                throw new Exception("星期中出现了非01字符");
            }
        }

        param.setHp((param.getWeekCount()*n)/20);

        param.setPairId(loginStatus.getPairId());

        if(param.getRewardType()==0){

        }
        else if(param.getRewardType() == 1 ) {
            int level = getLevel(pairMapper.info(param.getPairId()).getAPunchCount() + pairMapper.info(param.getPairId()).getBPunchCount());
            if (Integer.parseInt(param.getRewardParam()) > level*3) throw new Exception("未解锁种子类型");
            taskMapper.createSeedA(param,n);
        }

        n = taskMapper.createA(param);

        return null;

    }

    @PostMapping("createB")
    public Object createB(
            LoginStatus loginStatus,
            @Valid TaskCreateB param
            )throws Exception{

        loginStatus.checkPair();
//        LoginStatus loginStatus = new LoginStatus(200,11165,"");

        Integer t = 0;

        String s = param.getRepeatWeek();

        for(int i=0;i<7;i++){
            char c = s.charAt(i);
            if(c=='1'){
                t++;
            }
            else if(c!='0'){
                throw new Exception("星期中出现了非01字符");
            }
        }

        param.setHp(t);

        param.setPairId(loginStatus.getPairId());

        param.setUserId(loginStatus.getUserId());
        Integer n = null;
        n = taskMapper.createB(param);


        if(n<=0){
            throw new Exception("创建B失败");
        }

        try {
            taskMapper.createSeedB(param, t, taskMapper.getCurrentTaskId(param.getPairId()));
        }catch (Exception e){

        }

        return null;

    }

    public int getLevel(int cnt){
        if (cnt<30) return  1;
        else if(cnt<50) return  2;
        else if(cnt<100) return  3;
        else return 4;
    }

}

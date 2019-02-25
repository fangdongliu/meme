package cn.fdongl.meme.punch.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.punch.entity.Image;
import cn.fdongl.meme.punch.entity.Punch;
import cn.fdongl.meme.punch.entity.PunchInput;
import cn.fdongl.meme.punch.entity.PunchStatus;
import cn.fdongl.meme.punch.mapper.PunchMapper;
import cn.fdongl.meme.task.entity.CreateFile;
import cn.fdongl.meme.task.entity.Task;
import cn.fdongl.meme.task.mapper.TaskMapper;
import cn.fdongl.meme.tool.ErrorDefiner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/punch")
@Log4j2
public class PunchController {

    @Autowired
    PunchMapper punchMapper;

    @Autowired
    TaskMapper taskMapper;

    @PostMapping("list")
    public Object list(
            Integer taskId,
            HttpServletRequest request) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        return punchMapper.list(taskId);

    }
    LoginStatus test(){
        return new LoginStatus(6,15,"");
    }
    @PostMapping("/status")
    public Object status(
            HttpServletRequest request
    ) throws Exception {
        LoginStatus loginStatus = LoginStatus.fromRequestAndCheckPair(request);

        Calendar calendar = Calendar.getInstance();

        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK)+5)%7;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        Task task = taskMapper.info2(loginStatus.getPairId());

        if(task==null){
            throw new Exception();
        }

        PunchStatus punchStatus = new PunchStatus();

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        String currentDateS = format.format(calendar.getTime());

        Date lastDate = calendar.getTime();

        lastDate.setTime(lastDate.getTime()-24*60*60*1000);

        String lastDateS = format.format(lastDate);

        Date currentDate = calendar.getTime();

        punchStatus.setAStatus(0);
        punchStatus.setBStatus(0);

        punchStatus.setAUserId(task.getAUserId());

        if(currentDate.getTime()>=task.getStartDate().getTime()) {

            if(lastDate.getTime()>task.getStartDate().getTime()){
                if(task.getARepeatWeek().charAt(dayOfWeek)=='1'){
                    Integer pId = punchMapper.status(task.getAUserId(),lastDateS,task.getTaskId());
                    if(pId!=null){
                        punchStatus.setAStatus(1);
                    }
                    else{
                        punchStatus.setAStatus(2);
                    }
                }
                if(task.getBRepeatWeek().charAt(dayOfWeek)=='1'){
                    Integer pId = punchMapper.status(task.getBUserId(),lastDateS,task.getTaskId());
                    if(pId!=null){
                        punchStatus.setBStatus(1);
                    }
                    else{
                        punchStatus.setBStatus(2);
                    }
                }
            }

            if(task.getARepeatWeek().charAt(dayOfWeek)=='1'){

                Integer pid = punchMapper.status(task.getAUserId(),currentDateS,task.getTaskId());

                if(pid !=null){
                    punchStatus.setAStatus(punchStatus.getAStatus()+4);
                }
                else if(task.getAStartTime()<hour){
                    punchStatus.setAStatus(punchStatus.getAStatus()+8);
                }
                else{
                    punchStatus.setAStatus(punchStatus.getAStatus()+12);
                }
            }

            if(task.getBRepeatWeek().charAt(dayOfWeek)=='1'){

                Integer pid = punchMapper.status(task.getBUserId(),currentDateS,task.getTaskId());

                if(pid !=null){
                    punchStatus.setBStatus(punchStatus.getBStatus()+4);
                }
                else if(task.getBStartTime()<hour){
                    punchStatus.setBStatus(punchStatus.getBStatus()+8);
                }
                else{
                    punchStatus.setBStatus(punchStatus.getBStatus()+12);
                }
            }
        }

        return punchStatus;
    }

    int getGrowupValue(int punchCount){

        int v = 0;
        do{
          v++;
          punchCount-=v*10;
        } while(punchCount>=0);

        return v;
    }

    @PostMapping("punch")
    public Object punch(
            @RequestParam("file")MultipartFile file,
            @RequestParam("description") String description,

            HttpServletRequest request
            ) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequestAndCheckPair(request);

        Calendar calendar = Calendar.getInstance();

        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK)+5)%7;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);


        Task task = taskMapper.info2(loginStatus.getPairId());


        if(task==null){
            throw new Exception();
        }

        if(task.getStatus()>0){
            throw new Exception();
        }

        Integer startTime=null;
        String repeatWeek = null;

        if(task.getAUserId() == loginStatus.getUserId()){
            startTime = task.getAStartTime();
            repeatWeek = task.getARepeatWeek();
        }
        else{
            startTime = task.getBStartTime();
            repeatWeek = task.getBRepeatWeek();
        }

        if(startTime!=hour){
            throw new Exception();
        }

        if(repeatWeek.charAt(dayOfWeek)!='1'){
            throw new Exception();
        }

        String suffix;
        try {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        }
        catch (Exception e){
            suffix="";
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "");

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("C:\\memeFiles\\punch\\"+filename)));
        out.write(file.getBytes());
        out.flush();
        out.close();
        CreateFile createFile = new CreateFile(filename,suffix,loginStatus.getUserId());
        punchMapper.createFile(createFile);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        Date date = calendar.getTime();



        if(task.getAUserId()==loginStatus.getUserId()){
            Integer pCount = punchMapper.getAPunchCount(loginStatus.getPairId());
            Integer growupValue = getGrowupValue(pCount);
            PunchInput punch = new PunchInput(loginStatus.getPairId(),growupValue,task.getTaskId(),filename,description,format.format(date),loginStatus.getUserId());
            Integer n = punchMapper.punchA(punch);
            if(n<=0){
                throw new Exception();
            }
        }
        else{
            Integer pCount = punchMapper.getBPunchCount(loginStatus.getPairId());
            Integer growupValue = getGrowupValue(pCount);
            PunchInput punch = new PunchInput(loginStatus.getPairId(),growupValue,task.getTaskId(),filename,description,format.format(date),loginStatus.getUserId());
            Integer n = punchMapper.punchB(punch);
            if(n<=0){
                throw new Exception();
            }
        }

        return null;
    }

    @PostMapping("/supply")
    public Map supply(
            @RequestParam("file")MultipartFile file,
            @RequestParam("description") String description,
            HttpServletRequest request
    ) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequestAndCheckPair(request);

        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();
        date.setTime(date.getTime()-24*60*60*1000);

        int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK)+5)%7;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        Task task = taskMapper.info2(loginStatus.getPairId());

        if(task.getStatus()>0){
            throw new Exception();
        }

        if(task==null){
            throw new Exception();
        }

        Integer startTime=null;
        String repeatWeek = null;

        if(task.getAUserId() == loginStatus.getUserId()){
            if(task.getAHp()<=task.getARCount()){
                throw new Exception();
            }
            startTime = task.getAStartTime();
            repeatWeek = task.getARepeatWeek();
        }
        else{
            if(task.getBHp()<=task.getBRCount()){
                throw new Exception();
            }
            startTime = task.getBStartTime();
            repeatWeek = task.getBRepeatWeek();
        }

        if(startTime!=hour){
            throw new Exception();
        }

        if(repeatWeek.charAt(dayOfWeek)!='1'){
            throw new Exception();
        }

        String suffix;
        try {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
        }
        catch (Exception e){
            suffix="";
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "");

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("C:\\memeFiles\\punch\\"+filename)));
        out.write(file.getBytes());
        out.flush();
        out.close();
        CreateFile createFile = new CreateFile(filename,suffix,loginStatus.getUserId());
        punchMapper.createFile(createFile);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");



        if(task.getAUserId()==loginStatus.getUserId()){
            Integer pCount = punchMapper.getAPunchCount(loginStatus.getPairId());
            Integer growupValue = getGrowupValue(pCount)/2;
            PunchInput punch = new PunchInput(loginStatus.getPairId(),growupValue,task.getTaskId(),filename,description,format.format(date),loginStatus.getUserId());
            Integer n = punchMapper.supplyA(punch);
            if(n<=0){
                throw new Exception();
            }
        }
        else{
            Integer pCount = punchMapper.getBPunchCount(loginStatus.getPairId());
            Integer growupValue = getGrowupValue(pCount)/2;
            PunchInput punch = new PunchInput(loginStatus.getPairId(),growupValue,task.getTaskId(),filename,description,format.format(date),loginStatus.getUserId());
            Integer n = punchMapper.supplyB(punch);
            if(n<=0){
                throw new Exception();
            }
        }

        return null;
    }

    @RequestMapping(value = "image",produces = MediaType.ALL_VALUE)
    public Object getImage(
            @RequestParam("filename")String filename,
            HttpServletResponse response) throws Exception {

        //设置文件路径
        File file = new File("C:\\memeFiles\\punch\\"+filename);
        if (file.exists()) {

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            while(fis.read(buffer)!=-1){
                os.write(buffer);
            }
            fis.close();
            os.flush();
            os.close();
        }

        return null;

    }

}

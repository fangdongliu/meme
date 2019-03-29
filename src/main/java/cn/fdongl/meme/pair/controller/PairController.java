package cn.fdongl.meme.pair.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.pair.entity.UserPair;
import cn.fdongl.meme.pair.mapper.PairMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pair")
public class PairController {

    @Autowired
    PairMapper pairMapper;



    @PostMapping({"","info"})
    public Object info(HttpServletRequest request,
                    Integer pairId,
                       LoginStatus loginStatus) throws Exception {

        if(pairId == null){
            if(loginStatus.getPairId()==null){
                throw new Exception();
            }
            else{
                pairId = loginStatus.getPairId();
            }
        }

        UserPair userPair = pairMapper.info(pairId);
        return userPair;
    }



    @PostMapping("list")
    public Object list(
            LoginStatus loginStatus
    ) throws AuthenticationException {

        return pairMapper.list(loginStatus.getUserId());

    }

    @PostMapping("start")
    public Object start(
            LoginStatus loginStatus
    ) throws Exception{
        Integer pairId = pairMapper.getUserPair(loginStatus.getUserId());
        if(pairId == null){
            pairId = pairMapper.start(loginStatus.getUserId());
            return pairId;
        }
        else {
            throw new Exception("已配对");
        }

    }

    @PostMapping("accept")
    public Object accept(
            LoginStatus loginStatus,
            @RequestParam("pairId") Integer pairId
    ) throws Exception {

        Integer currentPairId = pairMapper.getUserPair(loginStatus.getUserId());

        if(currentPairId!=null){
            throw new Exception("已配对");
        }

        pairMapper.deletePairs(loginStatus.getUserId());


        Integer n = pairMapper.accept(pairId,loginStatus.getUserId());

        if(n<=0){
            throw new Exception();
        }

        return null;

    }

    @PostMapping("end")
    public Object end(
            LoginStatus loginStatus,
            HttpServletRequest request
    ) throws Exception {

        Integer n = pairMapper.end(loginStatus.getUserId());

        if(n<=0){
            throw new Exception("归档失败");
        }

        return null;

    }

    @PostMapping("delete")
    public void del(
            LoginStatus loginStatus,
            @RequestParam("pairId") Integer pairId
    ) throws Exception {

        Integer n = pairMapper.deleteA(pairId,loginStatus.getUserId());

        if(n<=0){
            n = pairMapper.deleteB(pairId,loginStatus.getUserId());
            if(n<=0){
                throw new Exception("");
            }
        }

    }

}

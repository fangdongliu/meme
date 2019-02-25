package cn.fdongl.meme.pair.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.pair.entity.UserPair;
import cn.fdongl.meme.pair.mapper.PairMapper;
import cn.fdongl.meme.tool.ErrorDefiner;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                    Integer pairId) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

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
            HttpServletRequest request
    ) throws AuthenticationException {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        List list = pairMapper.list(loginStatus.getUserId());

        return list;
    }

    @PostMapping("start")
    public Object start(
            HttpServletRequest request
    ) throws Exception{
        LoginStatus loginStatus = LoginStatus.fromRequest(request);
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
            HttpServletRequest request,
            @RequestParam("pairId") Integer pairId
    ) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

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
            HttpServletRequest request
    ) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        Integer n = pairMapper.end(loginStatus.getUserId());

        if(n<=0){
            throw new Exception("归档失败");
        }

        return null;

    }

    @PostMapping("delete")
    public void del(
            HttpServletRequest request,
            @RequestParam("pairId") Integer pairId
    ) throws Exception {

        LoginStatus loginStatus = LoginStatus.fromRequest(request);

        Integer n = pairMapper.deleteA(pairId,loginStatus.getUserId());

        if(n<=0){
            n = pairMapper.deleteB(pairId,loginStatus.getUserId());
            if(n<=0){
                throw new Exception("");
            }
        }

    }

}

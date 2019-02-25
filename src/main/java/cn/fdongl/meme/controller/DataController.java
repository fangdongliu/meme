package cn.fdongl.meme.controller;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    Maps maps;

    @RequestMapping("/data")
    public Object data(Integer a) throws Exception {

        List<Object> ar = new ArrayList<>();
        ar.add(1);
        ar.add(2);

        if(a==0){
            throw new Exception("");
        }
        else if(a==1){
            throw new AuthenticationException("");
        }
        else {
            ar.add(maps.now().getTime());
            ar.add(maps.now());
            ar.add(maps.no().getTime());
            ar.add(maps.no());
            ar.add(maps.d().getTime());
            ar.add(maps.d());
            return ar;
        }

    }

}

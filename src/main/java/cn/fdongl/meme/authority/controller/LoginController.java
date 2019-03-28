package cn.fdongl.meme.authority.controller;

import cn.fdongl.meme.authority.entity.LoginStatus;
import cn.fdongl.meme.authority.mapper.LoginMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LoginMapper loginMapper;

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/login")
    public Object login(
            @RequestParam("code") String code,
            @RequestParam("nickname") String nickname,
            @RequestParam("avatar") String avatar,
            HttpServletRequest request)throws Exception {

//        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
//                "appid=wxf90a03366fb7d3cc&secret=23a16bbd93c0cc002408cf3bf220ac4f&js_code=" +
//                code + "&grant_type=authorization_code";
          String url= "https://api.weixin.qq.com/sns/jscode2session?" +
                  "appid=wx5dabc0ab51184872&secret=4df873de65b9b3fdaabb614cc79b3fc1&js_code=" +
                  code + "&grant_type=authorization_code";

        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = getRestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        Map m = objectMapper.readValue(response, Map.class);

        String openid = (String) m.get("openid");
        if(openid==null){
            throw new Exception();
        }

        Integer userId = loginMapper.getUser(openid);
        Integer pairId = null;

        if (userId == null) {
            userId = loginMapper.insertUser(openid, avatar, nickname);
            result.put("firstLogin", true);
        } else {
            pairId = loginMapper.getUserPair(userId);
            loginMapper.updateUser(userId,avatar,nickname);
        }

        LoginStatus loginStatus = new LoginStatus(userId, pairId, (String) m.get("session_key"));

        result.put("userId", userId);

        if(pairId!=null){
            result.put("pairId",pairId);
        }

        request.getSession().setAttribute("loginStatus", loginStatus);

        return result;

    }


}

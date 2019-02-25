package cn.fdongl.meme.authority.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Data
@AllArgsConstructor
public class LoginStatus {

    Integer userId;

    Integer pairId;

    String wx_session;

    public void setPairId(HttpServletRequest request,Integer pairId){
        request.getSession().setAttribute("pairId",pairId);
        this.pairId = pairId;
    }

    public static LoginStatus fromRequest(HttpServletRequest request) throws AuthenticationException {

        HttpSession session = request.getSession();

        LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

        if(loginStatus==null){
            throw new AuthenticationException("need authorization");
        }

        return loginStatus;

    }

    public static LoginStatus fromRequestAndCheckPair(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();

        LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

        if(loginStatus==null){
            throw new AuthenticationException("need authorization");
        }

        if(loginStatus.getPairId()==null){
            throw new Exception("未配对");
        }

        return loginStatus;

    }

}

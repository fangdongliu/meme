package cn.fdongl.meme.core;

import cn.fdongl.meme.authority.entity.LoginStatus;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(LoginStatus.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        if (request==null){
            throw new AuthenticationException("用户未登录");
        }
        LoginStatus loginStatus = (LoginStatus) request.getSession().getAttribute("loginStatus");

        //LoginStatus loginStatus = new LoginStatus(9,11212,"");

        if(loginStatus==null){
            throw new AuthenticationException("用户未登录");
        }

        return loginStatus;
    }
}

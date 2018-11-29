package com.z.kwenda.interceptor;

import com.z.kwenda.dao.LoginTicketDAO;
import com.z.kwenda.dao.UserDAO;
import com.z.kwenda.model.HostHolder;
import com.z.kwenda.model.LoginTicket;
import com.z.kwenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDao;

    @Autowired
    HostHolder hostHolder;

    //判断用户身份，并放入hostHolder，保证后台都可以访问
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket=null;
        if(httpServletRequest.getCookies() !=null ){
            for(Cookie cookie:httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(ticket !=null){
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){
                return true;
            }
            User user=userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    //在渲染之前，前端也可以直接访问user信息
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    //结束后清除掉
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}

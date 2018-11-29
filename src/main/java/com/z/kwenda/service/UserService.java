package com.z.kwenda.service;


import com.z.kwenda.dao.LoginTicketDAO;
import com.z.kwenda.dao.UserDAO;
import com.z.kwenda.model.LoginTicket;
import com.z.kwenda.model.User;
import com.z.kwenda.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.String;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date date=new Date();
        date.setTime(3600*24*100+date.getTime());
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public Map<String,String> login(String username, String password){
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public Map<String,String> regist(String username, String password){
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已存在");
            return map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl("../images/img/git command.png");
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}

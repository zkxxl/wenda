package com.z.kwenda.controller;

import com.z.kwenda.dao.LoginTicketDAO;
import com.z.kwenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger=LoggerFactory.getLogger(HomeController.class);

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reglogin"},method = {RequestMethod.GET})
    public String reg(Model model,@RequestParam(value="next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    //注册按钮
    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="next") String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){
        try {
            Map<String,String> map=userService.regist(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常："+e.getMessage());
        }
        return "index";
    }
    //登录按钮
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="next",required = false) String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){
        try {
            Map<String,String> map=userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常："+e.getMessage());
            return "login";
        }
    }

    //退出按钮
    @RequestMapping(path={"/logout"},method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket ){
        userService.logout(ticket);
        return "redirect:/";
    }
}

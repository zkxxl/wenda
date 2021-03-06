package com.z.kwenda.controller;


import com.z.kwenda.model.User;
import com.z.kwenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {

    //    WendaService wendaService=new WendaService();

    //注入，就无需初始化了
    @Autowired
    WendaService wendaService;

    //给一个字符串,求全排列
    @RequestMapping("/permutations")
    @ResponseBody
    public int permutations(@RequestParam(value="str") String str){
        ArrayList<String> al=new ArrayList<>();
        char[] chs=str.toCharArray();
        fullPermutation(al,chs,0);
        for(String s:al){
            System.out.println(s);
        }
        return al.size();
    }
    public void fullPermutation(ArrayList<String> al,char[] chs,int start){
        if(start==chs.length-1){
            al.add(String.valueOf(chs));
        }else{
            for(int i=start;i<chs.length;i++){
                char temp=chs[start];
                chs[start]=chs[i];
                chs[i]=temp;

                fullPermutation(al,chs,start+1);
            }
        }
    }

    @RequestMapping(path={"/s","/indexs"},method={RequestMethod.GET})
    @ResponseBody
    public String Index(HttpSession httpSession){
        return wendaService.getMessage(2)+"Hello NewCoder "+httpSession.getAttribute("meg");
    }

    @RequestMapping("/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "zk",required = false) String key){
//        return "hello world" + userId;
        return String.format(" Profile Page of %s/%d , type: %d  key: %s", groupId, userId,type,key);
    }

    //模板
    @RequestMapping(path={"/vm"},method={RequestMethod.GET})
//    @RequestMapping("/vm")
    public String template(Model model){
        model.addAttribute("value1","zkxxl");
        List list= Arrays.asList(new String[] {"blue","red","yellow"});
        model.addAttribute("colors",list);

        Map map=new HashMap<Integer,Integer>();
        for(int i=0;i<4;i++){
            map.put(i,i*i);
        }
        model.addAttribute("map",map);
        model.addAttribute("user",new User("xxl"));
        return "home";
    }

    @RequestMapping(path={"/request"},method={RequestMethod.GET})
    @ResponseBody
    public String template(Model model, HttpServletRequest request, HttpServletResponse response,
                           HttpSession session,@CookieValue("JSESSIONID") String sessionId){
        StringBuffer sb=new StringBuffer();
        sb.append("CookieValue:"+sessionId+"<br>");
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                sb.append("Cookie:"+cookie.getName()+" value:"+cookie.getValue());
            }
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append( request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");
        sb.append(request.getCookies()+"<br>");
        sb.append(request.getHeader("cookie")+"<br>");

        response.addHeader("nowcoderId","hello");
        response.addCookie(new Cookie("username","nowcoder"));
//        response.sendRedirect("/");
//        response.getOutputStream().write();
        return sb.toString();
    }
    @RequestMapping(path = {"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,HttpSession httpSession){
        httpSession.setAttribute("meg","jump from redirect");

        return "redirect:/";//默认是302的临时跳转
    }

    @RequestMapping(path = {"/redirects/{code}"},method = {RequestMethod.GET})
    public RedirectView redirects(@PathVariable("code") int code, HttpSession httpSession){
        httpSession.setAttribute("meg","jump from redirect");
        RedirectView red=new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);//301跳转
        }
        return red;
    }

    //异常
    @RequestMapping(path={"/admin"},method={RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "Hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    //统一的异常处理
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "ERROR："+e.getMessage();
    }


}

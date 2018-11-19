package com.z.kwenda.controller;

import com.z.kwenda.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SettingController {
//    WendaService wendaService=new WendaService();

    //注入，就无需初始化了
    @Autowired
    WendaService wendaService;

    @RequestMapping(path={"/setting"},method={RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession httpSession){
        return "Setting ok"+ wendaService.getMessage(1);
    }

}

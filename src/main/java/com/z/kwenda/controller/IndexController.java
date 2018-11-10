package com.z.kwenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @RequestMapping(path={"/","/index"},method={RequestMethod.GET})
    @ResponseBody
    public String Index(){

        return "Hello NewCoder";
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
//    @RequestMapping(path={"/vm"},method={RequestMethod.GET})
    @RequestMapping("/vm")
    public String template(){

        return "home";
    }

}

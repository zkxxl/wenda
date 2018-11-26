package com.z.kwenda.controller;

import com.z.kwenda.model.Question;
import com.z.kwenda.model.ViewObject;
import com.z.kwenda.service.QuestionService;
import com.z.kwenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger looger=LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questionList = questionService.getLastestQuestion(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(Question question : questionList){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }

    @RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        List<ViewObject> vos=getQuestions(0,0,10);
        model.addAttribute("vos",vos);
        return "index";
    }
}

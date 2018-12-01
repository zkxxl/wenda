package com.z.kwenda.service;

import com.z.kwenda.dao.QuestionDAO;
import com.z.kwenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLastestQuestion(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId,offset,limit);
    }
}

package com.z.kwenda.service;

import com.z.kwenda.dao.UserDAO;
import com.z.kwenda.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}

package com.z.kwenda.service;

import org.springframework.stereotype.Service;

@Service
public class WendaService {
    public String getMessage(int userId){
        return "hello Message:"+String.valueOf(userId);
    }
}

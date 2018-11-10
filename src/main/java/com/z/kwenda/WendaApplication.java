package com.z.kwenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class WendaApplication {
    /**
     * 打印内容
     * @param index 索引
     * @param object 对象
     */
    public static void print(int index,Object object){
        System.out.println(String.format("{%d}, %s",index,object.toString()));
    }
    public static void main(String[] args) {
        SpringApplication.run(WendaApplication.class, args);
//        print(1,"Hello World");
    }

}

package com.neuedu.web;

import com.neuedu.utils.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FTPController {

    @Autowired
    FTPService ftpService;

    @RequestMapping("/ftp")
    public String uploadFtp(){
        List<File> list = new ArrayList<>();
        File file = new File("D:\\ftpfile\\logo.png");
        list.add(file);
        boolean result = ftpService.uploadFile("qrcode",list);
        return "finish"+result;
    }
}

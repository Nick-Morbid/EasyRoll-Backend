package com.system.roll.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @PostMapping(value = "/upload")
    public void testUpload(@RequestParam(value = "file")MultipartFile file){
        System.out.println(file.getOriginalFilename());
    }
}

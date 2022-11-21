package com.system.roll.controller.common;

import com.system.roll.service.common.MajorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/major")
public class MajorController {

    @Resource
    private MajorService majorService;

    @GetMapping(value = "/query")
    public List<String> getMajorNameList(@RequestParam(value = "keyword",required = false,defaultValue = "")String keyword){
        return majorService.getMajorNameList(keyword);
    }
}

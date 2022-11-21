package com.system.roll.controller.common;

import com.system.roll.service.common.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/department")
public class DepartmentController {

    @Resource(name = "DepartmentService")
    private DepartmentService departmentService;

    @GetMapping("/query")
    public List<String> getDepartmentNameList(@RequestParam(value = "keyword",required = false,defaultValue = "")String keyword){
        return departmentService.getDepartmentNameList(keyword);
    }
}

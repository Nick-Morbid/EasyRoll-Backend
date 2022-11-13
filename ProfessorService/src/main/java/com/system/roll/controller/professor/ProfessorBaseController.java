package com.system.roll.controller.professor;

import com.system.roll.service.professor.ProfessorBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professor/base")
public class ProfessorBaseController {

    @Autowired
    private ProfessorBaseService professorBaseService;


}

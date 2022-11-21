package com.system.roll.service.auth;

import com.system.roll.entity.vo.InfoVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public interface AuthService {
    /**
     * 学生端授权登录
     * */
    InfoVo studentLogin(String code);

    /**
     * 督导队员端授权登录
     * */
    SupervisorVo supervisorLogin(String code);
    /**
     * 微信小程序授权网页端登录（教师/辅导员登录）
     * */
    ProfessorVo professorLogin(String socketId, String code);

    /**
     * 生成QRCode码，并作为文件流发送给前端
     * @return 随机生成的socketId（前端用该socketId去建立长连接）
     * */
    String generateQRCode(String socketId);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class LoginFormDto{
        private String  socketId;
        private String code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class RegisterFormDto{
        private String id;
        private String name;
        private String departmentName;
        private Integer role;//0：学生，1：督导队员，2：教师，3：辅导员
    }
}

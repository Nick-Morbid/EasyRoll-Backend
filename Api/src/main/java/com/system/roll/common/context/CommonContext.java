package com.system.roll.common.context;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component(value = "CommonContext")
public class CommonContext {
    /**
     * 用户的工作路径
     * */
    private String workDir = System.getProperty("user.dir");

    /**
     * 获取资源存储目录
     * */
    public String getResourceDir(){
        return this.workDir+"/resource/";
    }

    /**
     * 获取临时文件存放目录
     * */
    public String getTempDir(){
        return getResourceDir()+"/temp/";
    }
}

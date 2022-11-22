package com.system.roll.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.utils.IdUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class OssHandler {
    @Resource(name = "OSSClient")
    private OSS ossClient;
    @Resource
    private OssConfig ossConfig;
    @Resource
    private IdUtil idUtil;

    public OssResource getFile(String fileName){
        String bucketName = ossConfig.getBucketName();
        OSSObject ossObject = ossClient.getObject(bucketName, fileName);
        return new OssResource(ossObject);
    }

    public void postFile(InputStream inputStream,String filename){
        String bucketName = ossConfig.getBucketName();
        ossClient.putObject(bucketName,filename,inputStream);
    }

    public String postFile(MultipartFile file,String category){
        return postFile(file,category,null);
    }

    public String postFile(MultipartFile file,String category,String filename){
        try {
            if (category==null) category = "";
            else category = category+"/";
            if (filename==null) filename = idUtil.getId();
            String suffix = getSuffix(file.getOriginalFilename());
            filename = category+filename+"."+suffix;
            postFile(file.getInputStream(),filename);
            return filename;
        } catch (IOException e) {
            throw new ServiceException(ResultCode.UPLOAD_FAILURE);
        }
    }

    public String postFile(File file, String category){
        return postFile(file,category,null);
    }

    public String postFile(File file,String category,String filename){
        try {
            if (category==null) category = "";
            else category = category+"/";
            if (filename==null) filename = idUtil.getId();
            String suffix = getSuffix(file.getName());
            filename = category+filename+"."+suffix;
            postFile(new FileInputStream(file),filename);
            return filename;
        } catch (IOException e) {
            throw new ServiceException(ResultCode.UPLOAD_FAILURE);
        }
    }

    public String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    public void deleteFile(String fileName) {
        //检查一下是否为默认的文件
        if (isDefault(fileName)) return;
        String bucketName = ossConfig.getBucketName();
        ossClient.deleteObject(bucketName,fileName);
    }

    public boolean isDefault(String fineName){
        String[] split = fineName.split("/");
        String name = split[split.length-1];
        String[] split1 = name.split("\\.");
        return split1[0].equals("default");
    }

}

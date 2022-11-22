package com.system.roll.oss;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@PropertySource("classpath:properties/oss.properties")
@Configuration
@Slf4j
public class OssConfig {
    @Value(value = "${oss.endpoint}")
    private String endpoint;
    @Value(value = "${oss.accessKeyId}")
    private String accessKeyId;
    @Value(value = "${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value(value = "${oss.bucketName}")
    private String bucketName;

    @Bean(name = "OSSClient")
    public OSS OSSClient(){
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(10000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启二级域名的访问方式，默认不开启。
        conf.setSLDEnabled(false);
        // 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
        conf.setProtocol(Protocol.HTTP);
        // 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
        conf.setUserAgent("aliyun-sdk-java");
        // 设置代理服务器端口。
//        conf.setProxyHost("<yourProxyHost>");
        // 设置代理服务器验证的用户名。
//        conf.setProxyUsername("<yourProxyUserName>");
        // 设置代理服务器验证的密码。
//        conf.setProxyPassword("<yourProxyPassword>");
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(true);
        String osName = System.getProperties().getProperty("os.name");
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
        init(ossClient);
        return ossClient;
    }

    /**
     * 一些初始化操作
     * */
    public void init(OSS ossClient){
        //设置请求者付费模式
//        Payer payer = Payer.Requester;
//        ossClient.setBucketRequestPayment(bucketName, payer);

        try {
            ossClient.createBucket(bucketName);
            log.info("[oss]:bucket创建完毕,bucketName:"+bucketName);
        }catch (OSSException e){
            log.info("[oss]:bucket已经存在,bucketName:"+bucketName);
        }
    }
}

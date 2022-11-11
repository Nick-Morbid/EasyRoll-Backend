package com.system.roll;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EasyRollStarter {
    public static void main(String[] args) {
        SpringApplication.run(EasyRollStarter.class,args);
    }
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();

                securityConstraint.setUserConstraint("CONFIDENTIAL"); SecurityCollection collection = new SecurityCollection(); collection.addPattern("/*");

                securityConstraint.addCollection(collection);

                context.addConstraint(securityConstraint); } };

//        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;

    }

//    @Bean
//    public Connector redirectConnector() {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setScheme("http");
//
//        //监听http的8091端口
//        connector.setPort(8091);
//
//        connector.setSecure(false);
//
//        //重定向到https的8090端口
//        connector.setRedirectPort(8090);
//
//        return connector;
//    }
}

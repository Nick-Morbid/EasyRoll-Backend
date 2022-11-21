package com.system.roll.entity.properites;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component(value = "RabbitProperties")
@PropertySource("classpath:properties/rabbit.properties")
public class RabbitProperties {
    @Value("${rabbit.wsExchange}")
    private String wsExchange;
    @Value("${rabbit.rollDataSource}")
    private String rollDataSource;
    @Value("${rabbit.rollDataExchange}")
    private String rollDataExchange;
    @Value("${rabbit.rollDataQueuePrefix}")
    private String rollDataQueuePrefix;

}

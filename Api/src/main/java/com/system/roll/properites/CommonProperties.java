package com.system.roll.properites;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:properties/common.properties")
public class CommonProperties {
    @Value(value = "${term.firstWeek}")
    private String firstWeek;
    @Value(value = "${term.totalWeek}")
    private Integer totalWeek;
}

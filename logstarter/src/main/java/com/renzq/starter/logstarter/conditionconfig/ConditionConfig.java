package com.renzq.starter.logstarter.conditionconfig;

import com.renzq.configure.annotation.imp.EnablePerfLog;
import com.renzq.configure.configbean.IPerfLog;
import com.renzq.configure.configbean.SimplePerfLog;
import com.renzq.starter.logstarter.bean.ConditionBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * ConditionConfig is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 15:00
 * Description:
 */
@Configuration
//@ConditionalOnProperty(name="server.host", value="8081")
//@ConditionalOnNotWebApplication
@ConditionalOnClass(IPerfLog.class)
public class ConditionConfig {

    @Bean
    ConditionBean conditionBean(){
        return new ConditionBean();
    }
}
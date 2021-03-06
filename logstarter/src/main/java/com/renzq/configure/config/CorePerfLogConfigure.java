package com.renzq.configure.config;

import com.renzq.configure.configbean.CorePerfLog;
import com.renzq.configure.configbean.IPerfLog;
import com.renzq.configure.configbean.SimplePerfLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CorePerfLogConfigure is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 14:34
 * Description:
 */
@Configuration
public class CorePerfLogConfigure {
    @Bean
    IPerfLog perfLogBean(){
        return new CorePerfLog();
    }
}
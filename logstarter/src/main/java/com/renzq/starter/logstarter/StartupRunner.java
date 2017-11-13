package com.renzq.starter.logstarter;

import com.renzq.configure.configbean.IPerfLog;
import com.renzq.starter.logstarter.bean.ConditionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * StartupRunner is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 11:12
 * Description:
 */
public class StartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    @Autowired
    private IPerfLog perfLogBean;

    @Autowired
    private ConditionBean conditionBean;

    @Override
    public void run(String... strings) throws Exception {
        log.info("......................");
        this.perfLogBean.echo();
        this.conditionBean.echo();
    }
}
package com.renzq.app.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReqSignTaskListener is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 12:28
 * Description:
 */
public class CreateReqSignTaskListener implements TaskListener {

    private static final transient Logger log = LoggerFactory.getLogger(CreateReqSignTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("CreateReqSignTaskListener is called....");
    }
}
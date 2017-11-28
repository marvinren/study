package com.renzq.app.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompleteReqSignTaskListener is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 12:30
 * Description:
 */
public class CompleteReqSignTaskListener implements TaskListener{
    private static final transient Logger log = LoggerFactory.getLogger(CompleteReqSignTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("CompleteReqSignTaskListener is called....");
    }
}
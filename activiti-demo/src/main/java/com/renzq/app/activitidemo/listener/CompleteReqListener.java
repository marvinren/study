package com.renzq.app.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompleteReqListener is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 10:35
 * Description:
 */
public class CompleteReqListener implements TaskListener {

    private static final transient Logger log = LoggerFactory.getLogger(CompleteReqListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("完成需求[completeReq]事件....");
    }
}
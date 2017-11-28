package com.renzq.app.activitidemo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReqCreateListener is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 10:24
 * Description:
 */
public class ReqCreateListener implements TaskListener {

    private static final transient Logger log = LoggerFactory.getLogger(ReqCreateListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("创建需求[ReqCreate]事件...");
    }
}
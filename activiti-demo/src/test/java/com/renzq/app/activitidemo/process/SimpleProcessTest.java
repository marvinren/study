package com.renzq.app.activitidemo.process;

import com.renzq.app.activitidemo.process.utils.ProcessUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * SimpleProcessTest is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 14:35
 * Description:
 */
public class SimpleProcessTest {
    private final static transient Logger log = LoggerFactory.getLogger(SimpleProcessTest.class);
    private final static transient String PROCESS_NAME = "processes/demo/SimpleProcess.bpmn";

    private static ProcessEngine processEngine;
    private static ProcessUtils processUtils;
    private static Deployment deployment;



    @BeforeClass
    public static void setUp(){
        processUtils = new ProcessUtils();
        processEngine = processUtils.getProcessEngine();
        deployment = processUtils.deployProcess(PROCESS_NAME);
    }

    @Test
    public void testNormal(){
        ProcessInstance processInstance = processUtils.startProcessSimple(deployment, "simpleProcess");
        processUtils.logInfoAllNotAssignTask();
        processUtils.completeTaskByDefinitionKey("SubmitApply", new HashMap<String, Object>(){
            {
               put("apply_option", "yes");
            }});

        processUtils.completeTaskByDefinitionKey("approve", new HashMap<String, Object>(){
            {
                put("manager_option", "yes");
            }});

        processUtils.logInfoAllNotAssignTask();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        // Re-query the process instance, making sure the latest state is available
        processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
//        log.info("{}, {}", processInstance.isSuspended(), processInstance.isEnded());

        assertThat(processInstance, nullValue());

        processUtils.logInfoAllNotAssignTask();


    }
}
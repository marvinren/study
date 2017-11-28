package com.renzq.app.activitidemo.process;

import com.renzq.app.activitidemo.process.utils.ProcessUtils;
import com.renzq.app.activitidemo.service.WorkFlowAssistService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * JointlySignProcessTest is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/28 0028
 * Time: 13:02
 * Description:
 */
public class JointlySignProcessTest {
    private final static transient Logger log = LoggerFactory.getLogger(JointlySignProcessTest.class);
    private final static transient String PROCESS_NAME = "processes/demo/RejectProcess.bpmn";

    private static ProcessEngine processEngine;
    private static ProcessUtils processUtils;
    private static Deployment deployment;
    private static WorkFlowAssistService workFlowAssistService;



    @BeforeClass
    public static void setUp(){
        processUtils = new ProcessUtils();
        processEngine = processUtils.getProcessEngine();
        deployment = processUtils.deployProcess(PROCESS_NAME);
        workFlowAssistService = new WorkFlowAssistService(
                processEngine.getRepositoryService(),
                processEngine.getRuntimeService(),
                processEngine.getTaskService(),
                processEngine.getFormService(),
                processEngine.getHistoryService()
        );

    }
    @Test
    public void testMutliInstanceReject() throws Exception {
        ProcessInstance rejectProcess = processUtils.startProcessSimple(deployment, "rejectProcess");

        //提交
        processUtils.completeTaskByDefinitionKey("Apply", new HashMap<String, Object>() {{
            put("apply", "virtual");
            put("staffList", Arrays.asList("user1", "user2", "user3"));
        }});

        //会签
        List<Task> appoveTasks = this.processEngine.getTaskService().createTaskQuery().processInstanceId(rejectProcess.getId())
                .taskDefinitionKey("appove").list();

        MatcherAssert.assertThat(appoveTasks.size(), is(3));
        Assert.assertArrayEquals(processUtils.getTaskInProcessInstanceNames(rejectProcess.getId()), new String[]{"领导审批", "领导审批", "领导审批", "获取资源"});

        for(Task appoveTask:appoveTasks){
            processEngine.getTaskService().complete(appoveTask.getId());
        }

        Assert.assertArrayEquals(processUtils.getTaskInProcessInstanceNames(rejectProcess.getId()), new String[]{"获取资源"});

        //获取资源
        processUtils.completeTaskByDefinitionKey("ResourceFetch", null);
        processUtils.completeTaskByDefinitionKey("ResourceCatch", null);
        Assert.assertArrayEquals(processUtils.getTaskInProcessInstanceNames(rejectProcess.getId()), new String[]{"实施完结"});


        processUtils.logInfoAllTaskInProcess(rejectProcess.getId());

    }
}
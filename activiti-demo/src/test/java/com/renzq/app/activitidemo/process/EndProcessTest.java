package com.renzq.app.activitidemo.process;

import com.renzq.app.activitidemo.process.utils.ProcessUtils;
import com.renzq.app.activitidemo.service.WorkFlowAssistService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * EndProcessTest is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/28 0028
 * Time: 13:13
 * Description:
 */
public class EndProcessTest {
    private final static transient Logger log = LoggerFactory.getLogger(EndProcessTest.class);
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

        List<Task> tasks = processUtils.getTaskInProcessInstance(rejectProcess.getId());

        log.info("{}", processEngine.getTaskService().getVariable(tasks.get(0).getId(), "apply"));
        log.info("{}", processEngine.getRuntimeService().getVariable(rejectProcess.getId(), "apply"));

        //取消
        for(Task task: tasks)
            workFlowAssistService.endProcess(task.getId());

        String processId = rejectProcess.getId();
        ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(rejectProcess.getId()).singleResult();

        Assert.assertNull(processInstance);
        processUtils.logInfoHistoryTask(processId);

    }
}
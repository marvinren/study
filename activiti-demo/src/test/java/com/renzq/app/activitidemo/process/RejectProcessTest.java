package com.renzq.app.activitidemo.process;

import com.renzq.app.activitidemo.process.utils.ProcessUtils;
import com.renzq.app.activitidemo.service.WorkFlowAssistService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessElementImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * RejectProcessTest is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 15:06
 * Description:
 */
public class RejectProcessTest {
    private final static transient Logger log = LoggerFactory.getLogger(RejectProcessTest.class);
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
    public void testMutliInstanceReject() throws Exception{
        ProcessInstance rejectProcess = processUtils.startProcessSimple(deployment, "rejectProcess");

        //提交
        processUtils.completeTaskByDefinitionKey("Apply", new HashMap<String, Object>(){{
            put("apply", "virtual");
            put("staffList", Arrays.asList("user1", "user2", "user3"));
        }});



        //在会签节点驳回

        List<Task> appoveTasks = this.processEngine.getTaskService().createTaskQuery().processInstanceId(rejectProcess.getId())
                .taskDefinitionKey("appove").list();
        List<ActivityImpl> backActivity = this.workFlowAssistService.findBackActivity(appoveTasks.get(0).getId());

        processUtils.logInfoAllTaskInProcess(rejectProcess.getId());
        workFlowAssistService.backProcess(appoveTasks.get(0).getId(), backActivity.get(backActivity.size()-1).getId(), null);
        processUtils.logInfoAllTaskInProcess(rejectProcess.getId());

        Assert.assertArrayEquals(processUtils.getTaskInProcessInstanceNames(rejectProcess.getId()),
                new String[]{"获取资源","提交申请"});

        //打印
        processUtils.logInfoAllTaskInProcess(rejectProcess.getId());
        processUtils.logInfoHistoryTask(rejectProcess.getId());
        for(ActivityImpl a:backActivity){
            log.info(a.getId());
        }
        log.info(appoveTasks.get(0).getAssignee());
        log.info(appoveTasks.get(0).getName());

    }


    @Test
    public void testExclusionGatewayReject() throws Exception {
        ProcessInstance rejectProcess = processUtils.startProcessSimple(deployment, "rejectProcess");

        //提交
        processUtils.completeTaskByDefinitionKey("Apply", new HashMap<String, Object>(){{ put("apply", "subject");}});
        Task task = this.processEngine.getTaskService().createTaskQuery().processInstanceId(rejectProcess.getId()).singleResult();
        assertEquals(task.getTaskDefinitionKey(), "Deploy");

        //到达实施完成
        List<ActivityImpl> backActivity = this.workFlowAssistService.findBackActivity(task.getId());
        List<String> backActivityIds = backActivity.stream().map(ActivityImpl::getId).collect(Collectors.toList());
        assertArrayEquals(backActivityIds.toArray(new String[backActivityIds.size()]),
                new String[]{"Apply"});

        //回到提交申请
        Task task1 = processEngine.getTaskService().createTaskQuery().processInstanceId(rejectProcess.getId()).taskDefinitionKey("Deploy").singleResult();
        workFlowAssistService.backProcess(task1.getId(), backActivity.get(backActivity.size()-1).getId(), null);
        MatcherAssert.assertThat(processUtils.getNotAssignTask(), hasItem("提交申请"));
        Assert.assertArrayEquals(processUtils.getTaskInProcessInstanceNames(rejectProcess.getId()), new String[]{"提交申请"});

        //打印
        processUtils.logInfoAllTaskInProcess(rejectProcess.getId());
        processUtils.logInfoHistoryTask(rejectProcess.getId());
        for(ActivityImpl a:backActivity){
            log.info(a.getId());
        }



    }
}
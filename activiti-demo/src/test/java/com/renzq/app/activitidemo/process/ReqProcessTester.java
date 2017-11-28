package com.renzq.app.activitidemo.process;

import com.renzq.app.activitidemo.listener.CompleteReqListener;
import com.renzq.app.activitidemo.listener.CompleteReqSignTaskListener;
import com.renzq.app.activitidemo.listener.CreateReqSignTaskListener;
import com.renzq.app.activitidemo.listener.ReqCreateListener;
import com.renzq.app.activitidemo.process.utils.ProcessUtils;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

/**
 * ReqProcessTester is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 10:07
 * Description:
 */
public class ReqProcessTester {


    private final static transient Logger log = LoggerFactory.getLogger(ReqProcessTester.class);
    private final static transient String PROCESS_NAME = "processes/reqProcess.bpmn20.xml";

    private static ProcessEngine processEngine;
    private static ProcessUtils processUtils;
    private static Deployment deployment;


    @BeforeClass
    public static void setUp(){

        processUtils = new ProcessUtils();
        processEngine = processUtils.getProcessEngine();
        deployment = processUtils.deployProcess(PROCESS_NAME);

    }


    public void startprocess(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        log.info("查找流程定义，名称[{}], ID为:[{}]", processDefinition.getName(), processDefinition.getId());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variable = new HashMap<>();
        variable.put("createReqTaskListener", new ReqCreateListener());
        variable.put("completeReqTaskListener", new CompleteReqListener());
        variable.put("createReqSignTaskListener", new CreateReqSignTaskListener());
        variable.put("completeReqSignTaskListener", new CompleteReqSignTaskListener());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("reqProcess", variable);
        log.info("流程启动，流程实例processInstance的ID为{}, 流程实例的key为{}", processInstance.getProcessDefinitionId(), processInstance.getProcessDefinitionKey());
    }



    /**
     * 直接从集成商测试进入第三方负责人分配
     */
    @Test
    public void testTestToThird(){
        startprocess();
        processUtils.logInfoAllNotAssignTask();
        processUtils.completeTaskByDefinitionKey("ReqAllot", null);
        processUtils.logInfoAllNotAssignTask();
        //需求分析
        processUtils.completeTaskByDefinitionKey("ReqAnalysis", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //需求负责人审核
        processUtils.completeTaskByDefinitionKey("AnalysisVerify", new HashMap<String, Object>(){
            {
                put("verify", "C");
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //开发分配
        processUtils.completeTaskByDefinitionKey("DevAllot", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //需求开发
        processUtils.completeTaskByDefinitionKey("DevReq", new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });
        processUtils.logInfoAllNotAssignTask();

        //集成商测试分配
        processUtils.completeTaskByDefinitionKey("SIAllotTest", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        processUtils.logInfoAllNotAssignTask();

        //集成商测试
        processUtils.completeTaskByDefinitionKey("SITestReq", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        assertThat(processUtils.getNotAssignTask(), hasItems("第三方负责人分配","第三方负责人分配"));

        processUtils.logInfoAllNotAssignTask();


    }


    /**
     * 两条流程都结束测试并行网关
     */
    @Test
    public void testTwoLine(){
        startprocess();
        processUtils.logInfoAllNotAssignTask();
        processUtils.completeTaskByDefinitionKey("ReqAllot", null);
        processUtils.logInfoAllNotAssignTask();
        //需求分析
        processUtils.completeTaskByDefinitionKey("ReqAnalysis", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //需求负责人审核
        processUtils.completeTaskByDefinitionKey("AnalysisVerify", new HashMap<String, Object>(){
            {
                put("verify", "C");
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //开发分配
        processUtils.completeTaskByDefinitionKey("DevAllot", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        processUtils.logInfoAllNotAssignTask();
        //需求开发
        processUtils.completeTaskByDefinitionKey("DevReq", new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });
        processUtils.logInfoAllNotAssignTask();

        //集成商测试分配
        processUtils.completeTaskByDefinitionKey("SIAllotTest", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        processUtils.logInfoAllNotAssignTask();

        //集成商测试
        processUtils.completeTaskByDefinitionKey("SITestReq", new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });
        processUtils.logInfoAllNotAssignTask();
        assertThat(processUtils.getNotAssignTask(), hasItem("第三方需求分配"));
        assertThat(processUtils.getNotAssignTask(), not(hasItem("第三方负责人分配")));


        //第三方需求分配
        processUtils.completeTaskByDefinitionKey("ThirdReqAllot", null);
        processUtils.logInfoAllNotAssignTask();

        //第三方需求分析
        processUtils.completeTaskByDefinitionKey("ThirdReqAys", null);
        processUtils.logInfoAllNotAssignTask();

        //业支第三方分析审核
        processUtils.completeTaskByDefinitionKey("ThirdAysVerify", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });
        processUtils.logInfoAllNotAssignTask();

        assertThat(processUtils.getNotAssignTask(), hasItem("第三方负责人分配"));



    }


    /**
     * 两条一条跳出并行网关，另外一条不并不产生新的工单
     */
    @Test
    public void testOneLineOverFirst(){
        startprocess();

        //需求分配
        processUtils.completeTaskByDefinitionKey("ReqAllot", null);
        //需求分析
        processUtils.completeTaskByDefinitionKey("ReqAnalysis", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });
        //需求负责人审核
        processUtils.completeTaskByDefinitionKey("AnalysisVerify", new HashMap<String, Object>(){
            {
                put("verify", "C");
            }
        });
        //开发分配
        processUtils.completeTaskByDefinitionKey("DevAllot", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        //需求开发
        processUtils.completeTaskByDefinitionKey("DevReq", new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });
        //集成商测试分配
        processUtils.completeTaskByDefinitionKey("SIAllotTest", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        //集成商测试
        processUtils.completeTaskByDefinitionKey("SITestReq", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        //第三方负责人分配
        processUtils.completeTaskByDefinitionKey("ThirdAllot", null);



        //第三方需求分配
        processUtils.completeTaskByDefinitionKey("ThirdReqAllot", null);
        //第三方需求分析
        processUtils.completeTaskByDefinitionKey("ThirdReqAys", null);
        //业支第三方分析审核
        processUtils.completeTaskByDefinitionKey("ThirdAysVerify", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        List<String> notAssignTask = processUtils.getNotAssignTask();
        org.junit.Assert.assertArrayEquals(notAssignTask.toArray(new String[notAssignTask.size()]),
                new String[]{"第三方测试"});


        processUtils.logInfoAllNotAssignTask();




    }


    /**
     * 跳进并行流程里，再通过并行网关，依然可通过
     */
    @Test
    public void testJumpInParreldo(){
        startprocess();

        //需求分配
        processUtils.completeTaskByDefinitionKey("ReqAllot", null);
        //需求分析
        processUtils.completeTaskByDefinitionKey("ReqAnalysis", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });
        //需求负责人审核
        processUtils.completeTaskByDefinitionKey("AnalysisVerify", new HashMap<String, Object>(){
            {
                put("verify", "C");
            }
        });
        //开发分配
        processUtils.completeTaskByDefinitionKey("DevAllot", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        //需求开发
        processUtils.completeTaskByDefinitionKey("DevReq", new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });
        //集成商测试分配
        processUtils.completeTaskByDefinitionKey("SIAllotTest", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });
        //集成商测试
        processUtils.completeTaskByDefinitionKey("SITestReq", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        //第三方负责人分配
        processUtils.completeTaskByDefinitionKey("ThirdAllot", null);



        //第三方需求分配
        processUtils.completeTaskByDefinitionKey("ThirdReqAllot", null);
        //第三方需求分析
        processUtils.completeTaskByDefinitionKey("ThirdReqAys", null);
        //业支第三方分析审核
        processUtils.completeTaskByDefinitionKey("ThirdAysVerify", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        List<String> notAssignTask = processUtils.getNotAssignTask();
        org.junit.Assert.assertArrayEquals(notAssignTask.toArray(new String[notAssignTask.size()]),
                new String[]{"第三方测试"});


        //第三方测试
        processUtils.completeTaskByDefinitionKey("ThirdTest", null);

        //需求负责人审核
        processUtils.completeTaskByDefinitionKey("ReqResVerify",  new HashMap<String, Object>(){
            {
                put("verify", "B");
            }
        });

        //集成商测试分配
        processUtils.completeTaskByDefinitionKey("SIAllotTest", new HashMap<String, Object>(){
            {
                put("staffList", Arrays.asList("admin"));
            }
        });

        //集成商测试
        processUtils.completeTaskByDefinitionKey("SITestReq", new HashMap<String, Object>(){
            {
                put("verify", "A");
            }
        });

        notAssignTask = processUtils.getNotAssignTask();
        org.junit.Assert.assertArrayEquals(notAssignTask.toArray(new String[notAssignTask.size()]),
                new String[]{"第三方负责人分配"});

        processUtils.logInfoAllNotAssignTask();




    }


}
package com.renzq.app.activitidemo.process.utils;


import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProcessUtils is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 11:28
 * Description:
 */
public class ProcessUtils {

    private static final transient Logger log = LoggerFactory.getLogger(ProcessUtils.class);

    private ProcessEngine processEngine;

    public ProcessUtils(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                ;
        processEngine = cfg.buildProcessEngine();
        this.processEngine = processEngine;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public Deployment deployProcess(String processPath){

        String pName = processEngine.getName();
        String ver = ProcessEngine.VERSION;
        log.info("ProcessEngine [" + pName + "] Version: [" + ver + "]");

        RepositoryService repositoryService = processEngine.getRepositoryService();
        return repositoryService
                .createDeployment()
                .addClasspathResource(processPath).deploy();
    }

    public ProcessInstance startProcessSimple(Deployment deployment, String key){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        log.info("查找流程定义，名称[{}], ID为:[{}]", processDefinition.getName(), processDefinition.getId());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
        log.info("流程启动，流程实例processInstance的ID为{}, 流程实例的key为{}", processInstance.getProcessDefinitionId(), processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    public void logInfoAllNotAssignTask(){
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskUnassigned()//.taskDefinitionKey("ReqAllot")
                .list();
        log.info("获取到的需求分配的节点有{}个", tasks.size());
        for(Task task:tasks){
            //taskService.complete(task.getId(), null);
            log.info("{} 任务id: {}， 任务key：{}", task.getName(), task.getId(), task.getTaskDefinitionKey());
        }
    }

    public void logInfoAllTaskInProcess(String processInstanceId){
        String[] names = this.getTaskInProcessInstanceNames(processInstanceId);
        log.info("目前流程中的节点为：{}", String.join(",", names));
    }

    public void logInfoHistoryTask(String processInstanceId){
        HistoryService historyService = this.processEngine.getHistoryService();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByTaskCreateTime().asc().list();

        List<String> taskInstances = new ArrayList<>();
        for(HistoricTaskInstance t : historicTaskInstances)
            taskInstances.add(t.getName()+"_"+t.getTaskDefinitionKey());

        log.info("历史轨迹：{}", String.join("->", taskInstances));
    }

    public void completeTaskByDefinitionKey(String key, Map<String, Object> variable){
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> reqAllots = taskService.createTaskQuery().taskDefinitionKey(key).list();
        for(Task task: reqAllots){
            taskService.setAssignee(task.getId(), "admin"); //都是由admin来完成的
            taskService.complete(task.getId(), variable);
        }
    }

    public List<String> getNotAssignTask(){
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskUnassigned()//.taskDefinitionKey("ReqAllot")
                .list();
        return tasks.stream().map(TaskInfo::getName).collect(Collectors.toList());
    }

    public String[] getTaskInProcessInstanceNames(String processInstanceId){
        List<Task> tasks = this.getTaskInProcessInstance(processInstanceId);
        List<String> names = tasks.stream().map(TaskInfo::getName).collect(Collectors.toList());
        return names.toArray(new String[names.size()]);
    }

    public List<Task> getTaskInProcessInstance(String processInstanceId){
        TaskService taskService = this.processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks;
    }


}
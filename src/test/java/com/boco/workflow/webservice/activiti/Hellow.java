package com.boco.workflow.webservice.activiti;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:config/spring/applicationContext*.xml","classpath*:config/spring/spring-*.xml","classpath*:config/spring/local-*.xml"})
public class Hellow  extends AbstractJUnit4SpringContextTests {

	@Autowired
	ProcessEngine engine;
	
	@Test
	public void deployProcess(){
		
		Deployment deployment = engine.getRepositoryService().createDeployment()
			.addClasspathResource("diagrams/hello.bpmn")
			.name("申请流程").deploy();
		System.out.println(deployment);
		
		ProcessInstance instance=engine.getRuntimeService().startProcessInstanceByKey("hello");
		System.out.println(instance);
		List<Task> list = engine.getTaskService().createTaskQuery().list();
		System.out.println(list.size());
		for(Task t : list){
			System.out.println(t);
		}
		
	}
	
	@Test
	public void startProcess(){
		
		
		
	}
	
	@Test
	public void listTask(){
		
	}
	
	@Test
	public void completeTask(){
		
		
	//	engine.getTaskService().complete("35007");
		System.out.println("over");
		
	}
	
	@After
	public void deleteAll(){
		List<Deployment> list = engine.getRepositoryService().createDeploymentQuery().list();
		for(Deployment d : list){
		//	engine.getRepositoryService().deleteDeployment(d.getId(),true);
		}
	}
}

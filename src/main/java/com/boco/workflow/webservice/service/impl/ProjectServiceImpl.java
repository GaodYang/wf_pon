package com.boco.workflow.webservice.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.workflow.webservice.builder.ProjectBuilder;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.Project;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 
 * @author gaoyang 2017年3月7日
 *	新增工程的service
 */
@Service
public class ProjectServiceImpl extends AbstractService<ProjectBuilder,Project> implements IService{
	
	private static final Logger logger = Logger.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectDAO dao;
	
	@Override
	protected void doBusiness(Project project) {
		
		logger.info("ProjectServiceImpl.doBusiness");
		dao.insertProject(project.getBuilder().addCuid().build());
		
		
	}
}

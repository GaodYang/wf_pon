package com.boco.workflow.webservice.builder;

import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.pojo.Project;

/**
 * 构建工程的结构
 * @author gaoyang 2017年3月7日
 *
 */
public class ProjectBuilder extends AbstractBuilder<Project> implements IBuilder<Project>{

	public ProjectBuilder() {
		
		super(new Project());
	}
	
	public ProjectBuilder(Project project){
		
		super(project);
	}

	
	public ProjectBuilder addCuid(){
		
		pojo.setCuid(CUIDHexGenerator.getInstance().generate("T_WF_PROJECT"));
		return this;
	}
	
	
}

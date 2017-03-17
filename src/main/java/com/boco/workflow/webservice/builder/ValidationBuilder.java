package com.boco.workflow.webservice.builder;

import com.boco.workflow.webservice.pojo.Validation;

/**
 * 资源录入校验信息 构造类
 * @author gaoyang 2017年3月8日
 *
 */
public class ValidationBuilder extends AbstractBuilder<Validation> implements IBuilder<Validation>{

	public ValidationBuilder() {
		
		super(new Validation());
		
	}
	
	public ValidationBuilder addPrjCode(String prjCode){
		
		pojo.setPrjCode(prjCode);
		return this;
	}
	
	public ValidationBuilder addParentPrjCode(String parentPrjCode){
		
		pojo.setParentPrjCode(parentPrjCode);
		return this;
	}

	public ValidationBuilder addteststatus(String testStatus){
	
		pojo.setTestStatus(testStatus);
		return this;
	}

	public ValidationBuilder addtestinfo(String testInfo){
	
		pojo.setTestInfo(testInfo);
		return this;
	}


	
}

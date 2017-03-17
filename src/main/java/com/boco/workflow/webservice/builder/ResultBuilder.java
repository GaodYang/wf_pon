package com.boco.workflow.webservice.builder;

import com.boco.workflow.webservice.pojo.Result;

/**
 * 
 * @author gaoyang 2017年3月7日
 *
 *	消息返回体的构建类
 */
public class ResultBuilder extends AbstractBuilder<Result> implements IBuilder<Result>{

	
	public ResultBuilder() {
		
		super(new Result());
	}
	
	public ResultBuilder addIsSuccess(String isSuccess){
		
		pojo.setIsSuccess(isSuccess);
		return this;
	}
	
	public ResultBuilder addErrorInfo(String errorInfo){
		
		pojo.setErrorInfo(errorInfo);
		return this;
	}
	
	

}

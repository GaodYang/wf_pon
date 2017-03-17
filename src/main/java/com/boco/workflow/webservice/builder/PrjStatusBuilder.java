package com.boco.workflow.webservice.builder;

import com.boco.workflow.webservice.pojo.PrjStatus;

/**
 * 工程状态的构造类
 * @author gaoyang 2017年3月8日
 *
 */
public class PrjStatusBuilder extends AbstractBuilder<PrjStatus> implements IBuilder<PrjStatus>{
	
	public PrjStatusBuilder() {
		
		super(new PrjStatus());
	}
	
	/**
	 * 	工程编号（大项和子任务）
	 */
	public PrjStatusBuilder addPrjCode(String prjCode){
		
		pojo.setPrjCode(prjCode);
		return this;
	}
	
	/**
	 * 所属工程编码
	 */
	public PrjStatusBuilder addParentPrjCode(String parentPrjCode){
		
		pojo.setParentPrjCode(parentPrjCode);
		return this;
	}	
	
	/**
	 * 操作人名称
	 */
	public PrjStatusBuilder addUserName(String userName){
		
		pojo.setUserName(userName);
		return this;
	} 	
	
	/**
	 * 工程状态
	 */
	public PrjStatusBuilder addPrjStatus(String prjStatus){
		
		pojo.setPrjStatus(prjStatus);
		return this;
	}
	
}

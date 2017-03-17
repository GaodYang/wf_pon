package com.boco.workflow.webservice.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 工程状态类
 * @author gaoyang 2017年3月8日
 *
 */
@XStreamAlias("info")
public class PrjStatus {

	//	工程编号（大项和子任务）
	@XStreamAlias("prjcode")
	private String prjCode;
	
	//所属工程编码
	@XStreamAlias("parentprjcode")
	private String parentPrjCode;	
	
	//操作人名称
	@XStreamAlias("username")
	private String userName; 	
	
	//工程状态
	@XStreamAlias("prjstatus")
	private String prjStatus;

	public String getPrjCode() {
		return prjCode;
	}

	public void setPrjCode(String prjCode) {
		this.prjCode = prjCode;
	}

	public String getParentPrjCode() {
		return parentPrjCode;
	}

	public void setParentPrjCode(String parentPrjCode) {
		this.parentPrjCode = parentPrjCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrjStatus() {
		return prjStatus;
	}

	public void setPrjStatus(String prjStatus) {
		this.prjStatus = prjStatus;
	}	
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}

}

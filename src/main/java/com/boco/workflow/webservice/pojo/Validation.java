package com.boco.workflow.webservice.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 资源录入校验信息
 * @author gaoyang 2017年3月8日
 *
 */
@XStreamAlias("info")
public class Validation {

	/**
	 * 工程编号（大项和子任务）
	 */
	@XStreamAlias("prjcode")
	private String prjCode;
	
	/**
	 * 所属工程编码
	 */
	@XStreamAlias("parentprjcode")
	private String parentPrjCode;	
	
	/**
	 * 挂测状态
	 */
	@XStreamAlias("teststatus")
	private String testStatus;	
	
	/**
	 * 挂测结果信息
	 */
	@XStreamAlias("testinfo")
	private String testInfo;

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

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getTestInfo() {
		return testInfo;
	}

	public void setTestInfo(String testInfo) {
		this.testInfo = testInfo;
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}

}

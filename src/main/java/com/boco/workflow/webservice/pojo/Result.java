package com.boco.workflow.webservice.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author gaoyang 2017年3月7日
 *
 * webservice消息返回体
 */
@XStreamAlias("result")
public class Result {

	/**
	 * 是否成功
	 */
	@XStreamAlias("issuccess")
	private String isSuccess;
	
	/**
	 * 错误信息
	 */
	@XStreamAlias("errorinfo")
	private String errorInfo;

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	

}

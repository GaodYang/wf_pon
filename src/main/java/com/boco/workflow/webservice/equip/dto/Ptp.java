package com.boco.workflow.webservice.equip.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Ptp {

	
	/**
	 * cuid
	 */
	private String cuid;
	
	/**
	 * 端口状态
	 */
	private String portState;
	
	
	
	
	public String getCuid() {
		return cuid;
	}




	public void setCuid(String cuid) {
		this.cuid = cuid;
	}




	public String getPortState() {
		return portState;
	}




	public void setPortState(String portState) {
		this.portState = portState;
	}




	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}

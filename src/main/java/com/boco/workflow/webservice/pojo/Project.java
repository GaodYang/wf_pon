package com.boco.workflow.webservice.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.boco.workflow.webservice.builder.ProjectBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author gaoyang 2017年3月7日
 *
 * 新增项目的消息体
 */
@XStreamAlias("info")
public class Project {

	private String cuid;
	/**
	 * 立项文件名称
	 */
	@XStreamAlias("setupname")
	private String setupName;
	
	/**
	 * 立项文号
	 */
	@XStreamAlias("setupcode")
	private String setupCode;
	
	/**
	 * 立项申请文号
	 */
	@XStreamAlias("setupreqfilecode")
	private String setupReqFileCode;
	
	/**
	 * 立项申请人
	 */
	@XStreamAlias("setupreqername")
	private String setupReqerName;
	
	/**
	 * 立项申请人电话
	 */
	@XStreamAlias("setupreqerphone")
	private String setupReqerPhone;
	
	/**
	 * 立项申请日期 例如：2015-12-09
	 */
	@XStreamAlias("setupreqdate")
	private String setupReqDate;
	
	/**
	 * 工程名称
	 */
	@XStreamAlias("prjname")
	private String prjName;
	
	/**
	 * 工程编号
	 */
	@XStreamAlias("prjcode")
	private String prjCode;
	
	/**
	 * 工程类别
	 */
	@XStreamAlias("prjcategory")
	private String prjCategory;
	
	/**
	 * 工程类型
	 */
	@XStreamAlias("prjtype")
	private String prjType;
	
	/**
	 * 工程总投资(万)
	 */
	@XStreamAlias("cost")
	private String cost;
	
	/**
	 * 工程规模长度
	 */
	@XStreamAlias("length")
	private String length;
	
	/**
	 * 所属地市
	 */
	@XStreamAlias("domainname")
	private String domainName;

	/**
	 * 工程描述
	 */
	@XStreamAlias("prjdesc")
	private String prjDesc;
	
	/**
	 * FTP文件名称
	 */
	@XStreamAlias("ftpname")
	private String ftpName;
	
	/**
	 * 所属工程编码
	 */
	@XStreamAlias("parentprjcode")
	private String parentPrjCode;

	
	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getSetupName() {
		return setupName;
	}

	public void setSetupName(String setupName) {
		this.setupName = setupName;
	}

	public String getSetupCode() {
		return setupCode;
	}

	public void setSetupCode(String setupCode) {
		this.setupCode = setupCode;
	}

	public String getSetupReqFileCode() {
		return setupReqFileCode;
	}

	public void setSetupReqFileCode(String setupReqFileCode) {
		this.setupReqFileCode = setupReqFileCode;
	}

	public String getSetupReqerName() {
		return setupReqerName;
	}

	public void setSetupReqerName(String setupReqerName) {
		this.setupReqerName = setupReqerName;
	}

	public String getSetupReqerPhone() {
		return setupReqerPhone;
	}

	public void setSetupReqerPhone(String setupReqerPhone) {
		this.setupReqerPhone = setupReqerPhone;
	}

	public String getSetupReqDate() {
		return setupReqDate;
	}

	public void setSetupReqDate(String setupReqDate) {
		this.setupReqDate = setupReqDate;
	}

	public String getPrjName() {
		return prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	public String getPrjCode() {
		return prjCode;
	}

	public void setPrjCode(String prjCode) {
		this.prjCode = prjCode;
	}

	public String getPrjCategory() {
		return prjCategory;
	}

	public void setPrjCategory(String prjCategory) {
		this.prjCategory = prjCategory;
	}

	public String getPrjType() {
		return prjType;
	}

	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getPrjDesc() {
		return prjDesc;
	}

	public void setPrjDesc(String prjDesc) {
		this.prjDesc = prjDesc;
	}

	public String getFtpName() {
		return ftpName;
	}

	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}

	public String getParentPrjCode() {
		return parentPrjCode;
	}

	public void setParentPrjCode(String parentPrjCode) {
		this.parentPrjCode = parentPrjCode;
	}
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this);
	}
	
	public ProjectBuilder getBuilder(){
		
		return new ProjectBuilder(this);
	}
	

}

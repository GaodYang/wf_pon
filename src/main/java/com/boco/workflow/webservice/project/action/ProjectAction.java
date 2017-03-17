package com.boco.workflow.webservice.project.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.boco.core.bean.SpringContextUtil;
import com.boco.workflow.webservice.project.bo.ProjectBO;

/**
 * 项目工程action
 * @author gaoyang 2017年3月15日
 *
 */
public class ProjectAction {

	private ProjectBO getProjectBO(){
		
		return (ProjectBO)SpringContextUtil.getBean("projectBO");
	}
	/**
	 * 查询单条数据
	 * 
	 * @param request
	 * @param cuid 
	 */
	 public Map<String,Object> queryProjectByCode(HttpServletRequest request, String prjcode)throws Exception {
		 
			return getProjectBO().queryProjectByCode(prjcode);
	 }
}

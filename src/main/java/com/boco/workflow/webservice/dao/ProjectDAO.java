package com.boco.workflow.webservice.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.pojo.PrjStatus;
import com.boco.workflow.webservice.pojo.Project;

@Repository
public class ProjectDAO extends AbstractDAO{

	private static final Logger logger = Logger.getLogger(ProjectDAO.class);
	
	/**
	 * 新建工程
	 * @param project
	 */
	public void insertProject(Project project){
		
		try {
			this.getBaseDAO().getSqlMapClient().insert(NetWorkConstant.PROJECT_SQL_MAP + ".insertProject", project);
		} catch (SQLException e) {

			logger.error(e);
		}
	}

	/**
	 * 更新工程状态
	 * @param prjStatus
	 */
	public void updateProjectStatus(PrjStatus prjStatus) {
		
		try {
			this.getBaseDAO().getSqlMapClient().update(NetWorkConstant.PROJECT_SQL_MAP + ".updateProjectStatus", prjStatus);
		} catch (SQLException e) {

			logger.error(e);
		}
		
	}
}

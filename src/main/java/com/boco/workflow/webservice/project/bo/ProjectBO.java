package com.boco.workflow.webservice.project.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.workflow.webservice.constants.NetWorkConstant;

@Service
public class ProjectBO {

	@Autowired
	private IbatisDAO IbatisDAO;

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryProjectByCode(String prjcode) throws SQLException {
		
		List<Map<String,Object>> list = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.PROJECT_SQL_MAP + ".queryProjectByCode", prjcode);
		Map<String,Object> map = new HashMap<String,Object>();
		if(list != null && list.size() > 0){
			map = list.get(0);
		}
		return map;
	}
	
	
}

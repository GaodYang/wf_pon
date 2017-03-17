package com.boco.workflow.webservice.dao;

import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;

public class AbstractDAO {

	protected IbatisDAO getBaseDAO() {

		return (IbatisDAO) SpringContextUtil.getBean("IbatisDAO");
	}
}

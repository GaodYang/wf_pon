package com.boco.workflow.webservice.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.boco.workflow.webservice.builder.ValidationBuilder;
import com.boco.workflow.webservice.dao.ProjectDAO;
import com.boco.workflow.webservice.pojo.Validation;
import com.boco.workflow.webservice.service.AbstractService;
import com.boco.workflow.webservice.service.IService;

/**
 * 挂测结果处理
 * @author gaoyang 2017年3月16日
 *
 */
public class HangingResutServiceImpl extends AbstractService<ValidationBuilder,Validation> implements IService{

	private static final Logger logger = Logger.getLogger(HangingResutServiceImpl.class);

	@Autowired
	private ProjectDAO dao;
	@Override
	protected void doBusiness(Validation e) {
		
		logger.info("HangingResutServiceImpl.doBusiness");
		//存储结果？
		
		//向管线发消息
		
	}
}

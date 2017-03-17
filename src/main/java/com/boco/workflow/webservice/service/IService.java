package com.boco.workflow.webservice.service;

/**
 * 服务类接口
 * @author gaoyang 2017年3月8日
 *
 */
public interface IService {

	//接收xml，返回结果报文
	String execute(String xml);
}

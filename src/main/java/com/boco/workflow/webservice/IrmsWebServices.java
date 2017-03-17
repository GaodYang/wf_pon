package com.boco.workflow.webservice;

import com.boco.workflow.webservice.service.IService;
import com.boco.workflow.webservice.service.factory.WebServiceFactory;
import com.boco.workflow.webservice.service.impl.HangingResutServiceImpl;
import com.boco.workflow.webservice.service.impl.ProjectServiceImpl;
import com.boco.workflow.webservice.service.impl.SyncPrjStatusServiceImpl;

/**
 * 提供webservice服务
 * @author gaoyang 2017年3月9日
 *
 */
public class IrmsWebServices {

	/**
	 * 创建工程
	 * @param xml
	 * @return
	 */
	public String createProject(String xml){
		
		IService service = WebServiceFactory.getService(ProjectServiceImpl.class);
		return service.execute(xml);
	}
	
	/**
	 * 同步工程状态
	 * @param xml
	 * @return
	 */
	public String syncPrjStatus(String xml){
		
		IService service = WebServiceFactory.getService(SyncPrjStatusServiceImpl.class);
		return service.execute(xml);
	}
	
	/**
	 * 挂测结果
	 * @param xml
	 * @return
	 */
	public String syncHangingResult(String xml){
		
		IService service = WebServiceFactory.getService(HangingResutServiceImpl.class);
		return service.execute(xml);
	}
}

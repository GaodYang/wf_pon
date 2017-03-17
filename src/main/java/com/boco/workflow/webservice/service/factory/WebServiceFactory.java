package com.boco.workflow.webservice.service.factory;

import com.boco.core.bean.SpringContextUtil;
import com.boco.workflow.webservice.service.IService;

/**
 * 获取spring中的服务类
 * @author gaoyang 2017年3月9日
 *
 */
public class WebServiceFactory {

	private WebServiceFactory() throws IllegalAccessException {
		
		throw new IllegalAccessException("不能创建WebServiceFactory。");
	}
	
	public static <T extends IService>IService getService(Class<T> clazz){
		
		IService service = (IService)SpringContextUtil.getBean(getBeanNameByClassName(clazz.getSimpleName()));
		return service;
	}
	
	/**
	 * 将类名首字母小写
	 * @param className
	 * @return
	 */
	private static String getBeanNameByClassName(String className){
		
		char[] name = className.toCharArray();
		name[0] += 32;
		return String.valueOf(name);
	}

}

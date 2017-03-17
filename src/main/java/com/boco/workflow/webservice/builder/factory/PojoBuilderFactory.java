package com.boco.workflow.webservice.builder.factory;


import org.apache.log4j.Logger;

import com.boco.workflow.webservice.builder.IBuilder;

/**
 * 
 * @author gaoyang 2017年3月7日
 *
 *  工厂模式，或者建造类
 */
public class PojoBuilderFactory {

	private static final Logger logger = Logger.getLogger(PojoBuilderFactory.class);
	private PojoBuilderFactory() {
	}
	
	//获取构建器
	@SuppressWarnings("unchecked")
	public static <T extends IBuilder<?>>T getBuilder(Class<T> cls){
		
		IBuilder<?> builder = null;
		try {
			builder = (IBuilder<?>)Class.forName(cls.getName()).newInstance();
		} catch (Exception e) {
			
			logger.error("创建构造器失败!", e);
		} 
		return (T)builder;
	}
	
}

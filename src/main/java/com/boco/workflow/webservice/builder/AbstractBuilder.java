package com.boco.workflow.webservice.builder;

import com.boco.workflow.webservice.utils.XStreamUtil;

public abstract class AbstractBuilder<T> implements IBuilder<T>{

	protected T pojo;
	
	public AbstractBuilder(T pojo) {
		
		this.pojo = pojo;
	}
	/**
	 * 实例转换为xml
	 * @return
	 */
	public String toXml(){
		
		String xml = XStreamUtil.toXml(pojo);
		return xml;
	}
	
	/**
	 * xml转换为实例
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T fromXml(String xml){
		
		Class<?> clazz = pojo.getClass();
		pojo = null;
		pojo = (T)XStreamUtil.fromXml(clazz, xml);
		return pojo;
	}
	
	@Override
	public T build() {
		
		return pojo;
	}
	

	
}

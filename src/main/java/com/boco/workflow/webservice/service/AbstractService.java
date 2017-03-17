package com.boco.workflow.webservice.service;

import java.lang.reflect.ParameterizedType;

import org.apache.log4j.Logger;

import com.boco.workflow.webservice.builder.AbstractBuilder;
import com.boco.workflow.webservice.builder.ResultBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.constants.WebServiceConstant.ResultEnum;

/**
 * 基础服务类
 * @author gaoyang 2017年3月7日
 * 基础服务：报文解析、报文校验、结果返回
 * @param <E>
 */
public abstract class AbstractService<T extends AbstractBuilder<E>, E> implements IService{

	private static final Logger logger = Logger.getLogger(AbstractService.class);
	
	/**
	 * 解析xml，转化为实体类
	 * @param xml
	 */

	private E analyzeXml(String xml){

		E e = getBuilder().fromXml(xml);
		logger.info("报文转化为实体：" + e);
		return e;
	}
	
	/**
	 * 消息校验
	 * @return
	 */
	private boolean validate(E e){
		
		return true;
	}
	
	/**
	 * 返回消息
	 * @param isSuccess
	 * @param errorInfo
	 * @return
	 */
	private String buildResult(ResultEnum reEnum){
		
		String xml = PojoBuilderFactory.getBuilder(ResultBuilder.class)
					.addErrorInfo(reEnum.getErrorInfo()).addIsSuccess(reEnum.getIsSuccess()).toXml();
		return xml;
	}
	
	/**
	 * 执行具体业务
	 */
	abstract protected void doBusiness(E e) ;
	
	
	@Override
	public String execute(String xml){
		
		String result = "";
		//报文解析
		E e = this.analyzeXml(xml);
		//报文验证
		if(this.validate(e)){
			//处理业务逻辑
			this.doBusiness(e);
			result = this.buildResult(ResultEnum.SUCESS);
		}else{
			
			//验证未通过
			result = this.buildResult(ResultEnum.VALIDERROR);
		}
		return result;
	}
	 
	 /**
	  * 根据泛型类型获取构造器
	  * @return
	  */
	@SuppressWarnings({ "unchecked"})
	public T getBuilder(){
		 
		return  PojoBuilderFactory.getBuilder(((Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
	}
	 


}

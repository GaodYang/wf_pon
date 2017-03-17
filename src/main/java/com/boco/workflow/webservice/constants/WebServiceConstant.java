package com.boco.workflow.webservice.constants;

/**
 * 
 * @author gaoyang 2017年3月7日
 * 常量定义类
 */
public class WebServiceConstant {

	private WebServiceConstant() throws IllegalAccessException {
		throw new IllegalAccessException("不能创建此类");
	}
	
	/**
	 * xml消息头
	 */
	public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
	
	/**
	 * CDATA标签开始
	 */
	public static final String PREFIX_CDATA = "<![CDATA[";  
	
	/**
	 * CDATA标签结束
	 */
	public static final String SUFFIX_CDATA = "]]>";  
	/**
	 * 返回报文枚举
	 * @author gaoyang 2017年3月8日
	 *
	 */
	public static enum ResultEnum{
		
		SUCESS("0",""),VALIDERROR("1","校验不同过"),FAIL("1","");
		
		private String isSuccess;
		
		private String errorInfo;
		
		ResultEnum(String isSucess,String errorInfo){
			this.isSuccess = isSucess;
			this.errorInfo = errorInfo;
		}

		public String getIsSuccess() {
			return isSuccess;
		}

		public String getErrorInfo() {
			return errorInfo;
		}
		
	}
}

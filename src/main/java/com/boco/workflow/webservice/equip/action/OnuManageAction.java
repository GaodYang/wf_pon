package com.boco.workflow.webservice.equip.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.boco.core.bean.SpringContextUtil;
import com.boco.core.utils.exception.UserException;
import com.boco.workflow.webservice.equip.bo.CheckRelationBO;
import com.boco.workflow.webservice.equip.bo.OnuManageBO;

/**
 * onu管理action
 * @author gaoyang 2017年3月13日
 *
 */
public class OnuManageAction {
	
	private static final Logger logger = Logger.getLogger(OnuManageAction.class);
	
	private CheckRelationBO getCheckRelationBO(){
		
		return (CheckRelationBO)SpringContextUtil.getBean("checkRelationBO");
	}
	
	private OnuManageBO getOnuManageBO(){
		
		return (OnuManageBO)SpringContextUtil.getBean("onuManageBO");
	}
	/**
	 * 删除ONU信息
	 * 
	 * @param request
	 * @param cuidList
	 */
	public void deleteOnuInfo(HttpServletRequest request, List<String> cuidList)throws Exception {
		
		boolean flag = getCheckRelationBO().isPtpRelated(cuidList);
		if(!flag){
			
			getOnuManageBO().deleteOnuList(cuidList);
		}
		else {
			
			throw new UserException("onu下存在端口，不能删除！");
		}

	}
	
	/**
	 * 查询ONU名称是否存在
	 * 
	 * @param request
	 * @param name
	 */
	public boolean isOnuNameExist(HttpServletRequest request, String name)throws Exception {
		
		return getOnuManageBO().isOnuNameExist(name);
	}
	
	/**
	 * 根据网元型号得到对应的设备供应商
	 */
	public Map<String,String> queryVendorByModel(HttpServletRequest request,String modelCuid)throws Exception{
		return getOnuManageBO().queryVendorByModel(modelCuid);
	}
	
	/**
	 * 查询单条ONU数据
	 * 
	 * @param request
	 * @param cuid 
	 */
	 public Map<String,Object> queryOnuByCuid(HttpServletRequest request, String cuid)throws Exception {
		 
			return getOnuManageBO().queryOnuByCuid(cuid);
	 }
	 
	 /**
	 * 新增ONU信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addOnuInfo(HttpServletRequest request, Map map) throws Exception {
		
		getOnuManageBO().addOnuInfo(map);
	}
	
	/**
	 * 修改ONU数据信息
	 * 
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public void modifyOnuInfo(HttpServletRequest request, @SuppressWarnings("rawtypes") Map map) throws Exception {

		getOnuManageBO().modifyOnuInfo(map);
	}

}

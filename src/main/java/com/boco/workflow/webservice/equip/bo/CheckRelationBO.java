package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.utils.exception.UserException;
import com.boco.workflow.webservice.constants.NetWorkConstant;

/**
 * 检查关联关系bo
 * @author gaoyang 2017年3月13日
 *
 */
@Service
public class CheckRelationBO {
	
	@Autowired
	private IbatisDAO IbatisDAO;

	/**
	 * 查询设备下是否含有端口
	 * @param cuidList
	 * @return
	 * @throws SQLException 
	 */
	public boolean isPtpRelated(List<String> cuidList) throws SQLException{
		
		boolean flag = false;
		//校验设备下是否存在端口
		@SuppressWarnings("unchecked")
		List<Integer> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryPtpByRelatedNeCuid", cuidList);
		
		if(!list.isEmpty()){
			flag = true;
		}

		return flag;
	}

	/**
	 * 查询产品客户占用端口情况
	 * @param cuidList
	 * @throws SQLException 
	 */
	public void isPtpProductRelated(List<Map<String,String>> cuidList) throws Exception {
		
		for(Map<String,String> map : cuidList){
			
			@SuppressWarnings("unchecked")
			List<Integer> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryProductPort",map.get("CUID"));
			if(list != null && list.size() > 0){
				
				throw new UserException("【" + map.get("LABEL_CN") + "】 在产品客户中被占用！");
			}
		}
		
		
	}

	/**
	 * 检查端口是否被设备占用
	 * @param cuidList
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void isPtpUpneRelated(List<Map<String,String>> cuidList) throws Exception {
		
		for(Map<String,String> map : cuidList){
			
			List<String> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryDevRelatedPort",map.get("CUID"));
			if(list != null && list.size() > 0){
				
				throw new UserException("【" + map.get("LABEL_CN") + "】 被设备【" + list.get(0) + "】占用！");
			}
		}
		
	}
	
}

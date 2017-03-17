package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.utils.BoUtil;
import com.boco.workflow.webservice.equip.dto.Ptp;

/**
 * 端口管理类
 * @author gaoyang 2017年3月13日
 *
 */
@Service
public class PtpManageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	/**
	 * 更新端口状态
	 * @param state
	 * @param cuid
	 * @throws SQLException 
	 */
	public void updatePortState(String state,String cuid) throws SQLException{
		
		Ptp ptp = new Ptp();
		ptp.setCuid(cuid);
		ptp.setPortState(state);
		
		IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePortState", ptp);
	}

	/**
	 * 删除端口
	 * @param cuidList
	 * @throws SQLException 
	 */
	public void deletePtpInfo(List<Map<String,String>> cuidList) throws SQLException {

		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deletePtpByCuid",cuidList);
		
	}

	/**
	 * 查询端口
	 * @param cuid
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> queryLoadPtpByCuid(String cuid) throws SQLException {
		
		Map<String,String> returnMap = null;
		
		List<Map<String,String>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryLoadPtpByCuid",
				cuid);
		if (list != null && list.size() > 0) {
			
			returnMap =  list.get(0);
		}
		
		return returnMap;
		
	}

	/**
	 * 查询单条端口信息
	 * @param devTable
	 * @param cuid
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryPtpByCuid(String cuid) throws SQLException {
		
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryPtpByCuid",cuid);
		
		Map<String,Object> returnMap = BoUtil.convertResult2Panel(list);
		
		return returnMap;
			
	}
}

package com.boco.workflow.webservice.equip.bo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.core.utils.id.CUIDHexGenerator;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.boco.workflow.webservice.dao.utils.BoUtil;

/**
 * onu管理bo
 * @author gaoyang 2017年3月13日
 *
 */
@Service
public class OnuManageBO {

	@Autowired
	private IbatisDAO IbatisDAO;
	
	@Autowired
	private PtpManageBO ptpManageBO;
	/**
	 * 删除onu相关信息
	 * @param cuids
	 * @throws SQLException
	 */
	public void deleteOnuList(List<String> cuids) throws SQLException{
		
		//删除关联板卡
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteCardByNeCuid", cuids);
		//释放上联端口
		BoUtil.batchUpdate(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".updateOnuPtpFree", cuids);
		//删除onu
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteOnuByCuid", cuids);
		//删除覆盖范围
		BoUtil.batchDelete(this.IbatisDAO.getSqlMapClient(),NetWorkConstant.EQUIP_SQL_MAP + ".deleteGponCoverbyNeCuid", cuids);
	}

	

    /**
     * 查询onu名称是否存在
     * @param name
     * @return
     * @throws SQLException 
     */
	public boolean isOnuNameExist(String name) throws SQLException {
		
		if (StringUtils.isNotBlank(name)) {
			@SuppressWarnings("unchecked")
			List<Integer> list = this.IbatisDAO.getSqlMapClient().queryForList(
					NetWorkConstant.EQUIP_SQL_MAP + ".queryOnuByLabelcn", name);
			if (null != list && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 根据网元型号得到对应的设备供应商
	 * @throws SQLException 
	 */
	public Map<String,String> queryVendorByModel(String modelCuid) throws SQLException {
		
		Map<String,String> returnMap = new HashMap<String,String>();
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryVendorBymodel", modelCuid);
		if(!list.isEmpty() && list.size()>0){
			Map<String,Object> map = (Map<String,Object>)list.get(0);
			returnMap.put("text", map.get("LABEL_CN").toString());
			returnMap.put("value", map.get("CUID").toString());
		}
		return returnMap;
	}

	/*
	 * 查询onu信息
	 */
	public Map<String,Object> queryOnuByCuid(String cuid) throws SQLException {
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = this.IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryOnuByCuid", cuid);
		Map<String, Object> returnMap = BoUtil.convertResult2Panel(list);

		return returnMap;
			
		
	}

	/**
	 * 新增onu信息
	 * @param map
	 * @throws SQLException 
	 */
	public void addOnuInfo(Map<String, Object> map) throws Exception {

		String cuid = CUIDHexGenerator.getInstance().generate("TRANS_ELEMENT");

		//插入的时候加入了一些定值
		map.put("CUID", cuid);
		map.put("DEV_CUID", "AN_ONU" + "-" + cuid);
		
		if(map.get("CREATE_TIME") == null){
			map.put("CREATE_TIME",new Timestamp(System.currentTimeMillis()));
		}else{
			map.put("CREATE_TIME", new Date(IbatisDAOHelper.getLongValue(map, "CREATE_TIME")));
		}
		map.put("LAST_MODIFY_TIME", new Timestamp(System.currentTimeMillis()));

		String cabCuid = map.get("RELATED_ACCESS_POINT").toString();
		List<?> dcuids = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid",cabCuid);
		String relatedDistrictCuid = BoUtil.getSingleValueByList(dcuids, "RELATED_DISTRICT_CUID");
		map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
		
		//占用新端口
		ptpManageBO.updatePortState("2", IbatisDAOHelper.getStringValue(map,"RELATED_POS_PORT_CUID"));
		  
		this.IbatisDAO.getSqlMapClient().insert(NetWorkConstant.EQUIP_SQL_MAP + ".insertOnuInfo", map);
	
	}

	/**
	 * 修改onu
	 * @param map
	 */
	public void modifyOnuInfo(Map<String,Object> map) throws Exception{

		String onuCuid = ObjectUtils.toString(map.get("CUID"));
		map.put("LAST_MODIFY_TIME",new Timestamp(System.currentTimeMillis()));
		
		if(StringUtils.isNotBlank((String)map.get("CREATE_TIME"))){
			map.put("CREATE_TIME", new Date(IbatisDAOHelper.getLongValue(map, "CREATE_TIME")));
		}
		this.getDistrictCuidByCab(map);

		String oldPort = IbatisDAOHelper.getStringValue(map, "oldPort");
		String newPort = IbatisDAOHelper.getStringValue(map,"RELATED_POS_PORT_CUID");
		if(oldPort == null || !oldPort.equals(newPort)){
			//释放旧端口	
			if(StringUtils.isNotBlank(oldPort)){
				ptpManageBO.updatePortState("1", oldPort);
			}
			//占用新端口
			ptpManageBO.updatePortState("2", newPort);
		}
		
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateOnuInfo", map);
		
		///更新板卡和端口的名称
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updateCardNameByOnu", onuCuid);
		this.IbatisDAO.getSqlMapClient().update(NetWorkConstant.EQUIP_SQL_MAP + ".updatePtpNameByOnu", onuCuid);
		
	}
	
	/**
	 * 获取地区id
	 * @param map
	 * @throws SQLException 
	 */
	private void getDistrictCuidByCab(Map<String,Object> map) throws SQLException{
		
		String cabCuid = map.get("RELATED_ACCESS_POINT").toString();
		List<?> dcuids = IbatisDAO.getSqlMapClient().queryForList(NetWorkConstant.EQUIP_SQL_MAP + ".queryRelatedDistrictCuidByCabCuid",cabCuid);
		String relatedDistrictCuid = BoUtil.getSingleValueByList(dcuids, "RELATED_DISTRICT_CUID");
		map.put("RELATED_DISTRICT_CUID", relatedDistrictCuid);
	}
	
	
}

package com.boco.workflow.webservice.upload.bo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.boco.common.util.debug.LogHome;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.utils.lang.Assert;
import com.boco.core.utils.lang.StringHelper;
import com.boco.workflow.webservice.constants.NetWorkConstant;

@Repository
public class ImportBasicDataBO {
	
	private static final String IMPROT_BASIC_DATA_SQL_MAP0 = "IMPROTBASEDATA";
	private static final String IMPROT_BASIC_DATA_SQL_MAP1 = "EQUIP";
	private static final String IMPROT_BASIC_DATA_SQL_MAP2 = "AREA";
	private static final String IMPROT_BASIC_DATA_SQL_MAP3 = "TOPO";
	private static final String IMPROT_BASIC_DATA_SQL_MAP4 = "GPON";
	private static final String IMPORT_PON = "PONIMPORT";
	private static final String FULLADDRESS_SQL_MAP = "FULLADDRESS";
	private static final String GPONCOVER_SQL_MAP = "GPONCOVER";
	private static final String BUSINESSCOMMUNITY_SQL_MAP = "BUSINESSCOMMUNITY";

	
	/*
	 * 判断POS的上联设备主用端口是否在该所属网元下存在
	 */
	public List isPortExist(String portName,String neCuid){
		Map map = new HashMap();
		map.put("RELATED_NE_CUID",neCuid);
		map.put("LABEL_CN",portName);
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP0+".queryPtpByLabelcnNeCuid", map);
	}



	/**
	 * 取得所有机盘类型信息
	 * 
	 * @return Map key厂商label_cn+机盘label_cn
	 * @author WHuan
	 * @throws SQLException 
	 */
	public Map getAllCardKind() throws SQLException {
		List list = null;
		Map cardKindMap = new HashMap();
		list = this.IbatisDAO.getSqlMapClient().queryForList(
				IMPROT_BASIC_DATA_SQL_MAP0 + ".queryCardKind");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map mapSub = new HashMap();
				mapSub = (Map) list.get(i);
				String vendorName = (String) mapSub.get("LABEL_CN");
				String cardKindName = (String) mapSub.get("CARDTYPE_NAME");
				cardKindMap.put(vendorName + cardKindName, cardKindName);
			}
		}// 如果list为空,就不用给cardKindMap填值,直接返回空map
		return cardKindMap;
	}

	/**
	 * 取得所有网元型号信息
	 * 
	 * @return Map key厂商名+网元型号名
	 * @author WHuan
	 * @throws SQLException 
	 */
	public Map getAllNeModel() throws SQLException {
		List list = null;
		Map neModelMap = new HashMap();
		list = this.IbatisDAO.getSqlMapClient().queryForList(
				IMPROT_BASIC_DATA_SQL_MAP0 + ".queryNeModel");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map mapSub = new HashMap();
				mapSub = (Map) list.get(i);
				String vendorName = (String) mapSub.get("LABEL_CN");
				String neModelName = (String) mapSub.get("PRODUCT_MODEL");
				neModelMap.put(vendorName + neModelName, neModelName);
			}
		}// 如果list为空,就不用给cardKindMap填值,直接返回空map
		return neModelMap;
	}

	/**
	 * 将room的全部集合放在map中，进行校验
	 * 
	 * @return room map结合
	 */
	public Map getRoomCuidName() {
		Map roomCuidNameMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,LABEL_CN  FROM ROOM ");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String cuid = (String) mapSub.get("LABEL_CN");
			String labelcn = (String) mapSub.get("CUID");
			roomCuidNameMap.put(cuid, labelcn);
		}
		return roomCuidNameMap;
	}

	/**
	 * 将site的全部集合放在map中，进行校验
	 * 
	 * @return room map结合
	 */
	public Map getRoomCuidRelatedCuid() {
		Map roomCuidRelatedCuid = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,RELATED_SPACE_CUID  FROM ROOM");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String cuid = (String) mapSub.get("CUID");
			String districtCuid = (String) mapSub.get("RELATED_SPACE_CUID");
			roomCuidRelatedCuid.put(cuid, districtCuid);
		}
		return roomCuidRelatedCuid;
	}

	/**
	 * 将site的全部集合放在map中，进行校验
	 * 
	 * @return room map结合
	 */
	public Map getSiteCuidName() {
		Map siteMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,LABEL_CN  FROM SITE ");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String name = (String) mapSub.get("LABEL_CN");
			String cuid = (String) mapSub.get("CUID");
			siteMap.put(name, cuid);
		}
		return siteMap;
	}

	/**
	 * 将site的全部集合放在map中，进行校验
	 * 
	 * @return room map结合
	 */
	public Map getSiteCuidRelatedCuid() {
		Map districtMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,RELATED_SPACE_CUID  FROM SITE");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String cuid = (String) mapSub.get("CUID");
			String districtCuid = (String) mapSub.get("RELATED_SPACE_CUID");
			districtMap.put(cuid, districtCuid);
		}
		return districtMap;
	}

	/**
	 * 将site的全部集合放在map中，进行校验
	 * 
	 * @return room map结合
	 */
	public Map getDistrictCuidName() {
		Map districtMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,LABEL_CN  FROM DISTRICT");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String name = (String) mapSub.get("LABEL_CN");
			String cuid = (String) mapSub.get("CUID");
			districtMap.put(name, cuid);
		}
		return districtMap;
	}

	/**
	 * 将ems的全部集合放在map中，进行校验
	 * 
	 * @return ems map结合
	 */
	public Map getNmsCuidName() {
		Map nmsMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT  CUID,LABEL_CN  FROM NMS_SYSTEM ");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String labelcn = (String) mapSub.get("LABEL_CN");
			String cuid = (String) mapSub.get("CUID");
			nmsMap.put(labelcn, cuid);
		}
		return nmsMap;
	}

	/**
	 * 将ems的fdn全部集合放在map中，进行校验
	 * 
	 * @return ems map结合
	 */
	public Map getNmsCuidFdn() {
		Map nmsFdnMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT  CUID,FDN FROM NMS_SYSTEM");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String cuid = (String) mapSub.get("CUID");
			String fdn = (String) mapSub.get("FDN");
			nmsFdnMap.put(cuid, fdn);
		}
		return nmsFdnMap;
	}

	/**
	 * 将网元型号的全部集合放在map中，进行校验
	 * 
	 * @return 网元型号 map结合
	 */
	public Map<String,String> getNeModelCfgCuidName() {
		
		Map<String,String> neModelMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT PRODUCT_MODEL,CUID FROM NE_MODEL_CFG_TYPE");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String productmodel = (String) mapSub.get("PRODUCT_MODEL");
			String cuid = (String) mapSub.get("CUID");
			neModelMap.put(productmodel, cuid);
		}
		return neModelMap;
	}
	
	/**
	 * 得到机盘类型
	 * 
	 * @return 网元型号 map结合
	 */
	public Map getCardKind() {
		Map cardNodelMap  = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT CUID,CARDTYPE_NAME,PUBLIC_NAME FROM CARD_KIND");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String productmodel = (String) mapSub.get("CARDTYPE_NAME");
			BigDecimal publicName = (BigDecimal)mapSub.get("PUBLIC_NAME");
			String cuidAndPublicName = (String) mapSub.get("CUID")+"|"+publicName;
			cardNodelMap.put(productmodel, cuidAndPublicName);
		}
		return cardNodelMap;
	}
	
	/**
	 * 将供应厂商的全部集合放在map中，进行校验
	 * 
	 * @return 厂商map结合
	 */

	public Map getVendorCuidName() {
		Map vendorMap = new HashMap();
		List list = this.IbatisDAO.querySql("SELECT LABEL_CN,CUID FROM DEVICE_VENDOR ");
		for (int i = 0; i < list.size(); i++) {
			Map mapSub = (Map) list.get(i);
			String labelcn = (String) mapSub.get("LABEL_CN");
			String cuid = (String) mapSub.get("CUID");
			vendorMap.put(labelcn, cuid);
		}
		return vendorMap;
	}

	/**
	 * 通过端口的FDN得到所属EMS
	 * @param ptpFdn
	 * @return relateEmsCuid
	 * @throws Exception 
	 */
	public String getEmsByPtpFdn(String ptpFdn){
		String labelcn = ptpFdn.split(":")[0];
		labelcn = labelcn.split("=")[1];
		if(!StringUtils.isEmpty(labelcn)){
			List list = (this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP1 + ".queryEmsCuidByLabelcn",
					labelcn));
			if(list != null && list.size()>0){
				return list.get(0).toString();
			}//不处理空值
		}//不处理空值
		return null;
	}
	/**
	 * 批量更新端口数据,端口数据入库使用
	 */
	public boolean importPtpBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importPtpBatchUpdate";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新端口出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新端口出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入端口数据,端口数据入库使用
	 */
	public boolean importPtpBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importPtpBatchInsert";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加端口出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加端口出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量新增卡板，卡板数据入库使用
	 */
	public boolean importCardBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importCardBatchInsert";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientInsert(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加卡板出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量更新卡板,卡板入库使用
	 */
	public boolean importCardBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".importCardBatchUpdate";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新卡板出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量更新卡板,卡板入库使用
	 */
	public boolean importCardBatchUpdate1(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPROT_BASIC_DATA_SQL_MAP1 + ".updateCardInfoByNe";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新卡板出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新卡板出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 根据板卡批量更新端口名称
	 */
	public boolean updatePtpNameByCard(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = IMPORT_PON + ".updatePtpNameByCard";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"根据板卡批量更新端口名称出错，请您联系管理员！", e);
			throw new Exception(excelName+"根据板卡批量更新端口名称出错，请您联系管理员！");
		}
	    return res;
	}
	/**
	 * 批量导入
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean importBatchInsert(List list, String excelName) throws Exception {
		String sqlAddress = "";
		if (excelName.equals(Constant.ACCESSPOINTNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP2 + ".insertAccesspoint";
		} else if (excelName.equals(Constant.OLTNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertOltInfo";
		} else if (excelName.equals(Constant.POSNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertPosInfo";
		} else if (excelName.equals(Constant.ONUNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertOnuInfo";
		} else if (excelName.equals(Constant.TOPONAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP3 + ".insertPonTopoInfo";
		} else if (excelName.equals(Constant.CARDDB)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertCardInfo";
		} else if (excelName.equals(Constant.PORTDB)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertPtpInfo";
		}else if(excelName.equals("覆盖范围")){
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".insertCoverageInfo";
		}else if(excelName.equals("客户信息导入模板")){
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".insertCustomerInfo";
		}
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientInsert(list, sqlAddress));
		} catch (Exception e) {
			LogHome.getLog().error("批量添加出错，请您联系管理员！", e);
			throw new Exception("批量添加出错，请您联系管理员！");
		}
		return false;
	}
	
	/**
	 * pon_way信息表批量导入
	 * 
	 */
	public boolean importPonWayInsert(List list) throws Exception {
		String sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0+".insertPonWayInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientInsert(list, sqlAddress));
		} catch (Exception e) {
//			e.printStackTrace();
			LogHome.getLog().error("批量添加pon_way表出错！", e);
			throw new Exception("批量添加pon_way出错，请您联系管理员！");
		}
		return false;
	}
	/**
	 * 判断该业务的宽带账号是否在库中存在
	 */
	public List accountNameIsExsit(String accountName) throws Exception {
		String sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0+".queryAccountName";
		try {
			List list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(sqlAddress,accountName);
			return list;
		} catch (Exception e) {
//			e.printStackTrace();
			LogHome.getLog().error("批量添加pon_way表出错！", e);
			throw new Exception("批量添加pon_way出错，请您联系管理员！");
		}
	}
	
	/**
	 * 批量修改
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean importOltBatchUpdate(List list, String excelName) throws Exception {
		String sqlAddress = "";
		if (excelName.equals(Constant.ACCESSPOINTNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateImportAccesspoint";
		} else if (excelName.equals(Constant.OLTNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateImportOltInfo";
		} else if (excelName.equals(Constant.POSNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".updatePosInfo";
		} else if (excelName.equals(Constant.ONUNAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP1 + ".updateOnuInfo";
		} else if (excelName.equals(Constant.TOPONAME)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateImportPonTopoInfo";
		} else if (excelName.equals(Constant.CARDDB)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateImportCardInfo";
		} else if (excelName.equals(Constant.PORTDB)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateImportPtpInfo";
		}else if (excelName.equals("覆盖范围")) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateCoverageInfo";
		}else if (excelName.equals("客户信息导入模板")) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateCustomerInfo";
		}else if (excelName.equals(Constant.OLTMAINTAIN)) {
			sqlAddress = IMPROT_BASIC_DATA_SQL_MAP0 + ".updateOltMaintainInfo";
		}
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sqlAddress));
		} catch (Exception e) {
			LogHome.getLog().error("批量修改出错，请您联系管理员！", e);
			throw new Exception("批量修改出错，请您联系管理员！");
		}
		return false;
	}
	
	public void updatePortState(List<String> ports,String state){
		String sql = "1".equals(state) ? "updatePortStateFree":"updatePortStateOccu";
	
		this.IbatisDAO.getSqlMapClientTemplate().update(IMPROT_BASIC_DATA_SQL_MAP1+"." + sql  , ports);
	}
	
	public void insertCardInfo(Map map) throws Exception{
		String sql = IMPROT_BASIC_DATA_SQL_MAP1 + ".insertCardInfo";
		List list=new ArrayList();
		list.add(map);
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientInsert(list, sql));
		} catch (Exception e) {
			LogHome.getLog().error("批量添加出错，请您联系管理员！", e);
			throw new Exception("批量添加出错，请您联系管理员！");
		}
			
	}
	public void updateCardInfo(Map map) throws Exception{
		String sql = IMPROT_BASIC_DATA_SQL_MAP1 + ".updateCardInfo";
		List list=new ArrayList();
		list.add(map);
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientInsert(list, sql));
		} catch (Exception e) {
			LogHome.getLog().error("批量添加出错，请您联系管理员！", e);
			throw new Exception("批量添加出错，请您联系管理员！");
		}	
	}

	public List queryCardCuid(String cuid) throws Exception {
		String sql = IMPROT_BASIC_DATA_SQL_MAP0+".queryCardCuid";
		Map<String,String> map=new HashMap<String,String>();
		map.put("CUID", cuid);
		try {
			List list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(sql,map);
			return list;
		} catch (Exception e) {
			LogHome.getLog().error("批量添加出错，请您联系管理员！", e);
			throw new Exception("批量添加出错，请您联系管理员！");
		}
	}

	/**
	 * 注入DAO
	 */
	private IbatisDAO IbatisDAO;

	/**
	 * @return the ibatisDAO
	 */
	public IbatisDAO getIbatisDAO() {
		return IbatisDAO;
	}

	/**
	 * @param ibatisDAO
	 *            the ibatisDAO to set
	 */
	public void setIbatisDAO(IbatisDAO ibatisDAO) {
		IbatisDAO = ibatisDAO;
	}

	/**
	 * 时间入库转型
	 * 
	 * @param time
	 */
	public Timestamp formtTime(String time) {
		try{
			if (!StringHelper.isEmpty(time)) {
				return Timestamp.valueOf(time);
			}
		}catch(Exception ex){
			LogHome.getLog().error("");
		}
		return null;
	}

//	/**
//	 * 根据机房的名称查找到机房站点，地域。。。。。
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public Map setRelatedSpaceCuid(String name) {
//		Map map = new HashMap();
//		List list = null;
//		String roomCuid = "";
//		String siteCuid = "";
//		String districtCuid = "";
//		if (!StringHelper.isEmpty(name)) {
//			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
//					IMPROT_BASIC_DATA_SQL_MAP0 + ".getRelatedRoom", name);
//			roomCuid = list.get(0).toString();
//			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
//					IMPROT_BASIC_DATA_SQL_MAP1 + ".getRelatedSite", roomCuid);
//			if(list == null || list.isEmpty() || !list.get(0).toString().startsWith("SITE")){
//				list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
//						IMPROT_BASIC_DATA_SQL_MAP1 + ".getRelatedSpace", roomCuid);
//			}//无值不操作
//			if(list != null && list.size()>0){
//				siteCuid = list.get(0).toString();
//				list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
//						IMPROT_BASIC_DATA_SQL_MAP1 + ".getRelatedDistrict", siteCuid);
//				districtCuid = list.get(0).toString();
//				map.put(TransElement.AttrName.relatedRoomCuid, roomCuid);
//				map.put(TransElement.AttrName.relatedSiteCuid, siteCuid);
//				map.put(TransElement.AttrName.relatedDistrictCuid, districtCuid);
//			}
//		}//无值不操作
//		return map;
//	}

	/**
	 * 查询到EMS的fdn
	 * 
	 * @param fdnCuid
	 * @return
	 */
	public String getEmsFdn(String fdnCuid) {
		if (!StringHelper.isEmpty(fdnCuid)) {
			List<String> list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
					IMPROT_BASIC_DATA_SQL_MAP0 + ".queryEmsName", fdnCuid);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				String emsFdn = list.get(0).toString();
				return emsFdn;
			}
		}
		return null;
	}

	/**
	 * 生成Olt的fdn
	 * 
	 * @param emsName
	 *            传来的Ems名称
	 * @param name
	 *            OLt的名称
	 * @return
	 */
	public Map oltFdn(String emsName, String name) {
		// 根据EMS的FDN和OLT的LABEL_CN拼出OLT的FDN
		Map map = queryEmsByName(emsName);
		Map returnMap = new HashMap();
		String emsFdn = "";
		if (map != null) {
			emsFdn = map.get("FDN").toString();
		}
		emsFdn = "EMS=" + emsFdn + ":ManagedElement=" + name;
		returnMap.put(TransElement.AttrName.fdn, emsFdn);
		return returnMap;
	}

	/**
	 * Ems通过名称查询cuid
	 * 
	 * @param Name
	 */
	public Map<String, String> queryEmsByName(String Name) {
		List isList = null;
		Map<String, String> map = null;
		List list = null;
		if (!StringHelper.isEmpty(Name)) {
			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
					IMPROT_BASIC_DATA_SQL_MAP0 + ".queryEmsName", Name);
			if (list != null && list.size() > 0) {
				map = (Map) list.get(0);
			}
			return map;
		} else {
			return null;
		}
	}

	/**
	 * Vendor通过名称查询cuid
	 * 
	 * @param Name
	 */
	public Map<String, String> queryVendorByName(String Name) {
		List isList = null;
		Map<String, String> map = null;
		List list = null;
		if (!StringHelper.isEmpty(Name)) {
			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
					IMPROT_BASIC_DATA_SQL_MAP0 + ".queryDeviceVendor", Name);
			if (list != null && list.size() > 0) {
				map = (Map) list.get(0);
			}
			return map;
		} else {
			return null;
		}
	}

	/**
	 * Model通过名称查询cuid
	 * 
	 * @param Name
	 */
	public Map<String, String> queryModelByName(String Name) {
		List isList = null;
		Map<String, String> map = null;
		List list = null;
		if (!StringHelper.isEmpty(Name)) {
			list = this.IbatisDAO.getSqlMapClientTemplate().queryForList(
					IMPROT_BASIC_DATA_SQL_MAP0 + ".queryNeModelCfgType", Name);
			if (list != null && list.size() > 0) {
				map = (Map) list.get(0);
			}
			return map;
		} else {
			return null;
		}
	}
	
	public Map checkNeName(String neName, String neTypeStr) throws Exception {
		String queryDb = "";
		List<String> neNameList = new ArrayList<String>();
		neNameList.add(neName);
		if (neTypeStr.equals("OLT设备")) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryOltName";
		}  else if (neTypeStr.equals("ONU设备")) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryOnuName";
		} else if (neTypeStr.equals("分光器")) {
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryPosName";
		}else if(neTypeStr.equals("客户")){
			queryDb = IMPROT_BASIC_DATA_SQL_MAP0 + ".queryCustomerByName";
		}
		try {
				List subList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(queryDb,neNameList);
				if(subList != null && !subList.isEmpty()){
					return (Map)subList.get(0);
				}
		} catch (Exception e) {
			e.printStackTrace();
//			LogHome.getLog().error("与数据库校验判断名称是否存在出错，请您联系管理员！", e);
//			throw new Exception("与数据库校验判断名称是否存在出错，请您联系管理员！");
		}
		return null;
	}
	
	public String queryDistrictByLabelcn(String districtName) throws Exception{
		List subList = this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP0+".quertyDistrictCuidByName", districtName);
		if(subList != null && !subList.isEmpty()){
			return subList.get(0).toString();
		}else{
			return null;
		}
	}

	public List<Map> queryAZSiteBylabelcn(String labelCn) throws Exception{
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryAZSiteBylabelcn", labelCn);
	}
	public List<Map> queryADevBylabelcn(String labelCn) throws Exception{
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryADevBylabelcn", labelCn);
	}
	public List<Map> queryAPortBylabelcnAndDevCuid(String labelCn,String devCuid) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("labelCn", labelCn);
		param.put("devCuid", devCuid);
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryAPortBylabelcnAndDevCuid", param);
	}
	public List<Map> queryZDevBylabelcn(String labelCn) throws Exception{
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryZDevBylabelcn", labelCn);
	}
	public List<Map> queryZPortBylabelcnAndDevCuid(String labelCn,String devCuid) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("labelCn", labelCn);
		param.put("devCuid", devCuid);
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryZPortBylabelcnAndDevCuid", param);
	}
	public List<Map> queryGponLinkBylabelcns(Set<String> labelCns) throws Exception{
		List<String> param = new ArrayList<String>();
		param.addAll(labelCns);
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryGponLinkBylabelcns", param);
	}
	
	public List<Map> queryLinkTypeByLabelCn(String labelCn){
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryLinkTypeByLabelCn", labelCn);
	}
	
	public List<Map> queryBandWidthByLabelCn(String labelCn){
		return this.IbatisDAO.getSqlMapClientTemplate().queryForList(IMPROT_BASIC_DATA_SQL_MAP4+".queryBandWidthByLabelCn", labelCn);
	}
	
	public int updateGponLink(Map obj) throws Exception{
		return this.IbatisDAO.getSqlMapClientTemplate().update(IMPROT_BASIC_DATA_SQL_MAP4+".updateGponDeviceLinkInfo", obj);
	}
	public Object insertGponLink(Map obj) throws Exception{
		this.IbatisDAO.getSqlMapClientTemplate().insert(IMPROT_BASIC_DATA_SQL_MAP4 + ".insertGponCitcuitkInfo", obj);
		return this.IbatisDAO.getSqlMapClientTemplate().insert(IMPROT_BASIC_DATA_SQL_MAP4+".insertGponDeviceLinkInfo", obj);
		
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void organizeLinkData(Map<String,Object> map){
		String origDeviceCuid = ObjectUtils.toString(map.get("RELATED_ORIG_LOGIC_CUID"));
		String destDeviceCuid = ObjectUtils.toString(map.get("RELATED_DEST_LOGIC_CUID"));
		String origPortCuid = ObjectUtils.toString(map.get("RELATED_ORIG_PORT_CUID"));
		String destPortCuid = ObjectUtils.toString(map.get("RELATED_DEST_PORT_CUID"));
		
		Map<String,String> origInfo = (HashMap<String,String>)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(IMPROT_BASIC_DATA_SQL_MAP4 + ".queryOrigInfo",origDeviceCuid );
		Map<String,String> destInfo = (HashMap<String,String>)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(IMPROT_BASIC_DATA_SQL_MAP4 + ".queryDestInfo", destDeviceCuid);
		Map<String,String> origPortInfo = (HashMap<String,String>)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(IMPROT_BASIC_DATA_SQL_MAP4 + ".queryOrigPortLabelCnByCuid",origPortCuid );
		Map<String,String> destPortInfo = (HashMap<String,String>)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(IMPROT_BASIC_DATA_SQL_MAP4 + ".queryDestPortLabelCnByCuid",destPortCuid );
		
		String LabelCn = origInfo.get("LABEL_CN")+"("+origPortInfo.get("LABEL_CN")+")-"+destInfo.get("LABEL_CN")+"("+destPortInfo.get("LABEL_CN")+")";
		
		map.put("RELATED_A_BMCLASS_CUID", origInfo.get("RELATED_A_BMCLASS_CUID"));
		map.put("RELATED_ORIG_DIST_CUID", origInfo.get("RELATED_ORIG_DIST_CUID"));
		map.put("RELATED_A_SITE", origInfo.get("RELATED_A_SITE"));
		map.put("RELATED_DEST_DIST_CUID", destInfo.get("RELATED_DEST_DIST_CUID"));
		map.put("RELATED_Z_SITE", destInfo.get("RELATED_Z_SITE"));
		map.put("RELATED_Z_BMCLASS_CUID", destInfo.get("RELATED_Z_BMCLASS_CUID"));
		map.put("RELATED_BMCLASSTYPE_CUID","T_LOGIC_LINK_IP_TRUNK");
		map.put("LABEL_CN", LabelCn);
	}
	
	public void updateOltNetDomain(Map link){
		String orgBmclassCuid = ObjectUtils.toString(link.get("RELATED_A_BMCLASS_CUID"));
		if("T_LOGIC_CITYNET_AR".equals(orgBmclassCuid)||"T_LOGIC_CITYNET_BR".equals(orgBmclassCuid)||"T_LOGIC_CITYNET_CR".equals(orgBmclassCuid)){
			String swCuid = ObjectUtils.toString(link.get("RELATED_ORIG_LOGIC_CUID"));
			String OLTCuid = ObjectUtils.toString(link.get("RELATED_DEST_LOGIC_CUID"));
			Assert.isTrue(OLTCuid!=null&&!"".equals(OLTCuid),"链路数据中没有找到OLT的值！");
			Map swNetDomainMap = (Map)this.IbatisDAO.getSqlMapClientTemplate().queryForObject(IMPROT_BASIC_DATA_SQL_MAP4 + ".queryNetDaimCuidBySwCuid", swCuid);
			if(swNetDomainMap!=null){
				String domainCuid = ObjectUtils.toString(swNetDomainMap.get("RELATED_NET_DOMAIN_CUID"));
				if(!"".equals(domainCuid)){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("domainCuid", domainCuid);
					map.put("OLTCuid", OLTCuid);
					this.IbatisDAO.getSqlMapClientTemplate().update(IMPROT_BASIC_DATA_SQL_MAP4 + ".updateOltNetDomainByOltCuid", map);
				}
		     }
		}
   }
	/**
	 * 批量更新PON拓扑连接数据,导入入库使用
	 */
	public boolean importPonTopoLinkBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = PONTOPOLONK_SQL_MAP + ".updatePonTopoInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入PON拓扑连接数据,导入入库使用
	 */
	public boolean importPonTopoLinkBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = PONTOPOLONK_SQL_MAP + ".insertPonTopoInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量更新标准地址数据,导入入库使用
	 */
	public boolean importAddressBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = FULLADDRESS_SQL_MAP + ".updateAddressInfo";
		String sql2 = FULLADDRESS_SQL_MAP + ".updateGponCoverByAddress";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
			//级联更新覆盖范围
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql2));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入标准地址数据,导入入库使用
	 */
	public boolean importAddressBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = FULLADDRESS_SQL_MAP + ".insertAddressInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(
					new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量覆盖范围数据,导入入库使用
	 */
	public boolean importGponCoverBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = GPONCOVER_SQL_MAP + ".updateGponCoverForExcel";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入覆盖范围数据,导入入库使用
	 */
	public boolean importGponCoverBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = GPONCOVER_SQL_MAP + ".insertGponCoverForExcel";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	
	/**
	 * 批量更新业务区数据,导入入库使用
	 */
	public boolean importBusinessCommunityBatchUpdate(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = BUSINESSCOMMUNITY_SQL_MAP + ".updateBCInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量更新出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量更新出错，请您联系管理员！");
		}
		return res;
	}
	/**
	 * 批量插入业务区数据,导入入库使用
	 */
	public boolean importBusinessCommunityBatchInsert(List list, String excelName) throws Exception {
		boolean res = true;
		String sql1 = BUSINESSCOMMUNITY_SQL_MAP + ".insertBCInfo";
		try {
			this.IbatisDAO.getSqlMapClientTemplate().execute(new BatchSqlMapClientUpdate(list, sql1));
		} catch (Exception e) {
			res = false;
			LogHome.getLog().error(excelName+"批量添加出错，请您联系管理员！", e);
			throw new Exception(excelName+"批量添加出错，请您联系管理员！");
		}
		return res;
	}
	


}

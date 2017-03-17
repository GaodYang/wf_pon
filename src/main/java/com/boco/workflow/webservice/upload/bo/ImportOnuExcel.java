package com.boco.workflow.webservice.upload.bo;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.common.util.debug.LogHome;
import com.boco.core.bean.SpringContextUtil;
import com.boco.core.ibatis.dao.IbatisDAO;
import com.boco.core.spring.SysProperty;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;


/**
 * Onu设备信息导出到Excel
 */
@SuppressWarnings({"rawtypes","unchecked","unused"})
@Service
public class ImportOnuExcel {

	@Autowired
	private ImportBasicDataBO basicDataBO;
	List onuList = null;
	//校验模板列名称是否规范
	public static String[] onuExcelColumns = new String[]{
		/* 00 */	"中文名称",
		/* 01 */	//"PBOSS系统ONU名称",
		/* 02 */	"ONU名称",
		/* 03 */	"安装位置",
		/* 04 */	"所属EMS/SNMS",
		/* 05 */	"上联设备",
		/* 06 */	"上联设备端口",
		/* 07 */	"规格型号",
		/* 08 */	"厂家",
		/* 09 */	"接入方式",
		/* 10 */	"ONU类型",
		/* 11 */	"资产归属",
		/* 12 */	"资产归属人",
		/* 13 */	"维护班组",
		/* 14 */	"维护方式",
		/* 15 */	"设备IP地址",
		/* 16 */	"生命周期状态",
		/* 17 */	"软件版本",
		/* 18 */	"接入类型",
		/* 19 */	"ONU_ID号",
		/* 20 */	"ONU的认证类型",
		/* 21 */	"密码",
		/* 22 */	"LOGICID",
		/* 23 */	"MAC地址",
		/* 24 */	"ONU设备SN/MAC",
		/* 25 */	"建设日期",
		/* 26 */	"所属工程"	,
		/* 27 "宽带端口数量", */   
		/* 27  */	"一线数据维护人",
		/* 28  */	"移动公司数据质量责任人"
		
		 
		};
	List onuNameList = null;
	Map nameMap = null;
	Map neModelCuidNameMap;
	private DecimalFormat df = new DecimalFormat ("#.000000");
	
	/**
	 * 有参构造方法
	 * @param neModelCuidNameMap
	 */
	public ImportOnuExcel(Map neModelCuidNameMap){
		this.neModelCuidNameMap = neModelCuidNameMap;
	}

	
	
	/**
	 * 导入Onu基础数据
	 */
	public  ImportResultDO importOnuBasicData(Workbook writeWorkBook ,Sheet writeSheet,String pathname, String excelName) throws Exception {
		ImportResultDO importResultDO = new ImportResultDO(excelName);
		List returnList = null;
		onuList = new ArrayList();
		onuNameList= new ArrayList();
		nameMap=new HashMap();
		try {
			//通过第一行数据获得表格的总列数
			Row xrow=writeSheet.getRow(0);
			int lastColumns =xrow.getLastCellNum();
			//检查导入文件表头信息
			List<String> headErrorInfoList = new ArrayList<String>();
			Map headingMap = getOnuHeadingMap(writeSheet, 0, headErrorInfoList,lastColumns);	
			//表格头部有问题,返回异常信息
			if(!headErrorInfoList.isEmpty()){
				importResultDO.setInfo(headErrorInfoList);
				importResultDO.setSuccess(false);
				importResultDO.setShowFileUrl(false);
				return importResultDO;
			}
			// 取得每一列对应的列号，然后进行校验判断。			
			int relatedPosCuid = Integer.parseInt(headingMap.get("上联设备").toString());
			int labelCn = Integer.parseInt(headingMap.get(Constant.ONU_LABELCN).toString());
			int lastRows = writeSheet.getLastRowNum();
			List portNameList = new ArrayList();
			for (int i = 5; i <= lastRows; i++) {
				Row xRow = writeSheet.getRow(i);
				if(xRow != null){
					Object name=xRow.getCell(labelCn);
					if(name != null && !"".equals(name.toString().trim())){
						
						Map verificationMap = verificationOnuCell( writeWorkBook,  writeSheet,xRow ,headingMap,i,lastColumns,lastRows,portNameList);
						if(ImportCommonMethod.isExistFalse(writeWorkBook, writeSheet, lastColumns,lastRows)==0){
							addOnuModel(xRow , headingMap, i,verificationMap);
						}
					}else{
						int labelCnInt = Integer.parseInt(headingMap.get(Constant.ONU_LABELCN).toString());
						String constantValue=Constant.ONU_LABELCN;
						ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, labelCnInt, i, lastColumns,constantValue);
					}
				}
			}
			
			int falseExitDB = ImportCommonMethod.isExistFalse(writeWorkBook, writeSheet, lastColumns,lastRows);
			importResultDO.setInfo("导入表格中共有:" + (lastRows-4) + "条数据");
			importResultDO.setSuccess(true);
			if(falseExitDB>0){
				importResultDO.setSuccess(false);
				importResultDO.setInfo("有"+falseExitDB+"条错误数据,数据未进行导入.");
			}else{
				if (!onuList.isEmpty()) {
					returnList = basicDataBO.isExistListName(onuNameList,excelName);	
					Map map = ImportCommonMethod.changeListToMap(returnList);
					importResultDO.setInfo("成功导入了"+onuList.size()+"条数据");
					ImportCommonMethod.ImportExcelToDB (map , onuList, excelName, control, importResultDO);
				}	
			}
		} catch (Exception e) {
			importResultDO.setInfo("导入失败");
			LogHome.getLog().error(excelName+"导入失败", e);			
			throw new Exception(excelName+"导入失败"+e.getMessage());
		}
		return importResultDO;
	}
	
	/**
	 * Onu单元格校验
	 */
	private  Map verificationOnuCell(Workbook writeWorkBook, Sheet writeSheet,Row xRow ,Map headingMap , int i ,int lastColumns ,int lastRows,List portNameList ) throws Exception{
		Map map = new HashMap();
		// 01.PBOSS系统ONU名称
		//int standardName = Integer.parseInt(headingMap.get(Constant.PBOSSONUTANDARDNAME).toString());
		// 02.ONU名称
		int labelCn = Integer.parseInt(headingMap.get(Constant.ONU_LABELCN).toString());
		// 03.安装位置
		int relatedAccessPoint = Integer.parseInt(headingMap.get(Constant.RELATEDACCESSPOINT).toString());
		// 04.所属EMS/SNMS
		int relatedEmsCuid = Integer.parseInt(headingMap.get(Constant.RELATEDEMSCUID).toString());
		// 05.归属分光器
		int relatedPosCuid = Integer.parseInt(headingMap.get("上联设备").toString());
		// 06.归属分光器端口
		int relatedPosPortCuid = Integer.parseInt(headingMap.get("上联设备端口").toString());
		// 07.设备型号
		int model = Integer.parseInt(headingMap.get("规格型号").toString());
		// 08.生产厂商
		int relatedVendorCuid = Integer.parseInt(headingMap.get("厂家").toString());			
		// 09.接入方式
		int fttx = Integer.parseInt(headingMap.get(Constant.FTTX).toString());
		// 10.ONU类型
		int onuType = Integer.parseInt(headingMap.get(Constant.ONUTYPE).toString());
		// 11.产权
		int ownership = Integer.parseInt(headingMap.get("资产归属").toString());			
		// 12.产权人
		int ownershipMan = Integer.parseInt(headingMap.get("资产归属人").toString());
		// 13.维护人
		int preserver = Integer.parseInt(headingMap.get("维护班组").toString());
		// 14.维护方式 	在下面声明maintMode
		// 15.设备IP地址
		int devIp = Integer.parseInt(headingMap.get(Constant.DEVIP).toString());
		// 16.生命周期状态
		int liveCycle = Integer.parseInt(headingMap.get(Constant.LIVECYCLE).toString());
		// 17.软件版本
		int softVersion = Integer.parseInt(headingMap.get(Constant.SOFTVERSION).toString());
		// 18.接入类型
		int accessType = Integer.parseInt(headingMap.get(Constant.ACCESSTYPE).toString());
		// 19.ONU_ID号
		int onuId = Integer.parseInt(headingMap.get(Constant.ONUID).toString());
		// 20.ONU的认证类型
		int authType = Integer.parseInt(headingMap.get(Constant.AUTHTYPE).toString());
		// 21.密码
		int password = Integer.parseInt(headingMap.get(Constant.PASSWORD).toString());
		// 22.LOGICID
		int logicId = Integer.parseInt(headingMap.get(Constant.LOGICID).toString());
		// 23.MAC地址
		int macAddress = Integer.parseInt(headingMap.get(Constant.MACADDRESS).toString());
		// 24.SN码
		int seqNo = Integer.parseInt(headingMap.get("ONU设备SN/MAC").toString());
		// 25.建设日期
		int creatTime = Integer.parseInt(headingMap.get(Constant.CREATTIME).toString());
		// 26.所属工程
		int relatedProjectCuid = Integer.parseInt(headingMap.get(Constant.RELATEDPROJECTCUID).toString());
		/*// 26.宽带端口数量
		int PORT_NUMBER = Integer.parseInt(headingMap.get("宽带端口数量").toString());*/
		//27.一线数据维护人
		int MAINT_PERSON = Integer.parseInt(headingMap.get("一线数据维护人").toString());
		//28.移动公司数据质量责任人
		int DATA_QUALITY_PERSON = Integer.parseInt(headingMap.get("移动公司数据质量责任人").toString());
		/*int relatedSpaceCuid = Integer.parseInt(headingMap.get(Constant.ROOMACCESSPOINT).toString());
		int location = Integer.parseInt(headingMap.get(Constant.LOCATION).toString());
		int realLongitude = Integer.parseInt(headingMap.get(Constant.REALLONGITUDE).toString());
		int realLatitude = Integer.parseInt(headingMap.get(Constant.REALLATITUDE).toString());
		int setUpTime = Integer.parseInt(headingMap.get(Constant.SETUPTIME).toString());
		int backNetWorkTime = Integer.parseInt(headingMap.get(Constant.BACKNETWORKTIME).toString());*/
		
		try {
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, labelCn, i, lastColumns,Constant.ONU_LABELCN);
			String onuCuid = "";
			String onuName = xRow.getCell(labelCn)==null?"":xRow.getCell(labelCn).toString();
			if(!ImportCommonMethod.isEmpty(onuName)){
				Map temp = getImportAttributeQueryBO().getOnuByName(onuName);
				if(temp!=null){
					onuCuid = ObjectUtils.toString(temp.get("CUID"));
				}
			}
			// 01.PBOSS系统ONU名称
			//ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, standardName, i, lastColumns,Constant.PBOSSONUTANDARDNAME);
			// 02.ONU名称
			ImportCommonMethod.isExcelExist(writeWorkBook, writeSheet, labelCn, i, lastColumns,lastRows,nameMap);
			// 03.安装位置
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedAccessPoint, i, lastColumns,Constant.RELATEDCABCUID);
			verificationRelated(writeWorkBook, writeSheet, relatedAccessPoint, i, lastColumns, Constant.RELATEDACCESSPOINT, xRow.getCell(relatedAccessPoint).toString());
			// 04.所属EMS/SNMS
			Object cellContant=xRow.getCell(relatedEmsCuid);
			String emsName = "";
			if(cellContant != null && !StringHelper.isEmpty(cellContant.toString())){
				emsName = cellContant.toString();
			}
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedEmsCuid, i, lastColumns,Constant.RELATEDEMSCUID);
			verificationRelated(writeWorkBook, writeSheet,  relatedEmsCuid, i, lastColumns,Constant.RELATEDEMSCUID,emsName);
			// 05.归属分光器
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedPosCuid, i, lastColumns,"上联设备");
			String posCuid = "";
			Map posTemp = verificationRelated(writeWorkBook, writeSheet,  relatedPosCuid, i, lastColumns,"上联设备",xRow.getCell(relatedPosCuid).toString());
			if(posTemp!=null){
				posCuid = ObjectUtils.toString(posTemp.get("CUID"));
			}
			// 06.归属分光器端口
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedPosCuid, i, lastColumns,"上联设备端口");
			if (xRow.getCell(relatedPosPortCuid) !=null && xRow.getCell(relatedPosPortCuid) !=null) {
				//  归属分光器和归属分光器端口 是否有关联关系
				if(portNameList.contains(xRow.getCell(relatedPosPortCuid).toString())){
					ImportCommonMethod.VerificationCardNameRepeat(writeWorkBook, writeSheet,relatedPosPortCuid, i, lastColumns);
				} else {
					portNameList.add(xRow.getCell(relatedPosPortCuid).toString());
					if(!ImportCommonMethod.isEmpty(posCuid)){
						Map ptpMap = ImportCommonMethod.verificationRelatedPosPort(writeWorkBook, writeSheet,posCuid,relatedPosCuid, relatedPosPortCuid, i, lastColumns,Constant.RELATEDPOSPORTCUID1);
						if(ptpMap!=null){
					    	String portSubType = ObjectUtils.toString(ptpMap.get("PORT_SUB_TYPE"));
					    	String posPtpCuid = ObjectUtils.toString(ptpMap.get("CUID"));
					    	if("12".equals(portSubType)){
					    		ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, relatedPosPortCuid, lastColumns, "归属分光器端口是分光器上联口，请重新填写归属分光器的下联端口！");
					    	}else{
					    		//验证上联设备和端口是否已经存在链路
					    		if(!ImportCommonMethod.isEmpty(posPtpCuid)&&!ImportCommonMethod.isEmpty(posCuid)){
					    			map.put("RELATED_POS_PORT_CUID", posPtpCuid);
					    			map.put("RELATED_POS_CUID", posCuid);
//					    			Map<String,String> param =new HashMap<String,String>();
//					    			param.put("DEST_NE_CUID",posCuid);
//					    			param.put("DEST_PTP_CUID",posPtpCuid);
//					    			if(!ImportCommonMethod.isEmpty(onuCuid)){
//					    				param.put("NOT_ORIG_NE_CUID",onuCuid);
//					    			}
//					    			if(getPonTopoLinkManageBo().isPonTopoLinkExist(param)){
//					    				ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, relatedPosCuid, lastColumns, "归属分光器+归属分光器端口已存在PON拓扑链路信息，请重新填写！");
//					    			}
					    		}
					    	}
					    }
					}
				}
			}
			//所属机房/资源点 可空
			/*ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedSpaceCuid, i, lastColumns,Constant.ROOMACCESSPOINT);
			//如果机房/资源点不为空，判断是在数据库存在
			if(xRow.getCell(relatedSpaceCuid)!=null&& !StringHelper.isEmpty(xRow.getCell(relatedSpaceCuid).toString())){
				verificationRelated(writeWorkBook, writeSheet,  relatedSpaceCuid, i, lastColumns,Constant.ROOMACCESSPOINT,xRow.getCell(relatedSpaceCuid).toString());						
			}
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedUpPortCuid, i, lastColumns,Constant.RELATEDUPPORTCUID);
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);
			// 设备安装地址 是否在数据库中存在
			String locationStr  = xRow.getCell(location)==null?"":xRow.getCell(location).toString();
			if(locationStr!= null && locationStr.trim().length() > 0) {
				ImportCommonMethod.verificationAddressNew(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);
			}
			ImportCommonMethod.verificationAddress(writeWorkBook, writeSheet, location, i, lastColumns,Constant.LOCATION);
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedOltCuid, i, lastColumns,Constant.RELATEDOLTCUID);*/
			// 07.设备型号		08.生产厂商
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, model, i, lastColumns,"规格型号");
			verificationRelated(writeWorkBook, writeSheet,  model, i, lastColumns,"规格型号",xRow.getCell(model).toString());
			if (xRow.getCell(devIp) !=null && !"".equals( xRow.getCell(devIp).toString().trim())) {
				ImportCommonMethod.verificationIp(writeWorkBook, writeSheet, devIp, i, lastColumns,Constant.DEVIP);
			}
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, relatedVendorCuid, i, lastColumns,"厂家");
			//校验生产厂商和设备型号直接的关系 2016年4月14日19:33:55
			String producterStr=writeSheet.getRow(i).getCell(relatedVendorCuid).getStringCellValue();
			String modelStr=writeSheet.getRow(i).getCell(model).getStringCellValue();
			List list = getImportAttributeQueryBO().getProducterAndModelRelated(producterStr,modelStr,2);
			if(list==null||list.size()==0){
				ImportCommonMethod.updateExcelFormat(writeWorkBook,writeSheet,i, relatedVendorCuid);
				Cell cell=xRow.getCell(lastColumns);
				if(cell==null){
					cell=xRow.createCell(lastColumns);
					cell.setCellValue("生产厂商和设备型号关系错误 \r\n");
				}else{
					String strValue=cell.getStringCellValue();
					strValue=strValue + "生产厂商和设备型号关系错误 \r\n";
					cell.setCellValue(strValue);				
				}
			}
			verificationRelated(writeWorkBook, writeSheet,  relatedVendorCuid, i, lastColumns,"厂家",xRow.getCell(relatedVendorCuid).toString());
			// 09.接入方式
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, fttx, i, lastColumns,Constant.FTTX);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, fttx, i, lastColumns,Constant.FTTX);
			// 10.ONU类型
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, onuType, i, lastColumns,Constant.ONUTYPE);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, onuType, i, lastColumns,Constant.ONUTYPE);
			// 11.产权
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, ownership, i, lastColumns,"资产归属");
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, ownership, i, lastColumns,Constant.OWNERSHIP);
			// 12.产权人
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, ownershipMan, i, lastColumns,"资产归属人");
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, ownershipMan, i, lastColumns,Constant.OWNERSHIPMAN);
			// 13.维护人	可空
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, preserver, i, lastColumns,Constant.PRESERVER);
			if(xRow.getCell(preserver)!=null && !"".equals(xRow.getCell(preserver).toString())){

				 String emps = xRow.getCell(preserver).toString();
				 List empreserver = getAnPosManageBO().getEmPreservert(emps);
						if (empreserver==null && empreserver.size()<=1){
							ImportCommonMethod.printOnlyErrorInfo(writeWorkBook, writeSheet, i, preserver, lastColumns,"维护班组的信息在EOMS数据库中不存在！");
						}
				}
			// 14.维护方式
			if(headingMap.get(Constant.MAINTMODE) !=null){
				int maintMode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
				ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);	
			}
			// 15.设备IP地址	可空
			// 16.生命周期状态
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, liveCycle, i, lastColumns,Constant.LIVECYCLE);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, liveCycle, i, lastColumns,Constant.LIVECYCLE);
			// 17.软件版本		可空
			// 18.接入类型		可空
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, accessType, i, lastColumns,Constant.ACCESSTYPE);
			if (xRow.getCell(accessType) !=null && !"".equals( ObjectUtils.toString(xRow.getCell(accessType)))) {	
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, accessType, i, lastColumns,Constant.ACCESSTYPE);
			}
			// 19.ONU_ID号	可空
			// 20.ONU的认证类型
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, authType, i, lastColumns,Constant.AUTHTYPE);
			ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, authType, i, lastColumns,Constant.AUTHTYPE);
			// 21.密码
			// 22.LOGICID
			// 23.MAC地址
			// 24.SN码
			// 25.建设日期
			if (xRow.getCell(creatTime) !=null && !"".equals( xRow.getCell(creatTime).toString())) {				
				ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, creatTime, i, lastColumns,Constant.CREATTIME);
			}
			// 26.所属工程		可空
			
			/*ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, realLongitude, i, lastColumns,Constant.REALLONGITUDE);
			if (xRow.getCell(realLongitude) !=null && !"".equals( xRow.getCell(realLongitude).toString())) {
				ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, realLongitude, i, lastColumns,Constant.REALLATITUDE);
			}	
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, realLatitude, i, lastColumns,Constant.REALLATITUDE);
			if (xRow.getCell(realLatitude) !=null && !"".equals( xRow.getCell(realLatitude).toString())) {
				ImportCommonMethod.verificationNumber(writeWorkBook, writeSheet, realLatitude, i, lastColumns,Constant.REALLATITUDE);
			}
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, setUpTime, i, lastColumns,Constant.SETUPTIME);
			ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, setUpTime, i, lastColumns,Constant.SETUPTIME);
			if (xRow.getCell(backNetWorkTime) !=null && !"".equals( xRow.getCell(backNetWorkTime).toString())) {
				if(xRow.getCell(backNetWorkTime)!=null){
					ImportCommonMethod.verificationDate(writeWorkBook, writeSheet, backNetWorkTime, i, lastColumns,Constant.BACKNETWORKTIME);
				}
			}
			if(headingMap.get(Constant.MAINTMODE) !=null){
				int maintMode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
				ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);
				ImportCommonMethod.verificationEnum(writeWorkBook, writeSheet, maintMode, i, lastColumns,Constant.MAINTMODE);	
			}*/
			
			/*
			 * 27 宽带端口数量
			 */
			//ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, PORT_NUMBER, i, lastColumns,"宽带端口数量");

			/**
			 * 30.一线数据维护人（代维/一线）
			 */
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, MAINT_PERSON, i, lastColumns,"一线数据维护人");
			/**
			 * 31.数据质量责任人（移动）
			 */
			ImportCommonMethod.verificationEmpty(writeWorkBook, writeSheet, DATA_QUALITY_PERSON, i, lastColumns,"移动公司数据质量责任人");
			
		} catch (Exception e) {
			LogHome.getLog().error("接入网-ONU设备校验出错", e);			
			throw new Exception("接入网-ONU设备校验出错："+e.getMessage());
		}
		return map;
	}

	/**
	 * 将Excel数据增加到Onu模型中
	 */
	private void addOnuModel(Row xRow ,Map headingMap,int i,Map verificationMap) throws Exception{
		// 规定时间格式
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 01.PBOSS系统ONU名称
	//	int standardName = Integer.parseInt(headingMap.get(Constant.PBOSSONUTANDARDNAME).toString());
		// 02.ONU名称
		int labelCn = Integer.parseInt(headingMap.get(Constant.ONU_LABELCN).toString());
		// 03.安装位置
		int relatedAccessPoint = Integer.parseInt(headingMap.get(Constant.RELATEDACCESSPOINT).toString());
		// 04.所属EMS/SNMS
		int relatedEmsCuid = Integer.parseInt(headingMap.get(Constant.RELATEDEMSCUID).toString());
		// 05.归属分光器
		int relatedPosCuid = Integer.parseInt(headingMap.get("上联设备").toString());
		// 06.归属分光器端口
		int relatedPosPortCuid = Integer.parseInt(headingMap.get("上联设备端口").toString());
		// 07.设备型号
		int model = Integer.parseInt(headingMap.get("规格型号").toString());
		// 08.生产厂商
		int relatedVendorCuid = Integer.parseInt(headingMap.get("厂家").toString());			
		// 09.接入方式
		int fttx = Integer.parseInt(headingMap.get(Constant.FTTX).toString());
		// 10.ONU类型
		int onuType = Integer.parseInt(headingMap.get(Constant.ONUTYPE).toString());
		// 11.产权
		int ownership = Integer.parseInt(headingMap.get("资产归属").toString());			
		// 12.产权人
		int ownershipMan = Integer.parseInt(headingMap.get("资产归属人").toString());
		// 13.维护人
		int preserver = Integer.parseInt(headingMap.get("维护班组").toString());
		// 14.维护方式
		int maintMode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
		// 15.设备IP地址
		int devIp = Integer.parseInt(headingMap.get(Constant.DEVIP).toString());
		// 16.生命周期状态
		int liveCycle = Integer.parseInt(headingMap.get(Constant.LIVECYCLE).toString());
		// 17.软件版本
		int softVersion = Integer.parseInt(headingMap.get(Constant.SOFTVERSION).toString());			
		// 18.接入类型
		int accessType = Integer.parseInt(headingMap.get(Constant.ACCESSTYPE).toString());
		// 19.ONU_ID号
		int onuId = Integer.parseInt(headingMap.get(Constant.ONUID).toString());
		// 20.ONU的认证类型
		int authType = Integer.parseInt(headingMap.get(Constant.AUTHTYPE).toString());
		// 21.密码
		int password = Integer.parseInt(headingMap.get(Constant.PASSWORD).toString());
		// 22.LOGICID
		int logicid = Integer.parseInt(headingMap.get(Constant.LOGICID).toString());
		// 23.MAC地址
		int macAddress = Integer.parseInt(headingMap.get(Constant.MACADDRESS).toString());						
		// 24.SN码
		int seqNo = Integer.parseInt(headingMap.get("ONU设备SN/MAC").toString());
		// 25.建设日期
		int creatTime = Integer.parseInt(headingMap.get(Constant.CREATTIME).toString());
		// 26.所属工程
		int relatedProjectCuid = Integer.parseInt(headingMap.get(Constant.RELATEDPROJECTCUID).toString());
		// 27 宽带端口数量
		//int PORT_NUMBER = Integer.parseInt(headingMap.get("宽带端口数量").toString());
		//27.一线数据维护人
		int MAINT_PERSON = Integer.parseInt(headingMap.get("一线数据维护人").toString());
		//28.移动公司数据质量责任人
		int DATA_QUALITY_PERSON = Integer.parseInt(headingMap.get("移动公司数据质量责任人").toString());
		
		/*int relatedSpaceCuid = Integer.parseInt(headingMap.get(Constant.ROOMACCESSPOINT).toString());
		int location = Integer.parseInt(headingMap.get(Constant.LOCATION).toString());
		int relatedOltCuid = Integer.parseInt(headingMap.get(Constant.RELATEDOLTCUID).toString());
		int realLongitude = Integer.parseInt(headingMap.get(Constant.REALLONGITUDE).toString());
		int realLatitude = Integer.parseInt(headingMap.get(Constant.REALLATITUDE).toString());
		int setUpTime = Integer.parseInt(headingMap.get(Constant.SETUPTIME).toString());
		int backNetWorkTime = Integer.parseInt(headingMap.get(Constant.BACKNETWORKTIME).toString());*/
		
		//int relatedOltPort = Integer.parseInt(headingMap.get(Constant.RELATEDOLTPORT).toString());
		//int relatedUpPortCuid = Integer.parseInt(headingMap.get(Constant.RELATEDUPPORTCUID).toString());
	
		try {
			Map map = new HashMap();
			String cuid = "";
			Long objectId = AnmsUtilBO.getObjectId(AnOnu.CLASS_NAME);
//			if(xRow.getCell(relatedUpPortCuid)!=null){
//				map.put("aPortName", xRow.getCell(relatedUpPortCuid).toString());
//				map.put("RELATED_UP_PORT_CUID", xRow.getCell(relatedUpPortCuid).toString());
//			}
			if (xRow.getCell(labelCn)!=null && !"".equals(xRow.getCell(labelCn).toString())) {
				// 01.PBOSS系统ONU名称
				//map.put(AnOnu.AttrName.standardName, xRow.getCell(standardName).toString());
				// 02.ONU名称
				// TODO 加入名称校验，如果重复，则去pon_link校验
				String onuName = xRow.getCell(labelCn).toString();
				map.put(AnOnu.AttrName.labelCn, onuName);
				
				if(!ImportCommonMethod.isEmpty(onuName)){
					Map temp = getImportAttributeQueryBO().getOnuByName(onuName);
					if(temp!=null){
						cuid = ObjectUtils.toString(temp.get("CUID"));
					}
				}
				
				map.put("aNodeName", xRow.getCell(labelCn).toString());
				onuNameList.add(onuName);
				nameMap.put(onuName, i);
			}
			// 03.安装位置
			if (xRow.getCell(relatedAccessPoint)!=null && !StringHelper.isEmpty(xRow.getCell(relatedAccessPoint).toString())) {
				String accessPointName=xRow.getCell(relatedAccessPoint).toString();
				String accessPointCuid = getImportAttributeQueryBO().getPropertyCuidByName(accessPointName, "CAB");
				map.put(AnOnu.AttrName.relatedAccessPoint,accessPointCuid);
				if(accessPointCuid!=null &&!StringHelper.isEmpty(accessPointCuid)){
					String relatedDistrictCuid = getImportAttributeQueryBO().getRelatedDistrictCuidByCabCuid(accessPointCuid);
					map.put(AnOnu.AttrName.relatedDistrictCuid,relatedDistrictCuid);
				}
			}
			// 04.所属EMS/SNMS
			if(xRow.getCell(relatedEmsCuid)!=null && !StringHelper.isEmpty(xRow.getCell(relatedEmsCuid).toString())){
				map.put("emsName", xRow.getCell(relatedEmsCuid).toString());
				String emsName = xRow.getCell(relatedEmsCuid).toString();
				Map emsMap = ImportCache.getInstance().getEmsMap(emsName);
//				String relatedDistrictCuid = (String)map.get(AnOnu.AttrName.relatedDistrictCuid);
//				boolean isDistrictEmpty = StringHelper.isEmpty(relatedDistrictCuid);
				if (emsMap != null && !emsMap.isEmpty()) {
					map.put(AnOnu.AttrName.relatedEmsCuid, emsMap.get("CUID"));
//					if(isDistrictEmpty){
//						map.put(AnOnu.AttrName.relatedDistrictCuid, emsMap.get("RELATED_SPACE_CUID"));
//					}
				}
//				else{
//					if(isDistrictEmpty){
//						map.put(AnOnu.AttrName.relatedDistrictCuid, SysProperty.getInstance().getValue("district"));
//					}
//				}
				map.put("NMSNAME", xRow.getCell(relatedEmsCuid).toString());	
			}
			// 05.归属分光器
			if(xRow.getCell(relatedPosCuid)!=null && !"".equals(xRow.getCell(relatedPosCuid).toString())){
				map.put("aName",xRow.getCell(relatedPosCuid).toString());
				Map posMap = getImportAttributeQueryBO().getPosByName(xRow.getCell(relatedPosCuid).toString());
				if(posMap!=null){
					String  posCuid = posMap.get("CUID").toString();
					map.put(AnOnu.AttrName.relatedPosCuid,posCuid);
					// 06.归属分光器端口
					if(xRow.getCell(relatedPosPortCuid)!=null){
						Map ptpMap = getImportAttributeQueryBO().getPtpByNameAndNeCuid(xRow.getCell(relatedPosPortCuid).toString(),posCuid);
						if(ptpMap!=null){
							map.put("aPortName",xRow.getCell(relatedPosPortCuid).toString());
							map.put("RELATED_POS_PORT_CUID",ptpMap.get("CUID"));
						}
					}
				}
			}
			
			// 07.设备型号
			if(xRow.getCell(model)!=null && !"".equals(xRow.getCell(model).toString())){
				map.put(AnOnu.AttrName.model, neModelCuidNameMap.get(xRow.getCell(model).toString()));
			}
			// 08.生产厂商
			if (xRow.getCell(relatedVendorCuid)!=null && !StringHelper.isEmpty(xRow.getCell(relatedVendorCuid).toString())) {
				map.put(AnOnu.AttrName.relatedVendorCuid, getImportAttributeQueryBO().getPropertyCuidByName(xRow.getCell(relatedVendorCuid).toString(),DeviceVendor.CLASS_NAME));
			}
			// 09.接入方式
			if(xRow.getCell(fttx)!=null && !"".equals(xRow.getCell(fttx).toString())){				
				String constantValue=Constant.FTTX;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(fttx).toString())){				
					map.put(AnOnu.AttrName.fttx, (java.lang.Long) ElementEnum.FTTX_TYPE.getValue(xRow.getCell(fttx).toString()));
				}
			}
			// 10.ONU类型
			if(xRow.getCell(onuType)!=null && !"".equals(xRow.getCell(onuType).toString())){
				String constantValue=Constant.ONUTYPE;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(onuType).toString())){	
					//临时做法 源代码中ONU类型中没有 HGU 和SFU
					if("HGU".equals(xRow.getCell(onuType).toString())){
						map.put(AnOnu.AttrName.onuType,3);
					}else if("SFU".equals(xRow.getCell(onuType).toString())){
						map.put(AnOnu.AttrName.onuType,4);
					}else if("MDU".equals(xRow.getCell(onuType).toString())){
						map.put(AnOnu.AttrName.onuType,5);
					}else if("未知".equals(xRow.getCell(onuType).toString())){
						map.put(AnOnu.AttrName.onuType,6);
					}else{
						map.put(AnOnu.AttrName.onuType, (java.lang.Long) ElementEnum.ONU_TYPE.getValue(xRow.getCell(onuType).toString()));
					}
				}			
			}
			// 11.产权
			if(xRow.getCell(ownership)!=null  && !"".equals(xRow.getCell(ownership).toString())){
				String constantValue=Constant.OWNERSHIP;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(ownership).toString())){				
					//枚举类型可能被人改过目前临时解决
					String ovnershipStr = xRow.getCell(ownership).toString();
					map.put(TransElement.AttrName.ownership,AnmsConst.OWNERSHIP.getValue(ovnershipStr));
				}//else填充自动默认值			
			}	
			// 12.产权人
			if(xRow.getCell(ownershipMan)!=null  && !"".equals(xRow.getCell(ownershipMan).toString())){
				String constantValue=Constant.OWNERSHIPMAN;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(ownershipMan).toString())){					
					map.put(AnOnu.AttrName.ownershipMan,(java.lang.Long) AnmsConst.OWNERSHIP_MAN.getValue(xRow.getCell(ownershipMan).toString()));
				}
			}
			// 13.维护人
			if(xRow.getCell(preserver)!=null && !"".equals(xRow.getCell(preserver).toString())){
				map.put(AnPos.AttrName.preserver, xRow.getCell(preserver).toString());
			}
			// 14.维护方式
			if(headingMap.get(Constant.MAINTMODE) != null){
				int maintmode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
				if(xRow.getCell(maintmode) != null && !StringHelper.isEmpty(xRow.getCell(maintmode).toString())){
					map.put(AnOnu.AttrName.maintMode,(java.lang.Long) ElementEnum.ELEMENT_MAINT_MODE.getValue( xRow.getCell(maintmode).toString()));
				}
			}
			// 15.设备IP地址
			if(xRow.getCell(devIp)!=null && !"".equals(xRow.getCell(devIp).toString())){
				map.put(AnOnu.AttrName.devIp, xRow.getCell(devIp).toString());
			}
			// 16.生命周期状态
			if(xRow.getCell(liveCycle)!=null && !"".equals(xRow.getCell(liveCycle).toString())){
				String constantValue=Constant.LIVECYCLE;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(liveCycle).toString())){			
					map.put(AnOnu.AttrName.liveCycle, (java.lang.Long) ElementEnum.LIVE_CYCLE.getValue(xRow.getCell(liveCycle).toString()));
				}
			}
			// 17.软件版本
			if(xRow.getCell(softVersion)!=null  && !"".equals(xRow.getCell(softVersion).toString())){
				map.put(AnOnu.AttrName.softVersion, xRow.getCell(softVersion).toString());
			}
			// 18.接入类型
			if(xRow.getCell(accessType) !=null && !StringHelper.isEmpty(xRow.getCell(accessType).toString())){
				String accessTyepStr = xRow.getCell(accessType).toString();
				//接入类型枚举临时解决办法
				int accessTyepInt = 4;
				if(accessTyepStr.equals("集客")){
					accessTyepInt = 1;
				}else if(accessTyepStr.equals("家客")){
					accessTyepInt = 2;
				}else if(accessTyepStr.equals("集客/家客共用")){
					accessTyepInt = 3;
				}
				map.put("ACCESS_TYPE",new Long(accessTyepInt));
			}
			// 19.ONU_ID号
			if(xRow.getCell(onuId)!=null && !"".equals(xRow.getCell(onuId).toString())){
				if(com.boco.core.utils.lang.StringHelper.isNumber(xRow.getCell(onuId).toString())){
					map.put(AnOnu.AttrName.onuId,Double.valueOf(xRow.getCell(onuId).toString()).intValue());
				}	
			}
			// 20.ONU的认证类型
			if(xRow.getCell(authType)!=null && !"".equals(xRow.getCell(authType).toString())){
				/*String constantValue=Constant.AUTHTYPE;
				if(ImportCommonMethod.isEnum(constantValue,xRow.getCell(authType).toString())){		
					map.put(AnOnu.AttrName.authType, (java.lang.Long) AnmsConst.AUTH_TYPE.getValue(xRow.getCell(authType).toString()));
				}*/
				//修改原来的逻辑如下  2016年4月12日19:27:59
				String authTypeStr = xRow.getCell(authType).toString();
				String authTypeNum = "";
				if("SN".equals(authTypeStr)){
					authTypeNum="1";
				}else if("MAC".equals(authTypeStr)){
					authTypeNum="2";
				}else if("PASSWORD".equals(authTypeStr)){
					authTypeNum="3";
				}else if("LOGICID".equals(authTypeStr)){
					authTypeNum="4";
				}
				map.put(AnOnu.AttrName.authType, authTypeNum);
			}
			// 21.密码
			if(xRow.getCell(password)!=null && !"".equals(xRow.getCell(password).toString())){
				map.put(AnOnu.AttrName.password,xRow.getCell(password).toString());
			}
			// 22.LOGICID
			if(xRow.getCell(logicid)!=null && !"".equals(xRow.getCell(logicid).toString())){
				map.put("LOGICID",xRow.getCell(logicid).toString());
			}
			// 23.MAC地址
			if(xRow.getCell(macAddress)!=null  && !"".equals(xRow.getCell(macAddress).toString())){
				map.put(AnOnu.AttrName.macAddr,xRow.getCell(macAddress).toString());
			}
			// 24.SN码
			if(xRow.getCell(seqNo)!=null  && !"".equals(xRow.getCell(seqNo).toString())){
				map.put(AnOnu.AttrName.seqno,ImportCommonMethod.getCellValue(xRow.getCell(seqNo)));
			}
			// 25.建设日期
			if (xRow.getCell(creatTime) !=null && !"".equals(xRow.getCell(creatTime).toString())) {
				int creatType=xRow.getCell(creatTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					Date dateCreateTime =  xRow.getCell(creatTime).getDateCellValue();
					map.put(AnOnu.AttrName.creattime, getImportBasicDataBO().formtTime(
							formatter.format(dateCreateTime)));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					String dateCreateTime=xRow.getCell(creatTime).toString();
					map.put(AnOnu.AttrName.creattime, ImportCommonMethod.getTimestame(dateCreateTime));
				}
				
			}
			// 26.所属工程
			if(xRow.getCell(relatedProjectCuid)!=null && !"".equals(xRow.getCell(relatedProjectCuid).toString())){
				map.put(AnOnu.AttrName.relatedProjectCuid, xRow.getCell(relatedProjectCuid).toString());
			}
			// 27 宽带端口数量
			/*if(xRow.getCell(PORT_NUMBER) !=null && !"".equals(xRow.getCell(PORT_NUMBER).toString())){
				map.put("PORT_NUMBER",xRow.getCell(PORT_NUMBER).toString());
			}*/
			//27.一线数据维护人
			if(xRow.getCell(MAINT_PERSON) !=null && !"".equals(xRow.getCell(MAINT_PERSON).toString())){
				map.put("MAINT_PERSON",xRow.getCell(MAINT_PERSON).toString());
			}
			//28.移动公司数据质量责任人
			if(xRow.getCell(DATA_QUALITY_PERSON) !=null && !"".equals(xRow.getCell(DATA_QUALITY_PERSON).toString())){
				map.put("DATA_QUALITY_PERSON",xRow.getCell(DATA_QUALITY_PERSON).toString());
			}
			
			
			//所属机房/资源点
			/*if(xRow.getCell(relatedSpaceCuid)!=null && !StringHelper.isEmpty(xRow.getCell(relatedSpaceCuid).toString())){
				Map temp = getImportAttributeQueryBO().getRoomAccessPointByName(xRow.getCell(relatedSpaceCuid).toString());
				if(temp!=null&&temp.size()>0){
					map.put("RELATED_SPACE_CUID", temp.get("CUID"));
					getAnOltManageBO().setRelatedMapValuesNew(map, AnOnu.CLASS_NAME);
				}
			}
			//设备安装地址
			if(xRow.getCell(location) !=null && !"".equals(xRow.getCell(location).toString())){
				String locationStr = xRow.getCell(location).toString();
				List<Map> addressList = getAnOltManageBO().selectFullAddressByLabelCn(locationStr);
				if(addressList!=null&&addressList.size()>0){
					map.put(AnOnu.AttrName.location, addressList.get(0).get("CUID"));
					String districtCuid = map.get(AnOnu.AttrName.relatedDistrictCuid)+"";
					if(ImportCommonMethod.isEmpty(districtCuid)){
						map.put(AnOnu.AttrName.relatedDistrictCuid,addressList.get(0).get("CITY"));
					}
				}
			}
			if(xRow.getCell(relatedOltPort)!=null && !"".equals(xRow.getCell(relatedOltPort).toString())){
				String relatedOltPortName = xRow.getCell(relatedOltPort).toString();
				map.put(AnOnu.AttrName.relatedOltPortCuid, relatedOltPortName);
				relatedOltPortSet.add(relatedOltPortName);
			}
			if(xRow.getCell(relatedOltCuid)!=null && !"".equals(xRow.getCell(relatedOltCuid).toString())){
				map.put(AnOnu.AttrName.relatedOltCuid, xRow.getCell(relatedOltCuid).toString());
				map.put("oltName", xRow.getCell(relatedOltCuid).toString());
				if(!oltList.contains(xRow.getCell(relatedOltCuid).toString())){
					oltList.add( xRow.getCell(relatedOltCuid).toString());
				}
			}
			if(xRow.getCell(realLongitude)!=null && !"".equals(xRow.getCell(realLongitude).toString())){
				Float fltRealLongitude=Float.parseFloat(xRow.getCell(realLongitude).toString());
				map.put(AnOnu.AttrName.realLongitude, fltRealLongitude);
			}
			
			if(xRow.getCell(realLatitude)!=null && !"".equals(xRow.getCell(realLatitude).toString())){
				Float fltRealLatitude=Float.parseFloat(xRow.getCell(realLatitude).toString());
				map.put(AnOnu.AttrName.realLatitude,fltRealLatitude);
			}
			if (xRow.getCell(setUpTime) !=null&&!"".equals(xRow.getCell(setUpTime).toString())) {
				int creatType=xRow.getCell(setUpTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					Date dateSetUpDate =  xRow.getCell(setUpTime).getDateCellValue();
					map.put(AnOnu.AttrName.setupTime, getImportBasicDataBO().formtTime(	formatter.format(dateSetUpDate)));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					String dateSetUp=xRow.getCell(setUpTime).toString();
					map.put(AnOnu.AttrName.setupTime, ImportCommonMethod.getTimestame(dateSetUp));
				}						
			}
			if (xRow.getCell(backNetWorkTime) !=null && !"".equals(xRow.getCell(backNetWorkTime).toString())) {
				int creatType=xRow.getCell(backNetWorkTime).getCellType();						
				if(creatType==Cell.CELL_TYPE_NUMERIC){
					Date dateBackNetworkTime =  xRow.getCell(backNetWorkTime).getDateCellValue();
					map.put(AnOnu.AttrName.backNetworkTime, getImportBasicDataBO().formtTime(formatter.format(dateBackNetworkTime)));
				}else if(creatType==Cell.CELL_TYPE_STRING){
					String dateBackTime=xRow.getCell(backNetWorkTime).toString();
					map.put(AnOnu.AttrName.backNetworkTime, ImportCommonMethod.getTimestame(dateBackTime));
				}						
			}
			if(headingMap.get(Constant.MAINTMODE) != null){
				int maintmode = Integer.parseInt(headingMap.get(Constant.MAINTMODE).toString());
				if(xRow.getCell(maintmode) != null && !StringHelper.isEmpty(xRow.getCell(maintmode).toString())){
					map.put(AnOnu.AttrName.maintMode,(java.lang.Long) ElementEnum.ELEMENT_MAINT_MODE.getValue( xRow.getCell(maintmode).toString()));
				}
			}*/
			if(!ImportCommonMethod.isEmpty(cuid)){
				map.put(AnOnu.AttrName.cuid, cuid);
				map.put(AnOnu.AttrName.devCuid, "AN_ONU-"+cuid);
			}
			
			map.put("OBJECTID", objectId);
			map.put("GT_VERSION", 0);
			map.put("ISDELETE", 0);
			map.put(AnOnu.AttrName.isPermitSysDel, 0);
			map.put(GenericDO.AttrName.createTime, new Timestamp(System.currentTimeMillis()));
			map.put(GenericDO.AttrName.lastModifyTime, new Timestamp(System.currentTimeMillis()));
			map.put(AnOnu.AttrName.useState, 1);
			map.put(AnOnu.AttrName.isClosenet, 0);
			//插入的时候加入了一些定值
	/**		
	 * 直接在放入lis前 赋FDN信息
	 * by lichao
	 */
				String	onuFdn = "";
				String onuposCuid="" ;
				if(map.get("FDN")=="" ||map.get("FDN")== null ){
					onuposCuid =  ObjectUtils.toString(map.get("RELATED_POS_CUID"));		 	
				Map temp1=getImportAttributeQueryBO().getPosByLabelCuid(onuposCuid) ; 
				onuFdn = temp1.get("FDN") + ":ONU=" + ObjectUtils.toString(map.get("LABEL_CN"));;
				map.put("FDN", onuFdn);
				}		
			onuList.add(map);					
		} catch (Exception e) {
			LogHome.getLog().error("接入网-ONU设备",e);			
			throw new Exception(e);
		}
	}
	/**
	 * 判断onu的Excel中第o行是否存在已有字段，这些字段是必不可少的字段
	 */
	private static Map getOnuHeadingMap(Sheet sheet, int headingRow, List errorlist,int lastColumns) {
		Map headingMap = new HashMap();
		Row xRow = sheet.getRow(0);
		for(int i=1;i<lastColumns;i++){
			Object columnName = xRow.getCell(i);
			if(columnName != null){
				String columnString = columnName.toString();
	    	    if(!columnString.equals(onuExcelColumns[i])){
	    	    	errorlist.add("导入的EXCEL文件不符合规范,请使用模板文件");
	    		  break;
	    	    }
				headingMap.put(columnName.toString(),i);
			}else{
				errorlist.add("该模板的第"+(i+1)+"列头名称不能为空!");
			}
		}
		return headingMap;
	}
	
	/**
	 * 验证关联关系是否存在，不正确将错误打印到最后一列中去
	 * 
	 * @param ws
	 *            打开的的副本ws环境可以对单元格进行操作的
	 * @param sheet
	 *            第一个sheet表
	 * @param i
	 *            当前第几行
	 * @param j
	 *            当前第几列
	 * @param lastColumns
	 *            总列数
	 * @throws Exception
	 *             验证关联关系是否存在，不正确将错误打印到最后一列中去
	 */
	private  Map verificationRelated(Workbook writeWorkBook, Sheet writeSheet, int j, int i, int lastColumns, String constantValue ,String propertyName)
			throws Exception {
		boolean flag = false;	
		Map res = null;
		try {
			Row xRow=writeSheet.getRow(i);
			Object cellContant=xRow.getCell(j);
			if (cellContant !=null && !"".equals(cellContant.toString())) {
				//表格中有EMS 机房 厂家根据EMS获得
				if (constantValue.equals(Constant.RELATEDROOMCUID)) {
					if(!StringHelper.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,Room.CLASS_NAME))){
						flag = true;
					}
				} else if (constantValue.equals(Constant.RELATEDEMSCUID)) {	
					if(!StringHelper.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,NmsSystem.CLASS_NAME))){
						flag = true;
					}
				} else if (constantValue.equals(Constant.ROOMACCESSPOINT)) {	
					Map temp = getImportAttributeQueryBO().getRoomAccessPointByName(propertyName);
					if(temp!=null&&temp.size()>0){
						res = temp;
						flag = true;
					}
				} else if (constantValue.equals(Constant.RELATEDPOSCUID) || constantValue.equals("上联设备")) {	
					Map temp = getImportAttributeQueryBO().getPosByName(propertyName);
					if(temp!=null&&temp.size()>0){
						res = temp;
						flag = true;
					}
				} else if (constantValue.equals("厂家")) {
					if(!StringHelper.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName,DeviceVendor.CLASS_NAME))){
						flag = true;
					}
				} else if (constantValue.equals("规格型号")) {
					flag = neModelCuidNameMap.containsKey(cellContant.toString());
				} else if (constantValue.equals(Constant.RELATEDACCESSPOINT)) {
					if(!StringHelper.isEmpty(getImportAttributeQueryBO().getPropertyCuidByName(propertyName, "CAB"))){
						flag = true;
					}
				}
				if (!flag) {
					ImportCommonMethod.updateExcelFormat(writeWorkBook,writeSheet,i, j);				
					// 操作最后一行单元格：通用的获取cell值的方式,返回字符串
					Cell xCell=xRow.getCell(lastColumns);
					if(xCell==null){
						xCell=xRow.createCell(lastColumns);
						//设置最后一列记录异常信息单元格的列宽度
//						writeSheet.setColumnWidth(lastColumns, writeSheet.getColumnWidth(lastColumns)*3);
						xCell.setCellValue(cellContant.toString()+ " 数据库中不存在 \r\n");
					}else{
						String strValue=xCell.getStringCellValue();
						strValue=strValue+cellContant.toString()+" 数据库中不存在 \r\n";
						xCell.setCellValue(strValue);				
					}
				}
			}//else不符合条件直接退出
		} catch (Exception e) {
			LogHome.getLog().error("", e);
			throw new Exception("");
		}
		return res;
	}
	
}

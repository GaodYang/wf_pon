package com.boco.workflow.webservice.upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.boco.workflow.webservice.upload.bo.Constant;
import com.boco.workflow.webservice.upload.bo.ImportBasicDataBO;
import com.boco.workflow.webservice.upload.bo.ImportOnuExcel;
import com.boco.workflow.webservice.upload.servlet.ImportResultDO;

public class ImportResService {
	
	@Autowired
	private ImportBasicDataBO basicDataBO;
	
	Map<String,String> neModelCuidNameMap =null;
 	//解析excel
	public void resolveExcel(File file) throws Exception{
		
		FileInputStream inputStream = new FileInputStream(file);
		Map<String,ImportResultDO> map = new HashMap<String,ImportResultDO>();
		
		Workbook writeWorkBook = WorkbookFactory.create(inputStream);
		
		Sheet writeSheet =writeWorkBook.getSheet(Constant.ONUNAME);
		if(neModelCuidNameMap.isEmpty()){
			neModelCuidNameMap = basicDataBO.getNeModelCfgCuidName();
		}
		ImportOnuExcel importOnuExcel=new ImportOnuExcel(neModelCuidNameMap);
		map.put(Constant.ONUNAME, importOnuExcel.importOnuBasicData(writeWorkBook,writeSheet,file.getAbsolutePath(), Constant.ONUNAME));
			
	
	}

}

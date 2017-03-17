package com.boco.workflow.webservice.upload.servlet;

import java.util.ArrayList;
import java.util.List;

public class ImportResultDO {

	private String sheetName;
	private List<String> info = new ArrayList<String>();
	private boolean isSuccess = false;
	private boolean showFileUrl = true;
	
	public boolean isShowFileUrl() {
		return showFileUrl;
	}

	public void setShowFileUrl(boolean showFileUrl) {
		this.showFileUrl = showFileUrl;
	}

	public ImportResultDO(String sheetName){
		this.sheetName = sheetName;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<String> getInfo() {
		return info;
	}
	public void setInfo(List<String> info) {
		this.info = info;
	}
	
	public void setInfo(String info){
		this.info.add(info);
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}	
}

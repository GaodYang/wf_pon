
package com.boco.workflow.webservice.upload.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.boco.common.util.debug.LogHome;
import com.boco.core.utils.lang.TimeFormatHelper;
import com.boco.workflow.webservice.upload.service.ImportResService;


@WebServlet(name="ImportServlet",urlPatterns="/ImportServlet")
public class ImportServlet extends HttpServlet {
	
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		String fileType = ".xls";
		
		String prjcode = request.getParameter("prjcode");
		
		try {
			
			ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem>  fileItems = upload.parseRequest(request);
			FileItem fileitem = fileItems.get(0);
			if(fileitem != null){

				//解析文件导入
				String tempFile = TimeFormatHelper.getFormatDate(new Date(), TimeFormatHelper.TIME_FORMAT_B) + fileType;
				//路径+文件名称
				String upLoadFileName =  request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator + tempFile;
				//上传文件写到服务器上
				File file = new File(upLoadFileName);
				fileitem.write(file);
				Map map = new HashMap();
				
				
				new ImportResService().resolveExcel(file);
				/*	ImportBusinessAction importBusinessAction = new ImportBusinessAction();
					FileInputStream fileInputStream = new FileInputStream(file);
					tempFile = TimeFormatHelper.getFormatDate(new Date(), TimeFormatHelper.TIME_FORMAT_B) + ".xls";
					map = importBusinessAction.importBusiness(request.getSession().getServletContext().getRealPath("/") + "upload" + File.separator+tempFile ,fileInputStream);*/
				
				Iterator<Entry<String, ImportResultDO>> iterator = map.entrySet().iterator();
				StringBuilder showMessage = new StringBuilder();
				String errorFileDownLoadUrl = "";
				while(iterator.hasNext()){
					Entry<String, ImportResultDO> entry = iterator.next();
					if(entry != null){
						showMessage.append(entry.getKey()+"<br>");
						ImportResultDO importResultDO = entry.getValue();
						if(importResultDO != null){
							if(!importResultDO.isSuccess() && importResultDO.isShowFileUrl()){
								errorFileDownLoadUrl = (request.getSession().getServletContext().getContextPath() + File.separatorChar + "upload" + File.separatorChar + tempFile).replace("\\", "/");
							}
							List<String> infoList = importResultDO.getInfo();
							for(String msg : infoList){
								showMessage.append(msg+"<br>");
							}
						}
					}
				}
				response.getWriter().write("{'success':true,'errorFileDownLoadUrl':'" + errorFileDownLoadUrl + "','showMessage':'" + showMessage.toString() + "'}");
			}else{
				//读取文件失败
				response.getWriter().write("{'success':true,'showMessage':'导入文件失败,读取文件异常'}");
			}
		} catch (Exception e) {
			LogHome.getLog().error("导入文件失败", e);
			response.getWriter().write("{'success':true,'showMessage':'系统错误，请联系管理员！ at " + TimeFormatHelper.getFormatDate(new Date(), TimeFormatHelper.TIME_FORMAT_A) + "'}");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

}
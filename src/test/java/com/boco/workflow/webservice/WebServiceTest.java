package com.boco.workflow.webservice;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.junit.Test;

import localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesServiceLocator;

public class WebServiceTest {

	@Test
	public void testService() throws ServiceException{
		String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
				"<info>"+
				"<setupname>name</setupname>"+
				"<setupcode>code</setupcode>"+
				"<setupreqfilecode>fcode</setupreqfilecode>"+
				"<setupreqername>sname</setupreqername>"+
				"<setupreqerphone>phone</setupreqerphone>"+
				"<setupreqdate>date</setupreqdate>"+
				"<prjname>name</prjname>"+
				"<prjcode>pcode</prjcode>"+
				"<prjcategory>cy</prjcategory>"+
				"<prjtype>type</prjtype>"+
				"<cost>cost</cost>"+
				"<length>l</length>"+
				"<domainname>dname</domainname>"+
				"<prjdesc>desc</prjdesc>"+
				"<ftpname><![CDATA[fname$#&^$%]]></ftpname>"+
				"<parentprjcode>pcode</parentprjcode>"+
				"</info> ";
		
		localhost.wf_pon.services.IrmsWebServices.IrmsWebServicesService services = new IrmsWebServicesServiceLocator();
		try {
			String xml = services.getIrmsWebServices().createProject(str);
			System.out.println(xml);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

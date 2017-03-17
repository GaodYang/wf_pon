package com.boco.workflow.webservice;

import org.junit.Test;

import com.boco.workflow.webservice.service.IService;
import com.boco.workflow.webservice.service.impl.ProjectServiceImpl;

public class ServiceTest {

	@Test
	public void TestBaseService(){
		
		IService si = new ProjectServiceImpl();
		
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
		String xml = si.execute(str);
		System.out.println(xml);
	}
	
}

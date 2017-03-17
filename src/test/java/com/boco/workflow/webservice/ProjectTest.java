package com.boco.workflow.webservice;

import java.lang.reflect.Field;

import org.junit.Test;

import com.boco.workflow.webservice.builder.ResultBuilder;
import com.boco.workflow.webservice.builder.factory.PojoBuilderFactory;
import com.boco.workflow.webservice.pojo.Result;

public class ProjectTest {

	private String abc ;
	@Test
	public void testXml(){
		
		String xml = PojoBuilderFactory.getBuilder(ResultBuilder.class).addErrorInfo("34").addIsSuccess("sad").toXml();
		System.out.println(xml);
		
		Result result = PojoBuilderFactory.getBuilder(ResultBuilder.class).fromXml(xml);
		System.out.println(result);
		
		
	}
	
	public static void main(String[] args) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		
		ProjectTest test = new ProjectTest();
		test.abc = "2342134";
		Field f = test.getClass().getDeclaredField("abc");
		
		String a = (String) f.get(test);
		System.out.println(test.getClass().getName());
	}
}

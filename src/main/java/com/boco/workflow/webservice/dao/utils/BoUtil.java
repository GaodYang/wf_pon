package com.boco.workflow.webservice.dao.utils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.boco.common.util.debug.LogHome;
import com.boco.core.ibatis.dao.IbatisDAOHelper;
import com.boco.workflow.webservice.constants.NetWorkConstant;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * bo工具类
 * @author gaoyang 2017年3月13日
 *
 */
public class BoUtil {

	/**
	 * 在返回结果中获取唯一值
	 * @param list
	 * @return
	 */
	public static String getSingleValueByList(@SuppressWarnings("rawtypes") List list,String key){
		
		if(list != null && list.size() > 0){
			
			Class<?> clazz = list.get(0).getClass();
			if(clazz == String.class){
				
				return list.get(0).toString();
				
			}else if(clazz == Map.class){
				
				return IbatisDAOHelper.getStringValue((Map<?,?>) list.get(0),key);
			}
			
		}
		
		return null;
	}
	
	/**
	 * 将string时间转化为date
	 * @param time
	 * @return
	 */
	public static Date formatTime(String time){
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		if(StringUtils.isNotBlank(time)){
			
			try {
				date = format.parse(time);
				
			} catch (ParseException e) {
				
				LogHome.getLog(BoUtil.class).error("时间转换错误！",e);
			}
		}
		return date;
	}
	
	/**
	 * 批量删除
	 * @param statementName
	 * @param list
	 * @throws SQLException
	 */
    public static void batchDelete(SqlMapClient client,final String statementName, final List<?> list) throws SQLException {
    	
    	if(list == null || list.size() == 0){
    		
    		return;
    	}
    	for (int i = 0, n = list.size(); i < n; i++) {
       	  if(i != 0 && i%NetWorkConstant.COMMIT_COUNT == 0 ){
       		client.executeBatch();
       	  }
       	  client.delete(statementName, list.get(i));
         }
    	client.executeBatch();
     }
    
    /**
     * 批量更新
     * @param statementName
     * @param list
     * @throws SQLException
     */
    public static void batchUpdate(SqlMapClient client,final String statementName, final List<?> list) throws SQLException {
    	
    	if(list == null || list.size() == 0){
    		
    		return;
    	}
    	for (int i = 0, n = list.size(); i < n; i++) {
       	  if(i != 0 && i%NetWorkConstant.COMMIT_COUNT == 0 ){
       		client.executeBatch();
       	  }
       	  client.update(statementName, list.get(i));
         }
    	client.executeBatch();
    }
    
    /**
     * 将查询结果转化为页面显示的结果
     * @param list
     * @return
     */
    public static Map<String,Object> convertResult2Panel(List<Map<String,Object>> list){
    	
    	Map<String,Object> returnMap = new HashMap<String,Object>();
		
		for (Map<String,Object> map : list) {

			for (String key : map.keySet()) {
				if (key.startsWith("N_")) {
					String attr = StringUtils.substring(key, 2);
					String text = IbatisDAOHelper.getStringValue(map, key);
					String value = IbatisDAOHelper.getStringValue(map, attr);

					Map<String, String> relMap = new HashMap<String, String>();
					relMap.put("text", text);
					relMap.put("value", value);
					returnMap.put(attr, relMap);
				} else if (!returnMap.containsKey(key)) {
					returnMap.put(key, IbatisDAOHelper.getStringValue(map, key));
				}
			}

		}

		return returnMap;
    }
	
	
}

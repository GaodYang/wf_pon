<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html>
  <head>
    <title>资源信息</title>
    
	<%@ include file="/commons/common.jsp"%>
	<%@ include file="/commons/dwr.jsp"%>
	<%@ include file="/commons/ext.jsp"%>
	<%@ include file="/cmp_include/grid.jsp"%>
	<%@ include file="/cmp_include/form.jsp"%>
	<script type="text/javascript" src="${ctx}/commons/utils/UrlHelper.js"> </script>
	<script type="text/javascript" src="${ctx}/cmp_res/grid/ResGridPanel.js"></script>
	<script type="text/javascript" src="${ctx}/jsp/network/resource/resource_renderer.js"></script>	
	<script type="text/javascript" src="${ctx}/jsp/network/resource/resource.js"></script>
	<script type="text/javascript">
		Ext.onReady(function() {
			var param = UrlHelper.getUrlObj(window.location.search.substring(1));
			var username = param.username;
			if(!Ext.isEmpty(username)){
				username= username.replace(/\*/g,"#");
				username = username.replace(/\%/g,"=");
			}
			var prjcode = param.prjcode;
			if(!Ext.isEmpty(prjcode)){
				prjcode= prjcode.replace(/\*/g,"#");
				prjcode =prjcode.replace(/\%/g,"=");
			}
			var parentprjcode = param.parentprjcode;
			if(!Ext.isEmpty(parentprjcode)){
				parentprjcode= parentprjcode.replace(/\*/g,"#");
				parentprjcode =parentprjcode.replace(/\%/g,"=");
			}
			var panel = new NETWORK.resource({
				username : username,
				prjcode  : prjcode,
				parentprjcode : parentprjcode
				
			});
			var view = new Ext.Viewport({
                layout : 'fit',
                items : [panel]
            });
		});
	</script>

  </head>
  
 
</html>

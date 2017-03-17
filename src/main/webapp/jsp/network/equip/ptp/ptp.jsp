<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<%@ include file="/commons/common.jsp"%>
		<%@ include file="/commons/ext.jsp"%>
		<%@ include file="/commons/dwr.jsp"%>
		<script type="text/javascript" src="${ctx}/commons/utils/UrlHelper.js"> </script>
		<%@ include file="/jsp/common/grid.jsp"%>
		<script type="text/javascript" src="${ctx}/jsp/network/equip/plugins/event/grid_event.js"> </script>
		<script type="text/javascript" src="${ctx}/jsp/network/equip/ptp/ptp.js"> </script>
		<script type="text/javascript" src="${ctx}/jslib/zeroclipboard/ZeroClipboard.js"> </script>
		<script type="text/javascript" src="${ctx}/jsp/common/util.js"> </script>
		<script type="text/javascript">
		Ext.onReady(function(){
			var param = UrlHelper.getUrlObj(window.location.search.substring(1));
			var cardName = '';
			if(!Ext.isEmpty(param.cardName)){
				cardName= param.cardName.replace(/\*/g,"#");
				cardName = cardName.replace(/\%/g,"=");
			}
			var devName = '';
			if(!Ext.isEmpty(param.devName)){
				devName= param.devName.replace(/\*/g,"#");
				devName =devName.replace(/\%/g,"=");
			}
			var panel = new NETWORK.ptp({
				cardCuid	: param.cardCuid,
				cardName	: cardName,
				devCuid 	: param.devCuid,
				devName 	: devName,
				devTable	: param.devTable
			});
			var view = new Ext.Viewport({
				layout:'fit',
				items:[panel]
			});
		});		
 </script>
	</head>
	<body>
	 
	</body>
</html>
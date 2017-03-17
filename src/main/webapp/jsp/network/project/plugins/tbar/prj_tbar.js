Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/commons/utils/FrameHelper.js");


Frame.grid.plugins.tbar.prj_tbar = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.tbar.prj_tbar.superclass.constructor.call(this);
		var returnCfg = [
		 				{
							text : '关联信息',
							iconCls : 'c_chart_organisation',
							id	: 'PrjTBar.relation',
							scope : this,
							handler : function() {
								var record = this.grid.getSelectionModel().getSelected();
								if (!Ext.isEmpty(record)) {
									if(this.grid.getSelectionModel().selections.length > 1){
										Ext.Msg.alert('提示', '只能选择一条数据!');
										return;
									}else{
										var param = "username="+ 'admin' +"&prjcode="+ record.json.PRJ_CODE +"&parentprjcode="+ record.json.PARENT_PRJ_CODE;
										param = param.replace(/\#/g,"*");
										FrameHelper.openUrl("$(WEBAN_SERVER)/jsp/network/resource/resource.jsp?"+param,'资源信息');
										return;
									}
								} else {
									Ext.Msg.alert('提示', '请选择一条数据!');
									return;
								}
							}
						}
						];
		
			return [returnCfg];
		}
});
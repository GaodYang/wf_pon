Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/equip/pos/pos_query.js");
$importjs(ctx + "/jsp/network/equip/plugins/tbar/onu_tbar.js");
$importjs(ctx + "/jsp/network/equip/plugins/renderer/equip_renderer.js");

NETWORK.pos= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.pos',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	
	initComponent : function() {
		this._initItems();
		NETWORK.pos.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			rendererPlugin : 'equip_renderer',
			gridCfg : {
				cfgParams : {
					templateId : 'POS_MANAGE'
				}
			},
			queryPlugin : 'pos_query',
			tbarPlugin : 'onu_tbar',
			view : new Ext.grid.GridView({
				forceFit : true
			}),
			pageSize : 20,
			loadData : false
		});
		
		this.items = [this.rolePanel];
	}
});
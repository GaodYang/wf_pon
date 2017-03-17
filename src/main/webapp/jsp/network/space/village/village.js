Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/space/village/village_query.js");
$importjs(ctx + "/jsp/network/space/village/village_tbar.js");
$importjs(ctx + "/jsp/network/space/village/village_renderer.js");

NETWORK.village= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.village',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	
	initComponent : function() {
		this._initItems();
		NETWORK.village.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			rendererPlugin : 'village_renderer',
			gridCfg : {
				cfgParams : {
					templateId : 'VILLAGE_MANAGE'
				}
			},
			queryPlugin : 'village_query',
		//	tbarPlugin : 'address_tbar',
			view : new Ext.grid.GridView({
				forceFit : true
			}),
			pageSize : 20,
			loadData : false
		});
		
		this.items = [this.rolePanel];
	}
});
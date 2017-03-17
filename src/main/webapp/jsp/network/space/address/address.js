Ext.namespace("NETWORK");
$importjs(ctx + "/jsp/network/space/address/address_query.js");
$importjs(ctx + "/jsp/network/space/address/address_tbar.js");
$importjs(ctx + "/jsp/network/space/address/address_renderer.js");

NETWORK.address= Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.address',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	
	initComponent : function() {
		this._initItems();
		NETWORK.address.superclass.initComponent.call(this);
	},
	_initItems : function() {
		this.rolePanel = new Frame.grid.ResGridPanel({
			region : 'center',
//			title : 'pos信息',
			border : false,
			enableGeneralQuery: false,
			enableContextMenu : true,
			rendererPlugin : 'address_renderer',
			gridCfg : {
				cfgParams : {
					templateId : 'ADDRESS_MANAGE'
				}
			},
			queryPlugin : 'address_query',
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
Ext.ns('NETWORK');
$importjs(ctx + "/jsp/network/equip/ptp/ptp.js");
$importjs(ctx + "/jsp/network/equip/pos/pos.js");
$importjs(ctx + "/jsp/network/equip/onu/onu.js");
$importjs(ctx + "/jsp/network/space/address/address.js");
$importjs(ctx + "/jsp/network/space/village/village.js");
$importjs(ctx + "/jsp/network/space/cover/cover.js");
$importjs(ctx + "/jsp/network/resource/import/import.js");
	              
NETWORK.equip = Ext.extend(Ext.Panel, {
	id 	: 'equip',
	border : false,
	autoScroll : true,
	prjcode  : '',
	initComponent : function() {
		this._initItems();
		NETWORK.equip.superclass.initComponent.call(this);
	},
	_initItems : function() {
		var scope = this;
		var items = []; 
		var onu = new NETWORK.onu({});
		var onuPtp = new NETWORK.ptp({id:'onuPtp',devTable:'onu'});
		var pos = new NETWORK.pos({});
		var posPtp = new NETWORK.ptp({id:'posPtp',devTable:'pos'});
		var address = new NETWORK.address({});
		var village = new NETWORK.village({});
		var cover = new NETWORK.cover({});

		
		this.equipPanel = new Ext.TabPanel({
			id 	: 'equipPanel',
			border : false,
			hideBorders : true,
			width:'100%',
			height:600,
			activeTab:0,
			items : [
			         {title: "onu信息",layout: 'fit',items:onu},
			         {title: "onu端口",layout: 'fit',items:onuPtp},
			         {title: "pos信息",layout: 'fit',items:pos},
			         {title : 'pos端口',layout: 'fit',items:posPtp},
			         {title : '标准地址',layout: 'fit',items:address},
			         {title : '业务区',layout: 'fit',items:village},
			         {title : '覆盖范围',layout: 'fit',items:cover}
			]
		
		});
		
		this.import_res = new NETWORK.import({prjcode : this.prjcode});
		items.push(this.import_res,this.equipPanel);
		this.items = items;
		return this.items;
	}
});
Ext.reg('NETWORK.equip', NETWORK.equip);
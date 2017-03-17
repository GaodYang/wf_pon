Ext.ns('NETWORK');
NETWORK.attach = Ext.extend(Ext.Panel, {
	id 	: 'attach',
	border : false,
	autoScroll : true,
	initComponent : function() {
		this._initItems();
		NETWORK.attach.superclass.initComponent.call(this);
	},
	_initItems : function() {
		var scope = this;
		var items = []; 
		var labelWidth = 160;
		
		var _downloadBtn = new Ext.Button({
            text: "下载附件",
            minWidth: 80,
            handler: function() {
                Ext.Msg.alert('we');
            }
        });
		 
		this.attachPanel = new Ext.form.FormPanel({
			id 	: 'attachPanel',
			layout : 'form',
			border : false,
			hideBorders : true,
			defaults : {
				anchor : '-20'
			},
			items : [
			{
				layout : 'column',
				hideBorders : true,
				items : [{
					columnWidth : .5,
					layout : 'form',
					width:1200,
					labelWidth : labelWidth,
					defaults : {
						anchor : '-20'
					},
					items : [{
						labelWidth :labelWidth,
	   			 		id:'FTP_NAME',
	   			 		readOnly:true,
	   			 		name : 'FTP_NAME',
	   			 		xtype : 'textfield',
	   			 		readOnly : true,
	   			 		fieldLabel : '附件名称',
	   			 		value:''}]
				},{
					columnWidth : .1,
					layout : 'form',
					defaults : {
						anchor : '-20'
					},
					items : [_downloadBtn]
				}]
			}
			]
		
		});
		items.push(this.attachPanel);
		this.items = items;
		return this.items;
	}
	
});
Ext.reg('NETWORK.attach', NETWORK.attach);
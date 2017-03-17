Ext.ns('NETWORK');
NETWORK.project = Ext.extend(Ext.Panel, {
	id 	: 'project',
	border : false,
	autoScroll : true,
	username : '',
	prjcode  : '',
	parentprjcode : '',
	initComponent : function() {
		this._initItems();
		NETWORK.project.superclass.initComponent.call(this);
	},
	_initItems : function() {
		var scope = this;
		var items = []; 
		var labelWidth = 160;
		this.customPanel = new Ext.form.FormPanel({
			id 	: 'customPanel',
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
	   			 		id:'PRJ_CODE',
	   			 		readOnly:true,
	   			 		name : 'PRJ_CODE',
	   			 		xtype : 'textfield',
	   			 		editable : false,
	   			 		fieldLabel : '<font style="color:red;">工程编号*</font>',
	   			 		value:this.prjcode}]
				},{
					columnWidth : .5,
					layout : 'form',
					width:600,
					labelWidth : labelWidth,
					defaults : {
						anchor : '-20'
					},
					items : [{
						xtype : 'textfield',
						readOnly:true,
						fieldLabel : '<font style="color:red;">工程名称*</font>',
						allowBlank : false,
						id : 'PRJ_NAME',
						name : 'PRJ_NAME',
						value : ''
					}]
				}]
			},
			{
				layout : 'column',
				hideBorders : true,
				items : [{
					columnWidth : .5,
					layout : 'form',
					labelWidth : labelWidth,
					width:600,
					defaults : {
						anchor : '-20'
					},
					items : [{
						xtype : 'textfield',
						fieldLabel : '<font style="color:red;">操作人*</font>',
						name : 'USER_NAME',
						id:'USER_NAME',
						value:this.username
					}]
				},{
					columnWidth : .5,
					layout : 'form',
					labelWidth : labelWidth,
					defaults : {
						anchor : '-20'
					},
					width:600,
					items : [{
						xtype : 'textfield',
						fieldLabel : '所属工程编号',
						id : 'PARENT_PRJ_CODE',
						name : 'PARENT_PRJ_CODE',
						readOnly:true,
						value:this.parentprjcode
					}]
				}
			  ]
			}
			]
		
		});
		items.push(this.customPanel);
		this.items = items;
		return this.items;
	}
});
Ext.reg('NETWORK.project', NETWORK.project);
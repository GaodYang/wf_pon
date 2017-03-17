Ext.namespace("Frame.grid.plugins.query");

Frame.grid.plugins.query.ReplyOrderQuery = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.query.ReplyOrderQuery.superclass.constructor.call(this);
		

		var panel = new Ext.Panel( {
			height : 120,
			border : false,
			hideBorders : true,
			layout : 'column',
			defaults : {
				layout : 'form',
				columnWidth : .5,
				defaults : {
					anchor : '90%'
				}
			},
			items : [{
				items : [{
					xtype : 'textfield',
					fieldLabel : '工单ID',
					name : 'RELATED_TASK_CUID',
					width : 250,
					queryCfg : {
						type : "string",
						relation : "="
					}
				}]
			},{
				items : [{
					xtype : 'textfield',
					fieldLabel : '区域ID',
					name : 'REGION_ID',
					width : 250,
					queryCfg : {
						type : "string",
						relation : "="
					}
				}]
			},{
				items : [{
					xtype : 'asyncombox',
					fieldLabel : '是否成功',
					triggerAction : "all",
					name : 'SUCCESS',
					value : '',
					width : 250,
					comboxCfg : {
						cfgParams : {
							code : 'ISSUCCESS'
						}
					},
					queryCfg : {
						type : "string",
						relation : "="
					}
				}]
			}, {
				
				layout : 'form',
				columnWidth : .5,
				defaults : {
					anchor : '-20'
				},
				items : [ {
					xtype : 'compositefield',
					labelWdith : 120,
					fieldLabel : '回单时间',
					items : [{
						flex : 1,
						xtype : 'datetimefield',
						name : 'CREATE_TIME',
						format : 'yyyy-mm-dd hh24:mi:ss',
						width : 185,
						queryCfg : {
							type : "date",
							relation : "between"
						}
					}, {
						xtype : 'displayfield',
						left : 180,
						value : '至'
				
					}, {
						flex : 2,
						xtype : 'datetimefield',
						name : 'CREATE_TIME',
						format : 'yyyy-mm-dd hh24:mi:ss',
						width : 190,
						queryCfg : {
							type : "date",
							relation : "between"
						}
					}]
				}]
			}]		
		});
		return panel;
  }
});
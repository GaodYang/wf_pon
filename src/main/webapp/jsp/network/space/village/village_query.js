Ext.ns("Frame.grid.plugins.query");
Frame.grid.plugins.query.village_query = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.query.village_query.superclass.constructor
				.call(this);
		var queryVals = {};
		var scope = this;
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
					fieldLabel : '业务区名称',
					name : 'LABEL_CN',
					emptyText : '业务区名称',
					width : 250,
					value : queryVals["LABEL_CN"],
					queryCfg : {
						type : "string",
						relation : "like",
						blurMatch : 'both'
					}
				}]
			},
			{
				items : [{
					xtype : 'asyncombox',
					fieldLabel : '所属地市',
					name : 'RELATED_DISTRICT_CUID',
					emptyText : '所属地市',
					width : 250,
					value : '',
					comboxCfg : {
						cfgParams : {
							code : 'DISTRICT_NAME'
						}
					},
					queryCfg:{
						type : "string",
						relation :"like",
						blurMatch : 'both'
					}
				}]
			}
			,{
				items : [{
					xtype : 'textfield',
					fieldLabel : '维护责任人',
					name : 'MAINTAIN_PERSON',
					emptyText : '维护责任人',
					width : 250,
					value : queryVals["MAINTAIN_PERSON"],
					queryCfg : {
						type : "string",
						relation : "like",
						blurMatch : 'both'
					}
				}]
			
			
			},{
				items : [{
					xtype : 'textfield',
					fieldLabel : '地址',
					name : 'ADDRESS',
					emptyText : '地址',
					width : 250,
					value : queryVals["ADDRESS"],
					queryCfg : {
						type : "string",
						relation : "like",
						blurMatch : 'both'
					}
				}]
			},{
				items : [{
					xtype : 'asyncombox',
					fieldLabel : '维护单位',
					name : 'MAINTAIN_DEPT_CUID',
					width : 250,
					emptyText : '维护单位',
				    blurMatch : 'both',
					comboxCfg : {
					    boName : 'XmlTemplateComboxBO',
						cfgParams : {
							code : 'CONSTRUCT_DEPT_COMBOX_1'
						}
					}
				}]
			},{
				items : [{
					xtype : 'asyncombox',
					fieldLabel : '施工单位',
					name : 'CONSTRUCT_DEPT_CUID',
					width : 250,
					emptyText : '施工单位',
				    blurMatch : 'both',
					comboxCfg : {
					    boName : 'XmlTemplateComboxBO',
						cfgParams : {
							code : 'CONSTRUCT_DEPT_COMBOX'
						}
					}
				}]
			}
			
			]		
		});
		return panel;
  }
});
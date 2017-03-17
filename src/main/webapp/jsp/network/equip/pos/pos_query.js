Ext.ns("Frame.grid.plugins.query");
$importcss(ctx + "/jsp/common/style/customize.css");

Frame.grid.plugins.query.pos_query = Ext.extend(Object, {
	constructor: function(grid){
		this.grid = grid;
		Frame.grid.plugins.query.pos_query.superclass.constructor.call(this);
		var queryVals = {};
		var scope = this;
		
		var baseQueryItems = [ {
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
				xtype : 'textfield',
				fieldLabel : '分光器名称',
				name : 'LABEL_CN',
				value : queryVals["LABEL_CN"],
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
			}]
		}, {
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
					xtype : 'asyncombox',
					fieldLabel : '生产厂商',
					name : 'RELATED_VENDOR_CUID',
					width : 168,
					emptyText : "请选择...",
					hideTrigger1 : true,
					triggerAll : true,
					loadData : false,
					editable : false,
					comboxCfg : {
						cfgParams : {
							code : 'ALLVENDOR'
						}
					},
					queryCfg:{
						relation : '='
					}
					
				}]
		},{
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [{
				xtype : 'asyncombox',
				fieldLabel : '所属地市',
				triggerAction : "all",
				name : 'RELATED_DISTRICT_CUID',
				value : '',
				comboxCfg : {
					cfgParams : {
						code : 'DISTRICT_NAME'
					}
				},
				queryCfg:{
					type : "string",
					relation : "like"
				}
			}]
	},{
		layout : 'form',
		columnWidth : .5,
		defaults : {
			anchor : '-20'
		},
		items : [ {
			xtype : 'asyncombox',
			fieldLabel : '安装位置',
			name : 'RELATED_CAB_CUID',
			comboxCfg  : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'ACCESSPOINT'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			}
		}]
	}, {
		   layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
					xtype : 'asyncombox',
					fieldLabel : '产权人',
					name : 'OWNERSHIP_MAN',
					emptyText : "请选择...",
					hideTrigger1 : true,
					triggerAll : true,
					loadData : false,
					editable : false,
					comboxCfg : {
						cfgParams : {
							code : 'OWNERMENSHIP'
						}
					},
					queryCfg:{
						relation : '='
					}
					
				}]
		}, {
			   layout : 'form',
				columnWidth : .5,
				defaults : {
					anchor : '-20'
				},
				items : [ {
						xtype : 'asyncombox',
						fieldLabel : '所属上联设备',
						name : 'RELATED_OLT_CUID',
						emptyText : "请选择...",
						hideTrigger1 : true,
						triggerAll : true,
						loadData : false,
						editable : true,
						comboxCfg : {
							cfgParams : {
								code : 'RELATED_OLT_NAME'
							}
						},
						queryCfg:{
							relation : '='
						}
						
					}]
			}];
		
		
		var panel = new Ext.Panel( {
			bodyStyle : 'margin-top:3px',
			autoScroll : true,
			id     : 'posPanel',
			border : 'false',
			hideBorders : true,
			layout : 'column',
			height : 145,
			defaults : {
				layout : 'form',
				columnWidth : .45,
				defaults : {
					anchor : '90%'
				}
			},
			items : [baseQueryItems]	
		});
		return panel;
	}
});
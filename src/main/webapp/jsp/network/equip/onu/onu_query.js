Ext.ns("Frame.grid.plugins.query");
$importcss(ctx + "/jsp/common/style/customize.css");

Frame.grid.plugins.query.onu_query = Ext.extend(Object, {
	constructor: function(grid){
		this.grid = grid;
		Frame.grid.plugins.query.onu_query.superclass.constructor.call(this);
		var queryVals = {};
		var scope = this;
		var onuLabelcn = grid.onuLabelcn;
		var baseQueryItems = [ {
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
				xtype : 'textfield',
				fieldLabel : 'ONU名称',
				name : 'LABEL_CN',
				value : onuLabelcn,
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
			}  ]
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
			items : [ {
				xtype : 'textfield',
				fieldLabel : '设备IP',
				name : 'DEV_IP',
				value : queryVals["DEV_IP"],
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
			}]
		},{
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
				xtype : 'textfield',
				fieldLabel : '工程编号',
				name : 'RELATED_PROJECT_CUID',
				value : grid.prjcode,
				hidden: true,
				queryCfg : {
					type : "string",
					relation : "=",
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
			items : [{
				xtype : 'asyncombox',
				fieldLabel : '所属EMS/SNMS',
				name : 'RELATED_EMS_CUID',
				emptyText : '请选所属EMS/SNMS',
				triggerAction : "all",
				width : 168,
				comboxCfg : {
					cfgParams : {
						code : 'EUIIP_EMS'
					}
				},
				queryCfg:{
					type : "string",
					relation : "like"
				}
			} ]
		},{
			layout : 'form',
			columnWidth : .5,
			defaults : {
				anchor : '-20'
			},
			items : [ {
				xtype : 'asyncombox',
				fieldLabel : '安装位置',
				name : 'RELATED_ACCESS_POINT',
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
			},
			{
				   layout : 'form',
					columnWidth : .5,
					defaults : {
						anchor : '-20'
					},
					items : [ {
							xtype : 'asyncombox',
							fieldLabel : '上联设备',
							name : 'RELATED_POS_CUID',
							emptyText : "请选择...",
							hideTrigger1 : true,
							triggerAll : true,
							loadData : false,
							editable : true,
							comboxCfg : {
								cfgParams : {
									code : 'RELATED_POS_NAME'
								}
							},
							queryCfg:{
								relation : '='
							}
							
						}]
				}];
		
		
		
		var panel = new Ext.Panel( {
			bodyStyle : 'margin-top:3px',
			autoScroll : false,
			id : 'onuPanel',
			border : 'false',
			height : 150,
			hideBorders : true,
			layout : 'column',
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
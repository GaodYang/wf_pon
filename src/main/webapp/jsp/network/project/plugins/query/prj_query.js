Ext.ns("Frame.grid.plugins.query");
$importcss(ctx + "/jsp/common/style/customize.css");

Frame.grid.plugins.query.prj_query = Ext.extend(Object, {
	constructor: function(grid){
		this.grid = grid;
		Frame.grid.plugins.query.prj_query.superclass.constructor.call(this);
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
				fieldLabel : '工程名称',
				name : 'PRJ_NAME',
				value : '',
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
				xtype : 'textfield',
				fieldLabel : '工程编号',
				name : 'PRJ_CODE',
				value : '',
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
			}]
		}];
		
		var panel = new Ext.Panel( {
			bodyStyle : 'margin-top:3px',
			autoScroll : false,
			id : 'onuPanel',
			border : 'false',
		//	height : 180,
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
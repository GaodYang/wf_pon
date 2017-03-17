Ext.ns("Frame.grid.plugins.query");
Frame.grid.plugins.query.cover_query = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.query.cover_query.superclass.constructor.call(this);
		var queryVals = {};
		var scope = this;
		
		
		this.relatedNe = new IRMS.combo.AsynCombox({
			fieldLabel : '关联箱体',
			name : 'RELATED_NE_CUID',
			blurMatch : 'both',
			anchor 	   : '90%',
			value : '',
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'GPONCOVERNEFROMNE'
				},
				queryCfg : {
					type : 'string',
					relation : '='
				}
			}
		});
		
		var _scope = this;
		  this.address = new Ext.form.TextField( {
					fieldLabel : '关联标准地址',
					name : 'ADDRLABELCN',
					width : 250,
					//value : queryVals["GPONCOVERADDRESS"],
					queryCfg : {
						type : "string",
						relation : "like",
						blurMatch : 'both'
					}
			});
       /* this.address = new IRMS.combo.AsynCombox({
			fieldLabel : '关联标准地址',
			allowBlank : true,
			name : 'STANDARD_ADDR',
			
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'GPONCOVERADDRESS'   
				},
				queryCfg : {
					type : 'string',
					relation : '='
				}
			},
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
					combo.expand();
					var parentCuid = _scope.relatedNe.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(parentCuid)){
						whereParams['RELATED_NE_CUID'] = {
			    				key : 'RELATED_NE_CUID',
								value : parentCuid,
								relation : '='	
			    		}
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				}  
			}
		});	*/
        
		var oltCuid = grid.oltCuid;
		
		var RELATED_DISTRICT_CUID = new IRMS.combo.AsynCombox({
			fieldLabel : '所属地市',
			triggerAction : "all",
			name : 'RELATED_DISTRICT_CUID',
			comboxCfg : {
				cfgParams : {
					code : 'DISTRICT_NAME'
				}
			},
			queryCfg:{
				type : "string",
				relation : "="
			}
		});
		var county = new IRMS.combo.AsynCombox({
        	fieldLabel : '区县',
			name : 'RELATED_COUNTY_CUID',
			comboxCfg : {
				boName:"XmlTemplateComboxBO",
				cfgParams : {
					code : 'COMBO_DISTRICT_COUNTY'
				}
			},
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
					var parentCuid = RELATED_DISTRICT_CUID.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(parentCuid)){
						whereParams['RELATED_SPACE_CUID'] = {
			    				key : 'RELATED_SPACE_CUID',
								value : parentCuid,
								relation : '='	
			    		}
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				}
			}
		});
        var lable_cn = {
        		xtype : 'textfield',
				fieldLabel : '覆盖范围描述',
				name : 'COVER_INFO',
				emptyText : '覆盖范围描述',
				width : 250,
				value : queryVals["COVER_INFO"],
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
		};
        var RELATED_COMMUNITY_LABEL = {
        		xtype : 'textfield',
				fieldLabel : '所属业务区',
				name : 'RELATED_COMMUNITY_LABEL',
				emptyText : '所属业务区',
				width : 250,
				value : queryVals["RELATED_COMMUNITY_LABEL"],
				queryCfg : {
					type : "string",
					relation : "like",
					blurMatch : 'both'
				}
		};
		
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
			items : [{items:[this.relatedNe]},
			         {items:[this.address]},
			         {items:[lable_cn]},
			         {items:[RELATED_COMMUNITY_LABEL]},
			         {items:[RELATED_DISTRICT_CUID]},
			         {items:[county]}
			         ]		
		});
		return panel;
  }
});
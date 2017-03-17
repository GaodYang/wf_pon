$importjs(ctx + "/jsp/common/jQuery.Hz2Py-min.js");
Ext.ns("Frame.grid.plugins.query");

Frame.grid.plugins.query.address_query = Ext.extend(Object, {
	constructor : function(grid) {
		this.grid = grid;
		Frame.grid.plugins.query.address_query.superclass.constructor
				.call(this);
		var _scope = this;
		var relatedcommunitycuid = new IRMS.combo.AsynCombox({
			fieldLabel : '所属业务区',
			name : 'RELATED_COMMUNITY_CUID',
			comboxCfg : {
				boName:"XmlTemplateComboxBO",
				cfgParams : {
					code : 'COMBO_BUSINESS_COMMUNITY'
				}
			}
		
		});
		var province = new IRMS.combo.AsynCombox({
			fieldLabel : '省份',
			name : 'PROVINCE',
			comboxCfg : {
				boName:"XmlTemplateComboxBO",
				cfgParams : {
					code : 'COMBO_DISTRICT_PROVINCE'
				}
			},
			listeners:{
				"change" : function(_this,newValue,oldValue){
					if(!Ext.isEmpty(newValue)){
						  city.setValue("");
						  county.setValue("");
					}
				}  
			}
		});
        var city = new IRMS.combo.AsynCombox({
        	fieldLabel : '地市',
			name : 'CITY',
			comboxCfg : {
				boName:"XmlTemplateComboxBO",
				cfgParams : {
					code : 'COMBO_DISTRICT_CITY'
				}
			},
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
					var parentCuid = province.getValue();
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
				},
				"change" : function(_this,newValue,oldValue){
					if(!Ext.isEmpty(newValue)){
						county.setValue("");
					}
				} 
			}
		});	
        var county = new IRMS.combo.AsynCombox({
        	fieldLabel : '区县',
			name : 'COUNTY',
			comboxCfg : {
				boName:"XmlTemplateComboxBO",
				cfgParams : {
					code : 'COMBO_DISTRICT_COUNTY'
				}
			},
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
					var parentCuid = city.getValue();
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
				fieldLabel : '地址名称',
				name : 'LABEL_CN',
				emptyText : '请填写地址名称',
				width : 250,
				listeners : {
					"change" : function(_this,newValue,oldValue){
						if(!Ext.isEmpty(newValue)){
							var label_cn = pinyin.getCamelChars(newValue.toLowerCase());
							_this.queryCfg = {};
							_this.queryCfg.type = "append";
							_this.queryCfg.sqlTemplate = "(LABEL_CN LIKE "+"'%"+newValue+"%'"+" OR PINYIN LIKE '%"+label_cn+"%' )";
						}
						return true;
					}
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
			items : [{
				items : [lable_cn]
			},{
				items : [province]
			},{
				items : [city]
			},{
				items : [county]
			},{
				items : [relatedcommunitycuid]
			}]		
		});
		return panel;
  }
});
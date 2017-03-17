$importjs(ctx + "/jsp/network/equip/ptp/ptp_onu.js");
$importjs(ctx + "/jsp/network/equip/ptp/ptp_pos.js");
$importcss(ctx + "/jsp/common/style/customize.css");
$importjs(ctx + "/dwr/interface/PtpManageAction.js");
$importjs(ctx + "/jsp/network/equip/plugins/renderer/equip_renderer.js");

NETWORK.ptp = Ext.extend(Ext.Panel, {
	id 			: 'NETWORK.ptp' ,
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	//机盘传过来的参数
    cardCuid 	: '',
    cardName	: '',
    devCuid		: '',
    devName		: '',
    devTable	: '',
    initComponent : function() {
		var panelType = "";
		if('onu'==this.devTable){
			panelType = "ONU_PTP_MANAGE";
		}else if('pos'==this.devTable){
			panelType = "POS_PTP_MANAGE";
		}
		var _scope = this;

		var baseQueryItem = [{
             items:[{
            	xtype 		: 'textfield',
     			fieldLabel 	: '端口名称',
     			name 		: 'LABEL_CN',
     			id          :  this.devTable +  'labelcnSelect',
     			queryCfg 	: {
     				type 		: "string",
     				relation 	: "like",
     				blurMatch : 'both'
     			}
             }]},{
                 items:[{
 	            	xtype : 'asyncombox',
 	     			fieldLabel : '所属设备',
 	     			triggerAll 		: true,
 	     			disabled		: this._disabled,
 	     			hideTrigger1 	: true,
 	     			name : 'RELATED_NE_CUID',
 	     			id   : this.devTable +  'devSelectNeCombo',
 	     			comboxCfg  : {	
 	     				boName : 'XmlTemplateComboxBO',
 	     				cfgParams : {
 	     					code : 'DEV_NE_SELECT'   
 	     				},
 	     				queryParams : {
 	     					TYPE : {
 	     						key : 'TYPE',
 	     						value : this.devTable,
 	     						relation : '='
 	     					}
 	     				}
 	     			}
 	             }]},{
             items:[{
                xtype : 'asyncombox',
     			fieldLabel      :  '板卡名称',
     			triggerAll 		: true,
     			disabled		: this._disabled,
     			hideTrigger1 	: true,
     			name       : 'RELATED_CARD_CUID',
     			id         : this.devTable +  'devSelectCardCombo',
     			anchor 	   : '90%',
     			comboxCfg  : {	
     				boName : 'XmlTemplateComboxBO',
     				cfgParams : {
     					code : 'RELATED_CARD_NAME'   
     				},
     				queryParams : {
     					LABEL_CN : {
     						key : 'LABEL_CN',
     						value : '',
     						relation : 'like'
     					}
     				}
     			},
     			//监听事件,在查询之前得到已选OLT的值,根据OLT的端口
     			listeners:{
     				"beforequery" : function(_this,newValue,oldValue){
     				    Ext.getCmp(this.devTable + 'devSelectCardCombo').clearValue();
     					var neCuid = Ext.getCmp(this.devTable + 'devSelectNeCombo').value;
     					var whereParams = {};
     					if(Ext.isEmpty(neCuid)){
     						
     					}else{
     						whereParams['RELATED_DEVICE_CUID'] = {
     			    				key : 'RELATED_DEVICE_CUID',
     								value : neCuid,
     								relation : '='	
     			    		}
     					}
     					_this.combo.comboxCfg.queryParams=whereParams;
     					return true;
     				}  
     			} }]}, {
                items:[{
	               xtype : 'asyncombox',
	   			   fieldLabel: '所属区域',
	               name: 'RELATED_DISTRICT_CUID',
	               id  :this.devTable +  'devSelectDistrictCombo',
	               hideTrigger1 : true,
	   			   triggerAll : true,
	   			   loadData : false,
	   			   editable : false,
	               anchor: '90%',
	               queryCfg: {
	                   type: "string",
	                   relation: "like",
	                   blurMatch: 'right'
	               },
	               comboxCfg: {
	                   boName: 'XmlTemplateComboxBO',
	                   cfgParams: {
	                       code: 'DISTRICT_NAME'
	                   }
	               },
	               editable: true
	           }]},{
                items:[{
                	xtype 		: 'textfield',
         			fieldLabel 	: '端口VLAN',
         			id : this.devTable +  'vlanField',
         			name 		: 'VLAN',
         			queryCfg 	: {
         				type 		: "string",
         				relation 	: "like",
         				blurMatch : 'both'
         			}}]}
		 ];
			
		var bbarArray =[];
		bbarArray.push({
			xtype		:'button',
            scope      	: this,
            text       	: '新增',
            iconCls    	: 'c_add',
            id			: this.devTable +  'AnPtp.add',
            handler    	: this.doAdd
		},{
			xtype		:'button',
            scope      	: this,
            text       	: '修改',
            iconCls    	: 'c_pencil',
            id			: this.devTable +  'AnPtp.modify',
            handler    	: this.doUpdate
		},{
			xtype		:'button',
			id			: this.devTable +  'AnPtp.delete',
            scope      	: this,
            text       	: '删除',
            iconCls    	: 'c_delete',
            id			: this.devTable +  'AnPtp.delete',
            handler    	: this.doDelete
		});

		bbarArray.push('->',{
				xtype		:'button',
                scope      	: this,
                text       	: '查询',
                iconCls    	: 'c_find',
                handler    	: this.doQuery
			},{
				xtype		:'button',
                scope      	: this,
                text       	: '重置',
                iconCls    	: 'c_arrow_rotate_anticlockwise',
                handler    	: this.doClear
			});
		this.queryPanel = new Ext.FormPanel( {
			region		:'north',
			border 		: false,
			id          :  this.devTable +  'ptpPanel',
			height		: 100,
			labelAlign 	: 'left',
			frame 		: true,
			autoDestroy : true,
			layout 		: 'column',
			layout 		: 'column',
			defaults 	: {
				layout 		: 'form',
				columnWidth : .30,
				defaults 	: {
					anchor 		: '90%'
				}
			},
			bbar:bbarArray,
			items 		: [baseQueryItem ]
		});
		this.PtpGridPanel = new Frame.grid.ResGridPanel({
			enableQueryPanel	: false,
			region 				: 'center',
			rendererPlugin		: 'equip_renderer',
			border 				: false,
			header 				: false,
			loadData			: false,
			hasBbar	 			: true,
			enableContextMenu : true,
			enableGeneralQuery: false,
			pageSize : 20,
			gridCfg 			: {
				boName 	  			: 'XmlTemplateGridBO',
//				exportBoName        : 'EquipGridExportBO',
				cfgParams : {
					templateId 		: panelType
				}
			}
		});
		this.items = [this.queryPanel,this.PtpGridPanel];
		this.initData();
		this.initTask();
	//	var task = new Ext.util.DelayedTask(this.initTask,this);
	//	task.delay(200);
		NETWORK.ptp.superclass.initComponent.call(this);
	},
	initData : function() {
		var table = this.devTable;
		/*if(table == 'onu'){
			Ext.getCmp(this.devTable + 'devSelectNeCombo').comboxCfg.panelType = 'onu';
		}else if (table == 'pos'){
			Ext.getCmp(this.devTable + 'devSelectNeCombo').comboxCfg.panelType = 'pos';
		}*/
			if(this.devName!='1'){
				var result =  {
						text : this.devName,
						value : this.devCuid,
						data : []
				};
				Ext.getCmp(this.devTable + 'devSelectNeCombo').setValue(result);
			}
			if(this.cardName!='1'){
				var result =  {
						text : this.cardName,
						value : this.cardCuid,
						data : []
				};
				Ext.getCmp(this.devTable + 'devSelectCardCombo').setValue(result);
			}
	},
	getWhereParams	:	function(){
    	var whereParams = {};
    	if(Ext.getCmp(this.devTable + 'devSelectCardCombo') && !Ext.isEmpty(Ext.getCmp(this.devTable + 'devSelectCardCombo').getValue())){
    		whereParams[Ext.getCmp(this.devTable + 'devSelectCardCombo').getName()] = {
    				key 		: Ext.getCmp(this.devTable + 'devSelectCardCombo').getName(),
					value 		: Ext.getCmp(this.devTable + 'devSelectCardCombo').getValue(),
					relation 	: Ext.getCmp(this.devTable + 'devSelectCardCombo').relation	
    		}
    	}
    	if(Ext.getCmp(this.devTable + 'devSelectNeCombo') && !Ext.isEmpty(Ext.getCmp(this.devTable + 'devSelectNeCombo').getValue())){
    		whereParams[Ext.getCmp(this.devTable + 'devSelectNeCombo').getName()] = {
    				key 		: Ext.getCmp(this.devTable + 'devSelectNeCombo').getName(),
					value 		: Ext.getCmp(this.devTable + 'devSelectNeCombo').getValue(),
					relation 	: Ext.getCmp(this.devTable + 'devSelectNeCombo').relation	
    		}
    	}
    	if(Ext.getCmp(this.devTable + 'labelcnSelect') && !Ext.isEmpty(Ext.getCmp(this.devTable + 'labelcnSelect').getValue())){
    		whereParams[Ext.getCmp(this.devTable + 'labelcnSelect').getName()] = {
    				key 		: Ext.getCmp(this.devTable + 'labelcnSelect').getName(),
    				value 		: "%"+Ext.getCmp(this.devTable + 'labelcnSelect').getValue()+"%",
    				relation 	: Ext.getCmp(this.devTable + 'labelcnSelect').queryCfg.relation
    		}
    	}
    	if(Ext.getCmp(this.devTable + 'devSelectDistrictCombo') && !Ext.isEmpty(Ext.getCmp(this.devTable + 'devSelectDistrictCombo').getValue())){
    		whereParams[Ext.getCmp(this.devTable + 'devSelectDistrictCombo').getName()] = {
    				key 		: Ext.getCmp(this.devTable + 'devSelectDistrictCombo').getName(),
    				value 		: Ext.getCmp(this.devTable + 'devSelectDistrictCombo').getValue()+"%",
    				relation 	: Ext.getCmp(this.devTable + 'devSelectDistrictCombo').queryCfg.relation
    		}
    	}
    	if(Ext.getCmp(this.devTable + 'vlanField') && !Ext.isEmpty(Ext.getCmp(this.devTable + 'vlanField').getValue())){
    		whereParams[Ext.getCmp(this.devTable + 'vlanField').getName()] = {
    				key 		: Ext.getCmp(this.devTable + 'vlanField').getName(),
    				value 		: "%"+Ext.getCmp(this.devTable + 'vlanField').getValue()+"%",
    				relation 	: Ext.getCmp(this.devTable + 'vlanField').queryCfg.relation
    		}
    	}
    	
    	return whereParams;
    },
    showDetailView : function(record, type, winTitlw, readOnly, object) {
    	var ptpCuid = '';
    	if(!Ext.isEmpty(record)){
    		ptpCuid = record.json.CUID;
    	}
    	var ptpEditPanel = "";
    	if('pos'==this.devTable){
    		ptpEditPanel = "NETWORK.PtpPos";
    	}else if('onu'==this.devTable){
			ptpEditPanel = "NETWORK.PtpOnu";
		}else{
			Ext.Msg.alert('提示', '设备类型值错误，请联系管理员!');
			return;
		}
    	if(!Ext.isEmpty(ptpEditPanel)){
    		var config = {
    				winTitle : winTitlw,
    				width 		: (window.screen.width/2 -50),
    				height 		: (window.screen.height/2 -70),
    				region 		: 'center',
    				_ptpCuid 	: ptpCuid,
    				_cardCuid	: this.cardCuid,
    				_cardName	: this.cardName,
    				_readOnly 	: readOnly,
    				_devTable	: this.devTable,
    				_devCuid	: this.devCuid,
    				_devName	: this.devName,
    				_type 		: type
    			}
    			openWindow(ptpEditPanel,true,config,function(_result, _scope,_options) {
    				if(!Ext.isEmpty(_result) && !Ext.isEmpty(type)){
    					PtpManageAction.queryLoadPtpByCuid(_result.CUID,function(_data){
    						if (!Ext.isEmpty(_data) && type == 'add') {
    							if (!Ext.isEmpty(_data) && type == 'add') {
    								object.PtpGridPanel.grid.getStore().loadData( {
    									'list' : [_data]
    								}, true);
    							}
    						}else if(!Ext.isEmpty(_data) && type == 'modify'){
    							object.PtpGridPanel.grid.getStore().reload();
    						}
    					});
    				}
    			}, object,object.scope);
    	}
	},
    doAdd	: function(){
		this.showDetailView('', 'add', '新增端口数据', false, this);
	},
	doDelete	: function(){
		var _scope = this;
		var selections = _scope.PtpGridPanel.grid.getSelectionModel().getSelections();
		if (selections.length > 0) {
			var cuids = [];
			var flag = true;
			Ext.each(selections, function(item) {
				
				if(item.json.PORT_STATE != '1'){
					
					Ext.Msg.alert('提示',item.json.LABEL_CN + " 不是空闲端口，不能删除！" );
					flag = false;
					return ;
				}
				cuids.push(item.json);
			});
			if(!flag){
				return;
			}
			DWREngine.setAsync(false);
			Ext.Msg.confirm('提示','是否真的要删除选中的端口？',function(btn) {
				if (btn == 'yes') {
					
					PtpManageAction.deletePtpInfo(cuids,function() {
							
						Ext.Msg.alert('提示',"删除成功！");
						_scope.PtpGridPanel.grid.getStore().remove(selections);
					});
				}
			});
			DWREngine.setAsync(true);
		} else {
			Ext.Msg.alert('提示', '请选择需要删除的数据!');
			return;
		}
	},
	doUpdate	: function(){
		var record = this.PtpGridPanel.grid.getSelectionModel().getSelected();
		if (!Ext.isEmpty(record)) {
			if(this.PtpGridPanel.grid.getSelectionModel().selections.length > 1){
				Ext.Msg.alert('提示', '只能选择一条数据修改!');
				return;
			}else{
				this.showDetailView(record, 'modify', '修改端口数据', false,this);
			}
		} else {
			Ext.Msg.alert('提示', '请选择需要修改的数据!');
			return;
		}
	},
	initTask:function(){
		this.showComponent();
	//	this.doQuery();
	},
	showComponent:function(){
		if('pos'==this.devTable){
			Ext.getCmp(this.devTable + 'vlanField').hide();
		}
	},
    doQuery : function() {
		this.PtpGridPanel.doLoad(this.getWhereParams());
	},
	doClear	:	function(){
//		this.PtpGridPanel.grid.startEditing(0, 0);
		if(Ext.getCmp(this.devTable + 'devSelectCombo')){
			Ext.getCmp(this.devTable + 'devSelectCombo').reset();
		}
		if(Ext.getCmp(this.devTable + 'labelcnSelect')){
			Ext.getCmp(this.devTable + 'labelcnSelect').reset();
		}
		if(Ext.getCmp(this.devTable + 'devSelectNeCombo')){
			Ext.getCmp(this.devTable + 'devSelectNeCombo').setValue(null);
		}
		if(Ext.getCmp(this.devTable + 'vlanField')){
			Ext.getCmp(this.devTable + 'vlanField').reset();
		}
		if(Ext.getCmp(this.devTable + 'devSelectDistrictCombo')){
			Ext.getCmp(this.devTable + 'devSelectDistrictCombo').reset();
		}
	}
});

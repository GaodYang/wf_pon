$importjs(ctx + "/jsp/common/util.js");
$importjs(ctx + "/dwr/interface/OnuManageAction.js");
$importjs(ctx + "/jsp/common/ux/DateTimeField.js");
Ext.ns('NETWORK');
 
NETWORK.onu_panel = Ext.extend(Ext.Panel, {
	id 			: 'NETWORK.OnuPanel',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'form',
	autoScroll  : true,
	modifyflag	: false,
	_record 	: null,
	_returnFlag : '',
	_disabled 	: this._disabled ? this._disabled : true,
    _type		: this._type?this._type:'',
    _returnJson	: '',
    returnRecord: null,
    oldPort     :'',
    
	initComponent : function() {
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
		//判断是添加还是修改
		var isNeedValid = '';
		//判断是否是第一次修改
		var modifyoltfalg = true;
		//修改的初始化值
		var labelcn = '';
		var scope = this;
		if(this._type == 'add'){
			isNeedValid = 'add';
			this._disabled = false;
		}
		if(this._type == 'modify'){
			isNeedValid = 'modify';
			this._disabled = false;
		}
		this.cuid = new Ext.form.TextField( {
			name 	: 'CUID',
			hidden 	: true
		});
		
		var config=this;

		DWREngine.setAsync(true);
		this.onuTextFiled = new Ext.form.TextField( {
			fieldLabel  : getNotNullFont('ONU名称') ,
			anchor 		: '90%',
			disabled	:this._disabled,
			allowBlank  : false,
			name 		: 'LABEL_CN',
			invalidText : '名称已经存在',
			validator   : function(name) {
				var flag = true;
				if(isNeedValid == 'modify' && modifyoltfalg){
					labelcn = name;
					modifyoltfalg = false;
				}
				if ((!Ext.isEmpty(name) && isNeedValid == 'add') || (isNeedValid == 'modify' && labelcn != name)) {
					DWREngine.setAsync(false);
					OnuManageAction.isOnuNameExist(name, function(_data) {
						flag =  !_data;
					});
					DWREngine.setAsync(true);
				}
				return flag;
			}
		});	
		
		this.relatedaccesspoint = new IRMS.combo.AsynCombox({
			fieldLabel : getNotNullFont('安装位置'),
			triggerAll 		: true,
			editable : true,
			allowBlank  : false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			name       : 'RELATED_ACCESS_POINT',
			anchor 	   : '90%',
			blurMatch : 'both',
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
		});
		
		this.relatedemscuid = new IRMS.combo.AsynCombox( {
			fieldLabel : '所属EMS/SNMS',
			name : 'RELATED_EMS_CUID',
			id : 'relatedems',
			emptyText : '请输入所属EMS/SNMS',
			triggerAll 		: true,
			anchor : '100%',
			disabled : this._disabled,
			blurMatch : 'both',
			comboxCfg  : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'EUIIP_EMS'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			}
		});
		
		var _scope = this;
		this.relatedposcuid = new IRMS.combo.AsynCombox({
			fieldLabel : getNotNullFont('上联设备') ,
			triggerAll 		: true,
			disabled		: this._disabled,
			hideTrigger1 	: false,
			allowBlank 		: false,
			name       : 'RELATED_POS_CUID',
			anchor 	   : '90%',
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_POS_NAME'   
				}
			},
			listeners	: {
				"select"	: function(_this,a,b){

					scope.relatedposportcuid.setValue("");
				}
			}
			
		});
		this.relatedposportcuid = new IRMS.combo.AsynCombox({
			fieldLabel : getNotNullFont('上联设备端口') ,
			name       : 'RELATED_POS_PORT_CUID',
			anchor 	   : '90%',
			triggerAll : true,
			hideTrigger1 : false,
			allowBlank : true,
			blurMatch : 'both',
			triggerAll : true,
			editable : true,
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'RELATED_POS_PORT_CUID'   
				},
				queryParams : {
					LABEL_CN : {
						key : 'LABEL_CN',
						value : '',
						relation : 'like'
					}
				}
			},
			// 监听事件,在查询之前得到已选网元的值,根据网元信息查找相应网元下的端口
			listeners:{
				"beforequery" : function(_this,newValue,oldValue){
//				    config.relatedPosCuid.setValue("")
					var oltCuid = _scope.relatedposcuid.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(oltCuid)){
						whereParams['RELATED_NE_CUID'] = {
			    				key : 'RELATED_NE_CUID',
								value : oltCuid,
								relation : '='	
			    		};
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				}  
			}
		});
		
		this.model = new  IRMS.combo.AsynCombox( {
			fieldLabel 	: getNotNullFont('规格型号') ,
			id 			: 'MODELID',
			name 		: 'MODEL',
			emptyText 	: '请输入网元型号',
			anchor 		: '90%',
			allowBlank  : false,
			triggerAll 	: true,
			disabled	: this._disabled,
			comboxCfg  : {	
				boName : 'XmlTemplateComboxBO',
				cfgParams : {  
					code : 'MODEL_ONU'   
				}
			},
			listeners	: {
				"beforequery" : function(_this,newValue,oldValue){
					var relatedvendorcuid = _scope.relatedvendorcuid.getValue();
					var whereParams = {};
					if(!Ext.isEmpty(relatedvendorcuid)){
						whereParams['VENDOR_NAME'] = {
			    				key : 'VENDOR_NAME',
								value : relatedvendorcuid,
								relation : '='	
			    		};
					}
					_this.combo.comboxCfg.queryParams=whereParams;
					return true;
				},
				"change"	: function(_this,a,b){
					if(!Ext.isEmpty(a)){
						DWREngine.setAsync(false);
						OnuManageAction.queryVendorByModel(a, function(_data) {
							scope.relatedvendorcuid.setValue(_data.value);
						});
						DWREngine.setAsync(true);
					}
				}
			}
		});
		
		this.relatedvendorcuid = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('厂家'),
			id 				: 'VENDOR',
			name 			: 'RELATED_VENDOR_CUID',
			anchor 			: '90%',
			triggerAll 		: true,
			disabled		: this._disabled,
			hideTrigger1 	: false,
			allowBlank 		: false,
			editable 		: false,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ALLVENDOR'
				}
			},
			listeners		: {
				"change"	: function(_this,newValue,oldValue){
					scope.model.setValue("");
				}
			}
		});
		
		this.fttx = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('接入方式'),
			name 			: 'FTTX',
			anchor 			: '90%',
			triggerAll 		: true,
			allowBlank  	: false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			editable 		: false,
			config			: config,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'FTTX'
				}
			}
		});
		
	/*	this.onutype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('ONU类型'),
			name 			: 'ONU_TYPE',
			anchor 			: '90%',
			triggerAll 		: true,
			allowBlank  	: false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			editable 		: false,
			config			: config,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ONUTYPE'
				}
			}
		});
		*/
		this.ownership = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('资产归属'),
			name 			: 'OWNERSHIP',
			anchor 			: '90%',
			value			: 1,
			triggerAll 		: true,
			allowBlank 		: false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			editable 		: false,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'OWNERSHIP_NM'
				}
			}
		});
		
		this.ownershipman = new  IRMS.combo.AsynCombox( {
			fieldLabel : getNotNullFont('资产归属人'),
			name : 'OWNERSHIP_MAN',
			anchor : '90%',
			editable : false,
			allowBlank  : false,
			triggerAll 		: true,
			hideTrigger1 	: true,
			value : 1,
			comboxCfg : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'OWNERMENSHIP'
				}
			}
		});
		
		this.preserver = new Ext.form.TextField( {
			fieldLabel : '维护班组',
			anchor 	   : '90%',
			name       : 'PRESERVER'
			
		});
		
		// 定义ext的ComboBox静态值
		var channelData = [['1','自维'],['2','代维']];
		var channelStore = new Ext.data.SimpleStore({
			 fields:['channel_id','channel_name'],
			 disabled:this._disabled,
		     data:channelData
		});
		this.maintmode = new  Ext.form.ComboBox( {
			fieldLabel : '维护方式',
			name : 'MAINT_MODE',
			anchor : '90%',
			typeAhead: true,
			value : '1',
			allowBlank  : false,
	        triggerAction: 'all',
	        editable : false,	
			store : channelData,
			valueField : 'channel_id',
			disabled:this._disabled,
	        displayField : 'channel_name'
		});
		
		this.devip = new Ext.form.TextField( {
			fieldLabel  : '设备IP地址',
			anchor 		: '90%',
			disabled	: this._disabled,
			regex : /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
			regexText : "请输入正确的IP格式！",
			name 		: 'DEV_IP'
		});
		
		this.livecycle = new  IRMS.combo.AsynCombox( {
			fieldLabel : getNotNullFont('生命周期状态'),
			name 			: 'LIVE_CYCLE',
			anchor 			: '90%',
			allowBlank  	: false,
			editable 		: false,
			triggerAll 		: true,
			hideTrigger1 	: true,
			value 			: 1,
			comboxCfg : {
				boName : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'LIVECYCLE'
				}
			}
		});
		
		this.softversion = new Ext.form.TextField( {
			fieldLabel 	: '软件版本',
			anchor 		: '90%',
			disabled	: this._disabled,
			name 		: 'SOFT_VERSION'
		});
		this.portnumber = new Ext.form.TextField( {
			fieldLabel :'宽带端口数量',
			allowBlank : true,
			anchor : '90%',
			name   : 'PORT_NUMBER'
		});
		this.maint_person = new Ext.form.TextField( {
			fieldLabel : getNotNullFont('一线数据维护人（代维/一线)'),
			allowBlank : false,
			anchor : '90%',
			name : 'MAINT_PERSON'
		});	
		this.data_quality_person = new Ext.form.TextField( {
			fieldLabel : getNotNullFont('数据质量责任人（移动）'),
			allowBlank : false,
			anchor : '90%',
			name : 'DATA_QUALITY_PERSON'
		});	
		this.accesstype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('接入类型'),
			name 			: 'ACCESS_TYPE',
			anchor 			: '90%',
			triggerAll 		: true,
			allowBlank  	: false,
			hideTrigger1 	: true,
			editable 		: false,
			config			: config,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ACCESSTYPE'
				}
			}
		});
		
		this.onuid = new Ext.form.TextField( {
			fieldLabel 	: 'ONU_ID号',
			anchor 	   	: '90%',
			disabled   	: this._disabled,
			name       	: 'ONU_ID'
		});
		
		var config = this;
		this.authtype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('ONU的认证类型'),
			name 			: 'AUTH_TYPE',
			anchor 			: '90%',
			triggerAll 		: true,
			allowBlank  	: false,
			disabled		: this._disabled,
			hideTrigger1 	: true,
			editable 		: false,
			config			: config,
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'AUTHTYPE'
				}
			},
			listeners:{
				"change" : function(combo,record,number){
				  	var id=combo.getValue();  
					if(id == 1){
						config.seqno.show();
						config.macaddress.hide();
						config.password.hide();
						config.logicid.hide();
					}else if(id == 2){
						config.seqno.hide();
						config.macaddress.show();
						config.password.hide();
						config.logicid.hide();
					}else if(id == 3){
						config.seqno.hide();
						config.macaddress.hide();
						config.password.show();
						config.logicid.hide();
					}else{
						config.seqno.hide();
						config.macaddress.hide();
						config.password.hide();
						config.logicid.show();
					}
				}  
			}
		});
		
		this.password = new Ext.form.TextField({
			fieldLabel : '密码',
			anchor 	   : '90%',
			disabled   : this._disabled,
			hidden	   : true,
			name       : 'PASSWORD'
		});
		
		this.logicid = new Ext.form.TextField({
			fieldLabel : 'LOGICID',
			anchor 	   : '90%',
			disabled   : this._disabled,
			hidden	   : true,
			name       : 'LOGICID'
		});
		
		this.macaddress = new Ext.form.TextField( {
			fieldLabel  : 'MAC地址',
			anchor 		: '90%',
			disabled	: this._disabled,
			hidden	    : true,
			regex : /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/,
			regexText : "请输入正确的MAC地址格式！注意分隔符为:",
			name 		: 'MAC_ADDR'
		});
		
		this.seqno = new Ext.form.TextField( {
			fieldLabel 	: 'ONU设备SN/MAC',
			anchor 		: '90%',
			disabled	: this._disabled,
			// hidden	   	: true,
			name 		: 'SEQNO'
		});
		
		this.creattime = new  Ext.ux.DateTimeField ({
			fieldLabel 	: '建设日期',
			name 		: 'CREATE_TIME',
			disabled	: this._disabled,
			value    	: new Date(),
			allowBlank  : true,
			disabled		: this._disabled,
			triggerAll 		: true,
			hideTrigger1 	: true,
			editable 	: false,
			anchor 		: '90%',
			format		: 'Y-m-d H:i:s'
		});
		
		this.relatedprojectcuid = new Ext.form.TextField({
			fieldLabel	: '所属工程',
			name 		: 'RELATED_PROJECT_CUID',
			anchor		: '90%',
			disabled	: this._disabled
		});
		
		this.remark = new Ext.form.TextArea( {
			fieldLabel 	: '备注',
			anchor 		: '90%',
			disabled	: this._disabled,
			name 		: 'REMARK'
		});
		
		
		this.showEquipmentInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			items : [{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.onuTextFiled]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.devip]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedaccesspoint]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedemscuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedposcuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedposportcuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedvendorcuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.model]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.ownership]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.ownershipman]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.preserver]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.maintmode]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.fttx]
				}
				]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.cuid]
				}/*,{
					columnWidth : .5,
					layout : 'form',
					items:[this.devip]
				}*/]
			}]
		});
		
		this.showManagementInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			items : [{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.livecycle]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.softversion]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.accesstype]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.onuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.authtype]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.password]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.logicid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.macaddress]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.seqno]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.creattime]
				}]
			
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatedprojectcuid]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.portnumber]
				}
				]
			
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.maint_person]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.data_quality_person]
				}
				]
			
			}
			]
		});
		
		this.showRemarkInfoPanel = new Ext.FormPanel( {
			region		: 'center',
			border 		: false,
			labelAlign 	: 'left',
			frame 		: true,
			autoScroll  : true,
			autoDestroy : true,
			layout 		: 'form',
			items 		: [this.remark]
		});
		
		var filedSet1 = new Ext.form.FieldSet({
			title 			: '设备属性',
			collapsible 	: true,
			frame			: false,
			region			: 'center',
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			items			: [this.showEquipmentInfoPanel]
		});
		
		var filedSet2 = new Ext.form.FieldSet({
			title 			: '管理属性',
			collapsible 	: true,
			frame			: true,
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			collapsed 		: true,
			items:[this.showManagementInfoPanel]
		});
		
		var filedSet3 = new Ext.form.FieldSet({
			title 			: '备注',
			collapsible 	: true,
			frame			: true,
			autoHeight		: true,
			titleCollapse 	: true,
			shadow 			: true,
			collapsed 		: true,
			items:[this.showRemarkInfoPanel]
		});
		
		this.bbar = [ 
				'->',{
					xtype 		: 'button',
					iconCls 	: 'c_accept',
					text 		: '确定',
					scope		: this,
					hideMode	: 'offsets',
					hidden		: this._disabled,
					handler 	: this.save
				}, {
					xtype 	: 'button',
					iconCls : 'c_cancel',
					text 	: '关闭',
					scope 	: this,
					handler : this.cancel
				} 
		];
		this.items = [filedSet1,filedSet2,filedSet3];
		this.initData();
		NETWORK.onu_panel.superclass.initComponent.call(this);
	},
	//修改赋初值
	initData : function() {
		oldPort = '';
		if (!Ext.isEmpty(this._cuid)) {
			this.modifyflag = true;
			this.doRefresh();
			this.modifyflag = false;
		}
	},
    save:function(){
		var scope = this;
		if(!this.showEquipmentInfoPanel.getForm().isValid()){
			
			Ext.Msg.alert('提示','存在必填的设备属性');
		}else if(!this.showManagementInfoPanel.getForm().isValid()){
			
			Ext.Msg.alert('提示','存在必填的管理属性');
		}else{
			if(!Ext.isEmpty(this._type)){
	    		if(this._type=='modify'){
	    			var cuidRecord = scope._cuid;
					var obj = scope.getOnuInfoData();
					obj.CUID = cuidRecord;
	    			OnuManageAction.modifyOnuInfo(obj,function(){

							Ext.Msg.alert('提示','修改成功');
							if(scope.parent != null){
								scope.parent.close();
							}
	    			});
	    		}else if(this._type=='add'){
	    			
						var onuInfo = scope.getOnuInfoData();
						OnuManageAction.addOnuInfo(onuInfo, function() {

							Ext.Msg.alert('提示','添加成功');
							scope.parent.close();
						});
	    		}
	    	}else{
				scope.cancel();
			}
		}
    	
    },
    
    formatTime	:	function(dateTime){
    	if(!Ext.isEmpty(dateTime)){
    		return dateTime.format('Y-m-d H:i:s');
    	}
    	return '';
    },
    
    getOnuInfoData : function() {
    	var LABEL_CN = this.showEquipmentInfoPanel.getForm().findField('LABEL_CN').getValue();
    	var CUID = this.showEquipmentInfoPanel.getForm().findField('CUID').getValue();
    	var RELATED_ACCESS_POINT = this.showEquipmentInfoPanel.getForm().findField('RELATED_ACCESS_POINT').getValue();
    	var RELATED_EMS_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_EMS_CUID').getValue();
    	var RELATED_POS_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_POS_CUID').getValue();
    	var RELATED_POS_PORT_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_POS_PORT_CUID').getValue();
    	var MODEL = this.showEquipmentInfoPanel.getForm().findField('MODEL').getValue();
    	var RELATED_VENDOR_CUID = this.showEquipmentInfoPanel.getForm().findField('RELATED_VENDOR_CUID').getValue();
    	var FTTX = this.showEquipmentInfoPanel.getForm().findField('FTTX').getValue();
    	var OWNERSHIP = this.showEquipmentInfoPanel.getForm().findField('OWNERSHIP').getValue();
    	var OWNERSHIP_MAN = this.showEquipmentInfoPanel.getForm().findField('OWNERSHIP_MAN').getValue();
    	var MAINT_PERSON = this.showManagementInfoPanel.getForm().findField('MAINT_PERSON').getValue();
		var DATA_QUALITY_PERSON = this.showManagementInfoPanel.getForm().findField('DATA_QUALITY_PERSON').getValue();
		var PORT_NUMBER = this.showManagementInfoPanel.getForm().findField('PORT_NUMBER').getValue();

    	
    	var PRESERVER = this.showEquipmentInfoPanel.getForm().findField('PRESERVER').getValue();
    	var MAINT_MODE = this.showEquipmentInfoPanel.getForm().findField('MAINT_MODE').getValue();
    	var DEV_IP = this.showEquipmentInfoPanel.getForm().findField('DEV_IP').getValue();
    	
    	var LIVE_CYCLE = this.showManagementInfoPanel.getForm().findField('LIVE_CYCLE').getValue();
    	var SOFT_VERSION = this.showManagementInfoPanel.getForm().findField('SOFT_VERSION').getValue();
    	var ACCESS_TYPE = this.showManagementInfoPanel.getForm().findField('ACCESS_TYPE').getValue();
    	var ONU_ID = this.showManagementInfoPanel.getForm().findField('ONU_ID').getValue();
    	var AUTH_TYPE = this.showManagementInfoPanel.getForm().findField('AUTH_TYPE').getValue();
    	var PASSWORD = this.showManagementInfoPanel.getForm().findField('PASSWORD').getValue();
    	var LOGICID = this.showManagementInfoPanel.getForm().findField('LOGICID').getValue();
    	var MAC_ADDR = this.showManagementInfoPanel.getForm().findField('MAC_ADDR').getValue();
    	var SEQNO = this.showManagementInfoPanel.getForm().findField('SEQNO').getValue();
    	var CREATE_TIME = this.showManagementInfoPanel.getForm().findField('CREATE_TIME').getValue();
    	var RELATED_PROJECT_CUID = this.showManagementInfoPanel.getForm().findField('RELATED_PROJECT_CUID').getValue();
    	
		var REMARK = null;
		if(!Ext.isEmpty(this.showRemarkInfoPanel.getForm().findField('REMARK'))){
			REMARK = this.showRemarkInfoPanel.getForm().findField('REMARK').getValue();
		}
		
		var OnuData = {
			
				LABEL_CN				: LABEL_CN,
				CUID					: CUID,
				RELATED_ACCESS_POINT 	: RELATED_ACCESS_POINT,
				RELATED_EMS_CUID		: RELATED_EMS_CUID,
				RELATED_POS_CUID		: RELATED_POS_CUID,
				RELATED_POS_PORT_CUID   : RELATED_POS_PORT_CUID,
				MODEL		  			: MODEL,
				RELATED_VENDOR_CUID		: RELATED_VENDOR_CUID,
				FTTX 				  	: FTTX,
				OWNERSHIP 		  		: OWNERSHIP,
				OWNERSHIP_MAN 		  	: OWNERSHIP_MAN,
				PORT_NUMBER             :PORT_NUMBER,
				MAINT_PERSON		  : MAINT_PERSON,
				DATA_QUALITY_PERSON	  : DATA_QUALITY_PERSON,
				PRESERVER 			  	: PRESERVER,
				MAINT_MODE 		  		: MAINT_MODE,
				DEV_IP			  		: DEV_IP,
				LIVE_CYCLE 		  		: LIVE_CYCLE,
				SOFT_VERSION 			: SOFT_VERSION,
				ACCESS_TYPE			  	: ACCESS_TYPE,
				ONU_ID				  	: ONU_ID,
				AUTH_TYPE  				: AUTH_TYPE,
				PASSWORD 			  	: PASSWORD,
				LOGICID			  		: LOGICID,
				MAC_ADDR				: MAC_ADDR,
				SEQNO				  	: SEQNO,
				CREATE_TIME	  			: CREATE_TIME,
				RELATED_PROJECT_CUID	: RELATED_PROJECT_CUID,
				REMARK				  	: REMARK,
				oldPort                 : oldPort
		};
		return OnuData;
	},
	doRefresh : function() {
		var cuid = this._cuid;
		var scope = this;
		DWREngine.setAsync(false);
		OnuManageAction.queryOnuByCuid(cuid, function(data) {
			var record = Ext.data.Record.create([]);
			data.CREATE_TIME = new Date(data.CREATE_TIME);
			var _data = new record(data);
			scope.showEquipmentInfoPanel.getForm().loadRecord(_data);
			scope.showManagementInfoPanel.getForm().loadRecord(_data);
			scope.showRemarkInfoPanel.getForm().loadRecord(_data);
			oldPort = data.RELATED_POS_PORT_CUID.value;
		});
		DWREngine.setAsync(true);
	},
    getResult:function(){
		return this._returnJson;
    },
	cancel : function() {
    	if (this.parent != null) {
			this.parent.close();
		}
    	return null;
	}
});
Ext.reg('NETWORK.OnuPanel', NETWORK.onu_panel);
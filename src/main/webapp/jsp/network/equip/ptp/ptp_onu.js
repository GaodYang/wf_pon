$importjs(ctx + "/dwr/interface/PtpManageAction.js");

Ext.ns('NETWORK');

NETWORK.ptp_onu = Ext.extend(Ext.Panel, {
	id 			: 'NETWORK.PtpOnu',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'form',
	autoScroll  : true,
	_record		: null,
	_ptpCuid	: '',
	_cardCuid	: '',
	_cardName	: '',
	_devCuid	: '',
	_devName	: '',
	_devTable	: '',
	_disabled 	: this._disabled ? this._disabled : true,
    _type		: this._type?this._type:'',
    _returnJson	: null,
    
	initComponent : function() {
		Ext.QuickTips.init();
		var _scope = this;
		Ext.form.Field.prototype.msgTarget = 'side';
		//判断是添加还是修改
		var isNeedValid = '';
		//判断是否是第一次修改
		var modifycardfalg = true;
		//判断是否是第一次修改VLAN
		var modifyvlanfalg = true;
		var _devTable = this._devTable;
		//FDN修改标示符
		var fdnFlag = true;
		//修改的初始化值
		var labelcn = '';
		var fdnValue = '';
		
		if(this._type == 'add'){
			isNeedValid = 'add';
			this._disabled = false;
		}
		if(this._type == 'modify'){
			isNeedValid = 'modify';
			this._disabled = false;
		}
		
		this.labelcn = new Ext.form.TextField({
			name   		: 'LABEL_CN',
			fieldLabel	: getNotNullFont('端口名称'),
			disabled    : this._disabled,
			allowBlank  : false,
			anchor 	   	: '95%'
		});
		
		this.portsubtype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('端口类型'),
			name 			: 'PORT_SUB_TYPE',
			triggerAll 		: true,
			disabled   		: this._disabled,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'ONUPORTSUBTYPE'
				}
			}
		});
		
		this.portrate = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: getNotNullFont('端口速率'),
			name 			: 'PORT_RATE',
			triggerAll 		: true,
			disabled   		: this._disabled,
			hideTrigger1 	: true,
			allowBlank  	: false,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'PORTRATE'
				}
			}
		});
		
		this.vlan = new Ext.form.TextField( {
			fieldLabel : '端口VLAN',
			anchor : '90%',
			name : 'VLAN',
			emptyText:'多个VLAN时，用逗号隔开'
		});
		
		this.portno = new Ext.form.NumberField({
			name   		: 'PORT_NO',
			fieldLabel	: getNotNullFont('端口编号'),
			allowBlank  : false,
			minValue 	: 0,
			maxValue	: 9999,
			maxLength 	: 4,
			maxLengthText :'该输入项的最大长度为4位数字',
			disabled   	: this._disabled,
			allowNegative : false,
			anchor 	   	: '90%'
		});
		this.porttype = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: '光电属性',
			name 			: 'PORT_TYPE',
			triggerAll 		: true,
			hideTrigger1 	: true,
			disabled   		: this._disabled,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'PORTTYPE'
				}
			}
		});
		this.relatednecuid = new IRMS.combo.AsynCombox( {
			fieldLabel : getNotNullFont('所属设备'),
			name 	   : 'RELATED_NE_CUID',
			emptyText  : '请选网元名称',
			anchor     : '90%',
			triggerAll 		: true,
			hideTrigger1 	: true,
			disabled   		: this._disabled,
			allowBlank  : false,
			comboxCfg  : {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'PORTTYPE'//TODO:
				}
			}
		});
		this.portstate = new  IRMS.combo.AsynCombox( {
			fieldLabel 		: '端口状态',
			name 			: 'PORT_STATE',
			triggerAll 		: true,
			hideTrigger1 	: true,
			readOnly        : true,
			anchor 			: '90%',
			comboxCfg 		: {
				boName 	  : 'XmlTemplateComboxBO',
				cfgParams : {
					code : 'PORTSTATE'
				}
			}
		});
		this.remark = new Ext.form.TextArea({
			fieldLabel : '备注',
			anchor : '95%',
			name : 'REMARK'
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
					columnWidth : 1,
					layout : 'form',
					items:[this.labelcn]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.portsubtype]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.portno]
				}]
			
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.portrate]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.relatednecuid]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.porttype]
				},{
					columnWidth : .5,
					layout : 'form',
					items:[this.vlan]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : .5,
					layout : 'form',
					items:[this.portstate]
				}]
			},{
				layout : 'column',
				items:[{
					columnWidth : 1,
					layout : 'form',
					items:[this.remark]
				}]
			}]
		});
		this.bbar = [ 
				'->',{
					xtype 		: 'button',
					iconCls 	: 'c_accept',
					text 		: '确定',
					scope		:this,
					hideMode	: 'offsets',
					hidden		: this._disabled,
					handler 	: this.save
				}, {
					xtype 		: 'button',
					iconCls 	: 'c_cancel',
					text 		: '关闭',
					scope 		: this,
					handler 	: this.cancel
				} 
		];
		this.items = [this.showEquipmentInfoPanel];
		this.initData();
		NETWORK.ptp_onu.superclass.initComponent.call(this);
	},
	//判断valn的输入值是否符合要求
	isVlanOK:function(){
		var data=this.vlan.getValue();
		var reg=/^(\d{1,4})$/;
		var flag=1;
		if(!Ext.isEmpty(data)){
			if(data.indexOf(',')>-1){
				var temp=data.split(',');
				Ext.each(temp,function(child){
					var result=reg.exec(child);
					if(result==null){
						flag=2;
					}
				});
			}else{
				var result=reg.exec(data);
				if(result==null){
					flag=2;
				}
			}
		}
		if(flag==1){
			return true;
		}else{
			return false;
		}
	},
	//修改赋初值
	initData : function() {
		var scope = this;
		//所属设备
		var relatednevalue=  this.relatednecuid.value;
		if(Ext.isEmpty(relatednevalue)){
			if(this._devCuid != null){
				var result = {
						text : this._devName,
						value : this._devCuid,
						data : []
				}
				this.relatednecuid.setValue(result);
			}
		}
		var data = null;
		//根据端口Cuid查询单条信息为页面赋值
		if (!Ext.isEmpty(this._ptpCuid)) {
			DWREngine.setAsync(false);
			PtpManageAction.queryPtpByCuid(this._ptpCuid, function(_data){
				data = _data;
			});
			//查询单条数据赋值给_record
			this._record = data;
			DWREngine.setAsync(true);	
			var record = Ext.data.Record.create([]);
			var c_data = new record(data);
			this.showEquipmentInfoPanel.getForm().loadRecord(c_data);
		}
	},
    save:function(){
		var scope = this;
		if(this.showEquipmentInfoPanel.getForm().isValid()){
			var labelCn = this.showEquipmentInfoPanel.getForm().findField('LABEL_CN').getValue();
			if(Ext.isEmpty(labelCn.trim())){
				Ext.Msg.alert('提示','端口名称不能为空白!');
				return;
			}
			var portNo = this.portno.value;
			var ptpCuid = "-1";
			if(!Ext.isEmpty(scope._record)){
				ptpCuid = scope._record.CUID;
			}
			var equCuid  =  this.showEquipmentInfoPanel.getForm().findField('RELATED_NE_CUID').getValue();
			var returnValue = null;
			DWREngine.setAsync(false);
			PtpManageAction.isPtpNoExist(portNo,equCuid,ptpCuid, function(_data){
				returnValue = _data;
			});
			if(returnValue=='YES'){
				Ext.Msg.alert('提示','该ONU设备下端口编号【'+portNo+'】已存在！');
				return;
			}else{
				var returnFALGE = null;
				PtpManageAction.isPtpNameExist2(labelCn, equCuid,ptpCuid,function(_data) {
					returnFALGE = _data;
				});
				if(returnFALGE=="YES"){
					Ext.Msg.alert('提示','该ONU设备下端口名称已存在！');
					return;
				}else{
					if(!Ext.isEmpty(this._type)){
						var ptpInfo = scope.getPtpInfoData();
						if(this._type=='modify'){
							ptpInfo.CUID = ptpCuid;
							PtpManageAction.modifyPtpInfo(ptpInfo,scope._devTable, function(_data) {
								if (_data != null) {
									if(Ext.isEmpty(_data.ERROR)){
										Ext.Msg.alert('温馨提示','修改成功');
										scope._returnJson = _data;
										scope.parent.close();
									}else{
										Ext.Msg.alert('温馨提示',_data.ERROR);
									}
								}else{
									Ext.Msg.alert('温馨提示','修改失败,请联系管理员！！');
									scope.parent.close();
								}
							});
						}else if(this._type=='add'){
	    					var ptpInfo = scope.getPtpInfoData();
							PtpManageAction.addPtpInfo(ptpInfo,scope._devTable, function(_data) {
								if (_data != null) {
									if(Ext.isEmpty(_data.ERROR)){
										Ext.Msg.alert('温馨提示','添加成功');
										scope._returnJson = _data;
										scope.parent.close();
									}else{
										Ext.Msg.alert('温馨提示',_data.ERROR);
									}
								}else{
									Ext.Msg.alert('温馨提示','添加失败,请联系管理员！！');
									scope.parent.close();
								}
							});
						}
					}
				}
			}
		}
    },
    
	getPtpInfoData : function() {
    	var LABEL_CN = this.showEquipmentInfoPanel.getForm().findField(
						'LABEL_CN').getValue();
    	var PORT_SUB_TYPE = this.showEquipmentInfoPanel.getForm().findField(
						'PORT_SUB_TYPE' ).getValue();
    	var PORT_TYPE = this.showEquipmentInfoPanel.getForm().findField(
						'PORT_TYPE' ).getValue();
    	var PORT_NO = this.showEquipmentInfoPanel.getForm().findField(
						'PORT_NO').getValue();
		var RELATED_NE_CUID  = this.showEquipmentInfoPanel.getForm().findField(
						'RELATED_NE_CUID').getValue();
		var PORT_RATE  = this.showEquipmentInfoPanel.getForm().findField(
						'PORT_RATE').getValue(); 
		var VLAN  = this.showEquipmentInfoPanel.getForm().findField(
						'VLAN').getValue();
		var PORT_STATE  = this.showEquipmentInfoPanel.getForm().findField(
						'PORT_STATE').getValue();
		var REMARK  = this.showEquipmentInfoPanel.getForm().findField(
						'REMARK').getValue();
		var ptpData = {
				LABEL_CN 			  	: LABEL_CN,
				PORT_SUB_TYPE			: PORT_SUB_TYPE,
				PORT_NO 			  	: PORT_NO,
				PORT_TYPE				: PORT_TYPE,
				RELATED_NE_CUID			: RELATED_NE_CUID,
				PORT_RATE				: PORT_RATE,
				VLAN					: VLAN,
				REMARK                  : REMARK,
				PORT_STATE				: PORT_STATE
		};
		return ptpData;
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
Ext.reg('NETWORK.PtpOnu', NETWORK.ptp_onu);
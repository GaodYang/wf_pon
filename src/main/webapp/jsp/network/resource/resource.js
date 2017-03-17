Ext.namespace("NETWORK");

$importjs(ctx + "/jsp/network/resource/project.js");
$importjs(ctx + "/jsp/network/resource/attach.js");
$importjs(ctx + "/jsp/network/resource/equip.js");
$importjs(ctx + "/dwr/interface/ProjectAction.js");

NETWORK.resource = Ext.extend(Ext.Panel,{
	id 			: 'NETWORK.resource',
	region 		: 'center',
	border 		: false,
	frame 		: true,
	layout 		: 'border',
	autoScroll  : true,
	//参数
	username : '',
	prjcode  : '',
	parentprjcode : '',
	initComponent : function() {
		NETWORK.resource.superclass.initComponent.call(this);
		this._initItems();
		this.initData();
	},
	_initItems : function() {
		this.project=new NETWORK.project( {
   			title: '工程信息',
   			username : this.username,
   			prjcode  : this.prjcode,
   			parentprjcode : this.parentprjcode
		});
		this.attach=new NETWORK.attach( {
			title: '附件信息'
		});
		
		this.equip=new NETWORK.equip( {
			prjcode  : this.prjcode,
			title: '资源信息'
		});
		
		this.commonInfoFieldSet = new Ext.Panel({
  			 //title: '工程信息',
  			 border : false,
  			 frame : false,
  			 autoScroll:true,
  			 height : 550,
  			 region:'center',
  			 buttonAlign:'right', 
  			 items : [{layout : 'form',items : [this.project,this.attach,this.equip]}],
  			 buttons : [
				{
					xtype : 'button',
					width : 80,
					text : '挂测',
					id:'yanqij',
					iconCls : 'c_accept',
					listeners : {
						click: function(btn, e, eOpts){
	
		       					var mk = new Ext.LoadMask(document.body, {  
		    						msg: '提交中，请稍候！',  
		    						removeMask: true //完成后移除  
		    					});
		    					Ext.MessageBox.confirm('温馨提示', '确定进行挂测？',function(btn){
				            		 if(btn=="yes")
						   	 		 {
						   	 		 	mk.show(); //显示  
										Ext.Ajax.request({
										        url: ctx+'/RofhhljOssInfAction/newWorkSheet.do',
										        method: 'POST',
										        params: {
										        	type:Ext.getCmp("sheetType").getValue(),
										            commonality: JSON.stringify(Ext.getCmp("conform").getForm().getValues()),
										            installCustomInfo: JSON.stringify(Ext.getCmp("insetallform").getForm().getValues()),
										            moveCustomInfo: JSON.stringify(Ext.getCmp("moveform").getForm().getValues())
										        },
										        success: function(response) {
										            var result = JSON.parse(response.responseText);
										            mk.hide(); //关闭
										            // 返回值
										            if (result.RespCode!='1') {
										            	Ext.Msg.alert('温馨提示', result.RespMsg);
										            } else {
										                Ext.Msg.alert('错误提示', result.RespMsg);
										            }
										        }
										});
						   	 		 }else
						   	 		 {
						   	 		 	return false;
						   	 		 }
								    	
							 	});
						}
					}
				}]
  		})
		this.add(this.commonInfoFieldSet);
	},
	initData : function() {

		var scope = this;
		DWREngine.setAsync(false);
		ProjectAction.queryProjectByCode(this.prjcode, function(data) {
			var record = Ext.data.Record.create([]);
			var _data = new record(data);
			Ext.getCmp("customPanel").getForm().loadRecord(_data);
			Ext.getCmp("attachPanel").getForm().loadRecord(_data);
		});
		DWREngine.setAsync(true);
	},
});
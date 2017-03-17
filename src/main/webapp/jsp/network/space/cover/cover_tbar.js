Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/jsp/anms/authority/common/FilterTbarBtnNode.js");
$importjs(ctx + "/dwr/interface/RoleAuthorityAction.js");
$importjs(ctx + "/jsp/anms/common/AnmsOverride.js");
$importjs(ctx + "/commons/utils/FrameHelper.js");
$importjs(ctx + "/jsp/anms/common/Util.js");
$importjs(ctx + "/jsp/anms/gpon/GponDeviceLinkManage.js");
$importjs(ctx + "/dwr/interface/GponCoverManageAction.js");
$importjs(ctx + "/jsp/anms/gponcover/plugins/tbar/GponCoverFormPanel.js");
$importjs(ctx + "/jsp/anms/gponcover/plugins/tbar/GponCoverPTPQueryFormPanel.js");

Frame.grid.plugins.tbar.GponCoverCreateTBar = Ext.extend(Object,{
		constructor : function(grid){
			this.grid = grid;
			var oltCuid = grid.oltCuid;
			if(!Ext.isEmpty(oltCuid)){
				var whereParams = {};
				whereParams['RELATED_DEST_LOGIC_CUID'] = {
	    				key 		: 'RELATED_DEST_LOGIC_CUID',
						value 		:  oltCuid,
						relation 	: '='	
	    		}
				this.grid.doQuery(whereParams);
			}
			Frame.grid.plugins.tbar.GponCoverCreateTBar.superclass.constructor.call(this);
			var returnCfg =  [
			        {
						text : '新增',
						iconCls : 'c_add',
						scope : this,
						id	: 'GponCoverCreateTBar.add',
						handler : this.addGponCover.createDelegate(this)
					}, {
						text : '修改',
						iconCls : 'c_pencil',
						scope : this,
						id	: 'GponCoverCreateTBar.modify',
						handler : this.modifyGponCover.createDelegate(this)
					}, {
						text : '删除',
						iconCls : 'c_delete',
						scope : this,
						id	: 'GponCoverCreateTBar.delete',
						handler : function() {
							var selections = this.grid.getSelectionModel()
									.getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
											var val={};
										    val['GPONCOVER_CUID']=item.json.CUID;
										    cuids.push(val);
										});
								// 关联删除的Flag
								var relDelFlag = false;
								DWREngine.setAsync(false);
								var taskName = '';
								var _scope = this;
								Ext.Msg.confirm('提示', taskName + '确定删除选中的数据吗？',
										function(btn) {
											if (btn == 'yes') {
												GponCoverManageAction.deleteGponCover(cuids, function(_data) {
															_scope.grid.getStore().remove(selections);
															 
												});
											}
										});
								DWREngine.setAsync(true);
							} else {
								Ext.Msg.alert('提示', '请选择需要删除的数据!');
								return;
							}
						}
					}, {
						text : '批量导入',
						iconCls : 'c_page_excel',
						scope : this,
						id	: 'GponCoverCreateTBar.import',
						handler : function() {
							var scope = this;
							FrameHelper.openUrl("$(WEBAN_SERVER)/jsp/anms/importres/ImportBasicData.jsp?fileName=gponCoverTemplet",'覆盖范围导入');
							return;
						}
					}, {
						text : '端口状态',
						iconCls : 'c_chart_organisation',
						scope : this,
						id	: 'GponCoverCreateTBar.state',
						handler : this.listGponCover.createDelegate(this)
					}
					
					];
			
			FilterTbarBtnNode.filterTbarBtn(returnCfg,true);
			
			return [returnCfg];
		},
		addGponCover:function(){
			var GponCoverCuid = "";
			this.ManageFormPanel = new TNMS.maintain.number.GponCoverFormPanel({
				GponCoverCuid : GponCoverCuid
			});
			
			var  winSvlan = WindowHelper.openExtWin(this.ManageFormPanel,{
					width:900,
					title:"覆盖范围新增",
					closeAction: 'close',
					listeners:{
						scope:this,
						hide:function(winSvlan){
							//scope.grid.doQuery();
							winSvlan.close();
						}
					},
					buttons:[{
						text:'确定',
						iconCls : 'c_disk',
						scope : this,
						handler : function(){
						  var selValns=this.ManageFormPanel.getResult();
					      if(!Ext.isEmpty(selValns)&&selValns.length>0){
					    	  if(Ext.isEmpty(selValns[0].COVER_INFO)){
					    		  Ext.Msg.alert("温馨提示","请填写覆盖范围描述");
					    		  return;
					    	  }
					          MaskHelper.mask(this.ManageFormPanel.getEl(), "数据保存中，请稍候...");
					          var scope = this;
					          GponCoverManageAction.saveGponCover(selValns,function(msg){
								  MaskHelper.unmask(scope.ManageFormPanel.getEl());
								  if(!Ext.isEmpty(msg)){
									  Ext.Msg.alert("温馨提示",msg);
								  }else{
									  Ext.Msg.alert("温馨提示","保存成功！",function(){
										  winSvlan.hide();   
								     });
								  }
							  });
					      }else{
					    	  Ext.Msg.alert("温馨提示","请选择标准地址");
					      }
						}
					},{
						text:'取消',
						iconCls : 'c_door_in',
						scope : this,
						handler : function(){
							winSvlan.hide();
						}
					}]
				});
			    winSvlan.show();
		},
		modifyGponCover:function(){
			var scope = this;
			var sels = scope.grid.getSelectionModel().getSelections();
			if(sels.length!=1){
				Ext.Msg.alert("温馨提示","请先选择一条记录");
				return;
			}
			var relatedNeCuid = sels[0].json.RELATED_NE_CUID;
			var coverInfo = sels[0].json.COVER_INFO;
			this.ManageFormPanel = new TNMS.maintain.number.GponCoverFormPanel({
				relatedNeCuid : relatedNeCuid,
				coverInfo   : coverInfo
			});
			var  winSvlan = WindowHelper.openExtWin(this.ManageFormPanel,{
					width:800,
					title:"覆盖范围修改",
					closeAction: 'close',
					listeners:{
						scope:this,
						hide:function(winSvlan){
							winSvlan.close();
						}
					},
					buttons:[{
						text:'确定',
						iconCls : 'c_disk',
						scope : this,
						handler : function(){
						  var selValns=this.ManageFormPanel.getResult();
					      if(!Ext.isEmpty(selValns)&&selValns.length>0){
					    	  if(Ext.isEmpty(selValns[0].COVER_INFO)){
					    		  Ext.Msg.alert("温馨提示","请填写覆盖范围描述");
					    		  return;
					    	  }
					    	  if(!this.ManageFormPanel.showManagementInfoPanel.getForm().isValid() &&
					    			  !this.ManageFormPanel.gponcoveraddressPanel.getForm().isValid() &&
					    			  		!this.ManageFormPanel.coverInfoPanel.getForm().isValid() ){
					    		  
					    	  }else{
					    	      MaskHelper.mask(this.ManageFormPanel.getEl(), "数据保存中，请稍候...");
						          var scope = this;
						          GponCoverManageAction.updateGponCover(selValns,function(msg){
									  MaskHelper.unmask(scope.ManageFormPanel.getEl());
									  if(!Ext.isEmpty(msg)){
										  Ext.Msg.alert("温馨提示",msg);
									  }else{
										  Ext.Msg.alert("温馨提示","保存成功！",function(){
											  winSvlan.hide();   
											  scope.grid.getStore().reload();
									     });
									  }
								  });  
					    		  
					    	  }
					    		  
					    		  
					    		  
					    
					      }else{
					    	  Ext.Msg.alert("温馨提示","请选择标准地址");
					      }
						}
					},{
						text:'取消',
						iconCls : 'c_door_in',
						scope : this,
						handler : function(){
							winSvlan.hide();
						}
					}]
				});
			    winSvlan.show();
		},
		listGponCover:function(){
			var scope = this;
			var sels = scope.grid.getSelectionModel().getSelections();
			if(sels.length!=1){
				Ext.Msg.alert("温馨提示","请先选择一条记录");
				return;
			}
			var relatedNeCuid = sels[0].json.RELATED_NE_CUID;
			
			this.ManageFormPanel = new TNMS.maintain.number.GponCoverPTPQueryFormPanel({
				neCuid : relatedNeCuid,
				columnWidth: .9
		    });
			
			var  winSvlan = WindowHelper.openExtWin(this.ManageFormPanel,{
					width:900,
					title:"覆盖范围端口状态",
					closeAction: 'close',
			});
			winSvlan.show();
		}
	});
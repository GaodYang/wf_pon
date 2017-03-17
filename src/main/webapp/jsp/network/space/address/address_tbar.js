Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/dwr/interface/RoleAuthorityAction.js");
$importjs(ctx + "/jsp/anms/common/AnmsOverride.js");
$importjs(ctx + "/commons/utils/FrameHelper.js");
$importjs(ctx + "/jsp/anms/common/Util.js");
$importjs(ctx + "/jsp/anms/address/FullAddressManagePanel.js");
$importjs(ctx + "/jsp/anms/address/FullAddressBusinessManagePanel.js");
$importjs(ctx + "/jsp/anms/address/FullAddressBatchInsertManagePanel.js");
$importjs(ctx + "/jsp/anms/address/FullAddressTreeManagePanel.js");
$importjs(ctx + "/dwr/interface/FullAddressManageAction.js");

Frame.grid.plugins.tbar.address_tbar = Ext.extend(Object,{
		constructor : function(grid){
			this.grid = grid;
			Frame.grid.plugins.tbar.address_tbar.superclass.constructor.call(this);
			var returnCfg = [{
						text : '新增',
						iconCls : 'c_add',
						id	: 'FullAddressGridTBar.add',
						scope : this,
						handler : function() {
							this.showDetailView('', 'add', '新增标准地址', false, this,'');
						}
					}, {
						text : '修改',
						iconCls : 'c_pencil',
						id	: 'FullAddressGridTBar.modify',
						scope : this,
						handler : function() {
							var record = this.grid.getSelectionModel().getSelected();
							if (!Ext.isEmpty(record)) {
								if(this.grid.getSelectionModel().selections.length > 1){
									Ext.Msg.alert('提示', '只能选择一条数据修改!');
									return;
								}else{
									this.showDetailView(record, 'modify', '修改标准地址数据',
											false, this);
								}
							} else {
								Ext.Msg.alert('提示', '请选择需要修改的数据!');
								return;
							}
						}
					}, {
						text : '删除',
						iconCls : 'c_delete',
						id	: 'FullAddressGridTBar.delete',
						scope : this,
						handler : function() {
							var selections = this.grid.getSelectionModel()
									.getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
									var adrObj = {};
									adrObj['CUID'] = item.json.CUID;
									adrObj['LABEL_CN'] = item.json.LABEL_CN;
									cuids.push(adrObj);
								});
								// 关联删除的Flag
								DWREngine.setAsync(false);
								var taskName = '';
								var _scope = this;
								Ext.Msg.confirm('温馨提示', taskName + '确定删除选中的数据吗？',
										function(btn) {
											if (btn == 'yes') {
												FullAddressManageAction.deleteAddressInfo(cuids, function(_data) {
													if (!Ext.isEmpty(_data)) {
														Ext.Msg.alert('温馨提示', _data);
													}else{
														_scope.grid.getStore().remove(selections);
													}
												});
											}
										});
								DWREngine.setAsync(true);
							} else {
								Ext.Msg.alert('提示', '请选择需要删除的数据!');
								return;
							}
						}
					},{
						text : '导入',
						iconCls : 'c_page_excel',
					 	id : 'FullAddressGridTBar.importAddress',
						scope : this,
						handler : function() {
							FrameHelper.openUrl("$(WEBAN_SERVER)/jsp/anms/importres/ImportBasicData.jsp?fileName=fullAddressTemplet",
							'标准地址导入');
							return;
						}
					},{
						text : '导出',
						iconCls : 'c_page_excel',
						id : 'FullAddressGridTBar.exportAddress',
						scope : this,
						handler : function() {
							var selections = this.grid.getSelectionModel().getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
									cuids.push(item.json.CUID);
								});
								var mk = new Ext.LoadMask(document.body,{  
									msg: '数据处理中，请稍候！',  
									removeMask: true
								});
								mk.show();
								DWREngine.setAsync(false);
								FullAddressManageAction.exportGridData(cuids,function(results){
									var time = results.seconds;
									var files = results.files;
									if(files && files.length > 0) {
										var file = files[0];
										var url = ctx + "/download.do?file=" + file.filePath + "&fileName=" + file.fileName;
										mk.hide();
										url = encodeURI(url);
										url = encodeURI(url);
										var textTpl = "导出完成,耗时" + time + "毫秒。<br>";
										textTpl += "<a href='" + url + "'>" + file.fileName + "</a>";
										textTpl += " <a href='" + url + "'><font color='red'>点击下载</font></a>";
										Ext.Msg.alert('温馨提示',textTpl);
									}
								});
								mk.hide();
								DWREngine.setAsync(true);
							} else {
								Ext.Msg.alert('提示', '请选择需要导出的数据!');
								return;
							}
						}
					},{
						text : '所属业务区管理',
						iconCls : 'c_pencil',
						id : 'FullAddressGridTBar.businessManage',
						scope : this,
						handler : function() {
							this.showBuisnessView('所属业务区管理(批量更新标准地址所属业务区信息)');
						}
					},{
						text : '小区批量新增',
						id : 'FullAddressGridTBar.batchInsertAddress',
						iconCls : 'c_add',
						scope : this,
						handler : function() {
							var record = this.grid.getSelectionModel().getSelected();
							var cuid = "";
							if (!Ext.isEmpty(record)) {
								if(this.grid.getSelectionModel().selections.length > 1){
									Ext.Msg.alert('提示', '只能选择一条数据引用!');
									return;
								}else{
									cuid = record.json.CUID;
								}
							}
							this.showBatchInsertView('小区批量新增(批量新增城市型标准地址)',cuid);
						}
					},{
						text : '标准地址树状查看',
						id : 'FullAddressGridTBar.showAddressTree',
						iconCls : 'c_chart_organisation',
						scope : this,
						handler : function() {
							this.showTreeView('标准地址树状查看(树名称组合说明：区域名称-下级区域个数-标准地址总个数)');
						}
					}];
			return [returnCfg];
		},
		showDetailView : function(record,type,winTitlw,readOnly,object,oltCuid){
			var cuid = '';
			var adevType = '';
			if (!Ext.isEmpty(record)) {
				cuid = record.json.CUID;
			}
			var config = {
				winTitle : winTitlw,
				width : 800,
				height : 400,
				region : 'center',
				_cuid : cuid,
				_readOnly : readOnly,
				_type : type,
				_oltCuid:oltCuid
			};
		    openWindow('ANMS.FullAddressManagePanel', true, config, function(_result, _scope,
							_options) {
						if (!Ext.isEmpty(_result) && !Ext.isEmpty(type)) {
							DWREngine.setAsync(false);
							FullAddressManageAction.getAddressInfoByCuid(_result,function(_data){
								if (!Ext.isEmpty(_data) && type == 'add'){
									object.grid.getStore().loadData( {
										'list' : [_data]
									}, true); 
								}else if(!Ext.isEmpty(_data) && type == 'modify'){
									object.grid.getStore().reload({
										'list' :[_data]
									}, false);
								}
							});
							DWREngine.setAsync(true);
						}
					}, object, object.scope);
		},
		showBuisnessView : function(winTitle){
			this.FullAddressBusinessManagePanel = new ANMS.FullAddressBusinessManagePanel({
			    inputParams:{}
			});
			var domainWin = WindowHelper.openExtWin(this.FullAddressBusinessManagePanel,{
				width:800,
				title:winTitle,
				buttons:[{
					text:'确定',
					iconCls : 'c_accept',
					scope : this,
					handler : function(){
						this.FullAddressBusinessManagePanel.save();
					}
				},{
					text:'关闭',
					iconCls : 'c_cancel',
					scope : this,
					handler : function(){
						domainWin.close();
					}
				}]
			});
			domainWin.show();
		},
		showBatchInsertView : function(winTitle,adrCuid){
			this.FullAddressBatchInsertManagePanel = new ANMS.FullAddressBatchInsertManagePanel({
			    inputParams:{},
			    adrCuid : adrCuid
			});
			var batchWin = WindowHelper.openExtWin(this.FullAddressBatchInsertManagePanel,{
				width:800,
				title:winTitle,
				buttons:[{
					text:'确定',
					iconCls : 'c_accept',
					scope : this,
					handler : function(){
						this.FullAddressBatchInsertManagePanel.save();
					}
				},{
					text:'关闭',
					iconCls : 'c_cancel',
					scope : this,
					handler : function(){
						batchWin.close();
					}
				}]
			});
			batchWin.show();
		},
		showTreeView : function(winTitle){
			this.FullAddressTreeManagePanel = new ANMS.FullAddressTreeManagePanel({
			    inputParams:{}
			});
			var treeWin = WindowHelper.openExtWin(this.FullAddressTreeManagePanel,{
				width:800,
				title:winTitle,
				buttons:[{
					text:'关闭窗口',
					iconCls : 'c_accept',
					scope : this,
					handler : function(){
						treeWin.close();
					}
				}]
			});
			treeWin.show();
		}
	});
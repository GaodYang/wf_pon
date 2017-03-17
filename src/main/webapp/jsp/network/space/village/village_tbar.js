Ext.ns('Frame.grid.plugins.tbar');
$importjs(ctx + "/jsp/anms/authority/common/FilterTbarBtnNode.js");
$importjs(ctx + "/dwr/interface/RoleAuthorityAction.js");
$importjs(ctx + "/jsp/anms/common/AnmsOverride.js");
$importjs(ctx + "/commons/utils/FrameHelper.js");
$importjs(ctx + "/jsp/anms/common/Util.js");
$importjs(ctx + "/jsp/anms/businesscommunity/plugins/tbar/BusinessCommunityManage.js");
$importjs(ctx + "/dwr/interface/BusinessCommunityManageAction.js");

Frame.grid.plugins.tbar.village_tbar = Ext.extend(Object,{
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
			Frame.grid.plugins.tbar.village_tbar.superclass.constructor.call(this);
			var returnCfg =  [{
						text : '新增',
						iconCls : 'c_add',
						scope : this,
						id	: 'BusinessCommunityGridTBar.add',
						handler : function() {
							this.showDetailView('', 'add', '新增业务区',this);
						}
					}, {
						text : '修改',
						iconCls : 'c_pencil',
						scope : this,
						id	: 'BusinessCommunityGridTBar.modify',
						handler : function() {
							var record = this.grid.getSelectionModel().getSelected();
							if (!Ext.isEmpty(record)) {
								this.showDetailView(record, 'modify', '修改业务区', this);
							} else {
								Ext.Msg.alert('提示', '请选择需要修改的数据!');
								return;
							}
						}
					}, {
						text : '删除',
						iconCls : 'c_delete',
						scope : this,
						id	: 'BusinessCommunityGridTBar.delete',
						handler : function() {
							var selections = this.grid.getSelectionModel().getSelections();
							if (selections.length > 0) {
								var cuids = [];
								Ext.each(selections, function(item) {
											cuids.push(item.json.CUID);
										});
								// 关联删除的Flag
								var relDelFlag = false;
								DWREngine.setAsync(false);
								var taskName = '';
								var _scope = this;
								Ext.Msg.confirm('提示', taskName + '确定删除选中的数据吗？',
										function(btn) {
											if (btn == 'yes') {
												BusinessCommunityManageAction.deleteBCInfo(cuids, function(_data) {
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
						id	: 'BusinessCommunityGridTBar.import',
						handler : function() {
							var scope = this;
							FrameHelper.openUrl("$(WEBAN_SERVER)/jsp/anms/importres/ImportBasicData.jsp?fileName=businessCommunityTemplet",'业务区批量导入');
							return;
						}
					}];
			

			FilterTbarBtnNode.filterTbarBtn(returnCfg,true);
			
			return [returnCfg];
		},
		showDetailView : function(record,type,winTitlw,object){
			var cuid = '';
			var adevType = '';
			if (!Ext.isEmpty(record)) {
				cuid = record.json.CUID;
			}
			var config = {
				winTitle : winTitlw,
				width : 700,
				height : 330,
				region : 'center',
				_cuid : cuid,
				_type : type
			};
			
		openWindow('ANMS.BusinessCommunityManage', true, config, function(_result, _scope,_options) {
						if (!Ext.isEmpty(_result) && !Ext.isEmpty(type)) {
							DWREngine.setAsync(false);
							BusinessCommunityManageAction.queryLoadBCInfoByCuid(_result,function(_data) {
								if (!Ext.isEmpty(_data) && type == 'add') {
									object.grid.getStore().loadData( {
										'list' : [_data]
									}, true); 
								}else if(!Ext.isEmpty(_data) && type == 'modify'){
									object.grid.getStore().reload({
										'list' : [_data]
									}, false);
								}
							});
							DWREngine.setAsync(true);
						}
					}, object, object.scope);
		}
	});
	
	
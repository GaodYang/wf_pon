Ext.ns("Frame.grid.plugins.event");

$importjs(ctx + '/commons/utils/FrameHelper.js');
$importjs(ctx + '/commons/utils/UrlHelper.js');
$importjs(ctx + '/cmp_res/menu/ContextMenuHelper.js');

Frame.grid.plugins.event.grid_event = Ext.extend(Object, {
	constructor: function(grid){
		this.grid = grid;
		Frame.grid.plugins.event.grid_event.superclass.constructor.call(this);
		var evts = {};
		if(this.grid.enableContextMenu != false) {
			evts['rowcontextmenu'] = {
				scope : this,
				fn : this.onContextmenu
			}
		}
		if(self.dialogArguments == 'selectRecord') {
			evts['rowdblclick'] = {
				scope : this,
				fn : this.onRowdblclick
			}
		}
		evts['rowclick'] = {
			scope : this,
			fn : this.onRowclick
		}
		
		return evts;
	},
	onContextmenu : function(grid, rowIndex, e) {
		e.preventDefault();
		var record = grid.getStore().getAt(rowIndex);
		grid.getSelectionModel().selectRow(rowIndex);
		var obj = Ext.applyIf({
			cuid : record.json.CUID,
			bmClassId : record.json.BM_CLASS_ID
		}, record.json);
		var str = '';
		if(e.target) {
			try {
				str = e.target.innerHTML;
				str = str.replace(/<[^>]+>/g, '');
			}catch(e) {
			}
		}
		var contextmenu = ContextMenuHelper.build('', null, str);
		if(contextmenu) {
			var x = e.getPageX(), y = e.getPageY();
			contextmenu.showAt([x, y]);
		}
	},
	onRowdblclick : function(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		window.returnValue = [record];
		window.close();
	},
	onRowclick : function(grid, rowIndex, e) {
	},
	getExplorer : function () {
		var scope = window;
		var explorer = undefined;
		try {
			for(var i = 0; i < 5; i++) {
				if(scope.Ext) {
					explorer = scope.Ext.getCmp("explorer_frame_panel");
					if(explorer) {
						break;
					}
				}
				scope = scope.parent;
			}
		}catch(e){}
		return explorer;
	}
});
Ext.namespace('Frame.grid.plugins.renderer');

//<!-- 网元拓扑管理中的部分-->		
Frame.grid.plugins.renderer.BusinessCommunityRenderer = {
		//网元状态枚举
		rendererCellType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '自建';
					break;
				case 2:
					returnValue = '光割';
					break;
				case 3:
					returnValue = '光改';
					break;
				case 4:
					returnValue = '第三方';
					break;
				case 5:
					returnValue = '无线';
					break;
				case 6:
					returnValue = '商铺';
					break;
				case 7:
					returnValue = '一网通';
					break;
				}
			}
			return returnValue;
		}
}
		
 
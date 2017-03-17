Ext.namespace('Frame.grid.plugins.renderer');
Frame.grid.plugins.renderer.ReplyOrderGridRenderer = {
     
		rendererSuccess : function(value) {
		var returnValue = '';
		if (!Ext.isEmpty(value)) {
			
			value = Ext.num(value);
			
			switch (value) {
			case 1:
				returnValue = '成功';
				break;
			case 0:
				returnValue = '失败';
				break;
			}
			
		}
		return returnValue;
	}

};
		
 
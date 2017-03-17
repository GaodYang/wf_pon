Ext.namespace('Frame.grid.plugins.renderer');

Frame.grid.plugins.renderer.prj_renderer = {
		//公共盘类型
		rendererCardKind : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				switch (value) {
					case 0:
						returnValue = '未知';
					break;
					case 3:
						returnValue = '控制盘';
						break;
					case 6:
						returnValue = '电源盘';
						break;
					case 7:
						returnValue = '时钟盘';
						break;
					case 19:
						returnValue = 'OLT上联盘';
						break;
					case 20:
						returnValue = 'OLT支路盘';
						break;
					case 22:
						returnValue = 'ONU上联盘';
						break;
					case 23:
						returnValue = 'ONU下联盘';
						break;
					default:
						returnValue = '';
						break;
				}
			}
			return returnValue;
		}

	
};
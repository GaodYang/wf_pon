Ext.namespace('Frame.grid.plugins.renderer');

//<!-- 网元拓扑管理中的部分-->		
Frame.grid.plugins.renderer.address_renderer = {
		rendererREGIONTYPE1 : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
					case 1 :
						returnValue = '城市';
						break;
					case 2 :
						returnValue = '乡镇';
						break;
					case 3 :
						returnValue = '农村';
						break; 
				}
			}
			return returnValue;
		},
		rendererREGIONTYPE2 : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
					case 11 :
						returnValue = '商铺';
						break;
					case 12 :
						returnValue = '物流园区';
						break;
					case 13 :
						returnValue = '城中村';
						break; 
					case 14 :
						returnValue = '小区';
						break; 
					case 21 :
						returnValue = '县辖城区';
						break; 
					case 22 :
						returnValue = '城中村';
						break; 
					case 23 :
						returnValue = '小区';
						break; 
					case 24 :
						returnValue = '商铺';
						break; 
					case 31 :
						returnValue = '非中心村';
						break; 
					case 32	:
						returnValue = '自然村';
						break; 
				}
			}
			return returnValue;
		},
		//业务类型枚举
		rendererBusinessType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '语音';
					break;
				case 2:
					returnValue = '宽带';
					break;
				case 3:
					returnValue = 'IPTV';
					break;
				case 4:
					returnValue = '一网通';
					break;
				}
			}
			return returnValue;
		}
}
		
 
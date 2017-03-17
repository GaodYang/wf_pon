Ext.namespace('Frame.grid.plugins.renderer');

//<!-- 网元拓扑管理中的部分-->		
Frame.grid.plugins.renderer.cover_renderer = {
		
		rendererREGIONTYPE1 : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
					case 1 :
						returnValue = '家庭场景';
						break;
					case 2 :
						returnValue = '校园场景';
						break;
					case 3 :
						returnValue = '聚类场景';
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
					case 31 :
						returnValue = '集体宿舍楼';
						break;
					case 32 :
						returnValue = '党政军';
						break;
					case 33 :
						returnValue = '沿街商铺';
						break; 
					case 34 :
						returnValue = '住宅区商';
						break; 
					case 35 :
						returnValue = '商业楼宇';
						break; 
					case 36 :
						returnValue = '工业园区';
						break; 
				}
			}
			return returnValue;
		},
		//FTTX模式枚举
		rendererFTTX : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1:
					returnValue = 'FTTB';
					break;
				case 2:
					returnValue = 'FTTH';
					break;
				case 3:
					returnValue = 'LAN';
					break;
				case 4:
					returnValue = '无线';
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
		
 
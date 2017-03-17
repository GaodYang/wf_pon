Ext.namespace('Frame.grid.plugins.renderer');

Frame.grid.plugins.renderer.equip_renderer = {
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
		},
		rendererServiceState : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				switch (value) {
					case 1:
						returnValue = '插入';
					break;
					case 2:
						returnValue = '未插入';
						break;
					case 3:
						returnValue = '其他';
						break;
					default:
						returnValue = '';
						break;
				}
			}
			return returnValue;
		},
		//机盘槽位
		rendererOwnedContainer : function(value){
			var returnValue = '';
			if(!Ext.isEmpty(value)){
				DWREngine.setAsync(false);
				AnOltManageAction.getOwnedContainer(value,function(_data){
					if(_data != null && !Ext.isEmpty(_data)){
						returnValue = ''+_data;
					}
				});
				DWREngine.setAsync(true);
			}
			return returnValue;
		},
		//光电属性类型
		rendererPortType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1:
					returnValue = '电口';
					break;
				case 2:
					returnValue = '光口';
					break;
				case 4:
					returnValue = '逻辑口';
					break;
				default:
					returnValue = '';
					break;
				}
			}
			return returnValue;
		},
		//端口类型
		rendererPortState : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1:
					returnValue = '空闲';
					break;
				case 2:
					returnValue = '占用';
					break;
				case 3:
					returnValue = '预占用';
					break;
				case 4:
					returnValue = '坏端口';
					break;
				case 5:
					returnValue = '保留';
					break;
				}
			}
			return returnValue;
		},
		//覆盖端口类型
		rendererGponPortState : function(value) {
			var returnValue = '';
			alert(value);
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1: 
					returnValue = '空闲';
					break;
				case 2:
					returnValue = '<font style="font-weight:bolder;">占用</font>';
					break;
				case 3:
					returnValue = '预占用';
					break;
				case 4:
					returnValue = '<div style="background:#FF0000;">坏端口</div>';
					break;
				case 5:
					returnValue = '保留';
					break;
				}
			}
			return returnValue;
		},
		//端口运行状态
		rendererOperatingState : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '在线';
					break;
				case 2:
					returnValue = '不在线';
					break;
				case 3:
					returnValue = '测试';
					break;
				default :
					returnValue = '';
					break;
				}
			}
			return returnValue;
		},
		//是否直接带用户
		rendererCanAllocateToUser : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '否';
					break;
				case 1:
					returnValue = '是';
					break;
				}
			}
			return returnValue;
			
		},
		
		//接入类型枚举
		rendererAccessType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '集客';
					break;
				case 2:
					returnValue = '家客';
					break;
				case 3:
					returnValue = '集客/家客共用';
					break;
				case 4:
					returnValue = '其他';
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
					returnValue = 'FTTC';
					break;
				case 4:
					returnValue = 'FTTO';
					break;
				}
			}
			return returnValue;
			
		},
		
		//端口类型枚举
		rendererPortSubType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 3:
					returnValue = 'MSTP口';
					break;
				case 5:
					returnValue = '中频口';
					break;
				case 6:
					returnValue = '射频口';
					break;
				case 7:
					returnValue = 'IMA口';
					break;
				case 8:
					returnValue = 'LAG口';
					break;
				case 9:
					returnValue = 'GPON口';
					break;
				case 10:
					returnValue = 'EPON口';
					break;
				case 11:
					returnValue = 'OLT上联口';
					break;
				case 12:
					returnValue = '分光器上联口';
					break;
				case 13:
					returnValue = '分光器下联口';
					break;
				case 14:
					returnValue = 'ONU上联口';
					break;
				case 15:
					returnValue = 'FE';
					break;
				case 16:
					returnValue = 'POTS';
					break;
				case 21:
					returnValue = 'SDH';
					break;
				case 22:
					returnValue = 'PDH';
					break;
				case 23:
					returnValue = 'ADSL';
					break;
				case 24:
					returnValue = 'ADSL2_PLUS';
					break;
				case 25:
					returnValue = 'POS';
					break;
				case 26:
					returnValue = 'SHDSL';
					break;
				case 27:
					returnValue = 'VDSL2';
					break;
				case 28:
					returnValue = 'ATM';
					break;
				case 29:
					returnValue = 'ETH';
					break;
				case 30:
					returnValue = 'PSTN';
					break;
				case 31:
					returnValue = '波分口';
					break;
				case 32:
					returnValue = 'PTN口';
					break;
				case 33:
					returnValue = '光GE口';
					break;
				case 36:
					returnValue = '电GE口';
					break;
				case 37:
					returnValue = '电FE口';
					break;
				default:
					returnValue = '';
					break;
				}
			}
			return returnValue;
		},
		
		rendererBusCount : function(value, metadata, record, rowIndex, colIndex, store) {
			if(!Ext.isEmpty(value)) {
				//value格式：$code.$key=$cuid[$label]$title
				var s1 = [value.substring(0,value.indexOf("=")), value.substring(value.indexOf("=")+1)];
				if(s1.length == 2) {
					var dotIndex = s1[0].lastIndexOf('.');
					if(dotIndex<=-1){
						return '配置有误';
					}
					var $code = s1[0].substring(0,dotIndex);
					var $key = s1[0].substring(dotIndex+1);
					var $cuid = s1[1].substring(0, s1[1].indexOf('['));
					var $title = s1[1].substring(s1[1].indexOf(']')+1);
					if(!Ext.isEmpty($code) && !Ext.isEmpty($cuid)) {
						var $label = s1[1].substring(s1[1].indexOf('[')+1,s1[1].indexOf(']'));
						if($label != '0') {
							var url = "/cmp_res/grid/ResGridPanel.jsp?code="+$code+"&header=false&s_"+$key+"="+$cuid;
						if("GPON_COVER"==$code){
								url = "/jsp/anms/common/GponCover.jsp?devCuid="+$cuid;
							}else if("T_ROFH_CUSTOMER"==$code){
								url = "/jsp/anms/common/TRofhCustomer.jsp?menuId=PON_WAY_ANMS&devCuid="+$cuid;
							}
							var tabName = "统计";
							if($title) {
								tabName = $title;
							}else {
								var colName = this.header;
								if(!Ext.isEmpty(this.header)) {
									colName += '['+$label+']';
								}else {
									colName = this.header;
								}
								if(!Ext.isEmpty(colName)) {
									tabName = colName;
								}
							}
							var clickFn = "FrameHelper.openUrl('"+url+"','"+tabName+"');stopBubble(window.event);";
							var district = record.json.RELATED_DISTRICT_CUID;
							var style = 'cursor:pointer;color:#blue;text-decoration:underline';
							if(district.indexOf("DISTRICT-00001-00013")>=0){
	                        	style = 'cursor:pointer;color:#fff;text-decoration:underline';
	                        }
							value = '<span style="'+style+'" onclick="'+clickFn+'">'+$label+'</span>';
						}else {
							value = '0';
						}
					}else {
						value = '';
					}
				}else {
					value = '配置有误';
				}
			}
			return value;
		},
		rendererBusCountNM : function(value, metadata, record, rowIndex, colIndex, store) {
			if(!Ext.isEmpty(value)) {
				//value格式：$code.$key=$cuid[$label]$title
				var s1 = [value.substring(0,value.indexOf("=")), value.substring(value.indexOf("=")+1)];
				if(s1.length == 2) {
					var dotIndex = s1[0].lastIndexOf('.');
					if(dotIndex<=-1){
						return '配置有误';
					}
					var $code = s1[0].substring(0,dotIndex);
					var $key = s1[0].substring(dotIndex+1);
					var $cuid = s1[1].substring(0, s1[1].indexOf('['));
					var $title = s1[1].substring(s1[1].indexOf(']')+1);
					if(!Ext.isEmpty($code) && !Ext.isEmpty($cuid)) {
						var $label = s1[1].substring(s1[1].indexOf('[')+1,s1[1].indexOf(']'));
						if($label != '0') {
							var url = "/cmp_res/grid/ResGridPanel.jsp?code="+$code+"&header=false&s_"+$key+"="+$cuid;
						if("GPON_COVER"==$code){
								url = "/jsp/anms/common/GponCover.jsp?devCuid="+$cuid;
							}else if("T_ROFH_CUSTOMER"==$code){
								url = "/jsp/anms/common/TRofhCustomerNM.jsp?menuId=PON_WAY_ANMS&devCuid="+$cuid;
							}
							var tabName = "统计";
							if($title) {
								tabName = $title;
							}else {
								var colName = this.header;
								if(!Ext.isEmpty(this.header)) {
									colName += '['+$label+']';
								}else {
									colName = this.header;
								}
								if(!Ext.isEmpty(colName)) {
									tabName = colName;
								}
							}
							var clickFn = "FrameHelper.openUrl('"+url+"','"+tabName+"');stopBubble(window.event);";
							var district = record.json.RELATED_DISTRICT_CUID;
							var style = 'cursor:pointer;color:blue;text-decoration:underline';
							if(district.indexOf("DISTRICT-00001-00013")>=0){
	                        	style = 'cursor:pointer;color:#fff;text-decoration:underline';
	                        }
							value = '<span style="'+style+'" onclick="'+clickFn+'">'+$label+'</span>';
						}else {
							value = '0';
						}
					}else {
						value = '';
					}
				}else {
					value = '配置有误';
				}
			}
			return value;
		},
		rendererPportRate : function(value, metadata, record, rowIndex,
				colIndex, store) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {			 
				value = Ext.num(value);
				switch (value) {
				  case 1 :  
					  returnValue = '2M';
					  break; 
				  case 2 :  
					  returnValue = '8M';
					  break; 
				  case 3 :  
					  returnValue = '10M'; 
					  break; 
				  case 4 :  
					  returnValue = '34M';
					  break; 
				  case 5 :  
					  returnValue = '45M';
					  break; 
				  case 6 :  
					  returnValue = '68M';
					  break;
				  case 7 :  
					  returnValue = '100M';
					  break;
				  case 8 :  
					  returnValue = '140M';
					  break;
				  case 9 :  
					  returnValue = '155M';
					  break;
				  case 10 :  
					  returnValue = '280M';
					  break;
				  case 11 :  
					  returnValue = '310M';
					  break;
				  case 12 :  
					  returnValue = '565M';
					  break; 
				  case 13 :  
					  returnValue = '622M';
					  break; 
				  case 14 :  
					  returnValue = '1G';
					  break; 
				  case 15 :  
					  returnValue = '1.25G';
					  break; 
				  case 16 :  
					  returnValue = '2.5G';
					  break; 
				  case 17 :  
					  returnValue = '10G';
					  break; 
				  case 18 :  
					  returnValue = '20G';
					  break; 
				  case 19 :  
					  returnValue = '40G';
					  break; 
				  case 20 :  
					  returnValue = '80G'; 
					  break; 
				  case 21 :  
					  returnValue = '120G';
					  break; 
				  case 22 :  
					  returnValue = '6M';
					  break; 
				  case 23 :  
					  returnValue = '12M';
					  break;
				  case 24 :  
					  returnValue = '16M';
					  break;
				  case 25 :  
					  returnValue = '4M';
					  break;
				  case 26 :  
					  returnValue = '64k';
					  break;
				  case 27 :  
					  returnValue = 'NA';
					  break;
				  case 28 :  
					  returnValue = '3.5G';
					  break;
				  case 29 :  
					  returnValue = '320G';
					  break;
				  case 30 :  
					  returnValue = '400G';
					  break;
				  case 31 :  
					  returnValue = '800G';
					  break;
				  case 32 :  
					  returnValue = '1600G';
					  break;
				  case 33 :  
					  returnValue = '32M';
					  break;
				  case 34 :  
					  returnValue = 'FE';
					  break;
				  case 35 :  
					  returnValue = 'GE';
					  break;
				  case 36 :  
					  returnValue = '10GE';
					  break;
				  case 37 :  
					  returnValue = 'FC100';
					  break;
				  case 38 :  
					  returnValue = 'FC200';
					  break;
				  case 39 :  
					  returnValue = 'FC400';
					  break;
				  case 40 :  
					  returnValue = 'FC800';
					  break; 
				  case 48 :
					  returnValue = '20M';
					  break;
				}
			}
			return returnValue;
		},
		rendererCustomerName : function(value) {
			if (Ext.isEmpty(value)) {
				return value;
			}
			var strLength = value.length;
			var returnValue = '';
			for ( var i = 0; i < strLength; i++) {
				var ch = value.substring(i, i + 1);
				if (i != 0) {
					ch = '*';
				}
				returnValue = returnValue + ch;
			}
			return returnValue;
		},
		upgradeState : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = 'Idle';
					break;
				case 2:
					returnValue = 'Waiting';
					break;
				case 3:
					returnValue = 'InProgress';
					break;
				case 4:
					returnValue = 'Success';
					break;
				case 5:
					returnValue = 'Failure';
					break;
				case 6:
					returnValue = 'OnuTypeNotConsistent';
					break;
				case 7:
					returnValue = 'FileDownloadFail';
					break;
				case 8:
					returnValue = 'OnuNotExist';
					break;
				case 9:
					returnValue = 'NotConsistent';
					break;
				case 10:
					returnValue = 'OnuCommunicationFail';
					break;
				}
			}
			return returnValue;
			
		},
		upgradeFormatDate:function(value){
			if(!Ext.isEmpty(value)){
				return value.format('Y-m-d H:i:s');
			}
			return value;
		},
		upgradeFormatTime:function(value){
			var timeStr = "";  
			if(!Ext.isEmpty(value)){
				var hour = 0;
				var minute = 0;
				var second = 0;
				if(value>60){
					minute = parseInt(value / 60);
					if (minute < 60) {
						second = value % 60;
						timeStr = minute+"分钟"+second+"秒";
					}else{
						hour = parseInt(minute / 60);
						minute = minute % 60;  
		                second = value - hour * 3600 - minute * 60;
		                timeStr = hour+"小时"+minute+"分钟"+second+"秒";
					}
				}else{
					timeStr = value+"秒";
				}
			}else{
				timeStr = "0";
			}
			return timeStr;
		},
		getPercentValue : function(value){
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				returnValue = (value*100).toFixed(2)+'%';
			}
			return returnValue;
		},
		checkResultInfo : function(value, metadata, record, rowIndex, colIndex, store) {
			if(!Ext.isEmpty(value)) {
				//value格式：$type+$district_label_cn-$label
				var $type = value.substring(0,value.indexOf("+"));
				var $district_label_cn = value.substring(value.indexOf("+")+1,value.indexOf("-"));
				if(!Ext.isEmpty($district_label_cn)) {
					var $label = value.substring(value.indexOf("-")+1);
					if($label != '0') {
						var url = "/jsp/anms/common/CheckResultInfo.jsp?dealStatus="+$type+"&devCuid="+$district_label_cn;
						var tabName = "数据详单";
						var clickFn = "FrameHelper.openUrl('"+url+"','"+tabName+"');stopBubble(window.event);";
						var style = 'cursor:pointer;color:#blue;text-decoration:underline';
						value = '<span style="'+style+'" onclick="'+clickFn+'">'+$label+'</span>';
					}else {
						value = '0';
					}
				}
				return value;
			}
		},
		//产权人
		rendererOwnershipMan : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '移动';
					break;
				case 2:
					returnValue = '联通';
					break;
				case 3:
					returnValue = '电信';
					break;
				case 4:
					returnValue = '铁通';
					break;
				case 5:
					returnValue = '广电';
					break;
				case 6:
					returnValue = '政府';
					break;
				case 7:
					returnValue = '客户自有';
					break;
				case 8:
					returnValue = '物业';
					break;
				case 9:
					returnValue = '其它';
					break;
				}
			}
			return returnValue;
		},
		//维护方式枚举
		rendererMaintMode : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '自维';
					break;
				case 2:
					returnValue = '代维';
					break;
				}
			}
			return returnValue;
			
		},
		//产权枚举
		rendererOwnerShip : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1:
					returnValue = '自建';
					break;
				case 2:
					returnValue = '共建';
					break;
				case 3:
					returnValue = '第三方';
					break;
				case 4:
					returnValue = '租用';
					break;
				case 5:
					returnValue = '购买';
					break;
				case 6:
					returnValue = '置换';
					break;
				case 101:
					returnValue = '铁通';
					break;
				}
			}
			return returnValue;
			
		},
		//ONU类型枚举
		rendererOnuType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 0:
					returnValue = '未知';
					break;
				case 1:
					returnValue = 'MDU';
					break;
				case 2:
					returnValue = 'ONT';
					break;
				case 3:
					returnValue = 'HGU';
					break;
				case 4:
					returnValue = 'SFU';
					break;
				case 5:
					returnValue = 'SBU';
					break;
				case 6:
					returnValue = 'MTU';
					break;
				}
			}
			return returnValue;
			
		},
		//ONU的认证类型AUTH_TYPE
		rendererAuthType : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = 'SN';
					break;
				case 2:
					returnValue = 'MAC';
					break;
				case 3:
					returnValue = 'PASSWORD';
					break;
				case 4:
					returnValue = 'LOGICID';
					break;
				}
			}
			return returnValue;
			
		},
		//网元状态枚举
		rendererLiveCycle : function(value) {
			var returnValue = '';
			if (!Ext.isEmpty(value)) {
				value = Ext.num(value);
				switch (value) {
				case 1:
					returnValue = '在用';
					break;
				case 2:
					returnValue = '工程';
					break;
				case 3:
					returnValue = '退网';
					break;
				}
			}
			return returnValue;
		}

	
};
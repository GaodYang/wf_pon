Ext.namespace("IRMS.combo");
//dwr代理
$importjs(ctx + "/commons/dwr/DwrProxy.js");
//dwr Action
$importjs(ctx + "/dwr/interface/ComboxAction.js");
if('function' !== typeof RegExp.escape) {  
    RegExp.escape = function(s) {  
        if('string' !== typeof s) {  
            return s;  
        }  
        // Note: if pasting from forum, precede ]/\ with backslash manually  
        return s.replace(/([.*+?^=!:${}()|[\]\/\\])/g, '\\$1');  
    }; // eo function escape  
}
IRMS.combo.AsynCombox = Ext.extend(Ext.form.ComboBox, {
    forceSelection : true,
    loadingText : '数据加载中，请稍候...',
    mode : 'remote',
    triggerAction : 'query',
    resizable : true,
    minChars : 1,
    queryDelay : 100,
    lazyInit : false,
    trigger1Class : "x-form-clear-trigger",
	trigger2Class : "x-form-trigger",
	hideTrigger1 : false,
	hideTrigger2 : false,
	displayField : 'text',
	valueField : 'value',
	dataField : 'data',
	/**
	 * 文字颜色字段
	 * @type String
	 */
	colorField : 'color',
	/**
	 * 描述字段
	 * @type String
	 */
	remarkField : 'remark',
	/**
	 * 默认初始化数据
	 * @type Boolean
	 */
	loadData : false,
    /**
     * 输入的查询参数
     * @type String
     */
    queryParam : 'LABEL_CN',
    /**
     * VALUE初始化时的唯一标识
     * @type String
     */
    valueParam : 'CUID',
    /**
     * 显示分页条
     * @param Number
     * 设置后，将显示分页条
     */
    //pageSize : 50,
    /**
     * 模糊匹配规则
     * @type String 支持 left,right,both
     */
    blurMatch : 'right',
    /**
     * 点击trigger是否显示所有数据
     * @type Boolean
     */
    triggerAll : false,
    /**
     * 多选
     * @type Boolean
     */
    multSel : false,
    checkField : 'checked',
    separator:',',
    // override
    constructor : function(config) {
    	if(!config.comboxCfg) {
    		config.comboxCfg = new Object();
    	}
    	Ext.applyIf(config, {
    		hiddenName : config.name
    	});
    	Ext.applyIf(config.comboxCfg, {
    		cfgParams : new Object(),
    		queryParams : new Object(),
    		extParams : {'undefined':{key:'undefined'}}
    	});
    	if(!Ext.isEmpty(config.pageSize)) {
    		config.minListWidth = 250;
    	}
    	IRMS.combo.AsynCombox.superclass.constructor.call(this, config);
    },
    // override
    initComponent : function() {
    	this.tpl = '<tpl for="."><div class="x-combo-list-item" style="color:{'+this.colorField+'}" title="{'+this.remarkField+'}">{' + this.displayField + '}</div></tpl>';
    	var scope = this;
    	this.store = new Ext.data.Store({
			proxy: new Ext.ux.data.DwrProxy({
				apiActionToHandlerMap : {
					read : {
						dwrFunction : ComboxAction.getComboxData,
						getDwrArgsFunction : function() {
							return [scope.comboxCfg];
						}
					}
				}
			}),
			reader: new Ext.data.JsonReader({
				root : 'list',
				totalProperty : 'totalCount',
				id : 'value',
				fields : [
					{name: this.displayField},
					{name: this.valueField},
					{name: this.colorField},
					{name: this.remarkField},
					{name: this.dataField}
				]
			}),
			baseParams : {totalNum : this.multSel == true?1000:0},
			listeners : {
				scope : this
			}
		});
		if(Ext.isEmpty(this.value)) {
			this.setValue(this.value);
		}
		if(this.multSel == true) {
        	this.tpl =   
                 '<tpl for=".">'
                +'<div class="x-combo-list-item">'
                +'<img src="' + Ext.BLANK_IMAGE_URL + '" '
                +'class="ux-lovcombo-icon ux-lovcombo-icon-'
                +'{[values.' + this.checkField + '?"checked":"unchecked"' + ']}">'
                +'<div class="ux-lovcombo-item-text">{' + (this.displayField || 'text' )+ '}</div>'
                +'</div>'
                +'</tpl>';
	        this.on({
	        	scope:this,
	        	beforequery:this.onBeforeQuery,
	        	blur:this.onRealBlur
	        });
	        this.onLoad = this.onLoad.createSequence(function() {  
	            if(this.el) {  
	                var v = this.el.dom.value;  
	                this.el.dom.value = '';  
	                this.el.dom.value = v;  
	            }
	        });
		}else {
			//处理like的%号
			this.on({
	        	scope:this,
	        	beforequery: this.onBeforeQuery
	        });
		}
		IRMS.combo.AsynCombox.superclass.initComponent.call(this);
		this.triggerConfig = {
            tag:'span', cls:'x-form-twin-triggers', cn:[
            {tag: "img", src: Ext.BLANK_IMAGE_URL, alt: "", cls: "x-form-trigger " + this.trigger1Class},
            {tag: "img", src: Ext.BLANK_IMAGE_URL, alt: "", cls: "x-form-trigger " + this.trigger2Class}
        ]};
		if(Ext.isEmpty(this.value) && this.loadData === true) {
			this.on('afterrender', function(combo){
				combo.getStore().load();
			})
			this.getStore().on('load', function(store, records) {
				if(records.length > 0) {
					var oldValue = undefined;
					var newValue = records[0].data.value;
					this.setValue(records[0].data.value);
					this.fireEvent('change', this, newValue, oldValue);
				}
			}, this);
		}
    },
    initList : function(){
    	IRMS.combo.AsynCombox.superclass.initList.call(this);
    	if(this.multSel == true) {
    		this.footer = this.list.createChild({cls:'x-combo-list-ft'});
            this.tb = new Ext.Toolbar({
            	renderTo:this.footer,
            	items : [{
	            	text : '确定',
	            	iconCls : 'c_accept',
	            	scope : this,
	            	handler : function() {
	            		this.collapse();
	            	}
	            },{
	            	text : '取消',
	            	iconCls : 'c_door_in',
	            	scope : this,
	            	handler : function() {
	            		this.clearValue();
	            		this.collapse();
	            	}
	            }]
            });
            this.assetHeight += this.footer.getHeight();
    	}
    },
    getCheckedDisplay:function() {  
        var re = new RegExp(this.separator, "g");  
        return this.getCheckedValue(this.displayField).replace(re, this.separator + ' ');  
    },
    getCheckedValue:function(field) {  
        field = field || this.valueField;  
        var c = [];  

        // store may be filtered so get all records  
        var snapshot = this.store.snapshot || this.store.data;  
  
        snapshot.each(function(r) {  
            if(r.get(this.checkField)) {  
                c.push(r.get(field));  
            }  
        }, this);
  
        return c.join(this.separator);  
    },
    /**
     * override
     * 解决remote方式，列表store未加载数据时，value无法赋值的问题
     * @param {String} v
     */
    setValue : function(v){
    	var scope = this;
    	if(!Ext.isEmpty(v)) {
    		if(this.multSel == true) {
    			if(this.mode == 'remote') {
	    			var queryParam = {};
	    			DWREngine.setAsync(false);
	            	ComboxAction.getComboxData(queryParam, this.comboxCfg, function(obj){
	            		if(obj) {
		            		scope.getStore().loadData(obj);
	            		}
	            		scope.mode = 'local';
	            	});
	            	DWREngine.setAsync(true);
    			}
    			this.store.clearFilter();
    			this.store.each(function(r) {  
                    var checked = !(!v.match(  
                         '(^|' + this.separator + ')' + RegExp.escape(r.get(this.valueField))  
                        +'(' + this.separator + '|$)'))  
                    ;  
  
                    r.set(this.checkField, checked);  
                }, this);  
                this.value = this.getCheckedValue();  
                this.setRawValue(this.getCheckedDisplay());  
                if(this.hiddenField) {  
                    this.hiddenField.value = this.value;  
                }
    		}else {
    			if(Ext.isObject(v)) {
	    			if(!Ext.isEmpty(v[this.valueField])) {
	    				var r = this.findRecord(this.valueField, v[this.valueField]);
	    				if(!r) {
	    					this.getStore().loadData({
	    						list : [v],
	    						totalCount : 1
	    					});
	    				}
	    			}
	    			v = v[this.valueField];
	    		}else if(Ext.isString(v) || Ext.isNumber(v)) {
	    			v = v+'';
					var r = this.findRecord(this.valueField, v);
					//如果当前结果中没有该数据，则从后台查询
					if(!r && this.mode =='remote') {
		            	var queryParam = {};
		            	queryParam[this.valueParam] = v;
		                DWREngine.setAsync(false);
		            	ComboxAction.getComboxData(queryParam, this.comboxCfg, function(obj){
		            		if(obj) {
			            		scope.getStore().loadData(obj);
		            		}
		            	});
		            	DWREngine.setAsync(true);
					}
					var r = this.findRecord(this.valueField, v);
					if(!r && this.forceSelection != false) {
						v = '';
					}
	    		}
	    		var isChange = false;
		    	var oldValue = this.getValue();
		    	if(oldValue != v) {
		    		isChange = true;
		    	}
		      	IRMS.combo.AsynCombox.superclass.setValue.call(this, v);
		      	if(isChange) {
		      		this.fireEvent('change', this, v, oldValue);
		      	}
    		}
    	}else {
    		this.clearValue();
    	}
    },
    /**
     * override
     * 解决点击展开按钮，如果未填写查询内容，无法执行查询的问题
     */
    onTriggerClick : function() {
    	var v = this.getRawValue();
    	if(!this.isExpanded() && this.triggerAll === true) {
    		this.setRawValue("");
    	}
    	IRMS.combo.AsynCombox.superclass.onTriggerClick.call(this);
    	this.setRawValue(v);
    },
    getTrigger : function(index){
        return this.triggers[index];
    },
    afterRender: function(){
        IRMS.combo.AsynCombox.superclass.afterRender.call(this);
        var triggers = this.triggers,
            i = 0,
            len = triggers.length;
            
        for(; i < len; ++i){
            if(this['hideTrigger' + (i + 1)]){
                triggers[i].hide();
            }
        }    
    },
    initTrigger : function(){
        var ts = this.trigger.select('.x-form-trigger', true),
            triggerField = this;
            
        ts.each(function(t, all, index){
            var triggerIndex = 'Trigger'+(index+1);
            t.hide = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                triggerField['hidden' + triggerIndex] = true;
            };
            t.show = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
                triggerField['hidden' + triggerIndex] = false;
            };
            this.mon(t, 'click', this['on'+triggerIndex+'Click'], this, {preventDefault:true});
            t.addClassOnOver('x-form-trigger-over');
            t.addClassOnClick('x-form-trigger-click');
        }, this);
        this.triggers = ts.elements;
    },
    getTriggerWidth: function(){
        var tw = 0;
        Ext.each(this.triggers, function(t, index){
            var triggerIndex = 'Trigger' + (index + 1),
                w = t.getWidth();
            if(w === 0 && !this['hidden' + triggerIndex]){
                tw += this.defaultTriggerWidth;
            }else{
                tw += w;
            }
        }, this);
        return tw;
    },
    onResize : function(w, h) {
    	IRMS.combo.AsynCombox.superclass.onResize.apply(this, arguments);
    },
    // private
    onDestroy : function() {
        Ext.destroy(this.triggers);
        IRMS.combo.AsynCombox.superclass.onDestroy.call(this);
    },
    onTrigger1Click : function(){
    	if(this.readOnly || this.disabled){
            return;
        }
        var oldValue = this.getValue();
    	this.clearValue();
    	this.fireEvent('change', this, '', oldValue);
    },
	onTrigger2Click : function(){
		this.onTriggerClick();
	},
	onSelect:function(record, index) {  
		if(this.multSel == true) {
	        if(this.fireEvent('beforeselect', this, record, index) !== false){  
	  
	            // toggle checked field  
	            record.set(this.checkField, !record.get(this.checkField));  
	  
	            // display full list  
	            if(this.store.isFiltered()) {  
	                this.doQuery(this.allQuery);  
	            }  
	  
	            // set (update) value and fire event
	            this.setValue(this.getCheckedValue());  
	            this.fireEvent('select', this, record, index);  
	        }
		}else {
			IRMS.combo.AsynCombox.superclass.onSelect.call(this, record, index);
		}
    },
    onBeforeQuery : function(qe) {
    	if(this.multSel == true) {
    		qe.query = qe.query.replace(new RegExp(this.getCheckedDisplay() + '[ ' + this.separator + ']*'), '');
    		delete this.lastQuery;
    		this.minChars = 0;
    	}else {
    		this.minChars = 1;
    		var q = qe.query;
			if(q) {
				q = q.replace(/(^\s*)|(\s*$)/g,'');
			}
			if(q != '') {
				if(this.blurMatch == 'both') {
					q = '%' + q + '%';
				}else if(this.blurMatch == 'left') {
					q = '%' + q;
				}else {
					q = q + '%';
				}
				qe.query = q;
			}else {
				qe.query = '%%';
			}
    	}
		return true;
    },
    onRealBlur : function() {
    	this.list.hide();  
        var rv = this.getCheckedDisplay();
        var rva = rv.split(new RegExp(RegExp.escape(this.separator) + ' *'));  
        var va = [];  
        var snapshot = this.store.snapshot || this.store.data;  
  
        // iterate through raw values and records and check/uncheck items  
        Ext.each(rva, function(v) {  
            snapshot.each(function(r) {  
                if(v === r.get(this.displayField)) {  
                    va.push(r.get(this.valueField));  
                }  
            }, this);  
        }, this);
        this.setValue(va.join(this.separator));  
        this.store.clearFilter();  
    },
	clearValue : function(){
		if(this.multSel == true) {
			this.value = '';  
	        this.setRawValue(this.value);  
	        this.store.clearFilter();  
	        this.store.each(function(r) {  
	            r.set(this.checkField, false);  
	        }, this);  
	        if(this.hiddenField) {  
	            this.hiddenField.value = '';  
	        }  
	        this.applyEmptyText();  
		}
		delete this.lastQuery;
		IRMS.combo.AsynCombox.superclass.clearValue.call(this);
	},
	initEvents:function() {  
		IRMS.combo.AsynCombox.superclass.initEvents.apply(this, arguments);
		if(this.multSel == true) {
			this.keyNav.tab = false;  
		}
    },
    getRecordValue : function() {
    	var v = this.getValue();
    	var r = null;
    	if(!Ext.isEmpty(v)) {
    		r = this.findRecord(this.valueField, this.getValue());
    	}
    	return r;
    }
});
Ext.reg('asyncombox', IRMS.combo.AsynCombox);
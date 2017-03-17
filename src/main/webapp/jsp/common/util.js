/**
        * 打开弹出窗口

        * @param _xtype         ：窗口注册的xtype
        * @param _isModal       ：是否模态
        * @param _config        ：窗口参数
        * @param _setResultCB   ：窗口关闭后的回调方法, setResultCB(result, scope, options)
        * @param _scope         ：回调方法的对象
        * @param _options       ：回调方法的可选参数
        */
function openWindow(_xtype, _isModal, _config, _setResultCB,_scope, _options,_resizable){
	_config         = _config || {};
    _config.xtype   = _xtype;
    _config.region  = 'center';

    if(_resizable == null) _resizable = true;

    var panel = Ext.ComponentMgr.create(_config);

    win = new Ext.Window({
        title   : _config.winTitle,
        width   : panel.width,
        height  : panel.height,
        modal   : _isModal,
        layout  : 'border',
        resizable : _resizable,
        closeAction:'close',
        items   : [panel]
    });
    panel.parent = win;
    win.on('close', function(_event){
        if(typeof(panel.getResult) == 'function'){
            var result = panel.getResult();
            if(!isUndefined(result) && typeof(_setResultCB) == 'function'){
                Ext.getBody().mask('处理中,请稍候...','x-mask-loading');
                var task = new Ext.util.DelayedTask(function(){
                    _setResultCB(result, _scope, _options);
                    Ext.getBody().unmask();
                },_scope);
                task.delay(1);
            }
        }
    });
    win.show();
}

// 'Y-m-d H:i:s'
function parseTimestamp(_date){
    var date = null;
    try{
        var dt = _date.split(' ');
        var fullYear = dt[0].split('-');
        var fullTime = dt[1].split(':');
        date = new Date();
        date.setFullYear(fullYear[0], fullYear[1]-1, fullYear[2]);
        date.setHours(fullTime[0], fullTime[1], fullTime[2]);
    }catch(e){}
    return date;
}


function isUndefined(_var){
  return typeof _var == 'undefined';
}

function isNotNull(_var){
    if(typeof(_var)!='undefined' && _var!=null && _var!='null' && _var!='NaN' && _var!=''){
        return true
    }else{
        return false
    }
}

function showProperties(_obj){
  var _keys = Object.keys(_obj);
  var _str = "";
  for(var i=0; i<_keys.length; i++){
    var _value = eval("_obj." + _keys[i]);
    if(typeof _value == 'function') continue;
    _str += _keys[i] + "=" + _value;
    if(i < _keys.length - 1){
      _str += ", ";
    }
  }
  alert(_str);
}

function isIE(){
  return (navigator.appName == "Microsoft Internet Explorer");
}

function isEmptyString(_value){
    return typeof(_value) == 'undefined' || _value == null || _value.trim().length == 0;
}


/**
 * @class JHashTable: 和Java的HashTable一致的操作
**/
function JHashTable(_hashObj){
    this.hashObj = _hashObj != null ? _hashObj : {};
}

JHashTable.prototype.getKeys = function(){
    return Object.keys(this.hashObj);
}

JHashTable.prototype.getValues = function(){
    return Object.values(this.hashObj);
}

JHashTable.prototype.put = function(_key, _value){
    this.hashObj[_key] = _value;
}

JHashTable.prototype.get = function(_key){
    return  this.hashObj[_key];
}

JHashTable.prototype.containsKey = function(_key){
    for (var key in this.hashObj){
        if(key == _key) return true;
    }
	return false;
}

JHashTable.prototype.remove = function(_key){
	delete this.hashObj[_key];
}

JHashTable.prototype.clear = function(){
	this.hashObj = null;
 	this.hashObj = {};
}

/**
 * 设置非空属性
 * */
function getNotNullFont(text){
	return '<font color="red">'+text+'</font>';
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    } else {  
        return this.replace(reallyDo, replaceWith);  
    }  
}

String.prototype.startWith=function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
}

String.prototype.endWith=function(str){     
	  var reg=new RegExp(str+"$");     
	  return reg.test(this);        
}

Ext.ux.FieldPlugin = function(config) {
    Ext.apply(this, config);
};
Ext.extend(Ext.ux.FieldPlugin,Ext.util.Observable,{
    init: function(field){
        Ext.apply(field,{
            setReadonly: function(readonly){
                if(readonly == null) readonly = true;
                field.readOnly = readonly;
                if(field.getEl() != null){
                    field.getEl().dom.readOnly = readonly;
                }
            }
        });
    }
});


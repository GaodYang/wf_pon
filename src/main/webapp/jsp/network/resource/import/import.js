/**
*业务资源导入界面
*@author guoxiaolin
*/
Ext.ns("NETWORK");
$importcss(ctx + "/jslib/ext/ux/fileuploadfield/css/fileuploadfield.css");
$importjs(ctx + "/jslib/ext/ux/fileuploadfield/FileUploadField.js");
NETWORK.import = Ext.extend(Ext.Panel, {	
	id	: 'NETWORK.import',
	layout : 'vbox',
	border : false,
	readOnly : false,
//	autoScroll:true,
	frame : true,
	loadData : true,
	loadName:'家客资源导入模板',
	prjcode : '',
	padding:"0 0 0 0",
    margin:"0 0 0 0",
    updateCount : 0,
    height:80,
	layoutConfig:{
		align:"stretch"
	},
	initComponent : function() {
		//scope =this;
		this.fileSelector=new Ext.ux.form.FileUploadField({			              
			emptyText : "请点击右侧图标以选择需要导入的文件",
            hideLabel : true,
            name : "file",
            buttonText : "",
            anchor : "80%",
            flex : 1,
            buttonCfg : {
				iconCls : 'c_image_add' 
			}
	    });
		
		
		this.processBar=new Ext.ProgressBar({
			 anchor : "95%"
		});
		
		this.processPanel=new Ext.Panel({
			region:"north",
			 title    : "导入进度",
	            frame    : true,
	            border   : true,
	         //   height   : 56,
	            hidden   : true,
	            hideMode : "offsets",
	            layout : "fit",
	            items    : [this.processBar]
		});
		
		this.downloadBtn=new Ext.Button({
			text:"模板下载",
			iconCls : 'c_page_excel',
			minWidth:80
		});	
        this.importBtn   = new Ext.Button({
        	text: "批量导入",
        	iconCls : 'c_page_excel',
        	minWidth: 80        	
        });
        
        this.uploadForm = new Ext.FormPanel( {
			labelWidth : 150,
			fileUpload : true,
			items : [ {
				layout : 'column',
				items : [ {
					columnWidth : .7,
					layout: 'anchor',
					items : [  this.fileSelector ]
				}, {
					columnWidth : .1,
					items : [  this.downloadBtn ]
				}, {
					columnWidth : .1,
					items : [  this.importBtn ]
				} ]
			} ]

		});
        this.resultLabel = new Ext.form.Label({
        	fieldLabel: '<font size=14/>',
        	text:'导入结果'
        });
        this.southPanel = new Ext.Panel({
            flex : 1,
            layout: "border",
            border : false,
            autoScroll:true,
            items : [
                { region:"center", layout: "fit",  autoScroll:true, frame : true, border : true, items:[this.resultLabel]}]
        });  
        
        this.items = [this.uploadForm,this.southPanel];        
        
        this.downloadBtn.on("click",this.downloadBtnClick,this);
        
        this.importBtn.on("click",this.importBtnClick,this);    
        
        NETWORK.import.superclass.initComponent.call(this);
	},

    downloadBtnClick : function() {    

		var url = UrlHelper.replaceUrlArguments("$(WEBAN_SERVER)/jsp/upload/"+this.loadName+".xls", "");
		window.open(url);
    }, 
    importExcel : function() {
    	this.setImportMsg('');
         
	     var url =   "$(WEBAN_SERVER)/ImportServlet?prjcode="+this.prjcode ;
	     url = UrlHelper.replaceUrlArguments(url, "");
		 this.uploadForm.getForm().submit({
			waitMsg : '正在提交请稍后...',
			method : 'POST',
		    url     : url,		
		    success : function(form, action) {
			 	DWREngine.setAsync(false);
				var msg = "";			 
			 	if(!Ext.isEmpty(action.result.errorFileDownLoadUrl)) {
			 		msg += "<BR><a href='"+action.result.errorFileDownLoadUrl+"'>下载结果文件</a><BR><BR><P></P>";
			 	}
			 	msg += action.result.showMessage;
			 	this.setImportMsg(msg);
			 	DWREngine.setAsync(true);
			},
			failure : function(form, action) {				
			 	this.setImportMsg(action.result.showMssage);
			},
			scope : this
		})
    },
    setImportMsg : function(msg) {
        this.resultLabel.getEl().update(msg);
    },
	importBtnClick:function(){
    	var file = this.fileSelector.getValue();
        if(file.length < 1) {
            Ext.MessageBox.alert("提示", "请选择需要导入的Excel文件");
            return;
        }      
        if (file.lastIndexOf(".xls") == file.length - 4 ){        	
            this.importExcel();
        }else if(file.lastIndexOf(".xlsx") == file.length - 5){
        	 this.importExcel();
        }else{
        	Ext.MessageBox.alert("提示", "请选择后缀为.xls或.xlsx的Excel文件");        	
            return;
        }		
	}
});

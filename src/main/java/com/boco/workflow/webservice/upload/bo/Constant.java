package com.boco.workflow.webservice.upload.bo;

public class Constant {
	public static final String APORT1="OLT侧上行端口1";
	public static final String ZDEVICE1="OLT归属BRAS设备1";
	public static final String ZPORT1="OLT归属BRAS端口1";
	public static final String APORT2="OLT侧上行端口2";
	public static final String ZDEVICE2="OLT归属BRAS设备2";
	public static final String ZPORT2="OLT归属BRAS端口2";
	
	public static final String RELATEDUPPORTCUID="ONU侧上行端口";
	public static final String RELATEDPOSPORTCUID="分光器端口";
	public static final String RELATEDPOSPORTCUID1="归属分光器端口";
	public static final String RELATEDOLTPORTCUID="POS侧上行端口";
	public static final String CANALLOCATETOUSER ="是否直接带用户";
	
	public static final String LOGICID="LOGICID";
	/**
	 * Excel中的字段--------名称
	 */
	public static final String LABELCN="名称";
	public static final String OLT_LABELCN="OLT名称";
	public static final String POS_LABELCN="分光器名称";
	public static final String ONU_LABELCN="ONU名称";
	/**
	 * Excel中的字段--------网管名称
	 */
	public static final String STANDARDNAME="网管名称";
	
	/** 新增POS资源Excel中的字段--------PBOSS系统分光器名称 */
	public static final String PBOSSPOSTANDARDNAME="PBOSS系统分光器名称";
	
	/** 新增ONU资源Excel中的字段--------PBOSS系统分光器名称 */
	public static final String PBOSSONUTANDARDNAME="PBOSS系统ONU名称";
	/**
	 * Excel中的字段--------所属机房
	 */
	public static final String RELATEDROOMCUID="所属机房";
	
	/**
	 * Excel中的字段--------所属机架
	 */
	public static final String RELATEDRACKCUID="所属机架";
	/**
	 * Excel中的字段--------所属EMS/SNMS
	 */
	public static final String RELATEDEMSCUID="所属EMS/SNMS";
	
	/**
	 * add by Mark_zhang 
	 * desc:增加  所属地市(归属空间唯一标识) 字段 且  必填
	 */
	
	public static final String RELATED_DISTRICT_CUID="所属地市";
	
	/**
	 * Excel中的字段--------设备安装地址
	 */
	public static final String LOCATION="设备安装地址";
	/**
	 * Excel中的字段--------设备型号
	 */
	public static final String MODEL="设备型号";
	/**
	 * Excel中的字段--------设备IP地址
	 */
	public static final String DEVIP="设备IP地址";
	/**
	 * Excel中的字段--------生产厂商
	 */
	public static final String RELATEDVENDORCUID="生产厂商";
	/**
	 * Excel中的字段--------SN码
	 */
	public static final String SEQNO="SN码";
	/**
	 * Excel中的字段--------版本信息
	 */
	public static final String SOFTVERSION="软件版本";
	/**
	 * Excel中的字段--------经度
	 */
	public static final String REALLONGITUDE="经度";
	/**
	 * Excel中的字段--------纬度
	 */
	public static final String REALLATITUDE="纬度";
	/**
	 * Excel中的字段--------所属工程
	 */
	public static final String RELATEDPROJECTCUID="所属工程";
	/**
	 * Excel中的字段--------生命周期状态
	 */
	public static final String LIVECYCLE="生命周期状态";
	/**
	 * Excel中的字段--------入网时间
	 */
	public static final String SETUPTIME="入网时间";
	/**
	 * Excel中的字段--------退网时间
	 */
	public static final String BACKNETWORKTIME="退网时间";
	/**
	 * Excel中的字段--------已盘点标签编号
	 */	
	public static final String CHECKLABELNUMBER="已盘点标签编号";
	/**
	 * Excel中的字段--------产权
	 */
	public static final String OWNERSHIP="产权";
	/**
	 * Excel中的字段--------产权人
	 */
	public static final String OWNERSHIPMAN="产权人";
	/**
	 * Excel中的字段--------建设单位
	 */
	public static final String CREATOR="建设单位";
	/**
	 * Excel中的字段--------建设日期
	 */
	public static final String CREATTIME="建设日期";
	/**
	 * Excel中的字段--------维护方式
	 */
	public static final String MAINTMODE="维护方式";
	/**
	 * Excel中的字段--------维护人
	 */
	public static final String PRESERVER="维护人";
	/**
	 * Excel中的字段--------维护人地址
	 */
	public static final String PRESERVERADDR="维护人地址";
	/**
	 * Excel中的字段--------维护人电话
	 */
	public static final String PRESERVERPHONE="维护人电话";
	/**
	 * Excel中的字段--------保修截止日期
	 */
	public static final String REPAIRTIME="保修截止日期";
	/**
	 * Excel中的字段--------续保截止日期
	 */
	public static final String CONTINUEREPAIRTIME="续保截止日期";
	/**
	 * Excel中的字段--------固定资产编号
	 */
	public static final String EQUIPMENTCODE="固定资产编号";
	/**
	 * Excel中的字段--------固定资产分类编号
	 */
	public static final String EQUIPMENTGROUPCODE="固定资产分类编号";
	
	
	//AccessPoint
	/**
	 * Excel中的字段--------资源点名称
	 */
	public static final String APNAME="资源点名称";
	/**
	 * Excel中的字段--------所属区域
	 */
	public static final String DISTRICTCUID   ="所属区域";
	/**
	 * Excel中的字段--------站点
	 */
	public static final String SITECUID   ="站点";
	/**
	 * Excel中的字段--------地址
	 */
	public static final String ADDRESS ="地址";
	//经度纬度上边有
	/**
	 * Excel中的字段--------客户联系人
	 */
	public static final String VPCONTACT="客户联系人";
	/**
	 * Excel中的字段--------客户联系电话
	 */
	public static final String VPPHONE="客户联系电话";
	
	/**
	 * Excel中的字段--------维护经理
	 */
	public static final String MAINTAINMAN="维护经理";
	/**
	 * Excel中的字段--------维护经理电话
	 */
	public static final String MAINTAINPHONE  ="维护经理电话";
	
	/**
	 * topo:Excel中的字段--------本地名称
	 */
	public static final String NATIVENAME  ="本地名称";
	/**
	 * topo:Excel中的字段--------友好名称
	 */
	public static final String USERLABEL  ="友好名称";
	/**
	 * topo:Excel中的字段--------层速率
	 */
	public static final String LAYERRATE  ="层速率";
	/**
	 * topo:Excel中的字段--------方向
	 */
	public static final String DIRECTION  ="方向";
	/**
	 * topo:Excel中的字段--------源端网元
	 */
	public static final String ORIGNECUID  ="源端网元";
	/**
	 * topo:Excel中的字段--------源侧端口
	 */
	public static final String ORIGPTPCUID  ="源侧端口";
	/**
	 * topo:Excel中的字段--------宿端网元
	 */
	public static final String DESTNECUID  ="宿端网元";
	/**
	 * topo:Excel中的字段--------宿侧端口
	 */
	public static final String DESTPTPCUID  ="宿侧端口";
	/**
	 * Excel中的字段--------备注
	 */
	public static final String REMARK  ="备注";
	/**
	 * cardport:Excel中的字段--------网元名称
	 */
	public static final String RELATEDNECUID  ="网元名称";
	public static final String RELATEDNECUIDSB = "所属设备";
	/**
	 * cardport:Excel中的字段--------板卡名称
	 */
	public static final String CARDNAME  ="板卡名称";
	/**
	 * cardport:Excel中的字段--------CARDDB
	 */
	public static final String CARDDB  ="CARDDB";
	/**
	 * cardport:Excel中的字段--------PORTDB
	 */
	public static final String PORTDB  ="PORTDB";
	/**
	 * cardport:Excel中的字段--------单元盘型号（改为板卡型号--wangdp 2016-05-09）
	 */
	public static final String SINGLEMODEL  ="板卡型号";
	/**
	 * cardport:Excel中的字段--------所属机槽编号
	 */
	public static final String RELATEDUPPERCOMPONENTCUID  ="所属机槽编号";
	/**
	 * cardport:Excel中的字段--------端口名称
	 */
	public static final String PORTNAME  ="端口名称";
	/**
	 * cardport:Excel中的字段--------端口编号
	 */
	public static final String PORTNO  ="端口编号";
	/**
	 * cardport:Excel中的字段--------端口速率
	 */
	public static final String PORTRATE  ="端口速率";
	/**
	 * cardport:Excel中的字段--------端口类型
	 */
	public static final String PORTTYPE  ="光电属性";
	public static final String PORTSUBTYPE  ="端口类型";
	public static final String PORTSTATE  ="端口状态";
	/**
	 * cardport:Excel中的字段--------端口VLAN
	 */
	public static final String VLAN  ="端口VLAN";
	/**
	 * cardport:Excel中的字段--------Oun资源的安装位置
	 */
	public static final String RELATEDACCESSPOINT  ="安装位置";
	/**
	 * cardport:Excel中的字段--------所属OLT设备
	 */
	public static final String RELATEDOLTCUID  ="所属OLT设备";
	/**
	 * cardport:Excel中的字段--------所属上联设备
	 */
	public static final String RELATEDUNIONCUID  ="所属上联设备";
	public static final String RELATEDPORTCUID  ="所属上联端口";
	/**
	 * cardport:Excel中的字段--------所属厂商
	 */
	public static final String VENDORCUID  ="所属厂商";
	
	/**
	 * cardport:Excel中的字段--------分光比
	 */
	public static final String RATION  ="分光比";
	/**
	 * cardport:Excel中的字段--------安装位置
	 */
	public static final String RELATEDCABCUID  ="安装位置";
	
	/**
	 * cardport:Excel中的字段--------归属分光器
	 */
	public static final String RELATEDPOSCUID  ="归属分光器";
	/**
	 * cardport:Excel中的字段--------接入方式
	 */
	public static final String FTTX ="接入方式";
	/**
	 * cardport:Excel中的字段--------ONU类型
	 */
	public static final String ACCESSTYPE ="接入类型";
	public static final String RELATEDOLTPORT ="所属PON口";
	public static final String ONUTYPE ="ONU类型";
	/**
	 * cardport:Excel中的字段--------ONU ID号
	 */
	public static final String ONUID="ONU_ID号";
	/**
	 * cardport:Excel中的字段--------ONU的认证类型
	 */
	public static final String    AUTHTYPE="ONU的认证类型";
	/**
	 * cardport:Excel中的字段--------密码
	 */
	public static final String    PASSWORD ="密码";
	/**
	 * cardport:Excel中的字段--------MAC地址
	 */
	public static final String   MACADDRESS ="MAC地址";
	
	/**
	 * servlet传来的表明OLT进行导入导航 
	 */
	public static final String OLTNAME="接入网-OLT设备";
	 									
	/**
	 * servlet传来的表明POS进行导入导航 
	 */
	public static final String POSNAME="接入网-POS设备";
	/**
	 * servlet传来的表明ONU进行导入导航 
	 */
	public static final String ONUNAME="接入网-ONU设备";
	/**
	 * servlet传来的表明OLTPort进行导入导航 
	 */
	public static final String OLTPORTNAME="接入网-OLT端口";
	/**
	 * servlet传来的表明POSPortNAME进行导入导航 
	 */
	public static final String POSPORTNAME="接入网-POS端口";
	/**
	 * servlet传来的表明ONUPortNAME进行导入导航 
	 */
	public static final String ONUPORTNAME="接入网-ONU端口";
	/**
	 * servlet传来的表明AccessPointNAME进行导入导航 
	 */
	public static final String ACCESSPOINTNAME="接入网-资源点";
	/**
	 * servlet传来的表明PotoNAME进行导入导航 
	 */
	public static final String TOPONAME="接入网-拓扑";
	/**
	 * servlet传来的表明GponNAME进行导入导航 
	 */
	public static final String GPONNAME="接入网-拓扑链路";
	/**
	 * servlet传来的表明PONNAME进行导入导航 
	 */
	public static final String PONNAME="PON设备导入模板总表";
	/**
	 * 设置一些常量用于引用queryCount
	 */
	public static final int QUERYCOUNT=50;
	/**
	 * 设置一些常量用于引用用于界面识别传来的如何操作
	 */
	public static final String CONTROLUPADE="update";
	/**
	 * 设置一些常量用于引用用于界面识别传来的如何操作
	 */
	public static final String CONTROLDELETE="updateAll";
	/**
	 * 用于与数据库交到，用接入网-板卡
	 */
	public static final String PONCARD="接入网-板卡";
	/**
	 * 用于与数据库交到，用接入网-板卡
	 */
	public static final String PONPORT="接入网-端口";

	public static final int MAXCOLUMN=1000;
	/**
	 * 覆盖范围属性
	 */
	public static final String NETYPE="网元类型";
	public static final String COVERAGE="覆盖范围";
	public static final String RESIDENTIALNAME="小区、楼宇名";
	public static final String INSTALLEDADDRESS="安装地址";
	public static final String USEPROPERTY="应用属性";
	public static final String MAINTAINUNIT="维护单位";
	public static final String OWNERPROJECT="工程项目名称/编号";
	/**
	 * 客户信息
	 */
	public static final String CUSTOMERNAME="客户名称";
	public static final String CUSTOMERCODE="客户编号";
	public static final String CUSTOMERADDRESS="客户地址";
	public static final String CONTACTNUMBER="联系电话";
	public static final String EMAILADDRESS="EMAIL地址";
	
	/**
	 * 用于OLT代维信息导入
	 */
	public static final String OLTMAINTAIN="接入网-OLT代维信息";
	/**
	 * 用于POS或ONU导入，所属机房/资源点
	 */
	public static final String ROOMACCESSPOINT="所属机房/资源点";
	
	/**
	 * 代维驻点导入.
	 */
	public static final String ROFHSITE = "代维驻点";
	
	/**
	 * 代维公司导入.
	 */
	public static final String ROFHMAINTAINDEPT = "代维公司";
	/*
	 * 为查看链路信息新加的的
	 */
	public static String html2OnuStr = "<table  border='0' cellspacing='0' cellpadding='0' width='800' style='border:none;'><tr>\t<td></td>\t<td><font size=1>onuUpPortName</font></td>\t<td></td>\t<td><font size=1>posTwoUpPortName</font></td>\t<td></td>\t<td><font size=1>posOneUpPortName</font></td>\t<td></td>\t<td><font size=1>oltUpPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/onu.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/onu_pos_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_pos_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_olt_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/olt.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td>\t<td width='160'></td></tr><tr>\t<td></td>\t<td><font size=1>posTwoDownPortName</font></td>\t<td></td>\t<td><font size=1>posOneDownPortName</font></td>\t<td></td>\t<td><font size=1>oltDownPortName</font></td>\t<td></td>\t<td><font size=1>brasDownPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>onuName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posTwoName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posOneName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>oltName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";

	  public static String htmlOnuStr = "<table  border='0' cellspacing='0' cellpadding='0' width='800' style='border:none;'><tr>\t<td></td>\t<td><font size=1>onuUpPortName</font></td>\t<td></td>\t<td><font size=1>posUpPortName</font></td>\t<td></td>\t<td><font size=1>oltUpPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/onu.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/onu_pos_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_olt_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/olt.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td>\t<td width='160'></td></tr><tr>\t<td></td>\t<td><font size=1>posDownPortName</font></td>\t<td></td>\t<td><font size=1>oltDownPortName</font></td>\t<td></td>\t<td><font size=1>brasDownPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>onuName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>oltName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";

	  public static String html2PosStr = "<table  border='0' cellspacing='0' cellpadding='0' width='800' style='border:none;'><tr>\t<td></td>\t<td><font size=1>2posUpPortName</font></td>\t<td></td>\t<td><font size=1>1posUpPortName</font></td>\t<td></td>\t<td><font size=1>oltUpPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_pos_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_olt_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/olt.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td>\t<td width='160'></td></tr><tr>\t<td></td>\t<td><font size=1>1posDownPortName</font></td>\t<td></td>\t<td><font size=1>oltDownPortName</font></td>\t<td></td>\t<td><font size=1>brasDownPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posOneName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>oltName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";

	  public static String htmlPosStr = "<table  border='0' cellspacing='0' cellpadding='0' width='600' style='border:none;'><tr>\t<td></td>\t<td><font size=1>posUpPortName</font></td>\t<td></td>\t<td><font size=1>oltUpPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/pos.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/pos_olt_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/olt.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td>\t<td width='160'></td></tr><tr>\t<td></td>\t<td><font size=1>oltDownPortName</font></td>\t<td></td>\t<td><font size=1>brasDownPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>posName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>oltName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";

	  public static String htmlOltStr = "<table  border='0' cellspacing='0' cellpadding='0' width='400' style='border:none;'><tr>\t<td></td>\t<td><font size=1>oltUpPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/olt.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td>\t<td width='160'></td></tr><tr>\t<td></td>\t<td><font size=1>brasDownPortName</font></td>\t<td></td>\t<td></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>oltName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";

	  public static String htmlLanStr = "<table  border='0' cellspacing='0' cellpadding='0' width='400' style='border:none;'><tr>\t<td></td>\t<td><font size=1>lanDownPortName</font></td>\t<td></td></tr><tr>\t<td width='40'><img src='../../resources/icons/lan.jpg' /></td>\t<td width='160'><img width='100%' src='../../resources/icons/olt_bras_line.jpg'/></td>\t<td width='40'><img src='../../resources/icons/bras.jpg' /></td></tr><tr>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>lanName</font></td>\t<td colspan='2' width='200' style='padding-right:10px;'><font size=1>brasName</font></td></tr></table>";
}

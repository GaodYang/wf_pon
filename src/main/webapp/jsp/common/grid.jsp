<%@page language="java" pageEncoding="UTF-8"%>
<link type="text/css" rel="stylesheet" href="${ctx}/jslib/ext/ux/statusbar/css/statusbar.css"/>
<script type="text/javascript" src="${ctx}/jslib/ext/ux/statusbar/StatusBar.js"></script>

<!-- 表格数据交互控制层 -->
<script type="text/javascript" src="${ctx}/dwr/interface/GridViewAction.js"></script>
<!-- 表格组件扩展 -->
<link type="text/css" rel="stylesheet" href="${ctx}/cmp_min/grid-all_min.css"/>
<script type="text/javascript" src="${ctx}/cmp_min/grid-all_min.js"></script>

<!-- 表格加载辅助工具 -->
<script type="text/javascript">
Frame.SERVICE_DICT_URL = ctx + '/service_dict';
</script>
<script type="text/javascript" src="${ctx}/commons/utils/SynDataHelper.js"></script>
<script type="text/javascript" src="${ctx}/cmp_plugins/grid/utils/GridServiceDictLoader.js"></script>

<!-- 表格基础插件 -->
<!-- bbar 【自定义布局、表格数据导出】-->
<script type="text/javascript" src="${ctx}/cmp_plugins/grid/bbar/GridCustLayout.js"></script>
<script type="text/javascript" src="${ctx}/cmp_plugins/grid/bbar/GridDataExport.js"></script>

<!-- event【数据复制】-->
<script type="text/javascript" src="${ctx}/cmp_plugins/grid/event/GridEvent.js"></script>
<!-- query【通用查询面板】-->
<script type="text/javascript" src="${ctx}/cmp_plugins/grid/query/GridGeneralQueryForm.js"></script>

<link type="text/css" rel="stylesheet" href="${ctx}/jsp/common/combox/AsynCombox.css">
<script type="text/javascript" src="${ctx}/jsp/common/combox/AsynCombox.js"></script>
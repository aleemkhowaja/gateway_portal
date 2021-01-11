<%@include file="/WEB-INF/pages/common/Encoding.jsp" %>
<%@include file="/WEB-INF/pages/common/TLDImport.jsp" %>
<ps:hidden id="ivcrud_${_pageRef}" name="iv_crud" ></ps:hidden>
<jsp:include page="/WEB-INF/pages/common/login/InfoBar.jsp" />

<ps:set name="ivcrud_${_pageRef}"    value="iv_crud"/>
<ps:hidden id="iv_crud_${_pageRef}"  name="iv_crud"/>

<ps:url id="urlchannelListGrid" escapeAmp="false" action="ChannelListAction_loadChannelGrid" namespace="/path/channel">
   <ps:param name="iv_crud"  value="ivcrud_${_pageRef}"></ps:param>
   <ps:param name="_pageRef" value="_pageRef"></ps:param>
</ps:url>
<psjg:grid
	id               ="channelListGridTbl_Id_${_pageRef}"
	caption          =" "
    href             ="%{urlchannelListGrid}"
    dataType         ="json"
    hiddengrid       ='%{iv_crud== "R"}'
	pager            ="true"
	sortable         ="true"
	filter           ="true"
	gridModel        ="gridModel"
	rowNum           ="5"
	rowList          ="5,10,15,20"
    viewrecords      ="true"
    navigator        ="true"
    navigatorDelete  ="false"
    navigatorEdit    ="false"
    navigatorRefresh ="false"
    navigatorAdd     ="false"
    navigatorSearch  ="false"
    navigatorSearchOptions="{closeOnEscape: true,closeAfterSearch: true, multipleSearch: true,sopt:['eq','ne','lt','gt','le','ge']}"
    altRows          ="true"
    ondblclick       ="channelList_onDbClickedEventConform()"
    addfunc          ="channelList_onNewClicked"
    shrinkToFit      ="true"
     height			 ="125" >

	<psjg:gridColumn id="gtw_CHANNELVO.CHANNEL_ID" colType="number"
		name="gtw_CHANNELVO.CHANNEL_ID" index="gtw_CHANNELVO.CHANNEL_ID"
		title="%{getText('channel_id_key')}" editable="false" sortable="true"
		search="true" width="20" />
		
	<psjg:gridColumn id="gtw_CHANNELVO.DESCRIPTION" colType="text"
		name="gtw_CHANNELVO.DESCRIPTION" index="gtw_CHANNELVO.DESCRIPTION"
		title="%{getText('Description_key')}" editable="false" sortable="true"
		search="true" width="40" />
	
	<psjg:gridColumn id="interfaceDescription" colType="text"
		name="interfaceDescription" index="interfaceDescription"
		title="%{getText('interface_key')}" editable="false" sortable="true"
		search="true" width="20" />
		
	<psjg:gridColumn id="communicationProtocol" colType="text"
		name="communicationProtocol" index="communicationProtocol"
		title="%{getText('communication_protocol_key')}" editable="false" sortable="true"
		search="true" width="20" />
		
	<psjg:gridColumn id="serverType" colType="text"
		name="serverType" index="serverType"
		title="%{getText('server_type_key')}" editable="false" sortable="true"
		search="true" width="20" />
	
	<ps:if test='_pageRef == "CHNLMT" || _pageRef == "CHNLP" || _pageRef == "CHNLUP" || _pageRef == "CHNLS" || _pageRef == "CHNLRA"'>
		<psjg:gridColumn name="statusDesc" id="STATUS_DESC"
			searchoptions="{sopt:['eq','ne','bw','bn','ew','en','cn','nc']}"
			index="STATUS_DESC" colType="text" title="%{getText('Status_key')}"
			sortable="true" width="10" />
	</ps:if>
	
	
	<ps:if test='_pageRef == "CHNLAS" || _pageRef == "CHNLARA	"'>
		<psjg:gridColumn name="toBeStatusDesc" id="TO_BE_STATUS_DESC"
			searchoptions="{sopt:['eq','ne','bw','bn','ew','en','cn','nc']}"
			index="TO_BE_STATUS_DESC" colType="text" title="%{getText('to_be_status_key')}"
			sortable="true" width="10" />
	</ps:if>
	
	<psjg:gridColumn id="STATUS"
		searchoptions="{sopt:['eq','ne','bw','bn','ew','en','cn','nc']}"
		colType="text" name="gtw_CHANNELVO.STATUS" index="STATUS"
		title="%{getText('STATUS_key')}" hidden="true" />

</psjg:grid>
<div id="channelListMaintDiv_id_${_pageRef}" style="width:100%;">
   <ps:if test='iv_crud == "R"'>   
      <%@include file="ChannelMaint.jsp"%>
   </ps:if>     
</div>
<script  type="text/javascript">

    $.struts2_jquery.require("ChannelList.js" ,null,jQuery.contextPath+"/path/js/gateway/channel/");
    $.struts2_jquery.require("script.js" ,null,jQuery.contextPath+"/path/js/gateway/common/");
   // $.struts2_jquery.require("ChannelMaint.js" ,null,jQuery.contextPath+"/path/js/gateway/channel/");
    $("#gview_channelListGridTbl_Id_"+_pageRef+" div.ui-jqgrid-titlebar").hide();
    $("#gview_channelMachineIdGridTbl_Id_"+_pageRef+" div.ui-jqgrid-titlebar").hide();
    
</script>
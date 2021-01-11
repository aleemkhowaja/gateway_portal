<%@include file="/WEB-INF/pages/common/Encoding.jsp"%>
<%@include file="/WEB-INF/pages/common/TLDImport.jsp"%>
<%@ taglib prefix="ptt" uri="/path-toolbar-tags"%>
<script type="text/javascript">
	$.struts2_jquery.require("VerificationMaint.js", null, jQuery.contextPath+ "/path/js/gateway/channel/verification/");
</script>

	<div id="machineIDGridrDiv_${_pageRef}"  style="margin-bottom: 5px"
		title="<ps:text name="varification_details_key"/>" class="http_server channelCollapsableDiv_${_pageRef}">
		<ps:url id="urlchannelMachineId" escapeAmp="false" action="ChannelListAction_returnVerficationListForGrid" 
			namespace="/path/channel">
			<ps:param name="iv_crud" value="ivcrud_${_pageRef}"></ps:param>
			<ps:param name="_pageRef" value="_pageRef"></ps:param>
			<ps:param name="criteria.ChannelId" value="channelCO.gtw_CHANNELVO.CHANNEL_ID" />
		</ps:url>

		<psjg:grid 
			id="channelMachineIdGridTbl_Id_${_pageRef}" 
			caption=" "
			href="%{urlchannelMachineId}"
			editurl="%{urlchannelMachineId}"
			editinline="true" 
			dataType="json" 
			hiddengrid='' 
			pager="true"
			sortable="true" 
			filter="false" 
			gridModel="gridModel" 
			rowNum="20"
			rowList="20,25,50,75,100" 
			viewrecords="true" 
			navigator="true"
			navigatorDelete="true" 
			navigatorEdit="false" 
			navigatorRefresh="false"
			navigatorAdd="true" 
			navigatorSearch="false"
			navigatorSearchOptions="{closeOnEscape: true,closeAfterSearch: true, multipleSearch: true,sopt:['eq','ne','lt','gt','le','ge']}"
			altRows="true" 
			addfunc="verificationMaint_addRow(this)"
			delfunc="verificationMaint_deleteRow" 
			shrinkToFit="true" 
			height="130"
			onCompleteTopics="setGridLayout"
			onSuccessTopics="setGridLayout"
			>

			<psjg:gridColumn 
				id="imApiChannelsDetVO.HOST_NAME" 
				colType="text"
				name="imApiChannelsDetVO.HOST_NAME"
				index="imApiChannelsDetVO.HOST_NAME"
				align="center"
				title="%{getText('host_name_key')}" 
				sortable="true" 
				search="true"
				required="true" 
				editable="true"
				editoptions="{maxlength : 40,dataEvents: [{ type: 'change', fn: function(e) { verificationMaint_generateKey()}}]}"
				width="30" />

			<psjg:gridColumn 
				id="imApiChannelsDetVO.HASH_KEY" 
				colType="text"
				name="imApiChannelsDetVO.HASH_KEY"
				index="imApiChannelsDetVO.HASH_KEY" 
				title="%{getText('hash_key')}"
				editable="false" 
				sortable="true" 
				search="true" 
				maxValue="128" />

		</psjg:grid>
	</div>
	
	<script type="text/javascript">
	
		$("#channelMachineIdGridTbl_Id_"+_pageRef).subscribe("setGridLayout",function(rowObj)
		{
			indEvent_setLayout('channelMachineIdGridTbl_Id');		
		});
		
		$(".channelCollapsableDiv_"+_pageRef).collapsiblePanel();
		
	</script>
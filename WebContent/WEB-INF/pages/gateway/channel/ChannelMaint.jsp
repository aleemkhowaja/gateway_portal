<%@include file="/WEB-INF/pages/common/Encoding.jsp"%>
<%@include file="/WEB-INF/pages/common/TLDImport.jsp"%>
<%@ taglib prefix="ptt" uri="/path-toolbar-tags"%>
<%@include file="/WEB-INF/pages/gateway/common/GatewayAppTrans.jsp"%>

<script type="text/javascript">
	$.struts2_jquery.require("ChannelMaint.js", null, jQuery.contextPath + "/path/js/gateway/channel/");
</script>

<ps:hidden id='channelStatus_${_pageRef}' name='channelCO.gtw_CHANNELVO.STATUS'  />
<ps:set name="channelStatus_${_pageRef}"    value="channelCO.gtw_CHANNELVO.STATUS"/>
<ps:set name="ivcrud_${_pageRef}" value="iv_crud" />
<ps:set name="Duplicate_Value_var" 	 value="%{getEscText('Duplicate_Value_key')}"/>	
<ps:set name="at_least_one_host_is_required_var" 	 value="%{getEscText('at_least_one_host_is_required_key')}"/>	

<ps:form  id="channelMaintFormId_${_pageRef}" namespace="/path/channel" useHiddenProps="true">
	<ps:hidden name="channelCO.updateMode" 	id="updateMode_hdn_${_pageRef}" />
	<ps:hidden id="DATE_UPDATED_${_pageRef}" name="channelCO.gtw_CHANNELVO.DATE_UPDATED" />
	<ps:hidden id="isFormChanged_${_pageRef}" name="isFormChanged" />
	<ps:hidden id="interfaceType_${_pageRef}" name="channelCO.interfaceType" />
	
	<div id="machineIDHeaderDiv_${_pageRef}" class="channelCollapsableDiv_${_pageRef}" style="margin-bottom : 5px" title="<ps:text name="channel_details_key"/>">
		<table width="100%" cellpadding="2" cellspacing="2">
			<tr>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
				<td width="10%"></td>
			</tr>
			<tr>
				<td colspan="10">
					<br> 
				</td>
			</tr>
			<tr>
				<td colspan="1" nowrap="nowrap">
					<ps:label key="channel_id_key" for="channel_id_${_pageRef }" id="channel_id_lbl_${_pageRef}">
				</ps:label></td>
				<td colspan="1">
					<ps:textfield mode="number" id="channel_id_${_pageRef }"
						dependencySrc="${pageContext.request.contextPath}/path/channel/ChannelMaintAction_validateChannelId"
						parameter="channelCO.gtw_CHANNELVO.CHANNEL_ID:channel_id_${_pageRef }"
						dependency="channel_id_${_pageRef }:channelCO.gtw_CHANNELVO.CHANNEL_ID"
						name="channelCO.gtw_CHANNELVO.CHANNEL_ID" minValue="0" maxlength="16" required="false">
					</ps:textfield>
				</td>
			  	<td colspan="6"></td>
			  	<td colspan="1" nowrap="nowrap">
					<ps:textfield name="channelCO.statusDesc"
						id="statusDesc_${_pageRef}" readonly="true"
						cssStyle="background:${channelCO.statusColorCode}!important;color:white!important;text-align:center!important">
					</ps:textfield>
				</td>
				  <td colspan="1">
				  	<psj:a button="true" href="#" buttonIcon="ui-icon-triangle-2-s"
						onclick="channelList_onStatusClicked()">
						<ps:text name='status_key' />
					</psj:a>
				</td>
			</tr>
			<tr>			  
				<td colspan="1" width="13%;" nowrap="nowrap">
					<ps:label key="Description_key" for="Description_id_${_pageRef }"
						id="Description_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="5"><ps:textfield id="Description_id_${_pageRef }"
						name="channelCO.gtw_CHANNELVO.DESCRIPTION" maxlength="40"
						required="true"></ps:textfield>
				</td>
			</tr>
			<tr>			  
				<td nowrap="nowrap"><ps:label key="communication_protocol_key" for="communicationProtocol_id_${_pageRef }" id="communicationProtocol_lbl_${_pageRef}"></ps:label></td>
				
				<td >
					<ps:select
						id="communicationProtocol_id_${_pageRef}"
					 	list="communicationProtocolList"  
					 	listKey="code" 
					 	listValue="descValue" 
					 	name="channelCO.gtw_CHANNELVO.COMMUNICATION_PROTOCOL"
						cssStyle="width:100%"
						dependencySrc="${pageContext.request.contextPath}/path/channel/ChannelMaintAction_fillServerTypeCombo"
						dependency="serverType_${_pageRef}:serverTypeList"
						parameter="criteria.communicationProtocol:communicationProtocol_id_${_pageRef}, channelCO.gtw_CHANNELVO.CHANNEL_ID:channel_id_${_pageRef}" 
						afterDepEvent="channelMaint_afterCommunicationModeDependancy();"
						 />
				</td>
				
				<td nowrap="nowrap" align="right">
					<ps:label id="serverTypeLbl_${_pageRef}" key="server_type_key"
						for="type_${_pageRef}">
					</ps:label>
				</td>
				<td nowrap="nowrap">
					<ps:select id="serverType_${_pageRef}"
						name="channelCO.gtw_CHANNELVO.SERVER_TYPE"
						list="serverTypeList" listKey="code" listValue="descValue"
				
						cssStyle="width: 125px;"
						dependencySrc="${pageContext.request.contextPath}/path/channel/ChannelMaintAction_serverTypeDependancy"
						dependency="serverType_${_pageRef} : channelCO.gtw_CHANNELVO.SERVER_TYPE"
						parameter="channelCO.gtw_CHANNELVO.SERVER_TYPE:serverType_${_pageRef}"
					>
					</ps:select>
				</td>
			</tr>
			<tr>			  
				<td nowrap="nowrap">
					<ps:label key="ip_address_key" for="ip_id_${_pageRef }"
						id="ip_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="1">
					<ps:textfield id="ip_id_${_pageRef }" name="channelCO.gtw_CHANNELVO.IP_ADDRESS"
						maxlength="50" required="false" cssStyle="width:100%">
					</ps:textfield>
				</td>
			</tr>
			<tr id="comm_tcp_${_pageRef}" >		
				<td nowrap="nowrap">
					<ps:label key="port_key" for="port_id_${_pageRef }"
						id="port_lbl_${_pageRef}" >
					</ps:label>
				</td>
				<td colspan="1">
					<ps:textfield mode="number" id="port_id_${_pageRef}" name="channelCO.gtw_CHANNELVO.PORT"
						maxlength="8" required="false" cssStyle="width:100%" >
					</ps:textfield>
				</td>
			</tr>
			<tr id="htp_client_${_pageRef}" nowrap="nowrap">			  
				<td >
					<ps:label key="end_point_key" for="endPoint_id_${_pageRef }"
						id="endPoint_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="5">
					<ps:textfield id="endPoint_id_${_pageRef}" name="channelCO.gtw_CHANNELVO.END_POINT"
						maxlength="1000" required="false" cssStyle="width:100%">
					</ps:textfield>
				</td>
			</tr>
			<tr id="http_client_basic_auth_${_pageRef}" nowrap="nowrap">
				<td  nowrap="nowrap">
					<ps:label key="basic_auth_username_key" for="httpUsername_${_pageRef }"
 							id="httpUsername_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2">
					<ps:textfield id="httpUsername_${_pageRef }"
						name="channelCO.gtw_CHANNELVO.HTTP_USER" maxlength="50"
						cssStyle="text-transform:uppercase">
					</ps:textfield>
				</td>
				<td colspan="1" align="right" nowrap="nowrap" >
					<ps:label key="basic_auth_password_key" for="httpPassword_${_pageRef }"
						id="httpPassword_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2">
					<ps:password style="height:20px" id="httpPassword_${_pageRef}"
						name="channelCO.gtw_CHANNELVO.HTTP_PASSWORD" showPassword="true"
						maxlength="128" mode="character"  />
				</td>
			</tr>
			<tr id="htp_server_${_pageRef }" >
				<td nowrap="nowrap" >
					<ps:label key="username_key" for="userId_${_pageRef }"
						id="userId_lbl_${_pageRef}">
				    </ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="userId_${_pageRef }"
						name="channelCO.gtw_CHANNELVO.USER_ID"
						dependencySrc="${pageContext.request.contextPath}/path/channel/ChannelMaintAction_validateUser"
						parameter="channelCO.gtw_CHANNELVO.USER_ID:userId_${_pageRef }"
						dependency="userId_${_pageRef }:channelCO.gtw_CHANNELVO.USER_ID"
						maxlength="8" required="true" cssStyle="text-transform:uppercase">
					</ps:textfield>
				</td>
				<td colspan="1" align="right" nowrap="nowrap" >
					<ps:label key="pop_axs_priv_key" for="lookuptxt_template_id_${_pageRef }"
							id="template_id_lbl_${_pageRef}" >
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:hidden id = "selTempId_${_pageRef}" name ="channelCO.jsonMultiselectArray"></ps:hidden>
					<psj:livesearch id="template_id_${_pageRef}"
						name="channelCO.gtw_CHANNELVO.TEMPLATE_ID"
						multiSelect="true" 
						multiResultInput="selTempId_${_pageRef}"
						selectColumn="TEMPLATE_ID"
						actionName="${pageContext.request.contextPath}/path/channel/QueryLookupAction_constructQryLookup"
						searchElement="TEMPLATE_ID" 
						autoSearch="false" >
					</psj:livesearch>
				</td>
			</tr>
			
			<tr>
				<td nowrap="nowrap">
					<ps:label
						id="lbl_interface_${_pageRef}"
						key="interface_key" for="lookuptxt_interface_id_${_pageRef}">
					</ps:label>
				</td>
				<td>
					<psj:livesearch
						actionName="${pageContext.request.contextPath}/path/atmInterfaces/ATMInterfacesLookupAction_constructLookup"
						id="interface_id_${_pageRef}"
						name="channelCO.gtw_CHANNELVO.INTERFACE_CODE"
						paramList="compCode:companyCode_hdn_${_pageRef},atmInterfaceSC.isInterfaceValidationRequired:true" mode="number"
						size="5" maxlength="16" maxValue="9999999999999999" 
						parameter="atmInterfaceSC.interfaceId:lookuptxt_interface_id_${_pageRef}, atmInterfaceSC.isInterfaceValidationRequired:true"
						dependency="lookuptxt_interface_id_${_pageRef}:atmInterfaceCO.iso_INTERFACESVO.INTERFACE_CODE,
						interfaceDescription_${_pageRef}:atmInterfaceCO.iso_INTERFACESVO.DESCRIPTION,
						interfaceType_${_pageRef}:atmInterfaceCO.iso_INTERFACESVO.INTERFACE_TYPE"
						dependencySrc="${pageContext.request.contextPath}/path/atmInterfaces/ATMInterfacesLookupDependancyAction_returnATMInterfaceDetails"
						resultList="iso_INTERFACESVO.INTERFACE_CODE:lookuptxt_interface_id_${_pageRef}"
						searchElement="iso_INTERFACESVO.DESCRIPTION"
						required="true"
						>
					</psj:livesearch>
				</td>
				<td class="fldDataEdit center" colspan="4">
					<ps:textfield
						id="interfaceDescription_${_pageRef}" name="channelCO.interfaceDescription"
						readonly="true" tabindex="-1" cssStyle="height:20px" />
				</td>
			</tr>
<%-- 			<tr>			  
				<td>
					<ps:label key="interface_key" for="communicationFormat_id_${_pageRef }" id="communicationFormat_lbl_${_pageRef}"></ps:label>
				</td>
				<td >
					<psj:livesearch id="interface_id_${_pageRef}"
						name="channelCO.gtw_CHANNELVO.INTERFACE_CODE"
						resultList="CODE:lookuptxt_interface_id_${_pageRef},interfaceDescription_${_pageRef}:BRIEF_DESCRIPTION"
						actionName="${pageContext.request.contextPath}/path/channel/InterfaceLookupAction_constructInterfaceLookup"
						parameter="criteria.interfaceCode:lookuptxt_interface_id_${_pageRef}"
						dependencySrc="${pageContext.request.contextPath}/path/channel/InterfaceLookupDependancyAction_returnInterfaceByCode"
						dependency="lookuptxt_interface_id_${_pageRef}:channelCO.gtw_DGTL_INTERFACESVO.CODE, 
									interfaceDescription_${_pageRef}:channelCO.gtw_DGTL_INTERFACESVO.BRIEF_DESCRIPTION"
						searchElement="CODE" 
						autoSearch="false" 
						>
					</psj:livesearch>
				</td>
				<td colspan="4">
					<ps:textfield 
						id="interfaceDescription_${_pageRef}"
						name="channelCO.interfaceDescription" 
						cssStyle="text-transform:uppercase" readonly="true" >
					</ps:textfield></td>
			</tr> --%>
<%-- 			<tr>
				<td nowrap="nowrap" >
					<ps:label id="lbl_activeJMS_${_pageRef}" key="activeQueue_key"
						for="activeJMS_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap">
					<ps:checkbox id="activeJMS_${_pageRef}" name="channelCO.gtw_CHANNELVO.ACTIVE_QUEUE_YN"
						valOpt="Y:N"
						onchange="channelMaint_onChangeActiveJMS();"  />
				</td>
				<td nowrap="nowrap" >
					<ps:label id="lbl_parallelismControl_${_pageRef}"
						key="parallelismControl_key" for="parallelismControl_${_pageRef}"
						cssStyle="display: none;">
					</ps:label>
				</td>
				<td nowrap="nowrap">
					<ps:textfield id="parallelismControl_${_pageRef }"
						name="channelCO.gtw_CHANNELVO.PARALLELISM_CONTROL" maxlength="18"
						required="false" mode="number" cssStyle="width:68%;display: none;">
					</ps:textfield>
				</td>
			</tr> --%>
			<tr id="communication_protocol_tcp_${_pageRef}" >
				<td colspan="2" nowrap="nowrap">
					<ps:label
						id="lbl_restart_time_${_pageRef}"
						key="restart_socket_if_no_message_recieve_after_key"
						for="restart_time_${_pageRef}"></ps:label>
				</td>
				<td nowrap="nowrap">
					<psj:spinner id="restart_time_${_pageRef}" name="channelCO.gtw_CHANNELVO.SOCKET_RESTART_INTERVAL" ></psj:spinner>
				</td>
				<td nowrap="nowrap">
					<ps:label id="lbl_minutes_${_pageRef}"
						key="minutes_key"></ps:label>
				</td>
			</tr>
			
			<tr id="communication_protocol_http_${_pageRef}">
				<td colspan="1" nowrap="nowrap" >
					<ps:label
						id="request_time_out_lbl_${_pageRef}"
						key="request_time_out_key"
						for="request_time_out_${_pageRef}"></ps:label>
				</td>
				<td nowrap="nowrap" >
					<psj:spinner id="request_time_out_${_pageRef}" name="channelCO.gtw_CHANNELVO.HTTP_REQUEST_TIME_OUT" min="0" max="60" maxlength="8" ></psj:spinner>
				</td>
				<td nowrap="nowrap">
					<ps:label id="lbl_minutes_${_pageRef}"
						key="second_key"></ps:label>
				</td>
			</tr>
		</table>
	</div>
	<br>
	
	<!-- Verification Grid Details -->
	<%@include file="ChannelParams.jsp"%>
		
	<!-- Verification Grid Details -->
	<%@include file="verification/VerificationMaint.jsp"%>
	
	<div>
		<ptt:toolBar id="channelMaintToolBar_${_pageRef}" hideInAlert="true">
			<ps:if test='%{#ivcrud_${_pageRef}=="R"}'>
				<ps:if test='%{#channelStatus_${_pageRef}==null || #channelStatus_${_pageRef}=="A"}'>
					<psj:submit id="channelMaint_save_${_pageRef}" button="true"
						freezeOnSubmit="true">
						<ps:label key="Save_key" for="channelMaint_save_${_pageRef}" />
					</psj:submit>
				</ps:if>
				<ps:if test='%{#channelStatus_${_pageRef}=="A"}'>
					<psj:submit id="channelMaint_del_${_pageRef}" button="true"
						onclick="channelMaint_processDelete()" freezeOnSubmit="true"
						type="button">
						<ps:text name="Delete_key"></ps:text>
					</psj:submit>
				</ps:if>
			</ps:if>	
			<ps:if test='%{#ivcrud_${_pageRef}=="P"}'>
				<psj:submit id="channelMaint_approve_${_pageRef}" button="true"
					freezeOnSubmit="true" onclick="channelMaint_processApprove()">
					<ps:label key="approve_key" for="channelMaint_approve_${_pageRef}" />
				</psj:submit>
			</ps:if>
			<ps:if test='%{#ivcrud_${_pageRef}=="J"}'>
				<psj:submit id="channelMaint_reject_${_pageRef}" button="true"
					freezeOnSubmit="true" onclick="channelMaint_processReject()">
					<ps:label key="reject_key" for="channelMaint_reject_${_pageRef}" />
				</psj:submit>
			</ps:if>
			<ps:if test='%{#ivcrud_${_pageRef}=="UP"}'>
				<psj:submit id="channelMaint_UpdateAfterApprove_${_pageRef}"
					button="true" freezeOnSubmit="true">
					<ps:label key="update_key"
						for="channelMaint_UpdateAfterApprove_${_pageRef}" />
				</psj:submit>
			</ps:if>
			<ps:if test='%{#ivcrud_${_pageRef}=="S"}'>
				<psj:submit id="channelMaint_toBeSuspend_${_pageRef}" button="true" freezeOnSubmit="true" onclick="channelMaint_processTobeSuspend()">
    				<ps:label key="suspend_key" for="subscriptionMaint_toBeSuspend_${_pageRef}"/>
   				</psj:submit>
			</ps:if>
			
			<ps:if test='%{#ivcrud_${_pageRef}=="AS"}'>
				<psj:submit id="channelMaint_Suspend_${_pageRef}" button="true"
					freezeOnSubmit="true" onclick="channelMaint_processSuspend()">
					<ps:label key="approve_suspend_key" for="channelMaint_Suspend_${_pageRef}" />
				</psj:submit>
			</ps:if>
			
			<ps:if test='%{#ivcrud_${_pageRef}=="RA"}'>
				<psj:submit id="channelMaint_toBeReactivate_${_pageRef}" button="true" freezeOnSubmit="true" onclick="channelMaint_processTobeReactivate()">
    				<ps:label key="reactivate_key" for="subscriptionMaint_toBeReactivate_${_pageRef}"/>
    			</psj:submit>
			</ps:if>
			
			<ps:if test='%{#ivcrud_${_pageRef}=="AR"}'>
				<psj:submit id="channelMaint_Reactivate_${_pageRef}" button="true"
					freezeOnSubmit="true" onclick="channelMaint_processReactivate()">
					<ps:label key="approve_reactivate_key"
						for="channelMaint_Reactivate_${_pageRef}" />
				</psj:submit>
			</ps:if>
		</ptt:toolBar>
	</div>
	<ps:hidden name="updates" id="updateMachineIdParameter_${_pageRef}"></ps:hidden>
</ps:form>
<script type="text/javascript">
	var Duplicate_Value_key = "${Duplicate_Value_var}";
	var at_least_one_host_is_required_key = "${at_least_one_host_is_required_var}";
	$(document).ready(
			function() {
				$("#channelMaintFormId_" + _pageRef).processAfterValid("channelMaint_processSubmit", {});
				$("#serverType_"+_pageRef).change();
				
				$('#channelMaintFormId_'+_pageRef+' :input').on('change input', function() {
				    $("#isFormChanged_"+_pageRef).val("1");
				});
				
			});
</script>
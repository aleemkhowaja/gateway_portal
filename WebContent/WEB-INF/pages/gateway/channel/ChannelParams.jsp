<%@include file="/WEB-INF/pages/common/Encoding.jsp"%>
<%@include file="/WEB-INF/pages/common/TLDImport.jsp"%>
<%@ taglib prefix="ptt" uri="/path-toolbar-tags"%>
<%@include file="/WEB-INF/pages/gateway/common/GatewayAppTrans.jsp"%>
	
	<ps:hidden id="gtwChannelParamsCode_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.GTW_CHANNEL_PARAMS_ID" />
	<div id="channelIsoInterfacepParams_${_pageRef}" class="channelCollapsableDiv_${_pageRef}" style="margin-bottom : 5px;display:none;" title="<ps:text name="iso_channel_engine_parameters_key"/>">
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
				
				<td nowrap="nowrap" >
					<ps:label key="engine_worker_number_key" for="engineWorkerNumber_${_pageRef }"
						id="engineWorkerNumber_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="engineWorkerNumber_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_WORKER_NO"
						maxlength="3" required="true" mode="number" >
					</ps:textfield>
				</td>
				
				<td nowrap="nowrap" >
					<ps:label id="engineAddLogInHandler_lbl_${_pageRef}" key="engine_add_log_in_handler_key"
						for="engineAddLogInHandler_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap" colspan="2">
					<ps:checkbox id="engineAddLogInHandler_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_ADD_LOGGING_HANDLER_YN"
						valOpt="Y:N"  />
				</td>
				
			</tr>
			
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="engine_idle_timeout_key" for="engineIdleTimeout_${_pageRef }"
						id="engineIdleTimeout_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="engineIdleTimeout_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_IDLE_TIMEOUT"
						maxlength="3" required="true" mode="number">
					</ps:textfield>
				</td>
				
				<td nowrap="nowrap" >
					<ps:label id="engineAddEchoMessageListener_lbl_${_pageRef}" key="engine_add_echo_message_listener_key"
						for="engineAddEchoMessageListener_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap" colspan="2">
					<ps:checkbox id="engineAddEchoMessageListener_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_ADD_ECHO_MSG_LISTENER_YN"
						valOpt="Y:N"/>
				</td>
			</tr>
			
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="engine_acceptor_working_number_key" for="engineAcceptorWorkingNumber_${_pageRef }"
						id="engineAcceptorWorkingNumber_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="engineAcceptorWorkingNumber_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_ACCEPTOR_WORKER_NO"
						maxlength="3" required="true" mode="number">
					</ps:textfield>
				</td>
				
				<td nowrap="nowrap" >
					<ps:label id="engineLogFieldDescription_lbl_${_pageRef}" key="engine_log_field_description_key"
						for="engineLogFieldDescription_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap" colspan="2">
					<ps:checkbox id="engineLogFieldDescription_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_LOG_FIELD_DESCRIPTION_YN"
						valOpt="Y:N"/>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="engine_charset_key" for="engineCharSet_${_pageRef }"
						id="engineCharSet_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="engineCharSet_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_CHARSET"
						maxlength="10" required="true" mode="text">
					</ps:textfield>
				</td>
				
				<td nowrap="nowrap" >
					<ps:label id="engineLogSensitiveData_lbl_${_pageRef}" key="engine_log_sensitive_data_key"
						for="engineLogSensitiveData_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap" colspan="2">
					<ps:checkbox id="engineLogSensitiveData_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_LOG_SENSITIVE_DATA_YN"
						valOpt="Y:N"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="3" > </td>
				<td nowrap="nowrap" >
					<ps:label id="engineReplyOnError_lbl_${_pageRef}" key="engine_reply_on_error_key"
						for="engineReplyOnError_${_pageRef}" >
					</ps:label>
				</td>
				<td nowrap="nowrap" colspan="2">
					<ps:checkbox id="engineReplyOnError_${_pageRef}" name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.ENGN_REPLY_ON_ERROR_YN"
						valOpt="Y:N"/>
				</td>
			</tr>
			<tr> </tr> <tr> </tr> 
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="task_container_session_key" for="taskContainerSession_${_pageRef }"
						id="taskContainerSession_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="taskContainerSession_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.TASK_CONTAINER_SESSION_NO"
						maxlength="2" required="true" mode="number">
					</ps:textfield>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="task_container_consumer_key" for="taskContainerConsumer_${_pageRef }"
						id="taskContainerConsumer__lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="2" >
					<ps:textfield id="taskContainerConsumer_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.TASK_CONTAINER_CONSUMER_NO"
						maxlength="2" required="true" mode="number">
					</ps:textfield>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" >
					<ps:label key="task_container_destination_key" for="taskContainerDestination_${_pageRef }"
						id="taskContainerDestination_lbl_${_pageRef}">
					</ps:label>
				</td>
				<td colspan="5">
					<ps:textfield id="taskContainerDestination_${_pageRef }"
						name="channelCO.gtw_CHANNEL_ISO_INT_PARAMSVO.TASK_CONTAINER_DESTINATION"
						maxlength="255" required="true" mode="text" cssStyle="width:93%">
					</ps:textfield>
				</td>
			</tr>
		</table>
	</div>
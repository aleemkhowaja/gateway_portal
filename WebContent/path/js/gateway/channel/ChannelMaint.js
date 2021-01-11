/**
 * Process before Submit
 * 
 * @returns
 */
function channelMaint_processSubmit()
{
	var isFormChanged = $("#isFormChanged_"+_pageRef).val();
	if (_pageRef == 'CHNLMT' || _pageRef == 'CHNLUP')
	{
		if (isFormChanged == "" || typeof isFormChanged == "undefined")
		{
			_showProgressBar(false);
			_showErrorMsg(changes_not_available_key, info_msg_title);
			return;
		}
		else
		{
			channelMaint_save();
		}
	}
}

/**
 * After Confirmation save
 * 
 * @returns
 */
function channelMaint_save()
{

	var communicationProtocol = $("#communicationProtocol_id_" + _pageRef)
			.val();
	var servertype = $("#serverType_" + _pageRef).val();

	var action = jQuery.contextPath
			+ "/path/channel/ChannelMaintAction_save.action";

	var jsonStringUpdates = $("#channelMachineIdGridTbl_Id_" + _pageRef)
			.jqGrid('getAllRows');
	if (communicationProtocol != "T" && servertype == "HS"
			&& jsonStringUpdates == "{\"root\":[]}")
	{
		_showErrorMsg(at_least_one_host_is_required_key, error_msg_title, 300,
				100);
		return;
	}
	$("#updateMachineIdParameter_" + _pageRef).val(jsonStringUpdates);

	var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
	_showProgressBar(true);
	$.ajax({
		url : action,
		type : "post",
		data : formData,
		dataType : "json",
		success : function(data)
		{
			if (typeof data["_error"] == "undefined" || data["_error"] == null)
			{
				// call clear function
				channelMaint_clear();

				_showProgressBar(false);
				_showErrorMsg(record_saved_Successfully_key, info_msg_title,
						300, 100);

			}
			else
			{
				_showProgressBar(false);
			}
		}

	});
}

/**
 * Process before Approve record
 * 
 * @returns
 */
function channelMaint_processApprove()
{
	_showConfirmMsg(Confirm_Approve_Process_key, confirm_msg_title,
			channelMaint_approve, {}, '', '', 300, 100);
}

/**
 * After Confirmation Approve Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_approve(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_approve.action";
		var jsonStringUpdates = $("#channelMachineIdGridTbl_Id_" + _pageRef)
				.jqGrid('getAllRows');
		$("#updateMachineIdParameter_" + _pageRef).val(jsonStringUpdates);

		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();

		_showProgressBar(true);
		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(data)
			{
				if (typeof data["_error"] == "undefined"
						|| data["_error"] == null)
				{

					// call clear function
					// channelMaint_clear();
					// show/hide grid and set empty form
					showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
					$("#channelListGridTbl_Id_" + _pageRef).trigger(
							"reloadGrid");
					$("#channelListMaintDiv_id_" + _pageRef).html("");

					_showProgressBar(false);
					_showErrorMsg(record_was_Approved_Successfully_key,
							info_msg_title, 300, 100);
				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * Process before Suspend record
 * 
 * @returns
 */
function channelMaint_processTobeSuspend()
{
	_showConfirmMsg(Confirm_Suspend_Process_key, confirm_msg_title,
			channelMaint_tobeSuspended, {}, '', '', 300, 100);
}

/**
 * After Confirmation Suspend Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_tobeSuspended(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_tobeSuspended.action";
		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
		_showProgressBar(true);

		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(param)
			{
				if (typeof param["_error"] == "undefined"
						|| param["_error"] == null)
				{
					// call clear function
					// channelMaint_clear();

					// show/hide grid and set empty form
					showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
					$("#channelListGridTbl_Id_" + _pageRef).trigger(
							"reloadGrid");
					$("#channelListMaintDiv_id_" + _pageRef).html("");

					_showProgressBar(false);
					_showErrorMsg(record_was_Suspended_Successfully_key,
							info_msg_title, 300, 100);

				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * Process before Suspend record
 * 
 * @returns
 */
function channelMaint_processSuspend()
{
	_showConfirmMsg(confirm_suspended_approve_key, confirm_msg_title,
			channelMaint_suspend, {}, '', '', 300, 100);
}

/**
 * After Confirmation Suspend Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_suspend(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_suspend.action";
		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
		_showProgressBar(true);

		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(param)
			{
				if (typeof param["_error"] == "undefined"
						|| param["_error"] == null)
				{
					// call clear function
					// channelMaint_clear();

					// show/hide grid and set empty form
					showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
					$("#channelListGridTbl_Id_" + _pageRef).trigger(
							"reloadGrid");
					$("#channelListMaintDiv_id_" + _pageRef).html("");

					_showProgressBar(false);
					_showErrorMsg(suspended_record_approved_successfully_key,
							info_msg_title, 300, 100);

				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * Process before Suspend record
 * 
 * @returns
 */
function channelMaint_processTobeReactivate()
{
	_showConfirmMsg(Confirm_Reactivate_Process_key, confirm_msg_title,
			channelMaint_tobeReactivate, {}, '', '', 300, 100);
}

/**
 * After Confirmation Reactivate Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_tobeReactivate(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_tobeReactivate.action";

		// var jsonStringUpdates = $("#channelMachineIdGridTbl_Id_" +
		// _pageRef).jqGrid('getAllRows');
		// $("#updateMachineIdParameter_" + _pageRef).val(jsonStringUpdates);

		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
		_showProgressBar(true);
		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(param)
			{
				if (typeof param["_error"] == "undefined"
						|| param["_error"] == null)
				{
					// call clear function
					// channelMaint_clear();

					// show/hide grid and set empty form
					showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
					$("#channelListGridTbl_Id_" + _pageRef).trigger(
							"reloadGrid");
					$("#channelListMaintDiv_id_" + _pageRef).html("");

					_showProgressBar(false);
					_showErrorMsg(record_reactivated_successfully_key,
							info_msg_title, 300, 100);
				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * Process before Suspend record
 * 
 * @returns
 */
function channelMaint_processReactivate()
{
	_showConfirmMsg(confirm_reactivated_approve_key, confirm_msg_title,
			channelMaint_reactivate, {}, '', '', 300, 100);
}

/**
 * After Confirmation Reactivate Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_reactivate(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_reactivate.action";

		// var jsonStringUpdates = $("#channelMachineIdGridTbl_Id_" +
		// _pageRef).jqGrid('getAllRows');
		// $("#updateMachineIdParameter_" + _pageRef).val(jsonStringUpdates);

		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
		_showProgressBar(true);
		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(param)
			{
				if (typeof param["_error"] == "undefined"
						|| param["_error"] == null)
				{
					// call clear function
					// channelMaint_clear();

					// show/hide grid and set empty form
					showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
					$("#channelListGridTbl_Id_" + _pageRef).trigger(
							"reloadGrid");
					$("#channelListMaintDiv_id_" + _pageRef).html("");

					_showProgressBar(false);
					_showErrorMsg(reactivated_record_approved_successfully_key,
							info_msg_title, 300, 100);
				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * Process before Delete record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_processDelete()
{
	_showConfirmMsg(Confirm_Delete_Process_key, confirm_msg_title,
			channelMaint_delete, {}, '', '', 300, 100);
}

/**
 * After Confirmation Delete Record
 * 
 * @param confirm
 * @returns
 */
function channelMaint_delete(confirm)
{
	if (confirm)
	{
		var actionUrl = jQuery.contextPath
				+ "/path/channel/ChannelMaintAction_delete.action";

		// var jsonStringUpdates = $("#channelMachineIdGridTbl_Id_" +
		// _pageRef).jqGrid('getAllRows');
		// $("#updateMachineIdParameter_" + _pageRef).val(jsonStringUpdates);

		var formData = $("#channelMaintFormId_" + _pageRef).serializeForm();
		_showProgressBar(true);
		$.ajax({
			url : actionUrl,
			type : "post",
			dataType : "json",
			data : formData,
			success : function(param)
			{
				if (typeof param["_error"] == "undefined"
						|| param["_error"] == null)
				{
					// call clear function
					channelMaint_clear();

					_showProgressBar(false);
					_showErrorMsg(record_was_Deleted_Successfully_key,
							info_msg_title, 300, 100);

				}
				else
				{
					_showProgressBar(false);
				}
			}
		});
	}
	else
	{
		_showProgressBar(false);
	}
}

/**
 * clear form after save record
 * 
 * @returns
 */
function channelMaint_clear()
{
	$("#channelListGridTbl_Id_" + _pageRef).trigger("reloadGrid");

	var reloadAction = jQuery.contextPath
			+ "/path/channel/ChannelMaintAction_clear.action";
	var reloadParams = {};
	var iv_Crud = returnHtmlEltValue("ivcrud_" + _pageRef);
	reloadParams["iv_crud"] = iv_Crud;
	reloadParams["_pageRef"] = _pageRef;
	_showProgressBar(true);
	$.ajax({
		url : reloadAction,
		type : "post",
		data : reloadParams,
		success : function(data)
		{
			$("#channelListMaintDiv_id_" + _pageRef).html(data);
			if (_pageRef == 'CHNLUP')
			{
				showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
				$("#channelListMaintDiv_id_" + _pageRef).html("");
			}
			else
			{
				$("#channelListMaintDiv_id_" + _pageRef).html(data);
			}
			_showProgressBar(false);
		}

	});
}

/**
 * enable/disable fields according to communication protocol
 * 
 * @returns
 */
function channelMaint_onChangeCommuncationProtocol()
{
	var communicationProtocol = $("#communicationProtocol_id_" + _pageRef)
			.val();
	/*
	 * if(communicationProtocol != "" && communicationProtocol == "F") {
	 * $("#folderLocation_lbl_"+_pageRef).attr("style","display: block;");
	 * $("#folderLocation_id_"+_pageRef).attr("style","display: block;"); } else {
	 * $("#folderLocation_lbl_"+_pageRef).attr("style","display: none;");
	 * $("#folderLocation_id_"+_pageRef).attr("style","display: none;"); }
	 */

	if (communicationProtocol != "" && communicationProtocol == "T")
	{
		$("#userId_" + _pageRef).removeAttr("required");

		$("#accessByserviceRow_" + _pageRef).attr("style", "display: none");
		$("#machineIDGridrDiv_" + _pageRef).attr("style", "display: none");
		$("#serverTypeLbl_" + _pageRef).attr("style", "display: block");

		// TCP server visible
		$(".communication_protocol_tcp").attr("style", "display: table-row");

		// TCP client invisible
		$(".communication_protocol_tcp_client").attr("style", "display: none");

		// Http server/client invisible
		$(".http_client").attr("style", "display: none;");
		$(".http_server").attr("style", "display: none;");
		$(".communication_protocol_http").attr("style", "display: none");
	}
	else
	{
		$("#userId_" + _pageRef).attr("required", "true");
		
		//common remove required fields
		common_removeRequiredFields("ip_id","ip_lbl","id");
		
		$("#accessByserviceRow_" + _pageRef)
				.attr("style", "display: contents;");
		$("#machineIDGridrDiv_" + _pageRef).attr("style", "display: contents;");

		// http client invisble
		$(".http_client").attr("style", "display: none;");

		// TCP server invisible
		$(".communication_protocol_tcp").attr("style", "display: none;");
		// HTTp server visible
		$(".communication_protocol_http").attr("style", "display: table-row;");
		// HTTp server visible
		$(".http_server").attr("style", "display: table-row;");

		// TCP client/server invisible
		$(".communication_protocol_tcp_client").attr("style", "display: none");
		$(".tcp_server").attr("style", "display: none");
	}

	channelMaint_onChangeServerType();

}

/**
 * enable/disable fields by active JMS
 * 
 * @returns
 */
function channelMaint_onChangeActiveJMS()
{
	var isChecked = $("#activeJMS_" + _pageRef + ":checked").val();
	if (isChecked != undefined && (isChecked == "true" || isChecked == true))
	{
		$("#lbl_parallelismControl_" + _pageRef).attr("style",
				"display: block;");
		$("#parallelismControl_" + _pageRef).attr("style", "display: block;");
	}
	else
	{
		$("#lbl_parallelismControl_" + _pageRef)
				.attr("style", "display: none;");
		$("#parallelismControl_" + _pageRef).attr("style", "display: none;");
	}
}

/**
 * onchange server type enable/disable fields
 * 
 * @returns
 */
function channelMaint_onChangeServerType()
{
	var servertype = $("#serverType_" + _pageRef).val();
	var communicationProtocol = $("#communicationProtocol_id_" + _pageRef)
			.val();

	if (communicationProtocol != "" && communicationProtocol == "T")
	{
		if (servertype != "" && servertype == "TC")
		{
			$(".communication_protocol_tcp_client").attr("style",
					"display: table-row");
			
			
			//common make required fields
			common_makeRequiredFields("ip_id","ip_lbl","id");

		}
		else
		{
			$(".communication_protocol_tcp_client").attr("style",
					"display:none");
			//common remove required fields
			common_removeRequiredFields("ip_id","ip_lbl","id");
		}

	}
	else if (communicationProtocol != "" && communicationProtocol == "H")
	{
		if (servertype != "" && servertype == "HC")
		{
			$(".http_client").attr("style", "display: table-row");
			$(".http_server").attr("style", "display: none;");
			$("#userId_" + _pageRef).removeAttr("required");
		}
		else
		{
			$(".http_client").attr("style", "display: none;");
			$(".http_server").attr("style", "display: table-row;");
			$("#userId_" + _pageRef).attr("required", "true");
		}
	}

}

/**
 * call server type depnedancy after communicationMode dependancy
 * @returns
 */
function channelMaint_afterCommunicationModeDependancy()
{
	$("#serverType_"+_pageRef).change();
}

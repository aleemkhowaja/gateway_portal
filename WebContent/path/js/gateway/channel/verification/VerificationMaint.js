/**
 * Generate Key of Verification Host Name
 * 
 * @returns
 */
function verificationMaint_generateKey()
{
	_showProgressBar(true);
	var approveChanelId = $("#channel_id_" + _pageRef).val();
	var approveUser = $("#userId_" + _pageRef).val();
	var approvePassword = $("#Password_id_" + _pageRef).val();

	var $table = $("#channelMachineIdGridTbl_Id_" + _pageRef);
	var selectedRowId = $table.jqGrid('getGridParam', 'selrow');
	var myObject = $table.jqGrid('getRowData', selectedRowId);
	var machineId = myObject["imApiChannelsDetVO.HOST_NAME"];

	// check duplicate value
	var machineIdArr = $table.jqGrid('getCol', 'imApiChannelsDetVO.HOST_NAME');
	for (var i = 0; i < machineIdArr.length; i++)
	{
		var cur = machineIdArr[i];
		for (var j = i + 1; j < machineIdArr.length; j++)
		{
			var next = machineIdArr[j];
			if (cur != "" && next != "" && cur == next)
			{
				_showErrorMsg(Duplicate_Value_key, error_msg_title, 300, 100);
				rowid = $table.jqGrid('getGridParam', 'selrow');
				$table.jqGrid('setCellValue', rowid,
						'imApiChannelsDetVO.HOST_NAME', " ");
				_showProgressBar(false);
				return;
			}
		}
	}

	var dataObj = {
		"channelCO.gtw_CHANNELVO.CHANNEL_ID" : approveChanelId,
		"channelCO.gtw_CHANNELVO.USER_ID" : approveUser,
		"channelCO.channelUserPassword" : approvePassword,
		"channelCO.imApiChannelsDetVO.HOST_NAME" : machineId
	};

	$
			.ajax({
				url : jQuery.contextPath
						+ "/path/channel/ChannelMaintAction_generateVarificationHostKey?_pageRef="
						+ _pageRef,
				type : "get",
				dataType : "json",
				data : dataObj,
				success : function(data)
				{
					_showProgressBar(false);
					if (data["channelCO"]["imApiChannelsDetVO"]["HASH_KEY"] != null
							|| data["channelCO"]["imApiChannelsDetVO"]["HASH_KEY"] != "")
					{
						$("#channelMachineIdGridTbl_Id_" + _pageRef)
								.jqGrid(
										'setCellValue',
										selectedRowId,
										'imApiChannelsDetVO.HASH_KEY',
										data["channelCO"]["imApiChannelsDetVO"]["HASH_KEY"]);
						$("#channelMachineIdGridTbl_Id_" + _pageRef).jqGrid(
								"setCellReadOnly", selectedRowId,
								'imApiChannelsDetVO.HASH_KEY', "true");
					}
				},
			});
}

/**
 * Add Row in Verification Grid
 * 
 * @returns
 */
function verificationMaint_addRow()
{
	var result = $("#channelMachineIdGridTbl_Id_" + _pageRef).jqGrid(
			'checkRequiredCells');
	if (!result)
	{
		_showProgressBar(false);
		return;
	}
	else
	{
		$("#channelMachineIdGridTbl_Id_" + _pageRef).jqGrid('addInlineRow', {});
	}
	
	$("#isFormChanged_"+_pageRef).val("1");
}

/**
 * delete Row in Verification Grid
 * 
 * @param rowId
 * @returns
 */
function verificationMaint_deleteRow(rowId)
{
	$("#channelMachineIdGridTbl_Id_" + _pageRef).jqGrid('deleteGridRow', rowId);
}
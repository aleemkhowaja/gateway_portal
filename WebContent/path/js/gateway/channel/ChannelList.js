
function channelList_onDbClickedEventConform()
{
	var isFormChanged = $("#isFormChanged_"+_pageRef).val();
	
	if (isFormChanged == '' || typeof isFormChanged == "undefined")
	{
		channelList_onDbClickedEvent(true);
	}
	else
	{
		_showConfirmMsg(changes_made_confirm_msg, "","channelList_onDbClickedEvent", "yesNo", '', '', 300, 100);
	}
}
/**
 * dbclick record on grid
 * @returns
 */
function channelList_onDbClickedEvent(yesNo)
{
	
	if(yesNo)
	{
		var action = jQuery.contextPath
		+ "/path/channel/ChannelMaintAction_edit.action";
		var params = {};
		
		var $table = $("#channelListGridTbl_Id_" + _pageRef);
		var selectedRowId = $table.jqGrid('getGridParam', 'selrow');
		var myObject = $table.jqGrid('getRowData', selectedRowId);
		// get the selected rowId
		var channel = myObject["gtw_CHANNELVO.CHANNEL_ID"];
		params["channelCO.gtw_CHANNELVO.CHANNEL_ID"] = channel;
		params["channelIdOldStatus"] = myObject["gtw_CHANNELVO.STATUS"];
		var iv_Crud = returnHtmlEltValue("ivcrud_" + _pageRef);
		// alert(iv_Crud);
		params["iv_crud"] = iv_Crud;
		params["_pageRef"] = _pageRef;
		_showProgressBar(true);
		$.post(action, params, function(param)
		{
			_showProgressBar(false);
		
			if (param.indexOf("<script type=") != -1)
			{
				$("#channelListMaintDiv_id_" + _pageRef).show();
				$("#channelListMaintDiv_id_" + _pageRef).html(param);
				//disable the channel Id
				document.getElementById('channel_id_' + _pageRef).disabled = true
				showHideSrchGrid('channelListGridTbl_Id_' + _pageRef);
		
				var interfaceType = $("#interfaceType_"+_pageRef).val();
				if(interfaceType != "" && typeof interfaceType != "undefined" && interfaceType == "ISO")
				{
					$("#channelIsoInterfacepParams_"+_pageRef).css("display","block")
				}
				else
				{
					$("#channelIsoInterfacepParams_"+_pageRef).css("display","none")
				}
				
				//hide/show component according to communication protocol
				//channelMaint_onChangeCommuncationProtocol();
		
				//active JMS dependancy field show/hide
				channelMaint_onChangeActiveJMS();
			}
			else
			{
				var response = jQuery.parseJSON(param);
				_showErrorMsg(response["_error"], response["_msgTitle"], 400, 100);
			}
		}, "html");
	}
}

/**
 * new button click function
 * @returns
 */
function channelList_onNewClicked()
{
	//call clear function
	channelMaint_clear();
}

function channelList_onStatusClicked()
{
	var ChannelId = $("#channel_id_" + _pageRef).val();
	if (ChannelId == "")
	{
		return;
	}
	var loadSrc = jQuery.contextPath
			+ "/path/channel/ChannelStatusAction_search.action?criteria.ChannelId="
			+ ChannelId + "&_pageRef=" + _pageRef;
	var theFormId = "channelMaintFormId_" + _pageRef;
	showStatus(theFormId, _pageRef, loadSrc, {});
}

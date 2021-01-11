package com.path.gateway.actions.channel;

import java.util.List;

import com.path.gateway.bo.channel.ChannelBO;
import com.path.gateway.bo.channel.ChannelConstant;
import com.path.gateway.vo.channel.ChannelCO;
import com.path.gateway.vo.channel.ChannelSC;
import com.path.struts2.lib.common.base.GridBaseAction;
import com.path.vo.common.SessionCO;


/**
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * ChannelListAction.java used to
 */
public class ChannelListAction extends GridBaseAction
{
    private ChannelBO channelBO;
    private ChannelSC criteria = new ChannelSC();

    /**
     * return Channel Grid
     * 
     * @return
     */
    public String loadChannelGrid()
    {
	try
	{
	    SessionCO sessionCO = returnSessionObject();
	    String[] searchCol = { "gtw_CHANNELVO.CHANNEL_ID", "gtw_CHANNELVO.DESCRIPTION", "interfaceDescription", "communicationProtocol",
		    "serverType", "STATUS_DESC" };
	    criteria.setSearchCols(searchCol);
	    copyproperties(criteria);
	    criteria.setCompCode(sessionCO.getCompanyCode());
	    criteria.setLovTypeId(ChannelConstant.LOV_TYPE_STATUS);
	    criteria.setLovTypeLkOpt(ChannelConstant.LOV_LK_TYPE);
	    criteria.setCommunicationProtocolLovCode(ChannelConstant.COMMUNICATION_PROTOCOL_LOV);
	    criteria.setServerTypeLovCode(ChannelConstant.SERVER_TYPE_LOV);
	    criteria.setCurrAppName(sessionCO.getCurrentAppName());
	    criteria.setPreferredLanguage(sessionCO.getLanguage());
	    criteria.setMenuRef(get_pageRef());
	    criteria.setCrudMode(getIv_crud());

	    if(checkNbRec(criteria))
	    {
		setRecords(channelBO.returnChannelListCount(criteria));
	    }
	    setGridModel(channelBO.returnChannelList(criteria));
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * load Machine
     * 
     * @return
     */
    public String returnVerficationListForGrid()
    {
	try
	{
	    String[] searchCol = { "imApiChannelsDetVO.HOST_NAME", "imApiChannelsDetVO.HASH_KEY" };
	    criteria.setSearchCols(searchCol);
	    copyproperties(criteria);
	    if(criteria.getChannelId() != null)
	    {
		int channelMAchineIdCnt = channelBO.returnVerficationListCountForGrid(criteria);
		setRecords(channelMAchineIdCnt);
		List<ChannelCO> channelMAchineIdList = channelBO.returnVerficationListForGrid(criteria);
		setGridModel(channelMAchineIdList);
	    }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    @Override
    public Object getModel()
    {
	return criteria;
    }

    public ChannelSC getCriteria()
    {
	return criteria;
    }

    public void setCriteria(ChannelSC criteria)
    {
	this.criteria = criteria;
    }

    public void setChannelBO(ChannelBO channelBO)
    {
	this.channelBO = channelBO;
    }
}

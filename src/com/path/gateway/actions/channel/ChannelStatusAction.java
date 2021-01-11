package com.path.gateway.actions.channel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.path.bo.common.ConstantsCommon;
import com.path.gateway.bo.channel.ChannelConstant;
import com.path.gateway.dbmaps.vo.GTW_CHANNELVO;
import com.path.gateway.vo.channel.ChannelSC;
import com.path.struts2.lib.common.base.LookupBaseAction;
import com.path.vo.common.SessionCO;
import com.path.vo.common.select.SelectSC;
import com.path.vo.common.status.StatusAddFieldCO;
import com.path.vo.common.status.StatusCO;

/**
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * ChannelStatusAction.java used to
 */
public class ChannelStatusAction extends LookupBaseAction
{
    private String url;
    private ChannelSC criteria = new ChannelSC();
    private List<StatusAddFieldCO> addFields;

    /**
     * search Startus Grid
     * 
     * @return
     */
    public String search()
    {
	try
	{
	    ServletContext application = ServletActionContext.getServletContext();
	    String theRealPath = application.getContextPath();
	    // TODO add you criteria parameters to the URL
	    url = theRealPath + "/path/channel/ChannelStatusAction_loadStatusGrid.action?criteria.ChannelId="
		    + criteria.getChannelId();
	    addFields = new ArrayList<StatusAddFieldCO>();
	    StatusAddFieldCO newFld = new StatusAddFieldCO(ConstantsCommon.COLUMN_DATE_TYPE, "server_date");

	    addFields.add(newFld);
	}
	catch(Exception ex)
	{
	    handleException(ex, null, null);
	    return ERROR_ACTION;
	}
	return "SUCCESS_STATUS";
    }

    /**
     * Load Status Grid
     * 
     * @return
     */
    public String loadStatusGrid()
    {
	String[] searchCol = { "userName", "status_desc", "status_date", "server_date" };

	List<StatusCO> resultList;
	List<String> colList;
	GTW_CHANNELVO channelVO = new GTW_CHANNELVO();
	try
	{
	    SessionCO sessionCO = returnSessionObject();
	    criteria.setSearchCols(searchCol);
	    copyproperties(criteria);

	    // report_queryVO.setCOMP_CODE(sessionCO.getCompanyCode());
	    colList = ChannelConstant.channelStatusLst;
	    SelectSC lovCriteria = new SelectSC();
	    lovCriteria.setLanguage(sessionCO.getLanguage());
	    lovCriteria.setLovTypeId(ChannelConstant.LOV_TYPE_STATUS);
	    lovCriteria.setCompCode(sessionCO.getCompanyCode());

	    channelVO.setCHANNEL_ID(criteria.getChannelId());

	    resultList = returnCommonLibBO().generateStatusList(channelVO, colList, lovCriteria);
	    setGridModel(resultList);
	}
	catch(Exception ex)
	{
	    handleException(ex, null, null);
	}
	return SUCCESS;
    }

    public String getUrl()
    {
	return url;
    }

    public void setUrl(String url)
    {
	this.url = url;
    }

    public ChannelSC getCriteria()
    {
	return criteria;
    }

    public void setCriteria(ChannelSC criteria)
    {
	this.criteria = criteria;
    }

    public List<StatusAddFieldCO> getAddFields()
    {
	return addFields;
    }

    public void setAddFields(List<StatusAddFieldCO> addFields)
    {
	this.addFields = addFields;
    }
}
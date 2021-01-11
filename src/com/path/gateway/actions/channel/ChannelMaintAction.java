package com.path.gateway.actions.channel;

import java.util.ArrayList;
import java.util.List;

import com.path.bo.common.ConstantsCommon;
import com.path.bo.common.MessageCodes;
import com.path.bo.common.audit.AuditConstant;
import com.path.expression.common.PBFunc.BaseException;
import com.path.gateway.actions.common.base.GatewayBaseAction;
import com.path.gateway.bo.channel.ChannelBO;
import com.path.gateway.bo.channel.ChannelConstant;
import com.path.gateway.bo.common.GatewayCommonConstants;
import com.path.gateway.dbmaps.vo.GTW_CHANNELVO;
import com.path.gateway.dbmaps.vo.GTW_CHANNEL_DETVO;
import com.path.gateway.dbmaps.vo.GTW_PWS_TMPLT_MASTERVO;
import com.path.gateway.vo.channel.ChannelCO;
import com.path.gateway.vo.channel.ChannelSC;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.SecurityUtils;
import com.path.lib.common.util.StringUtil;
import com.path.lib.vo.GridUpdates;
import com.path.struts2.json.PathJSONUtil;
import com.path.vo.common.SessionCO;
import com.path.vo.common.audit.AuditRefCO;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.select.SelectSC;
import com.path.vo.common.status.StatusAddFieldCO;

/**
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * ChannelMaintAction.java used to
 */
public class ChannelMaintAction extends GatewayBaseAction
{
    private ChannelBO channelBO;
    private ChannelCO channelCO = new ChannelCO();
    private ChannelSC criteria = new ChannelSC();
    private String updates;
    private String channelIdOldStatus;
    private String url;
    private List<StatusAddFieldCO> addFields;
    private List<SelectCO> communicationProtocolList;
    private List<SelectCO> communicationFormatList;
    private List<SelectCO> serverTypeList;

    /**
     * return Channel Page
     * 
     * @return
     */
    public String loadChannelPage()
    {
	try
	{
	    set_searchGridId("channelListGridTbl_Id");
	    set_showNewInfoBtn("true");
	    set_showSmartInfoBtn("false");
	    fillSessionData();
	    criteria.setCommunicationProtocol("H");
	    fillDropDown();

	    if(ConstantsCommon.CRUD_MAINTAIN.equals(getIv_crud()))
	    {
		set_showNewInfoBtn("true");
	    }
	    if(ConstantsCommon.CRUD_APPROVE.equals(getIv_crud()))
	    {
		set_recReadOnly("true");
	    }
	    channelCO.setStatusDesc(getText("Active"));

	    // dynamic screen display
	    ChannelCO co = new ChannelCO();
	    GTW_CHANNELVO gtw_CHANNELVO = new GTW_CHANNELVO();
	    gtw_CHANNELVO.setSERVER_TYPE(ChannelConstant.HTTP_SERVER);
	    co.setGtw_CHANNELVO(gtw_CHANNELVO);

	    //set hide/mandatory fields 
	    setAdditionalScreenParams( channelBO.applySysParamSettings(co, true).getElementMap());

	    // set Color in Status Description
	    channelCO.setStatusColorCode(getStatusColorCode(GatewayCommonConstants.STATUS_ACTIVE,
		    GatewayCommonConstants.STATUS_COLOR_CODE_B));
	}
	catch(Exception ex)
	{
	    handleException(ex, null, null);
	}
	return "channelList";
    }

    /**
     * Save Channel Record
     * 
     * @return
     */
    public String save()
    {

	try
	{
	    fillSessionData();
	    AuditRefCO refCO = null;

	    // return Access By Service lookup selected data
	    if(!channelCO.getJsonMultiselectArray().isEmpty())
	    {
		GridUpdates guMultiselect = getGridUpdates(channelCO.getJsonMultiselectArray(),
			GTW_PWS_TMPLT_MASTERVO.class, true);
		channelCO.setAccessServiceList(guMultiselect.getLstAllRec());
	    }

	    // return verification grid data
	    if(updates != null && !updates.equals(""))
	    {
		GridUpdates gu = getGridUpdates(updates, ChannelCO.class, true);
		channelCO.setVarificationDetailList(gu.getLstAllRec());
	    }

	    if(ConstantsCommon.YES.equals(channelCO.getUpdateMode()))
	    {
		refCO = fillAuditDetails(AuditConstant.UPDATE);
		channelCO.setAuditObj(returnAuditObject(channelCO.getClass()));
	    }
	    else
	    {
		refCO = fillAuditDetails(AuditConstant.CREATED);
	    }

	    channelCO.setAuditRefCO(refCO);

	    // save Channel Record
	    channelCO = channelBO.save(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;

    }

    /**
     * return Channel Record by id
     * 
     * @return
     */
    public String edit()
    {
	try
	{
	    // fill comboboxes on screen
	    if(channelCO.getGtw_CHANNELVO() == null)
	    {
		channelCO = channelBO.applySysParamSettings(channelCO, true);
		setAdditionalScreenParams(channelCO.getElementMap());
	    }
	    else
	    {
		ChannelSC channelSC = new ChannelSC();
		SessionCO sessionCO = returnSessionObject();
		channelSC.setCompCode(sessionCO.getCompanyCode());
		channelSC.setBranchCode(sessionCO.getBranchCode());
		channelSC.setChannelId(channelCO.getGtw_CHANNELVO().getCHANNEL_ID());
		channelSC.setLovTypeId(ChannelConstant.LOV_TYPE_STATUS);
		channelSC.setLovTypeLkOpt(ChannelConstant.LOV_LK_TYPE);
		channelSC.setCrudMode(getIv_crud());
		channelSC.setCurrAppName(sessionCO.getCurrentAppName());
		channelSC.setPreferredLanguage(sessionCO.getLanguage());
		channelSC.setMenuRef(get_pageRef());
		channelSC.setOldStatus(channelIdOldStatus);
		// retrieve record
		channelCO = channelBO.edit(channelSC);
		// fill comboboxes
		criteria.setCommunicationProtocol(channelCO.getGtw_CHANNELVO().getCOMMUNICATION_PROTOCOL());
		fillDropDown();

		// set Access By User livesearch json data
		channelCO.setJsonMultiselectArray(
			"{\"root\":".concat(PathJSONUtil.serialize(channelBO.loadAssignedTemplateIdListGrid(channelSC),
				null, null, true, true)).concat("}"));

		if(!(channelCO.getGtw_CHANNELVO().getSTATUS().equals(channelSC.getOldStatus())))
		{
		    throw new BOException(MessageCodes.RECORD_CHANGED);
		}

		channelCO.setStatusColorCode(getStatusColorCode(channelCO.getGtw_CHANNELVO().getSTATUS(),
			GatewayCommonConstants.STATUS_COLOR_CODE_B));

		if(ConstantsCommon.CRUD_APPROVE.equals(getIv_crud()))
		{
		    set_recReadOnly(ConstantsCommon.TRUE);
		}
		else if(ConstantsCommon.ACTIVE_RECORD.equals(channelCO.getGtw_CHANNELVO().getSTATUS())
			|| ChannelConstant.IV_CRUD_UPDATE_AFTER_APPROVE.equals(getIv_crud())
				&& ChannelConstant.STATUS_APPROVED.equals(channelCO.getGtw_CHANNELVO().getSTATUS()))
		{
		    set_recReadOnly(ConstantsCommon.FALSE);
		}
		else
		{
		    set_recReadOnly(ConstantsCommon.TRUE);
		}
		channelCO.setUpdateMode(ConstantsCommon.YES);

		applyRetrieveAudit(AuditConstant.IM_API_CHANNEL_KEY, channelCO.getGtw_CHANNELVO(), channelCO);

		// decrypt password
		if(StringUtil.isNotEmpty(channelCO.getGtw_CHANNELVO().getHTTP_PASSWORD()))
		{
		    channelCO.getGtw_CHANNELVO().setHTTP_PASSWORD(SecurityUtils.decryptAES(ChannelConstant.PASS_ENC_KEY,
			    channelCO.getGtw_CHANNELVO().getHTTP_PASSWORD()));
		}
		
		
		//set hide/mandatory fields 
		
		setAdditionalScreenParams( channelBO.applySysParamSettings(channelCO, true).getElementMap());
	    }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	    return ERROR;
	}
	return "channelMaint";
    }

    /**
     * Delete Channel Record
     * 
     * @return
     */
    public String delete()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // delete record
	    channelBO.delete(channelCO);
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * approve Channel Record
     * 
     * @return
     */
    public String approve()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // approve record
	    channelBO.approve(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * tobeSuspend Channel Record
     * 
     * @return
     */
    public String tobeSuspended()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // suspend record
	    channelBO.tobeSuspended(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * suspend Channel Record
     * 
     * @return
     */
    public String suspend()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // suspend record
	    channelBO.suspend(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * reactivate Channel Record
     * 
     * @return
     */
    public String tobeReactivate()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // reactivate record
	    channelBO.tobeReactivate(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * reactivate Channel Record
     * 
     * @return
     */
    public String reactivate()
    {
	try
	{
	    // apply session value
	    fillSessionData();
	    AuditRefCO refCO = fillAuditDetails(AuditConstant.UPDATE);
	    refCO.setTrxNbr(getAuditTrxNbr());
	    channelCO.setAuditRefCO(refCO);
	    // reactivate record
	    channelBO.reactivate(channelCO);

	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * clear the Form
     */
    public String clear()
    {
	try
	{
	    // fill comboboxes on screen
	    criteria.setCommunicationProtocol(ChannelConstant.HTTP);
	    fillDropDown();
	    channelCO = new ChannelCO();
	    if(ConstantsCommon.CRUD_APPROVE.equals(getIv_crud())
		    || ChannelConstant.IV_CRUD_UPDATE_AFTER_APPROVE.equals(getIv_crud()))
	    {
		set_recReadOnly("true");
	    }
	    else
	    {
		set_recReadOnly("false");
	    }
	    channelCO.setStatusDesc(getText("active_key"));
	    channelCO = channelBO.applySysParamSettings(channelCO, true);
	    setAdditionalScreenParams(channelCO.getElementMap());

	    // set Color in Status Description
	    channelCO.setStatusColorCode(getStatusColorCode(GatewayCommonConstants.STATUS_ACTIVE,
		    GatewayCommonConstants.STATUS_COLOR_CODE_B));
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return "channelMaint";
    }

    /**
     * validate Channel Id
     * 
     * @return
     * @throws BaseException
     */
    public String validateChannelId() throws BaseException
    {

	try
	{
	    channelCO = channelBO.validateChannelId(channelCO);
	    NumberUtil.resetEmptyValues(channelCO);
	}
	catch(Exception e)
	{
	    channelCO.getGtw_CHANNELVO().setCHANNEL_ID(null);
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * Validate user
     * 
     * @return
     * @throws BaseException
     */
    public String validateUser() throws BaseException
    {
	try
	{
	    channelCO = channelBO.validateUser(channelCO);
	    NumberUtil.resetEmptyValues(channelCO);
	}
	catch(Exception e)
	{
	    channelCO.getGtw_CHANNELVO().setUSER_ID(null);
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * Generate Verification Host key
     * 
     * @return
     */
    public String generateVarificationHostKey()
    {
	try
	{
	    String chanelKey;
	    fillSessionData();
	    // update existing row in machine Name grid
	    if(channelCO.getListMachineId() != null && channelCO.getListMachineId().size() > 0)
	    {
		for(int i = 0; i < channelCO.getListMachineId().size(); i++)
		{
		    channelCO.setImApiChannelsDetVO(new GTW_CHANNEL_DETVO());
		    channelCO.getImApiChannelsDetVO().setHOST_NAME(channelCO.getListMachineId().get(i));
		    chanelKey = channelBO.generateVarificationHostKey(channelCO);
		    channelCO.getListMachineId().set(i, chanelKey);
		    channelCO.getImApiChannelsDetVO().setHASH_KEY(chanelKey);
		}
	    }
	    // insert new line in machine Name grid
	    else if(channelCO.getImApiChannelsDetVO() != null)
	    {
		chanelKey = channelBO.generateVarificationHostKey(channelCO);
		channelCO.getImApiChannelsDetVO().setHASH_KEY(chanelKey);
	    }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * Fill Session
     * 
     * @throws BaseException
     */
    public void fillSessionData() throws BaseException
    {
	SessionCO sessionCO = returnSessionObject();
	channelCO.setCompCode(sessionCO.getCompanyCode());
	channelCO.setBranchCode(sessionCO.getBranchCode());
	channelCO.setAppName(sessionCO.getCurrentAppName());
	channelCO.setProgRef(get_pageRef());
	channelCO.setUserID(sessionCO.getUserName());
	channelCO.setLanguage(sessionCO.getLanguage());
	channelCO.setUserID(sessionCO.getUserName());
	try
	{
	    channelCO.setRunningDate(returnCommonLibBO().addSystemTimeToDate(sessionCO.getRunningDateRET()));
	}
	catch(com.path.lib.common.exception.BaseException e)
	{
	    handleException(e, null, null);
	}
    }

    /**
     * Fill Audit Details
     * 
     * @param status
     * @return
     */
    private AuditRefCO fillAuditDetails(String status)
    {
	AuditRefCO refCO = initAuditRefCO();
	try
	{
	    refCO.setOperationType(status);
	    refCO.setKeyRef(AuditConstant.IM_API_CHANNEL_KEY);
	    refCO.setRunningDate(channelCO.getRunningDate());
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return refCO;
    }

    /**
     * Fill Dropdown
     */
    private void fillDropDown()
    {
	try
	{
	    SessionCO sessionCO = returnSessionObject();

	    communicationProtocolList = new ArrayList<SelectCO>();
	    // retrieve communication protocol
	    SelectSC selSC = new SelectSC(ChannelConstant.COMMUNICATION_PROTOCOL_LOV);
	    selSC.setLanguage(sessionCO.getLanguage());
	    selSC.setOrderCriteria("ORDER");
	    selSC.setLovCodesExclude("'H'");
	    communicationProtocolList = returnLOV(selSC);

	    fillServerTypeCombo();

	}
	catch(Exception ex)
	{
	    handleException(ex, null, null);
	}
    }

    /**
     * fill server Type combo by dependancy
     * 
     * @return
     */
    public String fillServerTypeCombo()
    {
	SessionCO sessionCO = returnSessionObject();

	SelectSC selSC = new SelectSC(ChannelConstant.SERVER_TYPE_LOV);
	selSC.setLanguage(sessionCO.getLanguage());
	selSC.setOrderCriteria("ORDER");
//	if(criteria.getCommunicationProtocol().equals(ChannelConstant.TCP))
//	{
	    selSC.setLovCodesExclude("'" + ChannelConstant.HTTP_SERVER + "','" + ChannelConstant.HTTP_CLIENT + "'");
	//}
	//else if(criteria.getCommunicationProtocol().equals(ChannelConstant.HTTP))
	//{
	  //  selSC.setLovCodesExclude("'" + ChannelConstant.TCP_SERVER + "','" + ChannelConstant.TCP_CLIENT + "'");
	//}
	try
	{
	    serverTypeList = returnLOV(selSC);
	}
	catch(Exception ex)
	{
	    handleException(ex, null, null);
	}

	return SUCCESS;
    }

    /**
     * dependancy of server type visible/set mandatory fields while select
     * server type
     * 
     * @return
     */
    public String serverTypeDependancy()
    {
	ChannelCO co = new ChannelCO();
	GTW_CHANNELVO gtw_CHANNELVO = new GTW_CHANNELVO();
	gtw_CHANNELVO.setSERVER_TYPE(channelCO.getGtw_CHANNELVO().getSERVER_TYPE());
	co.setGtw_CHANNELVO(gtw_CHANNELVO);
	co = channelBO.applySysParamSettings(co, false);
	setAdditionalScreenParams(co.getElementMap());
	return SUCCESS;
    }

    public void setChannelBO(ChannelBO channelBO)
    {
	this.channelBO = channelBO;
    }

    public ChannelCO getChannelCO()
    {
	return channelCO;
    }

    public void setChannelCO(ChannelCO channelCO)
    {
	this.channelCO = channelCO;
    }

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

    public String getUpdates()
    {
	return updates;
    }

    public void setUpdates(String updates)
    {
	this.updates = updates;
    }

    public String getChannelIdOldStatus()
    {
	return channelIdOldStatus;
    }

    public void setChannelIdOldStatus(String channelIdOldStatus)
    {
	this.channelIdOldStatus = channelIdOldStatus;
    }

    public String getUrl()
    {
	return url;
    }

    public void setUrl(String url)
    {
	this.url = url;
    }

    public List<StatusAddFieldCO> getAddFields()
    {
	return addFields;
    }

    public void setAddFields(List<StatusAddFieldCO> addFields)
    {
	this.addFields = addFields;
    }

    public List<SelectCO> getCommunicationProtocolList()
    {
	return communicationProtocolList;
    }

    public void setCommunicationProtocolList(List<SelectCO> communicationProtocolList)
    {
	this.communicationProtocolList = communicationProtocolList;
    }

    public List<SelectCO> getCommunicationFormatList()
    {
	return communicationFormatList;
    }

    public void setCommunicationFormatList(List<SelectCO> communicationFormatList)
    {
	this.communicationFormatList = communicationFormatList;
    }

    public List<SelectCO> getServerTypeList()
    {
	return serverTypeList;
    }

    public void setServerTypeList(List<SelectCO> serverTypeList)
    {
	this.serverTypeList = serverTypeList;
    }

}

package com.path.actions.lookups.atminterfaces;

import java.math.BigDecimal;
import java.util.HashMap;

import com.path.bo.atminterfaces.AtmInterfacesBO;
import com.path.bo.atminterfaces.AtmInterfacesConstants;
import com.path.dbmaps.vo.SYS_PARAM_SCREEN_DISPLAYVO;
import com.path.lib.common.exception.BOException;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.LookupBaseAction;
import com.path.vo.atminterfaces.AtmInterfacesCO;
import com.path.vo.atminterfaces.AtmInterfacesSC;
import com.path.vo.common.SessionCO;

/**	
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * AtmInterfacesLookupDependancyAction1.java used to
 */
public class AtmInterfacesLookupDependancyAction extends LookupBaseAction
{

    private AtmInterfacesSC atmInterfaceSC;
    private AtmInterfacesCO atmInterfaceCO;
    private AtmInterfacesBO atmInterfacesBO;
    

    /**
     * Construct vault Lookup based on the VO returned in the resultMap.
     * 
     * @return
     */
    public String returnATMInterfaceDetails()
    {
	try
	{
	    SessionCO sessionCO = returnSessionObject();
	    atmInterfaceSC.setCompCode(sessionCO.getCompanyCode());
	    atmInterfaceSC.setCurrAppName(sessionCO.getCurrentAppName());
	    atmInterfaceSC.setPreferredLanguage(sessionCO.getLanguage());
	    atmInterfaceSC.setInterfaceTypeLovId(AtmInterfacesConstants.INTERFACE_TYPE);
	    atmInterfaceSC.setCrudMode(getIv_crud());
	    atmInterfaceSC.setPageRef(get_pageRef());
	    atmInterfaceSC.setStatus(AtmInterfacesConstants.STATUS_APPROVED);
	    
	    // return Dependancy by interface Code
	    atmInterfaceCO = atmInterfacesBO.returnInterfaceById(atmInterfaceSC);
	    
	    if(atmInterfaceCO != null)
	    {
		if(atmInterfaceCO.getIsChannelExistByInterface() != null
			&& atmInterfaceCO.getIsChannelExistByInterface())
		{
		    throw new BOException(getText("interface_already_used_by_another_channel_key"));
		}

		if(atmInterfaceSC.getIsInterfaceValidationRequired() != null
			&& atmInterfaceSC.getIsInterfaceValidationRequired())
		{
		    HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> elementMap = fieldsVisibleByISOInterface(
			    atmInterfaceCO);
		    setAdditionalScreenParams(elementMap);
		}
	    }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }
    
    /**
     * visible fields by interface type = ISO in channle screen 
     * @param atmInterfaceCO
     * @return
     */
    private HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> fieldsVisibleByISOInterface(AtmInterfacesCO atmInterfaceCO)
    {
	HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> elementMap  = atmInterfaceCO.getElementMap();
	SYS_PARAM_SCREEN_DISPLAYVO channelISOIntParams = new SYS_PARAM_SCREEN_DISPLAYVO();
	SYS_PARAM_SCREEN_DISPLAYVO textFieldISOIntParams = new SYS_PARAM_SCREEN_DISPLAYVO();
	SYS_PARAM_SCREEN_DISPLAYVO checkBoxISOIntParams = new SYS_PARAM_SCREEN_DISPLAYVO();
	if(StringUtil.nullToEmpty(atmInterfaceCO.getIso_INTERFACESVO().getINTERFACE_TYPE()).equalsIgnoreCase(AtmInterfacesConstants.ISO_INTERFACE))
	{
	    channelISOIntParams.setIS_VISIBLE(BigDecimal.ONE);
	    textFieldISOIntParams.setIS_VISIBLE(BigDecimal.ONE);
	    checkBoxISOIntParams.setIS_VISIBLE(BigDecimal.ONE);
	    textFieldISOIntParams.setIS_MANDATORY(BigDecimal.ONE);
	    checkBoxISOIntParams.setIS_MANDATORY(BigDecimal.ZERO);
	}
	else
	{
	    channelISOIntParams.setIS_VISIBLE(BigDecimal.ZERO);
	    textFieldISOIntParams.setIS_VISIBLE(BigDecimal.ZERO);
	    checkBoxISOIntParams.setIS_VISIBLE(BigDecimal.ZERO);
	    textFieldISOIntParams.setIS_MANDATORY(BigDecimal.ZERO);
	    checkBoxISOIntParams.setIS_MANDATORY(BigDecimal.ZERO);
	}
	 elementMap.put("channelIsoInterfacepParams", channelISOIntParams);
	 elementMap.put("engineWorkerNumber", textFieldISOIntParams);
	 elementMap.put("engineAddLogInHandler", checkBoxISOIntParams);
	 
	 elementMap.put("engineIdleTimeout", textFieldISOIntParams);
	 elementMap.put("engineAddEchoMessageListener", checkBoxISOIntParams);
	 
	 elementMap.put("engineAcceptorWorkingNumber", textFieldISOIntParams);
	 elementMap.put("engineLogFieldDescription", checkBoxISOIntParams);
	 
	 elementMap.put("engineCharSet", textFieldISOIntParams);
	 elementMap.put("engineLogSensitiveData", checkBoxISOIntParams);
	 
	 elementMap.put("engineReplyOnError", checkBoxISOIntParams);
	 elementMap.put("taskContainerSession", textFieldISOIntParams);
	 elementMap.put("taskContainerConsumer", textFieldISOIntParams);
	 elementMap.put("taskContainerDestination", textFieldISOIntParams);
	return elementMap;
    }

    @Override
    public Object getModel()
    {
	return atmInterfaceSC;
    }

    public AtmInterfacesSC getAtmInterfaceSC()
    {
        return atmInterfaceSC;
    }

    public void setAtmInterfaceSC(AtmInterfacesSC atmInterfaceSC)
    {
        this.atmInterfaceSC = atmInterfaceSC;
    }

    public AtmInterfacesCO getAtmInterfaceCO()
    {
        return atmInterfaceCO;
    }

    public void setAtmInterfaceCO(AtmInterfacesCO atmInterfaceCO)
    {
        this.atmInterfaceCO = atmInterfaceCO;
    }

    public void setAtmInterfacesBO(AtmInterfacesBO atmInterfacesBO)
    {
        this.atmInterfacesBO = atmInterfacesBO;
    }
    
}
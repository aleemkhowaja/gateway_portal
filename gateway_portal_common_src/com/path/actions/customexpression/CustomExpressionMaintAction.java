package com.path.actions.customexpression;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.apache.struts2.util.URLDecoderUtil;
import com.path.bo.common.ConstantsCommon;
import com.path.bo.customexpression.CustomExpressionBO;
import com.path.bo.customexpression.CustomExpressionConstant;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.StringUtil;
import com.path.lib.vo.GridUpdates;
import com.path.struts2.lib.common.base.BaseAction;
import com.path.vo.common.SessionCO;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.select.SelectSC;
import com.path.vo.customexpression.CustomExpressionCO;
import com.path.vo.customexpression.CustomExpressionSC;
import com.path.vo.customexpression.ExpressionParamCO;

/**
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * CustomExpressionMaintAction.java used to
 */
public class CustomExpressionMaintAction extends BaseAction
{
    private CustomExpressionBO customExpressionBO;
    private CustomExpressionCO customExpressionCO = new CustomExpressionCO();
    private CustomExpressionSC customExpressionSC = new CustomExpressionSC();

    /**
     * Validate Expression by shortname
     * 
     * @return
     * @throws BaseException
     */
    public String validateCustomExpressionByShortName()
    {
	try
	{
	    SessionCO sessionCO = returnSessionObject();
	    customExpressionSC.setAppName(sessionCO.getCurrentAppName());
	    customExpressionBO.validateCustomExpressionByShortName(customExpressionSC);
	}
	catch(Exception e)
	{
	    customExpressionSC.setShortName("");
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    /**
     * return AutoComplete list
     * 
     * @return
     * @throws Exception
     */
    private void returnAutoCompleteData() throws Exception
    {
	StringBuffer autoCompleteList = new StringBuffer("");
	SessionCO sessionCO = returnSessionObject();

	SelectSC selSC = new SelectSC(CustomExpressionConstant.CONDITION_OPERATOR_LOV);
	selSC.setLanguage(sessionCO.getLanguage());

	ArrayList<SelectCO> operatorsList = (ArrayList<SelectCO>) returnLOV(selSC);

	for(int i = 0; i < operatorsList.size(); i++)
	{
	    autoCompleteList.append(operatorsList.get(i).getDescValue() + ";");
	}

	selSC = new SelectSC(CustomExpressionConstant.CONDITION_PB_FUNCTIONS);
	selSC.setLanguage(sessionCO.getLanguage());

	operatorsList = (ArrayList<SelectCO>) returnLOV(selSC);
	for(int i = 0; i < operatorsList.size(); i++)
	{
	    autoCompleteList.append(operatorsList.get(i).getDescValue() + ";");
	}
	
	
	/**
	  * Added sqlResult Menually 
	  * Later on it will move to db level and add the script
	*/
	autoCompleteList.append("sqlResult(stringSqlSyntax)" + ";");
	    
	customExpressionSC.setAppName(sessionCO.getCurrentAppName());
	// return Global Expression from Gateway
	this.customExpressionCO = customExpressionBO.returnCustomExpressionList(customExpressionSC, autoCompleteList);
    }

    /**
     * load Dialog Page
     */
    public String returnDialogPage()
    {
	try
	{
	    BigDecimal interfaceId = customExpressionCO.getInterfaceId();
	    String gridUrl = customExpressionCO.getGridUrl();
	    String expression = URLDecoderUtil.decode(StringUtil.nullToEmpty(customExpressionCO.getExpression()),"UTF8");
	    String status = customExpressionCO.getStatus();
	    String selectedRowFields = customExpressionCO.getResponseSelectedRowFields();
	    if(null != selectedRowFields)
	    {
	    	if(gridUrl.contains("?"))
	    	{
	    		gridUrl = gridUrl + "&criteria.responseSelectedRowFields="+selectedRowFields;
	    	}
	    	else{
	    		gridUrl = gridUrl + "?criteria.responseSelectedRowFields="+selectedRowFields;
	    	}
	    }
	    // return AutonComple list
	    returnAutoCompleteData();
	    customExpressionCO = customExpressionBO.applyVisiblityOnCustomExpressionFields(customExpressionCO);
	    customExpressionCO.setInterfaceId(interfaceId);
	    customExpressionCO.setGridUrl(gridUrl);
	    customExpressionCO.getCustom_EXPRESSIONVO().setEXPRESSION(expression);
	    setAdditionalScreenParams(customExpressionCO.getElementMap());
	    
	    if((StringUtil.isNotEmpty(status)) && 
		    ((status.equals(ConstantsCommon.STATUS_APPROVED) 
			    && !getIv_crud().equals(CustomExpressionConstant.IV_CRUD_UPDATE_AFTER_APPROVE))
			    ||  status.equals(ConstantsCommon.CRUD_SUSPEND)
			    ||  status.equals(ConstantsCommon.USR_DELETED_STS)
			    ||  getIv_crud().equals(ConstantsCommon.STATUS_APPROVED)))
	    {
		set_recReadOnly(ConstantsCommon.TRUE);
	    }
	    else
	    {
		set_recReadOnly(ConstantsCommon.FALSE);
	    }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return "formatExpressionDialog";
    }
    
    
    /**
     * Save Custom Expression
     * 
     * @return
     */
    public String save()
    {
	try
	{
	    SessionCO sessionCO = returnSessionObject();
	    
		customExpressionCO.getCustom_EXPRESSIONVO().setAPP_NAME(sessionCO.getCurrentAppName());
		customExpressionCO.getCustom_EXPRESSIONVO().setCREATED_BY(sessionCO.getUserName());
		customExpressionCO.getCustom_EXPRESSIONVO().setCREATED_DATE(returnCommonLibBO().addSystemTimeToDate(sessionCO.getRunningDateRET()));
			
		//parse Expression Dialof Iso Fields
		if(StringUtil.isNotEmpty(customExpressionCO.getExpressionIsofields()))
		{
		    GridUpdates gu = getGridUpdates(customExpressionCO.getExpressionIsofields(), ExpressionParamCO.class, true);
		    customExpressionCO.setAllGridList(gu.getLstAllRec());
		}
		    
		//save Custom Expression
		customExpressionBO.saveCustomExpression(customExpressionCO);
	   // }
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}
	return SUCCESS;
    }
    /**
     * apply Visiblity on Custon Expression Fields
     * @return
     */
    public String applyVisiblityOnCustomExpressionFields()
    {
	try
	{
	  customExpressionCO = customExpressionBO.applyVisiblityOnCustomExpressionFields(customExpressionCO);
	    setAdditionalScreenParams(customExpressionCO.getElementMap());
	    
	    return SUCCESS;
	}
	catch(BaseException e)
	{
	    handleException(e, null, null);
	}
	return ERROR;
    }
    
    
    public void setCustomExpressionBO(CustomExpressionBO customExpressionBO)
    {
	this.customExpressionBO = customExpressionBO;
    }

    public CustomExpressionSC getCustomExpressionSC()
    {
	return customExpressionSC;
    }

    public void setCustomExpressionSC(CustomExpressionSC customExpressionSC)
    {
	this.customExpressionSC = customExpressionSC;
    }

    public CustomExpressionCO getCustomExpressionCO()
    {
	return customExpressionCO;
    }

    public void setCustomExpressionCO(CustomExpressionCO customExpressionCO)
    {
	this.customExpressionCO = customExpressionCO;
    }
}
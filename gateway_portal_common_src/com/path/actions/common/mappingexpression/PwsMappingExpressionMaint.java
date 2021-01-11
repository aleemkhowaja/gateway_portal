/**
 * PwsMappingExpressionMaint.java - Jul 10, 2019  
 *
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * @author: Raed Saad
 *
 *USER STORY #862852 Adding Expressions to Mapping parameters in PWS dialog
 */
package com.path.actions.common.mappingexpression;

import java.math.BigDecimal;
import java.util.List;

import com.path.struts2.lib.common.base.BaseAction;
import com.path.vo.common.SessionCO;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.select.SelectSC;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;

public class PwsMappingExpressionMaint extends BaseAction {

	private String update;
	private List<SelectCO> expressionOptionList;
	private String fieldName;
	private WebServiceExplorerCO webServiceExplorerCO;
	
	/**
	 * @description function to load list of expressions from database
	 * @return
	 */
	public String expressionShowHideData()
	{
		try{
		    StringBuffer expressionStr = new StringBuffer("");
		    StringBuffer finalStr = new StringBuffer("");
		    
		    SessionCO sessionCO = returnSessionObject();
		    
		    // fill ExpressionType Drop Down
		    SelectSC expressionTypeSC = new SelectSC(new BigDecimal(502));
		    expressionTypeSC.setLanguage(sessionCO.getLanguage());
		    expressionTypeSC.setOrderCriteria("ORDER");
		    expressionTypeSC.setLovCodesExclude("'first','last','avg','sum','max','min','like','comulativesum','distinct','all','for'");
		    expressionOptionList = returnLOV(expressionTypeSC);
		    for(int i = 0; i < expressionOptionList.size(); i++)
		    {
			expressionStr.append(expressionOptionList.get(i).getDescValue().replaceAll(",", ";") + ",");
		    }
		    finalStr.append(expressionStr);		    
		    this.setUpdate(finalStr.toString());		    
		}
		catch(Exception ex)
		{
		    handleException(ex, null, null);
		}
		return "success";
	}

	public void callFunction()
	{
		
	}
	
	public String loadPwsExpressionDialog()
	{
		return "loadPwsMappingExpressionDialog";
	}
	
	public List<SelectCO> getExpressionOptionList() {
		return expressionOptionList;
	}

	public void setExpressionOptionList(List<SelectCO> expressionOptionList) {
		this.expressionOptionList = expressionOptionList;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}

	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}

}

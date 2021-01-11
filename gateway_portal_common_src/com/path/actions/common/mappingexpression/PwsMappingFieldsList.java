/**
 * PwsMappingFieldsList.java - Jul 16, 2019  
 *
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * @author: Raed Saad
 *
 */
package com.path.actions.common.mappingexpression;

import java.util.ArrayList;
import java.util.List;

import com.path.bo.common.pwsmapping.CommonPwsMappingConstant;
import com.path.lib.common.exception.BaseException;
import com.path.struts2.lib.common.base.GridBaseAction;
import com.path.vo.common.mappingexpression.PwsMappingExpressionCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class PwsMappingFieldsList extends GridBaseAction{
	private String fieldName;
	private String mappingFields;
	private List<PwsMappingExpressionCO> lstPwsMappingExpressionCO;
	private WebServiceExplorerCO webServiceExplorerCO;
//	private AtmInterfaceCO atmInterfaceCO;
//	private List<AtmInterfaceCO> lstAtmInterfaceCO;


	public String loadMappingFields() throws BaseException
	{
		try{
			PwsMappingExpressionCO pwsMappingExpressionCO = null;
			/*List<String> constantMappingFields = CommonPwsMappingConstant.MAPPING_FIELDS;
			List<PwsMappingExpressionCO> orgiLstPwsMappingExpressionCO = new ArrayList<PwsMappingExpressionCO>();
			lstPwsMappingExpressionCO = new ArrayList<PwsMappingExpressionCO>();
			for(String str : constantMappingFields)
			{
				pwsMappingExpressionCO = new PwsMappingExpressionCO();
				pwsMappingExpressionCO.setMappingGridFields(str);
				orgiLstPwsMappingExpressionCO.add(pwsMappingExpressionCO);
			}*/
			String mappingFields = null;
			Object obj = null;
			if(null != webServiceExplorerCO && null != webServiceExplorerCO.getMappingFields())
			{
				mappingFields = webServiceExplorerCO.getMappingFields();
				obj = JSONSerializer.toJSON(mappingFields);
			}
			JSONObject jsonObject = null;
			List<JSONObject> jsonLst = null;
			if( null != obj && obj instanceof JSONObject)
			{
				jsonObject = (JSONObject) JSONSerializer.toJSON(mappingFields);
				jsonLst = new ArrayList<JSONObject>();
				jsonLst.add(jsonObject);
			}
			else{
				jsonLst = (List<JSONObject>) JSONSerializer.toJSON(mappingFields);
			}

			String paramVal = null;
			lstPwsMappingExpressionCO = new ArrayList<>();
			for (JSONObject jsonObj : jsonLst)
			{
				pwsMappingExpressionCO = new PwsMappingExpressionCO();
				if(jsonObj.get("fieldName") != null)
				{
					paramVal = jsonObj.get("fieldName").toString();
					pwsMappingExpressionCO.setParameterName(paramVal);
					pwsMappingExpressionCO.setParameterValue(paramVal);
					lstPwsMappingExpressionCO.add(pwsMappingExpressionCO);
				}
			}
		
			super.setGridModel(lstPwsMappingExpressionCO);
		}
		catch(Exception ex)
		{
		    handleException(ex, null, null);
		}
		return "success";		
	}
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public List<PwsMappingExpressionCO> getLstPwsMappingExpressionCO() {
		return lstPwsMappingExpressionCO;
	}
	public void setLstPwsMappingExpressionCO(List<PwsMappingExpressionCO> lstPwsMappingExpressionCO) {
		this.lstPwsMappingExpressionCO = lstPwsMappingExpressionCO;
	}
	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}
	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}
	public String getMappingFields() {
		return mappingFields;
	}
	public void setMappingFields(String mappingFields) {
		this.mappingFields = mappingFields;
	}
}

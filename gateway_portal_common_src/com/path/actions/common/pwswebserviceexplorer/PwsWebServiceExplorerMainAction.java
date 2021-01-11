/**
 * PwsWebServiceExplorerMainAction.java - Mar 20, 2019  
 *
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * USER STORY #799705 Control record - PWS mapping screen
 * @author: Raed Saad
 *
 */
package com.path.actions.common.pwswebserviceexplorer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.struts2.util.URLDecoderUtil;

import com.path.bo.common.pwsmapping.CommonPwsMappingBO;
import com.path.bo.common.pwsmapping.CommonPwsMappingConstant;
import com.path.dbmaps.vo.COMMON_PWS_MAPPINGVO;
import com.path.dbmaps.vo.COMMON_PWS_MAPPING_DEFVO;
import com.path.dbmaps.vo.COMMON_PWS_MAPPING_DETAILSVO;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.BaseAction;
import com.path.vo.common.SessionCO;
import com.path.vo.common.pwsmapping.PwsDefinitionCO;
import com.path.vo.common.pwswebserviceexplorer.PwsWebServiceExplorerCO;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.webserviceexplorer.RequestResponseCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceUtil;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class PwsWebServiceExplorerMainAction extends BaseAction{
	
	private WebServiceExplorerCO webServiceExplorerCO;
	private CommonPwsMappingBO commonPwsMappingBO;
	private List<SelectCO> mappingValues;
	private PwsDefinitionCO pwsDefinitionCO;
	
	/**
	 * 
	 * @return
	 */
	public String save() 
	{
		try{
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			SessionCO sessionCO = super.returnSessionObject();
			Map<String,String> appName = webServiceUtil.getApplicationNameAndKey("PathWebServicesList");

			BigDecimal mappingId = webServiceExplorerCO.getMappingID();
			webServiceExplorerCO = webServiceUtil.returnGridDataToSave(webServiceExplorerCO);
			List<RequestResponseCO> lstReq = webServiceExplorerCO.getLstRequestResposneCO();
			PwsWebServiceExplorerCO pwsWebServiceExplorerCO = new PwsWebServiceExplorerCO();
			pwsWebServiceExplorerCO.setLstReq(lstReq);
			pwsWebServiceExplorerCO.setMode("IN");
			pwsWebServiceExplorerCO = this.processGridData(pwsWebServiceExplorerCO);
			
			List<COMMON_PWS_MAPPING_DETAILSVO> reqLstCommonPwsMappingDetailsVO = pwsWebServiceExplorerCO.getLstCommonPwsMappingVO();
			String webServiceExplorerGridUpdates = webServiceExplorerCO.getWebServiceExplorerGridUpdates();			
			String webServiceExplorerResponseGridUpdates = webServiceExplorerCO.getWebServiceExplorerResponseGridUpdates();
			webServiceExplorerCO.setWebServiceExplorerGridUpdates(webServiceExplorerResponseGridUpdates);
			webServiceExplorerCO = webServiceUtil.returnGridDataToSave(webServiceExplorerCO);
			List<RequestResponseCO> lstResp = webServiceExplorerCO.getLstRequestResposneCO();
			pwsWebServiceExplorerCO = new PwsWebServiceExplorerCO();
			pwsWebServiceExplorerCO.setLstReq(lstResp);
			pwsWebServiceExplorerCO.setMode("OUT");
			pwsWebServiceExplorerCO = this.processGridData(pwsWebServiceExplorerCO);

			List<COMMON_PWS_MAPPING_DETAILSVO> respLstCommonPwsMappingDetailsVO = pwsWebServiceExplorerCO.getLstCommonPwsMappingVO();
			webServiceExplorerCO.setWebServiceExplorerGridUpdates(webServiceExplorerGridUpdates);
			
			COMMON_PWS_MAPPINGVO commonPwsMappingVO = new COMMON_PWS_MAPPINGVO();
			commonPwsMappingVO.setWS_NAME(webServiceExplorerCO.getWebServiceName());
			commonPwsMappingVO.setOPER_NAME(webServiceExplorerCO.getOperationName());
			String serviceAppPropName = appName.get(webServiceExplorerCO.getApplicationName());
			if(!StringUtil.nullToEmpty(serviceAppPropName).isEmpty())
			{
				serviceAppPropName = serviceAppPropName.replace("app.", "").replace(".pws", "").replaceAll(".name", ""); 
			}
			pwsWebServiceExplorerCO.setCommonPwsMappingVO(commonPwsMappingVO);
			List<COMMON_PWS_MAPPING_DETAILSVO> combinedReqResp = new ArrayList<>();
			combinedReqResp.addAll(reqLstCommonPwsMappingDetailsVO);			
			combinedReqResp.addAll(respLstCommonPwsMappingDetailsVO);
			pwsWebServiceExplorerCO.setLstCommonPwsMappingDetailsVO(combinedReqResp);
			pwsWebServiceExplorerCO.getCommonPwsMappingVO().setMAPPING_ID(mappingId);
			pwsWebServiceExplorerCO.getCommonPwsMappingVO().setSERVICE_APP_NAME(serviceAppPropName);
			//pwsWebServiceExplorerCO.setRunningDate(sessionCO.getRunningDateRET());
			pwsWebServiceExplorerCO.setUserName(sessionCO.getUserName());
			pwsWebServiceExplorerCO.setCommonPwsMappingDefVO(new COMMON_PWS_MAPPING_DEFVO());
			pwsWebServiceExplorerCO.getCommonPwsMappingDefVO().setAPP_NAME(sessionCO.getCurrentAppName());
			pwsWebServiceExplorerCO.getCommonPwsMappingDefVO().setMAP_DESCRIPTION(webServiceExplorerCO.getDescription());
			mappingId = (BigDecimal)commonPwsMappingBO.saveCommonPWSMappingDetails(pwsWebServiceExplorerCO);
			if(null != mappingId)
			{
				pwsDefinitionCO = new PwsDefinitionCO();
				COMMON_PWS_MAPPING_DEFVO cpmd = new COMMON_PWS_MAPPING_DEFVO();
				cpmd.setMAPPING_ID(mappingId);
				pwsDefinitionCO.setDefinitionVO(cpmd);
				this.setPwsDefinitionCO(pwsDefinitionCO);
			}
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "success";
	}
	
	public String returnFieldName(Stack stack)
	{
		return stack.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", ".");
	}
	
	public String returnMappingName(Stack stack)
	{
		String x = stack.toString().replace("[[", "{").replace("]]", "}");
		x = x.replace(" ", "").replace(",", ".");
		x = x.replace("].[", "}.{");
		x = x.replace("[", "").replace("]", "");
		x = x.replace("{", "[").replace("}", "]");
		return x;
	}

	/**
	 * @description function to process the pws mapping grid data and fill it in a common pws mapping vo list
	 * @param pwsWebServiceExplorerCO
	 * @return pwsWebServiceExplorerCO
	 */
	public PwsWebServiceExplorerCO processGridData(PwsWebServiceExplorerCO pwsWebServiceExplorerCO)
	{
		COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = null;
		List<RequestResponseCO> lstReq = pwsWebServiceExplorerCO.getLstReq();
		String mode = pwsWebServiceExplorerCO.getMode();
		Stack<String> parentField = null;
		Stack<String> parentMappingField = null;
		Stack<String> fieldType = new Stack<String>();
		List<RequestResponseCO> lstGridRow = null;
		List<RequestResponseCO> subGridRow = null;
		String mappingArgType = pwsWebServiceExplorerCO.getNextType();
		for(RequestResponseCO reqRespCO : lstReq)
		{
			parentField = new Stack<String>();
			commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
			commonPwsMappingDetailsVO.setWS_NAME(webServiceExplorerCO.getWebServiceName());
			commonPwsMappingDetailsVO.setOPER_NAME(webServiceExplorerCO.getOperationName());
			commonPwsMappingDetailsVO.setMAPPING_ARG_MODE(mode);
			commonPwsMappingDetailsVO.setPARAM_NAME(reqRespCO.getFieldName());
			parentField = pwsWebServiceExplorerCO.getParentFields();
			parentMappingField = pwsWebServiceExplorerCO.getParentMappingFields();
			parentField.add(reqRespCO.getFieldName());
			// case request
			if(mode.equalsIgnoreCase("IN"))
			{
				if(null != reqRespCO.getMappingField())
				{
					// only mapping
					parentMappingField.add(reqRespCO.getMappingField());
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnMappingName(parentMappingField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE("IS_MAPPING_EXPRESSION");
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
					parentMappingField.pop();
				}
				else if(null != reqRespCO.getExpressionHiddenField())
				{
					// only expression
					commonPwsMappingDetailsVO.setSOURCE_KEY(URLDecoderUtil.decode(reqRespCO.getExpressionHiddenField(), "UTF-8"));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else if(null != reqRespCO.getValue() && reqRespCO.getValue().length()>0)
				{
					// default value
					commonPwsMappingDetailsVO.setSOURCE_KEY(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else if(pwsWebServiceExplorerCO.isMappedParent())
				{
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnMappingName(parentMappingField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(null);
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else{
					// selected field without mapping nor expression
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(null);
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
			}
			else{
				//case response
				if((null != reqRespCO.getMappingField()) && null != reqRespCO.getExpressionHiddenField())
				{
					//MAPPING + EXPRESSION
					parentMappingField.add(reqRespCO.getMappingField());
					commonPwsMappingDetailsVO.setSOURCE_KEY(URLDecoderUtil.decode(reqRespCO.getExpressionHiddenField(), "UTF-8"));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnMappingName(parentMappingField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
					parentMappingField.pop();
				}
				else if((null != reqRespCO.getMappingField()) && null == reqRespCO.getExpressionHiddenField())
				{
					//MAPPING ONLY
					parentMappingField.add(reqRespCO.getMappingField());
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnMappingName(parentMappingField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE("IS_MAPPING_EXPRESSION");
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
					parentMappingField.pop();
				}
				else if((null == reqRespCO.getMappingField()) && null != reqRespCO.getExpressionHiddenField())
				{
					//DEFAULT VALUE
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setSOURCE_KEY(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else if((null == reqRespCO.getMappingField()) && null != reqRespCO.getExpressionHiddenField())
				{
					//EXPRESSION ONLY
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setSOURCE_KEY(URLDecoderUtil.decode(reqRespCO.getExpressionHiddenField(), "UTF-8"));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(reqRespCO.getValue());
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else if(pwsWebServiceExplorerCO.isMappedParent())
				{
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(null);
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
				else{
					// selected field without mapping nor expression
					commonPwsMappingDetailsVO.setSOURCE_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setDESTINATION_KEY(this.returnFieldName(parentField));
					commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(null);
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE(mappingArgType);
					commonPwsMappingDetailsVO.setPARAM_NAME(this.returnFieldName(parentField));
				}
			}
			if("IS_LIST_OB".equals(pwsWebServiceExplorerCO.getNextType()))
			{
				parentField.pop();
				commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE("IS_LIST_OB");
				commonPwsMappingDetailsVO.setPARAM_NAME(reqRespCO.getFieldType());
				pwsWebServiceExplorerCO.appendToCommonPwsMappingList(commonPwsMappingDetailsVO);
				//get(0) since in list sub grid we do the mapping only once
				subGridRow = reqRespCO.getReqResCO().getLstInReqRes();
				pwsWebServiceExplorerCO.setLstReq(subGridRow);
				pwsWebServiceExplorerCO.setNextType("IS_LIST_FI");
				if(null != reqRespCO.getMappingField())
				{
					//in case the parent List is mapped
					pwsWebServiceExplorerCO.pushParentMappingFields(reqRespCO.getMappingField());
					this.processGridData(pwsWebServiceExplorerCO);
					pwsWebServiceExplorerCO.popParentMappingFields();
					pwsWebServiceExplorerCO.popParentField();
				}
				else{
					// in case the parent List is not mapped
					this.processGridData(pwsWebServiceExplorerCO);
					pwsWebServiceExplorerCO.popParentField();
				}
				pwsWebServiceExplorerCO.setNextType(null);
			}
			else 
				if(!isNonPrimativeDataType(reqRespCO.getFieldType()) && !this.isList(reqRespCO.getFieldType()))
			{
				//case When the grid row is of type object
				parentField.pop();
				if("IS_LIST_FI".equals(pwsWebServiceExplorerCO.getNextType()))
				{
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE("IS_LIST_FI");				
				}
				else{
					commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE("IS_WRAPPER");				
				}
				pwsWebServiceExplorerCO.appendToCommonPwsMappingList(commonPwsMappingDetailsVO);
				pwsWebServiceExplorerCO.pushParentField(reqRespCO.getFieldName());
				subGridRow = reqRespCO.getReqResCO().getLstInReqRes();
				pwsWebServiceExplorerCO.setLstReq(subGridRow);
				// getting the list reqRespCO that has the data in it
				if(null != reqRespCO.getMappingField())
				{
					//in case the parent object is mapped
					// to be checked
					parentField.pop();
					pwsWebServiceExplorerCO.setMappedParent(true);
					pwsWebServiceExplorerCO.pushParentField(reqRespCO.getFieldName());
					pwsWebServiceExplorerCO.pushParentMappingFields(reqRespCO.getMappingField());
					this.processGridData(pwsWebServiceExplorerCO);
					pwsWebServiceExplorerCO.setMappedParent(false);
					pwsWebServiceExplorerCO.popParentMappingFields();
					pwsWebServiceExplorerCO.popParentField();
				}
				else{
					// in case the parent object is not mapped
					this.processGridData(pwsWebServiceExplorerCO);
					pwsWebServiceExplorerCO.popParentField();
				}
				pwsWebServiceExplorerCO.setNextType(null);
			}
			else if(this.isList(reqRespCO.getFieldType()))
			{
				// Case when the grid row is of type list
				parentField.pop();
				commonPwsMappingDetailsVO.setMAPPING_ARG_TYPE("IS_LIST");
				pwsWebServiceExplorerCO.setNextType("IS_LIST_OB");
				pwsWebServiceExplorerCO.appendToCommonPwsMappingList(commonPwsMappingDetailsVO);
				lstGridRow = reqRespCO.getReqResCO().getLstInReqRes();
				if(lstGridRow.size()>0)
				{
					pwsWebServiceExplorerCO.pushParentField(reqRespCO.getFieldName());
				}
				//update reqRespCO with the object containing the data
				pwsWebServiceExplorerCO.setLstReq(lstGridRow);
				if(null != reqRespCO.getMappingField())
				{
					//in case the parent List is mapped
					pwsWebServiceExplorerCO.pushParentMappingFields(reqRespCO.getMappingField());
					this.processGridData(pwsWebServiceExplorerCO);
					pwsWebServiceExplorerCO.popParentMappingFields();
				}
				else{
					// in case the parent List is not mapped
					this.processGridData(pwsWebServiceExplorerCO);
				}
				pwsWebServiceExplorerCO.setNextType(null);
			}
			else{
				pwsWebServiceExplorerCO.appendToCommonPwsMappingList(commonPwsMappingDetailsVO);
				parentField.pop();
			}
		}
		return pwsWebServiceExplorerCO;		
	}
	
	/**
	 * @description
	 */
	public void processExpressionMapping(List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPwsMappingDetailsVO)
	{
		lstCommonPwsMappingDetailsVO = new ArrayList<COMMON_PWS_MAPPING_DETAILSVO>();
		Map<String,Object> orgiMap = new HashMap<String,Object>();
		List<Map<String, Object>> aList = new ArrayList<>();
		try{
			for(COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO : lstCommonPwsMappingDetailsVO)	
			{
				if("in".equalsIgnoreCase(commonPwsMappingDetailsVO.getMAPPING_ARG_MODE()))
				{
					orgiMap.put(commonPwsMappingDetailsVO.getSOURCE_KEY(), commonPwsMappingDetailsVO.getDESTINATION_KEY());
					aList.add(orgiMap);
				}
				else{
					orgiMap.put(commonPwsMappingDetailsVO.getDESTINATION_KEY(),commonPwsMappingDetailsVO.getSOURCE_KEY());
					aList.add(orgiMap);
				}
			}
			this.filterExpressionMap(aList);
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
	}
	
	public Map filterExpressionMap(List<Map<String, Object>> aList)
	{			
		String key = null;
    	Object value = null;
		Map orgiMap = new HashMap<String,Object>();
		Map newMap = new HashMap<String,Object>();
		try{
			aList.add(orgiMap);
			for(Map map : aList)
			{
				for(Object k : orgiMap.keySet())
				{
					key = (String) orgiMap.get(k);
					//value = commonLibBO.executeExpression(k.toString(), 0, aList);
					newMap.put(key, value);
				}
			}
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return newMap;
	}
	
	public String fillMappingFields()
	{
		try{
			if(null != webServiceExplorerCO && !StringUtil.nullToEmpty(webServiceExplorerCO.getMappingFields()).isEmpty())
			{ 
//				List<String> constantMappingFields = CommonPwsMappingConstant.MAPPING_FIELDS;
				String mappingFields = webServiceExplorerCO.getMappingFields();
				Object obj = JSONSerializer.toJSON(mappingFields);
				List<JSONObject> jsonLst = null;
				JSONObject jsonObject = null;
				List<SelectCO> mappingValuess = new ArrayList<SelectCO>();
				SelectCO selectCO = new SelectCO();
				selectCO.setCode("");
				selectCO.setDescValue("");
				mappingValuess.add(selectCO);
				if( obj instanceof JSONObject)
				{
					jsonObject = (JSONObject) JSONSerializer.toJSON(mappingFields);
					jsonLst = new ArrayList<JSONObject>();
					jsonLst.add(jsonObject);
				}
				else{
					jsonLst = (List<JSONObject>) JSONSerializer.toJSON(mappingFields);
				}
				for(JSONObject j : jsonLst)
				{
					for(Object keyObj : j.keySet())
					{
						selectCO = new SelectCO();
						selectCO.setCode(j.get("parameterValue").toString());
						selectCO.setDescValue(j.get("parameterName").toString());
						mappingValuess.add(selectCO);
					}
				}
				this.setMappingValues(mappingValuess);
			}
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "success";
	}

	public String fillMappingFieldsFromDb()
	{
		try{
			if(null != webServiceExplorerCO && null != webServiceExplorerCO.getMappingID())
			{ 
				COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
				commonPwsMappingDetailsVO.setMAPPING_ID(webServiceExplorerCO.getMappingID());
			
				List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPwsMappingDetails = commonPwsMappingBO.loadCommonPWSMappingDetails(commonPwsMappingDetailsVO);
				SelectCO selectCO = new SelectCO();
				List<SelectCO> mappingValues = new ArrayList<SelectCO>();
				selectCO.setCode("");
				selectCO.setDescValue("");
				List<String> constantMappingFields = CommonPwsMappingConstant.MAPPING_FIELDS;
				for(String field : constantMappingFields)
				{
					mappingValues.add(selectCO);
					selectCO = new SelectCO();
					selectCO.setCode(field);
					selectCO.setDescValue(field);
				}
				for(COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO1 : lstCommonPwsMappingDetails)
				{
					if(null != commonPwsMappingDetailsVO1.getDESTINATION_KEY())
					{
						mappingValues.add(selectCO);
						selectCO = new SelectCO();
						selectCO.setCode(commonPwsMappingDetailsVO1.getDESTINATION_KEY());
						selectCO.setDescValue(commonPwsMappingDetailsVO1.getDESTINATION_KEY());
					}
					else if(null != commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE())
					{
						mappingValues.add(selectCO);
						selectCO = new SelectCO();
						selectCO.setCode(commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE());
						selectCO.setDescValue(commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE());
					}
				}
				mappingValues.add(selectCO);
				this.setMappingValues(mappingValues);
			}
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "success";
	}

	private String isWrapperClass(String fieldType)
	{
		if(!isNonPrimativeDataTypeOrListORHash(fieldType))
		{
			return "IS_WRAPPER";
		}
		return null;
	}
	
	private String mappingArgType(Object obj)
	{
		if(obj instanceof Stack)
		{
			Stack<String> x = (Stack)obj;
			String y = x.toString();
			if(y.contains("IS_LIST"))
			{
				return "IS_LIST_FI";
			}
		}
		return null;
	}
	
	public String returnMappingArgType(RequestResponseCO reqRespCO,String mappingArgType)
	{
		if( null != this.mappingArgType(reqRespCO.getStackTrace()) && "IS_LIST_FI".equals(this.mappingArgType(reqRespCO.getStackTrace())))
		{
			return this.mappingArgType(reqRespCO.getStackTrace());
		}
		else if(null != this.isWrapperClass(reqRespCO.getFieldType()))
		{
			return this.isWrapperClass(reqRespCO.getFieldType());
		}
		else{
			return mappingArgType;
		}
	}
	
	private Boolean isNonPrimativeDataType(String fieldType)
	{
		return ("int".equals(fieldType) || "string".equals(fieldType) || "decimal".equals(fieldType) || "bigdecimal".equals(fieldType) || "dateTime".equals(fieldType) || "long".equals(fieldType) || "date".equals(fieldType) || "float".equals(fieldType) || "double".equals(fieldType) || "boolean".equals(fieldType));
	}
	
	
	private Boolean isNonPrimativeDataTypeOrListORHash(String fieldType)
	{
		return (this.isNonPrimativeDataType(fieldType) || fieldType.contains("List<") || fieldType.contains("HashMap<"));
	}
	
	private Boolean isList(String fieldType)
	{
		return fieldType.contains("List<");
	}

	private Boolean isHashMap(String fieldType)
	{
		return fieldType.contains("HashMap<");
	}
	
	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}

	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}

	public void setCommonPwsMappingBO(CommonPwsMappingBO commonPwsMappingBO) {
		this.commonPwsMappingBO = commonPwsMappingBO;
	}

	public List<SelectCO> getMappingValues() {
		return mappingValues;
	}

	public void setMappingValues(List<SelectCO> mappingValues) {
		this.mappingValues = mappingValues;
	}

	public PwsDefinitionCO getPwsDefinitionCO() {
		return pwsDefinitionCO;
	}

	public void setPwsDefinitionCO(PwsDefinitionCO pwsDefinitionCO) {
		this.pwsDefinitionCO = pwsDefinitionCO;
	}
}

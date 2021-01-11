/**
 * PwsWebserviceExplorerList.java - Feb 27, 2019  
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.path.actions.common.webserviceexplorer.WebServiceExplorerGridBaseList;
import com.path.actions.common.webserviceexplorer.WebServiceExplorerList;
import com.path.bo.common.pwsmapping.CommonPwsMappingBO;
import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.bo.common.webserviceexplorer.WebServiceExplorerConstant;
import com.path.dbmaps.vo.COMMON_PWS_MAPPING_DETAILSVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.vo.common.pwswebserviceexplorer.PwsWebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.RequestResponseCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerGridParamCO;

public class PwsWebserviceExplorerList extends WebServiceExplorerGridBaseList {
	
	  private Boolean isEditable;
	  private CommonPwsMappingBO commonPwsMappingBO;
	  private String requestGridId;
	  private String responseGridId;
	  private WebServiceExplorerGridParamCO webServiceExplorerGridParamCO;

	    /**
	     * @Description load main Grid
	     * @note incase we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
		  *@Default behaviour loads request
	     */
	    @Override
		public String loadWSDLInfoGrid() throws BaseException {
			String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
			BigDecimal serviceID = null;
			BigDecimal mappingId = null;
			String webServiceName = null;// do not remove
			String operationName = null;
			try{
				if(null != webServiceExplorerCO)
				{
					List<String> excludedParams = new ArrayList<String>();
					excludedParams.add("requestContextVO");
					webServiceExplorerCO.setExcludedParams(excludedParams);
					super.setGridCaption(webServiceExplorerCO.getOperationName());
					//webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
					webServiceExplorerCO.getWebServiceExplorerConfigVO();
					webServiceName = webServiceExplorerCO.getWebServiceName();//do not remove webService name param will change to original value after parsing the wsdl
					operationName = webServiceExplorerCO.getOperationName();
					mappingId = webServiceExplorerCO.getMappingID();
					List<RequestResponseCO> lstReqResCO = super.returnWsdlParseData(webServiceExplorerCO);
					webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
					
					List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPWSDetailsVO = new ArrayList<COMMON_PWS_MAPPING_DETAILSVO>();
					
					COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
					commonPwsMappingDetailsVO.setMAPPING_ID(mappingId);
					commonPwsMappingDetailsVO.setWS_NAME(webServiceName);
					commonPwsMappingDetailsVO.setOPER_NAME(operationName);
					if(webServiceExplorerCO.getRequestType().equalsIgnoreCase("request"))
					{
						commonPwsMappingDetailsVO.setMAPPING_ARG_MODE("IN");
					}
					else{
						commonPwsMappingDetailsVO.setMAPPING_ARG_MODE("OUT");
					}
					if(null != webServiceExplorerCO.getMappingID())
					{
						lstCommonPWSDetailsVO = commonPwsMappingBO.loadCommonPWSMappingDetails(commonPwsMappingDetailsVO);
						webServiceExplorerCO.setLstCommonPwsMappingDetailsVO(lstCommonPWSDetailsVO);
						webServiceExplorerCO = this.processWebServiceExplorerMappingData(webServiceExplorerCO);
					}
					super.setRecords(webServiceExplorerCO.getLstRequestResposneCO().size());
					super.setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
					//webServiceExplorerGridParamCO.mainGridActionRef
					this.getWebServiceExplorerGridParamCO();
				}
			}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return "success";
		}
		
	    /**
	     * @Description load sub Grid
	     * @note in case we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
		  *@Default behaviour loads request
	     */
		@Override
		public String loadSubGridInfo() throws BaseException {
			String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
			String[] gridIdSplitted;
			BigDecimal serviceID = null;
			String appName = null;
			String webServiceName = null;
			String operationName = null;
			try{
				if(null != webServiceExplorerCO && (null!= super.getId() || null != webServiceExplorerCO.getParentRowId()) && null != webServiceExplorerCO.getRequestResponseCO().getGridColumnID() && StringUtil.isNotEmpty(webServiceExplorerCO.getRequestResponseCO().getGridColumnID()))
				{
					appName = webServiceExplorerCO.getApplicationName();
					webServiceName = webServiceExplorerCO.getWebServiceName();
					operationName = webServiceExplorerCO.getOperationName();
					//webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
					webServiceExplorerCO = WebServiceExplorerList.extractDataFromGridRowID(webServiceExplorerCO);
					if(StringUtil.isNotEmpty(webServiceExplorerCO.getHasChildren()))
					{
						List<RequestResponseCO> lstReqResCO = super.returnWsdlParseData(webServiceExplorerCO);
						webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
					}	
					List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPWSDetailsVO = new ArrayList<COMMON_PWS_MAPPING_DETAILSVO>();
					
					COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
					commonPwsMappingDetailsVO.setMAPPING_ID(webServiceExplorerCO.getMappingID());
					commonPwsMappingDetailsVO.setWS_NAME(webServiceName);
					commonPwsMappingDetailsVO.setOPER_NAME(operationName);
					if(this.getWebServiceExplorerCO().getParentGridId().contains(super.get_pageRef()+"_response"))
					{
						commonPwsMappingDetailsVO.setMAPPING_ARG_MODE("OUT");
					}
					else
					{
						commonPwsMappingDetailsVO.setMAPPING_ARG_MODE("IN");
					}
					PwsWebServiceExplorerCO pwsWebServiceExplorerCO = new PwsWebServiceExplorerCO();
					pwsWebServiceExplorerCO.setCommonPwsMappingDetailsVO(commonPwsMappingDetailsVO);
					String filter = null;
					String paramNameFilter = null;
//					if(null != webServiceExplorerCO.getLoadConfig() && true == webServiceExplorerCO.getLoadConfig() && null != webServiceExplorerCO.getReqType() && "List".equals(webServiceExplorerCO.getReqType()))
//					{
//						filter = "IS_LIST_FI";
//						pwsWebServiceExplorerCO.setFilter(filter);
//					}
					if(null != webServiceExplorerCO.getSelectedFieldType() && webServiceExplorerCO.getSelectedFieldType().contains("List"))
					{
						paramNameFilter = webServiceExplorerCO.getParentRowId();
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef()+"_response_", ".");
						paramNameFilter = paramNameFilter.replace("_1_"+super.get_pageRef(), "");
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef(), ".");
						paramNameFilter = paramNameFilter.replace("_", ".");
						String [] x = paramNameFilter.split("\\.");
						String toReplace = x[x.length-1] +"." + x[x.length-2];						
						paramNameFilter = paramNameFilter.replace(toReplace, x[x.length-2]);
						paramNameFilter = paramNameFilter + ".%";
						pwsWebServiceExplorerCO.setParamNameFilter(paramNameFilter);
					}
					else if(null != webServiceExplorerCO.getParentRowId() && webServiceExplorerCO.getParentRowId().contains("_1_"+super.get_pageRef()))
					{
						paramNameFilter = webServiceExplorerCO.getParentRowId();
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef()+"_response_", ".");
						paramNameFilter = paramNameFilter.replace("_1_"+super.get_pageRef(), "");
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef(), ".");
						paramNameFilter = paramNameFilter.replace("_", ".");
						String [] x = paramNameFilter.split("\\.");
						Stack<String> stack = new Stack<String>();
						String toReplace = "";
						for(int i = 1; i<x.length; i++)
						{
							if(x[i-1].equals(x[i]))
							{
								stack.push(x[i]);
							}
						}
						for(int j = 0; j<=stack.size(); j++)
						{
							toReplace = stack.pop();
							paramNameFilter = paramNameFilter.replace(toReplace+"."+toReplace, toReplace);
						}
						paramNameFilter = paramNameFilter + "%";
						pwsWebServiceExplorerCO.setParamNameFilter(paramNameFilter);
					}
					else if(null != webServiceExplorerCO.getParentRowId())
					{
						paramNameFilter = webServiceExplorerCO.getParentRowId()+".%";
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef()+"_response", "");
						paramNameFilter = paramNameFilter.replace("_"+super.get_pageRef(), "");
						paramNameFilter = paramNameFilter.replace("_", ".");
						pwsWebServiceExplorerCO.setParamNameFilter(paramNameFilter);
					}
					if(null != webServiceExplorerCO.getMappingID())
					{
						lstCommonPWSDetailsVO = commonPwsMappingBO.loadCommonPWSMappingDetails(pwsWebServiceExplorerCO);
						webServiceExplorerCO.setLstCommonPwsMappingDetailsVO(lstCommonPWSDetailsVO);
						webServiceExplorerCO = this.processWebServiceExplorerMappingData(webServiceExplorerCO);
					}
					setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
				}
			}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return "success";
		}
		
		/**
		 * Overriding loadHashMapSubGridInfo and adding additional behavior to load data from database incase of stored data
		 */
		@Override
		public String loadHashMapSubGridInfo() throws BaseException
		{	webServiceExplorerCO.getParentRowId();
			try{
				if(null != webServiceExplorerCO && null != webServiceExplorerCO.getWebServiceExplorerConfigVO() && null != webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID() && !NumberUtil.isEmptyDecimal(webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID()))
				{
					webServiceExplorerCO = webServiceExplorerBO.loadWebServiceExplorerHashMapSavedData(webServiceExplorerCO);
				}
				else{
					List<RequestResponseCO> reqResLst = new ArrayList<RequestResponseCO>();
					RequestResponseCO resReqCO = new RequestResponseCO();		
					resReqCO.setFieldName("");
					resReqCO.setFieldType("map");				
					reqResLst.add(resReqCO);
					WebServiceExplorerCO  webServiceExplorerCO = new WebServiceExplorerCO();
					webServiceExplorerCO.setLstRequestResposneCO(reqResLst);
					setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
					return "success";
				}
				setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
				}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return "success";		
		}
		
		/**
		 * Overriding loadListSubGridInfo and adding additional behavior to load data from database in case of stored data
		 */
		@Override
		public String loadListSubGridInfo()
		{
			try{
				 if(null != webServiceExplorerCO.getParameterType() && webServiceExplorerCO.getParameterType().contains("List"))
				 {					 
					RequestResponseCO reqRespCO = webServiceExplorerCO.getRequestResponseCO();
					List<RequestResponseCO> lstReqRespCO = new ArrayList<RequestResponseCO>();
				//	webServiceExplorerCO.setMappingID(new BigDecimal(400044));// to be deleted
					String webServiceName = webServiceExplorerCO.getWebServiceName();
					String operationName = webServiceExplorerCO.getOperationName();
					String parentId = webServiceExplorerCO.getParentRowId();
					String id = reqRespCO.getID();
					webServiceExplorerCO.setPageRef(super.get_pageRef());	
					reqRespCO.setID(parentId+"_"+id+"_"+super.get_pageRef());
					List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPWSDetailsVO = new ArrayList<COMMON_PWS_MAPPING_DETAILSVO>();
					PwsWebServiceExplorerCO pwsWebServiceExplorerCO = new PwsWebServiceExplorerCO();
					COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
					commonPwsMappingDetailsVO.setMAPPING_ID(webServiceExplorerCO.getMappingID());
					commonPwsMappingDetailsVO.setWS_NAME(webServiceName);
					commonPwsMappingDetailsVO.setOPER_NAME(operationName);
					String filter = null;
					pwsWebServiceExplorerCO.setCommonPwsMappingDetailsVO(commonPwsMappingDetailsVO);
					lstReqRespCO.add(reqRespCO);
					if(null != webServiceExplorerCO.getLoadConfig() && webServiceExplorerCO.getLoadConfig())
					{
						reqRespCO.setLoadSubGrid("1");

						filter = webServiceExplorerCO.getFieldType();
						pwsWebServiceExplorerCO.setFilter(filter);
						if(null != webServiceExplorerCO.getMappingID())
						{
							lstCommonPWSDetailsVO = commonPwsMappingBO.loadCommonPWSMappingDetails(pwsWebServiceExplorerCO);
						}
						webServiceExplorerCO.setScreenName("alert");
						webServiceExplorerCO.setLstRequestResposneCO(lstReqRespCO);
						webServiceExplorerCO.setLstCommonPwsMappingDetailsVO(lstCommonPWSDetailsVO);
						webServiceExplorerCO = this.processWebServiceExplorerMappingData(webServiceExplorerCO);
					}
					else{
						webServiceExplorerCO.setRequestResponseCO(reqRespCO);
						webServiceExplorerCO.setLstRequestResposneCO(Arrays.asList(webServiceExplorerCO.getRequestResponseCO()));				
					}
				}
				else{
				// We can give the List grid rows an ID from here 
					RequestResponseCO reqRespCO = webServiceExplorerCO.getRequestResponseCO();
					String parentId = webServiceExplorerCO.getParentRowId();
					String id = reqRespCO.getID();
					webServiceExplorerCO.getRequestResponseCO().setID(parentId+"_"+id+"_"+super.get_pageRef());
					webServiceExplorerCO.setLstRequestResposneCO(Arrays.asList(webServiceExplorerCO.getRequestResponseCO()));					
				}
				setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
			}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return "success";
		}
		
		/**
		 * @description Overriding return Main Grid 
		 */
		public String loadMainGridFn()
		{
			// USER STORY #799705	Control record - PWS mapping screen
			//adding struts result map name		
			String grid =  super.loadMainGridFn("loadAlertGridSection");
			String gridId = super.getGridId();
			this.setRequestGridId(gridId.replace("response", "request"));
			this.setResponseGridId(gridId.replace("request", "response"));			
			return grid;
		}
		
		/**
		 * @author RaedSaad
		 * @description Overriding return Sub Grid
		 * @return Grid
		 */
		public String loadSubGridFn()
		{
			// USER STORY #799705	Control record - PWS mapping screen
			//adding struts result map name
			String grid = super.loadSubGridFn("loadAlertSubGrid");	
			String subGridId = super.getSubGridId();
			String parentRowId = webServiceExplorerCO.getParentRowId();
			String parentRow = null;
			if(parentRowId.equals("serviceContext_"+this.get_pageRef()+"_response") || parentRowId.equals("vendorContext_"+this.get_pageRef()+"_response")||(parentRowId.equals("serviceContext_"+this.get_pageRef())||parentRowId.equals("vendorContext_"+this.get_pageRef())))
			{
				set_recReadOnly("true");
			}
			this.setRequestGridId(subGridId.replace("response", "request"));
			this.setResponseGridId(subGridId.replace("request", "response"));
			return grid;
		}
		
		/**
		 * @author RaedSaad
		 * @description Overriding return Sub Grid
		 * @return Grid
		 */
		public String loadListSubGridFn()
		{
			String grid = super.loadListSubGridFn("loadPwsListSubGrid");
			String listSubGridId = super.getListGridId();
			return grid;
		}
		
		private int returnIndex(String keys,String param)
		{
			String [] arr = keys.split(",");
			for(int i = 0;i<arr.length;i++)
			{
				if(arr[i].contains(param) && arr[i].split("_")[1].equals(param))
				{
					return Integer.parseInt(arr[i].split("_")[0]);
				}
			}
			return -1;
		}
		
		/**
		 * @Description function to synchronize common pws mapping tabel with the parsed wsdl data
		 * @param webServiceExplorerCO
		 * @return
		 * @throws BaseException
		 */
		public WebServiceExplorerCO processWebServiceExplorerMappingData(WebServiceExplorerCO webServiceExplorerCO) throws BaseException 
		{
			List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPwsMappingDetailsVO = webServiceExplorerCO.getLstCommonPwsMappingDetailsVO();
			List<RequestResponseCO> lstReqRespCO = webServiceExplorerCO.getLstRequestResposneCO();
			int size = lstReqRespCO.size();
			String[][] arr = new String[size][size];
			Map<String,Object> mapObject = new HashMap<String,Object>();
			int i=0;
			String mode = null;
			String mapArgVal = null;
			String mapArgType = null;
			String paramName = null;
			String source = null;
			String destination = null;
			RequestResponseCO reqResp = null;
			String parentId = webServiceExplorerCO.getParentRowId();
			for(RequestResponseCO reqRespCO : lstReqRespCO)
			{
				arr[i][0] = reqRespCO.getFieldName();
				mapObject.put(i+"_"+reqRespCO.getFieldName(), reqRespCO);				
				i++;
			}
			int j = 0;
			String keys = mapObject.keySet().toString().replace("[", "").replace("]", "").replace(" ", "");
			int index = 0;
			String x[];
			String[] paramNameArray = null;
			int len = 0;
			String mapField = null;
			for(COMMON_PWS_MAPPING_DETAILSVO obj :lstCommonPwsMappingDetailsVO)
			{
				mode = obj.getMAPPING_ARG_MODE();
				mapArgVal = obj.getMAPPING_ARG_VALUE();
				mapArgType = obj.getMAPPING_ARG_TYPE();
				paramName = obj.getPARAM_NAME();
				source = obj.getSOURCE_KEY();
				destination = obj.getDESTINATION_KEY();
				if("useAccount".equals(paramName))
				{
					System.out.println();
				}
				index = returnIndex(keys,paramName);
				if(index < 0)
				{
					if(null != parentId)
					{
						paramNameArray = paramName.split("\\.");
						len = paramNameArray.length;
						paramName = paramNameArray[len-1];
						source = source.split("\\.")[source.split("\\.").length-1];
						destination = destination.split("\\.")[destination.split("\\.").length-1];
						index = returnIndex(keys,paramName);
						if(index < 0)
						{
							continue;
						}
					}
					else{
						continue;										
					}
				}
				reqResp = lstReqRespCO.get(index);
				if("IN".equals(mode))
				{
					mapField = source.split("\\.")[source.split("\\.").length-1];
					reqResp.setCheckGridRow("1");
					if("IS_LIST".equals(mapArgType))
					{
						reqResp.setLoadSubGrid("1");
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_LIST_OB".equals(mapArgType)){
						reqResp.setLoadSubGrid("1");
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_WRAPPER".equals(mapArgType))
					{
						reqResp.setLoadSubGrid("1");		
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_LIST_FI".equals(mapArgType) && !isNonPrimativeDataType(reqResp.getFieldType()))
					{
						reqResp.setLoadSubGrid("1");		
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);	
					}
					else{
						if("IS_MAPPING_EXPRESSION".equals(obj.getMAPPING_ARG_VALUE()))
						{
							reqResp.setDestination(destination);
							reqResp.setMappingField(mapField);
						}
						else if(null != obj.getMAPPING_ARG_VALUE())
						{				
							reqResp.setDestination(destination);
							reqResp.setMappingField(mapField);
							reqResp.setValue(mapArgVal);
						}
						else if(!source.equals(paramName))
						{
							//case expression
							reqResp.setDestination(source);
							reqResp.setMappingField(destination);
							reqResp.setExpressionHiddenField(source);
						}
					}
				}
				else{
					reqResp.setCheckGridRow("1");
					mapField = destination.split("\\.")[destination.split("\\.").length-1];
					if("IS_LIST".equals(mapArgType))
					{
						reqResp.setLoadSubGrid("1");
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_LIST_OB".equals(mapArgType)){
						reqResp.setLoadSubGrid("1");
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_WRAPPER".equals(mapArgType))
					{
						reqResp.setLoadSubGrid("1");		
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else if("IS_LIST_FI".equals(mapArgType) && !isNonPrimativeDataType(reqResp.getFieldType()))
					{
						reqResp.setLoadSubGrid("1");		
						reqResp.setDestination(destination);
						reqResp.setMappingField(mapField);
					}
					else{
						if("IS_MAPPING_EXPRESSION".equals(obj.getMAPPING_ARG_VALUE()))
						{
							reqResp.setMappingField(mapField);
							reqResp.setDestination(source);
						}
						else if(null != obj.getMAPPING_ARG_VALUE())
						{
							reqResp.setValue(mapArgVal);
							reqResp.setMappingField(mapField);
							reqResp.setDestination(source);
						}
						else if(!source.equals(paramName))
						{
							//case expression
							reqResp.setDestination(source);
							reqResp.setMappingField(destination);
							reqResp.setExpressionHiddenField(source);
						}
					}
				}
			}
			webServiceExplorerCO.setLstRequestResposneCO(lstReqRespCO);
			return webServiceExplorerCO;
		}
		
		
		/**
		 * @description function to load web service explorer saved data 
		 * @note list and hash map saved data will be loaded by using loadWebServiceExplorerListSavedData and loadWebServiceExplorerHashMapSavedData
		 */
		public WebServiceExplorerCO processWebServiceExplorerMappingData1(WebServiceExplorerCO webServiceExplorerCO) throws BaseException {
			List<RequestResponseCO> lstReqResCO = webServiceExplorerCO.getLstRequestResposneCO();
			Map<String,String> dataValues = new HashMap<String,String>();
			Map<String,String> outDataValues = new HashMap<String,String>();
			Map<String,String> mappingArgValues = new HashMap<String,String>();
			Map<String,String> outMappingArgValues = new HashMap<String,String>();
			Map<String,String> expressionMappingArgValues = new HashMap<String,String>();
			Map<String,String> isExpressionMappingField = new HashMap<String,String>();
			Map<String,String> mappingFields = new HashMap<String,String>();
			Map<String,String> destinationMap = new HashMap<String,String>();
			Map<String,String> mappingArgMode = new HashMap<String,String>();
			String orgiFieldId = null;
			String fieldID = null;
			String fieldValue = null;
			String dest = null;
			String parent = "";
			boolean flag = false;
			if(null != webServiceExplorerCO.getParentRowId())
			{
//				parent = webServiceExplorerCO.getParentRowId().replace("_"+webServiceExplorerCO.getPageRef(), "");
				parent = webServiceExplorerCO.getParentRowId();
			}
			List<String> retrievedFields = new ArrayList<String>();
			if(webServiceExplorerCO.getScreenName().equals("alert") && null != webServiceExplorerCO.getLstCommonPwsMappingDetailsVO())
			{
				for(COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO : webServiceExplorerCO.getLstCommonPwsMappingDetailsVO())
				{
					destinationMap.put(commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getPARAM_NAME());
				}
				for(COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO : webServiceExplorerCO.getLstCommonPwsMappingDetailsVO())
				{
					fieldID = commonPwsMappingDetailsVO.getDESTINATION_KEY();				
					fieldID = fieldID.replace("\\.", "_"+webServiceExplorerCO.getPageRef()+"_");
					fieldID = fieldID.replace(".", "_"+webServiceExplorerCO.getPageRef()+"_");
					mappingArgMode.put(fieldID,commonPwsMappingDetailsVO.getMAPPING_ARG_MODE());
					if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() && commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("IN"))
					{
						orgiFieldId = commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef();
						expressionMappingArgValues.put(orgiFieldId, commonPwsMappingDetailsVO.getSOURCE_KEY());
						isExpressionMappingField.put(orgiFieldId, commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
						String prmName = webServiceExplorerCO.getParameterName();
						String prmType = webServiceExplorerCO.getParameterType();
						String lstType = "List<"+commonPwsMappingDetailsVO.getPARAM_NAME()+">";
						if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE() && commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE().equals("IS_MAPPING_EXPRESSION"))
						{
							mappingFields.put(orgiFieldId, commonPwsMappingDetailsVO.getSOURCE_KEY());
							commonPwsMappingDetailsVO.setMAPPING_ARG_VALUE(null);
						}
						if((null != webServiceExplorerCO.getLoadConfig() && webServiceExplorerCO.getLoadConfig()) && (null != lstType && null != prmType && lstType.equals(prmType)))
						{
							mappingFields.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getDESTINATION_KEY());
							isExpressionMappingField.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
							destinationMap.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getDESTINATION_KEY());
						}
					}
					else if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() && commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("OUT"))
					{
						orgiFieldId = commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef();
						expressionMappingArgValues.put(orgiFieldId, commonPwsMappingDetailsVO.getSOURCE_KEY());
						isExpressionMappingField.put(orgiFieldId, commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
						String prmName = webServiceExplorerCO.getParameterName();
						String prmType = webServiceExplorerCO.getParameterType();
						String lstType = "List<"+commonPwsMappingDetailsVO.getPARAM_NAME()+">";
						if(!commonPwsMappingDetailsVO.getDESTINATION_KEY().equals(destinationMap.get(orgiFieldId)))
						{
							mappingFields.put(orgiFieldId, commonPwsMappingDetailsVO.getDESTINATION_KEY());
						}
						if((null != webServiceExplorerCO.getLoadConfig() && webServiceExplorerCO.getLoadConfig()) && (null != lstType && null != prmType && lstType.equals(prmType)))
						{
							mappingFields.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getDESTINATION_KEY());
							isExpressionMappingField.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
							destinationMap.put(parent.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "")+"_"+prmName+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getSOURCE_KEY());
						}
					}
					if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE() && null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() && commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("IN"))
					{
						mappingArgValues.put(fieldID+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
					}
					else if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE()  && null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() &&  commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("OUT"))
					{
						outMappingArgValues.put(fieldID+"_"+webServiceExplorerCO.getPageRef(), commonPwsMappingDetailsVO.getMAPPING_ARG_VALUE());
					}
					if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() && commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("IN"))
					{
					
						if("IS_LIST".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef(),"");
						}
						else if("IS_LIST_FI".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef(),commonPwsMappingDetailsVO.getSOURCE_KEY());
						}
						else if("IS_LIST_OB".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(fieldID+"_"+commonPwsMappingDetailsVO.getDESTINATION_KEY(),commonPwsMappingDetailsVO.getDESTINATION_KEY());
						}

						else{
							dataValues.put(fieldID+"_"+webServiceExplorerCO.getPageRef(),commonPwsMappingDetailsVO.getSOURCE_KEY());
						}
					}
					else if(null != commonPwsMappingDetailsVO.getMAPPING_ARG_MODE() &&  commonPwsMappingDetailsVO.getMAPPING_ARG_MODE().equalsIgnoreCase("OUT"))
					{
						if("IS_LIST".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef(),"");
						}
						else if("IS_LIST_FI".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(commonPwsMappingDetailsVO.getPARAM_NAME()+"_"+webServiceExplorerCO.getPageRef(),commonPwsMappingDetailsVO.getSOURCE_KEY());
						}

						else if("IS_LIST_OB".equals(commonPwsMappingDetailsVO.getMAPPING_ARG_TYPE()))
						{
							outDataValues.put(fieldID+"_"+commonPwsMappingDetailsVO.getSOURCE_KEY(),commonPwsMappingDetailsVO.getSOURCE_KEY());
						}
						else{
							outDataValues.put(fieldID+"_"+webServiceExplorerCO.getPageRef(),commonPwsMappingDetailsVO.getSOURCE_KEY());
						}
					}
					retrievedFields.add(fieldID+"_"+webServiceExplorerCO.getPageRef());
				}
			}
			String fields = retrievedFields.toString();
			fieldID = null;
			String id = null;		
			String prmName = webServiceExplorerCO.getParameterName();
			String prmType = webServiceExplorerCO.getParameterType();
			for(RequestResponseCO reqResCO : lstReqResCO)
			{		
				fieldID = reqResCO.getID();
				id = fieldID.replace("_"+webServiceExplorerCO.getPageRef()+"_response", "");
				id = id.replace("_"+webServiceExplorerCO.getPageRef(), "");
				if(null != webServiceExplorerCO.getLoadConfig() && webServiceExplorerCO.getLoadConfig() && null != prmType && prmType.contains("List"))
				{
					id = id.replace("_1","")+"_"+webServiceExplorerCO.getPageRef();
					fieldID = id;
				}
				else if(null != parent && parent.length()>0)
				{
					id = id.replace("_"+webServiceExplorerCO.getParentListFieldName()+"_1_",".")+"_"+webServiceExplorerCO.getPageRef();
					fieldID = id;
				}
				if("IS_MAPPING_EXPRESSION".equals(isExpressionMappingField.get(fieldID))) //and is request)	
				{					
					reqResCO.setValue(mappingArgValues.get(fieldID));
					reqResCO.setDestination(destinationMap.get(fieldID));
					if(null != webServiceExplorerCO.getLoadConfig() && webServiceExplorerCO.getLoadConfig())
					{
						reqResCO.setMappingField(mappingFields.get(fieldID));
						String x = mappingFields.get(fieldID);
						if(null != x)
						{
							x = mappingFields.get(fieldID).split("\\.")[(mappingFields.get(fieldID).split("\\.")).length-1];
							reqResCO.setMappingField(x);
						}
					}
					else{
						reqResCO.setMappingField(mappingFields.get(fieldID));
					}
				}
				else if(null == reqResCO.getValue() || reqResCO.getValue().isEmpty())
				{
					reqResCO.setValue(mappingArgValues.get(fieldID));
					reqResCO.setExpressionHiddenField(expressionMappingArgValues.get(fieldID));
					reqResCO.setDestination(destinationMap.get(fieldID));
					reqResCO.setMappingField(mappingFields.get(fieldID));
				}
				else{
					reqResCO.setExpressionHiddenField(expressionMappingArgValues.get(fieldID));
					reqResCO.setDestination(destinationMap.get(fieldID));
					reqResCO.setMappingField(mappingFields.get(id));
				}
				//reqResCO.setMappingField(dataValues.get(fieldID));
//				if(dataValues.containsKey(fieldID) && StringUtil.isEmptyString(fieldValue))
//				{
//					reqResCO.setLoadSubGrid(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LOAD_SUB_GRID_BY_FORCE);
//				}
//				else if(outDataValues.containsKey(fieldID) && StringUtil.isEmptyString(fieldValue))
//				{
//					reqResCO.setLoadSubGrid(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LOAD_SUB_GRID_BY_FORCE);
//				}
				if(dataValues.containsKey(fieldID))
				{
					reqResCO.setLoadSubGrid(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LOAD_SUB_GRID_BY_FORCE);
				}
				else if(outDataValues.containsKey(fieldID))
				{
					reqResCO.setLoadSubGrid(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LOAD_SUB_GRID_BY_FORCE);
				}
			}
			return webServiceExplorerCO;
		}
		
		private Boolean isNonPrimativeDataType(String fieldType)
		{
			return ("int".equals(fieldType) || "string".equals(fieldType) || "decimal".equals(fieldType) || "bigdecimal".equals(fieldType) || "dateTime".equals(fieldType) || "long".equals(fieldType) || "date".equals(fieldType) || "float".equals(fieldType) || "double".equals(fieldType) || "boolean".equals(fieldType));
		}

		public WebServiceExplorerCO getWebServiceGuiCO() {
			return webServiceExplorerCO;
		}

		public Boolean getIsEditable() {
			return isEditable;
		}

		public void setIsEditable(Boolean isEditable) {
			this.isEditable = isEditable;
		}

		public WebServiceExplorerCO getWebServiceExplorerCO() {
			return webServiceExplorerCO;
		}

		public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
			this.webServiceExplorerCO = webServiceExplorerCO;
		}

		public void setWebServiceExplorerBO(WebServiceExplorerBO webServiceExplorerBO) {
			this.webServiceExplorerBO = webServiceExplorerBO;
		}

		public void setCommonPwsMappingBO(CommonPwsMappingBO commonPwsMappingBO) {
			this.commonPwsMappingBO = commonPwsMappingBO;
		}

		public String getRequestGridId() {
			return requestGridId;
		}

		public void setRequestGridId(String requestGridId) {
			this.requestGridId = requestGridId;
		}

		public String getResponseGridId() {
			return responseGridId;
		}

		public void setResponseGridId(String responseGridId) {
			this.responseGridId = responseGridId;
		}

		public WebServiceExplorerGridParamCO getWebServiceExplorerGridParamCO() {
			return webServiceExplorerGridParamCO;
		}

		public void setWebServiceExplorerGridParamCO(WebServiceExplorerGridParamCO webServiceExplorerGridParamCO) {
			this.webServiceExplorerGridParamCO = webServiceExplorerGridParamCO;
		}

}

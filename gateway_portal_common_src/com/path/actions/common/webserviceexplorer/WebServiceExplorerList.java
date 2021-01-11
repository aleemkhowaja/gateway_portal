package com.path.actions.common.webserviceexplorer;
/**
 * @Auther:Raed Saad
 * @Date:MARCH 2018
 * @Team: PEMTS JAVA Team.
 * @copyright: PathSolution 2018
 * @User Story: USER STORY #653853  WSDL explorer
 * @Description: extends WebServiceExplorerGridBaseList and overrides grid loading behavior
 **/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.vo.common.webserviceexplorer.RequestResponseCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;

public class WebServiceExplorerList extends WebServiceExplorerGridBaseList{
    private Boolean isEditable;

    /**
     * @Description load main Grid
     * @note incase we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
	  *@Default behaviour loads request
     */
    @Override
	public String loadWSDLInfoGrid() throws BaseException {
		String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
		BigDecimal serviceID = null;
		try{
			if(null != webServiceExplorerCO)
			{
				super.setGridCaption(webServiceExplorerCO.getOperationName());
				//webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
				webServiceExplorerCO.getWebServiceExplorerConfigVO();
				List<RequestResponseCO> lstReqResCO = super.returnWsdlParseData(webServiceExplorerCO);
				webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
	
				if(null != webServiceExplorerCO.getWebServiceExplorerConfigVO())
				{
					serviceID = webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID();
				}
				
				if(null != serviceID && !"undefined".equals(serviceID) && !NumberUtil.isEmptyDecimal(serviceID))
				{
					webServiceExplorerCO.getLstRequestResposneCO();
					webServiceExplorerCO = webServiceExplorerBO.loadWebServiceExplorerSavedData(webServiceExplorerCO);
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
     * @Description load sub Grid
     * @note incase we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
	  *@Default behaviour loads request
     */
	@Override
	public String loadSubGridInfo() throws BaseException {
		String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
		String[] gridIdSplitted;
		BigDecimal serviceID = null;
		try{
			if(null != webServiceExplorerCO && (null!= super.getId() || null != webServiceExplorerCO.getParentRowId()) && null != webServiceExplorerCO.getRequestResponseCO().getGridColumnID() && StringUtil.isNotEmpty(webServiceExplorerCO.getRequestResponseCO().getGridColumnID()))
			{
				//webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
				
				if(null != webServiceExplorerCO.getWebServiceExplorerConfigVO())
				{
					serviceID = webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID();
				}
				webServiceExplorerCO = WebServiceExplorerList.extractDataFromGridRowID(webServiceExplorerCO);
				if(StringUtil.isNotEmpty(webServiceExplorerCO.getHasChildren()))
				{
					List<RequestResponseCO> lstReqResCO = super.returnWsdlParseData(webServiceExplorerCO);
					webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
				}					
				if(null != serviceID && !"undefined".equals(serviceID) && !NumberUtil.isEmptyDecimal(serviceID))
				{
					webServiceExplorerCO.getLstRequestResposneCO();
					webServiceExplorerCO = webServiceExplorerBO.loadWebServiceExplorerSavedData(webServiceExplorerCO);
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
	 * Overriding loadListSubGridInfo and adding additional behavior to load data from database incase of stored data
	 */
	@Override
	public String loadListSubGridInfo()
	{
		try{
			if(null != webServiceExplorerCO.getWebServiceExplorerConfigVO() && null != webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID() && !NumberUtil.isEmptyDecimal(webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_ID()))
			{				
				webServiceExplorerCO = webServiceExplorerBO.loadWebServiceExplorerListSavedData(webServiceExplorerCO);
			}
			else{
			// We can give the List grid rows an ID from here 
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
		this.getWebServiceExplorerCO().setScreenName("WebServiceExplorer");;
		return super.loadMainGridFn("loadGridSection");
	}
	
	/**
	 * @author RaedSaad
	 * @description Overriding return Sub Grid
	 * @return Grid
	 */
	public String loadSubGridFn()
	{
		this.getWebServiceExplorerCO().setScreenName("WebServiceExplorer");;
		return super.loadSubGridFn("loadSubGrid");
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
}

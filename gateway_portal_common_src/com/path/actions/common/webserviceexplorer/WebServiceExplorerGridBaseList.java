package com.path.actions.common.webserviceexplorer;
/**
 * @Auther:Raed Saad
 * @Date:MARCH 2018
 * @Team: PEMTS JAVA Team.
 * @copyright: PathSolution 2018
 * @User Story: USER STORY #653853  WSDL explorer
 * @Description: Base Class to be extended
 **/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.bo.common.webserviceexplorer.WebServiceExplorerConstant;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.GridBaseAction;
import com.path.vo.common.webserviceexplorer.RequestResponseCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerSC;

public class WebServiceExplorerGridBaseList extends GridBaseAction{
	protected WebServiceExplorerBO webServiceExplorerBO;
	protected WebServiceExplorerCO webServiceExplorerCO;
	protected WebServiceExplorerSC webServiceExplorerSC;
	private String gridId;
	private String subGridId;
	private String listGridId;
	private String hashMapGridId;
	private String gridCaption;
	
	/**
	 * @author Raed Saad
	 * @description Return List Of wsdl Parsed data to be set in a grid
	 * @input requestName - operationName and application name
	 * */
	public List<RequestResponseCO> returnWsdlParseData(WebServiceExplorerCO webServiceExplorerCO) throws BaseException 
	{
		if(null == webServiceExplorerCO.getPageRef())
		{
			webServiceExplorerCO.setPageRef(super.get_pageRef());
		}
		if(null != webServiceExplorerCO && null != webServiceExplorerCO.getOperationName() && (null!= webServiceExplorerCO.getApplicationName()||StringUtil.isEmptyString(webServiceExplorerCO.getApplicationName())) 
				&& StringUtil.isNotEmpty(webServiceExplorerCO.getRequestType()))
		{
			webServiceExplorerCO = webServiceExplorerBO.loadFuncRespArg(webServiceExplorerCO);
			return webServiceExplorerCO.getLstRequestResposneCO();
		}
		return new ArrayList<RequestResponseCO>();
	}

	/**
	 * @author RaedSaad
	 * @description Default behavior WSDL Parsing LoadGrid / can be overridden
	 * @return
	 * @throws BaseException
	 * @note incase we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
	 * @default behaviour loads request
	 */
	public String loadWSDLInfoGrid() throws BaseException 
	{
		String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
		BigDecimal serviceID = null;
		if(null != webServiceExplorerCO)
		{
			this.setGridCaption(webServiceExplorerCO.getOperationName());
			List<RequestResponseCO> lstReqResCO = this.returnWsdlParseData(webServiceExplorerCO);
			webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
			setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
		}
		return "success";
	}
	
	/**
	 * @author RaedSaad
	 * @description Default behavior WSDL Parsing LoadGrid / can be overridden
	 * @return
	 * @throws BaseException
	 * @note incase we want to load the web service response webServiceExplorerCO.setRequestType(WebServiceExplorerConstant.RESPONSE_TYPE);
	 * @default behaviour loads request
	 */
	public String loadSubGridInfo() throws BaseException {
		String[] searchCol = {"PARAMETER","TYPE","MANDATORY"};
		String[] gridIdSplitted;
		BigDecimal serviceID = null;
		if(null != webServiceExplorerCO)
		{
			webServiceExplorerCO = WebServiceExplorerList.extractDataFromGridRowID(webServiceExplorerCO);
			if(StringUtil.isNotEmpty(webServiceExplorerCO.getHasChildren()))
			{
				List<RequestResponseCO> lstReqResCO = this.returnWsdlParseData(webServiceExplorerCO);
				webServiceExplorerCO.setLstRequestResposneCO(lstReqResCO);
			}	
			setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
		}
		return "success";
	}
	
	/**
	 * @author RaedSaad
	 * @description load HashMap Sub Grid data
	 * @return
	 * @throws BaseException
	 */
	public String loadHashMapSubGridInfo() throws BaseException
	{		
		List<RequestResponseCO> reqResLst = new ArrayList<RequestResponseCO>();
		RequestResponseCO resReqCO = new RequestResponseCO();		
		resReqCO.setFieldName("");
		resReqCO.setFieldType("");
		resReqCO.setValue("value");
		reqResLst.add(resReqCO);
		WebServiceExplorerCO webServiceExplorerCO = new WebServiceExplorerCO();
		webServiceExplorerCO.setLstRequestResposneCO(reqResLst);
		setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
		return "success";		
	}
	
	/**
	 * @author RaedSaad
	 * @description Load List Sub Grid data
	 * @return
	 */
	public String loadListSubGridInfo()
	{
		// We can give the List grid rows an ID from here 
		webServiceExplorerCO.setLstRequestResposneCO(Arrays.asList(webServiceExplorerCO.getRequestResponseCO()));
		setGridModel(webServiceExplorerCO.getLstRequestResposneCO());
		return "success";
	}
	
//	/**
//	 * @author RaedSaad
//	 * @description return Main Grid
//	 * @return Grid
//	 */
//	public String loadWebServiceGuiListGrid()
//	{
//		webServiceExplorerCO.getWebServiceExplorerConfigVO();
//		return "loadGridSection";
//	}
	
	/**
	 * @author RaedSaad
	 * @description return Main Grid
	 * @return Grid
	 */
	public String loadMainGridFn()
	{		
		this.checkRequest();
		String GridName = returnGridName();
		this.setGridId(GridName);
		return WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_MAIN_GRID_RESULT;
	}
	
	/**
	 * @author RaedSaad
	 * @description return Main Grid with a certain result name defined in struts
	 * @return Grid
	 */
	public String loadMainGridFn(String gridResultName)
	{
		this.checkRequest();
		String GridName = returnGridName();
		this.setGridId(GridName);		
		return gridResultName;
	}
	
	/**
	 * @author RaedSaad
	 * @description return Sub Grid
	 * @return Grid
	 */
	public String loadSubGridFn()
	{
		this.checkRequest();
		String GridName = WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef()
		+ WebServiceExplorerConstant.UNDERSCORE + 	webServiceExplorerCO.getParentRowId() + 
		WebServiceExplorerConstant.SUB_GRID_REF;
		this.setSubGridId(GridName);
		return WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_SUB_GRID_RESULT;
	}	
	
	/**
	 * @author RaedSaad
	 * @description return Sub Grid with a certain result name defined in struts
	 * @return Grid
	 */
	public String loadSubGridFn(String subGridResultName)
	{
		this.checkRequest();
		super.get_pageRef();
		String GridName = this.returnGridName()
		+ WebServiceExplorerConstant.UNDERSCORE + 	webServiceExplorerCO.getParentRowId() + WebServiceExplorerConstant.SUB_GRID_REF;
		this.setSubGridId(GridName);
		return subGridResultName;
	}	
	/**
	 * @author RaedSaad
	 * @description return List Sub Grid
	 * @return Grid
	 */
	public String loadListSubGridFn()
	{	
		this.checkRequest();
		super.get_pageRef();	
		String GridName = WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef()
		+ WebServiceExplorerConstant.UNDERSCORE + 	webServiceExplorerCO.getParentRowId() + 
		WebServiceExplorerConstant.LIST_GRID_REF;
		this.setListGridId(GridName);
		return WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LIST_SUB_GRID_RESULT;
	}
	
	/**
	 * @author RaedSaad
	 * @description return List Sub Grid with a certain result name defined in struts
	 * @return Grid
	 */
	public String loadListSubGridFn(String subGridResultName)
	{
		this.checkRequest();
		super.get_pageRef();	
		String GridName = WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef()
		+ WebServiceExplorerConstant.UNDERSCORE + 	webServiceExplorerCO.getParentRowId() + 
		WebServiceExplorerConstant.LIST_GRID_REF;
		this.setListGridId(GridName);
		return subGridResultName;
	}
	
	/**
	 * @author RaedSaad
	 * @description return HashMap Sub Grid
	 * @return Grid
	 */
	public final String loadHashMapSubGridFn()
	{
		super.get_pageRef();
		webServiceExplorerCO.getDynamicGridId();
		webServiceExplorerCO.getMainGridId();
		webServiceExplorerCO.getParentRowId();
		String GridName = WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef()
		+ WebServiceExplorerConstant.UNDERSCORE + 	webServiceExplorerCO.getParentRowId() + 
		WebServiceExplorerConstant.HASH_MAP_GRID_REF;
		this.setHashMapGridId(GridName);
		return WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_HASHMAP_SUB_GRID_RESULT;
	}
	
	public void checkRequest()
	{
		String screen = this.getWebServiceExplorerCO().getScreenName();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		if(null != screen && screen.equals("webServiceExplorer"))
		{
			request.setAttribute("showMappingField", "false");
		}
		else if(null != screen && screen.equals("pwsGeneration"))
		{
			request.setAttribute("showMappingField", "false");
		}
		else if(null != screen)
		{
			request.setAttribute("showMappingField", "false");//was true
		}
		else{
			request.setAttribute("showMappingField", "false");
		}
		
		// will appear for both request and response grid
		request.setAttribute("showValue","false");
		request.setAttribute("showMappingField", "true");

//		//check if request or response
//		if(null != super.get_pageRef() && super.get_pageRef().contains("_response"))
//		{
//			request.setAttribute("showValue","false");
//			request.setAttribute("showMappingField", "true");
//		}
//		else if(null != webServiceExplorerCO && ((null != webServiceExplorerCO.getPageRef() && webServiceExplorerCO.getPageRef().contains("_response"))||(null != webServiceExplorerCO.getParentRowId() && webServiceExplorerCO.getParentRowId().contains("_response"))))
//		{
//			request.setAttribute("showValue","false");
//			request.setAttribute("showMappingField", "true");
//		}
//		else{
//			request.setAttribute("showValue","true");
//			request.setAttribute("showMappingField", "false");
//		}
	}
	/**
	 * @author Raed Saad
	 * @Description This function will load data base on grid row id
	 * */
	public static WebServiceExplorerCO extractDataFromGridRowID(WebServiceExplorerCO webServiceExplorerCO)
	{
		String id = webServiceExplorerCO.getRequestResponseCO().getGridColumnID();
		String[] gridIdSplitted;
		gridIdSplitted = id.split("_concat_");	
		String x []= null;
		String name = null;
		String data = null;
		for(String ids : gridIdSplitted)
		{
			x = null;
			x = ids.split("-");
			try{
				name = x[0];
				data = x[1];
				switch(name)
				{
					case WebServiceExplorerConstant.REQUEST_RESPONSE_HAS_RESTRICTION:
						webServiceExplorerCO.setHasRestrictions(StringUtil.nullToEmpty(data));
						break;
					case WebServiceExplorerConstant.REQUEST_RESPONSE_APPLICATION_NAME:
						webServiceExplorerCO.setApplicationName(StringUtil.nullToEmpty(data));
						break;					
					case "webServiceName":
						webServiceExplorerCO.setWebServiceName(StringUtil.nullToEmpty(data));
						break;
					case "operationName":
						webServiceExplorerCO.setOperationName(StringUtil.nullToEmpty(data));
						break;				
					case "fieldName":
						//
						break;
					case "fieldType":
						webServiceExplorerCO.setSelectedFieldType(StringUtil.nullToEmpty(data));
						break;
					case "fieldMinOCC":
						//
						break;
					case "hasChildren":
						webServiceExplorerCO.setHasChildren(StringUtil.nullToEmpty(data));
						break;
				}
			}
			catch(Exception e)
			{
				x = null;
				name = null;
				data = null;
			}			
		}		
		return webServiceExplorerCO;
	}
	
	/**
	 * @author Raed Saad
	 * @description function to return main Grid Name
	 * @return
	 */
	private String returnGridName()
	{
		String GridName = null;
//		if(null != this.getWebServiceExplorerCO().getRequestType() && (this.getWebServiceExplorerCO().getRequestType().equalsIgnoreCase("request")||webServiceExplorerCO.getRequestType().equalsIgnoreCase("response")))
//		{
//			GridName = WebServiceExplorerConstant.MAIN_GRID_ID + webServiceExplorerCO.getRequestType() + WebServiceExplorerConstant.UNDERSCORE + super.get_pageRef();
//		}
//		else{
//			GridName = WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef();
//		}
//		return GridName;
		return WebServiceExplorerConstant.MAIN_GRID_ID + super.get_pageRef();
	}
	
	public void setWebServiceExplorerBO(WebServiceExplorerBO webServiceExplorerBO) {
		this.webServiceExplorerBO = webServiceExplorerBO;
	}
	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}
	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}
	public String getGridId() {
		return gridId;
	}
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	public String getSubGridId() {
		return subGridId;
	}
	public void setSubGridId(String subGridId) {
		this.subGridId = subGridId;
	}
	public String getListGridId() {
		return listGridId;
	}
	public void setListGridId(String listGridId) {
		this.listGridId = listGridId;
	}
	public String getHashMapGridId() {
		return hashMapGridId;
	}
	public void setHashMapGridId(String hashMapGridId) {
		this.hashMapGridId = hashMapGridId;
	}

	public WebServiceExplorerSC getWebServiceExplorerSC() {
		return webServiceExplorerSC;
	}

	public void setWebServiceExplorerSC(WebServiceExplorerSC webServiceExplorerSC) {
		this.webServiceExplorerSC = webServiceExplorerSC;
	}

	public String getGridCaption() {
		return gridCaption;
	}

	public void setGridCaption(String gridCaption) {
		this.gridCaption = gridCaption;
	}
}

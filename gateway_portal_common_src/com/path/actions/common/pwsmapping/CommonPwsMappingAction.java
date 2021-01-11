/**
 *
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * USER STORY #799705 Control record - PWS mapping screen
 *
 */
package com.path.actions.common.pwsmapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.path.bo.common.ConstantsCommon;
import com.path.bo.common.pwsmapping.CommonPwsMappingBO;
import com.path.bo.common.pwsmapping.CommonPwsMappingConstant;
import com.path.dbmaps.vo.SYS_PARAM_SCREEN_DISPLAYVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.vo.GridUpdates;
import com.path.struts2.lib.common.base.BaseAction;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.select.SelectSC;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerGridParamCO;
import com.path.vo.common.webserviceexplorer.WebServiceUtil;
import com.path.vo.common.SessionCO;
import com.path.vo.common.pwsmapping.CommonMappingArgCO;
import com.path.vo.common.pwsmapping.PwsDefinitionCO;

public class CommonPwsMappingAction extends BaseAction {

	private ArrayList<SelectCO> serviceType = new ArrayList<SelectCO>();
	private ArrayList<SelectCO> dataType = new ArrayList<SelectCO>();
	/**
	 * Hold reference to the pws definition
	 */
	private PwsDefinitionCO pwsDef = new PwsDefinitionCO();

	/**
	 * Hold reference to the BO
	 */
	private CommonPwsMappingBO commonPwsMappingBO;
	
	//USER STORY #799705 Control record - PWS mapping screen
	private WebServiceExplorerGridParamCO webServiceExplorerGridParamCO;
	
	private WebServiceExplorerCO webServiceExplorerCO;

	/**
	 * load the main dialog
	 * 
	 * @return
	 */
	public String loadPwsDialog() {
		// get all information related to the pws
		try {
			this.initializePwsWebserviceExplorer();
			fillDropDownList();
			
			String temp = pwsDef.getArgumentItems();
			webServiceExplorerGridParamCO.setMappingFields(temp);
			if(null != pwsDef.getMappingVO().getMAPPING_ID() && !ConstantsCommon.EMPTY_BIGDECIMAL_VALUE.equals(pwsDef.getMappingVO().getMAPPING_ID()) )
			{
				pwsDef = commonPwsMappingBO.returnPwsInfo(pwsDef);
			}
			//to add handling for null pwsDef
			if(pwsDef != null)
			{
				pwsDef.setArgumentItems(temp);
				applyDynDisplay(pwsDef.getServiceType());
			}
			else
				applyDynDisplay(null);
		} 
		catch (BaseException e) {
			// TODO Auto-generated catch block
			handleException(e, null, null);
		}
		return "loadPwsDialog";
	}

	/**
	 * @author RaedSaad
	 * @USER STORY #799705	Control record - PWS mapping screen
	 */
	private void initializePwsWebserviceExplorer() {

		WebServiceUtil webServiceUtil = new WebServiceUtil();
		webServiceExplorerGridParamCO = webServiceUtil.returnGridParamCO(CommonPwsMappingConstant.ALERT_WEB_SERVICE_EXPLORER_LIST_ACTION_NAME);
		String subGridActionRef = webServiceExplorerGridParamCO.getSubGridActionRef();
		String mainGridActionRef = webServiceExplorerGridParamCO.getMainGridActionRef();
//		String listSubGridActionRef = webServiceExplorerGridParamCO.getListSubGridRef();
		String listSubGridActionRef = "PwsWebServiceExplorerList_loadListSubGridFn";
		webServiceExplorerGridParamCO.setSubGridActionRef(subGridActionRef);
		webServiceExplorerGridParamCO.setMainGridActionRef(mainGridActionRef);
		webServiceExplorerGridParamCO.setListSubGridRef(listSubGridActionRef);
		webServiceExplorerGridParamCO.setScreenNameSpace(CommonPwsMappingConstant.ALERT_WEB_SERVICE_EXPLORER_SCREEN_NAME_SPACE);		
		webServiceExplorerGridParamCO.setScreenName(CommonPwsMappingConstant.ALERT_WEB_SERVICE_EXPLORER_SCREEN_NAME);
	}
	
	/**
	 * USER STORY #799705 Control record - PWS mapping screen
	 * @return
	 */
	public String saveWebServiceExplorerMappingData()
	{
		try{
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			webServiceExplorerCO = webServiceUtil.returnGridDataToSave(webServiceExplorerCO);
//			commonPwsMappingBO.saveCommonPWSMappingDetails(webServiceExplorerCO);
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}		
		return SUCCESS;
	}

	/**
	 * Save the Newly created batch
	 * 
	 * @return
	 */
	public String save() {
		try {

			SessionCO sessionObject = returnSessionObject();

			// @todo set a flag to check if Mapping is created
			pwsDef.getMappingVO().setCREATED_BY(sessionObject.getUserName());
			pwsDef.getMappingVO().setCREATED_DATE(returnCommonLibBO().returnSystemDateWithTime());

			// update grid info
			if (!"".equals(pwsDef.getArgGridUpdates()) && pwsDef.getArgGridUpdates() != null) {
				GridUpdates gu = getGridUpdates(pwsDef.getArgGridUpdates(), CommonMappingArgCO.class, true);
				if (gu.getLstAllRec() != null && gu.getLstAllRec().size() > 0) {
					pwsDef.setArgGridUpdatesList(gu.getLstAllRec());
				}
			}

			// update Common Mapping Definition
			commonPwsMappingBO.savePws(pwsDef);

		} catch (Exception ex) {
			handleException(ex, null, null);
		}

		return SUCCESS;
	}

	/**
	 * This function will fill up all the drop downs
	 */
	@SuppressWarnings("unchecked")
	private void fillDropDownList() {
		//try {

			SessionCO sessionCO = returnSessionObject();
			String preferredLanguage = sessionCO.getPreferredLanguage();

			SelectSC selSC = null;
			selSC = new SelectSC(new BigDecimal(634));
			selSC.setPreferredLanguage(preferredLanguage);
			selSC.setLovCodesInlude("'R','W'");

			serviceType.add(new SelectCO("pws", "Platform Web Service"));
			//serviceType.addAll((ArrayList<SelectCO>) returnLOV(selSC));

			// populate data type
			dataType.add(new SelectCO("application/json", "application/json"));
			dataType.add(new SelectCO("application/xml", "application/xml"));

		/*} catch (BaseException e) {
			handleException(e, null, null);
		}*/
	}

	/**
	 * Check Periodicity type and apply dynamic screen param base on its value
	 * 
	 * @return
	 */
	public String applyDynamicDisplayByServiceType() 
	{
		try {
			applyDynDisplay(pwsDef.getServiceType());
			this.fillDropDownList();
			this.initializePwsWebserviceExplorer();
		} 
		catch (Exception e)
		{
			handleException(e, null, null);
		}
		return "loadPwsDialog";
	}

	/**
	 * Apply Dynamic screen display based on given Service type
	 * 
	 * @param serviceType
	 */
	private void applyDynDisplay(String serviceType) {

		HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> hm = new HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO>();
		serviceType= StringUtil.nullEmptyToValue(serviceType, "pws");
		// @todo add those to constant , after moving those to constant
		switch (serviceType) {
		case "R":
			applyDynDisplayForRest(hm, true);
			applyDynDisplayForSoap(hm, false);
			applyDynDisplayForPWS(hm, false);
			break;
		case "W":
			applyDynDisplayForRest(hm, false);
			applyDynDisplayForSoap(hm, true);
			applyDynDisplayForPWS(hm, false);
			break;
		// TODO fix this
		case "pws":
			applyDynDisplayForRest(hm, false);
			applyDynDisplayForSoap(hm, false);
			applyDynDisplayForPWS(hm, true);
			break;
		default:
			applyDynDisplayForRest(hm, false);
			applyDynDisplayForSoap(hm, false);
			applyDynDisplayForPWS(hm, false);
		}

		getAdditionalScreenParams().putAll(hm);
	}

	/**
	 * Apply Dynamic Screen display for Rest Element
	 * 
	 * @param hm
	 * @param show
	 */
	private void applyDynDisplayForRest(HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> hm, boolean show) {

		BigDecimal showElement = show ? BigDecimal.ONE : BigDecimal.ZERO;

		SYS_PARAM_SCREEN_DISPLAYVO tblScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		tblScreenDispVO.setIS_VISIBLE(showElement);
		hm.put("pws_tbl_rest", tblScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO endPointScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		endPointScreenDispVO.setIS_VISIBLE(showElement);
		endPointScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("wsEndPointId", endPointScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO contentTypeScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		contentTypeScreenDispVO.setIS_VISIBLE(showElement);
		contentTypeScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("contentTypeId", contentTypeScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO acceptTypeScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		acceptTypeScreenDispVO.setIS_VISIBLE(showElement);
		acceptTypeScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("acceptTypeId", acceptTypeScreenDispVO);
	}

	/**
	 * Apply Dynamic Screen display for Soap Element
	 * 
	 * @param hm
	 * @param show
	 */
	private void applyDynDisplayForSoap(HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> hm, boolean show) {

		BigDecimal showElement = show ? BigDecimal.ONE : BigDecimal.ZERO;

		SYS_PARAM_SCREEN_DISPLAYVO tblScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		tblScreenDispVO.setIS_VISIBLE(showElement);
		hm.put("pws_tbl_soap", tblScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO soapActionScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		soapActionScreenDispVO.setIS_VISIBLE(showElement);
		soapActionScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("soapAction", soapActionScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO msgPrefixScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		msgPrefixScreenDispVO.setIS_VISIBLE(showElement);
		msgPrefixScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("msgPrefixId", msgPrefixScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO nameSpaceScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		nameSpaceScreenDispVO.setIS_VISIBLE(showElement);
		nameSpaceScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("nameSpaceId", nameSpaceScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO soapOperationScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		soapOperationScreenDispVO.setIS_VISIBLE(showElement);
		soapOperationScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("soapOperationId", soapOperationScreenDispVO);

		SYS_PARAM_SCREEN_DISPLAYVO soapLocalPartScreenDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		soapLocalPartScreenDispVO.setIS_VISIBLE(showElement);
		soapLocalPartScreenDispVO.setIS_MANDATORY(showElement);
		hm.put("soapLocalPartId", soapLocalPartScreenDispVO);

	}

	/**
	 * Apply Dynamic Screen display for PWS Element
	 * 
	 * @param hm
	 * @param show
	 * @return
	 */
	private void applyDynDisplayForPWS(HashMap<String, SYS_PARAM_SCREEN_DISPLAYVO> hm, boolean show) {

		BigDecimal showElementTbl = show ? BigDecimal.ZERO : BigDecimal.ONE;

		SYS_PARAM_SCREEN_DISPLAYVO tblApiInfoDispVO = new SYS_PARAM_SCREEN_DISPLAYVO();
		tblApiInfoDispVO.setIS_VISIBLE(showElementTbl);
		hm.put("pwsApiInfo", tblApiInfoDispVO);
	}
	
	public String loadCommonPWSMapping(PwsDefinitionCO pwsDef)
	{
		System.out.println("test");
		try{
			Object obj = commonPwsMappingBO.loadCommonPwsMapping(pwsDef);
			System.out.println("test");
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}		
		return SUCCESS;
	}

	public CommonPwsMappingBO getCommonPwsMappingBO() {
		return commonPwsMappingBO;
	}

	public void setCommonPwsMappingBO(CommonPwsMappingBO commonPwsMappingBO) {
		this.commonPwsMappingBO = commonPwsMappingBO;
	}

	/**
	 * @return
	 */
	public PwsDefinitionCO getPwsDef() {
		return pwsDef;
	}

	/**
	 * @param pwsDef
	 */
	public void setPwsDef(PwsDefinitionCO pwsDef) {
		this.pwsDef = pwsDef;
	}

	public ArrayList<SelectCO> getServiceType() {
		return serviceType;
	}

	public void setServiceType(ArrayList<SelectCO> serviceType) {
		this.serviceType = serviceType;
	}

	public ArrayList<SelectCO> getDataType() {
		return dataType;
	}

	public void setDataType(ArrayList<SelectCO> dataType) {
		this.dataType = dataType;
	}

	public WebServiceExplorerGridParamCO getWebServiceExplorerGridParamCO() {
		return webServiceExplorerGridParamCO;
	}

	public void setWebServiceExplorerGridParamCO(WebServiceExplorerGridParamCO webServiceExplorerGridParamCO) {
		this.webServiceExplorerGridParamCO = webServiceExplorerGridParamCO;
	}

	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}

	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}
}

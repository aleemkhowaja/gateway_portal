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
import java.util.List;

import com.path.bo.common.pwsmapping.CommonPwsMappingBO;
import com.path.bo.common.pwsmapping.CommonPwsMappingConstant;
import com.path.dbmaps.vo.COMMON_PWS_MAPPING_DETAILSVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.GridBaseAction;
import com.path.vo.common.pwsmapping.CommonMappingArgCO;
import com.path.vo.common.pwsmapping.PwsDefinitionCO;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.select.SelectSC;

public class CommonPwsMappingGridAction extends GridBaseAction
{
	/**
	 * Hold reference to the BO
	 */
	private CommonPwsMappingBO commonPwsMappingBO;
	
	private PwsDefinitionCO pwsDefCO;
	
	/**
	 * Combo 
	 */
	private ArrayList<SelectCO> argTypeCombo = new ArrayList<SelectCO>();
	private ArrayList<SelectCO> argStatusCombo = new ArrayList<SelectCO>();
	private ArrayList<SelectCO> argMapperCombo = new ArrayList<SelectCO>();
	
	
	/**
	 * @return
	 */
	public String loadPwsGridParameters(){		
		try {
			List<CommonMappingArgCO> listOfPwsParams = null;
			//retrieve list of Arguments from DB with it's Mapping
			if(pwsDefCO != null && !NumberUtil.isEmptyDecimal(pwsDefCO.getMappingVO().getAPI_CODE()))
				listOfPwsParams = commonPwsMappingBO.returnApiMappingArgs(pwsDefCO);
			if(null!= pwsDefCO)
			{
				this.loadSrc(pwsDefCO);
			}
			// set gril model
			setGridModel(listOfPwsParams);
			
		} catch (BaseException e) {
			handleException(e, null, null);

		}
		
		return SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String loadArgTypeCombo() {
		try {
			// fix this later
			SelectSC selSC = new SelectSC(BigDecimal.valueOf(632));
			selSC.setPreferredLanguage(returnSessionObject().getPreferredLanguage());
			setArgTypeCombo((ArrayList<SelectCO>) returnLOV(selSC));
		} 
		catch (BaseException e) {
			handleException(e, null, null);
		}

		return SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String loadArgStatusCombo() {
		try {
//			SelectSC selSC = new SelectSC(BigDecimal.valueOf(1680));
			SelectSC selSC = new SelectSC(BigDecimal.valueOf(0));
			selSC.setPreferredLanguage(returnSessionObject().getPreferredLanguage());
			setArgStatusCombo((ArrayList<SelectCO>) returnLOV(selSC));
			
			// populate data type
			argStatusCombo.add(new SelectCO("I","IN"));
			argStatusCombo.add(new SelectCO("O","OUT"));
			argStatusCombo.add(new SelectCO("B","INOUT"));
			
		}
		catch (BaseException e) {
			handleException(e, null, null);
		}

		return SUCCESS;
	}
	
	
	/**
	 * @return
	 */
	public String loadArgMapperCombo() {
		
		if(pwsDefCO != null && !StringUtil.nullToEmpty(
				pwsDefCO.getArgumentItems()).isEmpty()){
			String[] listOfItems =  pwsDefCO.getArgumentItems().split(",");
			
			// populate data type
			for(String item : listOfItems){
				argMapperCombo.add(new SelectCO(item,item));
			}
		}
		if(pwsDefCO != null)
		{
			this.loadSrc(pwsDefCO);
		}
		return SUCCESS;
	}
	
	/**
	 * @description function to load the mapping values
	 * @param pwsDefCO
	 */
	private void loadSrc(PwsDefinitionCO pwsDefCO)
	{
		try{
			COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO = new COMMON_PWS_MAPPING_DETAILSVO();
			commonPwsMappingDetailsVO.setMAPPING_ID(pwsDefCO.getMappingVO().getMAPPING_ID());
			List<COMMON_PWS_MAPPING_DETAILSVO> lstCommonPwsMappingDetailsVO = commonPwsMappingBO.loadCommonPWSMappingDetails(commonPwsMappingDetailsVO);
			List<String> mappingFields = CommonPwsMappingConstant.MAPPING_FIELDS;
			SelectCO selectCO = new SelectCO();
			selectCO.setCode("");
			selectCO.setDescValue("");
			argMapperCombo.add(selectCO);
			for (String fieldMap : mappingFields)
			{
				selectCO = new SelectCO();
				selectCO.setCode(fieldMap);
				selectCO.setDescValue(fieldMap);
				argMapperCombo.add(selectCO);
			}
			for(COMMON_PWS_MAPPING_DETAILSVO commonPwsMappingDetailsVO1 : lstCommonPwsMappingDetailsVO)
			{
				if(null != commonPwsMappingDetailsVO1.getDESTINATION_KEY())
				{
					argMapperCombo.add(selectCO);
					selectCO = new SelectCO();
					selectCO.setCode(commonPwsMappingDetailsVO1.getDESTINATION_KEY());
					selectCO.setDescValue(commonPwsMappingDetailsVO1.getDESTINATION_KEY());
				}
				else if(null != commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE())
				{
					argMapperCombo.add(selectCO);
					selectCO = new SelectCO();
					selectCO.setCode(commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE());
					selectCO.setDescValue(commonPwsMappingDetailsVO1.getMAPPING_ARG_VALUE());
				}
			}
		
			this.setArgMapperCombo(argMapperCombo);
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
	}
	
	/**
	 * @param commonPwsMappingBO
	 */
	public void setCommonPwsMappingBO(CommonPwsMappingBO commonPwsMappingBO) {
		this.commonPwsMappingBO = commonPwsMappingBO;
	}
	
	
	/**
	 * @return
	 */
	public PwsDefinitionCO getPwsDefCO() {
		return pwsDefCO;
	}


	/**
	 * @param pwsDefCO
	 */
	public void setPwsDefCO(PwsDefinitionCO pwsDefCO) {
		this.pwsDefCO = pwsDefCO;
	}

	
	/**
	 * @return
	 */
	public ArrayList<SelectCO> getArgTypeCombo() {
		return argTypeCombo;
	}

	
	/**
	 * @param argTypeCombo
	 */
	public void setArgTypeCombo(ArrayList<SelectCO> argTypeCombo) {
		this.argTypeCombo = argTypeCombo;
	}

	/**
	 * @return the argStatusCombo
	 */
	public ArrayList<SelectCO> getArgStatusCombo() {
		return argStatusCombo;
	}

	/**
	 * @param argStatusCombo the argStatusCombo to set
	 */
	public void setArgStatusCombo(ArrayList<SelectCO> argStatusCombo) {
		this.argStatusCombo = argStatusCombo;
	}

	public ArrayList<SelectCO> getArgMapperCombo() {
		return argMapperCombo;
	}

	public void setArgMapperCombo(ArrayList<SelectCO> argMapperCombo) {
		this.argMapperCombo = argMapperCombo;
	}
}

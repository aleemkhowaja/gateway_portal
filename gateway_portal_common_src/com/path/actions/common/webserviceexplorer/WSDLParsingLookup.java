package com.path.actions.common.webserviceexplorer;

import java.util.ArrayList;

/**
 * @Auther:Raed Saad
 * @Date:MARCH 2018
 * @Team: PEMTS JAVA Team.
 * @copyright: PathSolution 2018
 * @User Story: USER STORY #653853  WSDL explorer
 * @Description: Saved Configuration Lookup
 **/

import java.util.List;

import org.apache.struts2.json.JSONException;

import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.dbmaps.vo.IMCO_PWS_TEST_MASTERVOWithBLOBs;
import com.path.lib.vo.LookupGrid;
import com.path.lib.vo.LookupGridColumn;
import com.path.struts2.lib.common.base.LookupBaseAction;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerSC;

public class WSDLParsingLookup extends LookupBaseAction{
	private static final long serialVersionUID = 1L;
	private WebServiceExplorerSC criteria = new WebServiceExplorerSC();
	private WebServiceExplorerCO webServiceExplorerCO;
	private WebServiceExplorerBO webServiceExplorerBO;
	
	@Override
	public Object getModel() {
		return criteria;
	}

	public String constructConfigLookup()
	    {
		try
		{
		    // Design the Grid by defining the column model and column names
		    String[] name = {"CONFIG_ID","CONFIG_NAME","APPLICATION_NAME","ENDPOINT_NAME","OPERATION_NAME"};
		    String[] colType = {"number", "text", "text", "text", "text"};
		    String[] titles = {getText("config_id_key"), getText("config_name_key"), getText("application_name_key"), getText("endpoint_name_key"), getText("operation_name_key")};

		    // Defining the Grid
		    LookupGrid grid = new LookupGrid();
		    grid.setCaption("config_key");
		    grid.setRowNum("10");
		    grid.setUrl("/path/common/WebserviceExplorer/WSDLParsingLookup_fillConfigLookup");
		    int cols = name.length;
		    List<LookupGridColumn> lsGridColumn = new ArrayList<LookupGridColumn>();

		    for(int i = 0; i < cols; i++)
		    {
			// Defining each column
				LookupGridColumn gridColumn = new LookupGridColumn();
				gridColumn.setName(name[i]);
				gridColumn.setIndex(name[i]);
				gridColumn.setColType(colType[i]);
				gridColumn.setTitle(titles[i]);
				gridColumn.setSearch(true);
				// adding column to the list
				lsGridColumn.add(gridColumn);
		    }
		    lookup(grid, lsGridColumn, null, criteria);
		}
		catch(Exception e)
		{
		    // log.error(e, "Error constructing the query lookup");
		    handleException(e, "Error constructing the query lookup", "constructingQryLkp.exceptionMsg._key");
		}

		return SUCCESS;
	    }

	    public String fillConfigLookup() throws JSONException
	    {
		try
		{
		    setSearchFilter(criteria);
		    copyproperties(criteria);

		    if(getRecords() == 0)
		    {
				setRecords(webServiceExplorerBO.webServiceExplorerConfigCoun(criteria));
				//setRecords(webServiceExplorerBO.webServiceExplorerConfigCoun(criteria));
				//qetRecords(webServiceExplorerBO.webServiceExplorerConfigCoun(criteria));

		    }

			 List<IMCO_PWS_TEST_MASTERVOWithBLOBs> lstWebserviceExplorerConfigVO = webServiceExplorerBO.loadWebServiceSavedConfiguration(criteria);
			 setGridModel(lstWebserviceExplorerConfigVO);
		}
		catch(Exception e)
		{
		    // log.error(e, "Error filling query lookup");
		    handleException(e, "Error filling query lookup", "fillQryLookup.exceptionMsg._key");
		}
		return SUCCESS;
	    }

	   /**
	    * @description wsdl parssing common fields lookup
	    * @return
	    */
		public String constructCommonFieldsLookup()
		{
			try{
				   // Design the Grid by defining the column model and column names
			    String[] name = {"imcoPwsChannelVO.CHANNEL_ID","imcoPwsChannelVO.USER_ID","imcoPwsChannelDetVO.HOST_NAME","imcoPwsChannelDetVO.HASH_KEY"};
			    String[] colType = {"number", "text", "text", "text", "text"};
			    String[] titles = {getText("channel_id_key"), getText("user_id_key"), getText("host_name_key"), getText("hashkey_key")};
	
			    // Defining the Grid
			    LookupGrid grid = new LookupGrid();
			    grid.setCaption("common_fields_key");
			    grid.setRowNum("10");
			    grid.setUrl("/path/common/WebserviceExplorer/WSDLParsingLookup_fillCommonFieldsLookup");
			    int cols = name.length;
			    List<LookupGridColumn> lsGridColumn = new ArrayList<LookupGridColumn>();
	
			    for(int i = 0; i < cols; i++)
			    {
				// Defining each column
					LookupGridColumn gridColumn = new LookupGridColumn();
					gridColumn.setName(name[i]);
					gridColumn.setIndex(name[i]);
					gridColumn.setColType(colType[i]);
					gridColumn.setTitle(titles[i]);
					gridColumn.setSearch(true);
					// adding column to the list
					lsGridColumn.add(gridColumn);
			    }
			    lookup(grid, lsGridColumn, null, criteria);
			}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return SUCCESS;
		}
		
		/**
		 * 
		 * @return
		 * @throws JSONException
		 */
	    public String fillCommonFieldsLookup() throws JSONException
	    {
			try
			{
			    setSearchFilter(criteria);
			    copyproperties(criteria);
		
			    if(getRecords() == 0)
			    {
					setRecords(webServiceExplorerBO.webServiceExplorerConfigCoun(criteria));
			    }
			    List<WebServiceExplorerCO>  lstWebServiceExplorerCO = webServiceExplorerBO.loadWebServiceExplorerCommonFieldsLookUp(criteria);
			    setGridModel(lstWebServiceExplorerCO);
			}
			catch(Exception e)
			{
				super.handleException(e, null, null);
			}
			return SUCCESS;	
		}
	
	public void setWebServiceGuiBO(WebServiceExplorerBO webServiceExplorerBO) {
		this.webServiceExplorerBO = webServiceExplorerBO;
	}

	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}

	public void setWebServiceExplorerCO(WebServiceExplorerBO webServiceEorerCxplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}

	public void setWebServiceExplorerBO(WebServiceExplorerBO webServiceExplorerBO) {
		this.webServiceExplorerBO = webServiceExplorerBO;
	}

}

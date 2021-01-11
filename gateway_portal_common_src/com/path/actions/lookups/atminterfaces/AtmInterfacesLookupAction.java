package com.path.actions.lookups.atminterfaces;

import java.util.List;

import com.path.bo.atminterfaces.AtmInterfacesBO;
import com.path.bo.atminterfaces.AtmInterfacesConstants;
import com.path.lib.vo.LookupGrid;
import com.path.struts2.lib.common.base.LookupBaseAction;
import com.path.vo.atminterfaces.AtmInterfacesCO;
import com.path.vo.atminterfaces.AtmInterfacesSC;
import com.path.vo.common.SessionCO;

public class AtmInterfacesLookupAction extends LookupBaseAction
{

   
   private AtmInterfacesBO atmInterfacesBO;
    
   private AtmInterfacesSC atmInterfaceSC = new AtmInterfacesSC();

    /**
     * Construct vault Lookup based on the VO returned in the resultMap.
     * 	
     * @return
     */
    public String constructLookup()
    {

	try
	{
	    // Design the Grid by defining the column model and column names
	    String[] name = 	{"iso_INTERFACESVO.INTERFACE_CODE", 
		    		 "iso_INTERFACESVO.COMP_CODE", 
		    		 "iso_INTERFACESVO.DESCRIPTION" , 
		    		 "iso_INTERFACESVO.INTERFACE_TYPE"
	    			};
	    
	    String[] colType = { "text", "text", "text", "text"};
	    String[] titles = { getText("code_key"), getText("comp_code_key"), getText("Description_key"), getText("interface_type_key")};

	    // Defining the Grid
	    LookupGrid grid = new LookupGrid();
	    grid.setCaption(getText("atminterfaces"));
	    grid.setRowNum("5");
	    grid.setUrl("/path/atmInterfaces/ATMInterfacesLookupAction_populateATMInterfacesLookup");
	    lookup(grid, atmInterfaceSC, name, colType, titles);
	}
	catch(Exception e)
	{
	    handleException(e, null, null);
	}

	return SUCCESS;
    }

    /**
     * Fill the lookup vault data filtered by the defined criteria
     * 
     * @return
     */
    public String populateATMInterfacesLookup()
    {
	try
	{
	    setSearchFilter(atmInterfaceSC);
	    copyproperties(atmInterfaceSC);
	    
	    SessionCO sessionCO = returnSessionObject();
	    atmInterfaceSC.setStatus(AtmInterfacesConstants.STATUS_APPROVED);
	    atmInterfaceSC.setCompCode(sessionCO.getCompanyCode());
	    atmInterfaceSC.setCurrAppName(sessionCO.getCurrentAppName());
	    atmInterfaceSC.setPreferredLanguage(sessionCO.getLanguage());
	    atmInterfaceSC.setInterfaceTypeLovId(AtmInterfacesConstants.INTERFACE_TYPE);
	    atmInterfaceSC.setCrudMode(getIv_crud());
	    atmInterfaceSC.setPageRef(get_pageRef());
	    
	    if(getRecords() == 0)
	    {
		setRecords(atmInterfacesBO.returnInterfaceListCount(atmInterfaceSC));
	    }
	    
	    List<AtmInterfacesCO> atmInterfaceCOs = atmInterfacesBO.returnInterfaceList(atmInterfaceSC);
	    setGridModel(atmInterfaceCOs);
	}
	catch(Exception e)
	{
	    log.error(e, "Error in fillLookupData of AtmInterfacesLookupAction");
	    handleException(e, null, null);
	}
	return SUCCESS;
    }

    @Override
    public Object getModel()
    {
	return atmInterfaceSC;
    }

    public AtmInterfacesSC getAtmInterfaceSC()
    {
        return atmInterfaceSC;
    }

    public void setAtmInterfaceSC(AtmInterfacesSC atmInterfaceSC)
    {
        this.atmInterfaceSC = atmInterfaceSC;
    }

    public void setAtmInterfacesBO(AtmInterfacesBO atmInterfacesBO)
    {
        this.atmInterfacesBO = atmInterfacesBO;
    }
}
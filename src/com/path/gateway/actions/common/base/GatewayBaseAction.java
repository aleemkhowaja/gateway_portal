package com.path.gateway.actions.common.base;

import com.path.gateway.bo.common.GatewayCommonConstants;
import com.path.struts2.lib.common.base.BaseAction;

/**
 * 
 * Copyright 2019, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * GatewayBaseAction.java used to
 */
public class GatewayBaseAction extends BaseAction
{
    /**
     * Returns the RGB color for status code
     *
     * @param status
     */
    protected String getStatusColorCode(String status, String space)
    {
	String colorCode = "";

	if("A".equals(status)) // Active
	{
	    colorCode = GatewayCommonConstants.STATUS_COLOR_CODE_B.equals(space) ? "RGB(000,128,000)"
		    : "RGB(255,255,255)";// GREEN
	}
	else if("P".equals(status)) // Approved
	{
	    colorCode = GatewayCommonConstants.STATUS_COLOR_CODE_B.equals(space) ? "RGB(000,000,255)"
		    : "RGB(255,255,255)"; // BLUE
	}
	else // Deleted,Reversed
	{
	    colorCode = GatewayCommonConstants.STATUS_COLOR_CODE_B.equals(space) ? "RGB(255,000,000)"
		    : "RGB(255,255,255)";
	}
	return colorCode;

    }

}

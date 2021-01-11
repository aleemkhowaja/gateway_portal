function indEvent_setLayout(id)
{
	$("#gbox_"+id+"_"+_pageRef).removeAttr("style");
	//$(".ui-jqgrid-btable").removeAttr("style");
	$("#"+id+"_"+_pageRef+"_pager").removeAttr("style");
	$("#gview_"+id+"_"+_pageRef).find('.ui-jqgrid-hdiv').removeAttr("style");
	$("#gview_"+id+"_"+_pageRef).removeAttr("style");
	$("#gview_"+id+"_"+_pageRef).find('.ui-jqgrid-bdiv').css('width', '');
}

/**
 * OnChange Show/Hide row
 * @returns
 */
function common_onChangeShowHideRow(evt, id, className, requiredids, required, component)
{
	var isChecked;
	if(evt == null)
	{
		isChecked = $("#"+id+":checked").val();
	}
	else
	{
		isChecked = $("#"+evt.id+":checked").val();
	}
	 
	if (isChecked != undefined && (isChecked == "true" || isChecked == true))
	{
		$("."+className+"_"+_pageRef).attr("style", "display: none;");
		if(required)
		{
			for(var i=0; i<requiredids.length; i++)
			{
				if(component == "lookup")
				{
					var id =  "lookuptxt_"+requiredids[i]+"_"+_pageRef;
					$("#"+id).removeAttr("required");
				}
			}
			
		}
	}
	else
	{
		$("."+className+"_"+_pageRef).attr("style", "display: table-row;");
		
		if(required)
		{
			for(var i=0; i<requiredids.length; i++)
			{
				if(component == "lookup")
				{
					var id = "lookuptxt_"+requiredids[i]+"_"+_pageRef;
					$("#"+id).attr("required", "true");
				}
			}
		}
	}	
}

/**
 * Auto complete while down key press 
 */
common_autoCompleteConstraints = function(theInputId,expression_cust_tags)
{ 
	var theInput = $("#"+theInputId);
	// don't navigate away from the field on tab when selecting an item
    theInput.bind( "keydown", function( event )
    {
    	if( event.keyCode === $.ui.keyCode.DOWN && !theInput.isopened)
	   	{
	       	theInput.autocomplete( "search", "" );
	   	}
    })
    .autocomplete({
    	minLength: 0,
    	inputId:theInputId,
    	source: expression_cust_tags,
    	open: function( event, ui )
    	{
    		theInput.isopened = true;
    	},
    	close: function( event, ui )
    	{
    		theInput.isopened = false;
    	},
    	focus: function()
    	{
    		// prevent value inserted on focus
    		return false;
    	},
    	select: function( event, ui )
    	{
			var cursPosition   = returnCursorPosStart(document.getElementById(theInputId));
			var strTillCurrPos = this.value.substring(0,cursPosition)
			/**
			 * [MarwanMaddah]: this pattern will catch all the words 
			 * that are exists in the input from index 0 untill the current cursor position
			 * then the last word will be replaced by the selected value from the Search result
			 */
			var patt       = /\w+/g;
			var result     = strTillCurrPos.match(patt);
			var firstPart  = "";
			if(result!= null && result.length > 0)
			{        	  
				var resultLgth = result.length;
				
				firstPart  = strTillCurrPos.substring(0,strTillCurrPos.lastIndexOf(result[resultLgth-1])); 
			}
			else
			{
				firstPart = strTillCurrPos; 
			}
			this.value = firstPart + " " +ui.item.value +" "+ this.value.substring(cursPosition);
			return false;
    	}
    });
};	


/**
 * add required attribute with field
 * @param selecter it will be class or id i.e description_id
 * @param lbl it will be class or id of label of required field
 * @param selecterType class/id 
 * @returns
 */
function common_makeRequiredFields(selecter, lbl, selecterType)
{
	if(selecterType == 'class')
	{
		$("."+lbl+"_"+_pageRef).addClass("required");
		$("."+selecter+"_"+_pageRef).attr("required", "true");
		$("."+lbl+"_"+_pageRef).append("<span class='required'>*</span>");
		
	}
	else
	{
		$("#"+lbl+"_"+_pageRef).addClass("required");
		$("#"+selecter+"_"+_pageRef).attr("required", "true");
		$("#"+lbl+"_"+_pageRef).append("<span class='required'>*</span>");
	}
}

/**
 * remove required attribute with field
 * @param selecter it will be class or id i.e description_id
 * @param lbl it will be class or id of label of required field
 * @param selecterType class/id 
 * @returns
 */
function common_removeRequiredFields(selecter, lbl, selecterType)
{
	if(selecterType == 'class')
	{
		$("."+lbl+"_"+_pageRef).removeClass("required");
		$("."+selecter+"_"+_pageRef).removeAttr("required");
		$("."+lbl+"_"+_pageRef).find("span").remove();
		
	}
	else
	{
		$("#"+lbl+"_"+_pageRef).removeClass("required");
		$("#"+selecter+"_"+_pageRef).removeAttr("required");
		$("#"+lbl+"_"+_pageRef).find("span").remove();
	}
}

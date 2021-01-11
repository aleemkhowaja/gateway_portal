package com.path.actions.common.webserviceexplorer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @Auther:Raed Saad
 * @Date:MARCH 2018
 * @Team: PEMTS JAVA Team.
 * @copyright: PathSolution 2018
 * @User Story: USER STORY #653853  WSDL explorer
 * @Description: Base Class to be extended based on usage requirements
 **/

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.bo.common.webserviceexplorer.WebServiceExplorerConstant;
import com.path.dbmaps.vo.IMCO_PWS_TEST_MASTERVOWithBLOBs;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.SecurityUtils;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.BaseAction;
import com.path.vo.common.select.SelectCO;
import com.path.vo.common.webserviceexplorer.RequestResponseCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerGridParamCO;
import com.path.vo.common.webserviceexplorer.WebServiceUtil;
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class WebServiceExplorerMaintAction extends BaseAction {
	private String wsURL = "";
	private String requestMessage = "";
	private String timeStamp;

	private List<SelectCO> applicationTypeList;
	private List<SelectCO> operationNameList;
	private List<SelectCO> requestTypeList;// = new ArrayList<SelectCO>();

	private List<SelectCO> restrictions;
	private List<SelectCO> mappingValues;
	private WebServiceExplorerCO webServiceExplorerCO;
	private WebServiceExplorerBO webServiceExplorerBO;
	
	private WebServiceExplorerGridParamCO webServiceExplorerGridParamCO;
	
	/**
	 * @description function to initialize screen parameters
	 * @return
	 */
	public String initializeScreen() {
		try {
			//mandatory step
			WebServiceUtil webServiceUtil = new WebServiceUtil();
			//passing the grid action name define in struts
			webServiceExplorerGridParamCO = webServiceUtil.returnGridParamCO(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_LIST_ACTION_NAME);
			webServiceExplorerGridParamCO.setScreenNameSpace(WebServiceExplorerConstant.WEB_SERVICE_EXPLORER_SCREEN_NAME_SPACE);
			if(null == webServiceExplorerCO)
			{
				webServiceExplorerCO = new WebServiceExplorerCO();
			}
			webServiceExplorerCO.setOnOfFlag(WebServiceExplorerConstant.WEBSERVICE_EXPLORER_ON);
			webServiceExplorerCO = webServiceExplorerBO.applySysParamOption(webServiceExplorerCO);
			getAdditionalScreenParams().putAll(webServiceExplorerCO.getHm());
			set_searchGridId("WebServiceExplorerConfigLookup");
		} 
		catch (Exception e) 
		{
			handleException(e, null, null);
		}
		return "loadWebServiceExplorerSavedConfigurationLookup";
	}

	/**
	 * @description function to apply screen visibility parameters
	 * @return json
	 */
	public String toolBarVisiblity()
	{
		try{
			webServiceExplorerCO = webServiceExplorerBO.applySysParamOption(webServiceExplorerCO);
			getAdditionalScreenParams().putAll(webServiceExplorerCO.getHm());
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "success";
	}
	
	public String operationNameOnChange() {
		webServiceExplorerCO.getApplicationName();
		webServiceExplorerCO.getOperationName();

		return "loadWebServiceExplorerMaintScreen";
	}

	public String dependencyByRestriction() {

		return "success";
	}

	public List<SelectCO> fillRequestTypeList() {
		List<SelectCO> requestTypeList = new ArrayList<SelectCO>();
		SelectCO selectCO = new SelectCO();

		requestTypeList.add(selectCO);

		selectCO = new SelectCO();
		selectCO.setCode("1");
		selectCO.setDescValue("Request");
		selectCO.setDefaultValue("Request");
		requestTypeList.add(selectCO);

		selectCO = new SelectCO();
		selectCO.setCode("2");
		selectCO.setDescValue("Response");
		selectCO.setDefaultValue("Response");
		requestTypeList.add(selectCO);
		this.setRequestTypeList(requestTypeList);
		return requestTypeList;
	}

	/**
	 * @Descrition function to fill Restriction
	 * @return
	 */
	public String fillRestrictions() {
		if(null != webServiceExplorerCO)
		{
			String rowId[] = webServiceExplorerCO.getRowId().split("_concat_");
			int hasRestrictions = 0;
			if (StringUtil.isNotEmpty(StringUtil.nullToEmpty(webServiceExplorerCO.getRowId())) && !"undefined".equalsIgnoreCase(StringUtil.nullToEmpty(webServiceExplorerCO.getRowId()))) {
				if(rowId[0] == null || rowId[0].equals("null"))
				{
					hasRestrictions  = 0;
				}
				else{
					hasRestrictions = Integer.parseInt(rowId[0]);
				}
			}
			try {
				if (hasRestrictions > 0) {
					webServiceExplorerCO = webServiceExplorerBO.loadSimpleType(webServiceExplorerCO);
					this.setRestrictions(webServiceExplorerCO.getRequestResponseCO().getRestrictions());
				}
			} 
			catch (Exception e) {
				handleException(e, null, null);
			}
		}
		return "success";
	}

	/**
	 * @Description function to fill mapping fields
	 * @return
	 */
	public String fillMappingFields()
	{
		if(null != webServiceExplorerCO && null != webServiceExplorerCO.getMappingFields() && webServiceExplorerCO.getMappingFields().length()>0)
		{
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(webServiceExplorerCO.getMappingFields());
			List<SelectCO> mappingValues = new ArrayList<SelectCO>();			
			SelectCO selectCO = new SelectCO();
			selectCO.setCode("");
			selectCO.setDescValue("");
			
			for(Object key : jsonObject.keySet())
			{
				mappingValues.add(selectCO);
				selectCO = new SelectCO();
				selectCO.setCode(jsonObject.get(key).toString());
				selectCO.setDescValue(jsonObject.get(key).toString());
			}
			mappingValues.add(selectCO);
			this.setMappingValues(mappingValues);
			mappingValues.toString();
		}
		return "success";
	}

	/**
	 * @Description function that load save configuration called after lookup dependency
	 * @return
	 */
	public String configLookupDependency()
	{
		IMCO_PWS_TEST_MASTERVOWithBLOBs webServiceExplorerConfigVO = new IMCO_PWS_TEST_MASTERVOWithBLOBs();
		try{
			webServiceExplorerConfigVO.setAPPLICATION_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getAPPLICATION_NAME());
			webServiceExplorerConfigVO.setOPERATION_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getOPERATION_NAME());
			webServiceExplorerConfigVO.setENDPOINT_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getENDPOINT_NAME());
			webServiceExplorerCO = webServiceExplorerBO.configLookupDependency(webServiceExplorerCO);
			if(null == webServiceExplorerCO.getWebServiceExplorerConfigVO())
			{
				webServiceExplorerCO.setWebServiceExplorerConfigVO(webServiceExplorerConfigVO);
			}
		}
		catch(Exception e)
		{
			webServiceExplorerCO.setWebServiceExplorerConfigVO(webServiceExplorerConfigVO);
			this.handleException(e, null, null);
		}
		return "success";
	}
	

	/**
	 * @saving Grid information
	 * @return
	 */
	public String saveRequest()
	{
		WebServiceUtil webServiceUtil = new WebServiceUtil();
		//List<RequestResponseCO> modifiedGridRows= webServiceUtil.returnGridParam(webServiceExplorerCO.getWebServiceExplorerGridUpdates());
		webServiceExplorerCO = webServiceUtil.returnGridDataToSave(webServiceExplorerCO);
//		webServiceExplorerCO = this.generateSoapRequest(webServiceExplorerCO);
		try{
			if(null != webServiceExplorerCO.getWebServiceExplorerDataVOLst() && !webServiceExplorerCO.getWebServiceExplorerDataVOLst().isEmpty())
			{
				webServiceExplorerBO.insertGridData(webServiceExplorerCO);
			}
		}
		catch(Exception e)
		{
			this.handleException(e, null, null);
		}
		return "success";
	}
	
	/**
	 * @description function to upgrade grid information
	 * @return
	 */
	public String updateRequest()
	{
		WebServiceUtil webServiceUtil = new WebServiceUtil();
		//List<RequestResponseCO> modifiedGridRows= webServiceUtil.returnGridParam(webServiceExplorerCO.getWebServiceExplorerGridUpdates());
		webServiceExplorerCO = webServiceUtil.returnGridDataToSave(webServiceExplorerCO);
//		webServiceExplorerCO = this.generateSoapRequest(webServiceExplorerCO);
		try{
			if(null != webServiceExplorerCO.getWebServiceExplorerDataVOLst() && !webServiceExplorerCO.getWebServiceExplorerDataVOLst().isEmpty())
			{
				webServiceExplorerCO = webServiceExplorerBO.updateGridData(webServiceExplorerCO);
				getAdditionalScreenParams().putAll(webServiceExplorerCO.getHm());
			}
		}
		catch(Exception e)
		{
			this.handleException(e, null, null);
		}
		return "success";
	}
	/**
	 * @description load server time stamp
	 * @return
	 */
	public String loadServerTimeStamp()
	{
		String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		String time = new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime());
		this.setTimeStamp(time);
		return "success";
	}
	
	/**
	 * @description channel id dependency
	 * @return
	 */
	public String channelIdDependency()
	{
		try{
			if(null != webServiceExplorerCO)
			{
				BigDecimal channelId = webServiceExplorerCO.getImcoPwsChannelVO().getCHANNEL_ID();
				if(NumberUtil.isEmptyDecimal(channelId))
				{
					webServiceExplorerCO.getImcoPwsChannelVO().setCHANNEL_ID(null);
					webServiceExplorerCO.getImcoPwsChannelDetVO().setHASH_KEY("");
					webServiceExplorerCO.getImcoPwsChannelDetVO().setHOST_NAME("");
				}
			}
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "success";
	}
	
	/*
	 * Request Response commented in common
	 */
	
	/**
	 * @descritpion loads config request into an output stream
	 */
	public void loadConfigSavedRequest()
	{
		IMCO_PWS_TEST_MASTERVOWithBLOBs webServiceExplorerConfigVO = new IMCO_PWS_TEST_MASTERVOWithBLOBs();
		try{
			webServiceExplorerConfigVO.setAPPLICATION_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getAPPLICATION_NAME());
			webServiceExplorerConfigVO.setOPERATION_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getOPERATION_NAME());
			webServiceExplorerConfigVO.setENDPOINT_NAME(webServiceExplorerCO.getWebServiceExplorerConfigVO().getENDPOINT_NAME());
			webServiceExplorerCO = webServiceExplorerBO.configLookupDependency(webServiceExplorerCO);
			if(null == webServiceExplorerCO.getWebServiceExplorerConfigVO())
			{
				webServiceExplorerCO.setWebServiceExplorerConfigVO(webServiceExplorerConfigVO);
			}
			String xmlRequest = webServiceExplorerCO.getWebServiceExplorerConfigVO().getCONFIG_REQUEST();
			
			this.loadRequest(xmlRequest);
		}
		catch(Exception e)
		{
			webServiceExplorerCO.setWebServiceExplorerConfigVO(webServiceExplorerConfigVO);
			this.handleException(e, null, null);
		}
	}
	
	/**
	 * @Description: convert grid json request into an xml request inorder to display and process it
	 * */
	
	public void showRequest() 
	{
		try {
			String xmlRequest = null;
			webServiceExplorerCO = this.generateSoapRequest(webServiceExplorerCO);
			if(null != webServiceExplorerCO)
			{
				xmlRequest = webServiceExplorerCO.getXmlRequest(); 
			}
			else
			{
				xmlRequest = "";
			}		
			this.loadRequest(xmlRequest);			
		} 
		catch (Exception e)
		{
			super.handleException(e, null, null);
		}
	}
	
	/**
	 * @Description process xml request and print formatted xml response in jsp
	 * @calls processXmlRequest
	 */
	public void processRequest() {
		try {
			WebServiceUtil webServiceUtil = WebServiceUtil.getInstance();
			List<RequestResponseCO> modifiedGridRows = webServiceUtil.returnGridParam(webServiceExplorerCO.getWebServiceExplorerGridUpdates());
			webServiceExplorerCO.setLstRequestResposneCO(modifiedGridRows);
			webServiceExplorerCO.setModifiedGridRows(modifiedGridRows);
			//String formattedSOAPResponse = this.processXmlRequest(webServiceExplorerCO.getXmlRequest());
			String formattedSOAPResponse = this.processHttpXmlRequest(webServiceExplorerCO);		
			this.loadResponse(formattedSOAPResponse);
		} 
		catch (Exception e) 
		{
			handleException(e, null, null);
		}
	}
	
	/**
	 * @Description process xml request and return formatted xml response
	 * @param xmlRequest
	 * @return
	 */
	public String processXmlRequest(String xmlRequest) {
		String unformattedXml = null;
		String formattedXml = null;
		String wsdlURL = webServiceExplorerCO.getWsdlUrl();
		// String wsdlURL =
		// "http://192.168.19.31:9080/imal_integration_services_cp_das_o12/pathservices/processApiServicesMessage?wsdl";
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage request = createSOAPRequest(webServiceExplorerCO);
			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(request, wsdlURL);
			// Print the SOAP Response
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			soapResponse.writeTo(b);
			unformattedXml = b.toString();
			formattedXml = this.format(unformattedXml);
			soapConnection.close();
		} 
		catch (Exception e) 
		{
			super.handleException(e, null, null);
		}
		return formattedXml;
	}
	
	/**
	 * @description function to process xml request
	 * it is better than processXmlRequest since it handles all response errors similar to soap ui
	 * @param webServiceExplorerCO
	 */
	private String processHttpXmlRequest(WebServiceExplorerCO webServiceExplorerCO)
	{
		String formattedXml = null;
		String wsdlURL = webServiceExplorerCO.getWsdlUrl();
		//to be removed
//		webServiceExplorerCO.setWsdlUrl("http://lbdm1-malimezzawi:8380/ode/processes/CreateCreditProcess"+"?wsdl");
//		String wsdlURL = webServiceExplorerCO.getWsdlUrl();//"http://lbdm1-malimezzawi:8380/ode/processes/CreateCreditProcess?wsdl";
		String xmlRequest = null;
		String authentication = null;
		try{
			//SOAPMessage request = createSOAPRequest(webServiceExplorerCO);
			WebServiceExplorerCO webServiceExplorerCO1 = generateFormattedXMLSoapRequest(webServiceExplorerCO);
			xmlRequest = webServiceExplorerCO1.getXmlRequest();
			HttpURLConnection conn = null;
			URL url = new URL(wsdlURL);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		    conn.setRequestProperty("Accept", "*/*");		
		    conn.setRequestProperty("SOAPAction", "");
		    if(null != webServiceExplorerCO.getSoapAction() && StringUtil.isNotEmpty(webServiceExplorerCO.getSoapAction()))
		    {
		    	conn.setRequestMethod(webServiceExplorerCO.getSoapAction());
		    }
		    //to be added	
		//	authentication = SecurityUtils.encodeB64((webServiceExplorerCO.getAuthUserID()+":"+webServiceExplorerCO.getAuthPassword()).getBytes());
		//	conn.setRequestProperty("Authorization", "Basic " + authentication);
		    //to be added
		    OutputStream os = conn.getOutputStream();
		    os.write(xmlRequest.getBytes("UTF-8"));
		    os.flush();
//		    if(conn.getResponseCode() == 403 || conn.getResponseCode() == 401)
//		    {
//		    	access denied
//		    }
//		    else
		    	if(conn.getResponseCode() != 200)
		    {
				Scanner errorScanner = new Scanner(conn.getErrorStream(), "UTF-8").useDelimiter("\\A");
				String errorStreamString = errorScanner.next();
				errorScanner.close();
				//throw new Exception(errorStreamString);
				return this.format(errorStreamString);
		    }
		 
		    else
		    {
				Scanner inputScanner = new Scanner(conn.getInputStream(), "UTF-8").useDelimiter("\\A");
				String inputStreamString = inputScanner.next();
				return this.format(inputStreamString);
		    }
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);	
		}
		return null;
	}
	
	/**
	 * @Description: Generate XML Request From JSON INput
	 */
	private WebServiceExplorerCO generateFormattedXMLSoapRequest(WebServiceExplorerCO webServiceExplorerCO)
	{
		try{
			SOAPMessage soapMessage;
			soapMessage = this.createSOAPRequest(webServiceExplorerCO);
						 
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			soapMessage.writeTo(b);
			String unformattedXml = b.toString();
			webServiceExplorerCO.setUnformattedXmlRequest(unformattedXml);
			String formattedXml = this.format(unformattedXml);
			webServiceExplorerCO.setXmlRequest(formattedXml);
		}
		catch(Exception e)
		{
			log.error("error in generateFormattedXMLSoapRequest");
			super.handleException(e, null, null);
		}
		return webServiceExplorerCO;
	}	

	/**
	 * 
	 * @param webServiceExplorerCO
	 * @return
	 * @throws Exception
	 */
	private SOAPMessage createSOAPRequest(WebServiceExplorerCO webServiceExplorerCO) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		createSoapEnvelope(webServiceExplorerCO, soapMessage);
		MimeHeaders headers = soapMessage.getMimeHeaders();
		String authentication = SecurityUtils.encodeB64((webServiceExplorerCO.getAuthUserID()+":"+webServiceExplorerCO.getAuthPassword()).getBytes());
		if (webServiceExplorerCO.getSoapAction() != null && !webServiceExplorerCO.getSoapAction().isEmpty()) 
		{
			headers.addHeader("SOAPAction", webServiceExplorerCO.getSoapAction());
		}
		headers.addHeader("Authorization", "Basic " + authentication);
		soapMessage.saveChanges();		
		return soapMessage;
	}

	/**
	 * 
	 * @param webServiceExplorerCO
	 * @param soapMessage
	 * @throws SOAPException
	 */
	private void createSoapEnvelope(WebServiceExplorerCO webServiceExplorerCO, SOAPMessage soapMessage) throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();
		// String myNamespace = "ser";
		// String myNamespaceURI = "http://services.client.ws.path.com/";
		String nameSpace = webServiceExplorerCO.getNameSpacePrefix();
		String nameSpaceUri = webServiceExplorerCO.getNameSpaceUri();
		if(StringUtil.isEmptyString(StringUtil.nullEmptyToValue(nameSpace, "")))
		{
			nameSpace = "ser";
		}
		if(StringUtil.isEmptyString(StringUtil.nullEmptyToValue(nameSpaceUri, "")))
		{
			nameSpaceUri = "http://services.client.ws.path.com/";
		}
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();	
		envelope.addNamespaceDeclaration(nameSpace, nameSpaceUri);
		envelope.getHeader().addTextNode("1");
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement(webServiceExplorerCO.getOperationName(), nameSpace);
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("request");
		//SOAPElement soapBodyElem1 = soapBodyElem.addChildElement(webServiceExplorerCO.getOperationName());
		for (RequestResponseCO reqRes : webServiceExplorerCO.getModifiedGridRows()) 
		{
			this.createRequest(reqRes, soapBodyElem1);
		}
	}

	/**
	 * @author Raed Saad
	 * @Description Creat SOAP Request
	 */	
	public void createRequest(RequestResponseCO reqResCO, SOAPElement soapElement) throws SOAPException
	{
		SOAPElement sElement = null;
		SOAPElement hashElement = null;
		SOAPElement hashEntry = null;
		if(null != reqResCO.getLstInReqRes() && null == reqResCO.getReqResCO())
		{
			for( RequestResponseCO reqRes : reqResCO.getLstInReqRes())
			{
				createRequest(reqRes,soapElement);
			}
		}
		else if(null != reqResCO.getReqResCO())
		{
			Boolean condition = reqResCO.getFieldType().contains("List<");//case List<Object>
			switch(condition.toString())
			{
				case "true":
					createRequest(reqResCO.getReqResCO(),soapElement);		
					break;
				case "false":
					sElement = soapElement.addChildElement(reqResCO.getFieldName());
					createRequest(reqResCO.getReqResCO(),sElement);	
					break;
			}
		}
		else if(null != reqResCO.getList())
		{
			List<String> list = reqResCO.getList();
			for(String s : list)
			{
				sElement = soapElement.addChildElement(reqResCO.getFieldName()).addTextNode(StringUtil.nullToEmpty(s));
			}
		}
		else if(null != reqResCO.getMap())
		{
			Map<String,String> map = reqResCO.getMap();
			sElement = soapElement.addChildElement(StringUtil.nullToEmpty(reqResCO.getFieldName()));
			for(String key : map.keySet())
			{
				hashEntry = sElement.addChildElement(StringUtil.nullToEmpty(WebServiceExplorerConstant.HASH_MAP_ENTRY));
				hashElement = hashEntry.addChildElement(StringUtil.nullToEmpty(WebServiceExplorerConstant.HASH_MAP_KEY)).addTextNode(key);	
				hashElement = hashEntry.addChildElement(StringUtil.nullToEmpty(WebServiceExplorerConstant.HASH_MAP_VALUE)).addTextNode(map.get(key));
			}
		}
		else if(null != reqResCO.getFieldName())
		{
			QName bodyName = new QName("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi");
			sElement = soapElement.addChildElement(reqResCO.getFieldName()).addTextNode(StringUtil.nullToEmpty(reqResCO.getValue()));
			if(null != reqResCO.getNillable() && true == reqResCO.getNillable())
			{
				//sElement.addAttribute(bodyName, "true");	
			}
		}
    }
	
	/**
	 * @Description generates formatted and unformatted xml requests
	 * @param webServiceExplorerCO
	 * @return
	 */
	private WebServiceExplorerCO generateSoapRequest(WebServiceExplorerCO webServiceExplorerCO)
	{
		WebServiceUtil webServiceUtil = new WebServiceUtil();
		List<RequestResponseCO> modifiedGridRows= webServiceUtil.returnGridParam(webServiceExplorerCO.getWebServiceExplorerGridUpdates());
		if (null != modifiedGridRows) 
		{
			webServiceExplorerCO.setModifiedGridRows(modifiedGridRows);
			try 
			{
				webServiceExplorerCO = this.generateFormattedXMLSoapRequest(webServiceExplorerCO);
				return webServiceExplorerCO;
			}
			catch (Exception e) 
			{
				super.handleException(e, null, null);
			}
		}
		return null;
	}
	
	/**
	 * @description print xml request on screen
	 * @param xmlRequest
	 * @throws IOException
	 */
	private String loadRequest(String xmlRequest) throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(WebServiceExplorerConstant.XML_CONTENT_TYPE);
		PrintWriter printWriter;
		printWriter = response.getWriter();
		if(null != xmlRequest)
		{
			printWriter.println(xmlRequest);
			printWriter.close();
		}
		try{
			response.flushBuffer();
		}
		catch(Exception e)
		{
			log.error("IMCO_ERROR_FLUSHING\n");
		}
		return xmlRequest;
	}
	
	/**
	 * @Description print xml resposne on screen
	 * @param xmlResponse
	 * @throws IOException
	 */
	private void loadResponse(String xmlResponse) throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		if(null != response.getWriter())
		{
			response.setContentType(WebServiceExplorerConstant.XML_CONTENT_TYPE);
			PrintWriter printWriter;
			printWriter = response.getWriter();
			printWriter.print(xmlResponse);
			printWriter.close();
		}
		else if(null != response.getOutputStream())
		{
			response.getOutputStream().write(xmlResponse.getBytes());
			response.getOutputStream().flush();
		}
		else{
		}
	}

	/**
	 * @Description format a string to xml format
	 * @param unformattedXml
	 * @return
	 */
	private String format(String unformattedXml) {
		try {
			Document document = parseXmlFile(unformattedXml);
//			OutputFormat format = new OutputFormat(document);
//			format.setIndenting(true);
//			format.setIndent(3);
			Writer out = new StringWriter();
//			XMLSerializer serializer = new XMLSerializer(out, format);
//			serializer.serialize(document);
			return out.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param in
	 * @return
	 */
	private Document parseXmlFile(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);
		} 
		catch (ParserConfigurationException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (SAXException e) 
		{
			throw new RuntimeException(e);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param soapOutputMessage
	 * @return
	 * @throws Exception
	 */
	private String parseSoapOutputMessage(String soapOutputMessage) throws Exception {
		try {
			InputStream is = new ByteArrayInputStream(soapOutputMessage.getBytes());
			SOAPMessage sm = MessageFactory.newInstance(SOAPConstants.DEFAULT_SOAP_PROTOCOL)
					.createMessage(new MimeHeaders(), is);
			SOAPBody soapBody = sm.getSOAPBody();

			return soapBody.toString();

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String loadShowProcessRequest()
	{
		return "loadRequestResponseSection";
	}
	
	public String loadToolBar()
	{
		return "loadToolBar";
	}
	 
	public List<SelectCO> getApplicationTypeList() {
		return applicationTypeList;
	}

	public void setApplicationTypeList(List<SelectCO> applicationTypeList) {
		this.applicationTypeList = applicationTypeList;
	}

	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}

	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}

	public List<SelectCO> getOperationNameList() {
		return operationNameList;
	}

	public void setOperationNameList(List<SelectCO> operationNameList) {
		this.operationNameList = operationNameList;
	}

	public List<SelectCO> getRequestTypeList() {
		return requestTypeList;
	}

	public void setRequestTypeList(List<SelectCO> requestTypeList) {
		this.requestTypeList = requestTypeList;
	}

	public List<SelectCO> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<SelectCO> restrictions) {
		this.restrictions = restrictions;
	}

	public List<SelectCO> getMappingValues() {
		return mappingValues;
	}

	public void setMappingValues(List<SelectCO> mappingValues) {
		this.mappingValues = mappingValues;
	}

	public void setWebServiceExplorerBO(WebServiceExplorerBO webServiceExplorerBO) {
		this.webServiceExplorerBO = webServiceExplorerBO;
	}

	public WebServiceExplorerGridParamCO getWebServiceExplorerGridParamCO() {
		return webServiceExplorerGridParamCO;
	}


	public void setWebServiceExplorerGridParamCO(WebServiceExplorerGridParamCO webServiceExplorerGridParamCO) {
		this.webServiceExplorerGridParamCO = webServiceExplorerGridParamCO;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}

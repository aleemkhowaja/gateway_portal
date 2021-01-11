package com.path.actions.common.webserviceexplorer;

import java.io.File;
import java.io.IOException;

/**
 * @Auther:Raed Saad
 * @Date:MARCH 2018
 * @Team: PEMTS JAVA Team.
 * @copyright: PathSolution 2018
 * @User Story: USER STORY #653853  WSDL explorer
 * @Description: Web Service List Tree Action Class
 **/

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

//import org.apache.tools.ant.Project;
//import org.apache.tools.ant.taskdefs.Expand;
import org.xml.sax.SAXException;

import com.path.bo.common.webserviceexplorer.WebServiceExplorerBO;
import com.path.bo.common.webserviceexplorer.WebServiceExplorerConstant;
//import com.path.codegen.common.lib.exception.GeneratorException;
//import com.path.codegen.webservicegen.parser.BPELParser;
//import com.path.codegen.webservicegen.parser.GenWSDLParser;
//import com.path.codegen.webservicegen.parser.ZipFileParser;
//import com.path.codegen.webservicegen.pojo.BPELZipHolder;
import com.path.lib.common.util.FileUtil;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.lib.common.base.TreeBaseAction;
import com.path.vo.common.SessionCO;
import com.path.vo.common.tree.TreeNode;
import com.path.vo.common.tree.TreeNodeCO;
import com.path.vo.common.webserviceexplorer.WebServiceExplorerCO;
import com.path.vo.common.webserviceexplorer.WebServiceUtil;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class WebServiceExplorerTreeAction extends TreeBaseAction{	
	private TreeNodeCO treeNodeCO;
	private WebServiceUtil webServiceUtil;
	private WebServiceExplorerBO webServiceExplorerBO;
	private WebServiceExplorerCO webServiceExplorerCO;
	
	public String loadWebServicesTree() {
		SessionCO sessionCO = returnSessionObject();
		try {
			Map<String,String> endPointAddress = new HashMap<String,String>();
			List<TreeNodeCO> treeNodes = new ArrayList<TreeNodeCO>();
			TreeNodeCO treeNodeCO = null;			
			webServiceUtil = new WebServiceUtil();
//			String type = webServiceExplorerCO.getFilterAppName();// to retrieve the app name
//			String appName = webServiceExplorerCO.getFilterPwsType();// to retrieve the filter
			List<String> typeList = new ArrayList<String>();
			List<String> appNameList = new ArrayList<String>();
			
//			List<String> applications = webServiceUtil.getApplicationNames(WebServiceExplorerConstant.PROPERTY_FILE_NAME,type);
//			Map<String,String> applicationsHashMap = webServiceUtil.getApplicationNameAndServerPATHMap(WebServiceExplorerConstant.PROPERTY_FILE_NAME,type,appName);
			String filter = null;
			Object obj = null;
			filter = webServiceExplorerCO.getFilter();

			obj = filter;
			JSONObject jsonObject = null;
			List<JSONObject> jsonLst = null;
			if(null != obj && !StringUtil.nullToEmpty(obj).isEmpty())
			{
				if(obj instanceof JSONObject)
				{
					jsonObject = (JSONObject) JSONSerializer.toJSON(filter);
					jsonLst = new ArrayList<JSONObject>();
					jsonLst.add(jsonObject);
				}
				else{
					jsonLst = (List<JSONObject>) JSONSerializer.toJSON(filter);
				}
				for(JSONObject j : jsonLst)
				{
					for(Object keyObj : j.keySet())
					{
						appNameList.add(keyObj.toString());
						
						obj = j.get(keyObj);
						String[] arr = obj.toString().split(",");
						for(int i = 0;i<arr.length;i++)
						{
							typeList.add(arr[i]);
						}
					}
				}
			}
//			Map<String,String> applicationsHashMap = webServiceUtil.getApplicationNameAndServerPATHMap(WebServiceExplorerConstant.PROPERTY_FILE_NAME);
			
			//Map<String,String> applicationsHashMap = webServiceUtil.getApplicationNameAndServerPATHMap(WebServiceExplorerConstant.PROPERTY_FILE_NAME,typeList,appNameList);

			List<HashMap<String,String>> licationNameHashMap = webServiceUtil.getApplicationNameAndServerPATHMap(WebServiceExplorerConstant.PROPERTY_FILE_NAME,typeList,appNameList);
			Map<String,List<String>> applicationEndpointsAndOperations = null;
			Map<String,HashMap<String,List<String>>> nestedHash;
			List<String> operationsList = null;
			String endPointKey = null;
			String operationKey = null;
			Map<String,Object> serviceNameSpace = new HashMap<String,Object>();
		//	int i = 0;
			
			for(HashMap<String,String> applicationsHashMap : licationNameHashMap)
			{
				for(String key : applicationsHashMap.keySet())
				{
					//create application name nodes
					treeNodeCO = new TreeNodeCO();
					treeNodeCO.setNodeCode(key);
					treeNodeCO.setTitle(key);
					treeNodeCO.setNodeOrder(BigDecimal.ZERO);
					treeNodes.add(treeNodeCO);
					nestedHash = webServiceUtil.getWebServiceEndPointsWithInfo(applicationsHashMap.get(key));
					if(null == nestedHash)
					{
						continue;
					}
					applicationEndpointsAndOperations = new HashMap<String,List<String>>();
	
					for(String k : nestedHash.keySet())
					{
						applicationEndpointsAndOperations.put(k, nestedHash.get(k).get("operations"));
						serviceNameSpace.put(k, nestedHash.get(k).get("NameSpaceUri").get(0));
					}
					//applicationEndpointsAndOperations = webServiceUtil.getWebServiceEndPointsHashMap(applicationsHashMap.get(key));
					int i = 0;
					for(String endPoint : applicationEndpointsAndOperations.keySet())
					{
						endPointAddress.put(endPoint, nestedHash.get(endPoint).get("Address").get(0));
	//					endPointKey = key + "_" + endPoint;
						endPointKey = key + "_" + serviceNameSpace.get(endPoint).toString();
						// create endpoints nodes
						treeNodeCO = new TreeNodeCO();
						treeNodeCO.setNodeCode(endPointKey);
	//					treeNodeCO.setTitle(endPoint);
						treeNodeCO.setTitle(serviceNameSpace.get(endPoint).toString());
						treeNodeCO.setParentNodeCode(key);
						treeNodeCO.setNodeOrder(BigDecimal.ONE);
						treeNodes.add(treeNodeCO);					
						operationsList = applicationEndpointsAndOperations.get(endPoint);
						int i1 = 0;
						for(String operation : operationsList)
						{
							// create operations node
							operationKey = endPointKey + "_" + operation;
							treeNodeCO = new TreeNodeCO();
							treeNodeCO.setNodeCode(operationKey);
							treeNodeCO.setTitle(operation);
							treeNodeCO.setParentNodeCode(endPointKey);
							treeNodeCO.setNodeOrder(new BigDecimal(2));
							treeNodes.add(treeNodeCO);	
						}
					}
				}
			}
			setNodes(treeNodes,TreeNode.NODE_STATE_CLOSED); 
		} catch (Exception e) {
			handleException(e, null, null);
		}
		return "JSON_TREE";
	}
	
	/**
	 * @description load web wsdl as tree
	 * @input wsdlUrl
	 * @return
	 */
	public String loadWsdlWebServicesTree()
	{
		try{
			Map<String,List<String>> map = null;
			if(webServiceExplorerCO.getAdapterType().equalsIgnoreCase("wsdl"))
			{
				map = (Map<String,List<String>>)webServiceExplorerBO.loadWsdlOperations(webServiceExplorerCO);
			}
			else{
				String wsdlName = webServiceExplorerCO.getBpelFileName().replace(".zip", ".wsdl");
				String bpelFile = webServiceExplorerCO.getBpelFileName();
				String bpelFileLoc = FileUtil.getFileURLByName("repository")+File.separator+"bpel"+File.separator;
				String tempWsdlRep = FileUtil.getFileURLByName("repository")+File.separator+"bpel"+File.separator+webServiceExplorerCO.getBpelFileName().replace(".zip", "");
				String wsdlUrl = tempWsdlRep+File.separator+wsdlName;
				webServiceExplorerCO.setWsdlUrl(wsdlUrl);
				webServiceExplorerCO.setBpelFullPath(bpelFileLoc + webServiceExplorerCO.getBpelFileName());
				this.unzip(bpelFileLoc+File.separator+bpelFile, tempWsdlRep);
//				webServiceExplorerCO = this.processBPELZip(webServiceExplorerCO.getBpelFullPath());
				map = new HashMap<String,List<String>>();
				map.put(webServiceExplorerCO.getServiceName(), webServiceExplorerCO.getOperations());
			}

			List<TreeNodeCO> treeNodes = new ArrayList<TreeNodeCO>();
			String endpoint = map.keySet().toArray()[0].toString();
			TreeNodeCO treeNodeCO = null;
			
			//create application name nodes
			String appKey = "RET";
			treeNodeCO = new TreeNodeCO();
			treeNodeCO.setNodeCode(appKey);
			treeNodeCO.setTitle(appKey);
			treeNodeCO.setNodeOrder(BigDecimal.ZERO);
			treeNodes.add(treeNodeCO);
			
			List<String> operationsList = (List<String>)map.get(endpoint);

			// create endpoints nodes
			String endPointKey = appKey + "_" + endpoint;

			treeNodeCO = new TreeNodeCO();
			treeNodeCO.setNodeCode(endPointKey);
			treeNodeCO.setTitle(endpoint);
			treeNodeCO.setParentNodeCode(appKey);
			treeNodeCO.setNodeOrder(BigDecimal.ONE);
			treeNodes.add(treeNodeCO);	
			String operationKey = null;
			for(String operation : operationsList)
			{
				// create operations node
				operationKey = endPointKey + "_" + operation;
				treeNodeCO = new TreeNodeCO();
				treeNodeCO.setNodeCode(operationKey);
				treeNodeCO.setTitle(operation);
				treeNodeCO.setParentNodeCode(endPointKey);
				treeNodeCO.setNodeOrder(new BigDecimal(2));
				treeNodes.add(treeNodeCO);	
			}
			setNodes(treeNodes,TreeNode.NODE_STATE_CLOSED);
		}
		catch(Exception e)
		{
			super.handleException(e, null, null);
		}
		return "JSON_TREE";
	}
	
	/**
	 * @param zipFilepath
	 * @param destinationDir
	 */
	
	public static void unzip(String zipFilepath, String destinationDir) 
	{
//		//@TODO to be used from generator latest api
//	    final class Expander extends Expand {
//	        public Expander() {
//	            Project antp = new Project();
//	            this.setProject(antp);
//	        }
//	    }
//	    File source = new File(zipFilepath);
//	    String folder = source.getName().substring(0,source.getName().indexOf('.'));
//
//	    Expander expander = new Expander();
//	    expander.setSrc(source);
//	    expander.setDest(new File(destinationDir));	    
//	    expander.setOverwrite(true);
//	    expander.execute();
	}
	
	/**
	 * @throws Exception
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws GeneratorException
	 */
	public WebServiceExplorerCO processBPELZip(String wsdlUrl) throws Exception, ParserConfigurationException, SAXException,
			IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException,Exception {
//		ZipFileParser zipFileParse = ZipFileParser.newInstance();
//		zipFileParse.parse(wsdlUrl);
//		BPELZipHolder bpelZipHolder = ZipFileParser.bpelZipHolder;
//		BPELParser bpelParser = BPELParser.newInstance();
//		bpelParser.setBpelZipHolder(zipFileParse.bpelZipHolder);
//		webServiceExplorerCO.setZipData(bpelZipHolder.getZipData());
//		webServiceExplorerCO.setZipDataAsString(bpelZipHolder.getZipDataAsString());
//
//		ZipFile zip = zipFileParse.bpelZipHolder.getZipFile();
//		ZipEntry entry = zip.getEntry(zipFileParse.bpelZipHolder.getZipDataEntry()
//				.get(zipFileParse.bpelZipHolder.getBpelFileName() + ".bpel"));
//		bpelParser.parse(zip.getInputStream(entry));
//		entry = zip.getEntry(zipFileParse.bpelZipHolder.getZipDataEntry().get(bpelParser.getBpelWsdlLocation()));
//		String x = zip.getName().replace("\\", "xox");
//		String arrStr[] = x.split("xox");
//		String bpelName = arrStr[arrStr.length - 1];
//		String location = zip.getName().replace(bpelName, "");
//		x = x.replace("xox", "\\").replace(bpelName, bpelParser.getWsdlLocation());
//		String n = "<import location=\"";
//		String n1 = n + location + bpelName.replace(".zip", "") + File.separator;
//		InputStream inputSream = zip.getInputStream(entry);
//		String str = ZipFileParser.getStringFromInputStream(inputSream);
//		String loc = zip.getName().replace(bpelName, bpelName.replace(".zip", "").replace(".rar", ""));
//		str = str.replace(n, n1);
//		this.getWebServiceExplorerCO().setWsdl(str);
//		GenWSDLParser.newInstance().fillData(org.apache.commons.io.IOUtils.toInputStream(str, "UTF-8"));
//		String serviceName = GenWSDLParser.serviceName;
//		
//		List<String> operations = GenWSDLParser.operations;
//		webServiceExplorerCO.setServiceName(serviceName);
//		webServiceExplorerCO.setOperations(operations);
//		webServiceExplorerCO.setMainWsdl(str);
//		webServiceExplorerCO.setMainWsdlName(bpelZipHolder.getBpelWsdl());
		return webServiceExplorerCO;
	}
	
	public TreeNodeCO getTreeNodeCO() {
		return treeNodeCO;
	}

	public void setTreeNodeCO(TreeNodeCO treeNodeCO) {
		this.treeNodeCO = treeNodeCO;
	}

	public WebServiceUtil getWebServiceUtil() {
		return webServiceUtil;
	}

	public void setWebServiceUtil(WebServiceUtil webServiceUtil) {
		this.webServiceUtil = webServiceUtil;
	}
	public WebServiceExplorerCO getWebServiceExplorerCO() {
		return webServiceExplorerCO;
	}
	public void setWebServiceExplorerCO(WebServiceExplorerCO webServiceExplorerCO) {
		this.webServiceExplorerCO = webServiceExplorerCO;
	}

	public WebServiceExplorerBO getWebServiceExplorerBO() {
		return webServiceExplorerBO;
	}

	public void setWebServiceExplorerBO(WebServiceExplorerBO webServiceExplorerBO) {
		this.webServiceExplorerBO = webServiceExplorerBO;
	}

}
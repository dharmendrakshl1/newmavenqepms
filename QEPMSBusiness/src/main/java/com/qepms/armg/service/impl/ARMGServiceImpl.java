package com.qepms.armg.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aspose.words.Document;
import com.qepms.armg.service.ARMGService;
import com.qepms.business.service.EmployeeViewService;
import com.qepms.business.service.impl.EmployeeViewServiceImpl;
import com.qepms.common.pdfConversion;
import com.qepms.common.util.CopyUtil;
import com.qepms.common.util.CustomDate;
import com.qepms.common.util.EmployeeSubmissionStatus;
import com.qepms.common.util.ManagerSubmissionStatus;
import com.qepms.common.util.SkillType;
import com.qepms.data.dao.employee.EmployeeMasterDAO;
import com.qepms.data.dao.employee.ResumeDAO;
import com.qepms.data.employee.Education;
import com.qepms.data.employee.EmployeeMaster;
import com.qepms.data.employee.Project;
import com.qepms.data.employee.Resume;
import com.qepms.data.employee.Skill;
import com.qepms.infra.constants.CommonConstants;
import com.qepms.infra.dto.armg.EducationDTO;
import com.qepms.infra.dto.armg.EmployeeMasterDTO;
import com.qepms.infra.dto.armg.ProjectDTO;
import com.qepms.infra.dto.armg.ResumeDTO;
import com.qepms.infra.dto.armg.SkillDTO;



@Transactional
public class ARMGServiceImpl implements ARMGService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ARMGServiceImpl.class);
	
	@Autowired
	private ResumeDAO resumeDAO;
	
	@Autowired
	private EmployeeMasterDAO employeeMasterDAO;
	
	@Autowired
	private EmployeeViewService employeeViewService;
	

	@Override
	public List<ResumeDTO> getResumeList() 
	{
		LOG.debug("armg getResumeList() service called ");
		List<ResumeDTO> resumeDTOList=new ArrayList<ResumeDTO>();
		ResumeDTO resumeDTO=null;
		try{
			EmployeeMasterDTO employeeMasterDTO=null;
			//get all the employees from DB
			List<EmployeeMaster> employees = employeeMasterDAO.getAllEmployees();
			
			//get all the resume ordered by date and desc
			List<Resume> resumeListWithDuplicates=resumeDAO.getLatestResumeList(employees,ManagerSubmissionStatus.APPROVED.name());
			
			//Only the latest resume eliminating duplicates
			boolean present = false;
			List<Resume> resumeList= new ArrayList<Resume>();
			
			for (Resume resumeDuplicate : resumeListWithDuplicates) {
				
				for (Resume resume : resumeList) 
				{
					if(resumeDuplicate.getEmployeeMaster().equals(resume.getEmployeeMaster()))
					{
						present=true;
					}
				       	
				}
				if(!present)
				{
					resumeList.add(resumeDuplicate);
					
				}
				present=false;
				
			}
			
			//List<Resume> resumeList=resumeDAO.getResumeList();
			Iterator resumeitr=resumeList.iterator();
			while(resumeitr.hasNext())
			{
				Resume resume=(Resume)resumeitr.next();
				resumeDTO=new ResumeDTO();
				
				/*
				 * setting employee details
				 */
				employeeMasterDTO=new EmployeeMasterDTO();
				employeeMasterDTO.setName(resume.getEmployeeMaster().getName());
				employeeMasterDTO.setEmpId(resume.getEmployeeMaster().getEmpId());
				resumeDTO.setEmployeeMaster(employeeMasterDTO);
				resumeDTO.setResumeid(resume.getResumeid());
				
				List<SkillDTO> skillDTOList =new ArrayList<SkillDTO>();
				SkillDTO sillDTO=null;
				Set<Skill> skillList = resume.getSkills();
				Iterator skillItr=skillList.iterator();
				while(skillItr.hasNext())
				{
					Skill skill=(Skill)skillItr.next();
					if(skill.getSkillCatagory().equals(SkillType.PRIMARY.name())||skill.getSkillCatagory().equals(SkillType.SECONDARY.name()))
					{
						sillDTO = new SkillDTO();
						sillDTO.setSkillType(skill.getSkillType());
						sillDTO.setSkillCatagory(skill.getSkillCatagory());
						sillDTO.setSkill(skill.getSkill());
						skillDTOList.add(sillDTO);
					}
				}
				resumeDTO.setSkills(skillDTOList);
				
				resumeDTOList.add(resumeDTO);
				 
			}
		}
		catch(Exception e)
		{
			LOG.debug("Exception in getResumeList() of armg service;"+e);
			e.printStackTrace();
		}
		return resumeDTOList;
	}

	@Override
	public String uploadResume(String filePath,EmployeeMasterDTO employeeMasterDTO) 
	{	
		LOG.debug("armg uploadResume() called");
		try
		{
		//creating persistence object
		Resume resumePOJO = new Resume();
		resumePOJO.setCreatedDate(CustomDate.getCustomDate());
		
		//upload the file 
		//String filePath=uploadToServerFolder(file);
		
		//convert file to html
		if(!filePath.isEmpty())
		{
			convertDocxToHTML(filePath);
		}
		
		//parse html
		ResumeDTO resumeDTO = readHTMLAndParseData(employeeMasterDTO);
		
		
		if(resumeDTO==null)
		{
			return CommonConstants.UPLOAD_FAILURE_STATUS + "\t" +"Invalid file";
		}
//		System.out.println("File Path is:::::::::::::::::"+filePath);
		//for server while uploading as jar
		String fileName=filePath.substring(filePath.lastIndexOf("/")+1);
		//for local system uncomment below and comment above
		//String fileName=filePath.substring(filePath.lastIndexOf("\\")+1);
		LOG.debug("upload functionality filename:"+fileName);
		
//		System.out.println("File Name is:::::::::::::::::"+fileName);
		String []fileNameParts =fileName.split("_");
		int empId = Integer.parseInt(fileNameParts[0]);
		
//		System.out.println("Emp Id is:::::::::::::::::::::::"+empId);
		EmployeeMaster employeeDetail = employeeMasterDAO.findById(empId);
		if(employeeDetail==null)
		{
			return CommonConstants.UPLOAD_FAILURE_STATUS + "\t" +"User Not Registered";
		}
		resumePOJO.setEmployeeMaster(employeeDetail);
		//create the persistence object 
		Resume resume=CopyUtil.copyDTOtoPOJO(resumeDTO,resumePOJO);
		resume.setManagerApprovalStatus(ManagerSubmissionStatus.APPROVED.name());
		resume.setCreatedDate(CustomDate.getCustomDate());
		resume.setUpdatedDate(CustomDate.getCustomDate());
		resume.setComments("Uploaded by RMG Team");
		if(resume.getDomain() == null){
			resume.setDomain(" ");
		}
		resume.setApprovedDate(CustomDate.getCustomDate());
		EmployeeMaster employee = resume.getEmployeeMaster();
		/*EmployeeMaster employeeDTO= employeeMasterDAO.findByName(resumeDTO.getEmployeeMaster().getName()); 
		employee.setEmpId(employeeDTO.getEmpId());
		employee.setEmployeeMail(employeeDTO.getEmployeeMail());*/
		
		
		//persist object
		
			employeeMasterDAO.attachDirty(employee);
			return CommonConstants.UPLOAD_SUCCESS_STATUS;
		}
		catch(Exception e)
		{
			LOG.debug("Upload resume service failure:"+e);
			e.printStackTrace();			
			return CommonConstants.UPLOAD_FAILURE_STATUS;
			
		}
		
	}

	

    /*
     * sub methods used for uploading resume	
     */
   // upload the docx file from UI to server folder
	/*private String uploadToServerFolder(MultipartFile file) {
	
		 if (!file.isEmpty()) {
	            try {
	                byte[] bytes = file.getBytes();
	                System.out.println("fileNmae="+file.getOriginalFilename());
	                // Creating the directory to store file
	               // String rootPath = System.getProperty("catalina.home");
	                String rootPath = CommonConstants.UPLOAD_DIRECTORY;
	                File dir = new File(rootPath);
	                if (!dir.exists())
	                    dir.mkdirs();
	 
	                // Create the file on server
	                File serverFile = new File(dir.getAbsolutePath()
	                        + File.separator + file.getOriginalFilename());
	                BufferedOutputStream stream = new BufferedOutputStream(
	                        new FileOutputStream(serverFile));
	                stream.write(bytes);
	                stream.close();
	 
	                LOG.info("Server File Location="
	                        + serverFile.getAbsolutePath());
	 
	                return serverFile.getAbsolutePath();
	            } catch (Exception e) {
	                return null;
	            }
	        } else {
	            return null;
	        }
		

		
	}*/
	
	//convert the docx file uploaded to html
	private void convertDocxToHTML(String filePath) {
			
			try 
			{
				Document doc = new Document(filePath);
				doc.save(CommonConstants.UPLOAD_DIRECTORY+"\\Generatedresume.html");
			} 
			catch (Exception e) 
			{
				LOG.debug("convertDocxToHTML failed. Details"+e);
				e.printStackTrace();
			}
			
		}
	
	//read the converted html and parse data
	
	private ResumeDTO readHTMLAndParseData(EmployeeMasterDTO employeeMasterDTO)
	{
		ResumeDTO resumeDTO=null;
		try{
			//getting header from header xml
			Map<String,String> headerMap=obtainHeaderFromXML();
			String htmlToParse=obtainHtmlFromSource();
			System.out.println("readHTMLAndParseData: "+htmlToParse);
			resumeDTO=parseResumeDTOFromHTMLString(htmlToParse,headerMap,employeeMasterDTO);
		}
		catch(Exception e)
		{
			LOG.debug("readHTMLAndParseData failed. Details:" +e);
			e.printStackTrace();
		}
			return resumeDTO;
		}
		
	

		/*
	     * end sub methods used for uploading resume	
	     */
	
	private Map<String, String> obtainHeaderFromXML() {
		// TODO Auto-generated method stub
	   Map<String,String> headerList= new HashMap<String,String>();
	   //Get the DOM Builder Factory
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   //Get the DOM Builder
	   try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//Load and Parse the XML document
		    //document contains the complete XML as a Tree.
			org.w3c.dom.Document document = builder.parse(new File(CommonConstants.UPLOAD_DIRECTORY+"//Headers.xml"));
		    //Iterating through the nodes and extracting the data.
		    NodeList nodeList = document.getDocumentElement().getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        //We have encountered an <header> tag.
		        Node node = nodeList.item(i);
		        if (node instanceof org.w3c.dom.Element) {
		        
		        	headerList.put(node.getTextContent(), node.getNodeName());
		        }
		    }
		 
		} 
	   catch (ParserConfigurationException e) {
		   LOG.debug("ParserConfigurationException obtainHeaderFromXML:"+e);
			e.printStackTrace();
		}
	    catch (SAXException e) {
	    	LOG.debug("SAXException obtainHeaderFromXML:"+e);
			e.printStackTrace();
		} catch (Exception e) {
			LOG.debug("Exception at obtainHeaderFromXML:"+e);
			e.printStackTrace();
		}

		return headerList;
	}
	
	private String obtainHtmlFromSource() {
		// TODO Auto-generated method stub
	     BufferedReader br = null;
	     String htmlToParse="";
	     try 
	     {
	            String sCurrentLine;
	            br = new BufferedReader( new FileReader(CommonConstants.UPLOAD_DIRECTORY+"\\Generatedresume.html"));
	            while ((sCurrentLine = br.readLine()) != null)
	            {
	               htmlToParse = htmlToParse + sCurrentLine;
	            }
   	     } 
	     catch (Exception e) 
   	        {
	    	 	LOG.debug("Exception obtainHtmlFromSource:"+e);
	            e.printStackTrace();
	        }
	     finally
	     {
	            try 
	            {
	                if (br!= null)
	                    br.close();
	            } 
	            catch (IOException ex)
	            {
	            	LOG.debug("IOException obtainHtmlFromSource:"+ex);
	            	ex.printStackTrace();
	            }
	        }
	    
		return htmlToParse;
	}


	
	private ResumeDTO parseResumeDTOFromHTMLString(String htmlToParse,Map<String, String> headerMap,EmployeeMasterDTO employeeMasterDTO) 
{
	
	   org.jsoup.nodes.Document doc = Jsoup.parse(htmlToParse);
	   org.jsoup.select.Elements elements = doc.body().select("*");
	   
	   ResumeDTO resumeDTO= new ResumeDTO();
	// EmployeeMasterDTO employeeMasterDTO=new EmployeeMasterDTO();
	   List<SkillDTO> skillDTOList=new ArrayList<SkillDTO>();
	   List<EducationDTO> educationDTOList= new ArrayList<EducationDTO>();
	   List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();
	   DecimalFormat dec = new DecimalFormat();
	   dec.setParseBigDecimal(true);
	   SkillDTO skillDTO= null;
	   EducationDTO educationDTO = new EducationDTO();
	   ProjectDTO projectDTO = null;
try{
	   List<org.jsoup.nodes.Element> parts = new ArrayList<org.jsoup.nodes.Element>();
	   for (org.jsoup.nodes.Element e : elements)
	   {
	            parts.add(e);
	            
	   }
	   for(int i=0;i<parts.size();i++)
	   {
	        if(parts.get(i).tagName().equalsIgnoreCase("p"))
	        {
	        	System.out.println("Hi1: "+parts.get(i).text());
	       
	        for (Map.Entry<String, String> entry : headerMap.entrySet())
	        {
	        	if(parts.get(i).text().contains(entry.getKey())) 
	        	{
	        		switch(entry.getValue())
	        		{
	   
	        		case "Title":
	        			//System.out.println(parts.get(i).text());
	        			employeeMasterDTO.setTitle(replaceSpecialChar(parts.get(i).text().split(":")[1]));
	        			//System.out.println(parts.get(i).text().split(":")[1]);
	        			break;
	    		
	        		case "FullName":
	        			//System.out.println(parts.get(i).text());
	        			employeeMasterDTO.setName(replaceSpecialChar(parts.get(i).text().split(":")[1]));
	        			break;
	    		
	        		case "TotalExperience":
	        			//System.out.println(parts.get(i).text());
	        			resumeDTO.setTotalExperience(replaceSpecialChar(parts.get(i).text().split(":")[1]));
	        			break;
	    		
	        		case "RelevantExperience":
	        			// System.out.println(parts.get(i).text());
	        			resumeDTO.setRelevantExperience(replaceSpecialChar(parts.get(i).text().split(":")[1]));
	        			break;
	       		
	        		case "Domain":
	        			//System.out.println(parts.get(i).text());
	        			String[] domain = parts.get(i).text().split(":");
	        			if(domain.length > 1){
	        				resumeDTO.setDomain(replaceSpecialChar(domain[1]));
	        			}else{
	        				resumeDTO.setDomain(" ");
	        			}
	        			System.out.println("domain in armg:"+resumeDTO.getDomain());
	        			break;
	        			
	        		case "Certifications":
	        			//System.out.println(parts.get(i).text());
	        			String[] certification = parts.get(i).text().split(":");
	        			if(certification.length > 1){
	        				resumeDTO.setCertification(replaceSpecialChar(certification[1]));
	        			}else{
	        				resumeDTO.setCertification(" ");
	        			}
	        			break;
	        			
	        		case "PrimarySkills":
	        			 //System.out.println(parts.get(i).text());
			    		skillDTO= new SkillDTO();
			    		skillDTO.setSkillCatagory(SkillType.PRIMARY.name());
			    		skillDTO.setCreatedDate(CustomDate.getCustomDate());
			    		skillDTO.setSkill(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    		skillDTO.setUpdatedDate(CustomDate.getCustomDate());
			    		skillDTOList.add(skillDTO);
			    		break;
			    		
	        		case "SecondarySkills":
			    		//System.out.println(parts.get(i).text());
			    		skillDTO= new SkillDTO();
			    		skillDTO.setSkillCatagory(SkillType.SECONDARY.name());
			    		skillDTO.setCreatedDate(CustomDate.getCustomDate());
			    		skillDTO.setSkill(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    		skillDTO.setUpdatedDate(CustomDate.getCustomDate());
			    		skillDTOList.add(skillDTO);
			    		break;
	    	
	        		case "ProfessionalSummary":
			    	 // System.out.println(parts.get(i+2).text());
			    	   
			    	   //break;
	        			i++;
						String professionalSummary="start";
						while(!parts.get(i).text().contains("Technical Skills"))
						    {
			   			 if(parts.get(i).tagName().equalsIgnoreCase("p") || parts.get(i).tagName().equalsIgnoreCase("li"))
			   			 {
			   				 if(professionalSummary.trim().equalsIgnoreCase("start"))
			   				 {
			   					professionalSummary = parts.get(i).text().trim();
			   				 }
			   				 else
			   				 {
			   					professionalSummary=(professionalSummary.trim()+ " \r\n " + parts.get(i).text().trim().trim()).trim();
			   				 }
							 }
							 i++;
						     }
						i--;
						//System.out.println(professionalSummary);
						if(professionalSummary.substring(professionalSummary.length() - 6, professionalSummary.length()).contains("\r\n ")){
							professionalSummary.substring(professionalSummary.length() - 6, professionalSummary.length()).replace("\r\n","");
						}
						resumeDTO.setProfessionalSummary(professionalSummary);
			//code end.................... 
						
						break;

	    	   
	        	    case "ClientName":
			    	   projectDTO = new ProjectDTO();
			    	   projectDTO.setClientName(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    	    //System.out.println("ClientName="+parts.get(i).text());
			    	   break;
	    	   
	        	    case "Duration":
			    	   projectDTO.setDuration(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    	  //System.out.println("Duration="+parts.get(i).text());
			    	   break;
	    	   
	        	    case "ProjectName":
			    	   projectDTO.setProjectName(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    	   //System.out.println("ProjectName="+parts.get(i).text());
			    	   break;
	    	   
	        	    case "Role":
			    	   projectDTO.setRole(replaceSpecialChar(parts.get(i).text().split(":")[1]));
			    	  // System.out.println("Role="+parts.get(i).text());
			    	   break;
	    	   
	        	    case "ProjectBrief":
//				    	System.out.println("Case start--------------------------------------------------------------------->");
						i++;
						String brief="start";
						 while(!parts.get(i).text().contains("Responsibilities:"))
						    {
			   			 if(parts.get(i).tagName().equalsIgnoreCase("p"))
			   			 {
			   				 if(brief.trim().equalsIgnoreCase("start"))
			   				 {
			   					 brief = parts.get(i).text().trim();
			   					 //System.out.println(brief);
			   				 }
			   				 else
			   				 {
			   					brief=brief.trim()+parts.get(i).text().trim();
			   				 }
							 // System.out.println(parts.get(i).text());
			   			 }
							 i++;
						     }
						 i--;
						projectDTO.setProjectDesc(replaceSpecialChar(brief));
						//System.out.println("Case end--------------------------------------------------------------------->");
						   break;
	        	 case "Responsibilities":
			    	//-- code added by Mukunda to get all the data in responsibilites	
					i++;
					String responsibilities="start";
					while(!parts.get(i).text().contains("Environment:"))
					    {
		   			 if(parts.get(i).tagName().equalsIgnoreCase("p") || parts.get(i).tagName().equalsIgnoreCase("li"))
		   			 {
		   				 if(responsibilities.trim().equalsIgnoreCase("start"))
		   				 {
		   					responsibilities = parts.get(i).text().trim();
		   				 }
		   				 else
		   				 {
		   					StringBuilder res = new StringBuilder();
		   					res.append(responsibilities.toString()+"\r\n ");
		   					responsibilities=responsibilities.trim()+parts.get(i).text().trim();
		   				 }
						 }
						 i++;
					     }
					i--;
					//System.out.println(responsibilities);
		//code end.................... 
					projectDTO.setResponsibility(replaceSpecialChar(responsibilities));
					break;

	        	 case "Environment":
			    	//System.out.println("Environment="+parts.get(i).text());
			    	projectDTO.setEnvironment(parts.get(i).text().split(":")[1]);
			    	projectDTO.setCreatedDate(CustomDate.getCustomDate());
			    	projectDTO.setUpdatedDate(CustomDate.getCustomDate());
			    	projectDTOList.add(projectDTO);
			    	break;
			   
			     case "TechnicalSkills":
			    	String html =parts.get(i+2).html();
			    	org.jsoup.nodes.Document docTable = Jsoup.parse(html);
			    	org.jsoup.select.Elements elementsByTag = docTable.select("table");
			    	for (org.jsoup.nodes.Element e : elementsByTag)
			    	{
			   
			    		org.jsoup.select.Elements rows = e.getElementsByTag("tr");
			    		for(org.jsoup.nodes.Element row : rows) {
			    		String Test = row.getElementsByTag("td").get(0).text();
			    		String Result = row.getElementsByTag("td").get(1).text();
			    		skillDTO= new SkillDTO();
			    		skillDTO.setSkillType(Test);
			    		skillDTO.setCreatedDate(CustomDate.getCustomDate());
			    		skillDTO.setUpdatedDate(CustomDate.getCustomDate());
			    		skillDTO.setSkillCatagory(SkillType.TECHNICAL.name());
			    		skillDTO.setSkill(Result);
			    		skillDTOList.add(skillDTO);
			        //   System.out.print(Test+"\t");
			        //   System.out.println(Result);
			           
			    		}
			    	}
			    	break;
	    
			    case "Education":
			    	
			    	//System.out.println("Education Details Start");
					
					//-- code added by Mukunda to get all the data in education	
					i++;
					for(int j=i; j<parts.size();j++)
					{
						if(parts.get(j).tagName().equalsIgnoreCase("p") || parts.get(j).tagName().equalsIgnoreCase("li"))
						{
							if(!parts.get(j).text().contains("Confidential Document")){
								if(!replaceSpecialChar(parts.get(j).text()).trim().isEmpty())
								{	
									//System.out.println(parts.get(j).text().trim());
									educationDTO = new EducationDTO();

									educationDTO.setCollege(" ");
									educationDTO.setDegree(" ");
									educationDTOList.add(educationDTO);
									//System.out.println(educationDTOList);
								}
							}
							
							
							//System.out.println(educationDTOList);
						}
						
					}
					
		//code end....................
					/*educationDTOList.add(educationDTO);*/
			    	
	        		}//END OF SWITCH
	        	}//END OF CONTAINS KEY
	        }//END OF FOR HEADER MAP
	     }//END OF IF PARAGRAPH
	}//END OF FOR EACH PARTS

	 
	   if(employeeMasterDTO==null || skillDTOList.size()==0||projectDTOList.size()==0)
	   {
		   resumeDTO=null;
	   }
	   else
	   {
		resumeDTO.setEmployeeMaster(employeeMasterDTO);
		resumeDTO.setSkills(skillDTOList);
		resumeDTO.setProjects(projectDTOList);
		resumeDTO.setEducations(educationDTOList);
		resumeDTO.setEmployeeSumbissionStatus(EmployeeSubmissionStatus.SUBMITTED.name());
	   }
	}
	catch(Exception e)
	{
		LOG.debug("parseResumeDTOFromHTMLString failure:"+e);
		e.printStackTrace();
	}
		return resumeDTO;
	}

//--- Added by Mukunda to replace special characters in word document parsing
private String replaceSpecialChar(String strReplace) {

//	strReplace = strReplace.replaceAll("\n"," ");
	strReplace = strReplace.replaceAll("[^\\w\\s\\-_()?:!.,;&]", "");
	return strReplace;
}
//--------------------------------------------------------------------------

@Override
public String exportResume(int resumeid, HttpServletRequest request, HttpServletResponse response) 
{
	System.out.println("Pdf Conversion for resume started");
	ResumeDTO resumeDTO = new ResumeDTO();
	//EmployeeViewServiceImpl exportResume = new EmployeeViewServiceImpl();
	//resumeDTO = employeeViewService.getResume(resumeid);
	resumeDTO = viewResume(resumeid);
	//System.out.println("PDF conversion resumeDTO for resume id :  "+resumeDTO.getResumeid());
	if(resumeDTO == null)
	{
		return CommonConstants.Export_FAILURE_STATUS + "\t" +"Resume not available";
	}
	try
	{
		//System.out.println("PDF Conversion for resume");
		pdfConversion convertToPdf = new pdfConversion();
		convertToPdf.convert(resumeDTO,request,response);
		return CommonConstants.Export_SUCCESS_STATUS;
	}
	catch(Exception e)
	{
		LOG.debug("exportResume failure.Details:"+e);
		e.printStackTrace();
		return "Export Failed";
		
	}
	
}



/*
 * testing
 */

/*public static void main(String[] args)
{
	Resume resumePOJO = new Resume();
	ARMGServiceImpl impl = new ARMGServiceImpl();
	//uploadToServerFolder(uploadDTO);
	impl.convertDocxToHTML();
	ResumeDTO resumeDTO = impl.readHTMLAndParseData();
	Resume resume=CopyUtil.copyDTOtoPOJO(resumeDTO,resumePOJO);
	
	
	
	
}*/


/*
 * end of testing
 */

//added by sampad
@Override
public  String insertEmployeeMaster(List<EmployeeMaster> employeeMasterList){
	
  String status = CommonConstants.UPLOAD_SUCCESS_STATUS;
  
  try
  {
  	for (EmployeeMaster employeeMaster : employeeMasterList) {
  		employeeMaster.setCreatedDate(CustomDate.getCustomDate());
      	employeeMaster.setUpdatedDate(CustomDate.getCustomDate());
      	if(employeeMaster.getEmpId()!=null)
      	{
      		//check id of employee is already in DB
      		EmployeeMaster employeeMasterInDB =  employeeMasterDAO.findById(employeeMaster.getEmpId());
      		if(employeeMasterInDB==null)
      		{
      			employeeMasterDAO.persist(employeeMaster);
      		}
      		else
      		{
      			employeeMasterInDB.setEmployeeMail(employeeMaster.getEmployeeMail());
      			employeeMasterInDB.setCurrentProject(employeeMaster.getCurrentProject());
      			employeeMasterInDB.setGroup(employeeMaster.getGroup());
      			employeeMasterInDB.setManagerMail(employeeMaster.getManagerMail());
      			employeeMasterInDB.setName(employeeMaster.getName());
      			employeeMasterInDB.setReportingManager(employeeMaster.getReportingManager());
      			employeeMasterInDB.setTitle(employeeMaster.getTitle());
      			employeeMasterInDB.setUpdatedDate(CustomDate.getCustomDate());
      			employeeMasterDAO.attachDirty(employeeMasterInDB);
      		}
      	}
      	else
      	{
      		break;
      	}
      	
			
		}
  	
  }
  catch(Exception e )
  {
  	status=CommonConstants.UPLOAD_FAILURE_STATUS;
  	LOG.debug("insertEmployeeMaster in armg service failure:"+e);
  	e.printStackTrace();
  }
  return status;

}

@Override
public ResumeDTO viewResume(int resumeid) {


	LOG.debug("getViewResume() method called in buisness layer");

	ResumeDTO resumeDTO = new ResumeDTO();
	try{
		Resume resume = resumeDAO.findById(resumeid);
		// Resume resume = resumeDAO.getResume(empId,employeeSubmissionStatus);

//		String[] totParts = resume.getTotalExperience().split(" ");
//		String[] relParts = resume.getRelevantExperience().split(" ");
//		String totYear = totParts[0];
//		String totMon = totParts[2];
//		String relYear = relParts[0];
//		String relMon = relParts[2];

		EmployeeMasterDTO employeeDTO = new EmployeeMasterDTO();
		resumeDTO.setEmployeeMaster(employeeDTO);
		SkillDTO skillDTO = null;
		List<SkillDTO> skillList = new ArrayList<SkillDTO>();
		EducationDTO eduactionDTO = null;
		List<EducationDTO> educationList = new ArrayList<EducationDTO>();

		ProjectDTO projectDTO = null;
		List<ProjectDTO> projectList = new ArrayList<ProjectDTO>();

		// get Employee details

		EmployeeMasterDTO employeemasterDTO = null;
		employeemasterDTO = new EmployeeMasterDTO();
		employeemasterDTO.setTitle(resume.getEmployeeMaster().getTitle());
		employeemasterDTO.setEmpId(resume.getEmployeeMaster().getEmpId());
		employeemasterDTO.setName(resume.getEmployeeMaster().getName());
		employeemasterDTO.setReportingManager(resume.getEmployeeMaster()
				.getReportingManager());
		employeemasterDTO.setEmployeeMail(resume.getEmployeeMaster()
				.getEmployeeMail());
		resumeDTO.setEmployeeMaster(employeemasterDTO);

		// getting relevant experience

		resumeDTO.setRelevantExperience(resume.getRelevantExperience());
		resumeDTO.setResumeid(resume.getResumeid());

//		// getting totmon and totyear seperate
//		resumeDTO.setTotYear(totYear);
//		resumeDTO.setTotMon(totMon);
//
//		// getting relmon and relyear seperate
//		resumeDTO.setRelYear(relYear);
//		resumeDTO.setRelMon(relMon);

		// getting total experience
		resumeDTO.setTotalExperience(resume.getTotalExperience());

		// getting domain and certification
		resumeDTO.setDomain(resume.getDomain());
		resumeDTO.setCertification(resume.getCertification());

		// getting professional summary

		resumeDTO.setProfessionalSummary(resume.getProfessionalSummary());
		System.out.println("EmployeeViewOfServicce profess summary::::"
				+ resumeDTO.getProfessionalSummary());
		List<String> summeryList = getProfessionalSummery(resume.getProfessionalSummary());
		if(summeryList != null && !summeryList.isEmpty()){
			resumeDTO.setProSummeryList(summeryList);
		}
		// get education details

		Education education = null;

		Set Educations = resume.getEducations();
		Iterator educationIterator = Educations.iterator();
		while (educationIterator.hasNext()) {

			education = (Education) educationIterator.next();
			eduactionDTO = new EducationDTO();
			eduactionDTO.setAggregate(education.getAggregate());
			eduactionDTO.setCollege(education.getCollege());
			eduactionDTO.setDegree(education.getDegree());
			eduactionDTO.setYearOfPassing(education.getYearOfPassing());
			eduactionDTO.setSpecialization(education.getSpecialization());
			educationList.add(eduactionDTO);
			resumeDTO.setEducations(educationList);
		}

		// get project details
		Project project = null;
		Set Projects = resume.getProjects();
		Iterator projectIterator = Projects.iterator();
		while (projectIterator.hasNext()) {
			project = (Project) projectIterator.next();
			projectDTO = new ProjectDTO();
			projectDTO.setProjectDesc(project.getProjectDesc());
			projectDTO.setClientName(project.getClientName());
			projectDTO.setDuration(project.getDuration());
			projectDTO.setProjectName(project.getProjectName());
			projectDTO.setEnvironment(project.getEnvironment());
			projectDTO.setResponsibility(project.getResponsibility());
			projectDTO.setRole(project.getRole());
			projectList.add(projectDTO);

			// adding to the resume DTO

			resumeDTO.setProjects(projectList);
		}

		// geting skill details
		Skill skill = new Skill();
		Set Skills = resume.getSkills();
		LOG.debug("skills " + resume.getSkills());

		Iterator skillIterator = Skills.iterator();
		while (skillIterator.hasNext()) {
			skill = (Skill) skillIterator.next();
			skillDTO = new SkillDTO();
			List<String> bulletList = null;
			
			skillDTO.setSkill(skill.getSkill());
			skillDTO.setSkillCatagory(skill.getSkillCatagory());
			skillDTO.setSkillType(skill.getSkillType());
			skillDTO.setUpdatedDate(skill.getUpdatedDate());
			skillList.add(skillDTO);

			resumeDTO.setSkills(skillList);
		}
	}
	catch(Exception e)
	{
		LOG.debug("Error in getViewResume business layer:"+e);
		e.printStackTrace();
		
	}
	return resumeDTO;

}
private List<String> getProfessionalSummery(String summery){
	List<String> summeryList = null;
	try{
		if(summery != null && !summery.isEmpty()){
			String[] lines = summery.split("\r\n|\r|\n");
			for (String line : lines) {
				if(summeryList == null){
					summeryList = new ArrayList<>();
				}
				if(!line.trim().equals(" "))
					summeryList.add(line);
			}
		}
	}
	catch(Exception exception)
	{
		LOG.debug("getProfessionalSummery failed in employee service:"+exception);
		exception.printStackTrace();
		
	}
	return summeryList;
}
}

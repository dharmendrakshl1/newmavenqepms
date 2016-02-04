/**
 * 
 */
package com.qepms.business.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.qepms.business.service.ManagerService;
import com.qepms.business.service.SendingMailService;
import com.qepms.business.util.QEPMSBusinessUtils;
import com.qepms.common.util.CustomDate;
import com.qepms.common.util.EmployeeSubmissionStatus;
import com.qepms.common.util.Format;
import com.qepms.common.util.ManagerSubmissionStatus;
import com.qepms.common.util.SkillType;
import com.qepms.data.dao.employee.ResumeDAO;
import com.qepms.data.employee.Education;
import com.qepms.data.employee.Project;
import com.qepms.data.employee.Resume;
import com.qepms.data.employee.Skill;
import com.qepms.infra.constants.CommonConstants;
import com.qepms.infra.dto.armg.EducationDTO;
import com.qepms.infra.dto.armg.EmployeeMasterDTO;
import com.qepms.infra.dto.armg.ProjectDTO;
import com.qepms.infra.dto.armg.ResumeDTO;
import com.qepms.infra.dto.armg.SkillDTO;




/**
 * @author AshwiniK
 *
 */
@Transactional
public class ManagerServiceImpl implements ManagerService {
	private static final Logger LOG = LoggerFactory
			.getLogger(ManagerService.class);
	
	private
	@Autowired 
	ResumeDAO resumeDAO;
	
	@Autowired
	SendingMailService sendingMailService;

	@Override
	public List<ResumeDTO> getListOfResumesBasedOnManagerEmail(String managerApprovalStatus, String reportingManageremail) 
	{
		LOG.debug("getListOfResumesBasedOnManagerEmail called in manager business layer");
		List<ResumeDTO> listOfResumeDTO = new ArrayList<ResumeDTO>();
		
		ResumeDTO resumeDTO = null;	
		EmployeeMasterDTO employeeDTO =null;
		List<Resume> listOfResumes = null;
	
		CommonConstants constants = null;
		try
		{
			if (managerApprovalStatus.equalsIgnoreCase("approved") || managerApprovalStatus.equalsIgnoreCase("pending") ) {
				listOfResumes  = resumeDAO.getListOfResumesBasedOnManagerEmail(managerApprovalStatus,reportingManageremail,EmployeeSubmissionStatus.SUBMITTED.name());
				
			}
			
			else if(managerApprovalStatus.equalsIgnoreCase("rejected"))
			{
				listOfResumes  = resumeDAO.getListOfResumesBasedOnManagerEmail(managerApprovalStatus,reportingManageremail,EmployeeSubmissionStatus.PENDING.name());
			}
			
			LOG.debug("Size of member list is"+listOfResumes.size());
			boolean present = false;
			boolean approved = false;
			List<Resume> listResumesWithoutDuplicate = new ArrayList<Resume>();
			for (Resume duplicateresume : listOfResumes) {
				
				for (Resume resume : listResumesWithoutDuplicate) {
					
					if(resume.getEmployeeMaster().equals(duplicateresume.getEmployeeMaster()))
					{
						present = true;
						break;
					}
					
				}
				if(!present)
				{
					listResumesWithoutDuplicate.add(duplicateresume);
					
				}
				present=false;
				
			}
			
			/*
			 * iterate over listOfResumes
			 */
			
			Iterator iterator = listResumesWithoutDuplicate.iterator();
			while(iterator.hasNext())
			{
				resumeDTO = new ResumeDTO();
				employeeDTO = new EmployeeMasterDTO();
				Resume resume=(Resume) iterator.next();
				employeeDTO.setEmpId(resume.getEmployeeMaster().getEmpId());
				employeeDTO.setName(resume.getEmployeeMaster().getName());
				resumeDTO.setEmployeeMaster(employeeDTO);
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
				
				resumeDTO.setUpdatedDate(resume.getUpdatedDate());
				resumeDTO.setCustomUpdatedDate(QEPMSBusinessUtils.convertedDate(resume.getUpdatedDate(), Format.ddMMyyyy.name()));
				resumeDTO.setApprovedDate(resume.getApprovedDate());
				resumeDTO.setRejectedDate(resume.getRejectedDate());
				listOfResumeDTO.add(resumeDTO);
				// Getting the resumes which are in PENDING status in Resume table
				
				//if(	resume.getManagerApprovalStatus().equalsIgnoreCase(constants.MANAGER_APPROVAL_STATUS))
				//{
					//resumeDTO.setTotalExperience(resume.getTotalExperience());
				
				/*//code added by Mukunda to display resume to manager which are latest and not approved
				List<Resume> listOfResumesApproved  = resumeDAO.getListOfResumes(ManagerSubmissionStatus.APPROVED.name(),reportingManager,EmployeeSubmissionStatus.SUBMITTED.name());
				LOG.debug("Size of member list is"+listOfResumesApproved.size());
				Iterator iteratorApproved = listOfResumesApproved.iterator();
				while(iteratorApproved.hasNext())
				{
					Resume approvedResume=(Resume) iterator.next();
					if(approvedResume.getEmployeeMaster().getEmpId().equals(resume.getEmployeeMaster().getEmpId()) && approvedResume.getUpdatedDate().before(resume.getUpdatedDate()))
					{
						
					}
					
					else
					{
						LOG.debug("There are no resumes with accepted status");
					}*/
				//}
				
				/*else
				{
					LOG.debug("There are no resumes with accepted status");
				}
		         */
					
			
			}
		
		}
		catch(Exception e)
		{
			LOG.debug("Exception in getListOfResumesBasedOnManagerEmail: details:"+e);
			e.printStackTrace();
		}
	
		return listOfResumeDTO;
	}

	public List<ResumeDTO> getListOfResumes(String managerApprovalStatus,String reportingManager){
		
		
		List<ResumeDTO> listOfResumeDTO = new ArrayList<ResumeDTO>();
		
		ResumeDTO resumeDTO = null;	
		EmployeeMasterDTO employeeDTO =null;
		List<Resume> listOfResumes = null;
	
		CommonConstants constants = null;
		try
		{
			if (managerApprovalStatus.equalsIgnoreCase("approved") || managerApprovalStatus.equalsIgnoreCase("pending") ) {
				listOfResumes  = resumeDAO.getListOfResumes(managerApprovalStatus,reportingManager,EmployeeSubmissionStatus.SUBMITTED.name());	
			}
			
			else if(managerApprovalStatus.equalsIgnoreCase("rejected"))
			{
				listOfResumes  = resumeDAO.getListOfResumes(managerApprovalStatus,reportingManager,EmployeeSubmissionStatus.PENDING.name());
			}
			
			LOG.debug("Size of member list is"+listOfResumes.size());
			boolean present = false;
			boolean approved = false;
			List<Resume> listResumesWithoutDuplicate = new ArrayList<Resume>();
			for (Resume duplicateresume : listOfResumes) {
				
				for (Resume resume : listResumesWithoutDuplicate) {
					
					if(resume.getEmployeeMaster().equals(duplicateresume.getEmployeeMaster()))
					{
						present = true;
						break;
					}
					
				}
				if(!present)
				{
					listResumesWithoutDuplicate.add(duplicateresume);
					
				}
				present=false;
				
			}
			
			/*
			 * iterate over listOfResumes
			 */
			
			Iterator iterator = listResumesWithoutDuplicate.iterator();
			while(iterator.hasNext())
			{
				resumeDTO = new ResumeDTO();
				employeeDTO = new EmployeeMasterDTO();
				Resume resume=(Resume) iterator.next();
				employeeDTO.setEmpId(resume.getEmployeeMaster().getEmpId());
				employeeDTO.setName(resume.getEmployeeMaster().getName());
				resumeDTO.setEmployeeMaster(employeeDTO);
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
				
				resumeDTO.setUpdatedDate(resume.getUpdatedDate());
				resumeDTO.setCustomUpdatedDate(QEPMSBusinessUtils.convertedDate(resume.getUpdatedDate(), Format.ddMMyyyy.name()));
				listOfResumeDTO.add(resumeDTO);
				// Getting the resumes which are in PENDING status in Resume table
				
				//if(	resume.getManagerApprovalStatus().equalsIgnoreCase(constants.MANAGER_APPROVAL_STATUS))
				//{
					//resumeDTO.setTotalExperience(resume.getTotalExperience());
				
				/*//code added by Mukunda to display resume to manager which are latest and not approved
				List<Resume> listOfResumesApproved  = resumeDAO.getListOfResumes(ManagerSubmissionStatus.APPROVED.name(),reportingManager,EmployeeSubmissionStatus.SUBMITTED.name());
				LOG.debug("Size of member list is"+listOfResumesApproved.size());
				Iterator iteratorApproved = listOfResumesApproved.iterator();
				while(iteratorApproved.hasNext())
				{
					Resume approvedResume=(Resume) iterator.next();
					if(approvedResume.getEmployeeMaster().getEmpId().equals(resume.getEmployeeMaster().getEmpId()) && approvedResume.getUpdatedDate().before(resume.getUpdatedDate()))
					{
						
					}
					
					else
					{
						LOG.debug("There are no resumes with accepted status");
					}*/
				//}
				
				/*else
				{
					LOG.debug("There are no resumes with accepted status");
				}
		         */
					
			
			}
		}
		catch(Exception e)
		{
			LOG.debug("Exception in manager business layer to getListOfResumes: "+e);
			e.printStackTrace();
		}
		
		return listOfResumeDTO;
		
	}

	public ResumeDTO getResume(int empId) {
		// TODO Auto-generated method stub
		ResumeDTO resumeDTO = new ResumeDTO();//empty
		
		LOG.debug("calling the getResume in manager business layer");
	
		try{
		Resume resume = resumeDAO.getResume(empId);//fully populated
		EmployeeMasterDTO employeeDTO = new EmployeeMasterDTO();
		
		/*
		 * getting title,employeeId,employeeName
		 */
		employeeDTO.setEmpId(resume.getEmployeeMaster().getEmpId());
		LOG.debug("employee ID" + resume.getEmployeeMaster().getEmpId());

		employeeDTO.setTitle(resume.getEmployeeMaster().getTitle());
		LOG.debug("employee title " + resume.getEmployeeMaster().getEmpId());
		employeeDTO.setName(resume.getEmployeeMaster().getName());
		LOG.debug("employee name " + resume.getEmployeeMaster().getEmpId());
		
		resumeDTO.setEmployeeMaster(employeeDTO);
		
		SkillDTO skillDTO = null;
		List<SkillDTO> skillList = new ArrayList<SkillDTO>();
		
		ProjectDTO projectDTO=null;
		List<ProjectDTO> projectList = new ArrayList<ProjectDTO>();
		
		EducationDTO eduactionDTO=null;
		List<EducationDTO> educationList = new ArrayList<EducationDTO>();
		
		/*
		 * getting total, relevant experience
		 */
		 
		
		resumeDTO.setRelevantExperience(resume.getRelevantExperience());
		resumeDTO.setTotalExperience(resume.getTotalExperience());
		resumeDTO.setDomain(resume.getDomain());
		resumeDTO.setCertification(resume.getCertification());
				
		 /*
		  *  getting professional summary
		  */
		 
		
		resumeDTO.setProfessionalSummary(resume.getProfessionalSummary());
		
		
		 /*
		  *  getting skills
		  */
		 
		
	    Set Skills = resume.getSkills();
	    LOG.debug("skills " + resume.getSkills());
	    
	    Iterator skillIterator = Skills.iterator();
	    while (skillIterator.hasNext())
	    {
	    	Skill skill=(Skill)skillIterator.next();
	    	skillDTO = new  SkillDTO();
	    	
	    	skillDTO.setSkill(skill.getSkill());
	    	skillDTO.setSkillCatagory(skill.getSkillCatagory());
	    	skillDTO.setSkillType(skill.getSkillType());
	    	skillDTO.setUpdatedDate(skill.getUpdatedDate());
	    	
	    	skillList.add(skillDTO);
	    	
	    	resumeDTO.setSkills(skillList);
	    }
	    
	   
	     
	      //   getting project details
	      
	     
	    
	    Set Projects=resume.getProjects();
	    Iterator projectIterator = Projects.iterator();
	    while (projectIterator.hasNext())
	    {
	    	Project project=(Project)projectIterator.next();
	    	projectDTO = new  ProjectDTO();
	    	
	    	projectDTO.setProjectDesc(project.getProjectDesc());
	    	projectDTO.setProjectName(project.getProjectName());
	    	projectDTO.setEnvironment(project.getEnvironment());
	    	projectDTO.setResponsibility(project.getResponsibility());
	    	projectDTO.setRole(project.getRole());
	    	
	    	projectList.add(projectDTO);
	    	
	    	
	    	
	    	 //adding to the resume DTO
	    	 
	    	 
	    	
	    	resumeDTO.setProjects(projectList);
	    }
	    
	    
	     
	      // getting  education details
	      
	     
	    
	    Set Educations = resume.getEducations();
	    Iterator educationIterator = Educations.iterator();
	    while (educationIterator.hasNext())
	    {
	    	Education education=(Education)educationIterator.next();
	    	eduactionDTO = new  EducationDTO();
	    	
	    	eduactionDTO.setAggregate(education.getAggregate());
	    	eduactionDTO.setCollege(education.getCollege());
	    	eduactionDTO.setDegree(education.getDegree());
	    	
	    	eduactionDTO.setYearOfPassing(education.getYearOfPassing());
	    	eduactionDTO.setSpecialization(education.getSpecialization());
	    	
	    	educationList.add(eduactionDTO);
	    	
	    	
	    	 
	    	  // adding to the resume DTO
	    	  
	    	 
	    	
	    	resumeDTO.setEducations(educationList);
	    }
		}
		catch(Exception e)
		{
			LOG.debug("getResume in manager service has exception:Details"+e);
			e.printStackTrace();
		}
		
		return resumeDTO;
	}
@Override
	public String postCommets(int resumeid, String comments) {
		// TODO Auto-generated method stub
		
		String status="null";
		String managerStatus=ManagerSubmissionStatus.REJECTED.name();
		String employeeStatus=EmployeeSubmissionStatus.PENDING.name();
		ResumeDTO resumeDTO = new ResumeDTO();//empty
	try{	
		Date date=new Date();
		int rowsAffected= resumeDAO.postComments(resumeid,comments,managerStatus,employeeStatus,date );//fully populated
		LOG.debug("calling the postComments in ResumeDAO");
		if(rowsAffected==1)
		{
			status="Resume Rejected Successfully";
		}
		
		//send mail
		Resume resume=resumeDAO.findById(resumeid);
		String subject="QPMS-Profile Management System Rejected By "+resume.getEmployeeMaster().getReportingManager();
        String content = "<html><head><style>table {color:black;}</style><style>div {color:black;}</style></head>"+
        "<div>Hi "+resume.getEmployeeMaster().getName()+",<br/><br/>This is to inform you that the resume is rejected.<br/>"+
        		"Please find the comments below<br/><br/>Comments:<p>"+comments+"</p></div>"
		+ "Click on this link to login and edit <a href='http://10.30.3.81:8081/qepms'>QPMS</a>";
        String to=resume.getEmployeeMaster().getEmployeeMail();
        String cc=resume.getEmployeeMaster().getManagerMail();
        
		sendingMailService.sendMail(to,cc,subject,content);
		
	}
	catch(Exception e)
	{
		LOG.debug("Exception in manger reject resume business layer:"+e);
		e.printStackTrace();
	}
		
		return status;
	}

	@Override
	public String acceptResume(int resumeid,String comments) {
		// TODO Auto-generated method stub
		String status="null";
		String managerStatus=ManagerSubmissionStatus.APPROVED.name();
		try{
			int rowsAffected=resumeDAO.acceptResume(resumeid,managerStatus,comments);
			if(rowsAffected==1)
			{
				status="Resume Accepted Successfully";
			}
			
			//send mail to armg and employee after approving
			Resume resume=resumeDAO.findById(resumeid);
			String to="QEPMS_Manager@quinnox.com";
			String cc=resume.getEmployeeMaster().getManagerMail()+","+resume.getEmployeeMaster().getEmployeeMail();
			String subject="QPMS-Profile Management System Resume Approved By "+resume.getEmployeeMaster().getReportingManager();
			String content="<html><head><style>table {color:black;}</style><style>div {color:black;}</style></head>"+
			         "<div>Hi Team_Armg,<br/><br/>This is to inform you that the resume submitted by "+resume.getEmployeeMaster().getName()+
			         " is approved by "+resume.getEmployeeMaster().getReportingManager()+" and ready for Review.<br/><br/></div>"
					+ "Click on this link to review <a href='http://10.30.3.81:8081/qepms'>QPMS</a>";
		
			sendingMailService.sendMail(to, cc, subject, content);
		}
		catch(Exception e)
		{
			LOG.debug("Exception while accepting resume by manager:"+e);
			e.printStackTrace();
		}
		return status;
	
	}

	
}

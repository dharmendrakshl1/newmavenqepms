package com.qepms.business.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.qepms.business.service.EmployeeViewService;
import com.qepms.business.service.SendingMailService;
import com.qepms.business.util.QEPMSBusinessUtils;
import com.qepms.common.util.CopyUtil;
import com.qepms.common.util.EmployeeSubmissionStatus;
import com.qepms.common.util.Format;
import com.qepms.common.util.ManagerSubmissionStatus;
import com.qepms.data.dao.employee.EducationDAO;
import com.qepms.data.dao.employee.EmployeeMasterDAO;
import com.qepms.data.dao.employee.ProjectDAO;
import com.qepms.data.dao.employee.ResumeDAO;
import com.qepms.data.dao.employee.SkillDAO;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class EmployeeViewServiceImpl implements EmployeeViewService {

	private static final Logger LOG = LoggerFactory
			.getLogger(EmployeeViewService.class);

	@Autowired
	private ResumeDAO resumeDAO;

	@Autowired
	private EmployeeMasterDAO employeeMasterDAO;

	@Autowired
	private EducationDAO educationDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private SkillDAO skillDAO;
	
	@Autowired
	private SendingMailService sendingMailService;

	// common implementation for draft submitted and rejected page and draft
	// page

	@Override
	public List<ResumeDTO> getResume(Integer empId,
			String managerApprovalStatus, String employeeSumbissionStatus) {

		LOG.debug("getResume() method called in buisness layer");
		List<ResumeDTO> resumeDTOList = new ArrayList<ResumeDTO>();
		List<Resume> resume = null;

		ResumeDTO resumeDTO = null;
		EmployeeMasterDTO employeemasterDTO = null;
		SkillDTO skillDTO = null;
		EducationDTO educationDTO = null;
		Skill skill = null;

		/*
		 * if(employeeSumbissionStatus==null||employeeSumbissionStatus.isEmpty())
		 * {
		 * resume=resumeDAO.getResumeListByManagerStatus(empId,managerApprovalStatus
		 * ); }
		 */
		/*
		 * else if(managerApprovalStatus==null ||
		 * managerApprovalStatus.isEmpty()) {
		 * resume=resumeDAO.getResumeListByEmployeeStatus
		 * (empId,employeeSumbissionStatus); }
		 */
		try{
			resume = resumeDAO.getResumeListByManagerStatus(empId,
					employeeSumbissionStatus, managerApprovalStatus);
			LOG.debug("Size of member list is" + resume.size());
	
			Iterator resumeIterator = (Iterator) resume.iterator();
			while (resumeIterator.hasNext()) 
			{
				Resume res = (Resume) resumeIterator.next();
	
				resumeDTO = new ResumeDTO();
	
				employeemasterDTO = new EmployeeMasterDTO();
				skillDTO = new SkillDTO();
				educationDTO = new EducationDTO();
				resumeDTO.setResumeid(res.getResumeid());
				resumeDTO.setCreatedDate(res.getCreatedDate());
				resumeDTO.setUpdatedDate(res.getUpdatedDate());
				resumeDTO.setCustomCreatedDate(QEPMSBusinessUtils.convertedDate(
						res.getCreatedDate(), Format.ddMMyyyy.name()));
				resumeDTO.setCustomUpdatedDate(QEPMSBusinessUtils.convertedDate(
						res.getUpdatedDate(), Format.ddMMyyyy.name()));
	
				resumeDTO.setRelevantExperience(res.getRelevantExperience());
				resumeDTO.setTotalExperience(res.getTotalExperience());
				resumeDTO.setDomain(res.getDomain());
				resumeDTO.setCertification(res.getCertification());
				resumeDTO.setProfessionalSummary(res.getProfessionalSummary());
				
				resumeDTO.setRelevantExperience(res.getRelevantExperience());
				resumeDTO.setEmployeeSumbissionStatus(res
						.getEmployeeSumbissionStatus());
				resumeDTO.setManagerApprovalStatus(res.getManagerApprovalStatus());
				resumeDTO.setComments((res).getComments());
	
				resumeDTO
						.setManagerApprovalStatus((res).getManagerApprovalStatus());
	
				employeemasterDTO.setEmpId(res.getEmployeeMaster().getEmpId());
				employeemasterDTO.setName(res.getEmployeeMaster().getName());
				employeemasterDTO.setTitle(res.getEmployeeMaster().getTitle());
				resumeDTO.setEmployeeMaster(employeemasterDTO);
	
				resumeDTOList.add(resumeDTO);
			}
		}
		catch (Exception e) 
		{
			LOG.debug("Error in EmployeeService to getResume:Details:"+e);
			e.printStackTrace();	
		}
		return resumeDTOList;

	}

	// implementation for viewresume page

	@Override
	public ResumeDTO getResume(int resumeid) {

		LOG.debug("getViewResume() method called in buisness layer");

		ResumeDTO resumeDTO = new ResumeDTO();
		try{
			Resume resume = resumeDAO.findById(resumeid);
			// Resume resume = resumeDAO.getResume(empId,employeeSubmissionStatus);
	
			String[] totParts = resume.getTotalExperience().split(" ");
			String[] relParts = resume.getRelevantExperience().split(" ");
			String totYear = totParts[0];
			String totMon = totParts[2];
			String relYear = relParts[0];
			String relMon = relParts[2];
	
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
	
			// getting totmon and totyear seperate
			resumeDTO.setTotYear(totYear);
			resumeDTO.setTotMon(totMon);
	
			// getting relmon and relyear seperate
			resumeDTO.setRelYear(relYear);
			resumeDTO.setRelMon(relMon);
	
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

	// implementation for edit resume

	@Override
	public ResumeDTO getEditResume(Integer empId) {

		LOG.debug("getEditResume() method called in buisness layer");
		ResumeDTO resumeDTO = new ResumeDTO();

	try
	{
		Resume resume = resumeDAO.getEditResume(empId);
		
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
		resumeDTO.setEmployeeMaster(employeemasterDTO);

		// geting relevant experience

		resumeDTO.setRelevantExperience(resume.getRelevantExperience());

		// geting total experience

		resumeDTO.setTotalExperience(resume.getTotalExperience());

		// getting domain and certification
		resumeDTO.setCertification(resume.getCertification());
		resumeDTO.setDomain(resume.getDomain());

		// geting professional summay

		resumeDTO.setProfessionalSummary(resume.getProfessionalSummary());

		// implementation for edited submit

		/*
		 * employeemasterDTO.setTitle(editedsubmitresume.getEmployeeMaster().
		 * getTitle());
		 * employeemasterDTO.setEmpId(editedsubmitresume.getEmployeeMaster
		 * ().getEmpId());
		 * employeemasterDTO.setName(editedsubmitresume.getEmployeeMaster
		 * ().getName()); resumeDTO.setEmployeeMaster(employeemasterDTO);
		 * 
		 * //geting relevant experience
		 * 
		 * resumeDTO.setRelevantExperience(editedsubmitresume.getRelevantExperience
		 * ());
		 * 
		 * //geting total experience
		 * 
		 * resumeDTO.setTotalExperience(editedsubmitresume.getTotalExperience());
		 * 
		 * //geting professional summary
		 * 
		 * resumeDTO.setProfessionalSummary(editedsubmitresume.
		 * getProfessionalSummary());
		 */

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

			skillDTO.setSkill(skill.getSkill().replace("\t", ""));
			skillDTO.setSkillCatagory(skill.getSkillCatagory());
			skillDTO.setSkillType(skill.getSkillType().replace("\t", ""));
			skillDTO.setUpdatedDate(skill.getUpdatedDate());
			skillList.add(skillDTO);

			resumeDTO.setSkills(skillList);
		}
	}
	catch(Exception e)
	{
		LOG.debug("Error in getEditResume() in Employee Service"+e);
		e.printStackTrace();		
	}
		return resumeDTO;

}

	// implementation for submitresume page
	@Override
	public ResumeDTO getSubmitResume(Integer empId) {

		LOG.debug("getSubmitResume() method called in buisness layer");
		ResumeDTO resumeDTO = new ResumeDTO();
	try
	{
		Resume resume = resumeDAO.getSubmitResume(empId);

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
		resumeDTO.setEmployeeMaster(employeemasterDTO);

		// getting relevant experience

		resumeDTO.setRelevantExperience(resume.getRelevantExperience());

		// getting total experience,domain,certifications

		resumeDTO.setTotalExperience(resume.getTotalExperience());
		resumeDTO.setCertification(resume.getCertification());
		resumeDTO.setDomain(resume.getDomain());

		// getting professional summary

		resumeDTO.setProfessionalSummary(resume.getProfessionalSummary());

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

			skillDTO.setSkill(skill.getSkill());
			skillDTO.setSkillCatagory(skill.getSkillCatagory());
			skillDTO.setSkillType(skill.getSkillType());
			skillDTO.setUpdatedDate(skill.getUpdatedDate());
			skillList.add(skillDTO);

			resumeDTO.setSkills(skillList);
		}
	}
	catch(Exception ex)
	{
		LOG.debug("getSubmitResume() error in Employee service:"+ex);
		ex.printStackTrace();		
	}
		return resumeDTO;

	}

	@Override
	public List<String> getSkillTypeNames() {
		LOG.debug("employee getSkillTypeNames() called");
		List<String> skillTypeNameList = new ArrayList<String>();
		BufferedReader br;
		try {

			String curDir = System.getProperty("user.dir");
			File GradeList = new File("Skill.txt");
			System.out.println("Current sys dir: " + curDir);
			System.out.println("Current abs dir: "
					+ GradeList.getAbsolutePath());
			String line;
			br = new BufferedReader(new FileReader("Skill.txt"));
			while ((line = br.readLine()) != null)
			{
				// process the line.
				skillTypeNameList.add(line);

			}
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			LOG.debug("FileNotFoundException:"+e);
			e.printStackTrace();
		}
		catch (Exception e) {
			LOG.debug("Exception in getSkillTypeNames():"+e);
			e.printStackTrace();
		}
		return skillTypeNameList;
	}

	// used
	// for submitting the resume by employee
	@Override
	public String submitResume(ResumeDTO resumeDTO) {
		LOG.debug("Employee submit resume() called");
		Resume resume = null;

		/*
		 * Added by Thammaiah to make sure that employee has only on latest
		 * submitted resume
		 */
	try
	{
		// get all the submitted resumes
		List<Resume> resumeInDB = getResumesFromDB(resumeDTO
				.getEmployeeMaster().getEmpId());

		for (Resume resumeSubmitted : resumeInDB) {

			resumeSubmitted
					.setEmployeeSumbissionStatus(EmployeeSubmissionStatus.NA
							.name());
			resumeDAO.attachDirty(resumeSubmitted);
		}

		// checking if submitted from edit form
		if (resumeDTO.getResumeid() != null) {
			resume = resumeDAO.findResumeById(resumeDTO.getResumeid());
			Education education = null;
			Skill skill = null;
			Project project = null;
			Set educations = resume.getEducations();
			Set projects = resume.getProjects();
			Set skills = resume.getSkills();
			Iterator educationItr = educations.iterator();
			while (educationItr.hasNext()) {
				education = (Education) educationItr.next();
				educationDAO.delete(education);

			}
			Iterator projectsItr = projects.iterator();
			while (projectsItr.hasNext()) {
				project = (Project) projectsItr.next();
				projectDAO.delete(project);
			}
			Iterator skillItr = skills.iterator();
			while (skillItr.hasNext()) {
				skill = (Skill) skillItr.next();
				skillDAO.delete(skill);
			}
		} else {
			resume = new Resume();
			resume.setCreatedDate(new Date());
		}
		Resume pesistableResume = CopyUtil.copyDTOtoPOJO(resumeDTO, resume);
		EmployeeMaster employeeMaster = employeeMasterDAO
				.findById(pesistableResume.getEmployeeMaster().getEmpId());
		employeeMaster.setTitle(pesistableResume.getEmployeeMaster().getTitle().replace("\t",""));
		pesistableResume.setEmployeeMaster(employeeMaster);
		Set resumes = new HashSet(0);
		resumes.add(pesistableResume);
		employeeMaster.setResumes(resumes);

		employeeMasterDAO.attachDirty(pesistableResume.getEmployeeMaster());
	}
	catch(Exception e)
	{
		LOG.debug("Employee submitResume() error"+e);
		e.printStackTrace();		
		return CommonConstants.RESUME_SUBMISSION_FAILURE_STATUS;
	}
		return CommonConstants.RESUME_SUBMISSION_SUCCESS_STATUS;
}

	private List<Resume> getResumesFromDB(Integer empId) {
		// TODO Auto-generated method stub
		List<Resume> submittedResumes = resumeDAO.getResumeListByManagerStatus(
				empId, EmployeeSubmissionStatus.SUBMITTED.name(),
				ManagerSubmissionStatus.PENDING.name());

		return submittedResumes;
	}

	@Override
	public ResumeDTO getDraftResume(Integer empId,
			String managerApprovalStatus, String employeeSumbissionStatus) {
		LOG.debug("getDraftResume() method called in buisness layer");
		ResumeDTO resumeDTO = new ResumeDTO();
		Resume resume = null;
		try{
			resume = resumeDAO.getDraftResumeForEmployee(empId,
					employeeSumbissionStatus, managerApprovalStatus);
	
			if (resume != null) {
				//EmployeeMasterDTO employeeDTO = new EmployeeMasterDTO();
	
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
				resumeDTO.setEmployeeMaster(employeemasterDTO);
	
				resumeDTO.setResumeid(resume.getResumeid());
				// getting relevant experience
	
				resumeDTO.setRelevantExperience(resume.getRelevantExperience());
	
				// getting total experience
	
				resumeDTO.setTotalExperience(resume.getTotalExperience());
	
				// getting domain
				resumeDTO.setDomain(resume.getDomain());
	
				// getting certifications
				resumeDTO.setCertification(resume.getCertification());
	
				// getting professional summary
	
				resumeDTO.setProfessionalSummary(resume.getProfessionalSummary());
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
	
					skillDTO.setSkill(skill.getSkill());
					skillDTO.setSkillCatagory(skill.getSkillCatagory());
					skillDTO.setSkillType(skill.getSkillType());
					skillDTO.setUpdatedDate(skill.getUpdatedDate());
					skillList.add(skillDTO);
	
					resumeDTO.setSkills(skillList);
				}	
			}
		}
		catch(Exception e)
		{
			LOG.debug("Exception in getDraftResume of Employee service:"+e);
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
	
	public static void main(String[] args) throws IOException {
		String string = "tracker: [\n { \n id: 1";
		String[] lines = string.split("\r\n|\r|\n");
		for (String line : lines) {
		    System.out.println(line);
		}
	}

	@Override
	public String sendMail(ResumeDTO resumeDTO) {
		
		EmployeeMaster employee=employeeMasterDAO.findById(resumeDTO.getEmployeeMaster().getEmpId());
		
		//send mail to manager
				String subject="QPMS-Profile Management System Submitted By "+employee.getName();
		        String content = "<html><head><style>table {color:black;}</style><style>div {color:black;}</style></head>"+
		        "<div>Hi "+employee.getReportingManager()+
		        ",<br/><br/>This is to inform you that the resume is submitted and ready for Review.<br/><br/></div>"
				+ "Click on this link to review <a href='http://10.30.3.81:8081/qepms'>QPMS</a>";
		        String to=employee.getManagerMail();
		        String cc=employee.getEmployeeMail();
				sendingMailService.sendMail(to,cc,subject,content); 
				
				return "Mail Sent";
	}
	
}

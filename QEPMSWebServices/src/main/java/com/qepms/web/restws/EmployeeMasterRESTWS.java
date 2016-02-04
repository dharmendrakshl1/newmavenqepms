package com.qepms.web.restws;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qepms.business.service.EmployeeViewService;
import com.qepms.infra.constants.CommonConstants;
import com.qepms.infra.dto.armg.ResumeDTO;
import com.qepms.infra.misc.wrapper.ResponseMessageWrapper;


@Controller
public class EmployeeMasterRESTWS extends BaseRESTWS {

	private static final Logger LOG = LoggerFactory
			.getLogger(EmployeeMasterRESTWS.class);

	@Autowired
	private EmployeeViewService employeeViewService;

   
	/*
	 * get resume based on status of employee or manager
	 */
	@RequestMapping(value = "/employee/resume", method = RequestMethod.GET)
	public @ResponseBody
	List<ResumeDTO> getResume(
			@RequestParam(value = "empId", required = false) final Integer empId,
			@RequestParam(value = "managerApprovalStatus", required = false) final String managerApprovalStatus,
			@RequestParam(value = "employeeSumbissionStatus", required = false) final String employeeSumbissionStatus)
	{
			List<ResumeDTO> resumeDTOList = null;
		LOG.debug("getResume() called with : managerApprovalStatus" + empId + managerApprovalStatus +employeeSumbissionStatus);
		
		try
		{
			resumeDTOList = employeeViewService.getResume(empId,managerApprovalStatus,employeeSumbissionStatus);
		} 
		catch (Exception exception) 
		{
			LOG.error("Exception Occured in employee get resume : " + exception);
			exception.printStackTrace();
		
//			throw new QEPMSApplicationException("Error while trying to get resumeList Error details: "+ empId 
//					+ managerApprovalStatus + employeeSumbissionStatus+" | Error details: "  + exception.getMessage());
		}

		return resumeDTOList;
	}
	
	/*
	 * Addded to get a single draft record from submit page
	 */
	@RequestMapping(value = "/employee/getdraftresume", method = RequestMethod.GET)
	public @ResponseBody
	ResumeDTO getDraftResume(
			@RequestParam(value = "empId", required = false) final Integer empId,
			@RequestParam(value = "managerApprovalStatus", required = false) final String managerApprovalStatus,
			@RequestParam(value = "employeeSumbissionStatus", required = false) final String employeeSumbissionStatus)
	{
			ResumeDTO resumeDTO = null;
		LOG.debug("getDraftResume() called with : managerApprovalStatus" + empId + managerApprovalStatus +employeeSumbissionStatus);

		try 
		{
			resumeDTO = employeeViewService.getDraftResume(empId,managerApprovalStatus,employeeSumbissionStatus);
		} 
		catch (Exception exception)
		{
			LOG.error("Exception Occured : " + exception);
//			throw new QEPMSApplicationException("Error while trying to get resumeList Error details: "+ empId 
//					+ managerApprovalStatus + employeeSumbissionStatus+" | Error details: "  + exception.getMessage());
			exception.printStackTrace();
		}
        
		return resumeDTO;
	}
	
		
	/*
	 * View particular resume 
	 */
	@RequestMapping(value = "/employee/vieweachresume", method = RequestMethod.GET)
	public @ResponseBody
	ResumeDTO viewResume(
			@RequestParam(value = "resumeid", required = false) final Integer resumeid){
			ResumeDTO resumeDTO = new ResumeDTO();
		LOG.debug("vieweachResume() of employee called:resumeId:"+resumeid);
		
		try {
			resumeDTO = employeeViewService.getResume(resumeid);
		} catch (Exception exception) {
			LOG.error("Error while trying to vieweachresume. Error details : " + exception);
//			throw new QEPMSApplicationException("Error while trying to get resumeList Error details:  | Error details: "  + exception.getMessage());
			exception.printStackTrace();
		}

		return resumeDTO;
	}
	
	
	
	
	/*
	 * Submit resume
	 */
	@RequestMapping(value = "/employee/submitsresume", method = RequestMethod.PUT)
	public @ResponseBody
	ResponseMessageWrapper submitResume(@RequestBody ResumeDTO resumeDTO){
		String status=null;
		ResponseMessageWrapper responseMessageWrapper = new ResponseMessageWrapper();
		LOG.debug("employee submitResume() called ");

		try {
			status = employeeViewService.submitResume(resumeDTO);
			responseMessageWrapper.setResponseMessage(status);
			
		} 
		catch (Exception exception) 
		{
			LOG.error("Exception Occured while submitting the resume : " + exception);
			exception.printStackTrace();
			responseMessageWrapper.setResponseMessage(CommonConstants.RESUME_SUBMISSION_FAILURE_STATUS);
			//throw new QEPMSApplicationException("Error while trying to call submitResume | Error details: "  + exception.getMessage());
		}

		return responseMessageWrapper;
	}
	@RequestMapping(value = "/employee/sendmail", method = RequestMethod.POST)
	public @ResponseBody
	ResponseMessageWrapper sendMail(@RequestBody ResumeDTO resumeDTO){
		String status=null;
		ResponseMessageWrapper responseMessageWrapper = new ResponseMessageWrapper();
		LOG.debug("employee submitResume() called ");

		try {
			status = employeeViewService.sendMail(resumeDTO);
			responseMessageWrapper.setResponseMessage(status);
			
		} 
		catch (Exception exception) 
		{
			LOG.error("Exception Occured while sending mail to manager : " + exception);
			exception.printStackTrace();
			responseMessageWrapper.setResponseMessage(CommonConstants.RESUME_SUBMISSION_FAILURE_STATUS);
			//throw new QEPMSApplicationException("Error while trying to call submitResume | Error details: "  + exception.getMessage());
		}

		return responseMessageWrapper;
	}
	
	@RequestMapping(value = "/employee/skilltypenames", method = RequestMethod.GET)
	public @ResponseBody List<String> getSkillTypeNames(){
		List<String> skillTypeNameList = null;
		LOG.debug("getSkillTypeNames() called with : ");
		try 
		{
			skillTypeNameList = employeeViewService.getSkillTypeNames();
		} 
		catch (Exception exception) 
		{
			LOG.error("Error while trying to get SkillTypeNames : " + exception);
//			throw new QEPMSApplicationException("Error while trying to get SkillTypeNames Error details: " 
//					+" | Error details: "  + exception.getMessage());
			exception.printStackTrace();
		}

		return skillTypeNameList;
	}
	
	
}

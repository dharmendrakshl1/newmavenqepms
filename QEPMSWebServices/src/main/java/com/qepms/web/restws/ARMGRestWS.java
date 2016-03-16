package com.qepms.web.restws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qepms.armg.service.ARMGService;
import com.qepms.data.employee.EmployeeMaster;
import com.qepms.infra.dto.armg.EmployeeMasterDTO;
import com.qepms.infra.dto.armg.EmployeeMasterWrapperDTO;
import com.qepms.infra.dto.armg.ResumeDTO;
import com.qepms.infra.dto.armg.UploadDTO;
import com.qepms.infra.misc.wrapper.ResponseMessageWrapper;


@Controller
public class ARMGRestWS {
	
	@Autowired 
	private ARMGService armgService;
	
	private static final Logger LOG = LoggerFactory.getLogger(ARMGRestWS.class);
	
	
	//getting all the resume to armg which is been uploaded
	@RequestMapping(value = "/armg/resume", method = RequestMethod.GET)
	public @ResponseBody List<ResumeDTO> getResumeList()
	{
		LOG.debug("authenticate() called from restWS ");
		 List<ResumeDTO> resumeDTOList=null;
		try 
		{
			resumeDTOList = armgService.getResumeList();
		}
		catch (Exception exception) 
		{
		    LOG.error("Exception Occured : " + exception);
//			throw new QEPMSApplicationException(
//					"Error while trying to authenticate user Error details: " + exception);
		    exception.printStackTrace();
		}
		return resumeDTOList;
	}
	/*
	 * upload file
	 */
	@RequestMapping(value = "/armg/upload",method = RequestMethod.POST)
	public @ResponseBody ResponseMessageWrapper submitResume(@RequestBody UploadDTO UploadDTO)
	{
		String status=null;
		ResponseMessageWrapper responseMessageWrapper = new ResponseMessageWrapper();
		EmployeeMasterDTO employeeMasterDTO=UploadDTO.getEmployeeMasterDTO();
		String file=UploadDTO.getFilePath();
		LOG.debug("armg upload resume() called ");

		try 
		{
			status = armgService.uploadResume(file,employeeMasterDTO);
			responseMessageWrapper.setResponseMessage(status);
		}
		catch (Exception exception) 
		{
		    LOG.error("exception in armg restws /armg/upload : " + exception);
		    System.out.println("exception in armg restws /armg/upload : " + exception);
		    exception.printStackTrace();
			//throw new QEPMSApplicationException("Upload of resume failed...Error while trying to authenticate user Error details!" +exception);
		}
		return responseMessageWrapper;
	}
	
	
	//------------ Code added by Mukunda for armg export functionality------
	@RequestMapping(value = "/armg/exportresume",method = RequestMethod.GET)
	public @ResponseBody ResponseMessageWrapper exportResume(@RequestParam(value = "resumeid", required = false)
												final Integer resumeid, HttpServletRequest request, HttpServletResponse response)
	{
		ResumeDTO resumeDTO = new ResumeDTO();
		String status=null;
		
		ResponseMessageWrapper responseMessageWrapper = new ResponseMessageWrapper();
		LOG.debug("armg exportResume() called ");

		try 
		{
			status = armgService.exportResume(resumeid,request,response);
			responseMessageWrapper.setResponseMessage(status);
			
		}
		catch (Exception exception) 
		{
		    LOG.error("Exception Occured for armg export in restws: " + exception);
		    System.out.println("Exception Occured for armg export: " + exception);
		    exception.printStackTrace();
			//throw new QEPMSApplicationException("Error while trying to export resume Error details: " + exception.getMessage());
		}
		return responseMessageWrapper;

//--------Code end----------------------------
		
	}
	@RequestMapping(value = "/armg/uploademployeedetails",method = RequestMethod.POST)
	public @ResponseBody ResponseMessageWrapper submitEployeeDetails(@RequestBody  EmployeeMasterWrapperDTO employMasterWrapper)
	{
		String status=null;
		ResponseMessageWrapper responseMessageWrapper = new ResponseMessageWrapper();
		LOG.debug("armguploademployeedetails() called");

		try 
		{
            List<EmployeeMaster> employeeMasterList = employMasterWrapper.getEmployeeMasterList();
			status = armgService.insertEmployeeMaster(employeeMasterList);
			responseMessageWrapper.setResponseMessage(status);

		}
		catch (Exception exception) 
		{
		    LOG.error("Exception Occured in upload employeedetails-armg restws : " + exception);
//			throw new QEPMSApplicationException(
//					"Error while trying to upload resume Error details: " + exception.getMessage());
		    exception.printStackTrace();
		}
		return responseMessageWrapper;
	}
	/*
	 * View particular resume 
	 */
	@RequestMapping(value = "/armg/viewresume", method = RequestMethod.GET)
	public @ResponseBody
	ResumeDTO viewResume(
			@RequestParam(value = "resumeid", required = false) final Integer resumeid){
			ResumeDTO resumeDTO = new ResumeDTO();
		LOG.debug("vieweachResume() of employee called:resumeId:"+resumeid);
		
		try {
			resumeDTO = armgService.viewResume(resumeid);
		} catch (Exception exception) {
			LOG.error("Error while trying to vieweachresume. Error details : " + exception);
//			throw new QEPMSApplicationException("Error while trying to get resumeList Error details:  | Error details: "  + exception.getMessage());
			exception.printStackTrace();
		}

		return resumeDTO;
	}
	
}

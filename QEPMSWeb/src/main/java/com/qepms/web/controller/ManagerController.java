/**
 * 
 */
package com.qepms.web.controller;

/**
 * @author AshwiniK
 *
 */
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.qepms.web.constants.QEPMSWebConstants;
import com.qepms.web.util.RESTUtil;

/*
 Manager controller inorder to route the jsp pages as per the request by user
 */

@Controller
public class ManagerController {
	
	@Autowired 
	RESTUtil restUtil;
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ManagerController.class);
	
	@RequestMapping(value = "/manager/landingpage", method = RequestMethod.GET)
	public String showManagerLandingPage() {
		LOG.debug("showManagerLandingPage() called");
		return QEPMSWebConstants.Manager.LANDING_PAGE_PATH;
	}
	
	
	@RequestMapping(value = "/manager/allresume", method = RequestMethod.GET)
	public String showManagerAllResume() {
		LOG.debug("showAllResume() called");
		return QEPMSWebConstants.Manager.ALLRESUME_PAGE_PATH;
		

	}

	@RequestMapping(value = "/manager/approvedresume", method = RequestMethod.GET)
	public String showManagerApprovedResume() {
		LOG.debug("showAllResume() called");
		return QEPMSWebConstants.Manager.APPROVED_RESUME_PAGE_PATH;
		

	}
	
	@RequestMapping(value = "/manager/rejectedresume", method = RequestMethod.GET)
	public String showManagerRejectedResume() {
		LOG.debug("showManagerRejectedResume() called");
		return QEPMSWebConstants.Manager.REJECTED_RESUME_PAGE_PATH;
		

	}
	

	
	@RequestMapping(value = "/manager/managerviewofresume", method = RequestMethod.GET)
		public String showManagerViewOfResume() {
			LOG.debug("showManagerViewOfResume() called");
		
			return QEPMSWebConstants.Manager.MANAGERVIEWOFRESUME_PAGE_PATH;
}
	
		
		/*@RequestMapping(value = "/manager/rejectresume", method = RequestMethod.POST)
		public ModelAndView rejectResume(
				@ModelAttribute @Valid ResumeDTO resumeDTO,
				BindingResult result) {
			LOG.debug("rejectResume() called from controller");

			ResponseMessageWrapper responseMessageWrapper = null;
			try {
				responseMessageWrapper = restUtil.putData(resumeDTO,
						QEPMSWebConstants.Manager.MANAGER_REJECT_RESUME_WS_PAGE_PATH);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ModelAndView(
					QEPMSWebConstants.Manager.MANAGER_REJECT_RESUME_WS_PAGE_PATH,
					"responseMessageWrapper", responseMessageWrapper);
		}
		*/
	
}

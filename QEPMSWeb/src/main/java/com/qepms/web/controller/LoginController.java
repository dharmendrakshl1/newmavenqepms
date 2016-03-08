package com.qepms.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.qepms.infra.constants.CommonConstants;
import com.qepms.infra.dto.login.UserDTO;
import com.qepms.infra.dto.sample.DepartmentDTO;
import com.qepms.infra.misc.wrapper.ResponseMessageWrapper;
import com.qepms.web.constants.QEPMSWebConstants;
import com.qepms.web.util.RESTUtil;

@Controller
public class LoginController {
	
	@Autowired
	RESTUtil restUtil;
	
	private static final Logger LOG = LoggerFactory
			.getLogger(LoginController.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLandingPage() {
		LOG.debug("showLandingPage() called");
		return QEPMSWebConstants.Login.LOGIN_PAGE_PATH;
	}
	
	@RequestMapping(value = "/login/welcomepage", method = RequestMethod.GET)
	public String showWelcomePage() {
		LOG.debug("showWelcomePage() called");
		return QEPMSWebConstants.Login.WELCOME_PAGE_PATH;
	}
	
	/*
	 * authenticate user
	 */
	@RequestMapping(value = "/login/authenticate", method = RequestMethod.POST)
	public ModelAndView authenticate(
			@ModelAttribute @Valid UserDTO userDTO,
			BindingResult result,HttpServletRequest request) {
		LOG.debug("authenticate() called from controller");

		ResponseMessageWrapper responseMessageWrapper = null;
		try {
			
			responseMessageWrapper = restUtil.putData(userDTO,QEPMSWebConstants.Login.LOGIN_WS_PAGE_PATH);
			
			if(responseMessageWrapper.getResponseMessage().equals(CommonConstants.LOGIN_SUCCESS_STATUS))
			{
				request.setAttribute("userDTO",userDTO );
				return new ModelAndView(QEPMSWebConstants.Login.WELCOME_PAGE_PATH,"responseMessageWrapper", responseMessageWrapper);
			}
			else if(responseMessageWrapper.getResponseMessage().equals(CommonConstants.LOGIN_CUSTOM_MESSAGE)){
				responseMessageWrapper.setResponseMessage(CommonConstants.LOGIN_CUSTOM_MESSAGE);
			}
			else
			{
				responseMessageWrapper.setResponseMessage(CommonConstants.LOGIN_FAILURE_ERROR);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView(QEPMSWebConstants.Login.LOGIN_PAGE_PATH,"responseMessageWrapper", responseMessageWrapper);
		
		


		
	}
	
	
	/*
	 * logout
	 */
	@RequestMapping(value = "/login/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
		HttpSession session =request.getSession();
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
        for (int i = 0; i < cookies.length; i++) {
         cookies[i].setMaxAge(0);
        }
       return QEPMSWebConstants.Login.LOGIN_PAGE_PATH;
    }
	
}

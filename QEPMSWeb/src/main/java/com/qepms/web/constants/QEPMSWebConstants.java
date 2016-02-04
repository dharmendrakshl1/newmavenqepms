package com.qepms.web.constants;

public interface QEPMSWebConstants {

	static interface Employee{

		// Paths to JSP pages
		public static final String LANDING_PAGE_PATH = "/employee/landing";
		public static final String SUBMITTEDRESUME_PAGE_PATH = "/employee/submittedresume";
		public static final String DRAFTRESUME_PAGE_PATH = "/employee/draftresume";
		public static final String REJECTEDRESUME_PAGE_PATH = "/employee/rejectedresume";
		public static final String SUBMITRESUME_PAGE_PATH = "/employee/submitresume";
		public static final String VIEWRESUME_PAGE_PATH ="/employee/viewresume";
		public static final String EDITRESUME_PAGE_PATH ="/employee/editresume";
		public static final String ADD_SKILL_POPUP_PAGE_PATH = "/common/skilldetail";

		// Paths to RESTful Web Services
		public static final String SUBMITRESUME_MSTR_WS_PATH="/employee/submitsresume";
		public static final String RESUME_MSTR_WS_PATH="/employee/resume";
		public static final String SUCCESSMAIL_MSTR_WS_PATH="/employee/sendmail";
		/*
		 * used to view resume
		 */
		public static final String EMPLOYEEVIEWRESUME_WS_PATH="/employee/vieweachresume";
		
		public static final String EMPLOYEEEDITRESUME_WS_PATH="/employee/editeachresume";
		public static final String TITLE_MSTR_WS_PATH = "/title/emptitle"; 
		public static final String SKILL_TYPE_NAME_WS_PATH = "/employee/skilltypenames";
		public static final String GET_EMPLOYEE_DRAFT_DETAILS ="/employee/getdraftresume";

	}
	static interface Manager{

		// Paths to JSP pages
		public static final String LANDING_PAGE_PATH = "/manager/landing";
		public static final String ALLRESUME_PAGE_PATH = "/manager/allresume";
		public static final String APPROVED_RESUME_PAGE_PATH = "/manager/approvedresume";
		public static final String REJECTED_RESUME_PAGE_PATH = "/manager/rejectedresume";
		public static final String MANAGERVIEWOFRESUME_PAGE_PATH = "/manager/managerviewofresume";
		public static final String MANAGER_ACCEPT_RESUME_PAGE_PATH = "/manager/accept";
		public static final String MANAGER_REJECT_RESUME_PAGE_PATH = "/manager/reject";

		// Paths to RESTful Web Services
		public static final String EMPLOYEE_MASTER_WS_PAGE_PATH = "/manager/listofresumes";
		public static final String EMPLOYEE_BASED_ON_MANAGER_EMAIL_WS_PAGE_PATH = "/manager/listofresumesbasedonemail";
		public static final String MANAGER_VIEW_RESUME_WS_PAGE_PATH = "/manager/viewresume";
		public static final String MANAGER_REJECT_RESUME_WS_PAGE_PATH = "/manager/rejectresume";
		public static final String MANAGER_ACCEPT_RESUME_WS_PAGE_PATH = "/manager/acceptresume";
		
		
	}
	
	static interface Login{

		// Paths to JSP pages
		public static final String LOGIN_PAGE_PATH = "login/loginpage";
		public static final String WELCOME_PAGE_PATH = "login/welcomepage";

		// Paths to RESTful Web Services
		public static final String LOGIN_WS_PAGE_PATH = "/login/userdetails";
	
	}
	
	
	static interface Armg
	{
		// Paths to JSP pages
		public static final String LANDING_PAGE_PATH = "/armg/landing"; 
		public static final String ARMGRESUME_PAGE_PATH = "/armg/armgresume";
		public static final String ARMGVIEWOFRESUME_PAGE_PATH = "/armg/armgviewofresume";
		public static final String IMPORTRESUME_PAGE_PATH = "/armg/importresume";
		public static final String CONFIGURATION_PAGE_PATH = "/armg/armgconfiguration";
		public static final String UPLOADRESUMETEMPLATE_PAGE_PATH = "/armg/uploadresumetemplate";
		public static final String UPLOADEXCEL_PAGE_PATH = "/armg/uploadeExcel";
		public static final String DOWNLOAD_PAGE_PATH = "/armg/downloadresume";
		
		// Paths to RESTful Web Services
		public static final String RESUME_MSTR_WS_PATH= "/armg/resume" ;
		public static final String UPLOADRESUME_MSTR_WS_PATH= "/armg/upload" ;
		public static final String EXPORT_RESUME_WS_PATH ="/armg/exportresume";
		public static final String UPLOAD_EMPLOYEE_MSTR_WS_PATH="/armg/uploademployeedetails";
		
		public static final String ARMGVIEWRESUME_WS_PATH="/armg/viewresume";
		
	
	}
	
	

}

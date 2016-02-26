
<%@page import="com.qepms.web.constants.QEPMSWebConstants"%>
<%@page import="com.qepms.infra.misc.wrapper.ResponseMessageWrapper"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.apache.shiro.subject.Subject"%>
<%@page import="com.qepms.web.util.RESTUtil"%>
<%@page import="com.qepms.infra.dto.armg.EmployeeMasterDTO"%>
<%@page import="com.qepms.infra.dto.armg.EmployeeMasterDTOList"%>
<%@page import="com.qepms.infra.dto.armg.SkillDTO"%>
<%@page import="com.qepms.infra.dto.armg.SkillDTOList"%>
<%@page import="com.qepms.infra.dto.armg.EducationDTO"%>
<%@page import="com.qepms.infra.dto.armg.EducationDTOList"%>
<%@page import="com.qepms.infra.dto.armg.ResumeDTO"%>
<%@page import="com.qepms.infra.dto.armg.ProjectDTO"%>
<%@page import="com.qepms.infra.dto.armg.ResumeDTOList"%>
<%@page import="com.qepms.common.util.SkillType"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	ApplicationContext context = RequestContextUtils.getWebApplicationContext(request); 
 	RESTUtil restUtil = (RESTUtil)context.getBean("restUtil");
 	String resumeid = request.getParameter("resumeid");
 	String status = request.getParameter("status");
 	
 	ResumeDTO resume = (ResumeDTO) restUtil.getData(QEPMSWebConstants.Employee.EMPLOYEEVIEWRESUME_WS_PATH+"?resumeid="+resumeid,ResumeDTO.class); 
 	pageContext.setAttribute("resume", resume);  	 
 	List<EducationDTO> educations = resume.getEducations();
 	pageContext.setAttribute("educations", educations); 
 	List<SkillDTO> skillList = resume.getSkills();
 	pageContext.setAttribute("skillList", skillList); 
 	List<ProjectDTO> projectList = resume.getProjects();
 	pageContext.setAttribute("projectList", projectList);
 	List<String> proSummeryList = resume.getProSummeryList();
 	pageContext.setAttribute("proSummeryList", proSummeryList); 
 	
 	//Skill type (primary/secondary)
 	String primary=SkillType.PRIMARY.name();
 	String secondary=SkillType.SECONDARY.name();
 	String check="null";
 	pageContext.setAttribute("primary", primary); 
 	pageContext.setAttribute("secondary", secondary); 
 	pageContext.setAttribute("check", check);
 	pageContext.setAttribute("status", status);
 	pageContext.setAttribute("managerApprovalStatus",resume.getManagerApprovalStatus());
 	System.out.println(resume.getManagerApprovalStatus());
 	 	
 %> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>Quinnox Employee Profile Management System</title>

<link href="<c:url value="/resources/styles/styles.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value="/resources/scripts/common/distributormgr.js"/>"></script>
<script src="<c:url value="/resources/scripts/common/jquery-1.8.3.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/simpletreemenu.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/common.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/datepicker.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/SpryTabbedPanels.js"/>" type="text/javascript"></script>


</head>

<body>
<%-- <div id="header">
  <%@include file="../common/header.jsp" %> 
  <script>distributormgr_header();selmenu('mn_employee');</script> 
</div> --%>
<div id="header">
<%@include file="../common/header.jsp" %>
  <script>
  var role="<%=user.getRole()%>";
  distributormgr_header(role);
  selmenu('mn_employee');
  </script> 
</div>
<!-- end of header !-->
<div id="content">
  <div id="leftNavigation"> 
    <script>distributormgr_employee_submenu('2','m_submitresume');</script> 
  </div>
  <!-- end of left navigation !-->
  <div id="contenPan">
    <div id="contenPan_LeftNavToggle"><a href="javascript:" onclick="toggle_leftmenu();"><img id="img_leftmenu" src="<c:url value="/resources/images/menuarrowopen.png"/>" alt="Close Left menu" title="Close Left menu" /></a></div>
    <div id="contenPan_main">
      <h1>View Resume
        
      </h1>
      <div id="TabbedPanels1" class="TabbedPanels">
        <ul class="TabbedPanelsTabGroup">
          <li class="TabbedPanelsTab" tabindex="0">View Resume</li>
         
        </ul>
        <div class="TabbedPanelsContentGroup">
          <div class="TabbedPanelsContent">
            
            <div class="subsection" id="forms">
              <ul>
                <li>
                  <div class="label">Title :</div>
                  <div class="fleft"><c:out value="${resume.employeeMaster.title}" /></div>
				</li>
				<li>
                  <div class="label">Employee Id : </div>
                 
                  <div class="fleft" ><c:out value="${resume.employeeMaster.empId}" />
                  </div>
				 </li>
				 <li>
                  <div class="label">Full Name : </div>
                  <div class="fleft"><c:out value="${resume.employeeMaster.name}" /></div>
                </li>
                <c:forEach items="${skillList}" var="skills">
					<c:choose>
						<c:when test="${skills.skillCatagory == primary}">
							<li>
								<div class="label">Primary Skills :</div>
								<div class="fleft"><c:out value="${skills.skill}" /></div>
							</li>
						</c:when>
						<c:when test="${skills.skillCatagory == secondary}">
							<li>
								<div class="label">Secondary Skills:</div>
								<div class="fleft"><c:out value="${skills.skill}" /></div> 
							</li>
						</c:when>
             		</c:choose>
             	</c:forEach>
             <li>
                  <div class="label">Total Experience:</div>
                  <div class="fleft">${resume.totalExperience} &nbsp;<font style="bold" class="label"></font></div>
                </li>
				 <li>
                  <div class="label">Relevant Experience:</div>
                  <div class="fleft">${resume.relevantExperience} &nbsp;<font  style="bold" class="label"></font></div>
                </li>
                <li>
                	<div class="label">Domain:</div>
                	<div class="fleft">${resume.domain} &nbsp;<font  style="bold" class="label"></font></div>
                </li>
                
                <li>
                	<div class="label">Certification:</div>
                	<div class="fleft">${resume.certification} &nbsp;<font  style="bold" class="label"></font></div>
                </li>
                
                <li>
                <table>
                <tr>
                <td><div class="label">Professional Summary:</div></td>
                <td>
                	<c:if test="${proSummeryList != null}">
	                	<c:forEach items="${proSummeryList}" var="summery">
					    	<img src="/qepms/resources/images/bullet_point.PNG"/> &nbsp; ${summery}<br/>
						</c:forEach>
					</c:if>
				</td>
                </tr>
                </table>
               </li>
              </ul>
            </div>
			<!-- start of TechnicalSkills !-->
            <div class="subsection">
             <h2>Technical Skills</h2>
              <div class="datagrid">
                <table> 
                  <thead>
                  <tr>
                      <th width="15%">Skill Category </th>
                      <th>Skill Value </th>
                    </tr>
                  </thead>
                       <tbody>
						<c:forEach items="${skillList}" var="skills">
							<c:choose>
								<c:when test="${skills.skillCatagory=='TECHNICAL'}">
			              			<tr>
	                      				<td><div><span></span>${skills.skillType}</div></td>
	                      				<td>${skills.skill}</td>
	                    			</tr>
                     			</c:when>
                     		</c:choose></c:forEach>
                     </tbody>
                     <tfoot>
                    <tr class="totalsec">
                      <td colspan="2">&nbsp;</td>
                      
                    </tr>
                  </tfoot>
                </table>
              </div>
             </div>
            <!-- end of TechnicalSkills !-->
			
			
		   <!-- Start :: Project Details Section -->
            <div class="subsection">
              <h2>Professional Experience</h2>
              <div class="datagrid">
               
                
                 <table> 
                  <thead>
                    <tr>
                      <th>Client Name</th>
                      <th>Duration</th>
					  <th>Project Name</th>
					  <th>Role</th>
					  <th>Project Brief</th>
					  <th>Responsibilities</th>
					  <th>Environment</th>
					 </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${projectList}" var="projects">
                  
                   <tr>
                      
					    <td><c:out value="${projects.clientName}"></c:out></td>
					    <td><c:out value="${projects.duration}"></c:out></td>
					    <td><c:out value="${projects.projectName}"></c:out></td>
					    <td><c:out value="${projects.role}"></c:out></td>
					    <td><c:out value="${projects.projectDesc}"></c:out></td>
					    <td><c:out value="${projects.responsibility}"></c:out></td>
					    <td><c:out value="${projects.environment}"></c:out></td>
                   </tr>
                    </c:forEach>
                    
                  </tbody> 
                  <tfoot>
                    <tr class="totalsec">
                      <td colspan="7">&nbsp;</td>
                      
                    </tr>
                  
                  </tfoot>
                </table>
              </div>
             </div>
            <!-- End :: Project Details Section -->
			
			
			
			<!-- Start :: Education Details Section -->
            <div class="subsection">
              <h2>Education Details</h2>
              <div class="datagrid">
                <table> 
                  <thead>
                    <tr>
                      <!-- <th>Degree</th>
                      <th>Aggregate(%)</th> -->
					  
					  <th>Degree</th>
					  <th>Specialization</th>
					 <th>College/University</th>
					 <th>Year of Passing(YYYY)</th>
                    </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${educations}" var="education">
                  
                   <tr>
                      
                       <%-- <td><c:out value="${education.aggregate}"></c:out></td> --%>
					   
					  <td><c:out value="${education.degree}"></c:out></td>
					  <td><c:out value="${education.specialization}"></c:out></td>
					   <td><c:out value="${education.college}"></c:out></td>
					   	<td><c:out value="${education.yearOfPassing}" /></td>
					  <!--   <td><c:out value="${education.yearOfPassing}"></c:out></td> --> 
					 
					 
                    </tr>
                    </c:forEach>
                    
                  </tbody> 
                  <tfoot>
                    <tr class="totalsec">
                      <td colspan="5">&nbsp;</td>
                      
                    </tr>
                  
                  </tfoot>
                </table>
              </div>
             </div>
            <!-- end of TechnicalSkills !-->
       
       
            <div class="bottombtn">
            
           <c:if test="${status != 'SUBMITTED' && managerApprovalStatus != 'APPROVED'}"> 
              <input type="button" value="Edit" onclick="location.href='/qepms/employee/editresume?resumeid=${resume.resumeid}'" />
           </c:if>
              <input type="button" value="Back" onclick="history.back();" />
            </div>
          </div>
          <!-- end of TabbedPanelsContent !-->
          
   
        </div>
      </div>
    </div>
  </div>
  <!-- end of content Panel !--> 
</div>
<!-- end of content !-->
<div id="footer"> 
  <script>footer_01();</script> 
</div>
<!-- end of footer !--> 
<script type="text/javascript">
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
</script>
</body>
</html>

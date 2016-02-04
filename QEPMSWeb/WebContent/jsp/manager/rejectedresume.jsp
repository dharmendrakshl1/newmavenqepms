<%@page import="com.qepms.web.constants.QEPMSWebConstants"%>
<%@page import="com.qepms.infra.misc.wrapper.ResponseMessageWrapper"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="java.util.List"%>
<%@page import="com.qepms.infra.dto.armg.ResumeDTO"%>
<%@page import="com.qepms.infra.dto.armg.ResumeDTOList"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.apache.shiro.subject.Subject"%>
<%@page import="com.qepms.web.util.RESTUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>Quinnox Employee Profile Management System</title>
<link href="<c:url value="/resources/styles/styles.css"/>" rel="stylesheet" type="text/css" />
<link href="<c:url value="/resources/styles/jquery.dataTables.css" />" rel="stylesheet"  type="text/css" />

<script type="text/javascript" src="<c:url value="/resources/scripts/common/distributormgr.js"/>"></script>
<script src="<c:url value="/resources/scripts/common/jquery-1.8.3.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/jquery.dataTables.js" />"type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/simpletreemenu.js"/>" type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/common.js"/>" type="text/javascript"></script>



<script>
var asInitVals = new Array();
jQuery.fn.dataTableExt.oSort['string-case-asc'] = function (x, y) {
    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
};

jQuery.fn.dataTableExt.oSort['string-case-desc'] = function (x, y) {
    return ((x < y) ? 1 : ((x > y) ? -1 : 0));
};


$(document).ready(function () {
	
	 // Setup - add a text input to each footer cell
    $('.jqueryDataTable tfoot th').each( function () {
        var title = $('.jqueryDataTable thead th').eq( $(this).index() ).text();
        $(this).html( '<div style="float: left;text-align: left;"><input type="text" placeholder="Search '+title+'" />' );
    } );
 
    // DataTable
    var table = $('.jqueryDataTable').DataTable({
    		"oLanguage": {

                'sSearch': "Search",
                'sLengthMenu': '<strong>Display</strong> <select>'+
                '<option value="10">10</option>'+
                '<option value="20">20</option>'+
                '<option value="30">30</option>'+
                '<option value="40">40</option>'+
                '<option value="50">50</option>'+
                '<option value="-1">All</option>'+
                '</select>'

            },
        	 'bSort':true,
        	 'sPaginationType':'full_numbers',
        	 'bFilter': true,
        	 'bSortClasses':false,
        	 'iDisplayLength':10		
    });
 
    // Apply the filter
    table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', table.column( colIdx ).footer() ).on( 'keyup change', function () {
            table
                .column( colIdx )
                .search( this.value )
                .draw();
        } );
    } );
	
	
    
});
</script>

</head>

<body>
<div id="header">
    <%@include file="../common/header.jsp" %>
<%
 	ApplicationContext context = RequestContextUtils.getWebApplicationContext(request); 
 	RESTUtil restUtil = (RESTUtil)context.getBean("restUtil");
 	/* String reportingManager = user.getEmployeeName();
 	
 	// Load the Member grid (first and every page reload)
 	List<ResumeDTO> resumelist = (List<ResumeDTO>)restUtil.getData(QEPMSWebConstants.Manager.EMPLOYEE_BASED_ON_MANAGER_EMAIL_WS_PAGE_PATH+"?managerApprovalStatus=Approved&reportingManager="+reportingManager, ResumeDTOList.class);
 	pageContext.setAttribute("resumelist", resumelist); */
 	
 	//Added By Chethana
 	 	String reportingManagerEmail = user.getEmployeEmail();
 	 	
 	 	// Load the Member grid (first and every page reload)
 	 	List<ResumeDTO> resumelist = (List<ResumeDTO>)restUtil.getData(QEPMSWebConstants.Manager.EMPLOYEE_BASED_ON_MANAGER_EMAIL_WS_PAGE_PATH+"?managerApprovalStatus=Rejected&reportingManageremail="+reportingManagerEmail, ResumeDTOList.class); 
 	 	pageContext.setAttribute("resumelist", resumelist);
 	 	//[End] Added by Chethana
 	
 	
%> 

	<script>
		var role="<%=user.getRole()%>";
	  	distributormgr_header(role);
		selmenu('mn_manager');
	</script>	
</div>
<!-- end of header !-->
<div id="content">
	<div id="leftNavigation">
     <script>distributormgr_manager_submenu('0','m_2_rejectedresume');</script>
    </div>
    <!-- end of left navigation !-->
        <div id="contenPan"><div id="contenPan_LeftNavToggle"><a href="javascript:" onclick="toggle_leftmenu();"><img id="img_leftmenu" src="<c:url value="/resources/images/menuarrowopen.png"/>" title="Close Left menu" /></a></div>
    <div id="contenPan_main">
    	<h1>Rejected Resume(s)
        </h1>
 
        <div class="subsection">
    	 <div class="datagridfordatatable">
    <table class="jqueryDataTable">
      <thead>
        <tr>
        	<th>EmpID</th>
            <th>Employee Name</th>
            <th>Primary Skills</th>
            <th>Secondary Skills</th>
            <th>Last Updated Date</th>
            <th>Rejected Date</th>
        </tr>
       </thead>
         <tbody>
         <c:forEach items="${resumelist}" var="resume">
           <tr>
             <td><a href="/qepms/manager/managerviewofresume?resumeid=${resume.resumeid}&status=REJECTED">${resume.employeeMaster.empId}</a></td>
             <td>${resume.employeeMaster.name}</td>
             
             <c:forEach items="${resume.skills}" var="skill">
              <c:if test="${skill.skillCatagory=='PRIMARY'}">
				 <td>${skill.skill}</td>
			 </c:if>
			 </c:forEach>
			  <c:forEach items="${resume.skills}" var="skill">
              <c:if test="${skill.skillCatagory=='SECONDARY'}">
				 <td>${skill.skill}</td>
			 </c:if>
			 </c:forEach>
			 <jsp:useBean id="updateDate" class="java.util.Date"/>
             <c:set target="${updateDate}" property="time" value="${resume.updatedDate}"/>
			  <td> <c:out value="${updateDate}"/></td>
			 
			  <jsp:useBean id="approveDate" class="java.util.Date"/>
			 <c:if test="${not empty approveDate}">
             <c:set target="${approveDate}" property="time" value="${resume.rejectedDate}"/>
              <td> <c:out value="${approveDate}"/></td> 
			</c:if>	  
			 
           </tr>
         </c:forEach>   
        </tbody>
         <tfoot>
        <tr>
        	<th>EmpID</th>
            <th>Employee Name</th>
            <th>Primary Skills</th>
            <th>Secondary Skills</th>
            <th>Updated Date</th>
            <th>Approved Date</th>
        </tr>
        </tfoot>
       
     </table>	
    
    </div>
    <div id='tablePagination'>
	
  	
  </div>
  </div>
    </div>
    <!-- end of content Panel !-->
</div>
</div>


<!-- end of content !-->
<div id="footer">
	<script>footer_01();</script>
</div>
<!-- end of footer !-->

<!--  datatable scripts -->

<script src="<c:url value="/resources/scripts/common/jquery.dataTables.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="http://code.jquery.com/ui/1.9.2/jquery-ui.js"/>" type="text/javascript"></script>


</body>
</html>

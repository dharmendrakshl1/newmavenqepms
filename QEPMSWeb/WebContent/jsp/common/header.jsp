    <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.Date"%>
    <%@page import="com.qepms.infra.dto.login.UserDTO"%>
    <%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
   <%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="java.util.List"%>
<%@page import="com.qepms.web.util.RESTUtil"%>
    <%
    UserDTO user=(UserDTO) session.getAttribute("user");
    if(user==null)
    {
    	ApplicationContext appContext = RequestContextUtils.getWebApplicationContext(request); 
     	RESTUtil appUtil = (RESTUtil)appContext.getBean("restUtil");
     	session.invalidate();
    	response.sendRedirect(appUtil.efmsAppBaseUrl+"/login/logout");
    }
    pageContext.setAttribute("user", user);
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="<c:url value="/resources/styles/NewDesign.css"/>" type="text/css" />
</head>
<body>

<div id="header">
<div class="headerLeft-new">
 <div class="inner_wrap">
<div id="header_container">
 <div id="application_contaienr_left">
	<div class="Qpms-Logo">
		<span class="loGo">QPMS</span>
		<span class="titleTxt">The Employee Profile Management System</span>
	</div>
</div>
	<div class="welcomeTxt"><p>Welcome <span class="username">${user.employeeName}</span> <span class="userrole">(${user.jobTitle})</span></p></div>
</div>
</div>
</div>


<div class="headerRight">
	<img src="<c:url value="/resources/images/quinnox_logo.jpg"/>" alt="Quinnox Logo" title="Quinnox Logo" />
</div>
</div>
</br>
<!-- 
<div id="date"></div>

<script>
var d = new Date();
document.getElementById("date").innerHTML = d.toDateString();
</script>
 -->
</body>
</html>
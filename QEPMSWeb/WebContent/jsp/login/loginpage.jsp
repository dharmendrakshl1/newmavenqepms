<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="com.qepms.infra.misc.wrapper.ResponseMessageWrapper"%>
<%@page import="com.qepms.infra.constants.CommonConstants"%>
<%
String error=CommonConstants.LOGIN_FAILURE_ERROR;
String customMessage=CommonConstants.LOGIN_CUSTOM_MESSAGE;
pageContext.setAttribute("error", error);
pageContext.setAttribute("customMessage", customMessage);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>Quinnox Employee Management System</title>
<link rel="stylesheet" href="<c:url value="/resources/styles/login.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/styles/styles.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/styles/NewDesign.css"/>" type="text/css" />
<script src="<c:url value="/resources/scripts/common/jquery-1.8.3.min.js"/>"></script>
<script src="<c:url value="/resources/scripts/common/login.js"/>"></script>
<script type="text/JavaScript">



function preventBack(){window.history.forward();}
setTimeout("preventBack()", 0);
window.onunload=function(){null};

	

</script>
</head>

<body>
<%-- <div id="container"><img src="<c:url value="/resources/images/quinnox_logo.png"/>" alt="Quinnox Logo" title="Quinnox Logo" /> --%>
<div id="container">

<!-- <div id="headerNew"> -->
<!-- <div class="headerLeft-new"> -->
<!-- 	<div class="Qpms-Logo"> -->
<!-- 		<span class="loGo">QPMS</span> -->
<!-- 		<div class="subtitle">The Employee Profile Management System</div> -->
<!-- 	</div> -->
<!-- </div> -->

<!-- <div class="headerRight"> -->
<%-- 	<img src="<c:url value="/resources/images/quinnox_logo.png"/>" alt="Quinnox Logo" title="Quinnox Logo" /> --%>
<!-- </div> -->
<!-- </div> -->
<div id="header">
               <div class="inner_wrap">
                  <div id="header_container">
                     <div id="application_contaienr_left">
                       <div class="qfocustitle">QPMS</div>
                        <div class="floatl">
                           <img src="<c:url value="/resources/images/chevron.gif"/>" alt="QTRF"/>
                        </div>
                        <div class="qfocussubtitle">The Employee Profile Management System
                        </div>
                     </div>
                     <div id="logo_container">
                        <img src="<c:url value="/resources/images/quinnox_logo.jpg"/>" width="305"
                           height="74" alt="Quinnox_Logo"></img>
                     </div>
                  </div>
               </div>
            </div>
  <div id="content">
    <div id="loginsection">
     <div class="loginBlock">
      
      <div id="loginform">
     <span class="customError">${responseMessageWrapper.responseMessage}</span>
        <form:form id="myform" action="/qepms/login/authenticate" modelAttribute="userDTO" commandName="userDTO">
        
     		 
          <ul>
          
            <li>
             
              <div class="left">User Name:</div>
              <div class="right">
                <input type="text" value="" name="userName" id="txtusername" class="txtusername" />
              </div>
              <div class="clear"></div>
            </li>
            <li>
              <div class="left">Password: </div>
              <div class="right">
                <input type="password" value="" name="password" id="txtpassword" class="txtpassword" />
              </div>
              <div class="clear"></div>
            </li>
            <li class="frmbtn">
              <div class="left">&nbsp;</div>
              <input type="submit" value="Login" name="btn_submit" id="btn_submit" class="btn_submit" >
            </li>
          </ul>
        </form:form>
      </div>
      </div>
    </div>
 </div> 
 <div id="note_container">
						<ul>
							<li class="title">Note</li>
							<li>Your User ID will be your email ID. <span>For
									e.g. if your Email ID is "abc@quinnox.com" then your User ID
									"abc".</span>
							</li>
							<li>Your password is the password used for your Email
								account.</li>
							<li><strong style="background: #fffabe;">Recommended
									Browsers: IE 7.0 and later versions till IE 9.0 in
									compatibility mode. </strong></li>
							<li>Recommended Screen resolution : 1024 by 768 pixels</li>
						</ul>
						<div class="clr"></div>
					</div>  
</div>
</body>
</html>
<%@page import="com.qepms.web.constants.QEPMSWebConstants"%>
<%@page import="com.qepms.infra.misc.wrapper.ResponseMessageWrapper"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="java.util.*"%>
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
<%@page import="com.qepms.common.util.EmployeeSubmissionStatus"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@page import="com.qepms.common.util.SkillType"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- <%@include file="../common/header.jsp" %> --%>


<%
	UserDTO userName = (UserDTO) session.getAttribute("user");
	ApplicationContext context = RequestContextUtils
			.getWebApplicationContext(request);
	Integer empId = userName.getEmpId();
	RESTUtil restUtil = (RESTUtil) context.getBean("restUtil");
	ResumeDTO resumes = (ResumeDTO) restUtil
			.getData(
					QEPMSWebConstants.Employee.GET_EMPLOYEE_DRAFT_DETAILS
							+ "?empId="
							+ empId
							+ "&employeeSumbissionStatus=PENDING&managerApprovalStatus=PENDING",
					ResumeDTO.class);
					
					ResumeDTO resumedata = (ResumeDTO) restUtil.getData(QEPMSWebConstants.Employee.GET_EMPLOYEE_DRAFT_DETAILS
							+ "?empId="
							+ empId
							+ "&employeeSumbissionStatus=SUBMITTED&managerApprovalStatus=APPROVED",
					        ResumeDTO.class);
					System.out.println("RESUMEID:"+resumedata.getResumeid());
					
					
					//sampad start
					if(resumedata.getResumeid()!=null){
					  ResumeDTO resume = (ResumeDTO) restUtil.getData(QEPMSWebConstants.Employee.EMPLOYEEVIEWRESUME_WS_PATH+"?resumeid="+resumedata.getResumeid(),ResumeDTO.class);
    
 	pageContext.setAttribute("resume", resume);  	 
 	List<EducationDTO> educations = resume.getEducations();
 	pageContext.setAttribute("educations", educations); 
    List<SkillDTO> skillList = resume.getSkills();
 	pageContext.setAttribute("skillList", skillList); 
 	List<ProjectDTO> projectList = resume.getProjects();
 	pageContext.setAttribute("projectList", projectList); 
 	
 	    //Skill type (primary/secondary)
 	 	String primary=SkillType.PRIMARY.name();
 	 	String secondary=SkillType.SECONDARY.name();
 	 	String check="null";
 	 	pageContext.setAttribute("primary", primary); 
 	 	pageContext.setAttribute("secondary", secondary); 
 	 	pageContext.setAttribute("check", check);
 	 	}
												//samapd end
						
	if (resumes.getResumeid() != null) {
		response.sendRedirect(RESTUtil.efmsAppBaseUrl
				+ "/employee/viewresume?resumeid="
				+ resumes.getResumeid());
		 /*  RequestDispatcher rd=request.getRequestDispatcher(RESTUtil.efmsAppBaseUrl+"/employee/viewresume?resumeid="+resumes.getResumeid());
		rd.forward(request, response);  */
	}
	///sampad--fro auto populat
	
 	//String resumeid = request.getParameter("resumeid");
 
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title>Quinnox Employee Profile Management System</title>
<link href="<c:url value="/resources/styles/styles.css"/>"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<c:url value="/resources/scripts/common/distributormgr.js"/>"></script>
<script
	src="<c:url value="/resources/scripts/common/jquery-1.8.3.min.js"/>"
	type="text/javascript"></script>
<script
	src="<c:url value="/resources/scripts/common/simpletreemenu.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/common.js"/>"
	type="text/javascript"></script>
<script
	src="<c:url value="/resources/scripts/common/SpryTabbedPanels.js"/>"
	type="text/javascript"></script>
<script src="<c:url value="/resources/scripts/common/datepicker.js"/>"
	type="text/javascript"></script>

<script>
$(document).ready(function() 
		{
	
			//On click of Add of technical skills-----------------function1
			$('#technicaldetailserror').hide();
			$('#projectdetailsserror').hide();
			$('#educationdetailserror').hide();
		
			$( "#btn_skilltypeadd" ).click(function() {
				
				//getting the value of textbox
				$('#technicaldetailserror').hide();
				$skillType=$('#skillid').val();
				$skillvalue=$('#skillvalue').val();
				$("#arrowBlue").show();
				$add = true;
				
				if($skillType==""||$skillType==null||$skillvalue==""||$skillvalue==null)
					{
					    $('#technicaldetailserror').show();
					    $('#technicaldetailserror').html('<font color="red">Please add both SkillType and SkillValue</font>');
					    $add = false;
					}
				
				
				if($add)
				{
					//appending table with value
					var y=document.getElementsByClassName("tempSkill");
					for(j=0;j<y.length;j++)
					{
						var temp2=y[i].innerHTML;
						if(temp2==$skillType)
						{
							alert('Duplicate Skill Type!! Please edit below options');
							return false;
						}
						
					}
					
					var x = document.getElementsByClassName("editSkill");
					for(i=0;i<x.length;i++)
					{						
						var temp=x[i].innerHTML;
						var trimVal=temp.trim();
						
						if(trimVal==$skillType)
						{
							alert('Duplicate Skill Type!! Please edit below options');
							return false;
						}
					
					
				}
					$("#technicaldetails table tbody:last").prepend(
							
						'<tr>' +
						'<td class="skilltypevalue"><div><span class="tempSkill">'+$skillType+'</span> </div></td>'  +
						'<td class="skillvalue">'+$skillvalue+'</td>' +
						'<td align="center" > <input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editTech($(this).parent().parent()); return false;" value="Edit" id="editSkills"> &nbsp; &nbsp;'+
						' <a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;"><img src="<c:url value="/resources/images/delete.png"/>" class="deletetechnicaldetails" align="top" /></a></td>' +
						'</tr>' 
						);
					
					//refreshing the text field
					$('#skillid').val('');
					$skillvalue=$('#skillvalue').val('');
					 $('#technicaldetailserror').hide();
					
				}

			ui_rows();
			});
			 
			
			
			<%--	$('.skilltypevalue').live("click",function(){
				
				$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
				
			}); 
				
			
				$('.skillvalue').live("click",function(){
					
				$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
					
			});
				
				$('.txt_clientname').on("click",function(){
					
				$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
					
			});
				
				$('.txt_duration').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.txt_projectname').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.txt_role').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.txt_projectbrief').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.txt_responsibilities').live("click",function(){
	
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
		
				});
				
				$('.txt_environment').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
		
				});
				
				
				$('.degree').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.aggregate').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				
				$('.description').live("click",function(){
	
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
		
				});
				$('.textarea_college').live("click",function(){
					
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
						
				});
				$('.yearofpassing').live("click",function(){
	
					$(this).replaceWith("<input type='text' value='"+$(this).text()+"'/>");
		
				});--%>


			

		//on click of Add of project details-----------------------function2	
		
		$("#primaryskills").add("#secondaryskills").add("#professionalsummary").keyup(function (e) {
		    autoheight(this);
		    autowidth(this);
		});
		
		function autoheight(a) {
			//alert("autoheight");
		    $(a).height(10);
		    $(a).height($(a).prop('scrollHeight') + 10);
		}
		
		function autowidth(a) {
			//alert("autowidth");
	        $(a).width(1)
	    	$(a).width($(a).prop('scrollWidth') + 1);
		}
		
		$( "#btn_projectdetails" ).click(function() {
				
				//getting the value of textbox
				$('#projectdetailsserror').hide(); 
				$clientname=$('#txt_clientname').val();
				$duration=$('#txt_duration').val();
				$projectname=$('#txt_projectname').val();
				$role=$('#txt_role').val();
				$projectbrief=$('#txt_projectbrief').val();
				$responsibility=$('#txt_responsibilities').val();
				$environment=$('#txt_environment').val();
				$add = true;
				
				if($clientname==""||$clientname==null||$duration==""||$duration==null||$projectname==""||$projectname==null||$role==""||$role==null||$projectbrief==""||$projectbrief==null||$responsibility==""||$responsibility==null||$environment==""||$environment==null)
				{
				    $('#projectdetailsserror').show();
				    $('#projectdetailsserror').html('<font color="red">Please fill all the below fields</font>');
				    $add = false;
				}
			
				//appending table with value
				if($add)
					{
			    $("#projectdetails table tbody:last").prepend(
									'<tr>' +	
		            				'<td><div><span>'+$clientname+'</span> </div></td>'  +
		            				'<td>'+$duration+'</td>' +
									'<td>'+$projectname+'</td>' +
									'<td>'+$role+'</td>' +
									'<td>'+$projectbrief+'</td>' +
									'<td>'+$responsibility+'</td>' +
									'<td>'+$environment+'</td>' +
									'<td align="center"><input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editProj($(this).parent().parent()); return false;" value="Edit" id="editProject"> &nbsp; &nbsp; '+
									'<a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;"><img src="<c:url value="/resources/images/delete.png"/>" alt="" align="top" class="deleteprojectdetails" /></a></td>' +
		        '</tr>'  );
				
			  //refreshing the text field
			    $('#txt_clientname').val('');
				$('#txt_duration').val('');
				$('#txt_projectname').val('');
				$('#txt_role').val('');
				$('#txt_projectbrief').val('');
				$('#txt_responsibilities').val('');
				$('#txt_environment').val('');
				
					}
			
			ui_rows();
			});
			
		//on click of Add of education details----------------------------function3
		$( "#btn_educationdetails" ).click(function() {
				
				//getting the value of textbox
				$('#educationdetailserror').hide();
				/* $degree=$('#degree').val();
				$aggregate=$('#aggregate').val(); */
				//$description=$('#description').val();
     			$degree=$("#degree").val();
     			
				$specialization=$('#specialization').val();
				$college=$('#college').val();
				$yearOfPassing=$('#yearOfPassing').val();
				//alert("year of passing:"+$yearOfPassing);
				/* $college=$('#textarea_college').val();
				$yearofpassing=$('#yearofpassing').val(); */
				$add = true;
				
				if($degree==""||$degree==null||$specialization==null||$specialization=="")
					{
					    $('#educationdetailserror').show();
					    $('#educationdetailserror').html('<font color="red">Please fill the below field</font>');
					    $add = false;
					}

				//appending table with value
				if($add)
					{
			   $("#educationdetails table tbody:last").prepend(
									'<tr>' +
		            				/* '<td><div><span>'+$degree+'</span> </div></td>'  +
		            				'<td>'+$aggregate+'</td>' + */
									
									'<td>'+$degree+'</td>' +
									'<td>'+$specialization+'</td>' +
									'<td>'+$college+'</td>' +
									'<td>'+$yearOfPassing+'</td>' +
									
									/* '<td>'+$college+'</td>' +
									'<td>'+$yearofpassing+'</td>' + */
									'<td align="center"><input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editEduc($(this).parent().parent()); return false;" value="Edit" id="editEducation"> &nbsp; &nbsp; ' +
									'<a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;"><img src="<c:url value="/resources/images/delete.png"/>" alt="" align="top" class="deleteeducationdetails" /></a></td>' +
		        '</tr>');
			
				
			  //refreshing the text field
			    /* $('#degree').val('');
				$('#aggregate').val(''); */
				//$('#description').val('');
				$('#degree').val('');
				$('#specialization').val('');
				$('#college').val('');
				$('#yearOfPassing').val('');
				/* $('#textarea_college').val('');
				$('#yearofpassing').val(''); */
				
				}

			ui_rows();
		});


		//Submit the resume

		$(".submitform").click(function() {
			$submit =validate();
			if($submit)
			{
				$('#technicaldetailsError').hide();
				$('#projectdetailsserror').hide();
				$('#educationdetailsError').hide();
				$canSubmit=true;
				$title=$( "#title option:selected" ).text();
				$status="<%=EmployeeSubmissionStatus.SUBMITTED.name()%>";
				$('#txt_title').val($title);
				$skillPresent=createSkillList();
				if(!$skillPresent)
					{
					    $('#technicaldetailsError').html("<font color='red'>Technical Details cannot be empty</font>");
			            $('#technicaldetailsError').show();
			            $canSubmit =false;
					}
				$projectPresent=createProjectList();
				if(!$projectPresent)
				{
					$('#projectdetailsserror').html("<font color='red'>Project Details cannot be empty</font>");
		            $('#projectdetailsserror').show();
		            $canSubmit =false;
				}
				$educationPresent=createEducationList();
				if(!$educationPresent)
					{
					    $('#').html("<font color='red'>Education Details cannot be empty</font>");
			            $('#educationdetailsError').show();
			            $canSubmit =false;
					}
				
				generateStatus($status);
				if($canSubmit)
					{
						$('#target').submit();
					}
			}
				
			
		});
	
		//Save resume as Draft

		$( ".draftform" ).click(function() {
			//generate relExp and TotExp
			var totYear= $("#totYear").val();
			var totMon= $("#totMon").val();
			$("#totalExperience").val(totYear+" Years "+totMon+" Months");
			var relYear= $("#relYear").val();
			var relMon= $("#relMon").val();
			$("#relevantExperience").val(relYear+" Years "+relMon+" Months");
			$title=$( "#title option:selected" ).text();
			$status="<%=EmployeeSubmissionStatus.PENDING.name()%>";
			$('#txt_title').val($title);
			createSkillList();
			createProjectList();
			createEducationList();
			generateStatus($status);
			$('#target').submit();
			
			
		});

		function generateStatus(status)
		{
			$str="<input type='hidden' name='employeeSumbissionStatus' value='" +status + "' />";
			$('#statusContainer').html($str);
		}

		function createSkillList()
		{
			$flag=false;
			$rows = $(".deletetechnicaldetails").closest("tr");
			
			if($rows.length>0)
				{
				    $flag=true;
				    
				}
			
			$str = "";
			var $i=0;
			
			for( $i=0; $i < $rows.length; $i++ )
				{
					$ele = $( $( $( ".deletetechnicaldetails" ).parent().parent().parent() )[ $i ] ).children() ; 
					
					$str = $str + "<input type='hidden' name='skills[" + $i + "].skillType' id='skillType" + $i + "' value='" + $ele.eq(0).text() + "' />"
					+"<input type='hidden' name='skills[" + $i + "].skill' id='skill" + $i + "' value='" + $ele.eq(1).text() + "' />"
					+"<input type='hidden' name='skills[" + $i + "].skillCatagory' id='skill" + $i + "' value='TECHNICAL' /><br>";
				
				}
			
			//creating primary skill
			$skill=$('#primaryskills').val();
			$skillcatagory="PRIMARY";
			$skillType=null;
			$primarystr= "<input type='hidden' name='skills[" + $i + "].skillType' id='skillType" + $i + "' value='" + $skillType + "' />"
			+"<input type='hidden' name='skills[" + $i + "].skill' id='skill" + $i + "' value='" + $skill + "' />"
			+"<input type='hidden' name='skills[" + $i + "].skillCatagory' id='skill" + $i + "' value='"+$skillcatagory+"' /><br>";
			
			//creating secondary skill
			$skill=$('#secondaryskills').val();
			$skillcatagory="SECONDARY";
			$skillType=null;
			$i++;
			$secondarystr= "<input type='hidden' name='skills[" + $i + "].skillType' id='skillType" + $i + "' value='" + $skillType + "' />"
			+"<input type='hidden' name='skills[" + $i + "].skill' id='skill" + $i + "' value='" + $skill + "' />"
			+"<input type='hidden' name='skills[" + $i + "].skillCatagory' id='skill" + $i + "' value='"+$skillcatagory+"' /><br>";
			
			$str = $str + $primarystr + $secondarystr;
			//alert($str);
			$("#generatedSkillListContainer").html($str);
			return $flag;

		}

		function createProjectList()
		{
			$flag=false;	
			
			$rows = $(".deleteprojectdetails").closest("tr");
			if($rows.length>0)
			{
			    $flag=true;
			    
			}
			
			$str = "";
			var $i=0;
			
			for( $i=0; $i < $rows.length; $i++ )
				{
					$ele = $( $( $( ".deleteprojectdetails" ).parent().parent().parent() )[ $i ] ).children() ; 
					
					
		$str = $str +"<input type='hidden' name='projects[" + $i + "].clientName' id='clientName" + $i + "' value='" + $ele.eq(0).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].duration' id='duration" + $i + "' value='" + $ele.eq(1).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].projectName' id='projectName" + $i + "' value='" + $ele.eq(2).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].role' id='role" + $i + "' value='" + $ele.eq(3).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].projectDesc' id='projectDesc" + $i + "' value='" + $ele.eq(4).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].responsibility' id='responsibility" + $i + "' value='" + $ele.eq(5).text() + "' />"
					+"<input type='hidden' name='projects[" + $i + "].environment' id='environment" + $i + "' value='" + $ele.eq(6).text()+"' /><br>";
				
				}
			//alert($str);
			$("#generatedProjectDetailsListContainer").html($str);
			return $flag;

		}

		function createEducationList()
		{
		
		    $flag=false;	
			$rows = $(".deleteeducationdetails").closest("tr");
			if($rows.length>0)
			{
			    $flag=true;
			    
			}
			
			$str = "";
			var $i=0;
			
			for( $i=0; $i < $rows.length; $i++ )
				{
					$ele = $( $( $( ".deleteeducationdetails" ).parent().parent().parent() )[ $i ] ).children() ; 
				
					
		$str = $str +"<input type='hidden' name='educations[" + $i + "].degree' id='degree" + $i + "' value='" +$ele.eq(0).text()+ "' />"
		            +"<input type='hidden' name='educations[" + $i + "].specialization' id='specialization" + $i + "' value='" +$ele.eq(1).text()+ "' />"
		            +"<input type='hidden' name='educations[" + $i + "].college' id='college" + $i + "' value='" +$ele.eq(2).text()+ "' />"
		            +"<input type='hidden' name='educations[" + $i + "].yearOfPassing' id='yearOfPassing" + $i + "' value='" +$ele.eq(3).text()+ "' />";
		            
				}
			
			$("#generatedEducatonDetailsListContainer").html($str);
			return $flag;

		}
		function validate()	
		{
		$flag =true;
		
        var primaryskills = $('#primaryskills').val();
        if (primaryskills == '' ||primaryskills==null ) {
           
            $('#primaryskillsError').html("<font color='red'>Primary Skills cannot be empty</font>");
            $('#primaryskillsError').show();
            $flag =false;
  	    }
        
        var secondaryskills = $('#secondaryskills').val();
        if (secondaryskills == '' ||secondaryskills==null ) {
           
            $('#secondaryskillsError').html("<font color='red'>Secondary Skills cannot be empty</font>");
            $('#secondaryskillsError').show();
            $flag =false;
        }
        
        var totYear= $("#totYear").val();
		var totMon= $("#totMon").val();
		//var totalExperience = $('#totalexperience').val();
	    if (totYear == '' ||totYear==null || totMon == '' ||totMon == null) {
	           
	            $('#totalExperienceError').html("<font color='red'>Total Experience cannot be empty</font>");
	            $('#totalExperienceError').show();
	            $flag =false;
        }
	    $("#totalExperience").val(totYear+" Years "+totMon+" Months");
	    
	    var relYear= $("#relYear").val();
		var relMon= $("#relMon").val();
		//document.submitResume.relevantExperience.value = relYear+" Years "+relMon+" Months";
       // var relevantExperience = $('#relevantexperience').val();
       if(relYear>totYear){
    	   $('#relevantExperienceError').html("<font color='red'>Relevant Experience cannot be greater than total Experience</font>");
           $('#relevantExperienceError').show();
           $flag =false;
    	}
       if(relYear==totYear && relMon>totMon){
    	   $('#relevantExperienceError').html("<font color='red'>Relevant Experience cannot be greater than total Experience</font>");
           $('#relevantExperienceError').show();
           $flag =false;
    	 }
	    if (relYear=='' ||relYear==null ||relMon=='' ||relMon==null) {
	           
	            $('#relevantExperienceError').html("<font color='red'>Relevant Experience cannot be empty</font>");
	            $('#relevantExperienceError').show();
	            $flag =false;
        }
	    $("#relevantExperience").val(relYear+" Years "+relMon+" Months");
	    
        var professionalSummary = $('#professionalsummary').val();
        if (professionalSummary == '' ||professionalSummary==null ) {
	         
	            $('#professionalSummaryError').html("<font color='red'>Professional Summary cannot be empty</font>");
	            $('#professionalSummaryError').show();
	            $flag =false;
	           
	        
	        
		}
        /*  var technicaldetails = $('#technicaldetails').val();
        if (technicaldetails == '' ||technicaldetails==null ) {
	         
	            $('#technicaldetailsError').html("<font color='red'>Technical Details cannot be empty</font>");
	            $('#technicaldetailsError').show();
	            $flag =false;
        }
        var projectdetails = $('#projectdetails').val();
        if (projectdetails == '' ||projectdetails==null ) {
	         
	            $('#projectdetailsError').html("<font color='red'>Project Details cannot be empty</font>");
	            $('#projectdetailsError').show();
	            $flag =false;
        } 
        var educationdetails = $('#educationdetails').val();
        if (educationdetails == '' ||educationdetails==null ) {
	         
	            $('#educationdetailsError').html("<font color='red'>Education Details cannot be empty</font>");
	            $('#educationdetailsError').show();
	            $flag =false;
        } */
		 
        return $flag;
		}

		  //Add blur function for validation
		  $("#primaryskills").blur(function(){
		    $('#primaryskillsError').hide();
		  });
		    
		  $("#secondaryskills").blur(function(){
			$('#secondaryskillsError').hide();
		  });
			    
		  $("#totalexperience").blur(function(){
			$('#totalExperienceError').hide();
		  });
				    
		  $("#relevantexperience").blur(function(){
			$('#relevantExperienceError').hide();
		  });
					    
		  $("#professionalsummary").blur(function(){
			$('#professionalSummaryError').hide();
		  });
		  
		 
		  $("#technicaldetails").blur(function(){
				$('#technicaldetailsError').hide();
		  });
		  $("#projectdetails").blur(function(){
				$('#projectdetailsError').hide();
		  });
		  $("#educationdetails").blur(function(){
				$('#educationdetailsError').hide();
		  });
		  
		//End blur for validation
		  
		  $("#primaryskills").attr('maxlength','255');
		  $("#secondayskills").attr('maxlength','255');
		  $("#totalExperience").attr('maxlength','20');
		  $("#relevantExperience").attr('maxlength','20');
		  $("#professionalSummary").attr('maxlength','10000');
		  $("#skillvalue").attr('maxlength','255');
		  $("#txt_clientname").attr('maxlength','50');
		  $("#txt_duration").attr('maxlength','20');
		  $("#txt_projectname").attr('maxlength','50');
		  $("#txt_role").attr('maxlength','255');
		  $("#txt_projectbrief").attr('maxlength','10000');
		  $("#txt_responsibilities").attr('maxlength','10000');
		  $("#txt_environment").attr('maxlength','10000');
		  /* $("#degree").attr('maxlength','50');
		  $("#aggregate").attr('maxlength','20'); */
		  //$("#description").attr('maxlength','5000');
		   $("#degree").attr('maxlength','50');
		  /* $("#textarea_college").attr('maxlength','50'); */
		  
		  
		  $("skillvalue").dblclick(function(){
			  $('#skillvalueError').hide();
			});	
		  
		  
		  $("#yearOfPassing").keypress(function (e) {
			     //if the letter is not digit then display error and don't type anything
			     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			        //display error message
			        $("#errmsg").html("Digits Only").show().fadeOut("slow");
			               return false;
			    }
			   });
		  $('#yearOfPassing').bind('copy paste cut',function(e) { 
			  e.preventDefault(); //disable cut,copy,paste

			  });
});
$(function(){
    var $select = $(".0-30");
    
     for (i=0;i<=30;i++){
        $select.append($('<option></option>').val(i).html(i))
    }
    var $select = $(".1-12");
    for (i=1;i<=12;i++){
        $select.append($('<option></option>').val(i).html(i))
    }
    
});

function editTech(id){
	var skillType = id.children("td:nth-child(1)").text();
	var skillTypeValue = id.children("td:nth-child(2)").text();
	if($('#skillid').val() == ""){
		 $('#skillid').val(skillType.trim());
			$('#skillvalue').val(skillTypeValue);
			$("#arrowBlue").hide();
			id.remove();
			ui_rows();
	}else{
		 $('#technicaldetailserror').show();
		 $('#technicaldetailserror').html('<font color="red">Please modify previous selected details.</font>');
	}
   
}

function editProj(id){
	var clientName = id.children("td:nth-child(1)").text();
	var duration = id.children("td:nth-child(2)").text();
	var project = id.children("td:nth-child(3)").text();
	var role = id.children("td:nth-child(4)").text();
	var projectbrief = id.children("td:nth-child(5)").text();
	var responsibilities = id.children("td:nth-child(6)").text();
	var environment = id.children("td:nth-child(7)").text();
    $('#txt_clientname').val(clientName);
	$('#txt_duration').val(duration);
	$('#txt_projectname').val(project);
	$('#txt_role').val(role);
	$('#txt_projectbrief').val(projectbrief);
	$('#txt_responsibilities').val(responsibilities);
	$('#txt_environment').val(environment);
	id.remove();
	ui_rows();
}

function editEduc(id){
	
	//var description = id.children("td:nth-child(1)").text();
	var degree = id.children("td:nth-child(1)").text();
	var specialization = id.children("td:nth-child(2)").text();
	var college = id.children("td:nth-child(3)").text();
	var yearOfPassing = id.children("td:nth-child(4)").text();
//	$('#description').val(description);
	$('#degree').val(degree);
	$('#specialization').val(specialization);
	$('#college').val(college);
	$('#yearOfPassing').val(yearOfPassing);

	id.remove();
	ui_rows();
}

</script>
</head>

<body>

	<div id="header">
		<%@include file="../common/header.jsp"%>
		<script>
  var role="<%=user.getRole()%>";
			distributormgr_header(role);
			selmenu('mn_employee');
		</script>
	</div>
	<!-- end of header !-->
	<div id="content">
		<div id="leftNavigation">
			<script>
				distributormgr_employee_submenu('0', 'm_submitresume');
			</script>
		</div>
		<!-- end of left navigation !-->
		<div id="contenPan">
			<div id="contenPan_LeftNavToggle">
				<a href="javascript:" onclick="toggle_leftmenu();"><img
					id="img_leftmenu"
					src="<c:url value="/resources/images/menuarrowopen.png"/>"
					alt="Close Left menu" title="Close Left menu" /></a>
			</div>

			<div id="contenPan_main">
				<form:form modelAttribute="resumeDTO" method="post" id="target"
					action="/qepms/employee/submitsresume" commandName="resumeDTO">
					<h1>
						<span><font color="Red">${responseMessageWrapper.responseMessage}</font></span>
					</h1>

					<div id="TabbedPanels1" class="TabbedPanels">
						<ul class="TabbedPanelsTabGroup">
							<li class="TabbedPanelsTab">Create Resume</li>

						</ul>
						<div class="TabbedPanelsContentGroup">
							<div class="TabbedPanelsContent">

								<div class="subsection" id="forms">
									<ul>
										<li>
											<div class="label">Title:</div>
											<div class="fleft">
												<select id="title" name="title" title="Select one of the three titles: Miss,Mr,Mrs">

													<option></option>
													<option selected>
														<c:out value="${resume.employeeMaster.title}" />
													</option>
													<option>Mr</option>
													<option>Miss</option>
													<option>Mrs</option>
												</select>
											</div>
											<div class="div_tittlehidden">
												<input id="txt_title" type="text"
													name="employeeMaster.title" style="visibility: hidden"
													value="${resume.employeeMaster.title}"></input>
											</div>
										</li>
										<li>
											<div class="label">Emp Id:</div>
											<div class="fleft">${user.empId}</div> <input type="hidden"
											name="employeeMaster.empId" value="${user.empId}"></input> <input
											type="hidden" name="employeeMaster.employeeMail"
											value="${user.employeEmail}"></input> <input type="hidden"
											name="employeeMaster.reportingManager"
											value="${user.employeeManager}"></input>

										</li>
										<li>
											<div class="label">Full Name:</div>
											<div class="fleft">
												<input type="hidden" id="fullname"></input>${user.employeeName}
											</div> <input type="hidden" name="employeeMaster.name"
											value="${user.employeeName}"></input>
										</li>

										<c:if test="${empty skillList}" >
											<li><span class="mandatory">*</span>
										<div class="label">Primary Skills:</div>
											<div class="fleft">
												<textarea id="primaryskills" rows="5" cols="14" name="OFF" wrap="OFF"
												title="Enter your primary skills. Eg: Java,C#,webMethods etc"></textarea>
											</div>
											<div id="primaryskillsError"></div></li>

										<li><span class="mandatory">*</span>
										<div class="label">Secondary Skills:</div>
											<div class="fleft">
												<textarea rows="5" cols="14" name="OFF" wrap="OFF" id="secondaryskills" 
												title="Enter your secondary skills. Eg: Java,C#,webMethods etc"></textarea>
											</div>
											
											</c:if>
											<div id="secondaryskillsError"></div></li> 
											
										<c:forEach items="${skillList}" var="skills">
											<c:choose>
												<c:when test="${skills.skillCatagory == 'PRIMARY'}">
													<li><span class="mandatory">*</span>
														<div class="label">Primary Skills:</div>
														<div class="fleft">
															<textarea rows="2" cols="5" id="primaryskills" title="Enter your primary skills. Eg: Java,C#,webMethods etc"><c:out
																	value="${skills.skill}" /></textarea>
														</div>
														 <div id="primaryskillsError"></div></li>
												</c:when>
												<c:when test="${skills.skillCatagory == 'SECONDARY'}">
													<li><span class="mandatory">*</span>
														<div class="label">Secondary Skills:</div>
														<div class="fleft">
															<textarea rows="2" cols="5" id="secondaryskills" title="Enter your secondary skills. Eg: Java,C#,webMethods etc"><c:out
																	value="${skills.skill}" /></textarea>
														</div>
														<div id="secondaryskillsError"></div></li>
												</c:when>
											</c:choose>
										</c:forEach>

										<li><span class="mandatory">*</span>
										<div class="label">Total Experience:</div>
											<div class="fleft"></div>
<!-- 												<input type="text" name="totalExperience" -->
<!-- 													id="totalexperience" />&nbsp; -->
											<select class="0-30" id="totYear" name="totYear" title="Select your total work experience in years">
											<option>${resume.totYear}</option>
											<option></option>
											</select> Year 
											<select class="1-12" id="totMon" name="totMon" title="Select your total work experience in months">
											<option>${resume.totMon}</option>
											<option></option>
											</select> Months
											<input type="hidden" id="totalExperience" name="totalExperience"  />
											&nbsp;
											<div id="totalExperienceError"></div></li>
											
											
										<li><span class="mandatory">*</span>
										<div class="label">Relevant Experience:</div>
											<div class="fleft"></div>
<!-- 												<input type="text" name="relevantExperience" -->
<!-- 													id="relevantexperience" />&nbsp; -->
										<select class="0-30" id="relYear" name="relYear" title="Select your relative work experience in years">
										<option>${resume.relYear}</option>
										<option></option>
										</select>Year 
										<select class="1-12" id="relMon" name="relMon" title="Select your relative work experience in months">
										<option>${resume.relMon}</option>
										<option></option>
										</select>Months
											<input type="hidden" id="relevantExperience" name="relevantExperience"/>
											&nbsp;
											<div id="relevantExperienceError"></div></li>
											
											<li><div class="label" >Domain:</div>
										<div class="fleft">
											<textarea rows="5" cols="14" name="domain" id="domain" style="margin: 0px; width: 282px; height: 103px;"
											title="Enter your project domain and details">${resume.domain}</textarea>
											</div>
										</li>
											
										<li><div class="label" >Certifications(if any):</div>
										<div class="fleft">
											<textarea rows="5" cols="14" name="certification" id="certification" style="margin: 0px; width: 282px; height: 103px;"
											title="Enter if you have attended any certification course/exam. ">${resume.certification}</textarea>
											</div>
										</li>

										<li><span class="mandatory">*</span>
											<div class="label">Professional Summary:</div>
											<div class="fleft">
												<textarea rows="5" cols="14" name="professionalSummary" id="professionalsummary" name="OFF" wrap="OFF"
												title="Summarize your area of expertise in multiple rows.">${resume.professionalSummary}</textarea>
											</div>
											<div id="professionalSummaryError"></div></li>
									</ul>
								</div>



								<!-- Start :: Technical Details Section -->
								<div class="subsection">
									<div id="technicaldetailserror"></div>
									<h2><font style="color:red">*</font>Technical Skills</h2>
									<div class="datagrid" id="technicaldetails">
										<div id="technicaldetailsError"></div>
										<table>
											<thead>
												<tr>
													<th width="15%">Skill Type</th>
													<th>Skill Value</th>
													<th width="5%">&nbsp;</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td nowrap="nowrap" title="Select one of the skill type or add your own"><input type="text" id="skillid"
														value="" name="skillId" class="normal" size="19"
														readonly="readonly" /> <a href="javascript:"
														onclick="PopupCenter('/qepms/common/addskillpopup','win','500','250');"><img
															src="<c:url value="/resources/images/arrowBlue.png"/>"
															alt="" id="arrowBlue"/></a></td>
													<td><input type="text" id="skillvalue" 
													title="Mention all the values of that particualr skill.   Eg:Scripting languages-->javascript,PHP,ActivePerl etc. "/></td>
													<td align="center"><input type="button" value="Add"
														id="btn_skilltypeadd" /></td>
												</tr>
												<c:forEach items="${skillList}" var="skills">
													<c:if test="${skills.skillType != check}">
														<tr>
															<td><div>
																	<span class="editSkill"><c:out value="${skills.skillType}" /></span>
																	
																</div></td>
															<td><c:out value="${skills.skill}" /></td>
															<td align="center">
																<input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editTech($(this).parent().parent()); return false;"
																		value="Edit" id="editSkills"/> &nbsp; &nbsp;
																<a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;">
															<img src="<c:url value="/resources/images/delete.png"/>" alt="" class="deletetechnicaldetails" /></a></td>
														</tr>
													</c:if>
												</c:forEach>
											</tbody>
											<tbody>

											</tbody>
											<tfoot>
												<tr class="totalsec">
													<td colspan="3">&nbsp;</td>

												</tr>
											</tfoot>
										</table>
									</div>

								</div>
								<div id="generatedSkillListContainer"></div>
								<!-- End :: Tecnical Details Section -->


								<!-- Start :: Project Details Section -->
								<div class="subsection">
									<div id="projectdetailsserror"></div>
									<h2><font style="color:red">*</font>Professional Experience</h2>
									<div class="datagrid" id="projectdetails">
										<div id="projectdetailsError"></div>
										<table>
											<thead>
												<tr>
													<th width="9%">Client Name</th>
													<th>Duration</th>
													<th>Project Name</th>
													<th width="9%">Role</th>
													<th width="15%">Project Brief</th>
													<th width="15%">Responsibilities</th>
													<th width="15%">Environment</th>
													<th width="5%">&nbsp;</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td nowrap="nowrap" title="Enter the client name of the project you have been working."><input type="text"
														id="txt_clientname" value="" name="" class="normal"
														size="19" /></td>
													<td title="The duration of the project in years and months. Eg: 1 year 2 months"><input type="text" id="txt_duration" /></td>
													<td title="Name of the project."><input type="text" id="txt_projectname" /></td>
													<td title="Your role in the particular project. Eg: project lead/team member etc."><input type="text" id="txt_role" /></td>
													<td title="Brief of the project. How it worked? and how was it achieved? etc."><textarea rows="5" cols="50" id="txt_projectbrief"></textarea></td>
													<td title="Explain your responsibilities and work done in the achievement of the project"><textarea rows="5" cols="30"
															id="txt_responsibilities"></textarea></td>
													<td title="Explain environment on what the project worked on"><textarea rows="5" cols="30" id="txt_environment"></textarea></td>
													<td align="center"><input type="button"
														id="btn_projectdetails" value="Add" /></td>
												</tr>
												<c:forEach items="${projectList}" var="projects">
													<tr>
														<td><c:out value="${projects.clientName}" /></td>
														<td><c:out value="${projects.duration}" /></td>
														<td><c:out value="${projects.projectName}" /></td>
														<td><c:out value="${projects.role}" /></td>
														<td><c:out value="${projects.projectDesc}" /></td>
														<td><c:out value="${projects.responsibility}" /></td>
														<td><c:out value="${projects.environment}" /></td>
														<td align="center">
															<input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editProj($(this).parent().parent()); return false;"
															value="Edit" id="editProject"/> &nbsp; &nbsp;
															<a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;">
																<img src="<c:url value="/resources/images/delete.png"/>"alt="" class="deleteprojectdetails" />
														</a></td>
													</tr>
												</c:forEach>
											</tbody>
											<tbody>

											</tbody>
											<tfoot>
												<tr class="totalsec">
													<td colspan="9">&nbsp;</td>

												</tr>
											</tfoot>
										</table>
									</div>
								</div>
								<div id="generatedProjectDetailsListContainer"></div>
								<!-- End :: Project Details Section -->

								<!-- Start :: Education Details Section -->
								<div class="subsection">
									<div id="educationdetailserror"></div>
									<h2><font style="color:red">*</font>Education Details</h2>

									<div class="datagrid" id="educationdetails">
										<div id="educationdetailsError"></div>
										<table>
											<thead>
												<tr>
<!-- 													<th >Description</th> -->
													<th >Degree</th>
													<th>Specialization</th>
													<th>College/University</th>
													<th>Date and Year of Passing<br><p>(Enter as "YYYY")</p></></th>
													<th width="5%">&nbsp;</th>
												</tr>
											</thead>
											<tbody>
												<tr>

													<!--<td><input type="text" id="aggregate"/></td> -->
<!-- 													<td title="Enter any description of ur academics"><input type="text" id="description" /></td> -->
													<td title="Degree you perceived Eg: B.E,B.Tech"><input type="text" id="degree" /></td>
													<td title="Enter your specialization in education. Eg: Computers,Accounts etc."><input type="text" id="specialization" /></td>
													<td title="Enter your college name"><input type="text" id="college" /></td>
													<td title="Enter the year you passed your college in YYYY format"><input type="text" id="yearOfPassing" maxlength="4"/>&nbsp;<span id="errmsg"></span></td>

													<td align="center"><input type="button"
														id="btn_educationdetails" value="Add" /></td>
												</tr>
												<c:forEach items="${educations}" var="education">
													<tr>

														
														<td><c:out value="${education.degree}" /></td>
														<td><c:out value="${education.specialization}" /></td>
														<td><c:out value="${education.college}" /></td>
														<td><c:out value="${education.yearOfPassing}" /></td>

														<td align="center"><input type="image" src="/qepms/resources/images/edit.png" onclick="javascript:editEduc($(this).parent().parent()); return false;" value="Edit" id="editEducation"/> &nbsp; &nbsp; 
														<a href="javascript:" onclick="$(this).parent().parent().remove(); ui_rows(); return false;"><img
																src="<c:url value="/resources/images/delete.png"/>" alt="" class="deleteeducationdetails" /></a></td>
													</tr>
												</c:forEach>
											</tbody>
											<tbody>

											</tbody>
											<tfoot>
												<tr class="totalsec">
													<td colspan="9">&nbsp;</td>

												</tr>
											</tfoot>
										</table>
									</div>

								</div>
								<div id="generatedEducatonDetailsListContainer"></div>
								<!-- End :: Education Details Section -->

								<div class="bottombtn">
									<input class="draftform" type="button" value="Save As Draft"
										id="employeemaster" /> <input class="submitform"
										id="submitform" type="button" value="Submit" /> <input
										type="button" value="Cancel" onclick="history.back();" />
								</div>
								<div id="statusContainer"></div>
							</div>
							<!-- end of TabbedPanelsContent !-->

						</div>
					</div>
				</form:form>
			</div>
		</div>
		<!-- end of content Panel !-->
	</div>
	<!-- end of content !-->
	<div id="footer">
		<script>
			footer_01();
		</script>
	</div>
	<!-- end of footer !-->
	<script type="text/javascript">
		var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
	</script>
</body>
</html>

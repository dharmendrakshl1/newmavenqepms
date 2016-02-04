// JavaScript Document

$(function(){

			
			/* Login Valiation 	----------------------------------*/	 
			$(".btn_submit").click(function(e){
			
				$(".customError").hide();
				var focusSet = false;
		        if (!$('.txtusername').val()) {
		            if ($(".txtusername").parent().next(".validation").length == 0) // only add if not added
		            {
		                $(".txtusername").attr("placeholder", "Please enter User Name");
		            }
		            e.preventDefault(); // prevent form from POST to server
		            $('.txtusername').focus();
		            focusSet = true;
		        } 
		        if (!$('.txtpassword').val()) {
		            if ($(".txtpassword").parent().next(".validation").length == 0) // only add if not added
		            {
		                $(".txtpassword").attr("placeholder", "Please enter Password");
		            }
		            e.preventDefault(); // prevent form from POST to server
		            if (!focusSet) {
		                $(".txtpassword").focus();
		            }
		        }});
			
			$(".txtusername").keypress(function(){$(".customError").hide();});
			$(".txtpassword").keypress(function(){$(".customError").hide();});
			

});
			









<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" session="true" %>
<%   
int timeout = session.getMaxInactiveInterval();
response.setHeader("Refresh", timeout + "; URL=login.jsp?timeout=true");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setDateHeader("Expires", 0); // Proxies.  
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="dist/tree.css">
<link rel="shortcut icon" type="image/png" href="dist/img/aawsa_icon.png"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<title>User Interface</title>
<style type="text/css">
.table_psw{
margin-top:1%;
border: 1px solid black;
width: 80%;
margin:0 auto;
white-space: nowrap; 
text-align: center;
padding-bottom: 1%;
}
.table_psw:nth-child(even) {

  background-color: #A3ADAC;
  border-top: 20px solid rgba(0,0,0,.3);
}
</style>
<script type="text/javascript">
function check_lis(){
	var currentPw = document.getElementById('currentPassword');
	var newPw = document.getElementById('newPassword');
	var confirmPW = document.getElementById('confirmPassword');
	var passwordMatchMessage=document.getElementById('passwordMatchM'); 
	if (currentPw.value === newPw.value) {
		   passwordMatchMessage.textContent = 'Your New Password must not matche with the Current Password. Please, Correct!';
		   passwordMatchMessage.style.color = 'red';
	    return false;
	    }
	if (newPw.value !== confirmPW.value) {
		   passwordMatchMessage.textContent = 'The New Password and Confirmation Password Miss Match!';
		   passwordMatchMessage.style.color = 'red';
	    return false;
	    }
	return (true);
	}
</script>
</head>
<body>
<nav class="titbarnav" style="margin-top: -6%;">
    <ul>
    <li> <a href="#">About Us</a> </li>
     <li><a href="#">Contact</a> 
     </li>
      <li id="cliclit"><a href="#">Accounts</a> 
      <div class="fire_login" id="list_it" style="display: none; float: right;">
     <ul>
     <li><a data-li="con_list" class="active_log" href="#">Login</a></li>
     <li><a data-li="con_disc"></a></li>
     </ul>
     </div>
      </li>
    </ul>
	</nav>
<div style="padding-top: 2%; width: 90%;">
<%int choice= (Integer)session.getAttribute("U_id1");%>
<form action="changePassword" method="post" onsubmit="return(check_lis());">
    <table class="table_psw">
        <tr>
            <td>Current Password</td>
            <td><input type="password" id="currentPassword" name="currentPassword" required="required" style="height: 25px; width: 250px;"></td>
        </tr>
        <tr>
            <td>New Password</td>
            <td><input type="password" id="newPassword" name="newPassword" style="height: 25px; width: 250px;"
            pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
            title="Must contain at least one number, one uppercase and lowercase letter, and at least 8 or more characters"/></td>
        </tr>
        <tr>
            <td>Confirm Password</td>
            <td><input type="password" id="confirmPassword" name="confirmPassword" required="required"  style="height: 25px; width: 250px;"></td>
        </tr>
        <tr>
        <td>
        <input type="text" name="userID" value="<%=choice%>" style="display: none;"/></td>
        </tr>
        <tr>
            <td></td><td><input type="submit" value="Change Password" style="height: 25px; width: 120px; text-align: center;
            margin-left: 15%; margin-top: 1.5%;"></td>
        </tr>
    </table>
    <div id="passwordMatchM" style="margin-left:25%;margin-top:1%;font-size: 17px;"></div>
</form>
</div>
</body>
</html>
<%@page import="java.util.zip.ZipEntry"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.nio.file.Files"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="net.AAWSAgDB.fileupload.Reset.RestResponseStatusCode"%>
<%@page import="net.AAWSAgDB.fileupload.Reset.RestResponseStatus"%>
<%@page import="net.AAWSAgDB.fileupload.model.File"%>
<%@page import="net.AAWSAgDB.fileupload.Reset.RestResponse"%>
<%@page import="java.util.List"%>
<%@ page language="java" session="true" %>
<%   
int timeout = session.getMaxInactiveInterval();
response.setHeader("Refresh", timeout + "; URL=login.jsp?timeout=true");
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setDateHeader("Expires", 0); // Proxies.  
%>
<html>
<head>
<meta name="viewport" http-equiv="Content-Type" content="text/html; charset=UTF-8; width=device-width; initial-scale=1.0"/> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="dist/tree.css">
<link rel="stylesheet" href="dist/screen_sizesmall_mediacontroll.css">
<link rel="shortcut icon" type="image/png" href="dist/img/aawsa_icon.png"/>
<title>DB User page</title>
 <%
 @SuppressWarnings("unchecked")
 List<File>storedWellInfo= (List<File>)request.getAttribute("msg1");
 @SuppressWarnings("unchecked")
 List<File>updateMessage = (List<File>)request.getAttribute("updateMsg");	
 @SuppressWarnings("unchecked")
 List<File>grant_pro= (List<File>)request.getAttribute("gr_pro");	
					 %>					 
<STYLE type="text/css">
td{
white-space: nowrap;
}
.table_ {
white-space: nowrap; 
table-layout: fixed;
left: 0px;
top: 0px;
margin-top:-0.75%;
width: 100%;
}
.table_div{
	margin-left: 0.55%;
    margin-top: -2.0%;
	}
.table_ thead {
text-align: center;
background-color: #28527b;
color: white;
z-index: 2;
}
.table_ thead tr {
  padding-right: 17px;
  box-shadow: 0 4px 6px rgba(0,0,0,.6);
  z-index: 2;
}
.table_ tbody {
  display: block;
  min-height: calc(280px + 1 px);
  max-height: 355px;
  /*use calc for fixed ie9 & <*/
  overflow-y:scroll;
  color: #000;
}
.table_ tr{
  display: block;
  overflow: hidden;
}
.table_ tr:nth-child(even) {
  background-color: #0000000b;
}
.table_{
border: 1px solid black;
}
.table_ tbody tr:nth-child(odd):hover{
background-color: #F1F0F0;	
}
.table_ tbody tr:nth-child(even):hover{
background-color: #DDD9D9;	
}
.table_ th,.table_ td {
border-right: 1px solid rgba(0,0,0,.3);
border-collapse: collapse;
text-align: left;
padding-right: 6px;
padding-top:6px;
font-size: 13px;
width: 0%;
float:left;
}
.table_ td:nth-child(1),.table_ th:nth-child(1){
width: 0%;
}
.table_ td:nth-child(2),.table_ th:nth-child(2){
width: 8%;
}
.table_ td:nth-child(3),.table_ th:nth-child(3){
width: 9%;
}
.table_ td:nth-child(4),.table_ th:nth-child(4){
width: 9%;
}
.table_ td:nth-child(5),.table_ th:nth-child(5){
width: 7%;
}
.table_ td:nth-child(6),.table_ th:nth-child(6){
width: 7%;
}
.table_ td:nth-child(7),.table_ th:nth-child(7){
width: 7%;
}
.table_ td:nth-child(8),.table_ th:nth-child(8){
width: 8%;
}
.table_ td:nth-child(9),.table_ th:nth-child(9){
width: 6%;
}
.table_ td:nth-child(10),.table_ th:nth-child(10){
width: 6%;
}
.table_ td:nth-child(11),.table_ th:nth-child(11){
width: 6%;
}
.table_ td:nth-child(12),.table_ th:nth-child(12){
width: 7.6%;
}
.table_ td:nth-child(13),.table_ th:nth-child(13){
width: 5%;
}
.table_ td:nth-child(14),.table_ th:nth-child(14){
width: 5%;
}
.long{
  display: inline-block;
  position: relative;
  width: 99%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: top;
}
.long:hover {
   z-index: 1;
  width: auto;
  background-color: #FFFFCC;

}	
		.error1 {
    padding: 1px;
    margin-bottom: 10px;
    border: 1px solid transparent;
    border-radius: 4px;
    color: #a94442;
    border-color: #ebccd1;
    margin-left:10%;
    width: 50%;
}

.msg1 {
    padding: 7px;
    margin-top: 10px;
    margin-bottom: 10px;
    border: 1px solid transparent;
    border-radius: 4px;
    background-color: #d9edf7;
    border-color: #bce8f1;
    margin-left:10%;
    width: 60%;
}
.existes {
    padding: 7px;
    margin-top: 10px;
    margin-bottom: 10px;
    border: 1px solid transparent;
    border-radius: 4px;
    color: black;
    background-color: #d9edf7;
    border-color: #bce8f1;
    margin-left:10%;
    width: 60%;
}
	form {
  width: 78.7%;
  border: 0px solid #f1f1f1;
  margin-left: 1%;
  margin-right: auto;
  margin-top: -4px;
  margin-bottom: 5px;
  text-align: center;
  white-space: nowrap;}
  form input[type="text"]:hover{
  background-color: #F1F0F0;
  border-color: black;
  border-radius: 8px; /* Rounded corners */	
  }
  form input[type="date"]:hover{
  background-color: #F1F0F0;
  border-color: black;
  border-radius: 8px; /* Rounded corners */
  }
   form select:hover{
    background-color: #F1F0F0;	
    border-color: black;
    border-radius: 8px; /* Rounded corners */
  }
  
</STYLE>
<script type="text/javascript">  
var dataExCsv="Detail Operation Data form Excel or CSV";
var dataDrill="Drilling Pump Test Historical Data";
var WellRepo="Well Report Data";
var pumpData="Pump and Generator Specification Data";

//for project report
var typeforDPTHD=[".pdf",".docx"]
var checkWellReportFileT=new RegExp("([a-zA-Z0-9\S_\\.\-:])+("+typeforDPTHD.join('|')+")$")
var fileTypeAllowedforDPTHD=["{.pdf}","{.docx}"]

var all_ExcelfileType=[".xlsx",".xls",".csv"];
var rexpr_gwData= new RegExp("([a-zA-Z0-9\S_\\.\-:])+("+all_ExcelfileType.join('|')+")$");
var all_fileTypeAllowed=["{.xlsx}","{.xls}","{.csv}"];

function checkgwExcelform(check_gwExcel){
	if((check_gwExcel.file1.files.length === 0) && (check_gwExcel.file2.files.length === 0) && (check_gwExcel.file3.files.length === 0) && 
			(check_gwExcel.file4.files.length === 0) && (check_gwExcel.file5.files.length === 0)){
		alert("Upload Atleast One Excel file for the Excel Sheet Data.... ");
		return false;
	}
	if(check_gwExcel.file1.files.length != 0){
		for(var i=0;i<check_gwExcel.file1.files.length;i++){
			if(!rexpr_gwData.test(check_gwExcel.file1.files.item(i).name)){	
		alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataExCsv)
			//alert("Note: You can only Upload: <<"+ vector.join(', ') +">> file and Its Supportive file Types for "+datarsa)
				return false;
				}
			}
		if(check_gwExcel.log1.checked == false){
			alert("Check List for Operational (Real Time Data) Data is Not Checked! Check It and upload your file ");
			mform1.file1.focus();
			return false;
			}
	}
	if(mform1.file2.files.length!=0){
		for(var i=0;i<mform1.file2.files.length;i++){
		if(!rexpr_gwData.test(mform1.file2.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataExCsv);
			return false;
			}
		}
		if(mform1.log2.checked == false){
			alert("Check List for Microbiological Data is Not Checked! Check It and Upload Your file ");
			mform1.file2.focus();
			return false;	
		}
	}
	if(mform1.file3.files.length!=0){
		for(var i=0;i<mform1.file3.files.length;i++){
		if(!rexpr_gwData.test(mform1.file3.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataExCsv);
			return false;
			}
		}
		if(mform1.log3.checked == false){
			alert("Check List for Physical Data is Not Checked! Check It and Upload Your File ");
			mform1.file3.focus();
			return false;	
		}
	}
	if(mform1.file4.files.length != 0){
		for(var i=0;i<mform1.file4.files.length;i++){
			if(!rexpr_gwData.test(mform1.file4.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataExCsv);
			}
				return false;
				}
		if(mform1.log4.checked == false){
			alert("Check List for Chemical Data is Not Checked! Check It and upload Your File ");
			mform1.file4.focus();
			return false;
			}
		}
	if(mform1.file5.files.length != 0){
		for(var i=0;i<mform1.file5.files.length;i++){
			if(!rexpr_gwData.test(mform1.file5.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataExCsv);
			}
				return false;
				}
		if(mform1.log5.checked == false){
			alert("Check List for Rehabilitation Data is Not Checked! Check It and upload Your File ");
			mform1.file5.focus();
			return false;
			}
		}
	return true;
}
function chechform(check_){
	if(document.getElementById('regionEn').selectedIndex == 0){
		alert("Well Address not selected, Please Select One!");
		return false;
		}
	if(document.getElementById('regionCityID').selectedIndex == 0){
		alert("City Or Region not selected, Please Select One!");
		return false;
		}
	if(document.getElementById('subcityzoneId').selectedIndex == 0){
		alert("Sub City Or Zone not selected, Please Select!");
		return false;
		}
	if(document.getElementById('Inv_id').selectedIndex == 0){
		alert("Well Field Not Selected, Please Select One!");
		return false;
		}
	if(document.getElementById('Dpermit').selectedIndex == 0){
		alert("Drilling License Issued not Selected, Please Select One!");
		return false;
		}
	//validate Well Report data attachments
	const rowControl = document.getElementById('dpthdRow');
	if(rowControl.style.display !== "none"){
		if(mform.pwell_id.files.length === 0){
			alert("Upload Drilling Pump Test History Data, Note the Attachment is in Excel or CSV File.... ");
			return false;
		}
		//validate productive well
		if(mform.pwell_id.files.length != 0){
			for(var i=0;i<mform.pwell_id.files.length;i++){
				if(!rexpr_gwData.test(mform.pwell_id.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+dataDrill)
				//alert("Note: You can only Upload: <<"+ vector.join(', ') +">> file and Its Supportive file Types for "+datarsa)
					return false;
					}
				}
			if(mform.pwell_check.checked == false){
				alert("Check List for Well Drilling Pump Test History Data Attachment is Not Checked! Check It and upload your Data");
				mform.pwell_id.focus();
				return false;
				}
		}
	}
	//Pumping System data attachments
		if(mform.pump_id.files.length === 0 || mform.reportDataid.files.length === 0){
			if(confirm("Pump and Generator Factory Spec Document  Or Well Report Document not attached. If you Click Ok, Your Submision will be without attachment")){
				return true;
				}
			else{
				return false;	
			}
			return false;	
		}
		if(mform.pump_id.files.length != 0){
			for(var i=0;i<mform.pump_id.files.length;i++){
				if(!rexpr_gwData.test(mform.pump_id.files.item(i).name)){	
			alert("Note: You can only Upload Excel Sheet or CSV file; Such as <<"+ all_fileTypeAllowed.join(' or ') +">> file types for "+pumpData)
				//alert("Note: You can only Upload: <<"+ vector.join(', ') +">> file and Its Supportive file Types for "+datarsa)
					return false;
					}
				}
			if(mform.pump_check.checked == false){
				alert("Check List for Pump and Generator Specification Report Attachment is Not Checked! Check It and upload your Data ");
				mform.pump_id.focus();
				return false;
				}
		}
		if(mform.reportDataid.files.length != 0){
			for(var i=0;i<mform.reportDataid.files.length;i++){
				if(!checkWellReportFileT.test(mform.reportDataid.files.item(i).name)){	
			alert("Note: You can only Upload PDF or Word file; Such as <<"+fileTypeAllowedforDPTHD.join(' or ') +">> file types for "+WellRepo)
				//alert("Note: You can only Upload: <<"+ vector.join(', ') +">> file and Its Supportive file Types for "+datarsa)
					return false;
					}
				}
			if(mform.report_check.checked == false){
				alert("Check List for Well Report is Not Checked! Check It and upload your Data ");
				mform.pump_id.focus();
				return false;
				}
		}
	return true;
}

/*Create text inpute for process*/
function showprofield(name){
	  if(name=='1')document.getElementById('div5').innerHTML='Process Name: <input type="text" name="other4" style="width: 190px;'
	  +'height: 25px;" required minlength="5" maxlength="30"/> <span class="validity"></span>';
	  else document.getElementById('div5').innerHTML='';
	}
/*Create text inpute for Basin*/
function showproIdfield(name){
	  if(name=='1')document.getElementById('div8').innerHTML='Project ID: <input type="text" name="u_proid" style="width: 190px;'
	  +'height: 25px;" required minlength="5" maxlength="30"/> <span class="validity"></span>';
	  else document.getElementById('div8').innerHTML='';
	}
function showusergroup(name){
	 if(name=='1')document.getElementById('divU').innerHTML='Project Group: <input type="text" name="u_group" style="width: 190px;'
		  +'height: 25px;" required minlength="5" maxlength="30"/> <span class="validity"></span>';
		  else document.getElementById('divU').innerHTML='';
		}
 </script>    
<title>Home page</title>
</head>
<body>
    <nav class="titbarnav">
    <ul>
    <li> <a href="#">About Us</a> </li>
     <li> <a href="#">Contact</a> </li>
      <li id="c_logout"> <a href="#">Accounts</a> 
      <div id="dis_logout" style="display: none;float: right; ">
      <ul>
      <li><a href="<c:url value="/logout" />">Logout</a></li>
      </ul>
      </div> 
      </li>
    </ul>
	</nav>
	<%int choice= (Integer)session.getAttribute("U_id1");String pr_code_active= (String)session.getAttribute("pr_code_active"); %>
<!-- State-of-the-Art Database User Action Ribbon -->
<div style="background: #ffffff; padding: 20px -1px; margin-top:0.3%; border-bottom: 2px solid #64748b; 
display: flex; justify-content: space-between; align-items: center; font-family: 'Segoe UI', sans-serif;width: 85%; position: absolute;">
    <h4 style="margin: 0; font-size: 14px; font-weight: bold; color: #334155; display: inline-flex; align-items: center; 
    gap: 4px; white-space: nowrap;">
        👤 Operator: <span style="color:#475569;"><c:out value="${sessionScope.U_auto}"/></span> | 
        Role: <span id="UserType_Controller" class="badge" style="background:#64748b; color:white; padding:2px 6px; border-radius:4px;"></span>
    </h4>
    <div style="display: flex; align-items: center; gap: 8px;">
        <label style="font-size:12px; font-weight:600; color:#475569;">Change Context:</label>
        <select class="form-control" onchange="executeContextSwitch(this.value)" style="height: 28px; font-size: 12px; padding: 2px 6px; border-radius: 4px; border: 1px solid #cbd5e1; cursor:pointer;">
            <option value="0" selected disabled>-- Select Workspace View --</option>
            <option value="corporateU">Portal Terminal</option>
            <!-- If the user's login token equals admin, allow them to navigate back up to privilege_confirm -->
            <c:if test="${sessionScope.pr_code_active == '110'}">
                <option value="privilege_confirm">Administrator Terminal</option>
            </c:if>
        </select>
    </div>
</div>
<div class="app-workspace-container">
<!-- SIDEBAR NAVIGATION TREE MENU BLOCK (20% Sizing Scale Layout) -->
    <aside class="app-navigation-sidebar">
     <div class="sidebar-app-title">
            <h2><i class="fa fa-database"></i>AAWSA Portal Engine</h2>
        </div>
         <!-- SECTION 1: STORE OPERATIONS -->
        <div class="menu-group-header">Store Data</div>
        <div class="dy_change">
        <nav style="display: flex; flex-direction: column; margin-bottom: 15px;">
            <a href="#" class="sidebar-nav-link" data-li="geo_tech" id="gtech_id">
                <i class="fa fa-pencil-square-o" style="width: 16px;"></i> Drilling & Pumping Log
            </a>
            <a href="#" class="sidebar-nav-link" data-li="oprate_tech" id="operational_id">
                <i class="fa fa-cogs" style="width: 16px;"></i> Detailed Operations Well Data
            </a>
        </nav>
        <!-- SECTION 2: MANAGEMENT LIFECYCLES -->
        <div class="menu-group-header">Manage Repository</div>
        <nav style="display: flex; flex-direction: column;">
            <a href="#" class="sidebar-nav-link" data-li="corpo_data" id="mange_rawdata">
                <i class="fa fa-file-excel-o" style="width: 16px;"></i> Drilling & Pumping Test History
            </a>
            <a href="#" class="sidebar-nav-link active_li" data-li="spa_data" id="mange_otherdoc">
                <i class="fa fa-wrench" style="width: 16px;"></i> Detail Operations Well Inventory
            </a>
            <a href="#" class="sidebar-nav-link" data-li="add_param" id="mange_addtional">
                <i class="fa fa-plus-circle" style="width: 16px;"></i> New Additional Attributes
            </a>
            <a href="#" class="sidebar-nav-link" data-li="add_RResponse" id="mange_RequestResponse">
                <i class="fa fa-exchange" style="width: 16px;"></i> Request Queue
            </a>
        </nav>
        </div></aside>
          <div class="main-window-workspace">
          <!-- Standard Authority Watermark Header Banner -->
        <div class="workspace-view-header" style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 12px; font-weight: bold; color: #64748b; text-transform: uppercase; letter-spacing: 0.5px;">
                Addis Ababa Water and Sewerage Authority (AAWSA)
            </span>
            <span style="font-size: 11px; background: #e2e8f0; color: #475569; padding: 2px 8px; border-radius: 4px; font-weight: 600;">
                Secure Operational Module
            </span>
        </div>
<!-- ========================================================================= -->
<!-- ADVANCED SECURED ADMINISTRATIVE ATTRIBUTES MANAGEMENT COMPONENT          -->
<!-- ========================================================================= -->
<div class="table_div item_li add_param" style="width: 99.5%; padding: 15px; 
box-sizing: border-box; display: none;">
    
    <!-- COMPONENT MASTER HEADER CARD PROFILE -->
    <div style="border-bottom: 2px solid #06402B; padding-bottom: 2px; margin-bottom: 2px; display: flex; align-items: center; gap: 2px;">
        <h2 style="margin: 0; font-size: 16px; font-weight: bold; color: #06402B;">
            <i class="fa fa-plus-circle"></i> Schema Management: Add New Attribute
        </h2>
    </div>

    <!-- Hidden session tracking parameters placeholder container -->
    <input type="text" id="addedUserID" name="user_idNP" value="<%=choice%>" style="display: none;" />

    <div class="WCRFormDiv" style="width: 100%; max-width:70%;">
        <table style="width: 100%; border-collapse: collapse; font-size: 12.5px; margin-bottom: 20px;">
            <tbody>
                <!-- ROW 1: MASTER PARAMETERS CONTENT SELECTION LAYER -->
                <tr style="border-bottom: 1px solid #f1f5f9;">
                    <td style="padding: 10px 6px; width: 30%; font-weight: 600; color: #475569;">
                        <label for="addtionalParamid">Target Content Group:</label>
                    </td>
                    <td style="padding: 10px 6px; width: 70%;">
                        <select id="addtionalParamid" name="addtionalParamid" class="form-control" style="width: 100%; max-width: 320px; 
                        height: 32px; font-size: 12.5px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 8px; color: #0f172a; 
                        background: #ffffff; cursor: pointer;" onchange="addtionalParamF(); addtionalParamF2();">
                            <option value="0">---------- Select Content Needed ----------</option>
                            <option value="City_OroZone">City / Oromia Zone</option>
                            <option value="Well Field">Well Field</option>
                            <option value="Well Owner">Well Owner</option>
                            <option value="Casing Material">Casing Material</option>
                            <option value="Well Type">Well Type</option>
                            <option value="Abandoned Reason">Abandoned Reason</option>
                            <option value="Pump Information">Pump Information</option>
                            <option value="Power System">Power System</option>
                            <option value="SCADA Connection">SCADA Connection</option>
                        </select>
                    </td>
                </tr>

                <!-- ROW 2: DYNAMIC ADAPTIVE GEOGRAPHIC CONDITIONAL SELECTION BLOCK -->
                <tr id="city_ZoneID" style="display: none; border-bottom: 1px solid #f1f5f9; background: #f8fafc;">
                    <td style="padding: 10px 6px; font-weight: 600; color: #475569;">
                        <label for="selectedCZID">City / Zone Target Name:</label>
                    </td>
                    <td style="padding: 10px 6px;">
                        <select id="selectedCZID" name="cityZname" class="form-control" style="width: 100%; max-width: 320px; height: 32px; font-size: 12.5px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 8px; color: #0f172a; background: #ffffff; cursor: pointer;">
                            <option value="0">-------- Select City / Zone Name --------</option>
                            <option value="6">Addis Ababa City Admin</option>
                            <option value="7">Sheger City</option>
                            <option value="8">North Shewa</option>
                            <option value="9">West Shewa</option>
                            <option value="10">East Shewa</option>
                            <option value="11">South West Shewa</option>
                        </select>
                    </td>
                </tr>

                <!-- ROW 3: DYNAMIC DYNAMIC DATA CAPTURE ALPHANUMERIC STRING TEXT FIELDS -->
                <tr id="addParametersId" style="display: none; border-bottom: 1px solid #f1f5f9; background: #f8fafc;">
                    <td style="padding: 10px 6px; font-weight: 600; color: #475569;">
                        <label for="additionalDataID">Add Attribute Label:</label>
                    </td>
                    <td style="padding: 10px 6px;">
                        <input type="text" id="additionalDataID" name="additionalDataName" placeholder="Enter new data parameter label..." style="width: 100%; max-width: 320px; height: 32px; font-size: 12.5px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 10px; color: #0f172a; box-sizing: border-box;" />
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- MASTER CENTRAL ACTION SUBMISSION CONTROL BAR INTERFACE BUTTON -->
        <div style="text-align: center; margin-top: 15px; padding-top: 10px;">
            <input type="submit" value="Save Attribute" id="addparam_button" style="height: 36px; width: 150px; background: #06402B; color: #ffffff; font-weight: 700; border: none; border-radius: 4px; font-size: 12px; cursor: pointer; box-shadow: 0 2px 4px rgba(0,0,0,0.1); transition: background 0.2s ease, transform 0.1s;" onmouseover="this.style.background='#042d1e'; this.style.transform='translateY(-1px)';" onmouseout="this.style.background='#06402B'; this.style.transform='translateY(0)';" onmousedown="this.style.transform='translateY(1px)';" />
        </div>

        <!-- ASYNCHRONOUS DATA ROUTING SYSTEM STATUS SHEETS DISPLAY NOTIFICATIONS INDICATOR -->
        <div id="addParamresponse" style="text-align: center; margin-top: 15px; font-size: 13px; font-weight: 600; color: #16a34a; font-family: sans-serif;"></div>
        <div id="errorMessage" style="text-align: center; margin-top: 15px; font-size: 13px; font-weight: 600; color: #ef4444; font-family: sans-serif;"></div>
    </div>
</div>    
            <!-- ========================================================================= -->
<!-- DATABASE OPERATOR SECURED ACCESS REQUEST REVIEW SHEET CONTAINER            -->
<!-- ========================================================================= -->
<div id="operator_request_panel" class="table_div item_li add_RResponse" style="width: 99.5%; padding: 15px; 
box-sizing: border-box; display: none;">
    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #06402B; padding-bottom: 8px; 
    margin-bottom: 15px;">
        <h2 style="margin: 0; font-size: 16px; font-weight: bold; color: #06402B;">
            <i class="fa fa-shield"></i> Repository Access Request Queue
        </h2>
        <button type="button" onclick="loadOperatorRequestQueue();" style="background:#06402B; color:#ffffff; border:none; padding:4px 12px; border-radius:4px; font-size:11.5px; cursor:pointer; display:inline-flex; align-items:center; gap:6px;">
            <i class="fa fa-refresh"></i> Refresh Queue
        </button>
    </div>
    
    <table id="OperatorRequestTable" style="width: 100%; border-collapse: collapse; font-size: 12.5px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);"> 
        <thead>
            <tr style="background-color: #06402B; color: white; text-align: left; font-weight: 700;">
                <th style="padding: 6px 8px; width: 6%;">Index</th>
                <th style="padding: 6px 8px; width: 14%;">Requester</th>
                <th style="padding: 6px 8px; width: 23%;">Organization Node</th>
                <th style="padding: 6px 8px; width: 25%;">Target Requested File</th>
                <th style="padding: 6px 8px; width: 15%;">Category Scope</th>
                <th style="padding: 6px 8px; text-align: center; width: 17%;">Review Decision Action</th>
            </tr>
        </thead>
        <tbody style="background: #ffffff; color: #334155;">
            <!-- Content rows dynamically hydrated via AJAH script execution -->
        </tbody>
    </table>
</div>
            
            <!--Manage Water Well Data  -->
            <div class="table_div item_li corpo_data" style="display: none;" id="displaywellCRD">
       <p style="text-align: center;margin-top: 1%; font-weight: bold; font-size: 17px;">Manage Drilling Pumping Test History Data</p>
    <input type="search" class="searchByWellIndex" id="DPTHDwellIndex" name="DPTHDwellIndex" placeholder="Search by Well Index" style="width: 200px; 
    height:25px; border-bottom-color:gray; position: relative; float: right; margin-top: 0%; margin-right: -0.06%;" 
            class="form-control"/>  
            <input type="text" id="user_idWCR" name="user_idWCR" value="<%=choice%>" style="display: none;" />
            <input type="text" id="userTypeWCR" name="user_Type" value="<%=pr_code_active%>" style="display: none;" />
            <input type="text" id="hold_wellIndex" value="" style="display: none;">
             <input type="text" id="ORwell_id" value="" style="display: none;">
             <input type="text" id="user_prvg" name="user_prvg" value="<%=pr_code_active%>" style="display: none;" />
            <table id="accessby_userId" class="table_ tab_projectr">
           <thead>
           <tr>
           <th style="display: none;"></th>
       <th>
       <select class="form-control dropdown_size" name="clCo_id" id="clCo_id">
          <option class="option_css" value="0">Region</option>
        </select>
        </th>
         <th>
        <select class="form-control dropdown_size" name="selCo_id" id="selCo_id">
        <option class="option_css" value="0">Well Field</option>
        </select> 
        </th>
        <th>
        <select class="form-control dropdown_size" name="Cor_cat_id" id="Cor_cat_id">
          <option class="option_css" value="0">Well Owner Group</option>
        </select>
        </th>
        <th>Well Owner</th>
        <th>Well Index</th>
        <th>Well Code</th>
        <th>Construction Year</th>
        <th>Well Status</th>
        <th>Well Depth</th>
        <th>Well Yield</th>
        <th>Data</th>
        <th>Update</th>
        <th>Delete</th>
           </tr>
           </thead>
           <tbody>
           </tbody>
           </table>
            </div> 
            <!--Manage Groundwater Data  -->
            <div class="table_div item_li spa_data" style="display: none;" id="displaywellOGWD">
       <p style="text-align: center;margin-top: 1%; font-weight: bold; font-size: 17px;">Manage Detail Operation Well Data</p>
    <input type="search" class="searchByWellIndex" id="DOWDWellIndex" name="DOWDWellIndex" placeholder="Search by Well Index" style="width: 200px; 
    height:25px; border-bottom-color:gray; position: relative; float: right; margin-top: 0%; margin-right: -0.06%;" 
            class="form-control"/>  
            <input type="text" id="user_idOR" name="user_idOR" value="<%=choice%>" style="display: none;" />
            <input type="text" id="user_Type" name="user_Type" value="<%=pr_code_active%>" style="display: none;" />
            <input type="text" id="hold_wellIndexOP" value="" style="display: none;">
            <input type="text" id="hold_wellIdOR" value="" style="display: none;">
            <table id="access_OpData" class="table_ tab_projectr">
           <thead>
           <tr>
           <th style="display: none;"></th>
       <th>
       <select class="form-control dropdown_size" name="clCo_id" id="clCo_id">
          <option class="option_css" value="0">Region</option>
        </select>
        </th>
         <th>
        <select class="form-control dropdown_size" name="selCo_id" id="selCo_id">
        <option class="option_css" value="0">Well Field</option>
        </select> 
        </th>
        <th>
        <select class="form-control dropdown_size" name="Cor_cat_id" id="Cor_cat_id">
          <option class="option_css" value="0">Well Owner Group</option>
        </select>
        </th>
        <th>Well Owner</th>
        <th>Well Index</th>
        <th>Well Code</th>
        <th>Well Status</th>
        <th>Well Depth</th>
        <th>Data</th>
        <th>Data Status</th>
        <th>Last Updated</th>
        <th>Update</th>
        <th>Delete</th>
           </tr>
           </thead>
           <tbody>
           </tbody>
           </table>
            </div> 
            
<!-- Basic Well Characteristics Bulk Import Data Sheet -->
<div class="table_div item_li geo_tech" style="display:none; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, 
sans-serif; color: #333;">
    
    <p style="text-align: center; font-weight: bold; font-size: 17px; color: #06402B; letter-spacing: 0.5px;">
        Store Drilling Pumping Test History Data
    </p>
    
    <form method="post" action="Upload" enctype="multipart/form-data" name="mform" onsubmit="return(chechform(this));">
        <!-- Core Session Data and Action Identifiers -->
        <input id="uu_id" name="db_userId" type="text" value="<%=choice%>" style="display:none;">
        <input id="Na" name="Na" type="text" value="Water Well" style="display: none;">
        
        <div id="basicWelltTabDiv" class="WCRFormDiv" style="background: #ffffff; padding: 4px; border-radius: 8px; 
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
            <table border="0" style="width: 100%; border-collapse: separate; border-spacing: 0 12px;">
                
                <!-- Well Index -->
                <tr>
                    <td style="width: 28%; font-weight: 600; font-size: 14px; color: #444; vertical-align: middle;">Well Index <span style="color:red;">*</span></td>
                    <td>
                        <input type="text" id="wellIndex" name="wellIndex" style="width: 100%; height: 28px; padding: 4px 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;" placeholder="e.g., AAWSA-WELL-01" required="required" minlength="4" maxlength="70"/>
                    </td>
                </tr>
                
                <!-- Well Address Cascading System -->
                <tr>
                    <td style="font-weight: 600; font-size: 14px; color: #444; vertical-align: top; padding-top: 4px;">Well Address <span style="color:red;">*</span></td>
                    <td>
                        <select id="regionEn" name="waterWellLocation" class="form-control" style="width: 100%; height: 32px; padding: 4px; border: 1px solid #ccc; border-radius: 4px;">
                            <option value="0" style="font-weight: bold;">--------- Select Well Address ---------</option>
                        </select>
                        
                        <div style="margin-top: 4px; display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
                            <div><label id="Id_div"></label>
                                <select id="regionCityID" name="regionCityID" class="form-control" style="display: none; width: 100%; height: 32px; padding: 4px; border: 1px solid #ccc; border-radius: 4px;">
                                    <option value="0" style="font-weight: bold;">---- Select Region or City ----</option>
                                </select>
                            </div>
                            <div><label id="pr_type"></label>
                                <select id="subcityzoneId" name="subcityzoneId" class="form-control" style="display: none; width: 100%; height: 32px; padding: 4px; border: 1px solid #ccc; border-radius: 4px;">
                                    <option value="0" style="font-weight: bold;">---- Select Sub City or Zone ----</option>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                
                <!-- Hidden Groundwater Field Placeholder -->
                <tr style="display: none;">
                    <td>GWD:</td>
                    <td>
                        <select id="fsource" name="gwDataname" class="form-control"></select>
                        <div id="divpRname"></div>
                    </td>
                </tr>
                
                <!-- Well Field -->
                <tr>
                    <td style="font-weight: 600; font-size: 14px; color: #444; vertical-align: middle;">Well Field <span style="color:red;">*</span></td>
                    <td>
                        <select id="Inv_id" name="wellField" class="form-control" style="width: 100%; height: 32px; padding: 4px; border: 1px solid #ccc; border-radius: 4px;" onchange="showfieldInv(this.options[this.selectedIndex].value)">
                            <option value="0" style="font-weight: bold;">------- Select Well Field -------</option>
                        </select>
                    </td>
                </tr>  
                <!-- Drilling Permit -->
                <tr>
                    <td style="font-weight: 600; font-size: 14px; color: #444; vertical-align: middle;">Drilling Permit <span style="color:red;">*</span></td>
                    <td>
                        <select id="Dpermit" name="Dpermit" class="form-control" style="width: 100%; height: 32px; padding: 4px; border: 1px solid #ccc; border-radius: 4px;" onchange="showfieldInv(this.options[this.selectedIndex].value)">
                            <option value="0" style="font-weight: bold;">------- Select Drilling Permit -------</option>
                            <option value="YES">YES</option>
                            <option value="NO">NO</option>
                        </select>
                    </td>
                </tr> 
                <!-- Automated Bulk File Upload Section -->
                <tr>
                    <td style="font-weight: 600; font-size: 14px; color: #444; vertical-align: top; padding-top: 8px;">Supp. & Related Files</td>
                    <td>
                        <div style="border: 1px solid #e2e8f0; border-radius: 6px; padding: 15px; background-color: #f8fafc;">
                            <label id="dpthdLabel" style="display: block; font-weight: bold; font-size: 13px; color: #06402B; 
                            margin-bottom: 10px; border-bottom: 2px solid #cbd5e1; padding-bottom: 4px; cursor: pointer;">
                                📁 File Upload Packages
                            </label>
                            
                            <table border="0" style="width: 100%; border-collapse: separate; border-spacing: 0 10px;">
                                <!-- 1. Drilling Pumping Test History Data Sheet -->
                                <tr id="dpthdRow">
                                    <td style="width: 45%; font-size: 13px; font-weight: 500; color: #555;">1. Master Dataset Engine (Excel) <span style="color:red;">*</span></td>
                                    <td>
                                        <span id="pwell_span" style="display: flex; align-items: center; gap: 6px;">
                                            <input type="checkbox" id="pwell_check" name="checkwellReport" value="Productive Well" checked="checked" style="transform: scale(1.1);"/>
                                            <input type="file" name="productiveWelldata" id="pwell_id" style="font-size: 12px; color: #555; width: 100%;"/>
                                        </span>
                                    </td>
                                </tr>
                                
                                <!-- 2. Well Completion Report -->
                                <tr>
                                    <td style="font-size: 13px; font-weight: 500; color: #555;">2. Comprehensive Well Report</td>
                                    <td>
                                        <span id="pumpSytemdiv" style="display: flex; align-items: center; gap: 6px;">
                                            <input type="checkbox" id="report_check" name="reportCheck" value="wellRepoDoc" style="transform: scale(1.1);"/>
                                            <input type="file" name="reportProWell" id="reportDataid" style="font-size: 12px; color: #555; width: 100%;"/>
                                        </span>
                                    </td>
                                </tr>
                                
                                <!-- 3. Pump and Generator Specifications -->
                                <tr>
                                    <td style="font-size: 13px; font-weight: 500; color: #555;">3. Pump & Generator Specs</td>
                                    <td>
                                        <span id="pumpGeneratorDiv" style="display: flex; align-items: center; gap: 6px;">
                                            <input type="checkbox" id="pump_check" name="pumpingCheck" value="pumping_doc" style="transform: scale(1.1);"/>
                                            <input type="file" name="pumpingSdata" id="pump_id" style="font-size: 12px; color: #555; width: 100%;"/>
                                        </span>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            
            <!-- Submission and Action Row -->
            <div id="btndiv_id" style="margin-top: 10px; display: flex; justify-content: flex-end; gap: 10px; padding-right: 3px;">
                <input type="submit" value="Save Data" id="click_me" style="height: 32px; width: 120px; font-weight: bold; 
                background-color: #06402B; color: white; border: none; border-radius: 4px; cursor: pointer; transition: background 0.2s;"/>
                <input id="reset" type="reset" value="Cancel" style="height: 32px; width: 100px; font-weight: 500; background-color: #e2e8f0;
                 color: #475569; border: none; border-radius: 4px; cursor: pointer;"/>
            </div>
        </div>
    </form>
</div>
            <!--Excel Sheet that contains Detail Operational Well Data -->
<!-- Detail Operational Well Data Sheet Bulk Upload Dashboard -->
<div class="table_div item_li oprate_tech" style="display: none; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333;">
    
    <p style="text-align: center; margin-top: 1%; font-weight: bold; font-size: 17px; color: #06402B; letter-spacing: 0.5px;">
        Store Detail Operation Well Data
    </p>
    
    <form method="post" action="Upload" enctype="multipart/form-data" name="mform1" onsubmit="return(checkgwExcelform(this));">
        
        <div class="WCRFormDiv" style="background: #ffffff; padding: 25px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
            
            <input id="uu_id_II" name="db_userIdII" type="text" value="<%=choice%>" style="display:none;">
            
            <div style="border-bottom: 2px solid #e2e8f0; padding-bottom: 8px; margin-bottom: 20px;">
                <span style="font-size: 16px; font-weight: bold; color: #06402B; display: flex; align-items: center; gap: 8px;">
                    📊 Automated Operational Spreadsheet Import Manager
                </span>
                <p style="font-size: 12px; color: #64748b; margin: 4px 0 0 0;">
                    Select the data categorization packages below and attach their corresponding template logs.
                </p>
            </div>

            <!-- FLAT CARD GRID MANAGER (Replaces legacy breaking nested tables) -->
            <div style="display: flex; flex-direction: column; gap: 12px;">
                
                <!-- 1. Core Detail Operational Well Inventory Data -->
                <div style="display: grid; grid-template-columns: auto 1fr 2fr; gap: 15px; align-items: center; background: #f8fafc; border: 1px solid #e2e8f0; padding: 12px; border-radius: 6px;">
                    <input type="checkbox" id="log1" name="aditionalGWlog" value="Operation_data" checked="checked" style="transform: scale(1.25); cursor: pointer;"/>
                    <label for="log1" style="font-weight: 600; font-size: 13.5px; color: #1e293b; white-space: nowrap; margin: 0;">
                        Detail Operational Well Data <span style="color:red;">*</span>
                    </label>
                    <input type="file" name="operationalFile" id="file1" style="font-size: 12px; color: #475569; width: 100%;" required="required"/>
                </div>

                <!-- 2. Rehabilitation Logging Package (Optional Extension) -->
                <div style="display: grid; grid-template-columns: auto 1fr 2fr; gap: 15px; align-items: center; background: #ffffff; border: 1px solid #e2e8f0; padding: 12px; border-radius: 6px;">
                    <input type="checkbox" id="log5" name="aditionalGWlog" value="rehab_data" style="transform: scale(1.25); cursor: pointer;"/>
                    <label for="log5" style="font-weight: 500; font-size: 13.5px; color: #475569; white-space: nowrap; margin: 0;">
                        Rehabilitation Data (Optional)
                    </label>
                    <input type="file" name="operationalFile" id="file5" style="font-size: 12px; color: #475569; width: 100%;"/>
                </div>

                <!-- 3. Chemical Water Quality Metrics (Optional Extension) -->
                <div style="display: grid; grid-template-columns: auto 1fr 2fr; gap: 15px; align-items: center; background: #ffffff; border: 1px solid #e2e8f0; padding: 12px; border-radius: 6px;">
                    <input type="checkbox" id="log4" name="aditionalGWlog" value="chemical_data" style="transform: scale(1.25); cursor: pointer;"/>
                    <label for="log4" style="font-weight: 500; font-size: 13.5px; color: #475569; white-space: nowrap; margin: 0;">
                        Chemical Water Quality (Optional)
                    </label>
                    <input type="file" name="operationalFile" id="file4" style="font-size: 12px; color: #475569; width: 100%;"/>
                </div>

                <!-- 4. Physical Water Quality Metrics (Optional Extension) -->
                <div style="display: grid; grid-template-columns: auto 1fr 2fr; gap: 15px; align-items: center; background: #ffffff; border: 1px solid #e2e8f0; padding: 12px; border-radius: 6px;">
                    <input type="checkbox" id="log3" name="aditionalGWlog" value="physical_data" style="transform: scale(1.25); cursor: pointer;"/>
                    <label for="log3" style="font-weight: 500; font-size: 13.5px; color: #475569; white-space: nowrap; margin: 0;">
                        Physical Water Quality (Optional)
                    </label>
                    <input type="file" name="operationalFile" id="file3" style="font-size: 12px; color: #475569; width: 100%;"/>
                </div>

                <!-- 5. Biological & Radiogenic Tracking Package (Optional Extension) -->
                <div style="display: grid; grid-template-columns: auto 1fr 2fr; gap: 15px; align-items: center; background: #ffffff; border: 1px solid #e2e8f0; padding: 12px; border-radius: 6px;">
                    <input type="checkbox" id="log2" name="aditionalGWlog" value="microBio_data" style="transform: scale(1.25); cursor: pointer;"/>
                    <label for="log2" style="font-weight: 500; font-size: 13.5px; color: #475569; white-space: nowrap; margin: 0;">
                        Biological & Radiogenic (Optional)
                    </label>
                    <input type="file" name="operationalFile" id="file2" style="font-size: 12px; color: #475569; width: 100%;"/>
                </div>

                <!-- Hidden Auditor User State Tracking Row Parameters -->
                <input type="hidden" id="U_id" name="U_id" value="<%=choice%>" />
            </div>

            <!-- ACTION FOOTER ACTION SYSTEM CONTROLS -->
            <div id="btndiv_id_II" style="margin-top: 25px; display: flex; justify-content: flex-end; gap: 12px;">
                <input type="submit" value="Execute Bulk Update" id="click_me_II" style="height: 34px; width: 180px; font-weight: bold; background-color: #06402B; color: white; border: none; border-radius: 4px; cursor: pointer; transition: background 0.2s;" class="btn btn-success"/>
                <input type="reset" value="Cancel" id="reset_II" style="height: 34px; width: 100px; font-weight: 500; background-color: #e2e8f0; color: #475569; border: none; border-radius: 4px; cursor: pointer;" class="btn btn-default"/>
            </div>
        </div>
    </form>
</div>
            <!--Update WCR Data-->
            <div class="table_div item_li updateWCR" style="display: none;" id="displayUpdateContent">
            <span style="text-align: center; margin-left:6%; font-weight: bold; font-size: 17px; padding-bottom: 10px;">
              Drilling Pump Test Log Data Error Correction Page </span>
              
              <div id="wellIDforUpdate" style="display: none;"></div>
            <form action="updateWCR" method="post" name="WCRUpdateform" onsubmit="return(chechWCRUpdate(this));">
            <input id="uu_id" name="db_userUpdateWCR" type="text" value="<%=choice%>" style="display:none;">
            <div class="WCRFormDiv">
            <div class="formup">
            <table id="updateContent" border="0" class="updateData">
            <thead>
            </thead>
            <tbody>
            </tbody>
            </table>
            <span style="align-content:center;margin-top: 0.25%;">
            <input type="submit" value="Confirm" style="height: 20px; width: 100px;"/>
            <input type="reset" value="Cancel" id="back_to_table" style="height: 20px; width: 100px;"></span>
            </div>
            </div>
            </form>
            </div>
            <!--Update WO Data-->
            <div class="table_div item_li updateOGWD" style="display: none;" id="displayOGWUpdateContent">
            <p style="text-align: center;margin-top: 1%; font-weight: bold; font-size: 17px;">
              Detail Operation Well Data Error Correction Page </p>
              <div id="wellIDforOGWCUpdate" style="display: none;"></div>
            <form action="updateGWOD" method="post">
            <div class="WCRFormDiv">
            <div class="formup">
            <table id="updateOGWContent" border="0" class="updateData">
            <thead>
           </thead>
            <tbody>
            </tbody>
            </table>
            <span style="align-content:center;margin-top: 0.25%;">
            <input type="submit" value="Confirm" style="height: 20px; width: 100px;"/>
            <input type="reset" value="Cancel" id="back_to_OGDtable" style="height: 20px; width: 100px;"></span>
            </div>
            </div>
            </form>
            </div>
            <!-- Spring Water Data Sheet-->
              <div class="table_div item_li pro_report main-content" style="display: none;">
              <p style="text-align: center;margin-top: 1.1%; margin-left:-15%; font-weight: bold; font-size: 17px;">
              Add Spring Water Data </p>
             <form method="post" action="Upload" enctype="multipart/form-data"
			name="mformSW" onsubmit="return(checkSWform(this));">
			<div class="WCRFormDiv">
			<input id="action1" name="action1" type="text" value="Geotechnical" style="display: none;">
             <input id="vaal" name="vaal" type="text" value="Corporation Project" style="display: none;">
              <input id="Na1" name="Na1" type="text" value="Defualt" style="display: none;">
              <input id="u_id" name="u_id" type="text" value="<%=choice%>" style="display: none;">
			<div class="formup">
			<table border="0" align="center" class="xxxx" style="margin-left: 3%;">
				<tr>
				<td>Location(<span  style="color:red;"> *</span>):</td>
				<td>
				<select id="fcenter" name="fcenter" class="form-control" style="width: 280px; height: 25px;">
				<option value="0" style="font-weight: bold;">--------------------Select Center----------------------</option>
					</select>
				</td>
				</tr>
				<tr>
				<td>Owner Name(<span  style="color:red;"> *</span>):</td>
				<td>
				<select id="fprocess" name="fprocess" class="form-control" style="width: 280px; height: 25px;"
				onchange="showprofield(this.options[this.selectedIndex].value);">
				<option value="0" style="font-weight: bold;">-----Select Process or Use Add New option---------</option>
				<option value="1" style="font-weight: bolder;">-----------------( Add New )-----------------</option>
					</select>
						<div id="div5"></div>
				</td>
				</tr>
				<tr>
					<td class="td">Contractor(<span  style="color:red;"> *</span>):</td>
					<td>
				<select id="drfin_fid" name="drfin_fid" class="form-control" style="width: 280px; height: 25px;">
			<option value="0" style="font-weight: bold;">-------Select Project or Add New Project------</option>
			<option value="1" style="font-weight: bolder;">-----------------( Add New )-----------------</option>
					</select>
					    <table>
						<tr>
						<td id="Id_pro"></td>
						<td>
						<select style=" display: none;width: 170px;height: 25px; margin-left: -2px;" id="repopro_id" name="repopro_Tname" class="form-control">
						<option value="0" style="font-weight: bold;">---- Select Project Type----</option>
						</select>
						</td>
						</tr>
						</table>
						<table>
						<tr>
						<td id="div_ptype"></td>
						<td>
						<select style=" display: none;width: 170px;height: 25px; margin-left: -2px;" id="boundary_id" name="rep_site" class="form-control">
						<option value="0" style="font-weight: bold;">---- Select Project Types Component----</option>
						</select>
						</td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="td">Consultant (<span  style="color:red;"> *</span>):</td>
					<td style="white-space: nowrap;">
				<select id="ds_fid" name="ds_fid" class="form-control" style="width: 280px; height: 25px;">
			<option value="0" style="font-weight: bold;">-----Select Name or Use Add New option--------</option>
					</select>
					<div id="add_des_sup"></div>
						<select style="display: none;" id="super_desi" name="super_desi" class="form-control">
						<option value="0" style="font-weight: bold;">------- Select Data Type------</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="td">Springer Name(<span  style="color:red;"> *</span>):</td>
					<td style="white-space: nowrap;">
				<select id="br_fid" name="br_def" class="form-control" style="width: 280px; height: 25px;">
			<option value="0" style="font-weight: bold;">-----Select Name or Use Add New option--------</option>
					</select>
					<select style="display: none;" id="super_default" name="br_id_defualt" class="form-control">
						<option value="0" style="font-weight: bold;">------- Select Data Types Sub Branch------</option>
						</select>
					 <div id="Branchname" style="width:280px;"></div>
					</td>
				</tr>
               <tr>
					<td class="td" id="control_check">Spring Size(<span  style="color:red;"> *</span>): </td>
					<td style="white-space: nowrap;"><input type="checkbox" id="in_dr" name="log" value="Draft"/>
					<span style="margin-left: 0.0em;"> Further Information: </span>
					<input type="file" name="fileuploaddr" multiple="multiple" id="indrfile" /></td>
				</tr>
				<tr>
					<td class="td"></td>
					<td style="white-space: nowrap;"><input type="checkbox" id="in_fn" name="log" value="Final"/> 
					<span style="margin-left: 0.0em;"> Additional Materials: </span>
					<input type="file" name="fileuploadfin" multiple="multiple" id="inffile" /></td>
				</tr>
				<tr>
				<td>Found Date:</td>
				<td> <input type="date" id="start" name="pr_date" value="1950-07-22" min="1960-01-01" max="2090-12-31"></td>
				</tr>
				<tr style="display: none;">
					<td class="td">Prepared by:</td>
					<td><input type="text" id="U_id" name="U_id" value="<%=choice%>" style="height: 25px;" /></td>
				</tr>
				<tr>
				<td></td>
					<td class="td"><input id="reset" type="reset" style="height: 20px; width: 100px; margin-left: 0px; margin-top: 2px;"/>
					<input type="submit" value="Upload" style="height: 20px; width: 100px; margin-left: 5px;"/></td>
				</tr>
			</table>
			</div>
			</div>
		</form>
            </div>
            <div id="hold_response" class="main-content messageContainer" style="display: none;">
            <form action="successPage" method="get">
            <div class="WCRFormDiv" style="margin-left: -1.55%; margin-top: 0.25%;">
             <%
            String return_rpo="";
            String defualt_vaalue="";
 	 if(storedWellInfo!=null){
 		 for(File getfile:storedWellInfo){
 			 if(getfile.wellIndexforReport!=null){
 				if(getfile.wellIDforReport >= 1){
 			 		 return_rpo=getfile.wellIndexforReport;
 			 		 defualt_vaalue=getfile.WellWCRSubDiscription;	
 			 			 }
		 				else if(getfile.wellIDforReport==0001){
				 				return_rpo=getfile.wellIndexforReport; 
				 				defualt_vaalue=getfile.WellWCRSubDiscription;	
				 			 }
 			 			 else if(getfile.wellIDforReport==0){
 			 				return_rpo=getfile.wellIndexforReport; 
 			 				defualt_vaalue=getfile.WellWCRSubDiscription;	
 			 			 }
 			 }
 			 else if(getfile.regnam!=null){
  				if(getfile.regid > 1){
  	 				return_rpo=getfile.regnam;
  	 				defualt_vaalue=getfile.dsicription;
  	 			 }else if(getfile.regid==0){
  	 				return_rpo=getfile.regnam;
  	 				defualt_vaalue=getfile.dsicription;
  	 			 }else if(getfile.regid==0001){
  	 				return_rpo=getfile.regnam;
  	 				defualt_vaalue=getfile.dsicription;
  	 			 }else if(getfile.regid==00001){
  	 				return_rpo=getfile.regnam;
  	 				defualt_vaalue=getfile.dsicription;
  	 			 }
  			 }
 		} 
 		 %>
 		 <label style="font-weight: bold; font-size: 27px;">Status</label><br>
 		 <p id="jspContent"><label style="font-weight: bold; font-size: 19px;"><%=return_rpo%></label><br>
 		 <label style="font-weight: normal; font-size: 16px;"><%=defualt_vaalue %></label></p>
 		 <%
 	 }
	 if(updateMessage!=null){
		 for(File updateResponse:updateMessage){
			 if(updateResponse.updateWCRMessage!=null){
				 if(updateResponse.wellIdUWCRMessage!=0){
	 	 				return_rpo=updateResponse.updateWCRMessage;
	 	 				defualt_vaalue=updateResponse.updateWCRDicussion;
	 	 				}
				 else if(updateResponse.wellIdUWCRMessage==0){
					 return_rpo=updateResponse.updateWCRMessage;
					 defualt_vaalue=updateResponse.updateWCRDicussion;
					 }	 
				 }
			 else if(updateResponse.updateGOPUMessage!=null){
				 if(updateResponse.wellIdGWOUMessage!=0){
	 	 				return_rpo=updateResponse.updateGOPUMessage;
	 	 				defualt_vaalue=updateResponse.updateGWODicussion;
	 	 				}
				 else if(updateResponse.wellIdGWOUMessage==0){
					 return_rpo=updateResponse.updateGOPUMessage;
					 defualt_vaalue=updateResponse.updateGWODicussion;
					 }
			 }
 				
			 } 
		 %>
 		<label style="font-weight: bold; font-size: 27px;">Status</label><br>
 		 <p id="jspContent"><label style="font-weight: bold; font-size: 19px;"><%=return_rpo%></label><br>
 		 <label style="font-weight: normal; font-size: 16px;"><%=defualt_vaalue %></label></p>
 		 <%
		 }
 	 %>
 	 </div>
 	 </form>
            </div>
           </div></div>           
        <script type="text/javascript" src="dist/homepag_dynamic_tabledisplay.js"></script>
        <script type="text/javascript" src="dist/jquery-3.4.1.min.js"></script>
        <script type="text/javascript" src="dist/hompage_script.js"></script>
     <script>
     function executeContextSwitch(targetView) {
    	    if (targetView && targetView !== "0") {
    	        window.location.href = "${pageContext.request.contextPath}/RouteView?view=" + targetView;
    	    }
    	}
     
     var userType="${sessionScope.pr_code_active}";
	 const UserType_Controller=document.getElementById('UserType_Controller');
     if(userType==110){
    	 UserType_Controller.textContent = 'Administrator'; 
     }
     else{
		 UserType_Controller.textContent = 'Database Operator'; 
	 }
     document.addEventListener('DOMContentLoaded', function() {
    	    // Select all div elements that are containers for the p elements
    	    const containerDivs = document.querySelectorAll('.messageContainer'); // Replace with your actual class or selector

    	    containerDivs.forEach(div => {
    	        const pElement = document.getElementById("jspContent"); // Get the p element within the current div

    	        if (pElement && pElement.textContent.trim() !== '') {
    	            // If p element exists and its text content is not empty (after trimming whitespace)
    	            div.style.display = 'block';
    	        }
    	    });
    	});
     document.getElementById('dpthdLabel').addEventListener('click', function() {
    	    const row = document.getElementById('dpthdRow');
    	    
    	    if (row.style.display === 'none') {
    	        row.style.display = 'table-row';
    	        this.style.color='Red';
    	        this.textContent = 'Clear DPTHD';        
    	    } else {
    	        row.style.display = 'none';
    	        this.style.color='Black';
    	        this.textContent = 'Add DPTHD';
    	    }
    	});
     const city_ZoneID = document.getElementById("city_ZoneID");
     const selectedCZID = document.getElementById("selectedCZID");

     //control Well Location Display
     function locationDisplay(){
    	 const locationParameter = document.getElementById("regionEn");
    	 const regionCityName= document.getElementById("Id_div");
    	 const regionCityId= document.getElementById("regionCityID");
    	 const subCityZoneName= document.getElementById("pr_type");
    	 const cubCityZoneId= document.getElementById("subcityzoneId");
    	 if(locationParameter.value === "0"){
    		 regionCityName.style.display = "none";
    		 regionCityId.style.display = "none";
    		 subCityZoneName.style.display = "none";
    		 cubCityZoneId.style.display = "none";
    	     }else{
    	    	 regionCityName.style.display = "block";
        		 regionCityId.style.display = "block";
        		 subCityZoneName.style.display = "block";
        		 cubCityZoneId.style.display = "block";	 
    	     } 
     }
     var pro_dataResponse=document.getElementById("gtech_id");
     if (pro_dataResponse.addEventListener) {
    	 pro_dataResponse.addEventListener("click", function() {
	     document.getElementById("hold_response").style.display='none';	
	        }, false);}
     /* var pro_reportResponse=document.getElementById("pro_repo_id");
     if (pro_reportResponse.addEventListener) {
    	 pro_reportResponse.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);} */
     var mange_otherdoc=document.getElementById("mange_otherdoc");
     if (mange_otherdoc.addEventListener) {
    	 mange_otherdoc.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);}
     var mange_addtional=document.getElementById("mange_addtional");
     if (mange_addtional.addEventListener) {
    	 mange_addtional.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);}
     
     var mange_RequestResponse = document.getElementById("mange_RequestResponse");
     if (mange_RequestResponse.addEventListener) {
    	 mange_RequestResponse.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);}
     
     var manage_rawdata=document.getElementById("mange_rawdata");
     if (manage_rawdata.addEventListener) {
    	 manage_rawdata.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);}
     var pro_operational_id=document.getElementById("operational_id");
     if (pro_operational_id.addEventListener) {
    	 pro_operational_id.addEventListener("click", function() {
	      document.getElementById("hold_response").style.display='none';	
	        }, false);}
     
     
     /*Total station or GPS file Checke box controller */
      function Gps_checkbx(check_gps){
    	  check_gps=document.getElementById("log7");
    if(check_gps.checked == true){
		document.getElementById('GPS_file').innerHTML='<span style="margin-left: 5.3em;"> GPS data:</span>'
		+' <input type="checkbox" id="gps_data" name="ts_gps" value="GPS Data"/>'
	    +' <span class="validity"></span>';
	    document.getElementById('GPS_file').style.display="block";
	    document.getElementById('TS_file').innerHTML='<span style="margin-left: 2.0em;"> Total Station data:</span>'
		+' <input type="checkbox" id="TS_data" name="ts_gps" value="Total Station Data"/>'
		+' <span class="validity"></span>';
		document.getElementById('TS_file').style.display="block";	
	} 
    else{
    	document.getElementById('TS_file').innerHTML='';
    	document.getElementById('GPS_file').innerHTML='';
    	document.getElementById('GPS_file').style.display="none";
    	document.getElementById('TS_file').style.display="none";
    }
     }		
     
     function get_check(checkme){
    checkme=document.getElementById("log6");
    if(checkme.checked == true){
		document.getElementById('no_station').innerHTML='<span style="margin-left: 1.8em;"> Number of Station:</span>'
		+' <input type="number" id="st_no" name="st_no" style="width: 180px;height: 20px;" required min="1" max="999"/>'
	    +' <span class="validity"></span>';
	    document.getElementById('no_station').style.display="block";
	    document.getElementById('name_station').innerHTML='<span style="margin-left: 2.7em;"> Name of Station:</span>'
		+' <input type="text" id="st_name" name="st_name" style="width: 180px;height: 20px;" required minlength="6" maxlength="1000"/>'
		+' <span class="validity"></span>';
		document.getElementById('name_station').style.display="block";	
	} 
    else{
    	document.getElementById('name_station').innerHTML='';
    	document.getElementById('no_station').innerHTML='';
    	document.getElementById('no_station').style.display="none";
    	document.getElementById('name_station').style.display="none";
    }
     }		
     /*selection option clicking on button*/
    	 var toggler = document.getElementsByClassName("manage");
    	    var i;
    	    for (i = 0; i < toggler.length; i++) {
    	      toggler[i].addEventListener("click", function() {
    	        this.parentElement.querySelector(".sel1").classList.toggle("actives");
    	      });
    	    }
    	    </script>
         <script type="text/javascript" src="dist/jquery-3.4.1.min.js"></script>
    <script>
    var toggler = document.getElementsByClassName("caret");
    var i;
    for (i = 0; i < toggler.length; i++) {
      toggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".nested").classList.toggle("active");
        this.classList.toggle("caret-down");
      });
    }
    document.getElementById("c_logout").addEventListener("click", function(){
    	var logout=document.getElementById("dis_logout");
    	if(logout.style.display=="none"){
    		logout.style.display="block";
    	}
    	else{
    		logout.style.display="none";	
    	}
    });
    </script> 
    <script>
    </script>
    <script type="text/javascript" src="scripts/scripts.js"></script>
</body>
</html>
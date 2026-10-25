<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.time.Year"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.function.Function"%>
<%@page import="java.util.List"%>
<%@page import="net.AAWSAgDB.fileupload.Reset.RestResponse"%>
<%@page import="net.AAWSAgDB.fileupload.model.File" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> 
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
<script src='dist/script.js' type='text/javascript'></script>
<link rel="stylesheet" href="dist/tree.css">
<link rel="stylesheet" href="https://unicons.iconscout.com/release/v4.0.0/css/line.css">
<link rel="stylesheet" href="dist/commanUserstyle.css">
<link rel="stylesheet" href="dist/screen_sizesmall_mediacontroll.css">
<link rel="stylesheet" href="dist/leaflet/leaflet.css"/>
<link rel="stylesheet" href="dist/loadingStyle.css">
<link rel="shortcut icon" type="image/png" href="dist/img/aawsa_icon.png"/>
<title>Home page</title>
 <STYLE type="text/css">

.leaflet-container .combined-control {
  background-color: rgba(255, 255, 255, 0.7);
  box-shadow: 0 0 5px #bbb;
  padding: 0 5px;
  margin:0;
  margin-top:-6px;
  color: #333;
  font: 13px/1.5 "Helvetica Neue", Arial, Helvetica, sans-serif;
}
.combined-control {
border-top: 2px solid #333333;
border-right: 2px solid #333333;
border-left: 2px solid #333333;
}            
        .legend {
    width:197px;
    line-height: 215%;
    color: #555;
    font-weight:bold;
    background: rgba(255,255,255,0.6);
    background-color:white;
    cursor: pointer;
    overflow-x: auto; 
    overflow-y: auto;
    white-space: nowrap;
    font:12px; Arial, Helvetica, sans-serif;
    box-shadow: 0 0 15px rgba(0,0,0,0.2);
    
}
.legend i {
    font-size:13px;
    width: 15px;
    height: 16px;
    float: left;
    margin-right: 2px;
    margin-top: 4px;
    opacity: 0.6;
    overflow-x: auto; 
    overflow-y: visible;
} 
.textBox{
background-color:white;
margin: 0 0 0;
background: rgba(255,255,255,0.6);
box-shadow: 0 0 0 2px rgba(0, 0, 0, .1);
}
input[type="search"] {
  font-size:15px;
  border-radius: inherit;
}
input[type="search"]::placeholder {
  color: #4a4949;
} 
input[type="search"]:focus {
  box-shadow: rgba(255,255,255,0.6);
  border-color: #1183d6;
  outline: none;
}
input.icon_mark {
  border: 1px solid #555;
  width: 20px;
  height: 20px;
  padding: 9px 4px 9px 30px;
  background: transparent url("dist/img/search1.png") no-repeat;
  background-position: 4px;
}

.table_{
border: 1px solid black;
width: 100%;
white-space: nowrap;
}
.table_ tbody {
  display: block;
  min-height: calc(280px + 1 px);
  max-height: 500px;
  /*use calc for fixed ie9 & <*/
  overflow-y:scroll;
  color: #000;
}
.table_ thead {
text-align: center;
background-color: #28527b;
color: white;
z-index: 2;
}
.table_ thead tr {
  box-shadow: 0 4px 6px rgba(0,0,0,.6);
  z-index: 2;
}
.table_ tr{
  display: block;
  overflow: hidden;
}
.table_ tr:nth-child(even) {
  background-color: #0000000b;
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
.table_ tbody tr:nth-child(odd):hover{
background-color: #F1F0F0;	
}

.table_ td:nth-child(1),.table_ th:nth-child(1){
width: 6%;
}
.table_ td:nth-child(2),.table_ th:nth-child(2){
width: 36%;
}
.table_ td:nth-child(3),.table_ th:nth-child(3){
width: 24%;
}
.table_ td:nth-child(4),.table_ th:nth-child(4){
width: 14%;
}
.table_ td:nth-child(5),.table_ th:nth-child(5){
width: 6%;
}
.table_ td:nth-child(6),.table_ th:nth-child(6){
width: 9%;
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
</STYLE>   
</head>
<body>
<%
      @SuppressWarnings("unchecked")
      List<File>geo_reference=(List<File>)request.getAttribute("georef");
      @SuppressWarnings("unchecked")
      List<File>geo_legend=(List<File>)request.getAttribute("legend");
    %>
    <nav class="titbarnav">
    <ul>
    <li> <a href="#">About Us</a> </li>
     <li> <a href="#">Contact</a> </li>
      <li id="c_logout"> <a href="#">Accounts</a> 
      <div id="dis_logout" style="display: none; float: right;">
      <ul>
      <li><a data-li="con_logout" href="<c:url value="/logout" />">Logout</a></li>
      </ul>
      </div>
      </li>
    </ul>
	</nav>
	<!-- Loader Wrapper -->
    <div id="loader-wrapper">
        <div class="loader"></div>
    </div>    
    <!-- State-of-the-Art Database User Action Ribbon -->
<div style="background: #ffffff; padding: 20px -1px; margin-top:0.8%; border-bottom: 2px solid #64748b; 
display: flex; justify-content: space-between; align-items: center; font-family: 'Segoe UI', sans-serif;width: 85%; position: absolute;">
    <h4 style="margin: 0; font-size: 14px; font-weight: bold; color: #334155; display: inline-flex; align-items: center; 
    gap: 4px; white-space: nowrap;">
        👤 User: <span style="color:#475569;"><c:out value="${sessionScope.U_auto}"/></span> | 
        Role: <span id="UserType_Controller" class="badge" style="background:#64748b; background-size:10px; color:white; padding:2px 8px; font-size:12px; 
        border-radius:4px;"></span>
    </h4>
    <div id="control_display" style="display: flex; align-items: center; gap: 8px;">
        <label style="font-size:12px; font-weight:600; color:#475569;">Change Context:</label>
        <select class="form-control" onchange="executeContextSwitch(this.value)" style="height: 28px; font-size: 12px; padding: 2px 6px; 
        border-radius: 4px; border: 1px solid #cbd5e1; cursor:pointer;">
            <option value="0" selected disabled>-- Select Workspace View --</option>
            <!-- If the user's login token equals admin, allow them to navigate back up to privilege_confirm -->
            <c:choose>
             <%-- Condition 1: When Role is 120, it is an Operator --%>
			    <c:when test="${sessionScope.pr_code_active == '120'}">
			        <option value="hompage">Database Operation Terminal</option>
			    </c:when>
			    <%-- Condition 2: When Role is 110, it is an Administrator --%>
			    <c:when test="${sessionScope.pr_code_active == '110'}">
			        <option value="privilege_confirm">Administrator Terminal</option>
			        <option value="hompage">Database Operation Terminal</option>
			    </c:when>
			    <%-- Optional: Fallback option if they match neither role --%>
			    <c:otherwise>
			        <!-- Place any default text, a disabled option, or leave blank -->
			        <option value="corporateU">Portal Terminal</option>
			    </c:otherwise>
            </c:choose>
        </select>
    </div>
</div>
<div class="app-workspace-container" style="margin-top: -0.7%;">
<!-- UNIFORM SIDEBAR: State-of-the-Art WebGIS Filtering Engine & Document Repository -->
<!-- 2. FLOATING MENU TOGGLE TRIGGER BUTTON -->
<button type="button" id="map-menu-toggle-btn" class="sidebar-toggle-trigger" onclick="toggleWebGISFilterPanel()">
    <i class="fa fa-bars" id="toggle-icon-widget"></i>
</button>
<!-- 3. REFACTORED APP SIDEBAR LAYER CONTAINER -->
<!-- FIXED: Removed the inline style left rule that was blocking the toggle engine -->
<aside id="portal-webgis-sidebar" class="portal-sliding-sidebar">
    <div class="sidebar-app-title" style="padding: 20px; border-bottom: 1px solid rgba(255, 255, 255, 0.15); margin-bottom: 15px;">
        <h2 style="color: #ffffff; font-size: 16px; font-weight: 700; margin: 0; display: flex; align-items: center; gap: 10px;
        white-space: nowrap;">
            <i class="fa fa-globe"></i> AAWSA Data Portal</h2>
    </div>
    <!-- BASELINE LAYER CONTROLLERS -->
    <div class="menu-group-header" style="font-size: 11px; text-transform: uppercase; color: #a3e635; font-weight: 700; letter-spacing: 0.8px; padding: 10px 20px 6px 20px;">Baseline GIS Layers</div>
    <div class="table_div">
    <div style="padding: 0 20px 15px 20px; display: flex; flex-direction: column; gap: 10px;">
        <a href="#" class="sidebar-nav-link active_li" id="geo_ref" data-li="All_aa" style="display: flex; align-items: center; 
        gap: 12px; color: #ffffff; text-decoration: none; font-size: 13.5px; font-weight: 600; white-space: nowrap;">
            <i class="fa fa-map" style="color: #a3e635;"></i> GW Information (Spatial)</a>
        
        <div style="display: flex; flex-direction: column; gap: 4px;">
            <label for="wms_mapTrial" style="font-size: 11px; color: #cbd5e1; font-weight: 600; text-transform: uppercase;">Analytical Overlays:</label>
            <select id="wms_mapTrial" onchange="changeLayerOnMap(this.value)" class="form-control dropdown_size" style="width: 100%; height: 32px; background: #ffffff; border: 1px solid #cbd5e1; color: #1e293b; font-weight: 600; border-radius: 4px; cursor: pointer;">
                <option value="" style="font-weight: bold;">-- Custom Map Layer --</option>
            </select>
        </div>
        
    </div>

    <!-- DYNAMIC ADVANCED FILTER ACCORDION GROUPS -->
    <div class="menu-group-header" style="font-size: 11px; text-transform: uppercase; color: #a3e635; font-weight: 700; letter-spacing: 0.8px; padding: 10px 20px 6px 20px;">Advanced Query Filters</div>
    <div style="display: flex; flex-direction: column; padding: 0 12px; gap: 8px;">
        
        <!-- CATEGORY 1: WELL OPERATIONAL STATUS -->
        <details class="filter-accordion-card" open>
            <summary style="padding: 8px 12px; background: rgba(255,255,255,0.05); color: #ffffff; font-size: 12.5px; font-weight: 600; border-radius: 4px; cursor: pointer; display: flex; justify-content: space-between; align-items: center;">
                <span><i class="fa fa-info-circle" style="width:14px; color:#a3e635;"></i> Well Status</span>
                <i class="fa fa-chevron-down" style="font-size: 10px; color:#94a3b8;"></i>
            </summary>
            <div style="padding: 8px 12px; display: flex; flex-direction: column; gap: 6px; background: rgba(0,0,0,0.15); border-radius: 0 0 4px 4px;">
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="radioch" id="userQueryId" value="Functional" style="accent-color: #a3e635;"> Functional Wells
                <i id="functionalCount" class="multi-digit-badge"></i></label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="radioch" id="userQueryId1" value="Active" style="accent-color: #a3e635;"> Active Wells
                <i id="activeCount" class="multi-digit-badge"></i></label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="radioch" id="userQueryId2" value="Inactive" style="accent-color: #a3e635;"> In Active Wells
                <i id="InactiveCount" class="multi-digit-badge"></i> </label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="radioch" id="userQueryId3" value="Non Functional" style="accent-color: #a3e635;"> Abandoned Wells
                <i id="AbandonedCount" class="multi-digit-badge"></i></label>
            </div>
        </details>

        <!-- CATEGORY 2: OWNER ADMINISTRATION -->
        <details class="filter-accordion-card">
            <summary style="padding: 8px 12px; background: rgba(255,255,255,0.05); color: #ffffff; font-size: 12.5px; font-weight: 600; border-radius: 4px; cursor: pointer; display: flex; justify-content: space-between; align-items: center;">
                <span><i class="fa fa-building" style="width:14px; color:#a3e635;"></i> Owner Category</span>
                <i class="fa fa-chevron-down" style="font-size: 10px; color:#94a3b8;"></i>
            </summary>
            <div style="padding: 8px 12px; display: flex; flex-direction: column; gap: 6px; background: rgba(0,0,0,0.15); border-radius: 0 0 4px 4px;">
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="wellsWater" id="aawsaW" value="AAWSA" class="sKey2" style="accent-color: #a3e635;"> AAWSA Wells
                <i id="aawsaCount" class="multi-digit-badge"></i></label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="wellsWater" id="governmet" value="Governmental" class="sKey2" style="accent-color: #a3e635;"> Govern't Wells
                <i id="governmentCount" class="multi-digit-badge"></i></label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="wellsWater" id="nonGovern" value="Non Governmental" class="sKey2" style="accent-color: #a3e635;"> Non Govern't Wells
                <i id="nonGovenCount" class="multi-digit-badge"></i></label>
                <label style="display: flex; align-items: center; gap: 8px; color: #cbd5e1; font-size: 12px; cursor: pointer;">
                <input type="radio" name="wellsWater" id="private" value="Private" class="sKey2" style="accent-color: #a3e635;"> Private Wells
                <i id="privateCount" class="multi-digit-badge"></i></label>
            </div>
        </details>

        <!-- CATEGORY 3: HYDRAULIC FIELD ATTRIBUTES -->
        <details class="filter-accordion-card" id="Physical_hydrolics">
            <summary style="padding: 8px 12px; background: rgba(255,255,255,0.05); color: #ffffff; font-size: 12.5px; font-weight: 600; border-radius: 4px; cursor: pointer; display: flex; justify-content: space-between; align-items: center;">
                <span><i class="fa fa-tint" style="width:14px; color:#a3e635;"></i> Physical Hydraulics</span>
                <i class="fa fa-chevron-down" style="font-size: 10px; color:#94a3b8;"></i>
            </summary>
            <div style="padding: 10px; display: flex; flex-direction: column; gap: 10px; background: rgba(0,0,0,0.15); border-radius: 0 0 4px 4px; max-height: 260px; overflow-y: auto;">
                <!-- Depth Range -->
                <div>
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Well Depth (m):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input type="radio" name="wellsDepth" id="100" value="100" class="sKey3" style="accent-color:#a3e635;"> &le; 100
                        <i id="B100" class="multi-digit-badge"></i></label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input type="radio" name="wellsDepth" id="350" value="350" class="sKey3" style="accent-color:#a3e635;"> 101-350
                        <i id="BWN350" class="multi-digit-badge"></i></label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input type="radio" name="wellsDepth" id="500" value="500" class="sKey3" style="accent-color:#a3e635;"> 351-500
                        <i id="BWN500" class="multi-digit-badge"></i></label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input type="radio" name="wellsDepth" id="501" value="501" class="sKey3" style="accent-color:#a3e635;"> Above 500
                        <i id="A500" class="multi-digit-badge"></i></label>
                    </div>
                </div>
                                <!-- Discharge Yield Sub-Group -->
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Discharge Rate (L/s):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Discharge" id="15" value="15" class="sKeyQ" style="accent-color:#a3e635;"> 1 to 15</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Discharge" id="15.01" value="40" class="sKeyQ" style="accent-color:#a3e635;"> 15.01-40</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Discharge" id="40.01" value="70" class="sKeyQ" style="accent-color:#a3e635;"> 40.01-70</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Discharge" id="70.01" value="120" class="sKeyQ" style="accent-color:#a3e635;"> 70.01-120</label>
                    </div>
                </div>

                <!-- Static Water Levels Sub-Group -->
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Static Level (SWL):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="SWL" id="0.01" value="0.1" class="sKeySWL" style="accent-color:#a3e635;"> &le; 0.01</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="SWL" id="50" value="50" class="sKeySWL" style="accent-color:#a3e635;"> 1 to 50</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="SWL" id="100" value="100" class="sKeySWL" style="accent-color:#a3e635;"> 50.01-100</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="SWL" id="200" value="200" class="sKeySWL" style="accent-color:#a3e635;"> 100.01-200</label>
                    </div>
                </div>
                <!-- Dynamic Water Levels Sub-Group -->
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Dynamic Level (DWL):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input class="sKeyDWL" type="radio"  name="DWL" id="50" value="50" style="accent-color:#a3e635;"> 0.3 to 50</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input class="sKeyDWL" type="radio"  name="DWL" id="100" value="100" style="accent-color:#a3e635;"> 50.01 to 100</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input class="sKeyDWL" type="radio"  name="DWL" id="150" value="150" style="accent-color:#a3e635;"> 100.01 to 150</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input class="sKeyDWL" type="radio"  name="DWL" id="250" value="250" style="accent-color:#a3e635;"> 150.01 to 250</label>
                        
                   </div>
                </div>

                <!-- Transmissivity Sub-Group -->
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Transmissivity (m²/d):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;">
                        <input type="radio" name="Transmissivity" id="5" value="5" class="sKeyTRMT" style="accent-color:#a3e635;"> 1.4 to 5</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Transmissivity" id="50" value="50" class="sKeyTRMT" style="accent-color:#a3e635;"> 5.01 to 50</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Transmissivity" id="500" value="500" class="sKeyTRMT" style="accent-color:#a3e635;"> 50.01-500</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Transmissivity" id="501" value="1000000" class="sKeyTRMT" style="accent-color:#a3e635;"> Above 500</label>
                    </div>
                </div>
            </div>
        </details>

        <!-- CATEGORY 4: HYDROCHEMISTRY PARAMETERS -->
        <details class="filter-accordion-card" id="hydro_chemistry" style="display: none;">
            <summary style="padding: 8px 12px; background: rgba(255,255,255,0.05); color: #ffffff; font-size: 12.5px; font-weight: 600; border-radius: 4px; cursor: pointer; display: flex; justify-content: space-between; align-items: center;">
                <span><i class="fa fa-flask" style="width:14px; color:#a3e635;"></i> Hydrochemistry</span>
                <i class="fa fa-chevron-down" style="font-size: 10px; color:#94a3b8;"></i>
            </summary>
            <div style="padding: 10px; display: flex; flex-direction: column; gap: 12px; background: rgba(0,0,0,0.15); border-radius: 0 0 4px 4px; max-height: 260px; overflow-y: auto;">
                <div>
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">TDS (mg/L):</span>
                    <div style="display:flex; flex-direction:column; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="TDS" id="500" value="500" class="sKeyTDS" style="accent-color:#a3e635;"> 30 to 500</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="TDS" id="1000" value="1000" class="sKeyTDS" style="accent-color:#a3e635;"> 501 to 1000</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="TDS" id="1001" value="1000000" class="sKeyTDS" style="accent-color:#a3e635;"> Above 1000</label>
                    </div>
                </div>
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">EC (&micro;S/cm):</span>
                    <div style="display:flex; flex-direction:column; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="EC" id="1500" value="1500" class="sKeyEC" style="accent-color:#a3e635;"> 45 to 1500</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="EC" id="2500" value="2500" class="sKeyEC" style="accent-color:#a3e635;"> 1500 to 2500</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="EC" id="2501" value="1000000" class="sKeyEC" style="accent-color:#a3e635;"> Above 2500</label>
                    </div>
                </div>
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Fluoride (mg/L):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Fluoride" id="1.5" value="1.5" class="sKeyF" style="accent-color:#a3e635;"> 0.01-1.5</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Fluoride" id="3" value="3" class="sKeyF" style="accent-color:#a3e635;"> 1.51-3</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px; grid-column: span 2;"><input type="radio" name="Fluoride" id="3.01" value="1000000" class="sKeyF" style="accent-color:#a3e635;"> Above 3</label>
                    </div>
                </div>
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Iron (mg/L):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Iron" id="0.3" value="0.3" class="sKeyFe" style="accent-color:#a3e635;"> 0.01-0.3</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Iron" id="0.31" value="5" class="sKeyFe" style="accent-color:#a3e635;"> 0.31-5</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px; grid-column: span 2;"><input type="radio" name="Iron" id="5.01" value="1000000" class="sKeyFe" style="accent-color:#a3e635;"> Above 5</label>
                    </div>
                </div>
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Nitrate (mg/L):</span>
                    <div style="display:flex; flex-direction:column; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="Nitrate" id="45" value="45" class="sKeyNo3" style="accent-color:#a3e635;"> 0.01 to 45 (Standard)</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="Nitrate" id="45.01" value="100" class="sKeyNo3" style="accent-color:#a3e635;"> 45.01 to 100</label>
                                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:6px;"><input type="radio" name="Nitrate" id="100.01" value="240" class="sKeyNo3" style="accent-color:#a3e635;"> 100.01 to 240</label>
                    </div>
                </div>
                <div style="border-top: 1px solid rgba(255,255,255,0.08); padding-top: 6px;">
                    <span style="font-size: 11px; color:#94a3b8; font-weight:700; display:block; margin-bottom:4px;">Temperature (&deg;C):</span>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:4px;">
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Temperature" id="37" value="37" class="sKeytemp" style="accent-color:#a3e635;"> 18 to 37</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px;"><input type="radio" name="Temperature" id="50" value="50" class="sKeytemp" style="accent-color:#a3e635;"> 37.01-50</label>
                        <label style="color:#cbd5e1; font-size:11px; display:flex; align-items:center; gap:4px; grid-column: span 2;"><input type="radio" name="Temperature" id="90" value="90" class="sKeytemp" style="accent-color:#a3e635;"> 50.01 to 90</label>
                    </div>
                </div>
            </div>
        </details>
    </div>

    <div class="menu-group-header" style="font-size: 11px; text-transform: uppercase; color: #a3e635; font-weight: 700; letter-spacing: 0.8px; padding: 15px 20px 6px 20px;">Secured Repository</div>
    <div style="display: flex; flex-direction: column;">
        <a href="#" class="sidebar-nav-link" data-li= "Otn_we" id="DPTHData"><i class="fa fa-folder-open"></i> Groundwater Dataset</a>
        <a href="#" class="sidebar-nav-link" data-li= "Otn_bu" id="pro_clearbu"><i class="fa fa-file-pdf-o"></i> Well Completion Report</a>
        <a href="#" class="sidebar-nav-link" data-li= "Otn_gg" id="pro_cleargg"><i class="fa fa-clipboard"></i> Miscellaneous Data</a>
    </div>
    <div style="padding: 6px 12px 4px 12px;">
        <button id="reset_status" type="button" onclick="clearAllSpatialFilters()" style="width:100%; height:32px; background:#ef4444; color:#fff; 
        font-weight:bold; border:none; border-radius:4px; font-size:12px; cursor:pointer; display:flex; align-items:center; 
        justify-content:center; gap:6px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
            <i class="fa fa-refresh"></i> Reset Active Queries
        </button>
    </div></div>
</aside>
        <!-- ========================================================================= -->
<!-- ─── UNIFORM MAIN WORKSPACE WINDOW CANVASES (80% Sizing Scale Layout) ───  -->
<!-- ========================================================================= */ -->
<div class="main-window-workspace" style="display: flex; flex-direction: column; background: #f8fafc; margin-top: 0%; height: 100%; 
width: 100%; padding: 0px;margin-left: 0%;">

    <!-- ========================================================================= -->
    <!-- PANEL A: GROUNDWATER REGISTRY REPOSITORY TABULAR SUMMARY SHEET            -->
    <!-- ========================================================================= -->
    <div id="pro_displwe" class="table_div item_li Otn_we main-content" style="display: none; width: 100%; margin-left: 19%; margin-top: 1%">
        <input type="text" name="ux_name" id="Spauser_ID" value="${sessionScope.U_id1}" style="display: none;"/>
        <input type="text" name="PR_code" id="PR_code" value="${sessionScope.pr_code_active}" style="display: none;"/>
        <input type="text" name="DPTHDataName" id="DPTHDataId" value="DPTHData" style="display: none;">
        
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #06402B; padding-bottom: 6px; margin-bottom: 15px;">
            <h2 style="margin: 0; font-size: 16px; font-weight: bold; color: #06402B;"><i class="fa fa-folder-open"></i> Available Repositories: Groundwater Dataset</h2>
            <input type="search" id="pf1_namewe" name="pf1_namewe" placeholder="🔍 Search by file name..." class="form-control" style="width: 240px; height: 30px; font-size: 12px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 10px;" />
        </div>
        
        <table id="DPTHDataTeble" class="table_ updateData" style="width: 100%; border-collapse: collapse; font-size: 13px;"> 
            <thead>
                <tr style="background-color: #06402B; color: white; text-align: left;">
                    <th style="padding: 3px;">Record No.</th>
                    <th style="padding: 3px;">Data Category</th>
                    <th style="padding: 3px;">File Name</th>
                    <th style="padding: 3px; margin-top: 5px;">
                        <span id="rs-bullet-we" class="rs-label" style="font-weight:bold; color:#a3e635;">2010</span>
                        <input type="range" id="rs-range-line-we" name="rs-range-line-we" min="2010" max="2026" value="2010" 
                        style="vertical-align: middle; cursor: pointer;">
                    </th>
                    <th style="padding: 3px;">File Type</th>
                    <th style="text-align: center; padding: 3px;">Accessiblity</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>

    <!-- ========================================================================= -->
    <!-- PANEL B: WELL COMPLETION DOSSIER ARCHIVE SUMMARY SHEET                    -->
    <!-- ========================================================================= -->
    <div id="pro_displbu" class="table_div item_li Otn_bu main-content" style="display: none; width: 100%; margin-left: 19%; margin-top: 1%">
        <input type="text" name="uxbu_name" id="uxbu_name" value="${userid}" style="display: none;"/>
        
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #06402B; padding-bottom: 6px; margin-bottom: 15px;">
            <h2 style="margin: 0; font-size: 16px; font-weight: bold; color: #06402B;"><i class="fa fa-file-text-o"></i> Available Repositories: Well Completion Reports</h2>
            <input type="search" id="pf1_namebu" name="pf1_namebu" placeholder="🔍 Search by file name..." class="form-control" style="width: 240px; height: 30px; font-size: 12px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 10px;" />
        </div>
        
        <table id="Tpro_idbu" class="table_ updateData" style="width: 100%; border-collapse: collapse; font-size: 13px;"> 
            <thead>
                <tr style="background-color: #06402B; color: white; text-align: left;">
                    <th style="padding: 3px;">Record No.</th>
                    <th style="padding: 3px;">Well Field</th>
                    <th style="padding: 3px;">File Name</th>
                    <th style="padding: 3px;margin-top: 5px;">
                        <span id="rs-bullet-bu" class="rs-label" style="font-weight:bold; color:#a3e635;">2011</span>
                        <input type="range" id="rs-range-line-bu" name="rs-range-line-bu" min="2011" max="2026" value="2011" 
                        style="vertical-align:middle; cursor:pointer;">
                    </th>
                    <th style="padding: 3px;">File Type</th>
                    <th style="padding: 3px; text-align: center;">Download</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>

    <!-- ========================================================================= -->
    <!-- PANEL C: MISCELLANEOUS HYDROGEOLOGY PACKAGES METADATA SHEET               -->
    <!-- ========================================================================= -->
    <div id="pro_displgg" class="table_div item_li Otn_gg main-content" style="display: none; width: 100%; margin-left: 19%; margin-top: 1%;">
        <input type="text" name="uxgg_name" id="uxgg_name" value="${userid}" style="display: none;"/>
        
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #06402B; padding-bottom: 6px; margin-bottom: 15px;">
            <h2 style="margin: 0; font-size: 16px; font-weight: bold; color: #06402B;"><i class="fa fa-clipboard"></i> Available Repositories: Miscellaneous Data</h2>
            <input type="search" id="pf1_namegg" name="pf1_namegg" placeholder="🔍 Search by file name..." class="form-control" style="width: 240px; height: 30px; font-size: 12px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 4px 10px;" />
        </div>
        
        <table id="Tpro_idgg" class="table_" style="width: 100%; border-collapse: collapse; font-size: 13px;"> 
            <thead>
                <tr style="background-color: #06402B; color: white; text-align: left;">
                    <th style="padding: 3px;">Record No.</th>
                    <th style="padding: 3px;">Well Field</th>
                    <th style="padding: 3px;">File Name</th>
                    <th style="padding: 3px;margin-top:5px;">
                        <span id="rs-bullet-ggu" class="rs-label" style="font-weight:bold; color:#a3e635; margin-right:6px;">2011</span>
                        <input type="range" id="rs-range-line-ggu" name="rs-range-line-ggu" min="2011" max="2026" value="2011" 
                        style="vertical-align:middle; cursor:pointer;">
                    </th>
                    <th style="padding: 3px;">File Type</th>
                    <th style="padding: 3px; text-align: center;">Download</th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
    </div>
    <!-- ========================================================================= -->
    <!-- PANEL D: REFACTORED INTERACTIVE WEBGIS GEOSPATIAL DATA PORTAL (100% SCALE) -->
    <!-- ========================================================================= -->
    <div class="table_div item_li All_aa" style="width: 100%; height: 100%; box-sizing: border-box; position: relative; margin: 0; padding: 0;">
        
        <!-- Spinning loading indicator overlay mask link -->
        <div id="loaderID" class="loaderII" style="display: none; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); 
        z-index: 9999;"></div>
        
        <!-- FULL-BLEED Leaflet Map Canvas Box (100% Width & Height Allocation) -->
        <div id="map" style="width: 100%; height: 100%; border-radius: 0; box-shadow: none; background: #e2e8f0; overflow: hidden; 
        position: absolute;"></div>

        <!-- BUFFER POOL: Binds legacy metadata elements securely without cluttering the screen viewport -->
        <div style="display: none;" id="hidden-legacy-metadata-pool">
             <% if(geo_reference != null) {
                  for(File geo_ele : geo_reference) {
                      if(geo_ele.getWellId() != 0 && geo_ele.locx != 0.0 && geo_ele.locy != 0.0) { %>
                         <ul class="geo">
                             <li><%= geo_ele.getWellField() + "," + geo_ele.locx + ", " + geo_ele.locy + "," + geo_ele.getWellIndex() 
                             + "," + geo_ele.getWellId() + "," + geo_ele.getPyear() + "," + geo_ele.getWellType() %></li>
                         </ul>
             <% } } } %>
             <% if(geo_legend != null) {
                  for(File geo_lege : geo_legend) { %>
                     <ul class="geo_legend">
                         <li><%= geo_lege.pro_type + "," + geo_lege.getCount_project() %></li>
                     </ul>
             <% } } %>
        </div>
    </div>
    </div>
    </div>
        
        <script src="dist/leaflet/leaflet.js"></script>
        <script src="dist/loadingScript.js"></script>
        <script src="dist/studyArea.js" type="text/javascript"></script>
        <script src="dist/overLay_projects.js" type="text/javascript"></script>
        <%--Searches National data to the table using titles of the table--%>
        <script type="text/javascript" src="dist/tablemanage_nationaldoc.js"></script>
        <%--Searches corporation project data to the table using titles of the table--%>
        <%--search control for other office doc table --%>
      <script type="text/javascript" src="dist/tablemanage_officdoc.js"></script>
        <%--Automatic table change control--%>
        <script type="text/javascript" src="dist/dynamic_page_display.js"></script>
    	    <%--controls table title --%>
         <script type="text/javascript" src="dist/jquery-3.4.1.min.js"></script>
         
     <script>
     /**
      * Toggles the dynamic sidebar panel visibility smoothly.
      * Fixed class logic inversion to resolve layout toggle freezing bugs.
      */
     function toggleWebGISFilterPanel() {
         var sidebarEl = document.getElementById("portal-webgis-sidebar");
         var buttonEl  = document.getElementById("map-menu-toggle-btn");
         var iconEl    = document.getElementById("toggle-icon-widget");

         if (sidebarEl && buttonEl && iconEl) {
             // Toggle classes on elements using clean class lists
             sidebarEl.classList.toggle("sidebar-collapsed");
             buttonEl.classList.toggle("toggle-shifted");
             
             // Dynamically change icons based on panel visibility state
             if (sidebarEl.classList.contains("sidebar-collapsed")) {
                 iconEl.className = "fa fa-filter"; // Show filter icon when closed
                 console.log("Sidebar collapsed. Full map space active.");
             } else {
                 iconEl.className = "fa fa-bars";   // Show lines icon when open
                 console.log("Sidebar expanded. Filter menu active.");
             }
         }
     }
     
     function executeContextSwitch(targetView) {
 	    if (targetView && targetView !== "0") {
 	        window.location.href = "${pageContext.request.contextPath}/RouteView?view=" + targetView;
 	    }
 	}
     
     var sessionParameter="${sessionScope.pr_code_active}";
     const UserType_Controller=document.getElementById('UserType_Controller');
     if (sessionParameter==123){
    	const displayContent=document.getElementById("control_display");
    	if(displayContent){
    		displayContent.style.display='none';
    	}
     }
     if(sessionParameter==110){
    	 UserType_Controller.textContent = 'Administrator';
        }
   	 else if(sessionParameter==120){
   		 UserType_Controller.textContent = 'Database Operator'; 
   	 }
     else{
    	 UserType_Controller.textContent = 'Portal User'; 
     }
     
     /*selection option of Table header*/
    	 var toggler = document.getElementsByClassName("manage");
    	    var i;
    	    for (i = 0; i < toggler.length; i++) {
    	      toggler[i].addEventListener("click", function(){
    	      });
    	    }                    
        </script>
        <%--manages and controls tree content --%>
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
    })    
    
//GGUD
var rangeSlider_ggu = document.getElementById("rs-range-line-ggu");
var rangeBullet_ggu = document.getElementById("rs-bullet-ggu");
rangeSlider_ggu.addEventListener("input", showSliderValue_ggu, false);
var slidervalue_ggu;
var slidermax_ggu;
function showSliderValue_ggu(){
	rangeBullet_ggu.innerHTML = rangeSlider_ggu.value;
	slidervalue_ggu=rangeSlider_ggu.value-2010;
	slidermax_ggu=rangeSlider_ggu.max-2010;
  var bulletPosition = (slidervalue_ggu /slidermax_ggu);
  rangeBullet_ggu.style.left = (bulletPosition * 60) + "px";
}
//water and Energy
var rangeSlider_we = document.getElementById("rs-range-line-we");
var rangeBullet_we = document.getElementById("rs-bullet-we");
rangeSlider_we.addEventListener("input", showSliderValue_we, false);
var slidervalue;
var slidermax;
function showSliderValue_we(){
	rangeBullet_we.innerHTML = rangeSlider_we.value;
	slidervalue=rangeSlider_we.value-2010;
	slidermax=rangeSlider_we.max-2010;
  var bulletPosition = (slidervalue/slidermax);
  rangeBullet_we.style.left = (bulletPosition * 60) + "px";
}
//building 
var rangeSlider_bu = document.getElementById("rs-range-line-bu");
var rangeBullet_bu = document.getElementById("rs-bullet-bu");
rangeSlider_bu.addEventListener("input", showSliderValue_bu, false);
function showSliderValue_bu(){
  rangeBullet_bu.innerHTML = rangeSlider_bu.value;
  var bulletPosition = ((rangeSlider_bu.value-2010) /(rangeSlider_bu.max-2010));
  rangeBullet_bu.style.left = (bulletPosition * 60) + "px";
}


    </script> 
</body>
</html>
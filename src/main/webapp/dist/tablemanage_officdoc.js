/**
 * =========================================================================
 * ─── PANEL A: REPOSITORY DYNAMIC DATA EVENT LAYER MATRIX ───
 * =========================================================================
 */
var pro_tableacceswe = document.getElementById("DPTHDataTeble");
var xmlAccessDPTHData = new XMLHttpRequest();

// 🫵 OBJECTIVE MET: Fires dataset hydration asynchronously when Panel A is clicked and displayed
document.getElementById("DPTHData").onclick = function() {
    // Reveal the panel wrapper safely
    //this.style.display = "block";
    
    if (typeof xmlAccessDPTHData !== "undefined") {
        xmlAccessDPTHData = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        xmlAccessDPTHData = new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    if (xmlAccessDPTHData == null) {
        alert("Browser does not support XMLHTTP Request");
        return;
    }
     // 🫵 CRITICAL ALIGNMENT CORRECTION: Extracts the actual numerical user ID from your secure session input element
var activeUserDatabaseId = document.getElementById("Spauser_ID").value; 
    var url1 = "DPTHDataURL?id_active="+activeUserDatabaseId;
    xmlAccessDPTHData.open("POST", url1, true);
    xmlAccessDPTHData.onload = function () {
        var datanontype = JSON.parse(xmlAccessDPTHData.responseText);	
        protableprwe(datanontype);
    };
    xmlAccessDPTHData.send();
};

/**
 * Parses and populates Panel A's Groundwater registry data summary grid framework.
 * REFACTORED: Maps the correct unique user database identification parameter.
 */
function protableprwe(getdata) {
    // Safely look up the target table body layer context container
    var tableContainer = document.getElementById("DPTHDataTeble");
    if (!tableContainer) return;
    
    var tBodyElement = tableContainer.querySelector("tbody");
    if (!tBodyElement) {
        // Fallback fallback: Append a tbody if it isn't explicitly defined in DOM architecture
        tBodyElement = document.createElement("tbody");
        tableContainer.appendChild(tBodyElement);
    }

    var datadrop = '';
    var recordNo = 1;
    
    var activeUserDatabaseId = document.getElementById("Spauser_ID").value; 
    // Fallback security check gate if the session token is blank or unrendered
    if (!activeUserDatabaseId || activeUserDatabaseId === "") {
        activeUserDatabaseId = "0"; 
        console.warn("Security Alert: System failed to capture a valid user identity reference token. Defaulting to guest restrictions mode.");
    }

    for (var i1 = 0; i1 < getdata.length; i1++) {
        var row = getdata[i1];
        
        if (row.repo_id > 0) {
            // Generate a strictly unique element ID key tracking string for every row item container block
            var buttonUniqueId = "secure-panelA-btn-" + row.repo_id;
            
            // Reads permission states sent from the server via left join models
            var currentPermState = parseInt(row.requestStatusCache || 0); // 1: Pending, 2: Approved, 0: Unrequested
            //alert('Permision Id '+currentPermState);
            
		           var PR_code = document.getElementById("PR_code").value;
             //alert('User Type '+PR_code);
            var btnLabel = "Request";
            var btnStyle = "background: #dc2626; color: #ffffff; border: 1px solid #b91c1c; font-weight: 600;"; // Warning Red for locked items
            
            if (currentPermState === 2 || parseInt(PR_code) === 110 || parseInt(PR_code) === 120) {
                btnLabel = '<i class="fa fa-download"></i> Download';
                btnStyle = "background: #06402B; color: #ffffff; border: 1px solid #042d1e; font-weight: bold;"; // Corporate Forest Green
            }
            
            else if (currentPermState === 1) {
                btnLabel = '<i class="fa fa-hourglass-half"></i> Pending';
                btnStyle = "background: #f97316; color: #ffffff; border: 1px solid #ea580c;"; // Warning Orange
            }

            datadrop += '<tr>' +
                            '<td style="padding: 6px 4px; font-weight: 600; color: #475569; text-align: center;">' + recordNo + '</td>' +
                            '<td style="padding: 6px 4px;"><div class="long" style="font-weight: 600; color: #334155;">' + (row.dataCategory || '_') + '</div></td>' +
                            '<td style="padding: 6px 4px;"><div class="long" style="color: #0f172a;">' + (row.fileName || '_') + '</div></td>' +
                            '<td style="padding: 6px 4px;"><div class="long" style="font-family: monospace; color: #475569;">' + (row.recordate || '_') + '</div></td>' +
                            '<td style="padding: 6px 4px;"><div class="long"><span style="background: #f1f5f9; padding: 2px 6px; border-radius: 4px; font-size: 11px; color: #475569; font-weight: 600; text-transform: uppercase;">' + (row.filetype || '_') + '</span></div></td>' +
                            
                            // 🫵 INTEGRATED SECURITY PASS ACTION CELL: Sends the verified user database identifier parameter
                            '<td style="text-align: center; padding: 1px 0;">' +
                                '<div class="long">' +
                                    '<button type="button" id="' + buttonUniqueId + '" onclick="requestSecuredRepositoryDocument(' 
                                    + row.repo_id + ', \'' + activeUserDatabaseId + '\', \'' + buttonUniqueId + '\')" style="' + btnStyle 
                                    + ' padding: 4px 1px; border-radius: 4px; font-size: 11px; cursor: pointer; width: 90px; text-align: center;'
                                    +' box-sizing: border-box; transition: all 0.2s ease; box-shadow: 0 1px 1px rgba(0,0,0,0.1);">' +
                                        btnLabel +
                                    '</button>' +
                                '</div>' +
                            '</td>' +
                        '</tr>';
            recordNo += 1;
        }
    }
    
    // Purge old table data rows cleanly right before drawing the newly secured dataset matrix elements array
    tBodyElement.innerHTML = '';
    tBodyElement.insertAdjacentHTML('beforeend', datadrop);	    
}
/**
 * =========================================================================
 * ─── SECURED REPOSITORY SINGLE-CLICK ACCESS INTERCEPTOR ───
 * =========================================================================
 */
/**
 * Intercepts download attempts from the Secured Repository tables.
 * Validates permissions asynchronously against the database tracking tables.
 */
function requestSecuredRepositoryDocument(repo_id, user_process_id, button_element_id) {
    if (!repo_id || !user_process_id) return;

    // 1. Dynamic Page Loader Trigger
    if (typeof loaderID !== 'undefined' && loaderID) {
        loaderID.style.display = 'block';
    }

    // 2. Query download_requests schema table to inspect permission records
    var verificationUrl = "checkRepositoryAccess?repo_id=" + encodeURIComponent(repo_id) + "&user_id=" + encodeURIComponent(user_process_id);

    fetch(verificationUrl, { method: 'POST' })
        .then(function(response) { 
            if (!response.ok) throw new Error(); 
            return response.json(); 
        })
        .then(function(authData) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            
            var permissionState = parseInt(authData.status); // 0: Unrequested, 1: Pending, 2: Approved, 3: Declined
            var userTypeCode = parseInt(authData.userType || 0); // Active user privilege flag from backend

            // Bypass checks if user is Admin (110), Database Operator (120), or already Approved (2)
            if (userTypeCode === 110 || userTypeCode === 120 || permissionState === 2) {
                console.log("Access Granted. Redirecting document binary stream...");
                window.location.href = "downloadfile?file_id=" + repo_id + "&DPTH_ID=" + user_process_id;
            } 
            else if (permissionState === 1) {
                alert("🔒 Access Review Pending:\n\nYour permission token request for this document is currently in the Database Operator's verification queue.");
            } 
            else {
                var confirmSubmit = confirm("🔒 Access Restricted: You do not possess download privileges for this secured repository dataset.\n\nWould you like to transmit an official authorization request to the Database Operator right now?");
                if (confirmSubmit) {
                    dispatchAccessRequestToOperator(user_process_id, repo_id, button_element_id);
                }
            }
        })
        .catch(function(error) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            console.error("Security trace check crash:", error);
            alert("Security Error: Unable to verify role access parameters against the database.");
        });
}

/**
 * Logs a new request entry row inside gw_db_schema.download_requests.
 * Instantly morphs the button layout style to an alert orange 'Pending' state.
 */
function dispatchAccessRequestToOperator(user_id, repo_id, button_id) {
    if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'block';

    var submissionUrl = "submitRepositoryRequest?repo_id=" + encodeURIComponent(repo_id) + "&user_id=" + encodeURIComponent(user_id);

    fetch(submissionUrl, { method: 'POST' })
        .then(function(response) { if (!response.ok) throw new Error(); return response.json(); })
        .then(function(result) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            
            alert("🚀 Request Transmitted: The Database Operator has been notified. The download button will turn orange while awaiting approval.");
            
            // DYNAMIC BUTTON PROFLE SHIFT: Update layout properties to show an intermediate 'Pending' state
            var targetBtn = document.getElementById(button_id);
            if (targetBtn) {
                targetBtn.style.background = "#f97316"; // Transition color profile from Warning Red to Alert Orange
                targetBtn.style.color = "#ffffff";
                targetBtn.innerHTML = '<i class="fa fa-hourglass-half"></i> Pending';
            }
        })
        .catch(function(error) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            console.error("Request dispatch failed:", error);
            alert("Transmission Error: Failed to log request authorization token into database.");
        });
}



//Access project document table for GGUDSWS 
var pro_tableaccesgg=document.getElementById("Tpro_idgg");
var protableacess_prgg=document.getElementById("prosepr_idgg");
var protable_requestgg=new XMLHttpRequest();
document.getElementById("process_idgg").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof protable_requestgg != "undefined"){
		 protable_requestgg= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestgg = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestgg == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var userId=document.getElementById("uxgg_name").value
       var url1="select_byprocess?pro_id="+nonc_id+"&userId="+userId;
	protable_requestgg.open("POST",url1, true);
	protable_requestgg.onload= function () {
	var datanontype = JSON.parse(protable_requestgg.responseText);	
	protableprgg(datanontype);
	Acccess_protableprgg(datanontype);
	};
	protable_requestgg.send();
};
function Acccess_protableprgg(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_prgg.options.length=1;
		protableacess_prgg.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protableprgg(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==17){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idgg");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableaccesgg.insertAdjacentHTML('beforeend',datadrop);	    
}

//Access project document table for Water and Energy 
var pro_tableacceswe=document.getElementById("Tpro_idwe");
var protableacess_basinhwe=document.getElementById("prosel_idwe");
var userprocess;
var protable_requestwe=new XMLHttpRequest();
document.getElementById("prosepr_idwe").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof protable_requestwe != "undefined"){
		 protable_requestwe= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestwe = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestwe == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       } 
       userprocess=document.getElementById("ux_name").value;
       var url1="select_byprojectname?pro_id="+nonc_id+"&userId="+userprocess;
	protable_requestwe.open("POST",url1, true);
	protable_requestwe.onload= function () {
	var datanontyped = JSON.parse(protable_requestwe.responseText);	
	protablewe(datanontyped);
	Acccess_protableheaderwe(datanontyped);
	};
	protable_requestwe.send();
};
function Acccess_protableheaderwe(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= 0 && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_basinhwe.options.length=1;
		protableacess_basinhwe.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protablewe(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==15){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idwe");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableacceswe.insertAdjacentHTML('beforeend',datadrop);	    
}
//project file access by name Water and Energy
var pro_tablenamewe=document.getElementById("Tpro_idwe");
var pro_request_bynamewe=new XMLHttpRequest();
document.getElementById("pf1_namewe").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bynamewe != "undefined"){
		 pro_request_bynamewe= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   pro_request_bynamewe = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (pro_request_bynamewe == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       userprocess=document.getElementById("ux_name").value;
	var url1="select_prname?pfile_name="+nonc_id+"&userIdname="+userprocess;
	pro_request_bynamewe.open("POST", url1, true);
	pro_request_bynamewe.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bynamewe.responseText);	
	probynamewe(datanonbasin);
	};
	pro_request_bynamewe.send();
};
function probynamewe(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==15){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idwe");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tablenamewe.insertAdjacentHTML('beforeend',datadrop);	    
}
//project document type of Transport and Design
var prodocnon_tabletd=document.getElementById("Tpro_idtd");
var prohold_catahtd=document.getElementById("procat_idtd");
var prodocnoncata_requesttd=new XMLHttpRequest();
  document.getElementById("prosel_idtd").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnoncata_requesttd != "undefined"){
		 prodocnoncata_requesttd= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnoncata_requesttd = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnoncata_requesttd == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var tduser=document.getElementById("uxtd_name").value;
	var url1="select_prodoctype?type_id="+nonc_id+"&UserIdp="+tduser;
	prodocnoncata_requesttd.open("POST", url1, true);
	prodocnoncata_requesttd.onload= function () {
	var datanoncata = JSON.parse(prodocnoncata_requesttd.responseText);	
	prodocCatScattd(datanoncata);
	projdoccatheadertd(datanoncata);
	};
	prodocnoncata_requesttd.send();
};
function projdoccatheadertd(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		prohold_catahtd.options.length=1;
		prohold_catahtd.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function prodocCatScattd(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==16){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idtd");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
prodocnon_tabletd.insertAdjacentHTML('beforeend',datadrop);	    
}   
//project catagory Transport and Design
var proclunon_tabletd=document.getElementById("Tpro_idtd");
var prodocnonclu_requesttd=new XMLHttpRequest();
  document.getElementById("procat_idtd").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnonclu_requesttd != "undefined"){
		 prodocnonclu_requesttd= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnonclu_requesttd = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnonclu_requesttd == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var tduser=document.getElementById("uxtd_name").value;
	var url1="select_procat?clu_id="+nonc_id+"&userIdcl="+tduser;
	prodocnonclu_requesttd.open("POST", url1, true);
	prodocnonclu_requesttd.onload= function () {
	var datanoncata = JSON.parse(prodocnonclu_requesttd.responseText);	
	catprodocCatScattd(datanoncata);
	};
	prodocnonclu_requesttd.send();
};
function catprodocCatScattd(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==16){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idtd");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
proclunon_tabletd.insertAdjacentHTML('beforeend',datadrop);	    
} 
//project file access by date 
var pro_tabledatetd=document.getElementById("Tpro_idtd");
var pro_request_bydatetd=new XMLHttpRequest();
document.getElementById("rs-range-line-td").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bydatetd != "undefined"){
		 pro_request_bydatetd= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   pro_request_bydatetd = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (pro_request_bydatetd == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var tduser=document.getElementById("uxtd_name").value;
	var url1="select_datepro?date_id="+nonc_id+"&userIddate="+tduser;
	pro_request_bydatetd.open("POST", url1, true);
	pro_request_bydatetd.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bydatetd.responseText);	
	probydatetd(datanonbasin);
	};
	pro_request_bydatetd.send();
};
function probydatetd(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==16){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idtd");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tabledatetd.insertAdjacentHTML('beforeend',datadrop);	    
}
//project file access by name 
var pro_tablenametd=document.getElementById("Tpro_idtd");
var pro_request_bynametd=new XMLHttpRequest();
document.getElementById("pf1_nametd").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bynametd != "undefined"){
		 pro_request_bynametd= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   pro_request_bynametd = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (pro_request_bynametd == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var tduser=document.getElementById("uxtd_name").value;
	var url1="select_prname?pfile_name="+nonc_id+"&userIdname="+tduser;
	pro_request_bynametd.open("POST", url1, true);
	pro_request_bynametd.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bynametd.responseText);	
	probynametd(datanonbasin);
	};
	pro_request_bynametd.send();
};
function probynametd(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==16){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idtd");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tablenametd.insertAdjacentHTML('beforeend',datadrop);	    
}
//Access project document table for Building and Urban 
var pro_tableaccesbu=document.getElementById("Tpro_idbu");
var protableacess_prbu=document.getElementById("prosepr_idbu");
var protable_requestbu=new XMLHttpRequest();
document.getElementById("process_idbu").onchange = function(evnt){
	var process_id=evnt.target.value;
	 if (typeof protable_requestbu != "undefined"){
		 protable_requestbu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestbu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestbu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
       var url1="select_byprocess?pro_id="+process_id+"&userId="+Buser;
	protable_requestbu.open("POST",url1, true);
	protable_requestbu.onload= function () {
	var datanontype = JSON.parse(protable_requestbu.responseText);	
	protableprbu(datanontype);
	Acccess_proprbu(datanontype);
	};
	protable_requestbu.send();
};
function Acccess_proprbu(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_prbu.options.length=1;
		protableacess_prbu.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protableprbu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableaccesbu.insertAdjacentHTML('beforeend',datadrop);	    
}
//Access project document table for Building and Urban 
var pro_tableaccesbu=document.getElementById("Tpro_idbu");
var protableacess_basinhbu=document.getElementById("prosel_idbu");
var protable_requestbu=new XMLHttpRequest();
document.getElementById("prosepr_idbu").onchange = function(evnt){
	var process_id=evnt.target.value;
	 if (typeof protable_requestbu != "undefined"){
		 protable_requestbu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestbu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestbu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
       var url1="select_byprojectname?pro_id="+process_id+"&userId="+Buser;
	protable_requestbu.open("POST",url1, true);
	protable_requestbu.onload= function () {
	var datanontype = JSON.parse(protable_requestbu.responseText);	
	protablebu(datanontype);
	Acccess_protableheaderbu(datanontype);
	};
	protable_requestbu.send();
};
function Acccess_protableheaderbu(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_basinhbu.options.length=1;
		protableacess_basinhbu.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protablebu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableaccesbu.insertAdjacentHTML('beforeend',datadrop);	    
}
//project document type of Building and Urban
var prodocnon_tablebu=document.getElementById("Tpro_idbu");
var prohold_catahbu=document.getElementById("procat_idbu");
var prodocnoncata_requestbu=new XMLHttpRequest();
  document.getElementById("prosel_idbu").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnoncata_requestbu != "undefined"){
		 prodocnoncata_requestbu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnoncata_requestbu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnoncata_requestbu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
	var url1="select_prodoctype?type_id="+nonc_id+"&UserIdp="+Buser;
	prodocnoncata_requestbu.open("POST", url1, true);
	prodocnoncata_requestbu.onload= function () {
	var datanoncata = JSON.parse(prodocnoncata_requestbu.responseText);	
	prodocCatScatbu(datanoncata);
	projdoccatheaderbu(datanoncata);
	};
	prodocnoncata_requestbu.send();
};
function projdoccatheaderbu(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		prohold_catahbu.options.length=1;
		prohold_catahbu.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function prodocCatScatbu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
prodocnon_tablebu.insertAdjacentHTML('beforeend',datadrop);	    
}   
//project catagory Building and Urban
var proclunon_tablebu=document.getElementById("Tpro_idbu");
var prodocnonclu_requestbu=new XMLHttpRequest();
  document.getElementById("procat_idbu").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnonclu_requestbu != "undefined"){
		 prodocnonclu_requestbu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnonclu_requestbu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnonclu_requestbu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
	var url1="select_cl?clu_id="+nonc_id+"&userIdcl="+Buser;
	prodocnonclu_requestbu.open("POST", url1, true);
	prodocnonclu_requestbu.onload= function () {
	var datanoncata = JSON.parse(prodocnonclu_requestbu.responseText);	
	catprodocCatScatbu(datanoncata);
	catprojdoccatheaderbu(datanoncata);
	};
	prodocnonclu_requestbu.send();
};
function catprodocCatScatbu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
proclunon_tablebu.insertAdjacentHTML('beforeend',datadrop);	    
} 
//project file access by name for Building and Urban
var pro_tabledatebu=document.getElementById("Tpro_idbu");
var pro_request_bydatebu=new XMLHttpRequest();
document.getElementById("rs-range-line-bu").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bydatebu != "undefined"){
		 pro_request_bydatebu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   pro_request_bydatebu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (pro_request_bydatebu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
	var url1="select_datepro?date_id="+nonc_id+"&userIddate="+Buser;
	pro_request_bydatebu.open("POST", url1, true);
	pro_request_bydatebu.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bydatebu.responseText);	
	probydatebu(datanonbasin);
	};
	pro_request_bydatebu.send();
};
function probydatebu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tabledatebu.insertAdjacentHTML('beforeend',datadrop);	    
}
//project file access by name for Building and Urban
var pro_tablenamebu=document.getElementById("Tpro_idbu");
var pro_request_bynamebu=new XMLHttpRequest();
document.getElementById("pf1_namebu").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bynamebu != "undefined"){
		 pro_request_bynamebu= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   pro_request_bynamebu = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (pro_request_bynamebu == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Buser=document.getElementById("uxbu_name").value;
	var url1="select_prname?pfile_name="+nonc_id+"&userIdname="+Buser;
	pro_request_bynamebu.open("POST", url1, true);
	pro_request_bynamebu.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bynamebu.responseText);	
	probynamebu(datanonbasin);
	};
	pro_request_bynamebu.send();
};
function probynamebu(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==20){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idbu");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tablenamebu.insertAdjacentHTML('beforeend',datadrop);	    
}
//Access project document table for Research and Laboratory 
var pro_tableaccesrl=document.getElementById("Tpro_idrl");
var protableacess_baprrl=document.getElementById("prosepr_idrl");
var protable_requestrl=new XMLHttpRequest();
document.getElementById("process_idrl").onchange = function(evnt){
	var process_id=evnt.target.value;
	 if (typeof protable_requestrl != "undefined"){
		 protable_requestrl= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestrl = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestrl == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Rluser=document.getElementById("uxrl_name").value;
       var url1="select_byprocess?pro_id="+process_id+"&userId="+Rluser;
	protable_requestrl.open("POST",url1, true);
	protable_requestrl.onload= function () {
	var datanontype = JSON.parse(protable_requestrl.responseText);	
	protableprrl(datanontype);
	Acccess_proprrl(datanontype);
	};
	protable_requestrl.send();
};
function Acccess_proprrl(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_baprrl.options.length=1;
		protableacess_baprrl.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protableprrl(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==18){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idrl");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableaccesrl.insertAdjacentHTML('beforeend',datadrop);	    
}
//Access project document table for Research and Laboratory 
var pro_tableaccesrl=document.getElementById("Tpro_idrl");
var protableacess_basinhrl=document.getElementById("prosel_idrl");
var protable_requestrl=new XMLHttpRequest();
document.getElementById("prosepr_idrl").onchange = function(evnt){
	var process_id=evnt.target.value;
	 if (typeof protable_requestrl != "undefined"){
		 protable_requestrl= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   protable_requestrl = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (protable_requestrl == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Rluser=document.getElementById("uxrl_name").value;
       var url1="select_byprojectname?pro_id="+process_id+"&userId="+Rluser;
	protable_requestrl.open("POST",url1, true);
	protable_requestrl.onload= function () {
	var datanontype = JSON.parse(protable_requestrl.responseText);	
	protablerl(datanontype);
	Acccess_protableheaderrl(datanontype);
	};
	protable_requestrl.send();
};
function Acccess_protableheaderrl(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		protableacess_basinhrl.options.length=1;
		protableacess_basinhrl.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function protablerl(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==18){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idrl");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
pro_tableaccesrl.insertAdjacentHTML('beforeend',datadrop);	    
}
//project document type of Research and Laboratory
var prodocnon_tablerl=document.getElementById("Tpro_idrl");
var prohold_catahrl=document.getElementById("procat_idrl");
var prodocnoncata_requestrl=new XMLHttpRequest();
  document.getElementById("prosel_idrl").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnoncata_requestrl != "undefined"){
		 prodocnoncata_requestrl= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnoncata_requestrl = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnoncata_requestrl == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Rluser=document.getElementById("uxrl_name").value;
	var url1="select_prodoctype?type_id="+nonc_id+"&UserIdp="+Rluser;
	prodocnoncata_requestrl.open("POST", url1, true);
	prodocnoncata_requestrl.onload= function () {
	var datanoncata = JSON.parse(prodocnoncata_requestrl.responseText);	
	prodocCatScatrl(datanoncata);
	projdoccatheaderrl(datanoncata);
	};
	prodocnoncata_requestrl.send();
};
function projdoccatheaderrl(getheader){
	 var datadrop='';
	for(var i=0; i< getheader.length; i++){
		if(getheader[i].spa_id!= null && getheader[i].cata_gory==null){
	datadrop+='<option class="option_css" value="' + getheader[i].spa_id + '">' + getheader[i].spatial_or_non + '</option>';	
		}
		prohold_catahrl.options.length=1;
		prohold_catahrl.insertAdjacentHTML('beforeend',datadrop);	
	}
}
function prodocCatScatrl(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==18){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idrl");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
prodocnon_tablerl.insertAdjacentHTML('beforeend',datadrop);	    
}   
//project catagory Research and Laboratory
var proclunon_tablerl=document.getElementById("Tpro_idrl");
var prodocnonclu_requestrl=new XMLHttpRequest();
  document.getElementById("procat_idrl").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof prodocnonclu_requestrl != "undefined"){
		 prodocnonclu_requestrl= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   prodocnonclu_requestrl = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (prodocnonclu_requestrl == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
       var Rluser=document.getElementById("uxrl_name").value;
	var url1="select_cl?clu_id="+nonc_id+"&userIdcl="+Rluser;
	prodocnonclu_requestrl.open("POST", url1, true);
	prodocnonclu_requestrl.onload= function () {
	var datanoncata = JSON.parse(prodocnonclu_requestrl.responseText);	
	catprodocCatScatrl(datanoncata);
	catprojdoccatheaderrl(datanoncata);
	};
	prodocnonclu_requestrl.send();
};
function catprodocCatScatrl(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if( getdata[i1].cateid!= null && getdata[i1].branch_name!= null && getdata[i1].na_id==18){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idrl");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
 if(i>1) {
   row = table1.rows[i-1];
   row.parentNode.removeChild(row);
 }
}
proclunon_tablerl.insertAdjacentHTML('beforeend',datadrop);	    
} 
//project file access by name Research and Laboratory
var pro_tabledaterl=document.getElementById("Tpro_idrl");
var pro_request_bydaterl=new XMLHttpRequest();
document.getElementById("rs-range-line-rl").onchange = function(evnt){
	var nonc_id=evnt.target.value;
	 if (typeof pro_request_bydaterl != "undefined"){
		 pro_request_bydaterl= new XMLHttpRequest();
     }
     else if (window.ActiveXObject){
    	 pro_request_bydaterl = new ActiveXObject("Microsoft.XMLHTTP");
     }
     if (pro_request_bydaterl == null){
     alert("Browser does not support XMLHTTP Request")
     return;
     }
     var Rluser=document.getElementById("uxrl_name").value;
	var url1="select_datepro?date_id="+nonc_id+"&userIddate="+Rluser;
	pro_request_bydaterl.open("POST", url1, true);
	pro_request_bydaterl.onload= function () {
	var datanonbasin = JSON.parse(pro_request_bydaterl.responseText);	
	probydaterl(datanonbasin);
	};
	pro_request_bydaterl.send();
};
function probydaterl(getdata){
var datadrop='';
for(var i1=0; i1< getdata.length; i1++){
	if(getdata[i1].na_id==18){
	datadrop+='<tr><td data-Eco="Spa"><div class="long">' + getdata[i1].process + '</div></td>'+
    '<td data-Eco="proName"><div class="long">' + getdata[i1].project_name + '</div></td>'+
    '<td data-Eco="Spa"><div class="long">' + getdata[i1].data_type + '</div></td>'+
    '<td data-Eco="Cat"><div class="long">' + getdata[i1].branch_name + '</div></td>'+
    '<td data-Eco="Clu"><div class="long">' + getdata[i1].sub_branch + '</div></td>'+
    '<td data-Eco="Bas"><div class="long">' + getdata[i1].project_id + '</div></td>'+
    '<td data-Eco="Yea">' + getdata[i1].date + '</td>'+
    '<td data-Eco="Fil"><div class="long">' + getdata[i1].name + '</div></td>'+
    '<td data-Eco="Con">' + getdata[i1].file_ext + ' file</td>'+
    '<td data-Eco="Dow"> <a href="downloadfile?file_id='+getdata[i1].name + 
    '&project_name='+ getdata[i1].project_name +'&project_id='+ getdata[i1].project_id +
    '&cat_name='+ getdata[i1].data_type +'">Download</a></td></tr>';
}}
var table1 = document.getElementById("Tpro_idrl");
for (var i = table1.rows.length; i>0 ; i--) {   //iterate through rows
if(i>1) {
 row = table1.rows[i-1];
 row.parentNode.removeChild(row);
}
}
pro_tabledaterl.insertAdjacentHTML('beforeend',datadrop);	    
}
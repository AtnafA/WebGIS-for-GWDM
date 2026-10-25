/**
 * 
 */
/*Handle data from project name*/
var holdDatashetLog=document.getElementById("CoVrastar_id");
var xmlRequestDatalog=new XMLHttpRequest();
document.getElementById("ask_request").onclick=function(){
	if (typeof xmlRequestDatalog != "undefined"){
 			xmlRequestDatalog= new XMLHttpRequest();
 	         }
 	         else if (window.ActiveXObject){
 	        	xmlRequestDatalog= new ActiveXObject("Microsoft.XMLHTTP");
 	         }
 	         if (xmlRequestDatalog==null){
 	         alert("Browser does not support XMLHTTP Request")
 	         return;
 	         }
 	 var url="CRUDOperationLog";
 	        xmlRequestDatalog.open("POST", url,true);
 	       xmlRequestDatalog.onload=function(){
 	        var prodata=JSON.parse(xmlRequestDatalog.responseText);
 	       DatashetLogFunction(prodata);
 	        	 }
 	      xmlRequestDatalog.send();	         
}
function DatashetLogFunction(datashetLog){
 	 	var ret='';	
 	 	for(var i=0; i<datashetLog.length; i++){
 	 		if(datashetLog[i].logNumber!=0){
 	 		ret+='<tr><td data-Eco="Cat"><div class="long">' + datashetLog[i].logNumber + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datashetLog[i].well_indexLog + '</div></td>'+
   		          '<td data-Eco="Reg"><div class="long">' + datashetLog[i].dataSheet + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datashetLog[i].actionTook + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datashetLog[i].userName + '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datashetLog[i].dateActionTook + '</div></td></tr>'	
 	 		}
 	 	}
 	var table = document.getElementById("CoVrastar_id");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	holdDatashetLog.insertAdjacentHTML('beforeend',ret);	
}
var handleByDate = document.getElementById("CoVrastar_id");
var xmlHttpDateRequest=new XMLHttpRequest();
document.getElementById("dateSlider").onchange=function (event){
var dateEvent=event.target.value;
if (typeof xmlHttpDateRequest != "undefined"){
 			xmlHttpDateRequest= new XMLHttpRequest();
 	         }
 	         else if (window.ActiveXObject){
 	        	xmlHttpDateRequest= new ActiveXObject("Microsoft.XMLHTTP");
 	         }
 	         if (xmlHttpDateRequest==null){
 	         alert("Browser does not support XMLHTTP Request")
 	         return;
 	         }
 	         if(dateEvent>=1 && dateEvent<=31){
				  if(dateEvent<10){
					dateEvent='0'+dateEvent;  
				  }
			var url="CRUDOperationLogByDate?CRUDdate="+dateEvent;
			xmlHttpDateRequest.open("POST", url,true);
			xmlHttpDateRequest.onload=function(){
				var prodata=JSON.parse(xmlHttpDateRequest.responseText);
				holdDatashetLogByDateFun(prodata);
				}
				xmlHttpDateRequest.send();
				}
				}
function holdDatashetLogByDateFun(dataByDate){
	var ret='';	
 	 	for(var i=0; i<dataByDate.length; i++){
 	 		if(dataByDate[i].logNumberdate!=0){
 	 		ret+='<tr><td data-Eco="Cat"><div class="long">' + dataByDate[i].logNumberdate + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + dataByDate[i].well_indexLogdate + '</div></td>'+
   		          '<td data-Eco="Reg"><div class="long">' + dataByDate[i].dataSheetdate + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + dataByDate[i].actionTookdate + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + dataByDate[i].userNamedate + '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + dataByDate[i].dateActionTookdate + '</div></td></tr>'	
 	 		}
 	 	}
var table = document.getElementById("CoVrastar_id");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	handleByDate.insertAdjacentHTML('beforeend',ret);	
}

//User Account Management
var xmlrequestManageAccount=new XMLHttpRequest();
var manageAccount=document.getElementById("manageAccount");
document.getElementById("manage_user").onclick=function(evt){
	if (typeof xmlrequestManageAccount != "undefined"){
		xmlrequestManageAccount= new XMLHttpRequest();
         }
         else if (window.ActiveXObject){
        	 xmlrequestManageAccount= new ActiveXObject("Microsoft.XMLHTTP");
         }
         if (xmlrequestManageAccount==null){
         alert("Browser does not support XMLHTTP Request")
         return;
         }
         var userId=document.getElementById("1404ID").value;
  var url1="accountManagement?usreID="+userId;
         xmlrequestManageAccount.open("POST", url1,true);
         xmlrequestManageAccount.onload=function(){
        var design_sudata=JSON.parse(xmlrequestManageAccount.responseText);
        printUserAccount(design_sudata);
        	 }
         xmlrequestManageAccount.send();
         } 
function printUserAccount(datafrom){
	var datadrop='';
	var activate='';
	var status=false;
	for(var i=0; i< datafrom.length; i++){
			if(datafrom[i].user_id!=0){
				if(datafrom[i].status==='Active'){
					activate='InActivate';
					status=false;
				}else{
					activate='Activate';
					status=true;
				}
				 datadrop+='<tr><td data-Eco="Cat"><div class="long">' + datafrom[i].user_id + '</div></td>'+
				 '<td data-Eco="Clu"><div class="long">' + datafrom[i].fullName + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].userName + '</div></td>'+
   		          '<td data-Eco="Reg"><div class="long">' + datafrom[i].userType + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].status + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long"><select id="privilageType" name="privilageType" class="formSelectedPrivilege">'
   		          +'<option value="4444">Select User Type</option>'
   		          +'<option value="123">Portal User</option>'
   		          +'<option value="120">Database Operator</option>'
   		          +'<option value="110">Administrator</option></select></div></td>'
   		          +'<td data-Eco="Yea"><div class="long"><label><input class="status" type="radio" class="selectedValue" value="'+status+'"> '+activate+'</label></div></td>'
   		          +'<td><a href="#" onclick="deleteOD(this)"><strong>Save</strong></a></td></tr>';			
			}
	}
var table = document.getElementById("manageAccount");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	manageAccount.insertAdjacentHTML('beforeend',datadrop);       	
}
var holdUpdatedUserAccount=document.getElementById("manageAccount");
 	var xml_delete=new XMLHttpRequest();
 	function deleteOD(buttonDelete){
			var row=buttonDelete.parentNode.parentNode;	
			
			var checkedRadio = row.querySelector('input[type="radio"]:checked');
			var userStatusValue
			if (checkedRadio) {
				userStatusValue=checkedRadio.value
			}
			var selectedPrivilege= row.querySelector('.formSelectedPrivilege');
				var user_name=document.getElementById("userFullName").value=row.cells[1].innerText;
				var accountStatus=document.getElementById("activStatus").value=userStatusValue;
				var privilage=document.getElementById("privilage").value=selectedPrivilege.value
				var user1404Id=document.getElementById("1404ID").value;
				var accountUserId=document.getElementById("accountUserId").value=row.cells[0].innerText;
 	 			if(confirm("If you Click Ok, User Named <"+user_name+"> Account will be Updated")){
 	 	 	 		var ulr_="updateUserAccount?accountStatus="+accountStatus+"&userID="+accountUserId+"&privilege="+privilage+"&1404Id="+user1404Id;
 	 	 	 		xml_delete.open("POST",ulr_,true);
 	 	 	 		xml_delete.onload=function(){
 	 	 	 			var dataget=JSON.parse(xml_delete.responseText);
 	 	 	 		printUpdatedUserAccount(dataget);
 	 	 	 		}
 	 	 	 		xml_delete.send();
 					return true;
 					}
 				else{
 					return false;	
 				}
 				return false;	
	}
	function printUpdatedUserAccount(datafrom){
	var datadrop='';
	var activate='';
	var status=false;
	for(var i=0; i< datafrom.length; i++){
			if(datafrom[i].user_idUpdated!=0){
				if(datafrom[i].statusUpdated==='Active'){
					activate='InActivate';
					status=false;
				}else{
					activate='Activate';
					status=true;
				}
				 datadrop+='<tr><td data-Eco="Cat"><div class="long">' + datafrom[i].user_idUpdated + '</div></td>'+
				 '<td data-Eco="Clu"><div class="long">' + datafrom[i].fullNameUpdeated + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].userNameUpdated + '</div></td>'+
   		          '<td data-Eco="Reg"><div class="long">' + datafrom[i].userTypeUpdeted + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].statusUpdated + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long"><select id="privilageType" name="privilageType" class="formSelectedPrivilege">'
   		          +'<option value="4444">Select User Type</option>'
   		          +'<option value="123">Portal User</option>'
   		          +'<option value="120">Database Operator</option>'
   		          +'<option value="110">Administrator</option></select></div></td>'
   		          +'<td data-Eco="Yea"><div class="long"><label><input class="status" type="radio" class="selectedValue" value="'+status+'"> '+activate+'</label></div></td>'
   		          +'<td><a href="#" onclick="deleteOD(this)"><strong>Save</strong></a></td></tr>';			
			}
	}
var table = document.getElementById("manageAccount");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	holdUpdatedUserAccount.insertAdjacentHTML('beforeend',datadrop);       	
}
	
	
	
	
	
onclick=function(){prodata()};
	var xml_delete_proj=new XMLHttpRequest();
	var hold_response=document.getElementById("hold_response");
function prodata(){
		var table_project=document.getElementById('CoVrastar_id');
	 	for(var i=1;i < table_project.rows.length; i++){
	 				table_project.rows[i].onclick = function(){
	 					getvale1(this);	
	 					getUser_profile(this);
	 			};					
	 	}	
	};
 	function getvale1(row){
			row.cells[4].onclick=function(){	
			    var file_name=document.getElementById("hold_filename").value=row.cells[1].innerText;
 				if(confirm("If you Click Ok,  "+file_name+" Document Will be Granted")){
 					 var username=row.cells[2].innerText;
 					var des=document.getElementById("hold_res1").value;
 					var sup=document.getElementById("hold_res2").value;
 					var url1="grantdessup_pro?userN="+username+"&dse="+des+"&sup="+sup;
 		 	 	xml_delete_proj.open("POST",url1,true);
 		 	    xml_delete_proj.onload=function(){
 		 	    	var hold_respo=JSON.parse(xml_delete_proj.responseText);
 		 	  	      getreport(hold_respo);
 		 	 		}
 		 	    xml_delete_proj.send();
 					return true;
 					}
 				else{
 					return false;	
 				}
 				return false;		
		};	
	}
function getreport(datareport){
	var hold='';
	for(var i=0;i<datareport.length;i++){
	hold+='<li>'+datareport[i].respond_torequest+'</li>';	
	}
	document.getElementById("hold_response").innerHTML='';
	hold_response.insertAdjacentHTML('beforeend', hold)
}
var hold_user_profile=document.getElementById("hold_user_profile");
var holduser_xml=new XMLHttpRequest();
function getUser_profile(row) {
	row.cells[2].onclick=function(){
			var user_profile=row.cells[2].innerText;
			var url_user='userholder?email='+user_profile;
			holduser_xml.open('POST', url_user,true);
			holduser_xml.onload=function(){
				var data_user=JSON.parse(holduser_xml.responseText);
				userdetail(data_user);
			}
			holduser_xml.send();	
	}	
	}
function userdetail(detail){
	var data_container='';
	for(var i=0;i<detail.length;i++){
		data_container+='<tr><td style="width: 40%;">Employe Name:  </td><td style="width: 50%;">'+detail[i].emp_name+'</td></tr>'
		+'<tr><td style="width: 40%;">Center:  </td><td style="width: 50%;">'+detail[i].emp_center+'</td></tr>'
		+'<tr><td style="width: 40%;">Process:  </td><td style="width: 50%;">'+detail[i].emp_process+'</td></tr>';
	}
	document.getElementById("hold_user_profile").innerHTML='';
	hold_user_profile.insertAdjacentHTML('beforeend', data_container)
}
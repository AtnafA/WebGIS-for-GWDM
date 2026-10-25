/**
 * 
 */	
     var xmlHttp=new XMLHttpRequest();
     var xmlhttpdata=new XMLHttpRequest();
     var aReason=document.getElementById("aReason");
     var wellType=document.getElementById("wellType");
     var casingMaterId=document.getElementById("casingMaterId");
     var owendByID=document.getElementById("owendByID");
     var GHMetrology=document.getElementById("Inv_id");
     var geotech=document.getElementById("regionEn");
     var georegion=document.getElementById("fsource");
     document.getElementById("gtech_id").onclick = function(evt){
      		 if (typeof XMLHttpRequest != "undefined"){
      	         xmlHttp= new XMLHttpRequest();
      	         }
      	         else if (window.ActiveXObject){
      	         xmlHttp= new ActiveXObject("Microsoft.XMLHTTP");
      	         }
      	         if (xmlHttp==null){
      	         alert("Browser does not support XMLHTTP Request")
      	         return;
      	         }
      	var idfor_natio=document.getElementById("Na").value;
      	var urlt= "gWDBParam?Na="+idfor_natio;
      	xmlhttpdata.open("POST", urlt, true);
      	xmlhttpdata.onload = function () {
      	var pro_name= JSON.parse(xmlhttpdata.responseText);
      	     getGWdata(pro_name);
      	     getregionCity(pro_name);
      	     getWfield(pro_name);
      	     additionalWellfieldParam(pro_name);
      	     additionalWellOwnerParam(pro_name);
      	     additionalCasingMParam(pro_name)
      	     additionalWellTParam(pro_name);
      	     additionalAbanRParam(pro_name);
      	};
      	xmlhttpdata.send();		
     };	 
   function getGWdata(proname){
	var pro='';
	for(var i=0;i<proname.length;i++){
		if(proname[i].folderid!=0){
	pro+='<option value="' + proname[i].folderid + '">' + proname[i].raw_data_avialable + '</option>';	
		}
	}
	document.mform.fsource.options.length=0;	
	georegion.insertAdjacentHTML('beforeend',pro);	
   }  
   function getregionCity(proname){
		var pro='';
		for(var i=0;i<proname.length;i++){
			if(proname[i].regid!=0){
		pro+='<option value="' + proname[i].regid + '">' + proname[i].regnam + '</option>';	
			}
		}
		document.mform.regionEn.options.length=1;	
		geotech.insertAdjacentHTML('beforeend',pro);	
	   } 
   function getWfield(proname){
		var pro='';
			for(var i=0;i<proname.length;i++){
				if(proname[i].ptId!=0){
				pro+='<option value="' + proname[i].ptId + '">' + proname[i].ptName + '</option>';		
				}	
		}
		document.mform.Inv_id.options.length=1;	
		GHMetrology.insertAdjacentHTML('beforeend',pro);	
	   };  
   function additionalWellfieldParam(getheader){
	   var datadrop='';
	   for(var i=0; i< getheader.length; i++){
		   if(getheader[i].addwellFieldId!=0){
			datadrop+='<option value="' + getheader[i].addwellFieldId + '">' + getheader[i].addwellFieldName + '</option>';
			}}
			GHMetrology.insertAdjacentHTML('beforeend',datadrop);
			}
	function additionalWellOwnerParam(getheader){
	   var datadrop='';
	   for(var i=0; i< getheader.length; i++){
		   if(getheader[i].addwellOwnerId!=0){
			datadrop+='<option value="' + getheader[i].addwellOwnerName + '">' + getheader[i].addwellOwnerName + '</option>';
			}}
			document.mform.owendByID.options.length=5;
			owendByID.insertAdjacentHTML('beforeend',datadrop);
			}
	function additionalCasingMParam(getheader){
	   var datadrop='';
	   for(var i=0; i< getheader.length; i++){
		   if(getheader[i].addCasingMId!=0){
			datadrop+='<option value="' + getheader[i].addCasingMName + '">' + getheader[i].addCasingMName + '</option>';
			}}
			document.mform.casingMaterId.options.length=4;
			casingMaterId.insertAdjacentHTML('beforeend',datadrop);
			}
	function additionalWellTParam(getheader){
	   var datadrop='';
	   for(var i=0; i< getheader.length; i++){
		   if(getheader[i].addwellTId!=0){
			datadrop+='<option value="' + getheader[i].addwellTName + '">' + getheader[i].addwellTName + '</option>';
			}}
			document.mform.wellType.options.length=6;	
			wellType.insertAdjacentHTML('beforeend',datadrop);
			}
	function additionalAbanRParam(getheader){
	   var datadrop='';
	   for(var i=0; i< getheader.length; i++){
		   if(getheader[i].addAbanRId!=0){
			datadrop+='<option value="' + getheader[i].addAbanRName + '">' + getheader[i].addAbanRName + '</option>';
			}}
			document.mform.aReason.options.length=8;
			aReason.insertAdjacentHTML('beforeend',datadrop);
			}	
				        
  /* Access Data Investigated by Project Id*/
          var xml_getselectedGHM=new XMLHttpRequest();
       	document.getElementById("regionEn").onchange = function(ghm_event){
       		var id_regionCity=ghm_event.target.value;  
      
       		document.getElementById('Id_div').innerHTML='<span style="">Zone/ City:</span>';
       		document.getElementById('Id_div').style.display="block";
       		document.getElementById("regionCityID").style.display="block";
       		 
 if (typeof XMLHttpRequest != "undefined"){
      	         xml_getselectedGHM= new XMLHttpRequest();
      	         }
      	         else if (window.ActiveXObject){
      	         xml_getselectedGHM= new ActiveXObject("Microsoft.XMLHTTP");
      	         }
      	         if (xml_getselectedGHM==null){
      	         alert("Browser does not support XMLHTTP Request")
      	         return;
      	         }
   	var urlt= "regionCity?regionC="+id_regionCity;
   	xml_getselectedGHM.open("POST", urlt, true);
   	xml_getselectedGHM.onload = function () {
   	var pro_name= JSON.parse(xml_getselectedGHM.responseText);
   	get_ptName(pro_name);
   	};
   	xml_getselectedGHM.send();	
  } 
    var ptNameN=document.getElementById("regionCityID");
    function get_ptName(ptname){
	var pro='';
	for(var i=0;i<ptname.length;i++){
		if(ptname[i].regClassId!=0){
		pro+='<option value="' + ptname[i].regClassId + '">' + ptname[i].gerClassName + '</option>';		
		}
	}
	document.mform.regionCityID.options.length=1;
    ptNameN.insertAdjacentHTML('beforeend',pro);
   } 
   //Sub city Zone dropdown
       		var subCityZone=document.getElementById("subcityzoneId");
       		var subCityZoneXml= new XMLHttpRequest();
       		 document.getElementById("regionCityID").onchange=function(evnt){
			var regionCityValue=evnt.target.value;
			document.getElementById('pr_type').innerHTML='<span style="">Sub City/ Oromia zone:</span>';
       		document.getElementById('pr_type').style.display="block";
       		document.getElementById('subcityzoneId').style.display="block";
    		   if (typeof subCityZoneXml!= "undefined"){
           			subCityZoneXml= new XMLHttpRequest();
           	         }
           	         else if (window.ActiveXObject){
           	        	subCityZoneXml= new ActiveXObject("Microsoft.XMLHTTP");
           	         }
           	         if (subCityZoneXml==null){
           	         alert("Browser does not support XMLHTTP Request")
           	         return;
           	         }
    		      	var urlt="subcityZone?cityZone_id="+regionCityValue;
    		      	subCityZoneXml.open("POST", urlt, true);
    		      	subCityZoneXml.onload=function(){
    		  	        var sczdata=JSON.parse(subCityZoneXml.responseText);
    		  	      subCityZoneDropdoun(sczdata);
    		  	        	 }
    		      	subCityZoneXml.send();
    		    };
    	   function subCityZoneDropdoun(pOtput_data){
			   var pro_O='';
			   for(var i=0;i<pOtput_data.length;i++){
				if(pOtput_data[i].scz_Id!=0){
		pro_O+='<option value="' + pOtput_data[i].scz_Id + '">' + pOtput_data[i].scz_name + '</option>';
		}      
			   }
			document.mform.subcityzoneId.options.length=1;	
			subCityZone.insertAdjacentHTML('beforeend',pro_O);  
		   }
		document.getElementById("subcityzoneId").onchange=function(evnt){
       	document.getElementById('div1').style.display="table";
		   }       
		 	  	
 	/*Access Well ComplarionReport Data*/
 	var access_del_modefy=document.getElementById("accessby_userId");
 	var xmldelmod_access=new XMLHttpRequest();
 	document.getElementById("mange_rawdata").onclick = function(evt){
 		if (typeof xmldelmod_access != "undefined"){
 			xmldelmod_access= new XMLHttpRequest();
 	         }
 	         else if (window.ActiveXObject){
 	        	xmldelmod_access= new ActiveXObject("Microsoft.XMLHTTP");
 	         }
 	         if (xmldelmod_access==null){
 	         alert("Browser does not support XMLHTTP Request")
 	         return;
 	         }
 	         var access_user_id=document.getElementById("user_idWCR").value;
 	         var user_prvg=document.getElementById('user_prvg').value;
 	         var url="access_mod_del?access_id="+access_user_id+"&user_prvg="+user_prvg;
 	        xmldelmod_access.open("POST", url,true);
 	       xmldelmod_access.onload=function(){
 	        var prodata=JSON.parse(xmldelmod_access.responseText);
 	       access_dele_modefy(prodata);
 	        	 }
 	      xmldelmod_access.send();
 	         }
 	//Access by well Index
var XMLRequestByWellIndex=new XMLHttpRequest();
 document.getElementById('DPTHDwellIndex').onchange=function(element){
	 var wellIndexName=element.target.value
          //alert(nonc_id); 
	 if (typeof XMLRequestByWellIndex != "undefined"){
		 XMLRequestByWellIndex= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   XMLRequestByWellIndex = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (XMLRequestByWellIndex == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
    var access_user_id=document.getElementById("user_idWCR").value;
    var userType=document.getElementById("userTypeWCR").value;
    if(access_user_id!==""){
	var url1="accessByWellIndex?wellIndex="+wellIndexName+"&userId="+access_user_id+"&userTypeWCR="+userType;
	XMLRequestByWellIndex.open("POST", url1, true);
	XMLRequestByWellIndex.onload= function (){
	var wellProfile = JSON.parse(XMLRequestByWellIndex.responseText);	
	wellProfileByWellIndex(wellProfile);}
	XMLRequestByWellIndex.send();
	}
}
 	function access_dele_modefy(datafrom){
 		var ret='';
 	for(var i=0; i<datafrom.length; i++){
    	if(datafrom[i].well_ID!==0){
    		ret+='<tr><td data-Eco="Spa" style="display:none;"><div class="long">'+datafrom[i].well_ID+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionNameUpdate + '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellFieldUpdate + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroupUpdate + '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerNameUpdate + '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndexUpdate+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCodeUpdate+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].constractYearUpdate + '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatusUpdate+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepthUpdate+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellYieldUpdate+ '</div></td>'+
 		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCaseUpdate + '</div></td>'+
 		          '<td><a href="#" onclick="updateCWR(this)"><strong>Update</strong></a></td>'+
 		          '<td><a href="#" onclick="deleteCWR(this)"><strong>Delete</strong></a></td>'+
   		          '</tr>';
   		}
 	}
 	var table = document.getElementById("accessby_userId");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	access_del_modefy.insertAdjacentHTML('beforeend',ret);
 		}
 		var accessbyWellIndex=document.getElementById("accessby_userId");
 	 	function wellProfileByWellIndex(datafrom){
 		var ret='';
 	for(var i=0; i<datafrom.length; i++){
    		ret+='<tr><td data-Eco="Spa" style="display:none"><div class="long">'+datafrom[i].well_IDsearch4M+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionName+ '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellField+ '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroup+ '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerName+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndex+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCode+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].constractYear + '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatus+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepth+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellYieldM+ '</div></td>'+
 		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCase + '</div></td>'+
 		          '<td><a href="#" onclick="updateCWR(this)"><strong>Update</strong></a></td>'+
 		          '<td><a href="#" onclick="deleteCWR(this)"><strong>Delete</strong></a></td>'+
   		          '</tr>';
 	}
 	var table = document.getElementById("accessby_userId");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	accessbyWellIndex.insertAdjacentHTML('beforeend',ret);
 		}
 		//Update DPTH Data
 		var xmlaccessWCRforUpdate=new XMLHttpRequest();
  		function updateCWR(buttonHit){
			  if(typeof xmlaccessWCRforUpdate != "undefined"){
				  xmlaccessWCRforUpdate=new XMLHttpRequest();
			  }else if(window.ActiveXObject){
				  xmlaccessWCRforUpdate= new ActiveXObject("Microsoft.XMLHTTP");
			  }
			  if(xmlaccessWCRforUpdate==null){
				alert("Browser does not support XMLHTTP Request");
				return;
			  }
			 const row = buttonHit.parentNode.parentNode;
			 const firstCell = row.cells[0];
			 var diveValue=document.getElementById('wellIDforUpdate').value=firstCell.innerText;
			// console.log("Value is ",diveValue);
			 var paratoSend="accessDatatoUpdate?well_id="+diveValue;
			 xmlaccessWCRforUpdate.open("POST",paratoSend,true);
			 xmlaccessWCRforUpdate.onload=function(){
				 var dataAccessed=JSON.parse(xmlaccessWCRforUpdate.responseText);
				 displayWCRContent(dataAccessed);
			 }
			 xmlaccessWCRforUpdate.send();
			 }
var updateContent = document.getElementById("updateContent");

function displayWCRContent(diveValue) {
    var ret = '';
    
    // Global container layout override styling rules to lock sizing into a single window view
    var wrapper = document.getElementById("displayUpdateContent");
    if (wrapper) {
        wrapper.style.maxHeight = "92vh";
        wrapper.style.overflowY = "auto";
        wrapper.style.padding = "10px";
    }

    for (let i = 0; i < diveValue.length; i++) {
        if (diveValue[i].well_idUpdate > 0) {
            
            ret += 
            // ─── UNIQUE ID BOUNDED RECORD BLOCK WRAPPER ───
            '<div class="compact-well-row" style="background:#fff; border:1px solid #cbd5e1; border-radius:6px; padding:10px; margin-bottom:12px; font-family:\'Segoe UI\',system-ui,sans-serif; font-size:12px;">' +
                
                // Minimalist Top Control Ribbon Section Bar 
                '<div style="display:flex; justify-content:space-between; background:#06402B; color:#fff; padding:3px 8px; border-radius:4px; font-weight:600; font-size:11px; margin-bottom:8px;">' +
                    '<span>Borehole Index: ' + diveValue[i].wellIndexUpdate + '</span>' +
                    '<span>Record Tracker Node #' + (i + 1) + '</span>' +
                '</div>' +

                // Hidden primary database key buffers (safely bounded with distinct tracking loop array markers)
                '<input type="hidden" id="CROwnerIdUId_' + i + '" name="CROwnerIdUName" value="' + diveValue[i].ownerID + '"/>' +
                '<input type="hidden" id="CRGeolocUId_' + i + '" name="CRGeolocUName" value="' + diveValue[i].geolocID + '"/>' +
                '<input type="hidden" id="CRwellIdUId_' + i + '" name="CRwellIdUName" value="' + diveValue[i].well_idUpdate + '"/>' +

                // ULTRA-DENSE 4-COLUMN PARAMETER FORM MATRIX
                '<table style="width:100%; border-collapse:separate; border-spacing:6px 4px; table-layout:fixed;">' +
                    '<tr>' +
                        '<td style="width:13%; font-weight:600; color:#475569;">Well Index:</td>' +
                        '<td style="width:20%;"><input type="text" id="CRwellIndexUId_' + i + '" name="CRwellIndexUName" value="' + diveValue[i].wellIndexUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; background:#e2e8f0; border:1px solid #cbd5e1; border-radius:3px;" readonly/></td>' +
                        '<td style="width:13%; font-weight:600; color:#475569;">Main Aquifer:</td>' +
                        '<td style="width:20%;"><input type="text" id="CRmainaquiferUId_' + i + '" name="CRmainaquiferName" value="' + diveValue[i].mainAquiferUp + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="width:13%; font-weight:600; color:#475569;">Well Yield:</td>' +
                        '<td style="width:21%;"><input type="text" id="CRwellYieldUId_' + i + '" name="CRwellYieldUName" value="' + diveValue[i].wellYieldUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Well Code:</td>' +
                        '<td><input type="text" id="CRwellCodeUId_' + i + '" name="CRwellCodeUName" value="' + diveValue[i].wellCodeUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Well Type:</td>' +
                        '<td><input type="text" id="CRwellTypeId_' + i + '" name="CRwellTypeName" value="' + diveValue[i].wellType + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Static Level:</td>' +
                        '<td><input type="text" id="CRSWLUId_' + i + '" name="CRSWLUName" value="' + diveValue[i].swlUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Easting:</td>' +
                        '<td><input type="text" id="CREastingUId_' + i + '" name="CRweEastingUName" value="' + diveValue[i].easting + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Aban. Reason:</td>' +
                        '<td><input type="text" id="CRabanWellRId_' + i + '" name="CRabanRWellName" value="' + diveValue[i].abandonedReason + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Dynamic Level:</td>' +
                        '<td><input type="text" id="CRDWLUId_' + i + '" name="CRDWLUName" value="' + diveValue[i].dwlUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Northing:</td>' +
                        '<td><input type="text" id="CRNorthingUId_' + i + '" name="CRNorthingUName" value="' + diveValue[i].northing + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Sealed (Y/N):</td>' +
                        '<td><input type="text" id="CRSealedYNId_' + i + '" name="CRsealedYNName" value="' + diveValue[i].sealedYN + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Specific Cap:</td>' +
                        '<td><input type="text" id="CRSpecificCUId_' + i + '" name="CRSpecificCUName" value="' + diveValue[i].specificCapacityUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Longitude:</td>' +
                        '<td><input type="text" id="CRLongTUId_' + i + '" name="CRLongTUName" value="' + diveValue[i].longT + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Sealed Date:</td>' +
                        '<td><input type="text" id="CRSealedDateId_' + i + '" name="CRSealedDateName" value="' + diveValue[i].dateSealed + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Large Casing:</td>' +
                        '<td><input type="text" id="CRlargeCasingID_' + i + '" name="CRlargeCasingName" value="' + diveValue[i].largCasing + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Latitude:</td>' +
                        '<td><input type="text" id="CRLatId_' + i + '" name="CRLatUName" value="' + diveValue[i].lat + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Pump Status:</td>' +
                        '<td><input type="text" id="CRpumpStatusID_' + i + '" name="CRpumpstatusName" value="' + diveValue[i].pumpStatusUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Tele Casing:</td>' +
                        '<td><input type="text" id="CRtelCasingId_' + i + '" name="CRtelCasingName" value="' + diveValue[i].telescopCasing + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Elevation:</td>' +
                        '<td><input type="text" id="CRElevationUId_' + i + '" name="CRElevationUName" value="' + diveValue[i].elivation + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Pump Inst Date:</td>' +
                        '<td><input type="text" id="CRinstalledDateID_' + i + '" name="CRpumpInstalledDateName" value="' + diveValue[i].pumpInstalledDate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Casing Mtrl:</td>' +
                        '<td><input type="text" id="CRCasingMaterId_' + i + '" name="CRCasingMaterName" value="' + diveValue[i].casingMaterial + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Owner Category:</td>' +
                                            '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Owner Category:</td>' +
                        '<td><input type="text" id="CRSOwnerCatId_' + i + '" name="CRSOwnerCatName" value="' + diveValue[i].ownerCat + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Pump Cap (KW):</td>' +
                        '<td><input type="text" id="CRpumpCapacityID_' + i + '" name="CRpumpCapacityName" value="' + diveValue[i].pumpCapacity + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">SCADA Conn:</td>' +
                        '<td>' +
                            '<select id="scadaID_' + i + '" name="SCADAName" style="width:100%; height:22px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;">' +
                                '<option value="Available" ' + (diveValue[i].scadaConnection === 'Available' ? 'selected' : '') + '>Available</option>' +
                                '<option value="Not Avialable" ' + (diveValue[i].scadaConnection === 'Not Avialable' ? 'selected' : '') + '>Not Available</option>' +
                            '/select>' +
                        '</td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Owner Org:</td>' +
                        '<td><input type="text" id="CROwnerCUId_' + i + '" name="CROwnerCUName" value="' + diveValue[i].userCompany + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Pump Head (m):</td>' +
                        '<td><input type="text" id="CRpumpHeadID_' + i + '" name="CRpumpHeadName" value="' + diveValue[i].pumpheadUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">SCADA Oper:</td>' +
                        '<td>' +
                            '<select id="SCADACStatusID_' + i + '" name="SCADACStatusName" style="width:100%; height:22px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;">' +
                                '<option value="Functional" ' + (diveValue[i].scadaStatus === 'Functional' ? 'selected' : '') + '>Functional</option>' +
                                '<option value="Partially Functional" ' + (diveValue[i].scadaStatus === 'Partially Functional' ? 'selected' : '') + '>Partially Functional</option>' +
                                '<option value="Non Functional" ' + (diveValue[i].scadaStatus === 'Non Functional' ? 'selected' : '') + '>Non Functional</option>' +
                            '</select>' +
                        '</td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Owner Email:</td>' +
                        '<td><input type="text" id="CREmailUId_' + i + '" name="CREmailUName" value="' + diveValue[i].functionCondition + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Pump Position:</td>' +
                        '<td><input type="text" id="CRpumpPosID_' + i + '" name="CRpumpPosiName" value="' + diveValue[i].pumpPositionUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td colspan="2" rowspan="4" style="vertical-align:bottom; text-align:right;"></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Owner Phone:</td>' +
                        '<td><input type="text" id="CRPhoneUId_' + i + '" name="CRPhoneUName" value="' + diveValue[i].pumpStatus + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Discharge Rate:</td>' +
                        '<td><input type="text" id="CRdischargeRateID_' + i + '" name="CRdischargeRateName" value="' + diveValue[i].dischargeRate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Const. Date:</td>' +
                        '<td><input type="text" id="CRConstYUId_' + i + '" name="CRConstYUName" value="' + diveValue[i].constYearUpdate + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Generator Stat:</td>' +
                        '<td><input type="text" id="CRgeneratorstaID_' + i + '" name="CRgeneratorStaName" value="' + diveValue[i].gen_status + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                    '<tr>' +
                        '<td style="font-weight:600; color:#475569;">Drilling Permit:</td>' +
                        '<td><input type="text" id="CRDrillingLUId_' + i + '" name="CRDrillingLUName" value="' + diveValue[i].drillingLicense + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                        '<td style="font-weight:600; color:#475569;">Generator Cap:</td>' +
                        '<td><input type="text" id="CRgeneratorCapaID_' + i + '" name="CRcapacityCapaName" value="' + diveValue[i].gen_power + '" style="width:100%; height:20px; font-size:11px; padding:2px; border:1px solid #cbd5e1; border-radius:3px;"/></td>' +
                    '</tr>' +
                '</table>' +
                '<div id="errorHandling_' + i + '" style="color:#b91c1c; font-size:11px; margin-top:4px;"></div>' +
            '</div>';
        }
    }
    document.getElementById("displaywellCRD").style.display = 'none';
    document.getElementById("displayUpdateContent").style.display = "block";
    updateContent.innerHTML = '';
    updateContent.insertAdjacentHTML('beforeend', ret);
}

	//var access_del_respond=document.getElementById("accessby_userId");
	document.getElementById("back_to_table").onclick=function(){
		document.getElementById("displayUpdateContent").style.display='none';
		document.getElementById("displaywellCRD").style.display='block';
		}
			 
  	//delete well Completion Report
    var access_del_respond=document.getElementById("accessby_userId");
 	var xml_deleteCR=new XMLHttpRequest();
 	function deleteCWR(buttonClicked){
			const row=buttonClicked.parentNode.parentNode;
				var file_name=document.getElementById("hold_wellIndex").value=row.cells[5].innerText;
				var ORwell_id=document.getElementById("ORwell_id").value=row.cells[0].innerText;
 	 			if(confirm("If you Click Ok, "+file_name+" Well will be Deleted")){
 	 	 	 		//alert(this.rowIndex);
 	 	 			var access_user_id=document.getElementById("user_idWCR").value;
 	 	 	 		//alert(file_name);
 	 	 	 		var ulr_="delete_WOfile?well_id="+ORwell_id+"&user_id="+access_user_id;
 	 	 	 		xml_deleteCR.open("POST",ulr_,true);
 	 	 	 		xml_deleteCR.onload=function(){
 	 	 	 			var dataget=JSON.parse(xml_deleteCR.responseText);
 	 	 	 		updateWCRTableafterDelete(dataget);
 	 	 	 		}
 	 	 	 		xml_deleteCR.send();
 					return true;
 					}
 				else{
 					return false;	
 				}
 				return false;	
	}
	function updateWCRTableafterDelete(datafrom){
 		var ret='';
 		for(var i=0; i<datafrom.length; i++){
			 ret+='<tr><td data-Eco="Spa" style="display:none;"><div class="long">'+datafrom[i].well_IDsearch4M+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionName + '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellField + '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroup + '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerName + '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndex+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCode+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].constractYear + '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatus+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepth+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellYieldM+ '</div></td>'+
 		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCase + '</div></td>'+
 		          '<td><a href="#">Update</a></td>'+
 		          '<td><a href="#">Delete</a></td>'+
   		          '</tr>';
 	}
 	var table = document.getElementById("accessby_userId");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	access_del_respond.insertAdjacentHTML('beforeend',ret);
 		}
 /*Access Operational Report*/
 	var access_gw_data=document.getElementById("access_OpData");
 	var xmldelmod_access_GWDtata=new XMLHttpRequest();
 	document.getElementById("mange_otherdoc").onclick = function(evt){
		         	//var spa_id=evt.target.value;
		            //alert("clicked "+spa_id);
 		if (typeof xmldelmod_access_GWDtata != "undefined"){
 			xmldelmod_access_GWDtata= new XMLHttpRequest();
 	         }
 	         else if (window.ActiveXObject){
 	        	xmldelmod_access_GWDtata= new ActiveXObject("Microsoft.XMLHTTP");
 	         }
 	         if (xmldelmod_access_GWDtata==null){
 	         alert("Browser does not support XMLHTTP Request")
 	         return;
 	         }
 	         var access_user_id=document.getElementById("user_idOR").value;
 	         var userRole=document.getElementById("user_Type").value;
 	         var url="access_mod_gwData?access_id="+access_user_id+"&userRole="+userRole;
 	        xmldelmod_access_GWDtata.open("POST", url,true);
 	       xmldelmod_access_GWDtata.onload=function(){
 	        var prodata=JSON.parse(xmldelmod_access_GWDtata.responseText);
 	       access_dele_gwData(prodata);
 	        	 }
 	      xmldelmod_access_GWDtata.send();
 	         }
 	//Access by well Index
var XMLRequestGWByWellIndex=new XMLHttpRequest();
 document.getElementById('DOWDWellIndex').onchange=function(element){
	 var wellIndexName=element.target.value
          //alert(nonc_id); 
	 if (typeof XMLRequestGWByWellIndex != "undefined"){
		 XMLRequestGWByWellIndex= new XMLHttpRequest();
       }
       else if (window.ActiveXObject){
    	   XMLRequestGWByWellIndex = new ActiveXObject("Microsoft.XMLHTTP");
       }
       if (XMLRequestGWByWellIndex == null){
       alert("Browser does not support XMLHTTP Request")
       return;
       }
    var access_user_id=document.getElementById("user_idOR").value;
    var user_Type=document.getElementById("user_Type").value;
    if(access_user_id!==""){
	var url1="accessGWByWellIndex?wellIndex="+wellIndexName+"&userId="+access_user_id+"&userType="+user_Type;
	XMLRequestGWByWellIndex.open("POST", url1, true);
	XMLRequestGWByWellIndex.onload= function (){
	var wellProfile = JSON.parse(XMLRequestGWByWellIndex.responseText);	
	GWdataByWellIndex(wellProfile);}
	XMLRequestGWByWellIndex.send();
	}
}
 	function access_dele_gwData(datafrom){
 		var ret='';
 	for(var i=0; i<datafrom.length; i++){
    	if(datafrom[i].well_IDsearch4MOp!==0){
    		ret+='<tr><td data-Eco="Spa" style="display:none"><div class="long">'+datafrom[i].well_IDsearch4MOp+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionName4MOp+ '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellField4MOp+ '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroup4MOp+ '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerName4MOp+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndex4MOp+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCode4MOp+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatus4MOp+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepth4Mop+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCase4MOp + '</div></td>'+
 		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCondition4MOp+'</div></td>'+
 		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].lastUpdatedDate4MOp  + '</div></td>'+
 		          '<td><a href="#" onclick="updateOD(this)"><strong>Update</strong></a></td>'+
 		          '<td><a href="#" onclick="deleteOD(this)"><strong>Delete</strong></a></td>'+
   		          '</tr>';
   		}
 	}
 	var table = document.getElementById("access_OpData");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	access_gw_data.insertAdjacentHTML('beforeend',ret);
 		}
 			var accessbyGWDataWellIndex=document.getElementById("access_OpData");
 	 	function GWdataByWellIndex(datafrom){
 		var ret='';
 	for(var i=0; i<datafrom.length; i++){
    		ret+='<tr><td data-Eco="Spa" style="display:none"><div class="long">'+datafrom[i].well_IDGW+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionNameGW+ '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellFieldGW+ '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroupGW+ '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerNameGW+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndexGW+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCodeGW+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatusGW+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepthGW+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCaseGW + '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].dataCondition+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].lastUpdateGW+ '</div></td>'+
 		          '<td><a href="#" onclick="updateOD(this)"><strong>Update</strong></a></td>'+
 		          '<td><a href="#" onclick="deleteOD(this)"><strong>Delete</strong></a></td>'+
   		          '</tr>';
 	}
 	var table = document.getElementById("access_OpData");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	accessbyGWDataWellIndex.insertAdjacentHTML('beforeend',ret);
 		}
 		//Update Groundwater Operational Data
 		var xmlAccessGWODforUpdate=new XMLHttpRequest();				
 		function updateOD(buttonUpdate){
			 if(typeof xmlAccessGWODforUpdate!=="undefined"){
				 xmlAccessGWODforUpdate=new XMLHttpRequest(); 
			 }
			 else if(window.ActiveXObject){
				 xmlAccessGWODforUpdate = new ActiveXObject("Microsoft.XMLHTTP");	 
			 }
			 if(xmlAccessGWODforUpdate==null){
				 alert("Browser does not support XMLHTTP Request");
				 return;
			 } 
			var row = buttonUpdate.parentNode.parentNode;
			const cellText=row.cells[0].innerText
			var diveValue=document.getElementById('wellIDforOGWCUpdate').value=cellText;
			uri="accessGWODforUpdate?well_id="+diveValue;
			xmlAccessGWODforUpdate.open("POST",uri,true);
			xmlAccessGWODforUpdate.onload=function(){
				dataAccessed=JSON.parse(xmlAccessGWODforUpdate.responseText);
				displayOGWDContent(dataAccessed);
			}
			 xmlAccessGWODforUpdate.send(); 
		 }
		 function displayOGWDContent(diveValue){
			 document.getElementById("displayOGWUpdateContent").style.display="block";
			 var updateOGWContent=document.getElementById("updateOGWContent");
			 var ret='';
			 for(let i=0;i<diveValue.length;i++){
				 if(diveValue[i].well_idforODUpdate!==0){
    		ret+='<tr style="display:none"><td>enumID: </td><td><input type="text" id="enumID" name="enumIdName" value="'+diveValue[i].enumID+'"/></td></tr>'+
    		      '<tr style="display:none"><td>Well Code: </td><td><input type="text" id="wellID" name="wellIdName" value="'+diveValue[i].well_idforODUpdate+'"/></td></tr>'+
   		          '<tr><td>Well Index: </td><td><input type="text" id="wellIndexID" name="wellIndexName" value="'+diveValue[i].wellIndexforODUpdate+'" readonly/></td></tr>'+
   		          '<tr><td>Water Usege License: </td><td><input type="text" id="waterUseLID" name="waterUserLName" value="'+diveValue[i].waterUseLicense+'"/></td></tr>'+
   		          '<tr><td>Operation Start Date: </td><td><input type="text" id="opStartDID" name="opStartDName" value="'+diveValue[i].oprerationStartDate+'"/></td></tr>'+
   		          '<tr><td>Enumerator Company Name: </td><td><input type="text" id="enumCompanyID" name="enumCompanyName" value="'+diveValue[i].enumCompanyName+'"/></td></tr>'+
   		          '<tr><td>Enumrator Email: </td><td><input type="text" id="enumEmailID" name="enumEmailName" value="'+diveValue[i].enumEmail+'"/></td></tr>'+
   		          '<tr><td>Enumrator Phone: </td><td><input type="text" id="enumEmailPhoneID" name="enumEmailPhoneName" value="'+diveValue[i].enumPhone+'"/></td></tr>';
			 }}
   		          document.getElementById("displaywellOGWD").style.display='none';
   		          document.getElementById("updateOGWContent").innerHTML = '';
   		          updateOGWContent.insertAdjacentHTML('beforeend',ret);
		 }
		 document.getElementById("back_to_OGDtable").onclick=function(){
			 document.getElementById("displayOGWUpdateContent").style.display='none';
			 document.getElementById("displaywellOGWD").style.display='block';
			 }
		 
 		//delete well Operational Data
 	var access_del_respondOP=document.getElementById("access_OpData");
 	var xml_delete=new XMLHttpRequest();
 	function deleteOD(buttonDelete){
			var row=buttonDelete.parentNode.parentNode;	
				var file_name=document.getElementById("hold_wellIndexOP").value=row.cells[5].innerText;
				var hold_wellIdOR=document.getElementById("hold_wellIdOR").value=row.cells[0].innerText;
 	 			if(confirm("If you Click Ok, "+file_name+" Well will be Deleted")){
 	 	 	 		//alert(this.rowIndex);
 	 	 			var access_user_id=document.getElementById("user_idOR").value;
 	 	 	 		
 	 	 	 		//alert(file_name);
 	 	 	 		var ulr_="delete_ODfile?well_id="+hold_wellIdOR+"&user_id="+access_user_id;
 	 	 	 		xml_delete.open("POST",ulr_,true);
 	 	 	 		xml_delete.onload=function(){
 	 	 	 			var dataget=JSON.parse(xml_delete.responseText);
 	 	 	 		updateODTableafterDeletion(dataget);
 	 	 	 		}
 	 	 	 		xml_delete.send();
 					return true;
 					}
 				else{
 					return false;	
 				}
 				return false;	
	}
	 	function updateODTableafterDeletion(datafrom){
 		var ret='';
 	for(var i=0; i<datafrom.length; i++){
    		ret+='<tr><td data-Eco="Spa" style="display:none"><div class="long">'+datafrom[i].well_IDsearch4MOp+'</div></td>'+
    		      '<td data-Eco="Spa"><div class="long">' + datafrom[i].regionName+ '</div></td>'+
   		          '<td data-Eco="Cat"><div class="long">' + datafrom[i].wellField+ '</div></td>'+
   		          '<td data-Eco="Clu"><div class="long">' + datafrom[i].ownerGroup+ '</div></td>'+
   		          '<td data-Eco="Yea"><div class="long">' + datafrom[i].OunerName+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellIndex+ '</div></td>'+
   		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].wellCode+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellStatus+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].wellDepth+ '</div></td>'+
   		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCollectCase + '</div></td>'+
 		          '<td data-Eco="Fil"><div class="long">' + datafrom[i].dataCondition+'</div></td>'+
 		          '<td data-Eco="Bas"><div class="long">' + datafrom[i].lastUpdatedDate  + '</div></td>'+
 		          '<td><a href="#" onclick="updateOD(this)"><strong>Update</strong></a></td>'+
 		          '<td><a href="#" onclick="deleteOD(this)"><strong>Delete</strong></a></td>'+
   		          '</tr>';
 	}
 	var table = document.getElementById("access_OpData");
	for (var i = table.rows.length; i>0 ; i--) {   //iterate through rows
	  if(i>1) {
	    row = table.rows[i-1];
	    row.parentNode.removeChild(row);
	  }
	}
	access_del_respondOP.insertAdjacentHTML('beforeend',ret);
 		}
 		
 /*Add New Drop Down Paramaters*/
 	var hold_response=document.getElementById("addParamresponse");
 	var xml_forgrantresponse=new XMLHttpRequest();
 	document.getElementById("addparam_button").onclick = function(evt){
		// alert("Submit Button is clicked");
		 //console.log("What is Wrong");
 		if (typeof xml_forgrantresponse != "undefined"){
 			xml_forgrantresponse= new XMLHttpRequest();
 	         }
 	         else if (window.ActiveXObject){
 	        	xml_forgrantresponse= new ActiveXObject("Microsoft.XMLHTTP");
 	         }
 	         if (xml_forgrantresponse==null){
 	         alert("Browser does not support XMLHTTP Request")
 	         return;
 	         }
 	         var additionalDataID=document.getElementById("additionalDataID").value;	
 	        var addedUserID=document.getElementById("addedUserID").value;
 	        var addtionalParamid=document.getElementById("addtionalParamid").value;
 	        var selectedCZID=document.getElementById("selectedCZID").value;
 	        const errorMessageDiv = document.getElementById('errorMessage');
            errorMessageDiv.textContent = ''; // Clear previous error messages
            document.getElementById("addParamresponse").innerHTML='';
         if (addtionalParamid === '0') {
             errorMessageDiv.textContent = 'Select from Attribute Name List';
             errorMessageDiv.style.color='red';
         }
         else if (selectedCZID === '0' && addtionalParamid === 'City_OroZone') {
             errorMessageDiv.textContent = 'Select From City Or Oromia Zone Name List';
             errorMessageDiv.style.color='red';
         }
         else if (additionalDataID.trim() === '') {
             errorMessageDiv.textContent = 'Add New Attribute Name';
             errorMessageDiv.style.color='red';
         }else{
 	        var url="addParameter?data_id="+additionalDataID+"&user_id="+addedUserID+"&contentID="+addtionalParamid+"&cityZID="+selectedCZID;
 	        xml_forgrantresponse.open("POST", url,true);
 	        xml_forgrantresponse.onload=function(){
 	        var prodata=JSON.parse(xml_forgrantresponse.responseText);
 	        get_reponse(prodata);
 	        	 }
 	        xml_forgrantresponse.send(); 
           }
 	       }
 	function get_reponse(datap){
 	var ret='';	
 	for(var i=0; i<datap.length; i++){
 		ret+='<p>' + datap[i].newlyAddeddParam + '</p>';
 	}
 	document.getElementById("addParamresponse").innerHTML='';	
 	hold_response.insertAdjacentHTML('beforeend',ret);
 		}
/**
 * Asynchronously fetches and parses all active pending database permission requests.
 */
function loadOperatorRequestQueue() {
    var tableBody = document.querySelector("#OperatorRequestTable tbody");
    if (!tableBody) return;

    if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'block';

    fetch("FetchPendingRequests", { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(requests) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            
            var rowsHtml = '';
            if (!requests || requests.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:8px; color:#64748b; font-style:italic; '
                +'background:#f8fafc;">✨ Clear Queue: No pending file access requests found.</td></tr>';
                return;
            }

            for (var i = 0; i < requests.length; i++) {
                var req = requests[i];
                var indexNo = i + 1;

                rowsHtml += '<tr style="border-bottom: 1px solid #f1f5f9;">' +
                    '<td style="padding:8px; font-weight:600; color:#64748b; text-align:center;">' + indexNo + '</td>' +
                    '<td style="padding:8px; font-weight:600; color:#0f172a;">' + req.requestedUserName + '</td>' + // mapped username
                    '<td style="padding:8px; color:#475569;">' + req.orgName + '</td>' + // mapped orgname
                    '<td style="padding:8px; font-family:monospace; color:#334155;">' + req.fileNameResponse + '</td>' + // mapped filename
                    '<td style="padding:8px;"><span style="background:#e0f2fe; color:#0369a1; padding:2px 6px; border-radius:4px; '
                    +'font-size:11px; font-weight:600;">' + req.dataCategoryResponse + '</span></td>' +
                    
                    // 🫵 ACTION DECISION BUTTON BUTTONS SCRIPT GRID
                    '<td style="text-align:center; padding:6px 0; display:flex; justify-content:center; gap:8px;">' +
                        '<button type="button" onclick="processOperatorDecision(' + req.requestId + ', 2)" '
                        +'style="background:#22c55e; color:#fff; border:none; padding:4px 10px; border-radius:4px; font-size:11px; '
                        +'font-weight:bold; cursor:pointer; display:inline-flex; align-items:center; gap:4px;"><i class="fa fa-check"></i> '
                        +'Grant</button>' +
                        '<button type="button" onclick="processOperatorDecision(' + req.requestId + ', 3)" style="background:#ef4444; '
                        +'color:#fff; border:none; padding:4px 8px; border-radius:4px; font-size:11px; font-weight:bold; cursor:pointer; '
                        +'display:inline-flex; align-items:center; gap:4px;"><i class="fa fa-times"></i> Decline</button>' +
                    '</td>' +
                '</tr>';
            }
            tableBody.innerHTML = rowsHtml;
        })
        .catch(function(error) {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            console.error("Failed to load requests queue:", error);
        });
}

/**
 * Transmits the operator decision to Approve or Decline an access token.
 */
function processOperatorDecision(requestId, targetStatus) {
    var actionText = (targetStatus === 2) ? "GRANT DOWNLOAD ACCESS" : "DECLINE ACCESS";
    if (!confirm("Are you sure you want to securely " + actionText + " for this pending request?")) return;

    if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'block';

    var params = new URLSearchParams();
    params.append("request_id", requestId);
    params.append("action_status", targetStatus);

    fetch("ProcessRequestAction", {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(function(response) { return response.json(); })
    .then(function(success) {
        if (success) {
            alert("✔ Action Registered: Request pipeline authorization status updated successfully.");
            loadOperatorRequestQueue(); // Dynamically reload queue panel on the fly!
        } else {
            alert("Error: Database rejected transaction token update pass.");
        }
        if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
    })
    .catch(function(error) {
        if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
        console.error("Decision routing crash:", error);
    });
}

// Auto-hydrate the management panel queue when the window finishes drawing layout components
window.addEventListener("DOMContentLoaded", function() {
    loadOperatorRequestQueue();
}); 		 
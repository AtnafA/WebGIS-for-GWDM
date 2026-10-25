<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="net.AAWSAgDB.fileupload.model.File" %>
<%String baseUrl = getServletContext().getInitParameter("BaseUrl");%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>Groundwater Data Management System</title>
    
    <!-- 1. Leaflet GIS Mapping Asset Injections -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/leaflet/leaflet.css"/>
    <script src="${pageContext.request.contextPath}/dist/leaflet/leaflet.js"></script>
    
    <!-- Core Application Look & Feel Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/tree.css">
    <link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath}/dist/img/aawsa_icon.png"/>
    <link rel="stylesheet" href="https://cloudflare.com">
    
    <style>
        /* Master Document Canvas Allocations */
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
            font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
            background-color: #f1f5f9;
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }

        /* Top Privilege Navigation Utility Bar */
        .titbarnav {
            background-color:#87918e;
            padding: 0 30px;
            height: 45px;
            margin-right: 0%;
            margin-top: -0.90%;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            z-index: 10;
        }
        .titbarnav ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
            gap: 24px;
        }
        .titbarnav a {
            color: #ffffff;
            text-decoration: none;
            font-size: 13px;
            font-weight: 600;
            transition: color 0.2s;
        }
        .titbarnav a:hover {
            color: #a3e635;
        }

        /* Modern 40/60 Balanced Dashboard Grid System */
        .split-dashboard-container {
            display: flex;
            flex: 1;
            width: 100%;
            height: calc(100% - 75px);
        }

        /* Left Control Panel (40% View Grid Segment) */
        .left-auth-panel {
            width: 38.5%;
            background: #ffffff;
            display: flex;
            flex-direction: column;
            padding: 40px;
            box-sizing: border-box;
            border-right: 1px solid #cbd5e1;
            box-shadow: 4px 0 15px rgba(15,23,42,0.04);
            justify-content: center;
            overflow-y: auto;
        }

        /* Right GIS Visualization Panel (60% View Grid Segment) */
        .right-map-panel {
            width: 61.5%;
            height: 100%;
            position: relative;
            background: #e2e8f0;
        }
        #map {
            width: 100%;
            height: 100%;
            z-index: 1;
        }

        /* Form Controls & Styling Overhauls */
        .imgcontainer {
            text-align: center;
            margin-bottom: 20px;
        }
        .avatar {
            width: 90px;
            height: auto;
            border-radius: 50%;
            border: 3px solid #e2e8f0;
            padding: 4px;
            background: white;
        }
        .form-group-label {
            display: block;
            font-weight: 600;
            margin-bottom: 5px;
            font-size: 12.5px;
            color: #334155;
        }
        .form-input-box {
            width: 100%;
            height: 34px;
            padding: 6px 12px;
            font-size: 13px;
            border: 1px solid #cbd5e1;
            border-radius: 4px;
            box-sizing: border-box;
            margin-bottom: 12px;
            background-color: #f8fafc;
            transition: border 0.2s, background-color 0.2s;
        }
        .form-input-box:focus {
            border-color: #06402B;
            background-color: #fff;
            outline: none;
        }
        .btn-auth-submit {
            width: 100%;
            height: 36px;
            background-color: #06402B;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.2s;
        }
        .btn-auth-submit:hover {
            background-color: #042d1e;
        }
        .pwChangeLink {
            background: none;
            border: none;
            color: #1d4ed8;
            font-style: italic;
            cursor: pointer;
            font-size: 12px;
            padding: 0;
            margin-top: 8px;
            display: inline-block;
        }
        .pwChangeLink:hover {
            text-decoration: underline;
        }

        /* State-of-the-Art Footer Design Criteria */
        .global-portal-footer {
            background-color: #0f172a;
            color: #94a3b8;
            text-align: center;
            padding: -3px 20px;
            font-size: 12px;
            font-weight: 500;
            border-top: 1px solid #334155;
            height: 30px;
            box-sizing: border-box;
            z-index: 10;
        }
        
        /* Shift the Leaflet Zoom Controls container away from the edge boundaries */
.right-map-panel .leaflet-top.leaflet-left {
    top: 20px !important;
    left: 15px !important;
}

/* Ensure the zoom buttons are styled cleanly with modern rounded shadows */
.right-map-panel .leaflet-bar {
    border: 1px solid #cbd5e1 !important;
    box-shadow: 0 4px 6px -1px rgba(15, 23, 42, 0.1), 0 2px 4px -2px rgba(15, 23, 42, 0.1) !important;
}

.right-map-panel .leaflet-bar a {
    background-color: #ffffff !important;
    color: #334155 !important;
    border-bottom: 1px solid #e2e8f0 !important;
    transition: background 0.2s, color 0.2s;
}

.right-map-panel .leaflet-bar a:hover {
    background-color: #f8fafc !important;
    color: #06402B !important;
}

/* Custom Elegant Styling for Map Tooltips */
.custom-map-tooltip {
    background: rgba(15, 23, 42, 0.9) !important; /* Premium dark slate with slight opacity */
    backdrop-filter: blur(4px) !important;       /* Modern background blur effect */
    border: 1px solid rgba(255, 255, 255, 0.15) !important;
    border-radius: 6px !important;
    padding: 2px 14px !important;
    color: #f8fafc !important;
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.3), 0 4px 6px -4px rgba(0, 0, 0, 0.3) !important;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
}

/* Styled Arrow Pointer pointing to the borehole marker circle */
.custom-map-tooltip::before {
    border-top-color: rgba(15, 23, 42, 0.9) !important;
}

/* Text Element Layout Inside the Tooltip */
.tooltip-header {
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: #94a3b8;
    font-weight: 600;
    margin-bottom: -1px;
}

.tooltip-value {
    font-size: 13px;
    font-weight: 600;
    color: #ffffff;
    margin-bottom: 4px;
}

.tooltip-action-hint {
    display: block;
    margin-top: 1px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding-top: 2px;
    font-size: 11px;
    font-weight: 600;
    color: #38bdf8; /* Vibrant, readable sky blue accent */
    animation: gentlePulse 2s infinite ease-in-out;
}

@keyframes gentlePulse {
    0%, 100% { opacity: 0.85; }
    50% { opacity: 1; }
}          
    </style>
</head>
<body>

    <!-- Unified Context Controls Navigation Header -->
    <nav class="titbarnav">
        <ul>
            <li><a href="#">About Us</a></li>
            <li><a href="#">Contact</a></li>
            <li id="cliclit" style="position:relative;">
                <a href="#" style="cursor: pointer;">Accounts <i class="fa fa-caret-down"></i></a> 
            </li>
        </ul>
    </nav>

    <!-- MASTER DASHBOARD CONTAINER -->
    <div class="split-dashboard-container">
        
        <!-- LEFT 40% AUTHENTICATOR INTERFACE FRAME -->
        <div class="left-auth-panel">
            
            <!-- Dynamic Corporate Header Titles -->
            <h2 style="color: #06402B; font-size: 20px; font-weight: bold; margin: 0 0 5px 0; text-align: center; line-height: 1.3;">
                AAWSA Groundwater
            </h2>
            <h3 style="color: #475569; font-size: 14px; font-weight: 500; margin: 0 0 25px 0; text-align: center;">
                Information Management System
            </h3>

            <!-- Server-Side Context Notifications Display Panel -->
            <c:if test="${not empty error}">
                <div class="error" style="padding:10px; color:#991b1b; background:#fee2e2; border:1px solid #fca5a5; border-radius:4px; font-size:12px; margin-bottom:15px;"><c:out value="${error}"/></div>
            </c:if>
            <% if(request.getAttribute("changeSuccess") != null) { %>
                <div style="padding:10px; color:#166534; background:#dcfce7; border:1px solid #bbf7d0; border-radius:4px; font-size:12px; margin-bottom:15px; text-align:center;"><%=request.getAttribute("changeSuccess")%></div>
            <% } %>

            <!-- CORE SYSTEM CREDENTIAL SIGN-IN FORM -->
            <form name="loginForm" action="login" method="post" id="loginForm" style="display: block;">
                <div class="imgcontainer">
                    <img src="${pageContext.request.contextPath}/dist/img/aawsa_icon.png" alt="AAWSA Logo" class="avatar">
                </div>

                <label class="form-group-label">User Name:</label>
                <input type="text" class="form-input-box" placeholder="Enter Username" name="username" style="width: 100%;" required="required">
                
                <label class="form-group-label">Password:</label>
                <input type="password" class="form-input-box" placeholder="Enter Password" name="password" style="width: 100%;" required>
                
                <button type="submit" class="btn-auth-submit" style="margin-top:10px;">Login</button>
                <div style="text-align: center;">
                    <input type="button" value="Forgot password ?" id="changePassword" class="pwChangeLink">
                </div>
            </form>

            <!-- MANDATORY SECURITY ACCESS PASSWORD CORRECTION SUB-FORM -->
<!-- MANDATORY SECURITY ACCESS PASSWORD CORRECTION SUB-FORM -->
<form action="changeForgatenPassword" method="post" onsubmit="return check_lis();" id="PWchangingForm" style="display: none; 
width: 100%; box-sizing: border-box; margin-top: 6px;">
    
    <!-- Styled Card Header Context Section -->
    <div style="border-bottom: 2px solid #cbd5e1; padding-bottom: 4px; margin-bottom: 12px; text-align: center;">
        <h4 style="margin: 0; font-size: 15px; color: #06402B; font-weight: bold; letter-spacing: 0.3px;white-space: nowrap; ">
🔑 Account Recovery Verification
        </h4>
        <p style="font-size: 11px; color: #64748b; margin: 4px 0 0 0;">
            Verify your registration profile tokens to authenticate a mandatory credential reset.
        </p>
    </div>

    <!-- HIGH-INTEGRITY VERTICAL FORM GRID (Completely eliminates table overlap bugs) -->
    <div style="display: flex; flex-direction: column; gap: 8px; width: 100%; box-sizing: border-box;">
        
        <!-- Full Name Field -->
        <div style="width: 100%;">
            <label for="fullName" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 4px;">Full Name:</label>
            <input type="text" id="fullName" name="changerFullName" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" placeholder="Enter your full registered name"/>
        </div>

        <!-- User Name Field -->
        <div style="width: 100%;">
            <label for="userName" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 4px;">User Name:</label>
            <input type="text" id="userName" name="changerUserName" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" placeholder="Enter account username"/>
        </div>

        <!-- Email Address Field -->
        <div style="width: 100%;">
            <label for="userEmail" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 4px;">Email Address:</label>
            <input type="email" id="userEmail" name="changerEmail" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" placeholder="yourname@aawsa.gov.et"/>
        </div>

        <!-- Phone Number Field -->
        <div style="width: 100%;">
            <label for="phoneNumber" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 4px;">Phone Number:</label>
            <input type="text" id="phoneNumber" name="changerPhoneNumber" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" placeholder="+251-9XX-XXXXXX"/>
        </div>

        <!-- New Password Field -->
        <div style="width: 100%;">
            <label for="newPassword" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 4px;">New Password:</label>
            <input type="password" id="newPassword" name="changernewPassword" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" 
                   pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
                   title="Must contain at least one number, one uppercase and lowercase letter, and at least 8 or more characters" placeholder="••••••••"/>
        </div>

        <!-- Confirm Password Field -->
        <div style="width: 100%;">
            <label for="confirmPassword" style="display: block; font-weight: 600; font-size: 12px; color: #334155; margin-bottom: 
            4px;white-space: nowrap;">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmchangerPassword" required 
                   style="width: 100%; height: 32px; padding: 4px 10px; font-size: 13px; border: 1px solid #cbd5e1; border-radius: 4px; box-sizing: border-box; background: #f8fafc;" placeholder="Re-type new password"/>
        </div>

        <!-- Hidden Verification Tokens Placeholder -->
        <input type="hidden" name="userID" value=""/>
    </div>

    <!-- ACTION CONTROL SLOTS PANELS BUTTON CONTAINER -->
    <div style="display: flex; gap: 12px; margin-top: 20px; justify-content: flex-end;">
        <button type="submit" class="btn-auth-submit" style="height: 34px; width: 130px; background: #16a34a; font-weight: bold; border:none; 
        border-radius:4px; color:white; cursor:pointer; white-space: nowrap;">Confirm Update</button>
        <button type="button" id="calcelChange" class="btn-auth-submit" style="height: 34px; width: 100px; background: #ef4444; font-weight: bold;
         border:none; border-radius:4px; color:white; cursor:pointer;">Cancel</button>
    </div>

    <!-- Dynamic Mismatch Error Status Area -->
    <div id="passwordMatchM" style="margin-top: 12px; text-align: center; font-size: 12px; font-weight: 600;"></div>
</form>
        </div>
        
            <div class="right-map-panel">
			    <!-- ─── THE NEW MAP LOADING BAR OVERLAY PANEL ─── -->
			    <div id="map-loader-mask" class="map-loading-overlay">
			        <div class="map-spinner-widget"></div>
			        <div class="map-loading-text">Loading Groundwater Spatial Layers...</div>
			    </div>
			    
			    <!-- Core Leaflet Map Canvas Box Element -->
			    <div id="map"></div>
			</div>
            
    </div>

    <footer class="global-portal-footer">
        <script type="text/javascript" src="hdfoter/foot.js"></script>
    </footer>
                        <!-- Include your existing study area boundary dataset vector before running map initialization scripts -->
<script src="${pageContext.request.contextPath}/dist/studyArea.js"></script>

<script type="text/javascript">
// Initialize state synchronization variables to monitor layer loading
var isBasemapBoundariesLoaded = false;
var isBoreholeCoordinatesLoaded = false;

// Helper utility to safely remove the loader mask once both tasks clear checkpoints
function evaluateLoadingPipelineResolution() {
    if (isBasemapBoundariesLoaded && isBoreholeCoordinatesLoaded) {
        var loaderMask = document.getElementById("map-loader-mask");
        if (loaderMask) {
            // Smoothly trigger the CSS fade away opacity animation parameters
            loaderMask.classList.add("map-loader-hidden");
            console.log("🚀 Success: All map layers and coordinate vectors successfully loaded. Hiding loading bar mask.");
        }
    }
}
    // 1. Initialize Leaflet Map Canvas View Frame matching Addis Ababa boundary targets
    var map = L.map('map', {
        zoomControl: true,
        attributionControl: false
    }).setView([8.9590, 38.7901], 10.5);

    // Attach standard modern corporate open street map basemap layers
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
    }).addTo(map);

    // 2. High-Density Color Assignment Engine matching your exact FID requirements
    function getColor(d) {
        return d == 32 ? '#708090' :	
               d == 31 ? '#008080' :
               d == 30 ? '#FF7F50' :
               d == 29 ? '#EA4335' :
               d == 28 ? '#4285F4' :
               d == 27 ? '#34A853' :
               d == 26 ? '#FBBC04' : // Fixed syntax error: Added missing leading '#' hash indicator
               d == 25 ? '#1A73E8' :
               d == 24 ? '#8080FF' :
               d == 23 ? '#E6194B' :
               d == 22 ? '#656565' :	
               d == 21 ? '#FFD700' :
               d == 20 ? '#A52A2A' :
               d == 19 ? '#A9A9A9' :
               d == 18 ? '#000075' :
               d == 17 ? '#2F4F4F' :
               d == 16 ? '#808000' :
               d == 13 ? '#AAFFC3' :
               d == 12 ? '#800000' :	
               d == 11 ? '#FFFAC8' :
               d == 9  ? '#E6BEFF' :
               d == 8  ? '#FABEBE' :
               d == 7  ? '#BCF60C' :
               d == 6  ? '#F032E6' :
               d == 5  ? '#42D4F4' :
               d == 4  ? '#911EB4' :
               d == 3  ? '#3CB44B' :
               d == 2  ? '#4363D8' :
               d == 1  ? '#FFE119' :
               d == 0  ? '#F58231' :
                         '#f7f77e';
    }

    // Styling configuration callback layout rules for the study area boundary polygon features
    function styleStudyAreaFeature(feature) {
        // Adjust property key check matching your studyArea.js schema variables (e.g., FID or id)
        var featureFID = feature.properties.FID != null ? feature.properties.FID : feature.properties.id;
        return {
            fillColor: getColor(featureFID),
            weight: 1.5,
            opacity: 1,
            color: '#ffffff',
            fillOpacity: 0.4
        };
    }

    // 3. Render Administrative Boundaries Layer (studyArea.js GeoJSON instance vector)
    if (typeof regions !== 'undefined') {
        L.geoJSON(regions, {
        	 style: styleStudyAreaFeature,
             onEachFeature: function(feature, layer) {
                 // Optional boundary interactive popups
                 if (feature.properties && feature.properties.Woreda_Sub) {
                     layer.bindPopup(feature.properties.Woreda_Sub);
                 }
             }
        }).addTo(map);
        
        // Boundaries are loaded locally from JS resource asset right away
        isBasemapBoundariesLoaded = true; 
        evaluateLoadingPipelineResolution();
    } else {
        System.err.println("Geospatial baseline data check warning: 'studyAreaDataset' vector array is absent inside dist/studyArea.js.");
     // Mark as true to prevent locking the screen loader mask indefinitely
        isBasemapBoundariesLoaded = true; 
        evaluateLoadingPipelineResolution();
    }

    // 4. HIGH-PERFORMANCE ASYNCHRONOUS AJAX WATER WELL MARKER EXTRACTOR ENGINE
    function loadAndOverlayWellMarkers() {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '${pageContext.request.contextPath}/FetchWellMapCoordinates', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        
        xhr.onload = function() {
            if (xhr.status === 200) {
                var boreholes = JSON.parse(xhr.responseText);
                
                // Set up custom design parameters for borehole circles
                var boreholeMarkerOptions = {
                    radius: 5,
                    fillColor: "#06402B",
                    color: "#ffffff",
                    weight: 1,
                    opacity: 1,
                    fillOpacity: 0.85
                };

                for (var i = 0; i < boreholes.length; i++) {
                    var well = boreholes[i];
                    
                    // Access parameters matching your explicit java file initialization constructor sequence
                    var latitude  = well.locy; // locy maps directly to Latitude [Column 4]
                    var longitude = well.locx; // locx maps directly to Longitude [Column 3]
                    // ─── FIXED: COALESCING CHAINS TO RESOLVE PRIVATE FIELDS CONTRACTION ───
                    var wellField = well.wellField || well.wf_name || well.pro_name_ov || well.proNameOv || "Unknown Field";
                    var wellIndex = well.wellIndex || well.well_index || well.pro_code || well.proCode || "Unknown Index";
                    // Catches all potential naming styles generated by Jackson serialization mapping loops
                    var subCityName = well.subcityzone || well.subcityZone || well.subCityZone || well.subcity_zone || "Unknown Zone";

                    console.log("Verified Data Row -> Index: " + wellIndex + " | Zone: " + subCityName);


                    // Verify output values directly inside your browser developer tools console tab
                    console.log("Synchronized Borehole Mapping -> Field: " + wellField + " | Index: " + wellIndex);

                    if (latitude && longitude && latitude !== 0.0) {
                        // Inject point feature circle marker into Leaflet space
                        var circleMarker = L.circleMarker([latitude, longitude], boreholeMarkerOptions).addTo(map);
                        
                        // 🫵 DYNAMIC MOUSE HOVER TOOLTIP DEFINITION: Shows Well Field and Well Index context immediately on mouseover
                         // 🫵 DYNAMIC REFACTORED TOOLTIP DEFINITION: Clean structure separated from layout styling parameters
                        var hoverTooltipText = 
                            '<div class="tooltip-header">Well Field:</div>' +
                            '<div class="tooltip-value">' + wellField + '</div>' +
                            '<div class="tooltip-header">Borehole Name:</div>' +
                            '<div class="tooltip-value">' + wellIndex + '</div>' +
                            '<div class="tooltip-header">Worada/ Sub city/ Zone:</div>' +
                            '<div class="tooltip-value">' + subCityName + '</div>' +
                            '<div class="tooltip-action-hint">✨ Click borehole to synchronize data</div>';
                        circleMarker.bindTooltip(hoverTooltipText, {
                            sticky: true, // Forces tooltip tracking to snap strictly to mouse cursor pointers
                            direction: 'top',
                            className: 'custom-map-tooltip',
                            offset: [0, -5] // Shifts the layout tooltip bubble up away from circle overlap
                        });

                        // 5. INTERACTIVE AUTH FOCUS REDIRECTION HOOK
                        circleMarker.on('click', function(e) {
                            L.DomEvent.stopPropagation(e); // Stop event bubble triggers from hitting the basemap background
                            
                            var authPanelForm = document.getElementById("loginForm");
                            if (authPanelForm) {
                                // Provide an elegant, state-of-the-art interactive flash response layout notification highlight
                                authPanelForm.style.transition = "transform 0.3s ease, border-color 0.3s";
                                authPanelForm.style.transform = "scale(1.02)";
                                authPanelForm.style.borderColor = "#06402B";
                                authPanelForm.style.boxShadow = "0 0 15px rgba(6,64,43,0.3)";
                                
                                setTimeout(function() {
                                    authPanelForm.style.transform = "scale(1)";
                                    authPanelForm.style.borderColor = "#cbd5e1";
                                    authPanelForm.style.boxShadow = "none";
                                }, 1200);
                            }
                        });
                    }
                }
             // 🫵 CHECKPOINT TARGET MET: Asynchronous AJAX records have completed loading and rendering!
                isBoreholeCoordinatesLoaded = true;
                evaluateLoadingPipelineResolution();
            } else {
                console.error("Geospatial REST vector stream call returned a critical response error code: " + xhr.status);
                isBoreholeCoordinatesLoaded = true; // Fallback to let loader hide if call fails
                evaluateLoadingPipelineResolution();
            }
        };
        
        xhr.onerror = function() {
            console.error("Asynchronous data migration lookup link tracking failed at network interface boundary layer.");
            isBoreholeCoordinatesLoaded = true; // Fallback to let loader hide if call fails
            evaluateLoadingPipelineResolution();
        };

        xhr.send();
    }

    // Call well extractor engine on page initialization
    loadAndOverlayWellMarkers();
    
    // 2. Dynamic Redirection Hook: Clicking points focuses attention back onto the authentication form
    map.on('click', function(e) {
        var authPanel = document.getElementById("loginForm");
        if(authPanel) {
            authPanel.style.border = "2px solid #06402B";
            setTimeout(function() { authPanel.style.border = "none"; }, 1500);
        }
    });

    // 3. Dropdown Menu Trigger Configurations
    document.getElementById("cliclit").addEventListener("click", function(e) {
        e.stopPropagation();
        var item_ser = document.getElementById("list_it");
        item_ser.style.display = (item_ser.style.display === "none") ? "block" : "none";
    });
    window.onclick = function() {
        document.getElementById("list_it").style.display = 'none';
    };

    // 4. View Switching Frameworks
    function switchLoginView(viewKey) {
        var services = document.querySelectorAll(".item_service");
        var discPanel = document.querySelector(".con_disc");
        var formPanel = document.getElementById("loginForm");
        
        if(viewKey === "con_list") {
            if(discPanel) discPanel.style.display = "none";
            if(formPanel) formPanel.style.display = "block";
        } else if(viewKey === "con_disc") {
            if(formPanel) formPanel.style.display = "none";
            if(discPanel) discPanel.style.display = "block";
        }
    }

 // Password Window Toggles
    var passwordChange = document.getElementById("PWchangingForm");
    var loginForm = document.getElementById("loginForm");
    document.getElementById("changePassword").onclick = function() {
        passwordChange.style.display = 'block';
        loginForm.style.display = 'none';
    };
    document.getElementById("calcelChange").onclick = function() {
        passwordChange.style.display = 'none';
        loginForm.style.display = 'block';
    };
</script>
</body></html>
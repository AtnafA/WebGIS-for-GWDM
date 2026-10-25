/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 1 ───
 * =========================================================================
 */

// Dynamically extract the safe absolute Tomcat application deployment path
var appServerContextRoot = "/" + window.location.pathname.split('/')[1];

var data_tooverlay = [];
var trace_pro_loc = [];

/**
 * Modernised Asynchronous Data Extraction Pipeline.
 * Replaces old raw XMLHTTP objects with native Promise envelopes.
 */
function georeference(event) {
    var targetUrl = appServerContextRoot + "/project_overlay";
    
    fetch(targetUrl, { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("HTTP connection check anomaly: " + response.status);
            return response.json();
        })
        .then(function(pro_overlay) {
            get_projet_overlay(pro_overlay);
        })
        .catch(function(error) {
            console.error("Critical failure during project overlay array resolution:", error);
        });
}

function get_projet_overlay(data_pro) {
    if (!data_pro || data_pro.length === 0) return;
    
    data_tooverlay = [];
    trace_pro_loc = [];
    
    for (var i = 0; i < data_pro.length; i++) {
        data_tooverlay.push(data_pro[i].pro_name_ov);
        trace_pro_loc.push(data_pro[i].locx + ', ' + data_pro[i].locy);
    }
    console.log("Geospatial profile vectors synchronized. Node count: " + data_pro.length);
}

// Core base tile layer map initialization
var osm = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
});

var map = L.map('map', {
    attributionControl: false,
    zoomControl: false,
    zoomSnap: 0.1,  // 🫵 CRITICAL UNLOCK: Enforcers high-fidelity smooth decimal zooming increments
    zoomDelta: 0.1, // Allows fine-grained zooming control passes
    minZoom: 8,
    maxZoom: 20,
    center: [8.9590, 38.7701],
    zoom: 10.65,
    layers: [osm]
});

var spinner = document.getElementById('map-loading-spinner');
var loaderID = document.getElementById('loaderID');

var originalCenter = map.getCenter();
var originalZoom = map.getZoom();
var basemap = { 'Base Map': osm };

// ─── GEOSERVER ENDPOINT CONFIGURATIONS ───
var currentHost = window.location.hostname;
// Standardize ports matching your infrastructure layout instructions
var geoserverBaseUrl = 'http://' + currentHost + ':8090/geoserver';
var geoserverWmsUrl  = geoserverBaseUrl + '/wms';
var geoserverWfsUrl  = geoserverBaseUrl + '/wfs';

var longitude, latitude, pointValue;

// Dynamic parameters assembly block for WFS features extraction streams
var geoJsonUrlParams = new URLSearchParams({
    service: 'WFS',
    version: '1.0.0',
    request: 'GetFeature',
    typeName: 'AAWSAgeodata:operation_wquality_data',
    outputFormat: 'application/json'
});
var completeGeoJsonUrl = geoserverWfsUrl + '?' + geoJsonUrlParams.toString();

fetch(completeGeoJsonUrl)
    .then(function(response) {
        if (!response.ok) throw new Error('WFS Layer unreachable: ' + response.statusText);
        return response.json();
    })
    .then(function(pointGeoJSON) {
        if (pointGeoJSON && pointGeoJSON.features) {
            pointGeoJSON.features.forEach(function(feature) {
                var coordinates = feature.geometry.coordinates;
                var longX = coordinates[0];
                var latY  = coordinates[1];
                
                // Map model properties accurately matching your schema
                var pointValueNo     = feature.properties.no3_;
                var pointValueWI     = feature.properties.well_index;
                var pointValueFe     = feature.properties.fe_;
                var pointvalueFl     = feature.properties.f_;
                var pointValueWellId = feature.properties.well_id;
                
                datafromGeoServer(pointValueWellId, latY, longX, pointValueNo, pointValueWI, pointValueFe, pointvalueFl);
            });
        }
    })
    .catch(function(error) {
        console.error('Error fetching point vector datasets out of GeoServer:', error);
    });
    
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
var style = function(feature) {
    return {
        fillColor: typeof getColor === 'function' ? getColor(feature.properties.FID) : '#cbd5e1',
        weight: 1.5,
        opacity: 1,
        color: '#ffffff',
        fillOpacity: 0.4
    };
};

var overlayMapsOriginal;
var wmsWebMaps = document.getElementById("wms_mapTrial");
var accessedOverLay = {};

/**
 * Dynamically parses GeoServer WMS capabilities into our dropdown selector.
 */
function fetchGeoServerLayers() {
    var getCapabilitiesUrl = geoserverWmsUrl + "?service=WMS&version=1.1.0&request=GetCapabilities";
    
    fetch(getCapabilitiesUrl)
        .then(function(response) { return response.text(); })
        .then(function(xmlText) {
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(xmlText, "text/xml");
            var layerNodes = xmlDoc.evaluate("//Capability/Layer/Layer[Name]", xmlDoc, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
            
            if (wmsWebMaps) {
                for (var i = 0; i < layerNodes.snapshotLength; i++) {
                    var layerName  = layerNodes.snapshotItem(i).getElementsByTagName('Name')[0].textContent;
                    var layerTitle = layerNodes.snapshotItem(i).getElementsByTagName('Title')[0].textContent;
                    
                    var option = document.createElement("option");
                    option.value = layerName;
                    option.textContent = layerTitle;
                    wmsWebMaps.appendChild(option);
                }
            }
        })
        .catch(function(error) {
            console.error('Capabilities synchronization failed:', error);
        });
}

// Fire capacities fetch process smoothly on load
fetchGeoServerLayers();
/**
 * =========================================================================
 * ─── GEOSERVER PROCESSED LAYER CONTROL OVERHAUL (FIXED STATE ENGINE) ───
 * =========================================================================
 */
var layerGrouping = null;
var currentWmsLayer = null;

function changeLayerOnMap(layerKey) {
    // 1. Instantly uncheck the layer checkboxes inside Leaflet's UI when dropdown changes
    var layerControlContainer = document.querySelector('.leaflet-control-layers-overlays');
    if (layerControlContainer) {
        var checkboxes = layerControlContainer.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach(function(checkboxBox) {
            checkboxBox.checked = false; 
        });
    }
    // 2. Transactional cleanup of both core map layers
    if (map.hasLayer(Reion_layer))  map.removeLayer(Reion_layer);
    if (map.hasLayer(waterChemoL)) map.removeLayer(waterChemoL);
    
    activeOverlayLayerName = ""; 

    // 3. Unlink existing WMS layer asset and master parent LayerGroup wrappers
    if (currentWmsLayer && map.hasLayer(currentWmsLayer)) { map.removeLayer(currentWmsLayer); }
    if (layerGrouping && map.hasLayer(layerGrouping)) { map.removeLayer(layerGrouping); }
    
    currentWmsLayer = null;
    layerGrouping = null;

    // 4. FIXED VISIBILITY GATE: Toggle button display based on whether a layerKey exists
    try { map.removeControl(mapDownloadControl); } catch(e){} // Strip previous instance to clear cache

    if (layerKey && layerKey !== "") {
        // Bind and add new WMS layer profile to the map viewport
        currentWmsLayer = L.tileLayer.wms(geoserverWmsUrl, {
            layers: layerKey,
            format: 'image/png',
            transparent: true,
            version: '1.1.0'
        });
        layerGrouping = L.layerGroup([currentWmsLayer]).addTo(map);

        // SHOW BUTTON: Only appends the floating print action widget if a layer is loaded!
        mapDownloadControl.addTo(map);
    }
    
    // 5. Remove normal vector widget controls cleanly to give workspace breathing room
    if (typeof Region_legend !== 'undefined' && Region_legend) { try { map.removeControl(Region_legend); } catch(e){} }
    if (typeof tool_tip     !== 'undefined' && tool_tip)      { try { map.removeControl(tool_tip);      } catch(e){} }
    if (typeof search_tKey   !== 'undefined' && search_tKey)    { try { map.removeControl(search_tKey);    } catch(e){} }
    
    // Clear dynamic content display tables safely
    if (typeof getElementByIdCustome === 'function')    { getElementByIdCustome("", 0); }
    if (typeof getWellChemistryByWellId === 'function') { getWellChemistryByWellId("", 0); }
}
/**
 * Communicates with GeoServer using GetMap parameters to extract the complete map layer.
 * FIXED: Adds a geographic margin buffer to the eastern bounding box to capture 
 * the embedded legend cleanly without distortion or clipping.
 */
function downloadProcessedMapLayer() {
    var layerKeyToExport = null;

    if (currentWmsLayer && currentWmsLayer.wmsParams && currentWmsLayer.wmsParams.layers) {
        layerKeyToExport = currentWmsLayer.wmsParams.layers;
    } else if (typeof wmsMapoverlay !== 'undefined' && wmsMapoverlay && wmsMapoverlay.wmsParams) {
        layerKeyToExport = wmsMapoverlay.wmsParams.layers;
    }

    if (!layerKeyToExport || layerKeyToExport === "") {
        alert("Export Aborted: No active GeoServer processed analytical map layer is currently selected.");
        return;
    }

    if (typeof loaderID !== 'undefined' && loaderID) {
        loaderID.style.display = 'block';
    }

    // ─── 🫵 CRITICAL SAFEGUARD: DYNAMIC EXTENT WITH LEGENDBUFFER ───
    var absoluteBBoxString = "";
    var currentAspectRatio = 1.0;

    if (typeof admin_layer !== 'undefined' && admin_layer && typeof admin_layer.getBounds === 'function') {
        var shapefileBounds = admin_layer.getBounds();
        var southWest = shapefileBounds.getSouthWest();
        var northEast = shapefileBounds.getNorthEast();
        
        // 🫵 FIXED: Dynamically push the Eastern boundary further to the right by 0.15 degrees.
        // This expands the export frame to include the pre-composited legend area seamlessly!
        var extendedMaxLng = northEast.lng + 0.20; // Adjust between +0.10 and +0.20 if needed
        
        // Construct the padded bounding box query string: minx,miny,maxx,maxy
        absoluteBBoxString = southWest.lng + ',' + southWest.lat + ',' + extendedMaxLng + ',' + northEast.lat;
        
        // Compute the precise padded aspect ratio to completely avoid image stretching or compression bugs
        var widthDegrees  = extendedMaxLng - southWest.lng;
        var heightDegrees = northEast.lat - southWest.lat;
        currentAspectRatio = widthDegrees / heightDegrees;
        
        console.log("✔ Padded legend boundary extent computed successfully: " + absoluteBBoxString);
    } else {
        // Fallback to active screen bounds if admin layer context is unmounted
        var currentBounds = map.getBounds();
        var southWest = currentBounds.getSouthWest();
        var northEast = currentBounds.getNorthEast();
        absoluteBBoxString = southWest.lng + ',' + southWest.lat + ',' + northEast.lng + ',' + northEast.lat;
        currentAspectRatio = map.getSize().x / map.getSize().y;
        console.warn("⚠️ Warning: Admin layer context unavailable. Using active window bounds.");
    }

    // ─── SAFE RESOLUTION CONSTANTS (Low-Overhead High Definition) ───
    // Keeping the width at 3840px ensures crisp text readability while staying safe under memory ceilings
    var exportPixelWidth  = 3840; 
    var exportPixelHeight = Math.round(exportPixelWidth / currentAspectRatio); 

    // Assemble the complete high-quality OGC REST request
    var getMapUrlParams = new URLSearchParams({
        service: 'WMS',
        version: '1.1.1',
        request: 'GetMap',
        layers: layerKeyToExport,
        styles: '', 
        bbox: absoluteBBoxString, // Queries the padded maximum published layer footprint
        srs: 'EPSG:4326', 
        width: exportPixelWidth.toString(),
        height: exportPixelHeight.toString(),
        format: 'image/png', // Native lossless file format compression
        transparent: 'true',
        exceptions: 'application/vnd.ogc.se_inimage'
    });

    var completeGetMapExportUrl = geoserverWmsUrl + '?' + getMapUrlParams.toString();
    
    console.log("Launching background download pass via GetMap URL pipeline. Output dimensions: " + exportPixelWidth + "x" + exportPixelHeight + "px.");

    // Fetch and download the file natively as a binary file blob object block to prevent blank pages
    fetch(completeGetMapExportUrl, { method: 'GET' })
        .then(function(response) {
            if (!response.ok) throw new Error("GeoServer map layer compilation network exception: " + response.statusText);
            return response.blob(); 
        })
        .then(function(mapFileBlob) {
            var transientDownloadAnchor = document.createElement('a');
            var localBlobObjectUrl = window.URL.createObjectURL(mapFileBlob);
            var uniqueFileTimestamp = new Date().toISOString().split('T')[0];
            var stylizedLayerFileName = layerKeyToExport.replace(/[^a-zA-Z0-9-_]/g, '_');

            transientDownloadAnchor.href = localBlobObjectUrl;
            transientDownloadAnchor.download = 'CustomLayer_Map_' + stylizedLayerFileName + '_' + uniqueFileTimestamp + '.png';
            
            document.body.appendChild(transientDownloadAnchor);
            transientDownloadAnchor.click();
            
            document.body.removeChild(transientDownloadAnchor);
            window.URL.revokeObjectURL(localBlobObjectUrl); // Release system memory instantly
            
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
            console.log("✔ Lossless Complete Map Layer Export Successfully Completed.");
        })
        .catch(function(error) {
            console.error("WMS high-resolution export processing error:", error);
            alert("Map Export Failed: Unable to pull complete map data from GeoServer.");
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
        });
}
// Global reference handles for administrative polygon layers
var geojson;
var popups;
var rname;
var rnameII;

/**
 * Manages administrative boundaries layout vector mouseover triggers.
 * Optimized to prevent aggressive server-side request storms.
 */
// Establish a custom high-priority pane for borehole points on initialization
if (map && !map.getPane('boreholePane')) {
    var boreholePane = map.createPane('boreholePane');
    boreholePane.style.zIndex = 450; // Forces markers to float permanently above administrative woredas (zIndex 400)
    boreholePane.style.pointerEvents = 'none'; // Allows mouse events to click through the pane container elements
}
/**
 * Manages administrative boundaries layout vector mouseover triggers.
 * Optimized to preserve point visibility layers.
 */
function highlightFeature(e) {
    var layer = e.target;
    
    // High-performance boundary highlight style configuration matrix
    layer.setStyle({
        weight: 2.5,
        color: '#06402B', // Highlight with AAWSA Corporate Forest Green
        dashArray: '',
        fillOpacity: 0.15
    });
    
    // Bring polygon up to front of its OWN pane layer, without covering the borehole pane
    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
        layer.bringToFront();
    }

    // Resolve the Woreda identifier safely out of feature properties
    rnameII = layer.feature.properties.Woreda_Sub || layer.feature.properties.woreda_name;
    if (!rnameII) return;

    // Cache lookup token optimization check to minimize REST request floods
    if (layer.feature.properties.cached_data) {
        if (typeof tool_tip !== 'undefined' && tool_tip.update) {
            tool_tip.update(layer.feature.properties.cached_data);
        }
        return;
    }

    var targetRegionUrl = appServerContextRoot + "/acc_region?reg_name=" + encodeURIComponent(rnameII);
    fetch(targetRegionUrl, { method: 'POST' })
        .then(function(response) {
            if (response.ok) return response.json();
            throw new Error("Region vector lookup connection failure.");
        })
        .then(function(region_data) {
            layer.feature.properties.cached_data = region_data;
            if (typeof tool_tip !== 'undefined' && tool_tip.update) {
                tool_tip.update(region_data);
            }
        })
        .catch(function() {
            console.warn("Silent fallback: Regional analytics are temporarily unavailable for " + rnameII);
        });
}

function resetHighlight(e) {
    if (admin_layer) {
        admin_layer.resetStyle(e.target);
    }
    if (typeof tool_tip !== 'undefined' && tool_tip.update) {
        tool_tip.update();
    }
}

function zoomToFeature(e) {
    map.fitBounds(e.target.getBounds());
}

function onEachFeature(feature, layer) {
    layer.on({
        mouseover: highlightFeature,
        mouseout: resetHighlight,
        click: zoomToFeature
    });
}

// ─── INITIALIZE BASELINE ADMINISTRATIVE POLYGON LAYERS ───
var admin_layer = L.geoJSON(regions, {
    style: typeof style === 'function' ? style : null, 
    onEachFeature: onEachFeature
});

var Reion_layer = L.layerGroup([admin_layer]).addTo(map);

var waterChemlement = L.geoJSON(regions, {
    style: typeof style === 'function' ? style : null, 
    onEachFeature: onEachFeature
});
var waterChemoL = L.layerGroup([waterChemlement]);

// ─── EXTRACT AND ASSEMBLE METADATA LEGENDS ASYNCHRONOUSLY ───
var grades1 = [];
var countT = [];
var countAllT = 0;
var pro_type = document.querySelectorAll(".geo_legend");

for (var ij = 0; ij < pro_type.length; ij++) {
    var stor_pro = pro_type[ij].innerText;
    if (!stor_pro || stor_pro.trim() === "") continue;
    
    var inneerT = stor_pro.split(',');
    if (inneerT.length >= 2) {
        var proj_type = inneerT[0].trim();
        var countN    = parseInt(inneerT[1].trim(), 10) || 0;
        
        countAllT += countN;
        grades1.push(proj_type);
        countT.push(countN);
    }
}

// ─── EXTRACT HIDDEN DOM VECTOR BUFFER POOLS SECURELY ───
var XlocAll = [], YlocAll = [], Pname_codeAll = [], PnameAll = [], pro_yearAll = [], PyearU = [], pro_colorU = [], Pro_IDU = [];
var store_getele = document.querySelectorAll(".geo");	
for (var i = 0; i < store_getele.length; i++) {
    var elements = store_getele[i].innerText;
    if (!elements || elements.trim() === "") continue;
    
    var split_inneerT = elements.split(',');
    if (split_inneerT.length >= 7) {
        var Pname           = split_inneerT[0].trim();
        var xLong           = parseFloat(split_inneerT[1].trim());
        var yLat            = parseFloat(split_inneerT[2].trim());
        var Pcode           = split_inneerT[3].trim();
        var pro_id          = parseInt(split_inneerT[4].trim(), 10) || 0;
        var Pyear           = split_inneerT[5].trim();
        var pro_type_color  = split_inneerT[6].trim();
        
        // FIXED: Passing correct assigned color parameter to avoid undefined crasher bugs
        georeference_code(xLong, yLat, Pname, Pcode, pro_id, Pyear, pro_type_color);
        
        XlocAll.push(xLong);
        YlocAll.push(yLat);
        Pname_codeAll.push(Pcode);
        PnameAll.push(Pname);
        Pro_IDU.push(pro_id);
        pro_yearAll.push(Pyear);
        pro_colorU.push(pro_type_color);
    }
}

/**
 * Persists and plots dynamic point feature circle marker pins cleanly.
 * Replaced string-concatenated inline HTML markers with standardized layouts.
 */
function georeference_code(Xlong, Ylat, Lname, Lcode, Lpro_id, Lpyear, color_code) {
    if (!Ylat || !Xlong || Ylat === 0.0) return;
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var specific_coord = [Ylat, Xlong];

    // 🫵 STATE-OF-THE-ART INLINE DATA TABLE POPUPS (Replaces legacy side tables)
         // FIXED: Cleaned up trailing string syntax errors from your transition layout string block
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Borehole Metrics Profile</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + Lname + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + Lcode + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Const. Year:</td><td style="color:#0f172a; font-weight:600;">' + Lpyear + '</td></tr>' +
            '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + Lpro_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstance = L.circleMarker(specific_coord,boreholeMarkerOptions);
    
    // OBJECTIVE MET: Binds the data payload profile to open cleanly when mouse tips touch the point
    markerInstance.bindTooltip(popupContent, { 
        sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
        direction: 'top', 
        offset: [0, -5],
        pane:'tooltipPane'
    });
    
    // Handle full core records sheet data loading on click
    markerInstance.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(Lcode, Lpro_id, Lpyear);
        }
    });
    Reion_layer.addLayer(markerInstance);
}		

function get_latlong_by_legend_filter(data_byLegend) {
    if (!data_byLegend) return;
    for (var j = 0; j < data_byLegend.length; j++) {
        var item = data_byLegend[j];
        update_overlay(item.Le_locy, item.Le_locx, item.Le_pro_name_ov, item.Le_pro_code, item.Le_Pyear, item.Leg_Id_pro, item.Lpro_type);
    }
}

function update_overlay(Ulaty, UlongX, Uproject_name, Uproject_code, Uproject_year, Upro_id, Uproject_type) {
    if (!Ulaty || !UlongX) return;
    var fillColor=colorCoding(Uproject_name);
     var boreholeMarkerOptions = {
        radius: 5,
        fillColor: fillColor,
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var specific_coordII = [Ulaty, UlongX];
        var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Borehole Metrics Profile</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_name + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_code + '</td></tr>' +
            '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + Upro_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceL = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceL.bindTooltip(popupContent, { 
		sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
        direction: 'top', 
        offset: [0, -5],
        pane:'tooltipPane'
        });
 
    // Handle full core records sheet data loading on click
    markerInstanceL.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(Uproject_code, Upro_id, Uproject_year);
        }
    });   
    Reion_layer.addLayer(markerInstanceL);
}
/**
 * Parses and plots analytical hydrochemistry vector grids directly from the GeoServer WFS.
 */
function datafromGeoServer(pointValueWellId, laty, longX, pointDataNo, pointDataWI, pointDataFe, pointDataF) {
    if (!laty || !longX) return;
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var specific_chemical = [laty, longX];
  
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Hydrochemistry Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + pointDataWI + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Nitrate (NO₃):</td><td style="color:#0f172a; font-weight:600;">' + pointDataNo + ' mg/L</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Iron (Fe):</td><td style="color:#0f172a; font-weight:600;">' + pointDataFe + ' mg/L</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Fluoride (F⁻):</td><td style="color:#0f172a; font-weight:600;">' + pointDataF + ' mg/L</td></tr>' +
            '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + pointValueWellId + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var stor_chemicalFeature = L.circleMarker(specific_chemical,boreholeMarkerOptions);
    stor_chemicalFeature.bindTooltip(popupContent,{ 
		sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
        direction: 'top', 
        offset: [0, -5],
        pane:'tooltipPane'
        });
    
    // Handle full core records sheet data loading on click
    stor_chemicalFeature.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(pointDataWI, pointValueWellId, pointDataFe);
        }
    });

    waterChemoL.addLayer(stor_chemicalFeature);
} // This curly brace now cleanly closes your parent datafromGeoServer function block!

/**
 * =========================================================================
 * ─── SINGLE-CLICK MUTUALLY EXCLUSIVE WEBGIS LAYER ROUTER (FIXED) ───
 * =========================================================================
 */

// 1. Initialize baseline map layer controls securely
var overlayMapsOriginal = {
    'Water Well Data': Reion_layer,
    'Water Quality Data': waterChemoL
};
L.control.layers(basemap, overlayMapsOriginal).addTo(map);

var wellInformation = document.getElementById("Physical_hydrolics");
var hydrochemistry  = document.getElementById("hydro_chemistry");

// Global tracker monitoring the active operational layer state contract
var activeOverlayLayerName = "Water Well Data"; 

/**
 * High-Performance Transactional Layer Switcher Matrix.
 * Coordinates map layer swaps, sidebar accordion updates, and widget visibility.
 */
function switchPortalLayerContext(targetLayerName) {
    if (!targetLayerName || targetLayerName === activeOverlayLayerName) return;
    activeOverlayLayerName = targetLayerName;
    
    var isWellData = (targetLayerName === 'Water Well Data');
    var isChemData = (targetLayerName === 'Water Quality Data');

    // ─── AXIS A: WATER WELL INVENTORY DISPLAY MATRIX ───
    if (isWellData) {
		 try { map.removeControl(mapDownloadControl); } catch(e){} // Hides button immediately!
		
        // Clear all active custom WMS layers from the base map canvas safely
        if (layerGrouping && map.hasLayer(layerGrouping)) { map.removeLayer(layerGrouping); }
        if (currentWmsLayer && map.hasLayer(currentWmsLayer)) { map.removeLayer(currentWmsLayer); }
        currentWmsLayer = null;
        layerGrouping = null;

        var wmsSelect = document.getElementById("wms_mapTrial");
        if (wmsSelect) wmsSelect.value = "";

        // Swap the sidebar sub-menu accordion layouts natively
        if (hydrochemistry && wellInformation && document.body.contains(hydrochemistry)) {
            hydrochemistry.replaceWith(wellInformation);
        }

        // Clear dynamic data display summaries safely using method invocation hooks
        if (typeof getElementByIdCustome === 'function') { getElementByIdCustome("", 0); }

        // Defer widget placement slightly to guarantee a clean browser UI render pass
        setTimeout(function() {
            if (typeof search_tKey   !== 'undefined' && search_tKey)   { try { map.removeControl(search_tKey);   } 
            catch(e){} try { search_tKey.addTo(map);   } catch(e){} }
            if (typeof Region_legend !== 'undefined' && Region_legend) { try { map.removeControl(Region_legend); } 
            catch(e){} try { Region_legend.addTo(map); } catch(e){} }
            if (typeof tool_tip     !== 'undefined' && tool_tip)     { try { map.removeControl(tool_tip);     } 
            catch(e){} try { tool_tip.addTo(map);      } catch(e){} }
        }, 10);

        // Map vector layer visibility transactions
        if (map.hasLayer(waterChemoL)) map.removeLayer(waterChemoL);
        if (!map.hasLayer(Reion_layer)) map.addLayer(Reion_layer);
        
        console.log("✔ Context Swap Met: Water Well Engine fully operational.");
    } 
    
    // ─── AXIS B: HYDROCHEMISTRY ANALYTICS DISPLAY MATRIX ───
    else if (isChemData) {
		try { map.removeControl(mapDownloadControl); } catch(e){} // Hides button immediately!
        // Clear all active custom WMS layers from the base map canvas safely
        if (layerGrouping && map.hasLayer(layerGrouping)) { map.removeLayer(layerGrouping); }
        if (currentWmsLayer && map.hasLayer(currentWmsLayer)) { map.removeLayer(currentWmsLayer); }
        currentWmsLayer = null;
        layerGrouping = null;

        var wmsSelect = document.getElementById("wms_mapTrial");
        if (wmsSelect) wmsSelect.value = "";

        // Swap the sidebar sub-menu accordion layouts natively
        if (wellInformation && hydrochemistry && document.body.contains(wellInformation)) {
            wellInformation.replaceWith(hydrochemistry);
            hydrochemistry.style.display='block';
        }

        // Display an elegant prompt directing users to pick a borehole from the map layout
        var attributesDisplayContainer = document.getElementById("data_by_yrp");
        if (attributesDisplayContainer) {
            attributesDisplayContainer.innerHTML = 
                '<tr>' +
                    '<td colspan="4" style="text-align:center; padding:20px; color:#0284c7; font-weight:600; font-style:italic; background:#f0f9ff; border:1px dashed #bae6fd; border-radius:6px;">' +
                        '✨ Hydrochemistry Interface Active.<br>' +
                        '<span style="font-size:11px; font-weight:normal; color:#475569;">Please click on any blue borehole marker point on the map to display its laboratory water quality metrics table.</span>' +
                    '</td>' +
                '</tr>';
        }

        // Unlink well-related widgets from the map canvas viewport completely
        setTimeout(function() {
            if (typeof search_tKey   !== 'undefined' && search_tKey)   { try { map.removeControl(search_tKey);   } catch(e){} }
            if (typeof Region_legend !== 'undefined' && Region_legend) { try { map.removeControl(Region_legend); } catch(e){} }
            if (typeof tool_tip     !== 'undefined' && tool_tip)     { try { map.removeControl(tool_tip);      } catch(e){} } 

            var residualTextBox = document.querySelector('.textBox, .leaflet-control.textBox');
            var residualLegend  = document.querySelector('.legend, .leaflet-control.legend');
            var residualInfo    = document.querySelector('.info, .leaflet-control.info');

            if (residualTextBox && residualTextBox.parentNode) { residualTextBox.parentNode.removeChild(residualTextBox); }
            if (residualLegend  && residualLegend.parentNode)  { residualLegend.parentNode.removeChild(residualLegend); }
            if (residualInfo    && residualInfo.parentNode)    { residualInfo.parentNode.removeChild(residualInfo); }
        }, 10);

        // Map vector layer visibility transactions
        if (map.hasLayer(Reion_layer)) map.removeLayer(Reion_layer);
        if (!map.hasLayer(waterChemoL)) map.addLayer(waterChemoL);
        
        console.log("✔ Context Swap Met: Hydrochemistry Matrix fully operational.");
    }
}
/**
 * 🫵 RADIO-BUTTON MOUSE INTERCEPTOR INTERFACE HOOK
 * Attaches a dynamic event listener onto Leaflet's layers container box DOM tree elements.
 * Intercepts selection events instantly before Leaflet handles them, achieving a true single-click swap.
 */
document.addEventListener("change", function(event) {
    if (event.target && event.target.type === "checkbox") {
        var labelsContainer = event.target.nextSibling;
        if (labelsContainer && labelsContainer.textContent) {
            var selectedLayerString = labelsContainer.textContent.trim();
            
            if (selectedLayerString === "Water Well Data" || selectedLayerString === "Water Quality Data") {
                // Prevent standard multi-check behaviors right away
                event.preventDefault();
                
                // Synchronize raw input properties inside the DOM tree layout container
                var parentLayerWrapper = document.querySelector('.leaflet-control-layers-overlays');
                if (parentLayerWrapper) {
                    var allCheckboxes = parentLayerWrapper.querySelectorAll('input[type="checkbox"]');
                    allCheckboxes.forEach(function(box) {
                        var boxLabel = box.nextSibling ? box.nextSibling.textContent.trim() : "";
                        box.checked = (boxLabel === selectedLayerString); // Radios selection simulation state
                    });
                }               
                // Fire unified transactional router smoothly in a single task execution step
                switchPortalLayerContext(selectedLayerString);
            }
        }
    }
});
// Programmatic fallback route helper function
function triggerProgrammaticLayerSwap(layerNameString) {
    switchPortalLayerContext(layerNameString);
}

/**
 * =========================================================================
 * ─── SIDEBAR MASTER RESET INTERFACE CONTROLLER (ERROR-FREE) ───
 * =========================================================================
 */
document.getElementById("reset_status").onclick = function() {
    console.log("Master layout reset triggered: Refreshing spatial borehole layers to default states...");

    // 1. DYNAMIC INTERFACE LOADER: Display spinning status panel right away
    if (typeof loaderID !== 'undefined' && loaderID) {
        loaderID.style.display = 'block';
    }

    // 2. TRANSACTIONAL STATE SYNC: Uncheck all competing sidebar category filters independently
    var activeRadioch  = document.querySelector('input[name="radioch"]:checked');
    var activeWatercat = document.querySelector('input[name="wellsWater"]:checked');
    var activeDepth    = document.querySelector('input[name="wellsDepth"]:checked');
    
    if (activeRadioch)  activeRadioch.checked = false;
    if (activeWatercat) activeWatercat.checked = false;
    if (activeDepth)    activeDepth.checked = false;

    // Reset the central custom WMS dropdown to clear selection states
    var wmsSelect = document.getElementById("wms_mapTrial");
    if (wmsSelect) wmsSelect.value = "";

    // 3. SPATIAL GEOMETRY CLEANUP: Wipe out modified or filtered feature paths
    if (Reion_layer) {
        Reion_layer.clearLayers();
        // Re-inject your base administrative boundary layer polygon mapping cleanly back into the stack
        if (typeof admin_layer !== 'undefined' && admin_layer) {
            Reion_layer.addLayer(admin_layer);
        }
    }

    // 4. HIGH-PERFORMANCE ASYNCHRONOUS RE-HYDRATION PASS
    // Queries your context servlet URL to download and map original raw borehole metrics points
    var baselineSyncTargetUrl = appServerContextRoot + "/acc_proNameId";

    fetch(baselineSyncTargetUrl, { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("Baseline spatial layer extraction connection failed: " + response.statusText);
            return response.json(); // Transform payload back into structured JSON data rows
        })
        .then(function(legend_data) {
            // Check gate verifies the existence of your master data loop injector function before execution
            if (typeof hold_toOverlay === 'function') {
                // Iterates through rows and internally invokes georeference_code() row-by-row automatically!
                hold_toOverlay(legend_data);
                console.log("✔ Vector data pipeline re-hydrated: Baseline borehole points successfully mapped.");
            } else {
                console.warn("Execution Alert: Master mapping iterator 'hold_toOverlay' is not declared on screen.");
            }
            
            // Turn off interface loading wheel smoothly
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
        })
        .catch(function(error) {
            console.error("Critical: Master coordinate reset lookup transaction broke down at boundary layer:", error);
            alert("System Notice: Unable to securely re-hydrate map layer coordinate metrics from the database repository.");
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'none';
        });
};

// ─── GEOSPATIAL COORDINATES RADAR WIDGET CONTROL ───
var coordsControl = L.control({ position: 'bottomleft' });
coordsControl.onAdd = function () {
    this._div = L.DomUtil.create('div', 'combined-control');
    this._div.style.background = 'rgba(15, 23, 42, 0.85)';
    this._div.style.backdropFilter = 'blur(4px)';
    this._div.style.color = '#ffffff';
    this._div.style.padding = '4px 8px';
    this._div.style.borderRadius = '4px';
    this._div.style.fontSize = '11px';
    this._div.style.fontFamily = 'monospace';
    this._div.innerHTML = 'Lat: 0.00000, Lng: 0.00000';
    return this._div;
};
coordsControl.addTo(map);

map.on('mousemove', function (e) {
    if (coordsControl._div) {
        var lat = e.latlng.lat.toFixed(5);
        var lng = e.latlng.lng.toFixed(5);
        coordsControl._div.innerHTML = 'Lat: ' + lat + ', Lng: ' + lng;
    }
});

// Configure standardized map scale bar adjustments
L.control.scale({
    position: 'bottomleft',
    maxWidth: 130,
    metric: true,
    imperial: false,
    updateWhenIdle: true
}).addTo(map);

// ─── DYNAMIC REGIONAL COUNT TOOLTIPS CONTROL ───
var tool_tip = L.control({ position: 'topleft' });
tool_tip.onAdd = function () {
    this._div = L.DomUtil.create('div', 'info');
    this._div.style.background = '#ffffff';
    this._div.style.padding = '8px 12px';
    this._div.style.border = '1px solid #cbd5e1';
    this._div.style.borderRadius = '6px';
    this._div.style.boxShadow = '0 2px 6px rgba(0,0,0,0.05)';
    this.update();
    return this._div;
};

tool_tip.update = function (props) {
    if (this._div) {
        this._div.innerHTML = '<h4 style="margin:0 0 4px 0; font-size:12px; color:#06402B;">Borehole Density Map</h4>' + 
            (props && props[0] ?
            '<span style="font-size:11px; color:#475569;">In <b>' + props[0].region_tooltip + ':</b></span><br /><b style="font-size:13px;'
            +' color:#0f172a;">' + props[0].count_proname + '</b> Wells'
            : '<span style="font-size:11px; color:#64748b;">Hover over administrative region</span>');
    }
};
tool_tip.addTo(map);

function colorCoding(d) {
    return d == grades1[0] ? '#8000ff' :
           d == grades1[1] ? '#16a34a' : // Modernized bright green hex
           d == grades1[2] ? '#0affca' :
           d == grades1[3] ? '#2563eb' : // Modernized bold blue hex
           d == grades1[4] ? '#94ffff' :
           d == grades1[5] ? '#A52A2A' :
           d == grades1[6] ? '#09b5b5' :
           d == grades1[7] ? '#dc2626' : // Modernized warning red hex
                             '#bf650b';
}

var codeColor = {
    fillColor: colorCoding(grades1)
};

// ─── DYNAMIC WELL FIELDS LEGEND COMPONENT ───
var tText = "Well Count in Field: ";
/**
 * =========================================================================
 * ─── DYNAMIC WELL FIELDS LEGEND COMPONENT WITH INTEGRATED EVENT BINDINGS ───
 * =========================================================================
 */
var Region_legend = L.control({ position: 'bottomright' });
Region_legend.onAdd = function () {
    var div = L.DomUtil.create('div', 'legend');
    div.style.background = '#ffffff';
    div.style.padding = '10px';
    div.style.border = '1px solid #cbd5e1';
    div.style.borderRadius = '6px';
    div.style.maxHeight = '220px';
    div.style.overflowY = 'auto';
    div.style.boxShadow = '0 4px 10px rgba(0,0,0,0.08)';

    var grades = grades1;
    var countAll = countAllT;
    
    // Render core layout frame header inside the widget instance scope space
    div.innerHTML = '<span id="pt_code" style="font-size:11.5px; font-weight:700; color:#475569; text-transform:uppercase; '
    +'cursor:pointer; display:block; border-bottom:1px solid #e2e8f0; padding-bottom:4px; margin-bottom:6px;"><i class="fa fa-refresh"></i> ' 
    + tText + ' (' + countAll + ')</span>';
    
    // 1. RE-BIND THE REFRESH TRIGGER EXCLUSIVELY INSIDE CONSTRUCTOR LIFECYCLE
    // This fixes the pt_code clicking crash bug after changing layers
    var refreshHeaderBtn = div.querySelector("#pt_code");
    if (refreshHeaderBtn) {
        refreshHeaderBtn.onclick = function() {
            if (typeof loaderID !== 'undefined' && loaderID) loaderID.style.display = 'block';

            // Safe unchecking sequence parameters
            var activeRadioch  = document.querySelector('input[name="radioch"]:checked');
            var activeWatercat = document.querySelector('input[name="wellsWater"]:checked');
            var activeDepth    = document.querySelector('input[name="wellsDepth"]:checked');
            
            if (activeRadioch)  activeRadioch.checked = false;
            if (activeWatercat) activeWatercat.checked = false;
            if (activeDepth)    activeDepth.checked = false;

            if (Reion_layer) {
                Reion_layer.clearLayers();
                if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
            }

            var bulkOverlayUrl = appServerContextRoot + "/acc_proNameId";
            fetch(bulkOverlayUrl, { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(legend_data) {
                    if (typeof hold_toOverlay === 'function') hold_toOverlay(legend_data);
                    if (loaderID) loaderID.style.display = 'none';
                })
                .catch(function(error) {
                    console.error("Bulk refresh failed:", error);
                    if (loaderID) loaderID.style.display = 'none';
                });
        };
    }
    // 2. GENERATE LEGEND VALUE ROWS AND ATTACH CLICK INTERCEPT LISTENERS
    for (var i = 0; i < grades.length; i++) {
        var from   = grades[i];
        var countP = countT[i];   
        var span = document.createElement("span");
        span.classList.add("legendItem");
        span.style.display = 'flex';
        span.style.alignItems = 'center';
        span.style.gap = '8px';
        span.style.fontSize = '11.5px';
        span.style.color = '#334155';
        span.style.margin = '4px 0';
        span.style.cursor = 'pointer';
        span.dataset.from = from;
        
        var colorBox = document.createElement("i");
        colorBox.style.width = '12px';
        colorBox.style.height = '12px';
        colorBox.style.display = 'inline-block';
        colorBox.style.borderRadius = '3px';
        colorBox.style.background = colorCoding(from);
        
        var labelText = document.createTextNode(from + ' (' + countP + ')');
        
        span.appendChild(colorBox);
        span.appendChild(labelText);
        
        // Immediate localized runtime listener binding
        span.addEventListener("click", function (event) {
            var targetSpan = event.currentTarget;
            var filterKey  = targetSpan.dataset.from;
            
            if (Reion_layer) {
                Reion_layer.clearLayers();
                if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
            }
            if (typeof get_legendItem === 'function') {
                get_legendItem(filterKey);
            }
        });
        
        div.appendChild(span);
    }
    return div;
};
// Add to map for initial initialization pass grid parameters
Region_legend.addTo(map);

/**
 * =========================================================================
 * ─── VECTOR WELL INDEX SEARCH BAR COMPONENT WITH AUTO-LISTENER ATTACH ───
 * =========================================================================
 */
var search_tKey = L.control({ position: 'topright' });

search_tKey.onAdd = function () {
    var div = L.DomUtil.create('div', 'textBox');
    div.innerHTML = '<input type="search" class="form-control" id="pSearchKey" placeholder="🔍 Search By Well Index..." style="height:32px; '
    +'width:220px; font-size:12px; border:1px solid #cbd5e1; border-radius:4px; padding:4px 10px; box-shadow:0 2px 6px rgba(0,0,0,0.05);">';
    
    // 🫵 EXTRACT INPUT ELEMENT AND RE-ATTACH ACTIVE EVENT RUNTIME ON THE FLY!
    // This guarantees your search box is functional every single time you switch layers!
    var searchInput = div.querySelector("#pSearchKey");
    if (searchInput) {
        searchInput.onchange = function(evnt) {
            var proName = evnt.target.value;
            
            if (Reion_layer) {
                Reion_layer.clearLayers();
                if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
            }

            var searchTargetUrl = appServerContextRoot + "/acc_proName?ProName=" + encodeURIComponent(proName);

            fetch(searchTargetUrl, { method: 'POST' })
                .then(function(response) {
                    if (!response.ok) throw new Error("Search configuration error lookup pass failed.");
                    return response.json();
                })
                .then(function(legend_data) {
                    if (typeof hold_ProjectName === 'function') hold_ProjectName(legend_data);
                })
                .catch(function(error) {
                    console.error("Index search lookup failure:", error);
                });
        };
    }
    
    return div;
};

// Add to map for initial initialization pass grid parameters
search_tKey.addTo(map);

/**
 * Resets active query states asynchronously.
 * Swapped legacy XMLHttpRequest with modern fetch API layers.
 */
document.addEventListener("DOMContentLoaded", function() {
    var triggerHeader = document.getElementById("pt_code");
    if (triggerHeader) {
        triggerHeader.onclick = function() {
            if (loaderID) loaderID.style.display = 'block';

            // Clear input radio check selection tokens safely
            var activeRadioch  = document.querySelector('input[name="radioch"]:checked');
            var activeWatercat = document.querySelector('input[name="wellsWater"]:checked');
            var activeDepth    = document.querySelector('input[name="wellsDepth"]:checked');
            
            if (activeRadioch)  activeRadioch.checked = false;
            if (activeWatercat) activeWatercat.checked = false;
            if (activeDepth)    activeDepth.checked = false;

            if (Reion_layer) {
                Reion_layer.clearLayers();
                if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
            }

            var bulkOverlayUrl = appServerContextRoot + "/acc_proNameId";
            fetch(bulkOverlayUrl, { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(legend_data) {
                    hold_toOverlay(legend_data);
                    if (loaderID) loaderID.style.display = 'none';
                })
                .catch(function(error) {
                    console.error("Bulk refresh failed:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    };
}
});

function hold_toOverlay(data_toOverlay) {
    if (!data_toOverlay) return;
    for (var j = 0; j < data_toOverlay.length; j++) {
        var item = data_toOverlay[j];
        overlay_allProjects(item.locy, item.locx, item.pro_name_ov, item.pro_code, item.Pyear, item.Id_pro, item.Gpro_type);
    }
}
/**
 * Plots comprehensive historical well pins onto layer groupings.
 * Standardized divIcon attributes to completely resolve code crashes.
 */
function overlay_allProjects(Ulaty, UlongX, Uproject_name, Uproject_code, Uproject_year, Upro_id, Uproject_type) {
    if (!Ulaty || !UlongX) return;
    
var resolvedHexColor = colorCoding(Uproject_name);

var boreholeMarkerOptions = {
        radius: 5,
        fillColor: resolvedHexColor,
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var specific_coordII = [Ulaty, UlongX];

var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Borehole Core Identity</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_name + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_code + '</td></tr>' +
                 '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Const. Year:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_year + '</td></tr>' +
                 '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + Upro_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';
                
    var stor_every_allOverlay = L.circleMarker(specific_coordII,boreholeMarkerOptions);    
    stor_every_allOverlay.bindTooltip(popupContent,{ 
		sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
        direction: 'top', 
        offset: [0, -5],
        pane:'tooltipPane'
        });
        // Handle full core records sheet data loading on click
    stor_every_allOverlay.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(Uproject_code, Upro_id);
        }
    });
   
    Reion_layer.addLayer(stor_every_allOverlay);
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 4 ───
 * =========================================================================
 */

// ─── INDEXED GEOSPATIAL VECTOR SEARCH INTERFACE ENGINE ───
var searchInputBox = document.getElementById("pSearchKey");
if (searchInputBox) {
    searchInputBox.onchange = function(evnt) {
        var proName = evnt.target.value;
        
        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        var searchTargetUrl = appServerContextRoot + "/acc_proName?ProName=" + encodeURIComponent(proName);

        fetch(searchTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Search query connection break.");
                return response.json();
            })
            .then(function(legend_data) {
                hold_ProjectName(legend_data);
            })
            .catch(function(error) {
                console.error("Index search lookup failure:", error);
            });
    };
}

function hold_ProjectName(data_toOverlay) {
    if (!data_toOverlay) return;
    // FIXED: Enforced strict var keyword scoping boundary protections
    for (var j = 0; j < data_toOverlay.length; j++) {
        var item = data_toOverlay[j];
        overlay_ProjectName(item.locy, item.locx, item.pro_name_ov, item.pro_code, item.Pyear, item.Id_pro, item.Gpro_type);
    }
}

function overlay_ProjectName(Ulaty, UlongX, Uproject_name, Uproject_code, Uproject_year, Upro_id, Uproject_type) {
    if (!Ulaty || !UlongX) return;
    
    var resolvedHexColor = colorCoding(Uproject_name);
    var specific_coordII = [Ulaty, UlongX];
    
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: resolvedHexColor,
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };

var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Located Borehole Profile</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_name + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_code + '</td></tr>' +
                 '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Const. Year:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_year + '</td></tr>' +
                 '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + Upro_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    // FIXED: Stripped away nested array index loops duplication
    var markerInstanceSearch = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceSearch.bindTooltip(popupContent, {
		sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
        direction: 'top', 
        offset: [0, -5],
        pane:'tooltipPane'
	 });
        // Handle full core records sheet data loading on click
    markerInstanceSearch.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(Uproject_code, Upro_id);
        }
    });
    Reion_layer.addLayer(markerInstanceSearch);
}

// ─── CORE ATTRIBUTES MULTI-QUERY FILTER CHECKS INTERFACE ───
var functionalQueryInput = document.getElementById("userQueryId");
if (functionalQueryInput) {
    functionalQueryInput.onchange = function(evnt) {
        if (loaderID) loaderID.style.display = 'block';
        var nonc_id = evnt.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Auto-uncheck sibling criteria targets to preserve spatial map layout sync integrity
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        var selectedWellDepth   = document.querySelector('input[name="wellsDepth"]:checked');
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (selectedWellDepth)   selectedWellDepth.checked = false;

        var parametersFilterUrl = appServerContextRoot + "/accessWellBy_parameters?param_name=" + encodeURIComponent(nonc_id);

        fetch(parametersFilterUrl, { method: 'POST' })
            .then(function(response) { return response.json(); })
            .then(function(wellProfile) {
                wellProfileAccess(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Attributes filter processing exception:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    };
}

function wellProfileAccess(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var wellNode = getdata[i1];
        storeWellInfo4Query(wellNode.well_idParam, wellNode.lat4Query, wellNode.lng4Query, wellNode.wellIndex4Query, 
        wellNode.wellField4Query,wellNode.year);
    }
}

function storeWellInfo4Query(well_id, Ulaty, UlongX, well_name, wellfield4Query,Uproject_year) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };

var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Matching Filter Result</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                 '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Const. Year:</td><td style="color:#0f172a; font-weight:600;">' + Uproject_year + '</td></tr>' +
                 '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';


    var markerInstanceQuery = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceQuery.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane'
	 });
        // Handle full core records sheet data loading on click
    markerInstanceQuery.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(Uproject_code, Upro_id);
        }
    });
    
    Reion_layer.addLayer(markerInstanceQuery);
}

// ─── AMBIENT CRITERIA: NON-FUNCTIONAL BOREHOLES DATA TRACKS ───
var abandonedQueryInput = document.getElementById("userQueryId3");
if (abandonedQueryInput) {
    abandonedQueryInput.onchange = function(evnt) {
        if (loaderID) loaderID.style.display = 'block';
        var nonc_id = evnt.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean dependent dashboard metrics filters
        var Transmissivity      = document.querySelector('input[name="Transmissivity"]:checked');
        var currentDWL          = document.querySelector('input[name="DWL"]:checked');
        var currentSWL          = document.querySelector('input[name="SWL"]:checked');
        var Discharge           = document.querySelector('input[name="Discharge"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        var selectedWellDepth   = document.querySelector('input[name="wellsDepth"]:checked');
        
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (selectedWellDepth)   selectedWellDepth.checked = false;
        if (Discharge)           Discharge.checked = false;
        if (currentSWL)          currentSWL.checked = false;
        if (currentDWL)          currentDWL.checked = false;
        if (Transmissivity)      Transmissivity.checked = false;

        var nonFunctionFilterUrl = appServerContextRoot + "/accessWellBy_parameters?param_name=" + encodeURIComponent(nonc_id);

        fetch(nonFunctionFilterUrl, { method: 'POST' })
            .then(function(response) { return response.json(); })
            .then(function(wellProfile) {
                wellProfileAccessNonFunction(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Non-functional pipeline failure:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    };
}
/**
 * Processes and maps non-functional/abandoned well vector points.
 */
function wellProfileAccessNonFunction(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var wellNode = getdata[i1];
        storeWellInfo4NonFunction(wellNode.well_idParam, wellNode.lat4Query, wellNode.lng4Query, wellNode.wellIndex4Query, wellNode.wellField4Query);
    }
}

/**
 * Plots non-functional/abandoned borehole markers with popup layout bindings.
 */
function storeWellInfo4NonFunction(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;
    var specific_coordII = [Ulaty, UlongX];
    
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };

var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Abandoned Well Profile</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';


    var markerInstanceNonFun = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceNonFun.bindTooltip(popupContent, { 
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane'});

        // Handle full core records sheet data loading on click
    markerInstanceNonFun.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });
    
    Reion_layer.addLayer(markerInstanceNonFun);
}

/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 5 ───
 * =========================================================================
 */

// Helper utility to safely uncheck all cross-filtering parameters across categories
function resetAllCrossQueryRadioTokens() {
    var radioSelectors = [
        'input[name="Transmissivity"]:checked',
        'input[name="DWL"]:checked',
        'input[name="SWL"]:checked',
        'input[name="Discharge"]:checked',
        'input[name="radioch"]:checked',
        'input[name="wellsDepth"]:checked',
        'input[name="wellsWater"]:checked'
    ];
    
    radioSelectors.forEach(function(selector) {
        var activeRadio = document.querySelector(selector);
        if (activeRadio) activeRadio.checked = false;
    });
}

// ─── OWNER CATEGORY SELECTION EVENT HANDLERS (class=".sKey2") ───
document.querySelectorAll('.sKey2').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        if (spinner) {
            spinner.classList.remove('spinner-hidden');
            spinner.classList.add('spinner-visible');
        }

        var selectedOwnerCatName = event.target.value;

        // Clear existing map vector points
        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean sibling search elements except the active element's own row context selection
        var currentWellDepth = document.querySelector('input[name="wellsDepth"]:checked');
        var selectedValue    = document.querySelector('input[name="radioch"]:checked');
        var Discharge        = document.querySelector('input[name="Discharge"]:checked');
        var currentSWL        = document.querySelector('input[name="SWL"]:checked');
        var currentDWL        = document.querySelector('input[name="DWL"]:checked');
        var Transmissivity   = document.querySelector('input[name="Transmissivity"]:checked');

        if (selectedValue)    selectedValue.checked = false;
        if (currentWellDepth)  currentWellDepth.checked = false;
        if (Discharge)        Discharge.checked = false;
        if (currentSWL)        currentSWL.checked = false;
        if (currentDWL)        currentDWL.checked = false;
        if (Transmissivity)    Transmissivity.checked = false;

        var ownerQueryTargetUrl = appServerContextRoot + "/accessByOwnerCatname?param_name=" + encodeURIComponent(selectedOwnerCatName);

        // Thread-isolated promise engine replaces volatile global XMLHttp components natively
        fetch(ownerQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Owner category query connection error.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileAccessAbandoned(wellProfile);
                
                if (loaderID) loaderID.style.display = 'none';
                if (spinner) {
                    spinner.classList.remove('spinner-visible');
                    spinner.classList.add('spinner-hidden');
                }
            })
            .catch(function(error) {
                console.error("Failure running database owner filtering matrix:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    });
});

function wellProfileAccessAbandoned(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4Abandoned(node.well_ownerNameID, node.lat4Query_ownerName, node.lng4Query_ownerName, node.wellIndex4Query_ownerName, node.wellField4Query_ownerName, node.wellCount);
    }
}

function storeWellInfo4Abandoned(well_id, Ulaty, UlongX, well_name, wellfield4Query, countWellcategory) {
    if (!Ulaty || !UlongX) return;
    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Owner Category</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    // FIXED: Stripped out redundant index duplicator loops
    var markerInstanceOwner = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceOwner.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane'
	 });
    
   // Handle full core records sheet data loading on click
    markerInstanceOwner.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });

    Reion_layer.addLayer(markerInstanceOwner);     
}

// ─── WELL DEPTH RANGE SELECTION EVENT HANDLERS (class=".sKey3") ───
document.querySelectorAll('.sKey3').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        if (spinner) {
            spinner.classList.remove('spinner-hidden');
            spinner.classList.add('spinner-visible');
        }

        var selectedDepthRangeValue = event.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean sibling filters across categories to maintain sync stability
        var Transmissivity      = document.querySelector('input[name="Transmissivity"]:checked');
        var currentDWL          = document.querySelector('input[name="DWL"]:checked');
        var currentSWL          = document.querySelector('input[name="SWL"]:checked');
        var Discharge           = document.querySelector('input[name="Discharge"]:checked');
        var selectedValue       = document.querySelector('input[name="radioch"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        
        if (selectedValue)       selectedValue.checked = false;
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (Discharge)           Discharge.checked = false;
        if (currentSWL)          currentSWL.checked = false;
        if (currentDWL)          currentDWL.checked = false;
        if (Transmissivity)      Transmissivity.checked = false;

        var depthRangeTargetUrl = appServerContextRoot + "/accessByWellDepthByRange?param_name=" + encodeURIComponent(selectedDepthRangeValue);

        fetch(depthRangeTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Depth category request configuration error.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByWellDepth(wellProfile);
                
                if (loaderID) loaderID.style.display = 'none';
                if (spinner) {
                    spinner.classList.remove('spinner-visible');
                    spinner.classList.add('spinner-hidden');
                }
            })
            .catch(function(error) {
                console.error("Failure running database depth range filter pipeline:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    });
});

function wellProfileByWellDepth(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4WellDepthRange(node.well_DepthID, node.lat4Query_DepthRange, node.lng4Query_DepthRange, node.wellIndex4Query_DepthRange, node.wellField4Query_DepthRange);
    }
}

function storeWellInfo4WellDepthRange(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

   var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Depth Range Filter Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';
   

    var markerInstanceDepth = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceDepth.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
         
    // Handle full core records sheet data loading on click
    markerInstanceDepth.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });

    Reion_layer.addLayer(markerInstanceDepth);
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 6 ───
 * =========================================================================
 */

// ─── ACTIVE WELL METRICS FILTER SELECTION (id="userQueryId1") ───
var activeQueryInput = document.getElementById("userQueryId1");
if (activeQueryInput) {
    activeQueryInput.onchange = function(evnt) {
        if (loaderID) loaderID.style.display = 'block';
        if (spinner) {
            spinner.classList.remove('spinner-hidden');
            spinner.classList.add('spinner-visible');
        }

        var activeId = evnt.target.value;
        var functionalBaseNode = document.getElementById("userQueryId");
        var functionId = functionalBaseNode ? functionalBaseNode.value : "Functional";

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        var activeFilterUrl = appServerContextRoot + "/accessWellBy_funcActive?param_name=" + 
                              encodeURIComponent(functionId) + "&activeStatus=" + encodeURIComponent(activeId);

        fetch(activeFilterUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Active well query connection breach.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileAccessByFunActive(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
                if (spinner) {
                    spinner.classList.remove('spinner-visible');
                    spinner.classList.add('spinner-hidden');
                }
            })
            .catch(function(error) {
                console.error("Active well filtering exception:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    };
}

function wellProfileAccessByFunActive(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4FunctionStatusQuery(node.well_idParam, node.lat4Query, node.lng4Query, node.wellIndex4Query, node.wellField4Query);
    }
}

function storeWellInfo4FunctionStatusQuery(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];
    
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Active Operational Well</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceActive = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceActive.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceActive.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });

    Reion_layer.addLayer(markerInstanceActive);           
};

// ─── INACTIVE WELL METRICS FILTER SELECTION (id="userQueryId2") ───
var inactiveQueryInput = document.getElementById("userQueryId2");
if (inactiveQueryInput) {
    inactiveQueryInput.onchange = function(evnt) {
        if (loaderID) loaderID.style.display = 'block';
        var activeId = evnt.target.value;
        var functionalBaseNode = document.getElementById("userQueryId");
        var functionId = functionalBaseNode ? functionalBaseNode.value : "Functional";

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        var inactiveFilterUrl = appServerContextRoot + "/accessWellBy_funcActive?param_name=" + 
                                encodeURIComponent(functionId) + "&activeStatus=" + encodeURIComponent(activeId);

        fetch(inactiveFilterUrl, { method: 'POST' })
            .then(function(response) { return response.json(); })
            .then(function(wellProfile) {
                wellProfileAccessByFuninActive(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Inactive well filtering exception:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    };
}

function wellProfileAccessByFuninActive(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4FunctionStatusInA(node.well_idParam, node.lat4Query, node.lng4Query, node.wellIndex4Query, node.wellField4Query);
    }
}

function storeWellInfo4FunctionStatusInA(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

        var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Inactive Standby Well</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceInactive = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceInactive.bindTooltip(popupContent, {  
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
 // Handle full core records sheet data loading on click
    markerInstanceInactive.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });
    Reion_layer.addLayer(markerInstanceInactive);           
}

// ─── CURRENT DISCHARGE YIELD METRICS VECTOR LOG (class=".sKeyQ") ───
document.querySelectorAll('.sKeyQ').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        var nonc_id = event.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean sibling active filters to prevent dashboard view desynchronization
        var Transmissivity      = document.querySelector('input[name="Transmissivity"]:checked');
        var currentDWL          = document.querySelector('input[name="DWL"]:checked');
        var currentSWL          = document.querySelector('input[name="SWL"]:checked');
        var wellsDepth          = document.querySelector('input[name="wellsDepth"]:checked');
        var selectedValue       = document.querySelector('input[name="radioch"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        
        if (selectedValue)       selectedValue.checked = false;
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (wellsDepth)          wellsDepth.checked = false;
        if (currentSWL)          currentSWL.checked = false;
        if (currentDWL)          currentDWL.checked = false;
        if (Transmissivity)      Transmissivity.checked = false;

        var dischargeFilterUrl = appServerContextRoot + "/accessByDischargeRange?param_name=" + encodeURIComponent(nonc_id);

        fetch(dischargeFilterUrl, { method: 'POST' })
            .then(function(response) {
            if (!response.ok) throw new Error("Discharge filter layer connection error.");
            return response.json();
        })
        .then(function(wellProfile) {
            wellProfileByDischargeRate(wellProfile);
            if (loaderID) loaderID.style.display = 'none';
        })
        .catch(function(error) {
            console.error("Discharge analytics processing exception:", error);
            if (loaderID) loaderID.style.display = 'none';
        });
    });
});

/**
 * Iterates over the filtered well data array.
 * Maps keys safely to prevent undefined rendering errors.
 */
function wellProfileByDischargeRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4WellDischargeRate(
            node.well_DischargeID, 
            node.lat4Query_DischargeRange, 
            node.lng4Query_DischargeRange, 
            node.wellIndex4Query_DischargeRange, 
            node.wellField4Query_DischargeRange
        );
    }
}

/**
 * Plots circular marker pins for discharge results onto the Reion_layer group.
 * Explicitly binds an interactive tabular dashboard layout inside the popup.
 */
function storeWellInfo4WellDischargeRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;
    
    var specific_coordII = [Ulaty, UlongX];
    
var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Discharge Filter Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';


    var markerInstanceDischarge = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceDischarge.bindTooltip(popupContent, { 
	     sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
 // Handle full core records sheet data loading on click
    markerInstanceDischarge.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });
    
    Reion_layer.addLayer(markerInstanceDischarge);
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 7 ───
 * =========================================================================
 */

// ─── AMBIENT CRITERIA: STATIC WATER LEVEL RANGE CONTROLS (class=".sKeySWL") ───
document.querySelectorAll('.sKeySWL').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        var selectedSwlRangeValue = event.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean sibling interactive attributes filters across categories
        var Transmissivity      = document.querySelector('input[name="Transmissivity"]:checked');
        var currentDWL          = document.querySelector('input[name="DWL"]:checked');
        var Discharge           = document.querySelector('input[name="Discharge"]:checked');
        var wellsDepth          = document.querySelector('input[name="wellsDepth"]:checked');
        var selectedValue       = document.querySelector('input[name="radioch"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        
        if (selectedValue)       selectedValue.checked = false;
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (wellsDepth)          wellsDepth.checked = false;
        if (Discharge)           Discharge.checked = false;
        if (currentDWL)          currentDWL.checked = false;
        if (Transmissivity)      Transmissivity.checked = false;

        var swlQueryTargetUrl = appServerContextRoot + "/accessBySWLRange?param_name=" + encodeURIComponent(selectedSwlRangeValue);

        fetch(swlQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("SWL matrix data extraction connection error.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileBySWLRate(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Failure running database SWL filtering pipeline:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    });
});

function wellProfileBySWLRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4SWLRate(node.well_SWLID, node.lat4Query_SWLRange, node.lng4Query_SWLRange, node.wellIndex4Query_SWLRange, node.wellField4Query_SWLRange);
    }
}

function storeWellInfo4SWLRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];
        
var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i>SWL Level Filter Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceSwl = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceSwl.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
 // Handle full core records sheet data loading on click
    markerInstanceSwl.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });

    Reion_layer.addLayer(markerInstanceSwl);           
}

// ─── AMBIENT CRITERIA: DYNAMIC WATER LEVEL RANGE CONTROLS (class=".sKeyDWL") ───
document.querySelectorAll('.sKeyDWL').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        var selectedDwlRangeValue = event.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean dependent attribute filters to safeguard workflow state mapping
        var Transmissivity      = document.querySelector('input[name="Transmissivity"]:checked');
        var currentSWL          = document.querySelector('input[name="SWL"]:checked');
        var Discharge           = document.querySelector('input[name="Discharge"]:checked');
        var wellsDepth          = document.querySelector('input[name="wellsDepth"]:checked');
        var selectedValue       = document.querySelector('input[name="radioch"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        
        if (selectedValue)       selectedValue.checked = false;
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (wellsDepth)          wellsDepth.checked = false;
        if (Discharge)           Discharge.checked = false;
        if (currentSWL)          currentSWL.checked = false;
        if (Transmissivity)      Transmissivity.checked = false;

        var dwlQueryTargetUrl = appServerContextRoot + "/accessByDWLRange?param_name=" + encodeURIComponent(selectedDwlRangeValue);

        fetch(dwlQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("DWL analytical matrix extraction failure.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByDWLRate(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Failure running database DWL filtering pipeline:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    });
});

function wellProfileByDWLRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4DWLRate(node.well_DWLID, node.lat4Query_DWLRange, node.lng4Query_DWLRange, node.wellIndex4Query_DWLRange, node.wellField4Query_DWLRange);
    }
}

function storeWellInfo4DWLRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i>DWL Level Filter Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceDwl = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceDwl.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
 // Handle full core records sheet data loading on click
    markerInstanceDwl.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });
    
    Reion_layer.addLayer(markerInstanceDwl);           
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 8 ───
 * =========================================================================
 */

// ─── TRANSMISSIVITY RANGE FILTER SELECTION (class=".sKeyTRMT") ───
document.querySelectorAll('.sKeyTRMT').forEach(function(element) {
    element.addEventListener('change', function(event) {
        if (loaderID) loaderID.style.display = 'block';
        var selectedTrmtValue = event.target.value;

        if (Reion_layer) {
            Reion_layer.clearLayers();
            if (typeof admin_layer !== 'undefined') Reion_layer.addLayer(admin_layer);
        }

        // Clean sibling interactive attributes filters across categories
        var currentDWL          = document.querySelector('input[name="DWL"]:checked');
        var currentSWL          = document.querySelector('input[name="SWL"]:checked');
        var Discharge           = document.querySelector('input[name="Discharge"]:checked');
        var wellsDepth          = document.querySelector('input[name="wellsDepth"]:checked');
        var selectedValue       = document.querySelector('input[name="radioch"]:checked');
        var selectedWellNameCat = document.querySelector('input[name="wellsWater"]:checked');
        
        if (selectedValue)       selectedValue.checked = false;
        if (selectedWellNameCat) selectedWellNameCat.checked = false;
        if (wellsDepth)          wellsDepth.checked = false;
        if (Discharge)           Discharge.checked = false;
        if (currentSWL)          currentSWL.checked = false;
        if (currentDWL)          currentDWL.checked = false;

        var trmtQueryTargetUrl = appServerContextRoot + "/accessByTRMTRange?param_name=" + encodeURIComponent(selectedTrmtValue);

        fetch(trmtQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Transmissivity data extraction connection error.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByTRMTRate(wellProfile);
                if (loaderID) loaderID.style.display = 'none';
            })
            .catch(function(error) {
                console.error("Failure running Transmissivity filter pipeline:", error);
                if (loaderID) loaderID.style.display = 'none';
            });
    });
});

function wellProfileByTRMTRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4TRMTRate(node.well_TRMTID, node.lat4Query_TRMTRange, node.lng4Query_TRMTRange, node.wellIndex4Query_TRMTRange, node.wellField4Query_TRMTRange);
    }
}

function storeWellInfo4TRMTRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i>Transmissivity Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Get Completion Report</a></div>';

    var markerInstanceTrmt = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceTrmt.bindTooltip(popupContent, {
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
 // Handle full core records sheet data loading on click
    markerInstanceTrmt.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getElementByIdCustome === 'function') {
            getElementByIdCustome(well_name, well_id);
        }
    });

    Reion_layer.addLayer(markerInstanceTrmt);           
}

// ─── LEGEND-BASED SELECTION ITEM GROUPS LINK INTERFACE ───
function get_legendItem(legend_name) {
    if (spinner) {
        spinner.classList.remove('sidebar-collapsed');
        spinner.classList.remove('spinner-hidden');
        spinner.classList.add('spinner-visible');
    }
    if (loaderID) loaderID.style.display = 'block';
    
    var checkedNode = document.querySelector('input[name="radioch"]:checked');
    var selectedValueNode = checkedNode ? checkedNode.value : undefined;


    var legendQueryUrl = appServerContextRoot + "/acc_Legend?leg_name=" + encodeURIComponent(legend_name) 
    + "&checkList=" + encodeURIComponent(selectedValueNode);

    fetch(legendQueryUrl, { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("Legend data extraction connection failure.");
            return response.json();
        })
        .then(function(legend_data) {
            get_latlong_by_legend_filter(legend_data);
            
            if (loaderID) loaderID.style.display = 'none';
            if (spinner) {
                spinner.classList.remove('spinner-visible');
                spinner.classList.add('spinner-hidden');
            }
        })
        .catch(function(error) {
            console.error("Legend filtering processing exception:", error);
            if (loaderID) loaderID.style.display = 'none';
        });
}

// ─── HYDROCHEMISTRY PARAMETERS: TDS RANGE SELECTION CONTROLS (class=".sKeyTDS") ───
document.querySelectorAll('.sKeyTDS').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedTdsValue = event.target.value;

        // Clean sibling active water quality parameters filters to safeguard layout view state
        var electricC   = document.querySelector('input[name="EC"]:checked');
        var Fluoride    = document.querySelector('input[name="Fluoride"]:checked');
        var Iron        = document.querySelector('input[name="Iron"]:checked');
        var Nitrate     = document.querySelector('input[name="Nitrate"]:checked');
        var Temperature = document.querySelector('input[name="Temperature"]:checked');
        
        if (Temperature) Temperature.checked = false;
        if (Nitrate)     Nitrate.checked = false;
        if (Iron)        Iron.checked = false;
        if (Fluoride)    Fluoride.checked = false;
        if (electricC)   electricC.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var tdsQueryTargetUrl = appServerContextRoot + "/accessBYTDS?param_name=" + encodeURIComponent(selectedTdsValue);

        fetch(tdsQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry TDS query connection breakdown.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByTDSRate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database TDS filtering pipeline:", error);
            });
    });
});

function wellProfileByTDSRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4TDSRate(node.well_TDSID, node.lat_TDS, node.lng_TDS, node.wellIndex_TDS, node.wellField_TDS);
    }
}

function storeWellInfo4TDSRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;
    var specific_coordII = [Ulaty, UlongX];
    
var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i>TDS Filter Match Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';

    var markerInstanceTds = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    
    // Bind the premium popup dashboard content to the marker instance
    markerInstanceTds.bindTooltip(popupContent, { 
         sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceTds.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    // Commit the complete interactive marker safely back to the chemistry overlay group
    waterChemoL.addLayer(markerInstanceTds);
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 9 ───
 * =========================================================================
 */

// ─── HYDROCHEMISTRY PARAMETERS: ELECTRICAL CONDUCTIVITY FILTERS (class=".sKeyEC") ───
document.querySelectorAll('.sKeyEC').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedEcValue = event.target.value;

        // Clean sibling active water quality parameters filters to safeguard layout view state
        var totalDS     = document.querySelector('input[name="TDS"]:checked');
        var Fluoride    = document.querySelector('input[name="Fluoride"]:checked');
        var Iron        = document.querySelector('input[name="Iron"]:checked');
        var Nitrate     = document.querySelector('input[name="Nitrate"]:checked');
        var Temperature = document.querySelector('input[name="Temperature"]:checked');
        
        if (Temperature) Temperature.checked = false;
        if (Nitrate)     Nitrate.checked = false;
        if (Iron)        Iron.checked = false;
        if (Fluoride)    Fluoride.checked = false;
        if (totalDS)     totalDS.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var ecQueryTargetUrl = appServerContextRoot + "/accessBYEC?param_name=" + encodeURIComponent(selectedEcValue);

        fetch(ecQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry EC query connection failure.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByECRate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database EC filtering pipeline:", error);
            });
    });
});

function wellProfileByECRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfo4ECRate(node.well_ECID, node.lat_EC, node.lng_EC, node.wellIndex_EC, node.wellField_EC);
    }
}

function storeWellInfo4ECRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> EC Filter Match Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';

    var markerInstanceEc = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceEc.bindTooltip(popupContent, { 
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceEc.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    
    waterChemoL.addLayer(markerInstanceEc);       
}

// ─── HYDROCHEMISTRY PARAMETERS: FLUORIDE RANGE SELECTION CONTROLS (class=".sKeyF") ───
document.querySelectorAll('.sKeyF').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedFValue = event.target.value;

        // Clean sibling active water quality parameters filters to safeguard layout view state
        var totalDS    = document.querySelector('input[name="TDS"]:checked');
        var electricCo = document.querySelector('input[name="EC"]:checked');
        var Iron       = document.querySelector('input[name="Iron"]:checked');
        var Nitrate    = document.querySelector('input[name="Nitrate"]:checked');
        var Temperature = document.querySelector('input[name="Temperature"]:checked');
        
        if (Temperature) Temperature.checked = false;
        if (Nitrate)     Nitrate.checked = false;
        if (Iron)        Iron.checked = false;
        if (electricCo)  electricCo.checked = false;
        if (totalDS)     totalDS.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var fQueryTargetUrl = appServerContextRoot + "/accessBYFlouride?param_name=" + encodeURIComponent(selectedFValue);

        fetch(fQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry Fluoride query connection breakdown.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByFRate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database Fluoride filtering pipeline:", error);
            });
    });
});

function wellProfileByFRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfoFRate(node.well_FID, node.lat_F, node.lng_F, node.wellIndex_F, node.wellField_F);
    }
}

function storeWellInfoFRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Fluoride (F⁻) Match Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';

    var markerInstanceF = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceF.bindTooltip(popupContent, { 
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceF.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    
    waterChemoL.addLayer(markerInstanceF);       
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 10 ───
 * =========================================================================
 */

// ─── HYDROCHEMISTRY PARAMETERS: IRON FILTERS (class=".sKeyFe") ───
document.querySelectorAll('.sKeyFe').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedFeValue = event.target.value;

        // Clean sibling active water quality parameters filters to safeguard layout view state
        var totalDS     = document.querySelector('input[name="TDS"]:checked');
        var electricCo  = document.querySelector('input[name="EC"]:checked');
        var Fluoride    = document.querySelector('input[name="Fluoride"]:checked');
        var Nitrate     = document.querySelector('input[name="Nitrate"]:checked');
        var Temperature = document.querySelector('input[name="Temperature"]:checked');
        
        if (Temperature) Temperature.checked = false;
        if (Nitrate)     Nitrate.checked = false;
        if (Fluoride)    Fluoride.checked = false;
        if (electricCo)  electricCo.checked = false;
        if (totalDS)     totalDS.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var feQueryTargetUrl = appServerContextRoot + "/accessBYIron?param_name=" + encodeURIComponent(selectedFeValue);

        fetch(feQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry Iron query connection failure.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByFeRate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database Iron filtering pipeline:", error);
            });
    });
});

function wellProfileByFeRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfoFeRate(node.well_FeID, node.lat_Fe, node.lng_Fe, node.wellIndex_Fe, node.wellField_Fe);
    }
}

function storeWellInfoFeRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];
    
    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Iron (Fe) Filter Match Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';


    var markerInstanceFe = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceFe.bindTooltip(popupContent, { 
		 sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceFe.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    
    waterChemoL.addLayer(markerInstanceFe);       
}

// ─── HYDROCHEMISTRY PARAMETERS: NITRATE RANGE SELECTION CONTROLS (class=".sKeyNo3") ───
document.querySelectorAll('.sKeyNo3').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedNo3Value = event.target.value;

        // Clean sibling active water quality parameters filters to safeguard layout view state
        var totalDS    = document.querySelector('input[name="TDS"]:checked');
        var electricCo = document.querySelector('input[name="EC"]:checked');
        var Fluoride   = document.querySelector('input[name="Fluoride"]:checked');
        var Iron       = document.querySelector('input[name="Iron"]:checked');
        var Temperature = document.querySelector('input[name="Temperature"]:checked');
        
        if (Temperature) Temperature.checked = false;
        if (Iron)        Iron.checked = false;
        if (Fluoride)    Fluoride.checked = false;
        if (electricCo)  electricCo.checked = false;
        if (totalDS)     totalDS.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var no3QueryTargetUrl = appServerContextRoot + "/accessBYNitrate?param_name=" + encodeURIComponent(selectedNo3Value);

        fetch(no3QueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry Nitrate query connection breakdown.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileByNo3Rate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database Nitrate filtering pipeline:", error);
            });
    });
});

function wellProfileByNo3Rate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfoNo3Rate(node.well_No3ID, node.lat_No3, node.lng_No3, node.wellIndex_No3, node.wellField_No3);
    }
}

function storeWellInfoNo3Rate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;

    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Nitrate (NO₃⁻) Match Log</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';

    var markerInstanceNo3 = L.circleMarker(specific_coordII,boreholeMarkerOptions);
    markerInstanceNo3.bindTooltip(popupContent, { 
         sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
    markerInstanceNo3.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    
    waterChemoL.addLayer(markerInstanceNo3);       
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 11 ───
 * =========================================================================
 */

// ─── HYDROCHEMISTRY PARAMETERS: TEMPERATURE RANGE CONTROLS (class=".sKeytemp") ───
document.querySelectorAll('.sKeytemp').forEach(function(element) {
    element.addEventListener('change', function(event) {
        var selectedTempValue = event.target.value;

        // Clean sibling active water quality parameters filters independently to guarantee view state sync
        var totalDS    = document.querySelector('input[name="TDS"]:checked');
        var electricCo = document.querySelector('input[name="EC"]:checked');
        var Fluoride   = document.querySelector('input[name="Fluoride"]:checked');
        var Iron       = document.querySelector('input[name="Iron"]:checked');
        var Nitrate    = document.querySelector('input[name="Nitrate"]:checked');
        
        if (Nitrate)    Nitrate.checked = false;
        if (Iron)       Iron.checked = false;
        if (Fluoride)   Fluoride.checked = false;
        if (electricCo) electricCo.checked = false;
        if (totalDS)    totalDS.checked = false;

        if (waterChemoL) {
            waterChemoL.clearLayers();
            if (typeof waterChemlement !== 'undefined') waterChemoL.addLayer(waterChemlement);
        }

        var tempQueryTargetUrl = appServerContextRoot + "/accessBYTemprature?param_name=" + encodeURIComponent(selectedTempValue);

        fetch(tempQueryTargetUrl, { method: 'POST' })
            .then(function(response) {
                if (!response.ok) throw new Error("Hydrochemistry Temperature query network failure.");
                return response.json();
            })
            .then(function(wellProfile) {
                wellProfileBytempRate(wellProfile);
            })
            .catch(function(error) {
                console.error("Failure running database Temperature filtering pipeline:", error);
            });
    });
});

function wellProfileBytempRate(getdata) {
    if (!getdata) return;
    for (var i1 = 0; i1 < getdata.length; i1++) {
        var node = getdata[i1];
        storeWellInfotempRate(node.well_tempID, node.lat_temp, node.lng_temp, node.wellIndex_temp, node.wellField_temp);
    }
}

function storeWellInfotempRate(well_id, Ulaty, UlongX, well_name, wellfield4Query) {
    if (!Ulaty || !UlongX) return;
    console.log('Temp Data '+well_name);
    var specific_coordII = [Ulaty, UlongX];

    var boreholeMarkerOptions = {
        radius: 5,
        fillColor: "#06402B",
        color: "#ffffff",
        weight: 1.5,
        opacity: 1,
        fillOpacity: 0.85,
        pane: 'boreholePane' // LOCKED PATH: Binds circle directly into our high-priority pane
    };
    var popupContent = 
        '<div style="font-family:\'Segoe UI\',sans-serif; min-width:220px; padding:4px;">' +
            '<div style="font-weight:bold; font-size:12px; color:#06402B; border-bottom:1px solid #cbd5e1; padding-bottom:4px; margin-bottom:6px;">' 
            +'<i class="fa fa-tint"></i> Temperature Filter Match</div>' +
            '<table style="width:100%; font-size:11px; border-collapse:collapse; margin-bottom:6px;">' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Field:</td><td style="color:#0f172a; font-weight:600;">' + wellfield4Query + '</td></tr>' +
                '<tr><td style="font-weight:600; color:#475569; padding:2px 0;">Well Index:</td><td style="color:#0f172a; font-weight:600;">' + well_name + '</td></tr>' +
                '</table>' +
            '<a href="' + appServerContextRoot + '/DownloadBoreholeReport?wellId=' + well_id + '" style="display:flex; align-items:center; '
            +'justify-content:center;'
            +' gap:6px; width:100%; height:26px; background:#06402B; color:#ffffff !important; font-weight:bold; text-decoration:none; font-size:11px;'
            +' border-radius:4px; text-align:center; box-shadow:0 1px 3px rgba(0,0,0,0.1);">' +
                '<i class="fa fa-download"></i> Access Chemistry Trends</a></div>';

    var markerInstanceTemp = L.circleMarker(specific_coordII, boreholeMarkerOptions);
    markerInstanceTemp.bindTooltip(popupContent, { 
         sticky: true,       // Snaps tooltip tracking strictly to your mouse cursor coordinates
         direction: 'top', 
         offset: [0, -5],
         pane:'tooltipPane' });
    
 // Handle full core records sheet data loading on click
        markerInstanceTemp.on('click', function(e) {
        L.DomEvent.stopPropagation(e); // Stop event bubbling to prevent background region zoom triggers
        if (typeof getWellChemistryByWellId === 'function') {
            getWellChemistryByWellId(well_name, well_id);
        }
    });
    
    waterChemoL.addLayer(markerInstanceTemp);       
}

/**
 * ==================================================================================
 * ─── UNIFIED BOREHOLE SUMMARY ANALYTICS ENGINE (SIDEBAR DECOUPLED MODULE) ───
 * ==================================================================================
 * Asynchronously queries the backend database query pool and injects live count metrics 
 * directly into your sidebar accordion label nodes. Fully decoupled from map controls.
 */
function refreshBoreholeSummaryWidgetMetrics() {
    console.log("Synchronizing real-time groundwater borehole inventory metrics to sidebar...");

    fetch("FetchWellSummaryCounts", { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("Metrics data pipeline connection failure.");
            return response.json();
        })
        .then(function(data) {
            // ─── STEP 1: CACHE THE TARGET SIDEBAR ELEMENT HANDLES ───
            var functionalCount    = document.getElementById("functionalCount");
            var activeCount        = document.getElementById("activeCount");
            var activeWell         = document.getElementById("InactiveCount");
            var AbandonedCount     = document.getElementById("AbandonedCount");
            
            var aawsaWells         = document.getElementById("aawsaCount");
            var governmantWells    = document.getElementById("governmentCount");
            var nonGovernmantWells = document.getElementById("nonGovenCount");
            var privateWells       = document.getElementById("privateCount");
            
            var B100Wells          = document.getElementById("B100");
            var BWN350Wells        = document.getElementById("BWN350");
            var BWN500Wells        = document.getElementById("BWN500");
            var A500Wells          = document.getElementById("A500");

            // ─── STEP 2: TRANSACTIONAL IN-PLACE VALUE RE-HYDRATION PASS ───
            // We use a safe verification gate to ensure node labels update without duplicate compounding text loops
            if (functionalCount)    { updateSidebarNodeText(functionalCount, data.Function); }
            if (activeCount)        { updateSidebarNodeText(activeCount, data.Active); }
            if (activeWell)         { updateSidebarNodeText(activeWell, data.InActive); }
            if (AbandonedCount)     { updateSidebarNodeText(AbandonedCount, data.Abandoned); }
            
            if (aawsaWells)         { updateSidebarNodeText(aawsaWells, data.AAWSA); }
            if (governmantWells)    { updateSidebarNodeText(governmantWells, data.Government); }
            if (nonGovernmantWells) { updateSidebarNodeText(nonGovernmantWells, data.NonGovernment); }
            if (privateWells)       { updateSidebarNodeText(privateWells, data.Private); }
            
            // Safe fallback value checking for your hydraulic physical dimension categories
            if (B100Wells)          { updateSidebarNodeText(B100Wells, data.B100 || 0); }
            if (BWN350Wells)        { updateSidebarNodeText(BWN350Wells, data.BWN350 || 0); }
            if (BWN500Wells)        { updateSidebarNodeText(BWN500Wells, data.BWN500 || 0); }
            if (A500Wells)          { updateSidebarNodeText(A500Wells, data.A500 || 0); }

            console.log("✔ Success: Sidebar metrics synced cleanly. Database layers decoupled safely from map context widgets.");
        })
        .catch(function(error) {
            System.err.println("Critical Failure: Core statistical mapping loop pass broke down: " + error);
        });
}

/**
 * 🫵 REGEX SAFE GUARD ITERATOR TOOL
 * Prevents continuous appending bugs (e.g. producing text like "Functional (12)(12)(12)" across refresh clicks)
 * Strips out legacy parenthesis text counters from the raw HTML text string before re-injecting the new count.
 */
function updateSidebarNodeText(domElement, numericalValue) {
    if (!domElement) return;
    
    // Extract base text description cleanly by dropping any previous trailing text inside parenthesis
    var baselineDescriptionString = domElement.textContent.replace(/\s*\([\d_]+\)/g, "").trim();
    
    // Re-inject the formatted total text token cleanly
    domElement.textContent = baselineDescriptionString + " (" + (numericalValue === undefined ? 0 : numericalValue) + ")";
}

// Automatically fire the statistical async hydration loop pass on system initialization startup 
setTimeout(function() {
    refreshBoreholeSummaryWidgetMetrics();
}, 250);




// ─── CENTRALIZED DATA DETAIL SYNCHRONIZATION ENGINE ───
var pub_project_id;
var Title_project_name;

/**
 * Orchestrates synchronous queries based on customized geographic lookup variables.
 * Leverages native modern Fetch arrays instead of standard XMLHttpRequest modules.
 */
function getElementByIdCustome(TLname, pro_def_para) {
    pub_project_id = pro_def_para;
    Title_project_name = TLname;

    var detailAccessTargetUrl = appServerContextRoot + "/acc_All_pro_data?pro_id=" + encodeURIComponent(pro_def_para);

    fetch(detailAccessTargetUrl, { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("Detailed borehole extraction connection crash.");
            return response.json();
        })
        .then(function(region_data) {
            if (typeof acessAll_detail === 'function') {
                acessAll_detail(region_data);
            }
        })
        .catch(function(error) {
            console.error("Critical: Master profile detail row sync failure encountered:", error);
        });
}
/**
 * =========================================================================
 * ─── AAWSA GROUNDWATER WEBGIS CORE ENGINE MODULE - STEP 12 ───
 * =========================================================================
 * Renders the full borehole dataset into an advanced, compact layout.
 * Fixed null-pointer object properties and floating map card layout clashing bugs.
 */
/**
 * Renders the full borehole dataset into an advanced, compact layout.
 * Optimized with high-contrast badge indicators, structured grid metrics, and clean data typography.
 */
/**
 * Renders the full borehole dataset dynamically on-click.
 * Generates an interactive, floating side-sheet dashboard directly over the full-screen map layout.
 */
function acessAll_detail(datafrom) {
    if (!datafrom || datafrom.length === 0) return;
    
    var datadrop = '';
    
    for (let i = 0; i < datafrom.length; i++) {
        var record = datafrom[i];

        // Format zero numeric parameters cleanly to maintain water data schema standards
        var abstractionRate = (record.abstractionRate === 0 || !record.abstractionRate) ? '_' : record.abstractionRate;
        var pumpPosOpn      = (record.pumpPositionOpn === 0 || !record.pumpPositionOpn) ? '_' : record.pumpPositionOpn;
        var wellSWLOpen     = (record.wellSWLOpen === 0 || !record.wellSWLOpen) ? '_' : record.wellSWLOpen;
        var wellDWLOpn      = (record.wellDWLOpn === 0 || !record.wellDWLOpn) ? '_' : record.wellDWLOpn;

        // Extract coordinate parameters safely using zero fallbacks to prevent toFixed() runtime crashes
        var latCoord = parseFloat(record.geoLocYWellRepo || 0).toFixed(6);
        var lngCoord = parseFloat(record.geoLocxWellRepo || 0).toFixed(6);
        var specCap  = parseFloat(record.specificCapacity || 0).toFixed(2);

        // Dynamically style status values using bright, clear badge colors
        var statusText = record.currentWS || '_';
        var badgeBg = '#22c55e'; // Default bright green for active nodes
        if (statusText.toLowerCase().includes('in') || statusText.toLowerCase().includes('non')) {
            badgeBg = '#ef4444'; // Bright warning red for down or abandoned nodes
        } else if (statusText.toLowerCase().includes('standby') || statusText.toLowerCase().includes('active')) {
            badgeBg = '#f97316'; // Alert orange for temporary states
        }

        datadrop += 
            // ─── CARD HEADER LAYER WITH EMBEDDED DYNAMIC CLOSE BUTTON ───
            '<tr style="background:#06402B; color:#ffffff; font-weight:700;">' +
                '<td colspan="4" style="text-align:center; padding:12px; font-size:13px; position:relative; border-bottom:3px solid #a3e635;">' +
                    '<i class="fa fa-database"></i> ' + (record.well_index || Title_project_name) + ' Well Information' +
                    '<span onclick="closeDynamicAttributesPanel()" style="position:absolute; right:12px; top:50%; transform:translateY(-50%); cursor:pointer; color:#94a3b8; font-size:16px; font-weight:normal; transition:color 0.2s;" onmouseover="this.style.color=\'#fff\'" onmouseout="this.style.color=\'#94a3b8\'">&times;</span>' +
                '</td>' +
            '</tr>' +

            // ─── SECTION 1: PRIMARY IDENTITY METADATA MATRIX ───
            '<tr style="background:#f8fafc;"><td colspan="4" style="padding:6px 12px; font-weight:700; font-size:11px; color:#06402B; text-transform:uppercase; border-bottom:1px solid #e2e8f0;"><i class="fa fa-info-circle"></i> Identity Metrics</td></tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="width:25%; padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Well Field:</td>' +
                '<td style="width:25%; padding:6px 8px; font-weight:700; color:#0f172a;">' + (record.wellFieldOverlay || '_') + '</td>' +
                '<td style="width:25%; padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Zone/Sub-City:</td>' +
                '<td style="width:25%; padding:6px 8px; color:#0f172a; font-weight:600;">' + (record.sucityZoneName || '_') + '</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Coordinates:</td>' +
                '<td style="padding:6px 8px; font-family:monospace; font-size:11px; color:#334155;" colspan="3">' +
                    '<span style="background:#f1f5f9; padding:2px 6px; border-radius:4px; margin-right:8px;"><b>Lat:</b> ' + latCoord + '</span>' +
                    '<span style="background:#f1f5f9; padding:2px 6px; border-radius:4px;"><b>Lng:</b> ' + lngCoord + '</span>' +
                '</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Well Owner:</td>' +
                '<td style="padding:6px 8px; color:#334155; font-weight:600;">' + (record.wellOwner || '_') + '</td>' +
                '<td style="padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Const. Year:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0f172a;">' + (record.Cyear || '_') + '</td>' +
            '</tr>' +

            // ─── SECTION 2: METRIC COMPARISON SPLIT GRID ───
            '<tr style="background:#f1f5f9;">' +
                '<td colspan="2" style="text-align:center; font-weight:700; font-size:11px; color:#06402B; padding:6px 0; border-top:1px solid #cbd5e1; border-bottom:1px solid #cbd5e1; border-right:1px solid #cbd5e1;"><i class="fa fa-wrench"></i> Physical Baseline Data</td>' +
                '<td colspan="2" style="text-align:center; font-weight:700; font-size:11px; color:#06402B; padding:6px 0; border-top:1px solid #cbd5e1; border-bottom:1px solid #cbd5e1;"><i class="fa fa-dashboard"></i> Operational Log</td>' +
            '</tr>' +
            
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Well Depth:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0f172a; border-right:1px solid #f1f5f9;">' + (record.wellDepth || '_') + ' m</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Abstraction Rate:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0284c7;">' + abstractionRate + ' L/s</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Well Yield:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0f172a; border-right:1px solid #f1f5f9;">' + (record.wellYield || '_') + ' L/s</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Pump Position:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + pumpPosOpn + ' m</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Pump Position:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (record.pumpPosition || '_') + ' m</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">SWL:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + wellSWLOpen + ' m</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">SWL:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (record.wellSWL || '_') + ' m</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">DWL:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + wellDWLOpn + ' m</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">DWL:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (record.wellDWL || '_') + ' m</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Current Status:</td>' +
                '<td style="padding:6px 8px;">' +
                    '<span style="background:' + badgeBg + '; color:#ffffff; padding:2px 8px; border-radius:12px; font-size:10.5px; font-weight:700; display:inline-block; text-transform:uppercase;">' + statusText + '</span>' +
                '</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Specific Cap:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#16a34a; border-right:1px solid #f1f5f9;">' + specCap + ' <span style="font-size:10px; font-weight:normal; color:#64748b;">L/m/s</span></td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Condition Class:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (record.functionWellCondi || '_') + '</td>' +
            '</tr>' +
            
            // ─── SECTION 3: SYSTEM ANCILLARY AUDITS ───
            '<tr style="background:#f8fafc;"><td colspan="4" style="padding:6px 12px; font-weight:700; font-size:11px; color:#06402B; '
            +'text-transform:uppercase; border-top:1px solid #cbd5e1; border-bottom:1px solid #e2e8f0;"><i class="fa fa-shield"></i> '
            +'Safety & Infrastructure Audits</td></tr>' +
            '<tr style="background:#ffffff;"><td style="padding:6px 8px; color:#64748b; font-size:11.5px;" colspan="2">Potable Status:</td>'
            +'<td style="padding:6px 8px; font-weight:600; color:#0f172a;" colspan="2">' + (record.potableS || '_') + '</td></tr>' +
            '<tr style="background:#ffffff;"><td style="padding:6px 8px; color:#64748b; font-size:11.5px;" colspan="2">Non-Potable Cause:</td><td style="padding:6px 8px; font-weight:600; color:#ef4444; font-size:11px;" colspan="2">' + (record.potableSnonPotabableR || '_') + '</td></tr>' +
                        '<tr><td style="padding:6px 8px; color:#64748b; font-size:11.5px;" colspan="2">InActive/ Non-Function Cause:</td><td style="padding:6px 8px; font-weight:600; color:#ef4444; font-size:11px;" colspan="2">' + (record.reasonNonF || '_') + '</td></tr>' +
            '<tr><td style="padding:6px 8px; color:#64748b; font-size:11.5px; border-bottom:1px solid #cbd5e1;" colspan="2">Data Last Updated:</td><td style="padding:6px 8px; font-family:monospace; font-size:11px; color:#64748b; border-bottom:1px solid #cbd5e1;" colspan="2">' + (record.recordDate || '_') + '</td></tr>' +
            
            // ─── ACTION EXPORT ROW ───
            '<tr style="background:#f8fafc;" class="exclude-from-pdf">' +
                '<td colspan="4" style="text-align:center; padding:6px 0; border-radius:0 0 3px 3px;">' +
                    '<button type="button" onclick="downloadUlAsPdf(' + (record.well_idParam || pub_project_id) + ')" style="background:#06402B; '
                    +'color:#ffffff; font-weight:700; border:none; padding:6px 6px; border-radius:4px; font-size:11.5px; cursor:pointer; '
                    +'display:inline-flex; align-items:center; justify-content: center;padding: 8px 64px; gap:6px; box-shadow:0 2px 4px '
                    +'rgba(0,0,0,0.1); white-space: nowrap;"><i class="fa fa-file-pdf-o"></i> Export to PDF' +
                    '</button>' +
                '</td>' +
            '</tr>';
    }

    // 🫵 STATE-OF-THE-ART INJECTION: Dynamically compile, mount, and animate the dashboard panel on-demand
    var slidingPanelId = "dynamic-well-dashboard-sheet";
    var slidingPanel = document.getElementById(slidingPanelId);
    
    if (!slidingPanel) {
        // Build the self-contained container frame if it doesn't exist on the screen layout tree
        slidingPanel = document.createElement("div");
        slidingPanel.id = slidingPanelId;
        slidingPanel.style.position = "fixed";
        slidingPanel.style.right = "20px";
        slidingPanel.style.top = "60px";
        slidingPanel.style.width = "400px";
        slidingPanel.style.height = "calc(100vh - 80px)";
        slidingPanel.style.backgroundColor = "#ffffff";
        slidingPanel.style.boxShadow = "-4px 0 20px rgba(15, 23, 42, 0.15)";
        slidingPanel.style.borderRadius = "8px";
        slidingPanel.style.border = "1px solid #cbd5e1";
        slidingPanel.style.zIndex = "10005"; // Floats safely above the map and navigation toolbars
        slidingPanel.style.overflowY = "auto";
        slidingPanel.style.boxSizing = "border-box";
        slidingPanel.style.transition = "transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)";
        slidingPanel.style.transform = "translateX(450px)"; // Hidden out of frame initially
        
        document.body.appendChild(slidingPanel);
    }
    
    // Inject the structured data matrix layout directly into the element shell container
    slidingPanel.innerHTML = '<table style="width:100%; border-collapse:collapse; font-size:12px;">' + datadrop + '</table>';
    
    // Smooth hardware-accelerated slide-in animation trigger
    setTimeout(function() {
        slidingPanel.style.transform = "translateX(0)";
    }, 50);
}

/**
 * Native dismiss trigger. Smoothly slides the dashboard element out of mobile/desktop viewports safely.
 */
function closeDynamicAttributesPanel() {
    var slidingPanel = document.getElementById("dynamic-well-dashboard-sheet");
    if (slidingPanel) {
        // Safe check-gate metrics: Handles both horizontal desktop slides and vertical mobile bottom-sheet animations
        if (window.innerWidth <= 1024) {
            slidingPanel.style.transform = "translateY(55vh)";
        } else {
            slidingPanel.style.transform = "translateX(450px)";
        }
    }
}

var Customwell_id;
var Custom_name;

/**
 * Tracks historical water chemistry data logs natively over the active context.
 * FIXED: Added safe-guard checks to prevent unhandled DOM rendering thread crashes.
 */
function getWellChemistryByWellId(TLname, pro_def_para) {
    // Prevent execution if the parameter ID is invalid or zero during initialization swaps
    if (!pro_def_para || pro_def_para === 0) {
        console.warn("⚠️ Data Sync Notice: Aborted lookup pass for invalid Well ID: " + pro_def_para);
        return;
    }

    Customwell_id = pro_def_para;
    Custom_name = TLname || "Selected Borehole";

    var chemistryTargetUrl = appServerContextRoot + "/waterChemistryUrl?well_id=" + encodeURIComponent(pro_def_para);

    fetch(chemistryTargetUrl, { method: 'POST' })
        .then(function(response) {
            if (!response.ok) throw new Error("Water chemistry metadata request connection failure.");
            return response.json();
        })
        .then(function(region_data) {
            // Defer execution slightly to let Leaflet finalize map layer shifts safely
            setTimeout(function() {
                if (typeof detailWaterChemistry === 'function') {
                    detailWaterChemistry(region_data);
                }
            }, 50);
        })
        .catch(function(error) {
            console.error("Critical: Chemistry matrix row sync breakdown encountered:", error);
        });
}

/**
 * Parses and populates comprehensive groundwater water chemistry logs.
 * FIXED: Added complete safety checks around DOM objects to prevent layer-switching freezing bugs.
 */
/**
 * Parses and populates comprehensive groundwater water chemistry logs.
 * FIXED: Formats chemical metrics into an interactive, full-color sliding side-sheet panel.
 */
function detailWaterChemistry(datafrom) {
    var slidingPanelId = "dynamic-well-dashboard-sheet";
    var slidingPanel = document.getElementById(slidingPanelId);
    
    // 1. DYNAMIC CONTAINER MOUNTING HOOK
    if (!slidingPanel) {
        slidingPanel = document.createElement("div");
        slidingPanel.id = slidingPanelId;
        slidingPanel.style.position = "fixed";
        slidingPanel.style.right = "20px";
        slidingPanel.style.top = "60px";
        slidingPanel.style.width = "400px";
        slidingPanel.style.height = "calc(100vh - 80px)";
        slidingPanel.style.backgroundColor = "#ffffff";
        slidingPanel.style.boxShadow = "-4px 0 20px rgba(15, 23, 42, 0.15)";
        slidingPanel.style.borderRadius = "8px";
        slidingPanel.style.border = "1px solid #cbd5e1";
        slidingPanel.style.zIndex = "10005"; 
        slidingPanel.style.overflowY = "auto";
        slidingPanel.style.boxSizing = "border-box";
        slidingPanel.style.transition = "transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)";
        slidingPanel.style.transform = "translateX(450px)"; 
        
        document.body.appendChild(slidingPanel);
    }

    // 2. EMPTY DATA PROTECTION CHECK GATE
    if (!datafrom || datafrom.length === 0) {
        var emptyNotificationMsg = '<tr><td colspan="4" style="text-align:center; padding:20px; color:#475569; font-style:italic;">' +
                                   'ℹ️ No laboratory water quality records found for this well selection.</td></tr>';
        slidingPanel.innerHTML = '<table style="width:100%; border-collapse:collapse; font-size:12px;">' + emptyNotificationMsg + '</table>';
        setTimeout(function() { slidingPanel.style.transform = "translateX(0)"; }, 50);
        return;
    }
    
    var datadrop = '';
                
    for (let i = 0; i < datafrom.length; i++) {
        var chem = datafrom[i];
        
        // Extract coordinate parameters safely using zero fallbacks to prevent toFixed() runtime crashes
        var chemLat = parseFloat(chem.latitude  || chem.lat || 0).toFixed(6);
        var chemLng = parseFloat(chem.longitude || chem.longtude || chem.lng || 0).toFixed(6);

        datadrop += 
            // ─── CARD HEADER LAYER WITH EMBEDDED DYNAMIC CLOSE BUTTON ───
            '<tr style="background:#06402B; color:#ffffff; font-weight:700;">' +
                '<td colspan="4" style="text-align:center; padding:12px; font-size:13px; position:relative; border-bottom:3px solid #a3e635;">' +
                    '<i class="fa fa-flask"></i> ' + (chem.wellIndexHChem || Custom_name) + ' Hydrochemistry Log' +
                    '<span onclick="closeDynamicAttributesPanel()" style="position:absolute; right:12px; top:50%; transform:translateY(-50%); cursor:pointer; color:#94a3b8; font-size:16px; font-weight:normal; transition:color 0.2s;" onmouseover="this.style.color=\'#fff\'" onmouseout="this.style.color=\'#94a3b8\'">&times;</span>' +
                '</td>' +
            '</tr>' +

            // ─── SECTION 1: IDENTITY METADATA MATRIX ───
            '<tr style="background:#f8fafc;"><td colspan="4" style="padding:6px 12px; font-weight:700; font-size:11px; color:#06402B; text-transform:uppercase; border-bottom:1px solid #e2e8f0;"><i class="fa fa-info-circle"></i> Identity Metrics</td></tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="width:25%; padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Well Field:</td>' +
                '<td style="width:25%; padding:6px 8px; font-weight:700; color:#0f172a;">' + (chem.wellFieldHChem || '_') + '</td>' +
                '<td style="width:25%; padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Zone/Sub-City:</td>' +
                '<td style="width:25%; padding:6px 8px; color:#0f172a; font-weight:600;">' + (chem.subcityZone || '_') + '</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Coordinates:</td>' +
                '<td style="padding:6px 8px; font-family:monospace; font-size:11px; color:#334155;" colspan="3">' +
                    '<span style="background:#f1f5f9; padding:2px 6px; border-radius:4px; margin-right:8px;"><b>Lat:</b> ' + chemLat + '</span>' +
                    '<span style="background:#f1f5f9; padding:2px 6px; border-radius:4px;"><b>Lng:</b> ' + chemLng + '</span>' +
                '</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#64748b; font-weight:600; font-size:11.5px;">Owner Group:</td>' +
                '<td style="padding:6px 8px; color:#334155; font-weight:600;" colspan="3">' + (chem.wellownerGroup || '_') + '</td>' +
            '</tr>' +

            // ─── SECTION 2: LABORATORY QUALITY METRICS SPLIT GRID ───
            '<tr style="background:#f1f5f9;">' +
                '<td colspan="2" style="text-align:center; font-weight:700; font-size:11px; color:#06402B; padding:6px 0; border-top:1px solid #cbd5e1; border-bottom:1px solid #cbd5e1; border-right:1px solid #cbd5e1;"><i class="fa fa-dot-circle-o"></i> Physical & Cations Data</td>' +
                '<td colspan="2" style="text-align:center; font-weight:700; font-size:11px; color:#06402B; padding:6px 0; border-top:1px solid #cbd5e1; border-bottom:1px solid #cbd5e1;"><i class="fa fa-filter"></i> Anions & Heavy Metals</td>' +
            '</tr>' +
            
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Calcium (Ca²⁺):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#0f172a; border-right:1px solid #f1f5f9;">' + (chem.calcium || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">EC (&micro;S/cm):</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0284c7;">' + (chem.electricConductivity || '_') + '</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">pH Level:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#16a34a; border-right:1px solid #f1f5f9;">' + (chem.pH || '_') + '</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Temperature:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.temprature || chem.temperature || '_') + ' &deg;C</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Magnesium (Mg²⁺):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (chem.magnisium || chem.magnesium || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Phosphate (PO₄³⁻):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.posphate || chem.phosphate || '_') + ' mg/L</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Sodium (Na⁺):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (chem.soduim || chem.sodium || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Potassium (K⁺):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.potasium || chem.potassium || '_') + ' mg/L</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Sulfate (SO₄²⁻):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (chem.sulfate || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Bicarbonate (HCO₃⁻):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.bicarbonet || chem.bicarbonate || '_') + ' mg/L</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Chloride (Cl⁻):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155; border-right:1px solid #f1f5f9;">' + (chem.chloride || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Total Hardness:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.totalHardness_caco3 || '_') + ' mg/L</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Fluoride (F⁻):</td>' +
                '<td style="padding:6px 8px; border-right:1px solid #f1f5f9;">' +
                    '<span style="background:#ffe4e6; color:#e11d48; padding:2px 6px; border-radius:4px; font-weight:700;">' + (chem.flouride || chem.fluoride || '_') + ' <span style="font-size:9px; font-weight:normal;">mg/L</span></span>' +
                '</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Manganese (Mn):</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#334155;">' + (chem.manganese || '_') + ' mg/L</td></tr>' 
                +'<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Nitrate (NO₃⁻):</td>' +
                '<td style="padding:6px 8px; border-right:1px solid #f1f5f9;">' +
                    '<span style="background:#ffe4e6; color:#e11d48; padding:2px 6px; border-radius:4px; font-weight:700;">' + (chem.nitrate || '_') + ' <span style="font-size:9px; font-weight:normal;">mg/L</span></span>' +
                '</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Iron (Fe):</td>' +
                '<td style="padding:6px 8px;">' +
                    '<span style="background:#fef3c7; color:#b45309; padding:2px 6px; border-radius:4px; font-weight:700;">' + (chem.iron || '_') + ' <span style="font-size:9px; font-weight:normal;">mg/L</span></span>' +
                '</td>' +
            '</tr>' +
            '<tr style="background:#ffffff;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">TDS Level:</td>' +
                '<td style="padding:6px 8px; font-weight:700; color:#0f172a; border-right:1px solid #f1f5f9;">' + (chem.TDS || '_') + ' mg/L</td>' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px;">Water Odour:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#475569;">' + (chem.waterOdor || '_') + '</td>' +
            '</tr>' +
            '<tr style="background:#f8fafc;">' +
                '<td style="padding:6px 8px; color:#475569; font-size:11.5px; border-bottom:1px solid #cbd5e1;" colspan="2">Water Colour:</td>' +
                '<td style="padding:6px 8px; font-weight:600; color:#475569; border-bottom:1px solid #cbd5e1;" colspan="2">' + (chem.waterColor || '_') + '</td>' +
            '</tr>' +
            
            // ─── FIXED ACTION EXPORT ROW: FULL COVERAGE BACKGROUND & EXCLUDE FROM PRINTING TRACE ───
            '<tr class="exclude-from-pdf" style="background:#f8fafc;">' +
                '<td colspan="4" style="text-align:center; padding:15px 0;">' +
                    '<button type="button" onclick="downloadUlAsPdf(' + (chem.well_No3ID || Customwell_id) + ')" style="background:#06402B; color:#ffffff; font-weight:700; border:none; padding:10px 24px; border-radius:4px; font-size:12px; cursor:pointer; display:inline-flex; align-items:center; justify-content:center; gap:8px; box-shadow:0 2px 5px rgba(0,0,0,0.15); width:85%; box-sizing:border-box; transition:background 0.2s;" onmouseover="this.style.background=\'#042d1e\'" onmouseout="this.style.background=\'#06402B\'">' +
                        '<i class="fa fa-file-pdf-o"></i> Export to PDF' +
                    '</button>' +
                '</td>' +
            '</tr>';
    }

    // Inject the compiled data matrix layout safely into the side-sheet shell structure
    slidingPanel.innerHTML = '<table style="width:100%; border-collapse:collapse; font-size:12px;">' + datadrop + '</table>';
    
    // Smooth hardware-accelerated slide-in animation trigger
    setTimeout(function() {
        slidingPanel.style.transform = "translateX(0)";
    }, 50);
}

/**
 * Captures the active borehole dataset card and compiles it into an official full-color PDF report.
 * FIXED: Explicitly purges the entire button HTML table row cell node to strip it from the generated PDF output entirely.
 */
function downloadUlAsPdf(wellId) {
    var sourcePanel = document.getElementById("dynamic-well-dashboard-sheet");
    if (!sourcePanel) {
        sourcePanel = document.querySelector(".leaflet-popup-content");
    }

    if (!sourcePanel) {
        console.warn("Export aborted: No valid active dataset card discovered.");
        return;
    }

    var tableElement = sourcePanel.querySelector("table");
    if (!tableElement) return;

    console.log("Initializing an isolated print frame context container...");

    var printIframe = document.createElement('iframe');
    printIframe.style.position = 'absolute';
    printIframe.style.left = '-9999px';
    printIframe.style.top = '0';
    printIframe.style.width = '750px';
    printIframe.style.height = 'auto';
    document.body.appendChild(printIframe);

    var iframeDoc = printIframe.contentWindow.document;
    iframeDoc.open();
    iframeDoc.write('<!DOCTYPE html><html><head><title>Borehole Report</title>');
    iframeDoc.write('<link rel="stylesheet" href="https://cloudflare.com">');
    iframeDoc.write('<style>' +
        'body { font-family: "Segoe UI", sans-serif; background: #ffffff; margin: 0; padding: 10px; color: #1e293b; }' +
        'table { width: 100%; border-collapse: collapse; font-size: 12px; }' +
        'tr, td { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; color-adjust: exact !important; padding: 6px 10px; border-bottom: 1px solid #f1f5f9; }' +
        'span[onclick*="closeDynamicAttributesPanel"] { display: none !important; }' +
        '</style></head><body>');

    // Create an explicit node container to isolate the button cell rows purging operations
    var templateWrapperElement = document.createElement("div");
    templateWrapperElement.innerHTML = tableElement.outerHTML;

    // 🫵 OBJECTIVE MET: Locates and forcefully rips out the entire download button row before passing data to html2pdf
    var targetButtonRow = templateWrapperElement.querySelector(".exclude-from-pdf");
    if (targetButtonRow && targetButtonRow.parentNode) {
        targetButtonRow.parentNode.removeChild(targetButtonRow);
        console.log("✔ Security Sweep Complete: Button cell elements stripped out of PDF data stream cleanly.");
    }

    iframeDoc.write(templateWrapperElement.innerHTML);
    iframeDoc.write('</body></html>');
    iframeDoc.close();

    var wellIndexElement = iframeDoc.querySelector("td");
    var formattedWellIndex = "Borehole_Record";
    if (wellIndexElement && wellIndexElement.textContent) {
        formattedWellIndex = wellIndexElement.textContent.replace(/[^a-zA-Z0-9-_]/g, '_').trim();
    }

    var todayStamp = new Date().toISOString().split('T')[0];

    var options = {
        margin: [0.5, 0.4, 0.5, 0.4],
        filename: 'AAWSA_' + formattedWellIndex + '_' + todayStamp + '.pdf',
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: {
            scale: 2.5,
            useCORS: true,
            logging: false,
            background: '#ffffff',
            letterRendering: true
        },
        jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
    };

    html2pdf().set(options).from(iframeDoc.body).toPdf().get('pdf').then(function (pdfInstance) {
        if (printIframe && printIframe.parentNode) {
            printIframe.parentNode.removeChild(printIframe);
        }
    }).save();
}

/**
 * =========================================================================
 * ─── FLOATING CONTROL BUTTON CONTAINER INJECTION (REFACTORED) ───
 * =========================================================================
 */
var mapDownloadControl = L.control({ position: 'topright' });

mapDownloadControl.onAdd = function (map) {
    var div = L.DomUtil.create('div', 'leaflet-bar map-download-control-box');
    
    // Style a premium floating action action widget button
    div.innerHTML = 
        '<a href="#" onclick="downloadProcessedMapLayer(); return false;" title="Download Custom Processed Map" ' +
        'style="background:#06402B; color:#ffffff; width:34px; height:34px; line-height:34px; display:block; text-align:center;'
        +' border-radius:4px; border:1px solid #cbd5e1; box-shadow:0 2px 6px rgba(0,0,0,0.1); font-size:14px; transition:background 0.2s;" '
        +'onmouseover="this.style.background=\'#042d1e\'" onmouseout="this.style.background=\'#06402B\'">' +
            '<i class="fa fa-file-image-o"></i>' +
        '</a>';
        
    return div;
};

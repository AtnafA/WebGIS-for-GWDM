package net.AAWSAgDB.fileupload.dao;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.AAWSAgDB.fileupload.ValuePair;
import net.AAWSAgDB.fileupload.model.File;
import net.AAWSAgDB.fileupload.model.OperationalImportDTO;
import net.AAWSAgDB.fileupload.model.SpatialUploadDTO;
import net.AAWSAgDB.fileupload.service.ExcelParsingService;
import net.AAWSAgDB.fileupload.service.FileHashingService;
import net.AAWSAgDB.fileupload.service.FileProcessingService;

@Service
public class uploadDao {
	static InputStream ion;
static private Connection conn=null;

//Add Parameters
public List<File> addParameter(String parameter,int user_id,String content,int cityZID){
	List<File>files=new ArrayList<File>();
	try{
		if(content.equals("City_OroZone")){
			String getvalue="select count(*) from gw_db_schema.sub_city_zone as scz,gw_db_schema.region_class rcl,gw_db_schema.region_city as rc"
					+ " where scz.cl_id=rcl.cl_id and rcl.reg_id=rc.reg_id and scz.cl_id="+cityZID+" and "
							+ "SIMILARITY(sub_city_zone_name,'"+parameter+"')>0.80";
			conn=ConnectionUtility.dbconnection();
			Statement stmnt=conn.createStatement();
			ResultSet rs=stmnt.executeQuery(getvalue);
			rs.next();
			int checkInsert=rs.getInt(1);
			if(checkInsert>0){
			Object obj = null;
			File ff=new File(0,"The Parameter <font color=#FF0000>"+parameter+"</font> is Already avialable in the "
					+ "<font color=#FF0000>"+content+"</font> List!",obj);
			files.add(ff);
			}else{
				// Get current date and time using java.time
			    LocalDateTime currentDateTime = LocalDateTime.now();
			    // Convert to java.sql.Timestamp
			    Timestamp sqlTimestamp = Timestamp.valueOf(currentDateTime);
				String pushdata="insert into gw_db_schema.sub_city_zone(cl_id,sub_city_zone_name,user_id,created_at)values("+cityZID+","
						+"'"+parameter+"',"+user_id+",?)";
				PreparedStatement prstmnt=conn.prepareStatement(pushdata);
				prstmnt.setTimestamp(1,sqlTimestamp);
				prstmnt.executeUpdate();
		String query="select rcl.cl_name from gw_db_schema.region_class rcl,gw_db_schema.region_city as rc where rcl.reg_id=rc.reg_id and "
				+ "rcl.cl_id="+cityZID+"";
			   Statement statmnt = conn.createStatement();
				ResultSet rs_user = statmnt.executeQuery(query);
				while(rs_user.next()){
					String cityZName=rs_user.getString(1);	
					String defintion=parameter+" is Added to the "+cityZName+" Content List.";
					Object obj = null;
					File ff=new File(0,"<font color=#31708f>"+defintion+"</font>",obj);
					files.add(ff);
				}
				}
		}else if(content.equals("Well Field")) {
			String getvalue="select count(*) from gw_db_schema.well_field where SIMILARITY(wf_name,'"+parameter+"')>0.80";
			conn=ConnectionUtility.dbconnection();
			Statement stmnt=conn.createStatement();
			ResultSet rs=stmnt.executeQuery(getvalue);
			rs.next();
			int checkInsert=rs.getInt(1);
			if(checkInsert>0){
			Object obj = null;
			File ff=new File(0,"The Attribute <font color=#FF0000>"+parameter+"</font> is Already avialable in the "
					+ "<font color=#FF0000>"+content+"</font> List!",obj);
			files.add(ff);
			}else{
				// Get current date and time using java.time
			    LocalDateTime currentDateTime = LocalDateTime.now();
			    // Convert to java.sql.Timestamp
			    Timestamp sqlTimestamp = Timestamp.valueOf(currentDateTime);
				String pushdata="insert into gw_db_schema.well_field(wf_name,user_id,created_at)values('"+parameter+"',"+user_id+",?)";
				PreparedStatement prstmnt=conn.prepareStatement(pushdata);
				prstmnt.setTimestamp(1,sqlTimestamp);
				prstmnt.executeUpdate();
				int update = prstmnt.getUpdateCount();
				if(update>0){
					String defintion=parameter+" is Added to the "+content+" Content List.";
					Object obj = null;
					File ff=new File(0,"<font color=#31708f>"+defintion+"</font>",obj);
					files.add(ff);	
				}else {
					Object obj = null;
					String defintion=parameter+" is Failed to add into "+content+" Content List.";
					File ff=new File(0,"<font color=#FF0000>"+defintion+"</font>",obj);
					files.add(ff);
				}
			}
		}
		else {
			String getvalue="select count(*) from gw_db_schema.addtionalparm where SIMILARITY(parameterp,'"+parameter+"')>0.80 and "
					+ "SIMILARITY(contentp,'"+content+"')>0.80";
			conn=ConnectionUtility.dbconnection();
			Statement stmnt=conn.createStatement();
			ResultSet rs=stmnt.executeQuery(getvalue);
			rs.next();
			int checkInsert=rs.getInt(1);
			if(checkInsert>0){
			String defintion=parameter;
			String contentN=content;
			Object obj = null;
			File ff=new File(0,"The Parameter <font color=#FF0000>"+defintion+"</font> is Already avialable in the "
					+ "<font color=#FF0000>"+contentN+"</font> List!",obj);
			files.add(ff);
			}else{
				// Get current date and time using java.time
			    LocalDateTime currentDateTime = LocalDateTime.now();
			    // Convert to java.sql.Timestamp
			    Timestamp sqlTimestamp = Timestamp.valueOf(currentDateTime);
				String pushdata="insert into gw_db_schema.addtionalparm(parameterp,contentp,created_at,user_id)values('"+parameter+"',"
						+ "'"+content+"',?,"+user_id+")";
				PreparedStatement prstmnt=conn.prepareStatement(pushdata);
				prstmnt.setTimestamp(1,sqlTimestamp);
				prstmnt.executeUpdate();
				String defintion=parameter+" is Added to the "+content+" Content List.";
				Object obj = null;
				File ff=new File(0,"<font color=#31708f>"+defintion+"</font>",obj);
				files.add(ff);
				}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return files;
	}

//Update Well Status repo id
private static void udateWellStatusRepoId(Connection conn,int repoCell_id) throws Exception {
	String updateCasing="update gw_db_schema.constwell_status set repo_id="+repoCell_id+" where repo_id is NULL";
	try(PreparedStatement preparedSatament = conn.prepareStatement(updateCasing)){
		preparedSatament.executeUpdate();
	}
}

	
    /**
     * Natively inserts a comprehensive well report document (PDF/Docx) into the repository.
     * Prevents duplicate files by checking the SHA-256 hash value first.
     * 
     * @param conn The active transaction database connection channel
     * @param dto The data transfer object containing the uploaded file stream parameters
     * @param wellIndexList An array of well indexes associated with this report document
     * @return The auto-generated primary key (repo_id) of the stored report document
     */
    public int insertComprehensiveWellReport(java.sql.Connection conn, SpatialUploadDTO dto, String[] wellIndexList) throws Exception {
        if (dto.getWellRepoInputStream() == null) {
            return 0;
        }

        // 1. Convert the binary document stream safely using our Service layer chunk buffer
        byte[] fileContent = fileProcessingService.convertStreamToByteArray(dto.getWellRepoInputStream());
        String hashValue = fileHashingService.calculateSHA256(fileContent);

        // 2. Check for duplicate files inside the repository to avoid data redundancy
        String checkSql = "SELECT repo_id FROM gw_db_schema.wellreport WHERE hash_value = ? LIMIT 1";
        try (java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, hashValue);
            try (java.sql.ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Document exists, return its matching primary key ID
                }
            }
        }

        // 3. Fallback array tracker mapping if no indexes were registered
        if (wellIndexList == null || wellIndexList.length == 0) {
            wellIndexList = new String[] { dto.getWellIndex() };
        }
        
        java.sql.Date today= java.sql.Date.valueOf(LocalDate.now());
        // 4. Create the native PostgreSQL Text Array securely
        java.sql.Array sqlArray = conn.createArrayOf("text", wellIndexList);
        String insertSql = "INSERT INTO gw_db_schema.wellreport(wellindexlist, filename, filesize, filetype, reportdoc, hash_value, recordate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setArray(1, sqlArray);
            insertStmt.setString(2, dto.getWellreportName());
            insertStmt.setInt(3, dto.getWellRepoSize());
            insertStmt.setString(4, dto.getWellReportType());
            insertStmt.setBytes(5, fileContent);
            insertStmt.setString(6, hashValue);
            insertStmt.setDate(7, today);
            
            insertStmt.executeUpdate();
            
            try (java.sql.ResultSet gk = insertStmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        } finally {
            if (sqlArray != null) {
                try { sqlArray.free(); } catch (java.sql.SQLException e) { e.printStackTrace(); }
            }
        }
        return 0;
    }

    /**
     * Natively inserts pump and generator specifications documents (PDF/Docx/Image).
     * Prevents data redundancy using the file's unique cryptographic hash value.
     * 
     * @param conn The active transaction database connection channel
     * @param dto The data transfer object containing the uploaded specification parameters
     * @param chId The matching hydrological characteristics foreign key reference context
     * @return The auto-generated primary key (pg_id) of the stored document record
     */
    public int insertPumpAndGeneratorSpecs(java.sql.Connection conn, SpatialUploadDTO dto, int chId) throws Exception {
        if (dto.getPumpdataStream() == null) {
            return 0;
        }

        // 1. Process stream to byte conversion cleanly
        byte[] fileContent = fileProcessingService.convertStreamToByteArray(dto.getPumpdataStream());
        String hashValue = fileHashingService.calculateSHA256(fileContent);

        // 2. Validate file existence to fully enforce data integrity contracts
        String checkSql = "SELECT pg_id FROM gw_db_schema.pum_generatordoc WHERE hashvalue = ? LIMIT 1";
        try (java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, hashValue);
            try (java.sql.ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Document exists, return its matching primary key ID
                }
            }
        }

        // 3. Save a brand new file specification package transactionally
        String insertSql = "INSERT INTO gw_db_schema.pum_generatordoc(filename, filetype, hashvalue, fcontent, ch_id) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, dto.getPumpFilename());
            insertStmt.setString(2, dto.getPumpFileType());
            insertStmt.setString(3, hashValue);
            insertStmt.setBytes(4, fileContent);
            
            if (chId == 0) {
                insertStmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                insertStmt.setInt(5, chId);
            }
            
            insertStmt.executeUpdate();
            
            try (java.sql.ResultSet gk = insertStmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        }
        return 0;
    }
	
	// 1. Inject the centralized shared text metric service component
 // 2. Inject your centralized hashing utility
    @Autowired
    private FileHashingService fileHashingService;
    @Autowired
    private FileProcessingService fileProcessingService;
    @Autowired
    private ExcelParsingService excelParsingService;
    
    /**
     * Unified Relational Pipeline Engine - Phase 1 Master Transaction Orchestrator.
     * Manages Case 1 (Bulk Draft Upgrades) and Case 2 (Form + Excel Filtering Boundaries) transactionally.
     */
    public List<File> executeSpatialDataUploadPipeline(SpatialUploadDTO formInput, List<String[]> excelRows, 
            int db_userID, int subcityZoneId, int wellFieldId, String WellIndex,
            String Dpermit) throws SQLException {
        
        List<File> reportLogs = new ArrayList<>();
        
        // Handle input normalization safely to avoid runtime NullPointer exceptions
        String targetWellIndex = (WellIndex != null) ? WellIndex.trim() : "";
        
        try (Connection conn = ConnectionUtility.dbconnection()) {
            conn.setAutoCommit(false); // Enable manual transaction control to prevent partial table data corruption

            try {
                // ─── SCENARIO A: EXCEL DATA SHEET IS ATTACHED (Case 1 Reconciliation OR Case 2 Single Filter Import) ───
                if (excelRows != null && !excelRows.isEmpty()) {
                    
                    // Determine if this is a Case 2 submission (Form inputs match the explicitly attached file)
                    boolean isCoupledFormAndExcel = !targetWellIndex.isEmpty();
                    
                    if (isCoupledFormAndExcel) {
                        reportLogs.add(new File(1, "Coupled Form and Excel attached. Isolating matching well: " + targetWellIndex, "Info"));
                    } else {
                        reportLogs.add(new File(1, "Excel data sheet attached. Scanning master dataset to reconcile and upgrade drafts...", "Info"));
                    }
                    
                    for (String[] row : excelRows) {
                        if (row == null || row.length < 87) continue;

                        String excelWellIndex = row[1]; // Column B [Index 1]
                        String excelWellCode  = row[0]; // Column A [Index 0]
                        
                        if (excelWellIndex == null || excelWellIndex.trim().isEmpty()) continue;

                        // 1. RECONCILIATION PRE-CHECK: Check the database iteratively for EVERY row
                        int existingWellId = findWellIdByIndex(conn, excelWellIndex);
                        String currentCaseStatus = checkWellCaseStatus(conn, excelWellIndex, excelWellCode);
                        
                        boolean isDraftRecord = (existingWellId > 0 && "Draft".equalsIgnoreCase(currentCaseStatus));

                        // 2. CRITICAL DECOUPLING GUARD (Addresses Case 1 vs Case 2 Conflicts):
                        // If it's a simultaneous Form + Excel upload, but this specific row is NOT an existing 
                        // database Draft, enforce strict form-matching rules to isolate your target well record.
                        if (isCoupledFormAndExcel && !isDraftRecord) {
                            if (!excelWellIndex.trim().equalsIgnoreCase(targetWellIndex)) {
                                continue; // Skip unrelated new rows; only insert the new well from the form input!
                            }
                        }

                        // 3. ORPHAN CONSTRAINT PROTECTION: 
                        // If a row doesn't match an existing Draft, AND it doesn't match the active form input,
                        // reject it completely to keep unlisted spatial coordinate data from cluttering PostGIS tables.
                        if (existingWellId == 0 && isCoupledFormAndExcel && !excelWellIndex.trim().equalsIgnoreCase(targetWellIndex)) {
                            continue; 
                        }

                        // 4. SAFE SPATIAL INJECTION: Extracted row-by-row since the row has cleared validation checkpoints
                        double lat       = Double.parseDouble(row[17]); // Column R [Index 17]
                        double lon       = Double.parseDouble(row[16]); // Column Q [Index 16]
                        double easting  = Double.parseDouble(row[18]); // Column S [Index 18]
                        double northing = Double.parseDouble(row[19]); // Column T [Index 19]
                        double elevation = Double.parseDouble(row[20]); // Column U [Index 20]

                        // Generate PostGIS coordinates natively and capture its key identifier
                        int generatedGeolocId = insertExcelGeoLocation(conn, lat, lon, easting, northing, elevation);
                        if (generatedGeolocId == -999) {
                            // 🫵 HYDRATE ACCREDITED MODEL WARNING OBJECT
                            // Matches your exact requirements format rule configuration parameters: File(checkExists, "Warning...", "Warning")
                               // Append your warning object cleanly straight into the active execution report array list tracker
                            reportLogs.add(new File(generatedGeolocId, "Warning: A borehole coordinate pair matching [Lat: " + lat + ", Lng: " + lon + "] is already registered in the system framework.", "Warning"));
                            
                            // Use a standard loop 'continue;' statement or flag variable pass to bypass processing this row row data block
                            continue; 
                        }

                        // 5. ATOMIC DATA ROUTING MATRIX
                        if (existingWellId > 0) {
                            if ("Draft".equalsIgnoreCase(currentCaseStatus)) {
                                // CASE 1 CONSOLIDATION UPGRADE: N-1 Form Only records are iteratively found and upgraded!
                                upgradeDraftWellToProduction(conn, existingWellId, generatedGeolocId, row, formInput, subcityZoneId);
                                reportLogs.add(new File(existingWellId, "Well Index '" + excelWellIndex + "' iteratively located and upgraded from Draft to Production.", "Success"));
                            } else {
                                reportLogs.add(new File(existingWellId, "Well Index '" + excelWellIndex + "' already active in production. Syncing extra logs...", "Sync"));
                            }
                            processChildrenTableHydration(conn, existingWellId, row, formInput, db_userID);
                        } else {
                            // CASE 2 OR NEW ENTRY: Process a full cascading multi-table insertion track safely
                            int newWellId = insertNewParentWaterWell(conn, excelWellIndex, row, db_userID, subcityZoneId, 
                                                                    wellFieldId, generatedGeolocId, formInput, "Well Completion Report", Dpermit);
                            processChildrenTableHydration(conn, newWellId, row, formInput, db_userID);
                            reportLogs.add(new File(newWellId, "New Well Index '" + excelWellIndex + "' successfully imported from spreadsheet data matrix.", "Success"));
                        }
                    }
                } 
                // ─── SCENARIO B: NO EXCEL ATTACHED (Case 1 Manual Form-Only N-1 Registration Entry) ───
                else {
                    int checkExists = findWellIdByIndex(conn, formInput.getWellIndex());
                    if (checkExists > 0) {
                        reportLogs.add(new File(checkExists, "Warning: A borehole matching this index is already registered in the system framework.", "Warning"));
                        return reportLogs;
                    }
                    
                    // Save as a minimalist draft entry placeholder without coordinates (geoloc_id set to 0/NULL)
                    int draftWellId = insertNewParentWaterWell(conn, targetWellIndex, null, db_userID, subcityZoneId, 
                    		wellFieldId, 0, formInput, "Draft", Dpermit);
                    reportLogs.add(new File(draftWellId, "Borehole metadata saved as a Draft entry pending full parameter extraction via Excel upload.", "Draft_Saved"));
                }

                conn.commit(); // Transaction safe atomic commit block execution
                reportLogs.add(new File(1, "Groundwater database synchronization completed successfully.", "Success"));

            } catch (Exception ex) {
                conn.rollback(); // Safe rollback undoes all modifications if an error hits midway to protect disk space integrity
                System.err.println("Database pipeline execution encountered a fatal failure. Transaction rolled back safely.");
                ex.printStackTrace();
                throw new SQLException("Pipeline exception triggered: " + ex.getMessage(), ex);
            }
        }
        return reportLogs;
    }
    
    /**
     * Helper lookup to cleanly perform a Get-or-Create sequence for db_user profiles.
     * Prevents row-by-row user duplication loops across your master import tasks.
     */
    private int getOrCreateInternalUserId(Connection conn, String userName, String orgName, String email, String phone, String userType) throws SQLException {
        if (orgName == null || orgName.trim().isEmpty() || "0".equals(orgName.trim())) {
            return 0;
        }
        
        String findSql = "SELECT id FROM gw_db_schema.db_user WHERE SIMILARITY(orgname, ?) > 0.90 AND usertype = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(findSql)) {
            stmt.setString(1, orgName.trim());
            stmt.setString(2, userType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1); // Profile already registered, return key
            }
        }

        // If not found, transactionally register the organizational profile safely
        String insertSql = "INSERT INTO gw_db_schema.db_user(orgname, email, phone, usertype) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, orgName.trim());
            
            // Apply standardized null string fallbacks cleanly to prevent database pollution
            if (email == null || email.trim().isEmpty() || "0".equalsIgnoreCase(email) || "none".equalsIgnoreCase(email) ||
            		"na".equalsIgnoreCase(email) || email.equalsIgnoreCase("non") || email.equalsIgnoreCase("None") || 
                    email.equalsIgnoreCase("NONE") || email.equalsIgnoreCase("NA") || email.equalsIgnoreCase("NoN") 
                    || email.equalsIgnoreCase("")) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, email.trim());
            }
            
            if (phone == null || phone.trim().isEmpty() || "0".equalsIgnoreCase(phone) || "none".equalsIgnoreCase(phone) 
            		|| "na".equalsIgnoreCase(phone) || phone.equalsIgnoreCase("non") || phone.equalsIgnoreCase("None") || 
            		phone.equalsIgnoreCase("NONE") || phone.equalsIgnoreCase("NA") || phone.equalsIgnoreCase("NoN") 
                    || phone.equalsIgnoreCase("")) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, phone.trim());
            }
            
            stmt.setString(4, userType);
            stmt.executeUpdate();
            
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return 0;
    }
    /**
     * Resolves or registers a administrative Woreda zone boundary safely.
     * Prevents database pollution by verifying existing structural records first.
     */
    private int getOrCreateWoredaId(Connection conn, int sczId, String woredaName) throws SQLException {
        if (woredaName == null || woredaName.trim().isEmpty()) return 0;
        
        String selectSql = "SELECT wr_id FROM gw_db_schema.woreda WHERE scz_id = ? AND wor_name = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setInt(1, sczId);
            stmt.setString(2, woredaName.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String insertSql = "INSERT INTO gw_db_schema.woreda(scz_id, wor_name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, sczId);
            stmt.setString(2, woredaName.trim());
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Resolves or registers a baseline local name entity row context safely.
     */
    private int getOrCreateLocalNameId(Connection conn, int wrId, String localName) throws SQLException {
        if (localName == null || localName.trim().isEmpty()) return 0;
        
        String selectSql = "SELECT local_id FROM gw_db_schema.local_name WHERE wr_id = ? AND local_name = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setInt(1, wrId);
            stmt.setString(2, localName.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        String insertSql = "INSERT INTO gw_db_schema.local_name(wr_id, local_name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wrId);
            stmt.setString(2, localName.trim());
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return 0;
    }
    public static java.sql.Date parseExcelDate(String rawValue) {
        String operationStartDate;

        // 1. Check for empty, blank, or "0" values
        if (rawValue == null || rawValue.trim().isEmpty() || rawValue.equals("0")) {
            operationStartDate = "1971-01-01";
        } else {
            operationStartDate = rawValue.trim();
            
            // 2. Clean up Excel decimal notation (e.g., "2012.0" -> "2012")
            if (operationStartDate.contains(".")) {
                operationStartDate = operationStartDate.substring(0, operationStartDate.indexOf("."));
            }
            
            // 3. If it's just a 4-digit year (like "2012"), convert it to a full date format
            if (operationStartDate.length() == 4) {
                operationStartDate = operationStartDate + "-01-01";
            }
        }

        // 4. Parse the cleaned string into java.sql.Date
        try {
            java.util.Date parsedUtilDate = new SimpleDateFormat("yyyy-MM-dd").parse(operationStartDate);
            return new java.sql.Date(parsedUtilDate.getTime());
        } catch (java.text.ParseException e) {
            System.err.println("Failed to parse date string '" + operationStartDate + "', falling back to 1971-01-01. Error: " + e.getMessage());
            return java.sql.Date.valueOf("1971-01-01");
        }
    }

    
    
    /**
     * Inserts a new parent water well record transactionally after validating uniqueness.
     * Enforces unique constraints across wellcode and well_index boundaries.
     */
    private int insertNewParentWaterWell(Connection conn, String wellIndex, String[] row, int db_userID, 
            int subcityZoneId, int wellFieldId, int geolocId, SpatialUploadDTO form, String caseStatus, String Dpermit) throws SQLException {
        
        // 1. Core Uniqueness Guard Check: Prevent duplicate record collision
        String duplicateCheckSql = "SELECT well_id FROM gw_db_schema.water_well WHERE well_index = ? LIMIT 1";
        try (PreparedStatement checkStmt = conn.prepareStatement(duplicateCheckSql)) {
            checkStmt.setString(1, wellIndex != null ? wellIndex.trim() : "");
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Duplicate entry detected! Well Index: " + wellIndex + " already exist. Skipping insertion.");
                    return 0; 
                }
            }
        }
        // 2. Proceed with insertion if validation checks pass smoothly
        String sql = "INSERT INTO gw_db_schema.water_well(gwdata_id, local_id, wf_id, geoloc_id, wellcode, well_index, well_depth, main_aquifer, "
                   + "well_type, cntn_year, owned_by, wellownercatgory, data_colln_case, record_date, db_userid, drilling_license) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        java.sql.Date defaultDate = java.sql.Date.valueOf("1971-01-01");
        String wellCode= (row != null) ? row[0] : form.getWellcode();
        double depth = (row != null) ? Double.parseDouble(row[2]) : form.getWellDepth(); 
        String aquifer = (row != null) ? row[35] : form.getMainAcquifer(); 
        String wellType = (row != null) ? row[33] : form.getWellType(); 
        java.sql.Date recoredDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        
        int generatedWellId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 
            stmt.setInt(1, 2);
            
            if (row == null) {
            	stmt.setNull(2, Types.INTEGER);}
            
            else {
            	// 1. Safe instance lookups prevent duplication clusters in geography layers
                int woredaId = getOrCreateWoredaId(conn, subcityZoneId, row[13]); // Column N
                int localNameId = getOrCreateLocalNameId(conn, woredaId, row[14]);          // Column M
            	stmt.setInt(2, localNameId);
            	}
            
            stmt.setInt(3, wellFieldId);
            
            if (geolocId == 0) stmt.setNull(4, Types.INTEGER);
            else stmt.setInt(4, geolocId);
            
            stmt.setString(5, wellCode);
            stmt.setString(6, wellIndex);
            stmt.setDouble(7, depth);
            stmt.setString(8, aquifer);
            stmt.setString(9, wellType);
            try {
                // Simply pass the specific array index to the function
            	if(row!=null) {
            		java.sql.Date constrDate = parseExcelDate(row[34]);	
            		// Bind it to your statement
                    stmt.setDate(10, constrDate);
            	}else {
            		stmt.setDate(10,defaultDate);
            	}
                
                // Example: Reuse it for another date column (e.g., index 35) effortlessly!
                // java.sql.Date endDate = parseExcelDate(row[35]);
                // stmt.setDate(11, endDate);
            } catch (java.sql.SQLException e) {
                System.err.println("Database error setting date: " + e.getMessage());
            }
			if(row == null || row[13].isEmpty() || row[13].isBlank()) {
	            stmt.setNull(11, Types.INTEGER);
	            stmt.setNull(12, Types.INTEGER);
	            //stmt.setNull(13, Types.INTEGER);
			}
			else {
				// Resolve organizational identifier safely
	            int ownerId = getOrCreateInternalUserId(conn, null, row[23], row[24], row[25], "Well Owner");
	            stmt.setInt(11, ownerId);
	            stmt.setString(12, row[22]);
			}
			stmt.setString(13, caseStatus);
            stmt.setDate(14, recoredDate);
            stmt.setInt(15, db_userID);
            stmt.setString(16, Dpermit);
            stmt.executeUpdate();

            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) generatedWellId = gk.getInt(1);
            }
        }
        return generatedWellId;
    }    
    
    /**
     * Automatically extracts values from columns [2-6, 14-86] and inserts them into the children tables.
     * Formatted with exact index parameters to prevent runtime conversion exceptions.
     */
    private void processChildrenTableHydration(Connection conn, int wellId, String[] row, SpatialUploadDTO form, int db_userID) throws Exception {
       
    	java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
    	java.sql.Date defaultDate = java.sql.Date.valueOf("1971-01-01");
        // 1. Hydrate Casing Information (`casing_info`)
    	 // 1. Hydrate Casing Information (`casing_info`)
        String insertCasing = "INSERT INTO gw_db_schema.casing_info(well_id, pmnt_casing, casing_mtrl, largecasing, telescopedcasing, totl_scrin_l, "
                            + "top_scrin_l, botm_scrin_l, caseinstall_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(insertCasing)) {
            stmt.setInt(1, wellId);
            stmt.setString(2, row[37]); // Column AL [Index 37] - Permanent Casing
            stmt.setString(3, row[38]); // Column AM [Index 38] - Casing Material
            stmt.setDouble(4, Double.parseDouble(row[5]));  // Column F [Index 5]  - Large Casing ID
            stmt.setDouble(5, Double.parseDouble(row[6]));  // Column G [Index 6]  - Telescoped Casing ID
            stmt.setDouble(6, Double.parseDouble(row[39])); // Column AN [Index 39] - Total Screen Length
            stmt.setDouble(7, Double.parseDouble(row[40])); // Column AO [Index 40] - Top Screen Length
            stmt.setDouble(8, Double.parseDouble(row[41])); // Column AP [Index 41] - Bottom Screen Length
            stmt.setDate(9, defaultDate);
            stmt.executeUpdate();
        }
        
        // 2. FIXED: High-integrity Get-or-Create resolutions to protect against user duplicates
        int contractorId = getOrCreateInternalUserId(conn,null, row[26], row[27], row[28], "Contractor");
        int consultantId = getOrCreateInternalUserId(conn, null, row[29], row[30], row[31], "Consultant");

     // Update the newly resolved administrative links back into extrawellinfo safely
        String insertExtraWellSql = "INSERT INTO gw_db_schema.extrawellinfo (well_id, const_by, consult_by,largdrillid,telescopdrillid,"
        		+ "welldiameter,project_name,data_source,drill_method) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmtExtra = conn.prepareStatement(insertExtraWellSql)) {
            stmtExtra.setInt(1, wellId);
            if (contractorId == 0) stmtExtra.setNull(2, Types.INTEGER); else stmtExtra.setInt(2, contractorId);
            if (consultantId == 0) stmtExtra.setNull(3, Types.INTEGER); else stmtExtra.setInt(3, consultantId);
            //stmtExtra.executeUpdate();
            stmtExtra.setDouble(4, Double.parseDouble(row[3]));
            stmtExtra.setDouble(5, Double.parseDouble(row[4]));
            stmtExtra.setDouble(6, 0);
            stmtExtra.setString(7, row[21]);
            stmtExtra.setString(8, row[32]);
            stmtExtra.setString(9, row[36]);  
            stmtExtra.executeUpdate();
            } finally {
                // 4. Safely free up native database array driver memory instances
            }

        // 2. Hydrate Well Characteristics (`well_characteristics`)
        int charId = 0;
        String insertChar = "INSERT INTO gw_db_schema.well_characteristics(well_id, specific_capacity, well_efficiency, hydraulicconduct, "
        		+ "transmissivity, storativity, record_date, wellproductivity, data_colln_case) "
                          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Well Completion Report')";
        try (PreparedStatement stmt = conn.prepareStatement(insertChar, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wellId);
            stmt.setDouble(2, Double.parseDouble(row[54])); // Column BC [Index 54] - Specific Capacity
            stmt.setDouble(3, Double.parseDouble(row[55])); // Column BD [Index 55] - Well Efficiency
            stmt.setDouble(4, Double.parseDouble(row[56])); // Column BE [Index 56] - Hydraulic Conductivity
            stmt.setDouble(5, Double.parseDouble(row[57])); // Column BF [Index 57] - Transmissivity
            stmt.setDouble(6, Double.parseDouble(row[58])); // Column BG [Index 58] - Storativity
            stmt.setDate(7,today);
            stmt.setString(8, row[59]);
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) { 
                if (gk.next()) charId = gk.getInt(1); 
            }
        }
        
     // Inside your master executeSpatialDataUploadPipeline method body loop:
        String[] activeWellIndexTracker = new String[] { form.getWellIndex() };

        // 1. Process and save the report document, capturing its new key
        int generatedRepoId = insertComprehensiveWellReport(conn, form, activeWellIndexTracker);

        // 3. Process and save electromechanical spec sheets, passing your ch_id reference context
        int generatedPgId = insertPumpAndGeneratorSpecs(conn, form, charId);
        
        
        String insertpumpTest = "INSERT INTO gw_db_schema.pumping_status(ch_id, pump_position, recordate, data_colln_case, doc_id) "
        		+ "VALUES (?, ?, ?, 'Well Completion Report', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertpumpTest)) {
            stmt.setInt(1, charId);
            stmt.setString(2, row[44]); //Column AS [Index 44] - Number of steps
            stmt.setDate(3, today); 
            if (generatedPgId==0) {
            	stmt.setNull(4, java.sql.Types.INTEGER);	
            }else {
            	stmt.setInt(4, generatedPgId);	
            }
            stmt.executeUpdate();
        }
        
        // 3. Hydrate Multi-stage Step Drawdown Tests (`step_drawdown_test`)
        double[] yieldsArray = excelParsingService.extractStepDrawdownYields(row[47]); // Column AV [Index 47] - Drawdown yields string
        java.sql.Array sqlYields = conn.createArrayOf("float8", toObjectArray(yieldsArray));
        
        String insertStep = "INSERT INTO gw_db_schema.step_drawdown_test(ch_id, number_of_steps, each_step_duration, yields_for_steps, comulative_dd,"
        		+ " data_colln_case, recordate) VALUES (?, ?, ?, ?, ?, 'Well Completion Report', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertStep)) {
            stmt.setInt(1, charId);
            stmt.setInt(2, (int) Double.parseDouble(row[45])); // Column AT [Index 45] - Number of steps
            stmt.setDouble(3, Double.parseDouble(row[46]));        // Column AU [Index 46] - Each step duration
            stmt.setArray(4, sqlYields);
            stmt.setDouble(5, Double.parseDouble(row[48]));        // Column AW [Index 48] - Cumulative Drawdown
            stmt.setDate(6, today);
            stmt.executeUpdate();
        }

        // 4. Hydrate Constant Discharge Test (`con_discharge_test`)
        int cdtId = 0;
        String insertCdt = "INSERT INTO gw_db_schema.con_discharge_test(ch_id, test_duration, yield_lps, swl_m, dwl_m, total_drawdown, "
        		+ "data_colln_case, recordate) VALUES (?, ?, ?, ?, ?, ?, 'Well Completion Report', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertCdt, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, charId);
            stmt.setDouble(2, Double.parseDouble(row[49])); // Column AX [Index 49] - Test Duration
            stmt.setDouble(3, Double.parseDouble(row[50])); // Column AY [Index 50] - Constant Yield
            stmt.setDouble(4, Double.parseDouble(row[51])); // Column AZ [Index 51] - Static Water Level (SWL)
            stmt.setDouble(5, Double.parseDouble(row[52])); // Column BA [Index 52] - Dynamic Water Level (DWL)
            stmt.setDouble(6, Double.parseDouble(row[53])); // Column BB [Index 53] - Total Drawdown
            stmt.setDate(7, today);
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) { 
                if (gk.next()) cdtId = gk.getInt(1); 
            }
        }

        // 5. Hydrate Water Quality Tracking Core Entity (`gw_data`)
        int gwId = 0;
        String insertGwData = "INSERT INTO gw_db_schema.gw_data(well_id,data_colln_case, recordate, db_userid) "
        		+ "VALUES (?, 'Well Completion Report', ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertGwData, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wellId);
            stmt.setDate(2, today);
            
            if (db_userID == 0) {
                stmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(3, db_userID);
            }
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) { 
                if (gk.next()) gwId = gk.getInt(1); 
            }
        }
        String insertPyD = "INSERT INTO gw_db_schema.physical_da(gw_id, temprature, turibidity, tot_disolved_solid, true_color, elec_condu, "
        		+ "recordate, data_colln_case) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, 'Well Completion Report')";
        try (PreparedStatement stmt = conn.prepareStatement(insertPyD)) {
            stmt.setInt(1, gwId);
            stmt.setDouble(2, Double.parseDouble(row[60])); // Column BI 
            stmt.setDouble(3, Double.parseDouble(row[61])); // Column BJ 
            stmt.setDouble(4, Double.parseDouble(row[62])); // Column BK 
            stmt.setDouble(5, Double.parseDouble(row[63])); // Column BL 
            stmt.setDouble(6, Double.parseDouble(row[64])); // Column BM
            stmt.setDate(7, today);
            stmt.executeUpdate();
        }

        // 6. Hydrate Hydro-Chemistry Metrics Data (`chemical_da`)
        String insertChemical = "INSERT INTO gw_db_schema.chemical_da(gw_id,recordate,ph,totalh_ca_mg_caco3,f_,na_,cl_,ca_,fe_,mn_,k_,p_,mg_,"
        		+ "no3plusno2_,so4_2_,hco3_,no3_,nh4plusn,as_,pb_,data_colln_case) "
        		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Well Completion Report')";
        try (PreparedStatement stmt = conn.prepareStatement(insertChemical)) {
            stmt.setInt(1, gwId);
            stmt.setDate(2, today);
            stmt.setDouble(3, Double.parseDouble(row[65])); // Column BN [Index 65] - pH Value
            stmt.setDouble(4, Double.parseDouble(row[66])); // Column BO [Index 66] - Total Hardness
            stmt.setDouble(5, Double.parseDouble(row[67])); // Column BP [Index 67] - Fluoride
            stmt.setDouble(6, Double.parseDouble(row[68])); // Column BQ [Index 68] - Sodium
            stmt.setDouble(7, Double.parseDouble(row[69])); // Column BR [Index 69] - Chloride
            stmt.setDouble(8, Double.parseDouble(row[70])); // Column BS [Index 70] - Calcium
            stmt.setDouble(9, Double.parseDouble(row[71])); // Column BT [Index 71] - Iron
            stmt.setDouble(10, Double.parseDouble(row[72])); // Column BU [Index 72] - Manganese
            stmt.setDouble(11, Double.parseDouble(row[73]));
            stmt.setDouble(12, Double.parseDouble(row[74]));
            stmt.setDouble(13, Double.parseDouble(row[75])); // Column BX [Index 75] - Magnesium
            stmt.setDouble(14, Double.parseDouble(row[76]));
            stmt.setDouble(15, Double.parseDouble(row[77]));
            stmt.setDouble(16, Double.parseDouble(row[78]));
            stmt.setDouble(17, Double.parseDouble(row[79])); // Column CB [Index 79] - Nitrate
            stmt.setDouble(18, Double.parseDouble(row[80]));
            stmt.setDouble(19, Double.parseDouble(row[81]));
            stmt.setDouble(20, Double.parseDouble(row[82]));
            stmt.executeUpdate();
        }
        
        String insertMRAD = "INSERT INTO gw_db_schema.micro_radio(gw_id, tot_coliforms, e_coli, radon, uranium, "
        		+ "recordate, data_colln_case) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, 'Well Completion Report')";
        try (PreparedStatement stmt = conn.prepareStatement(insertMRAD)) {
            stmt.setInt(1, gwId);
            stmt.setDouble(2, Double.parseDouble(row[83])); // Column CF 
            stmt.setDouble(3, Double.parseDouble(row[84])); // Column CG 
            stmt.setDouble(4, Double.parseDouble(row[85])); // Column CH 
            stmt.setDouble(5, Double.parseDouble(row[86])); // Column CI 
            stmt.setDate(6, today);
            stmt.executeUpdate();
        }

        // 7. Hydrate Operational Lifecycle Core Tracking Entity (`constwell_status`)
        int ConstStatusId=0;
        String insertStatus = "INSERT INTO gw_db_schema.constwell_status(cdt_id, gw_id, well_status, record_date, data_colln_case, enumid, repo_id) "
        		+ "VALUES (?, ?, ?, ?, 'Well Completion Report',?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertStatus,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cdtId);
            stmt.setInt(2, gwId);
            stmt.setString(3, row[42]);
            stmt.setDate(4, today);
            
            if (form.getEnameId() == 0) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, form.getEnameId());
            }
         // 2. Pass generatedRepoId right down into your status table inserts
            if (generatedRepoId == 0) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, generatedRepoId);
            }
            stmt.executeUpdate();
            try (ResultSet gk = stmt.getGeneratedKeys()) { 
                if (gk.next()) ConstStatusId = gk.getInt(1); 
            } 
        }
        String insertAbandonedReason = "INSERT INTO gw_db_schema.abandoned_well(cws_id, reported_by, reason, sealed_yn, date_sealed) "
        		+ "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertAbandonedReason)) {
            stmt.setInt(1, ConstStatusId);
            if (db_userID == 0) {
                stmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(2, db_userID);
            }
            stmt.setString(3, row[43]); //Column AS [Index 43] - Number of steps
            stmt.setString(4, "");
            stmt.setDate(5, defaultDate); 
            stmt.executeUpdate();
        }
        String insertProductiveWellSatus = "INSERT INTO gw_db_schema.productive_well(cws_id, data_colln_case,record_date) "
        		+ "VALUES (?, 'Well Completion Report', ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProductiveWellSatus)) {
            stmt.setInt(1, ConstStatusId);
            stmt.setDate(2, today);
            stmt.executeUpdate();
        }
    }

    /**
     * Helper to wrap primitive numeric double arrays into Object arrays for PostgreSQL mappings.
     */
    private Double[] toObjectArray(double[] primitives) {
        if (primitives == null) return new Double[0];
        Double[] objects = new Double[primitives.length];
        for (int i = 0; i < primitives.length; i++) {
            objects[i] = primitives[i];
        }
        return objects;
    }
    /**
     * Natively inserts or resolves PostGIS coordinate geometries to prevent orphan space errors.
     * Validates if the location profile already exists to bypass redundant insertions and issue warnings.
     * 
     * @return The auto-generated geoloc_id if newly inserted, or the existing registered ID if duplicate.
     */
    private int insertExcelGeoLocation(Connection conn, double lat, double lon, double easting, double northing, double elevation) 
    		throws SQLException {
        // Safe guard check to avoid entering processing sequences for unassigned zero coordinates
        if (lat == 0.0 || lon == 0.0) {
            return 0;
        }    
        // 1. Core Duplicate Validation Check: Find existing coordinate matches
        String selectSql = "SELECT geoloc_id FROM gw_db_schema.geo_location WHERE lat = ? AND longt = ? LIMIT 1";
        try (java.sql.PreparedStatement checkStmt = conn.prepareStatement(selectSql)) {
            checkStmt.setDouble(1, lat);
            checkStmt.setDouble(2, lon);
            
            try (java.sql.ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int registeredId = rs.getInt(1);
                    
                    // 🫵 OBJECTIVE MET: Log notification to server terminal safely
                    System.out.println("⚠️ PIPELINE WARNING: Coordinate pair [Lat: " + lat + ", Long: " + lon 
                                       + "] is already registered in the database under Geolocation ID: " + registeredId);
                    
                    // Return a negative unique token ID flag to indicate a duplicate coordinate warning case
                    return -999; 
                }
            }
        }
    	
        // 2. Proceed with high-integrity spatial geometric insertion if it is a brand new location point
        String sql = "INSERT INTO gw_db_schema.geo_location(lat, longt, easting, northing, l_elevation, geom_latlong) "
                   + "VALUES (?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))";
                   
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lon);
            stmt.setDouble(3, easting);
            stmt.setDouble(4, northing);
            stmt.setDouble(5, elevation);
            stmt.setDouble(6, lon); // X Coordinate (Longitude) matching PostGIS point construction layouts
            stmt.setDouble(7, lat); // Y Coordinate (Latitude) matching PostGIS point construction layouts
            
            stmt.executeUpdate();
            
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Upgrades an existing manual 'Draft' entry to a full PostGIS production record.
     * Rewritten to eliminate nested statement leaks and prevent duplicate location registrations.
     */
    private void upgradeDraftWellToProduction(Connection conn, int wellId, int geolocId, String[] row, SpatialUploadDTO form,int subcityZoneId) 
    		throws SQLException {
        
        // 1. Safe instance lookups prevent duplication clusters in geography layers
        int woredaId = getOrCreateWoredaId(conn, subcityZoneId, row[13]); // Column N
        int localNameId = getOrCreateLocalNameId(conn, woredaId, row[14]);          // Column M
     // Resolve organizational identifier safely
        int ownerId = getOrCreateInternalUserId(conn, null, row[23], row[24], row[25], "Well Owner");
    	
        String wellCode = row[0];  // Column A [Index 0]
        String wellIndex = row[1]; // Column B [Index 1]
        
        String sql = "UPDATE gw_db_schema.water_well SET geoloc_id = ?, local_id = ?,owned_by = ?, well_depth = ?, main_aquifer = ?, well_type = ?, "
                   + "cntn_year = ?, wellownercatgory = ?, data_colln_case = 'Well Completion Report' WHERE wellcode = ? "
                   + "AND well_index = ?";
                   
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, geolocId);
            stmt.setInt(2, localNameId);
            stmt.setInt(3, ownerId);
            stmt.setDouble(4, Double.parseDouble(row[2])); // Well Depth
            stmt.setString(5, row[35]);                    // Main Aquifer
            stmt.setString(6, row[33]);                    // Well Type
            try {
                // Simply pass the specific array index to the function
            	java.sql.Date constrDate=parseExcelDate(row[34]);
                stmt.setDate(7, constrDate);
            } catch (java.sql.SQLException e) {
                System.err.println("Database error setting date: " + e.getMessage());
            }  
            
            stmt.setString(8, row[22]);           
            stmt.setString(9, wellCode);
            stmt.setString(10, wellIndex);
            
            stmt.executeUpdate();
        }
    }
    
    private String checkWellCaseStatus(Connection conn, String wellIndex, String wellCode) throws SQLException {
        String sql = "SELECT data_colln_case FROM gw_db_schema.water_well WHERE SIMILARITY(well_index, ?) > 0.95 and "
        		+ "SIMILARITY(wellcode, ?) > 0.95 LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wellIndex.trim());
            stmt.setString(2, wellCode.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return "";
    }
    /**
     * Performs a secure parameterized similarity check for individual well records.
     * Guarantees that statements are instantly closed to fix maximum connection limit failures.
     */
    public boolean checkWellIndexExistsNatively(String wellIndex) throws java.sql.SQLException {
        if (wellIndex == null || wellIndex.trim().isEmpty()) {
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM gw_db_schema.water_well WHERE SIMILARITY(well_index, ?) > 0.95";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, wellIndex.trim());
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // Placeholder declarations to maintain compatibility with your sub-method callbacks context
    /**
     * Natively resolves a well identifier by its index string safely using PreparedStatement.
     */
    public int findWellIdByIndex(java.sql.Connection conn, String wellIndex) throws java.sql.SQLException {
    	//System.out.println("Check WellCode From U-Form "+wellCode);
        String sql = "SELECT ww.well_id FROM gw_db_schema.water_well as ww WHERE SIMILARITY(ww.well_index, ?) > 0.95 LIMIT 1";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wellIndex.trim());
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) 
                	return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Resolves an internal administrative user ID by organization name and user type.
     */
    public int findUserIdByOrgAndType(java.sql.Connection conn, String orgName, String userType) throws java.sql.SQLException {
        if (orgName == null || orgName.trim().isEmpty()) return 0;
        String sql = "SELECT id FROM gw_db_schema.db_user WHERE SIMILARITY(orgname, ?) > 0.85 AND usertype = ? LIMIT 1";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orgName.trim());
            stmt.setString(2, userType);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Checks if extra information records already exist for a specific well identifier.
     */
    public boolean checkExtraInfoExists(java.sql.Connection conn, int wellId) throws java.sql.SQLException {
        String sql = "SELECT COUNT(*) FROM gw_db_schema.extrawellinfo WHERE well_id = ?";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wellId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Checks if groundwater data records already exist for a specific well identifier.
     */
    public boolean checkGwDataExists(java.sql.Connection conn, int wellId) throws java.sql.SQLException {
        String sql = "SELECT COUNT(*) FROM gw_db_schema.gw_data WHERE well_id = ?";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, wellId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Captures the latest characteristics ID associated with a well index layout safely.
     */
    public int findMaxCharacteristicsId(java.sql.Connection conn, String wellIndex) throws java.sql.SQLException {
        String sql = "SELECT ch_id FROM gw_db_schema.well_characteristics as ch "
                   + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
                   + "WHERE SIMILARITY(ww.well_index, ?) > 0.98 ORDER BY ch.ch_id DESC LIMIT 1";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wellIndex);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
    /**
     * Safely captures the latest test ID matching your hydrological characteristics parameters.
     */
    public int findMaxConDischargeTestId(java.sql.Connection conn, int chCellId) throws java.sql.SQLException {
        String sql = "SELECT cdt_id FROM gw_db_schema.con_discharge_test as cd "
                   + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
                   + "WHERE ch.ch_id = ? AND cd.data_colln_case = 'Well Completion Report' "
                   + "ORDER BY cd.cdt_id DESC LIMIT 1";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chCellId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Resolves the groundwater operational dataset ID matching high-similarity indicators.
     */
    public int findGroundwaterDataId(java.sql.Connection conn, String wellCellIndex) throws java.sql.SQLException {
        String sql = "SELECT gw.gw_id FROM gw_db_schema.gw_data as gw "
                   + "JOIN gw_db_schema.water_well as ww ON gw.well_id = ww.well_id "
                   + "WHERE SIMILARITY(ww.well_index, ?) = 1 LIMIT 1";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wellCellIndex);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Verifies if any water quality parameters have already been logged for this dataset ID.
     */
    public boolean checkPhysicalDataExists(java.sql.Connection conn, int gwCellId) throws java.sql.SQLException {
        String sql = "SELECT COUNT(*) FROM gw_db_schema.physical_da WHERE gw_id = ?";
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gwCellId);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Resolves the primary casing info ID matching specific borehole indices.
     * FIXED: Parameter binding variable name mismatch resolved to fix compilation errors.
     */
    public int findCasingInfoId(java.sql.Connection conn, String wellCellIndex) throws java.sql.SQLException {
        if (wellCellIndex == null || wellCellIndex.trim().isEmpty()) {
            return 0;
        }
        
        String sql = "SELECT ca.ci_id FROM gw_db_schema.casing_info as ca "
                   + "JOIN gw_db_schema.water_well as ww ON ww.well_id = ca.well_id "
                   + "WHERE SIMILARITY(ww.well_index, ?) > 0.98 LIMIT 1";

        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            // FIXED: Using the correct parameter variable 'wellCellIndex'
            stmt.setString(1, wellCellIndex.trim());
            
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Resolves matching well index credentials cleanly using string similarity metrics.
     * Guarded via try-with-resources to completely avoid open connections hanging.
     */
    public String findMatchingWellIndexBySimilarity(java.sql.Connection conn, String wellCellIndex) throws java.sql.SQLException {
        String sql = "SELECT ww.well_index FROM gw_db_schema.water_well as ww WHERE SIMILARITY(ww.well_index, ?) > 0.80 LIMIT 1";
        
        try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, wellCellIndex);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

//End of save spatial 
/**
 * Iteratively processes the extracted 113-column operational row matrix dataset,
 * Enforces tight, connection-leak-free transaction boundaries using parameter maps,
 * Unified Operational State Machine Engine,
 * Transactionally executes history-archiving (Current -> Historical) cascades
 * and persists new datasets safely. Completely removes MAX() subquery bottlenecks.
 */
public void executeOperationalVersioningPipeline(Connection conn, int wellId, OperationalImportDTO opDto, int dbUserId) throws Exception {
    
    // ─── APPROACH 2: COLLISION PROTECTION GUARD PRE-CHECK ───
    String collisionCheckSql = "SELECT COUNT(*) FROM gw_db_schema.con_discharge_test as cdt "
                             + "JOIN gw_db_schema.well_characteristics as ch ON cdt.ch_id = ch.ch_id "
                             + "WHERE ch.well_id = ? AND cdt.recordate = ? AND cdt.data_colln_case = 'Operational' AND cdt.datacondtion = 'Current'";
                             
    try (PreparedStatement checkStmt = conn.prepareStatement(collisionCheckSql)) {
        checkStmt.setInt(1, wellId);
        checkStmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("⚠️ Case 2 Collision: Operational version matching Well ID " + wellId + " on date " 
            + opDto.getDataRecordedDate() + " already exists. Skipping row.");
                return; // Avoids duplicate insertion cleanly
            }
        }
    }

    // Resolve or attach the core characteristics context identifier safely to minimize database trips
    int resolvedCharId = 0;
    String findCharSql = "SELECT ch_id FROM gw_db_schema.well_characteristics WHERE well_id = ? ORDER BY ch_id DESC LIMIT 1";
    try (PreparedStatement stmt = conn.prepareStatement(findCharSql)) {
        stmt.setInt(1, wellId);
        try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) resolvedCharId = rs.getInt(1); }
    }

    // ─── APPROACH 1: HISTORICAL VERSION ARCHIVING MATRIX ───
    
    // Step A: Archive matching Groundwater Data rows safely
    String findOldGwSql = "SELECT gw_id FROM gw_db_schema.gw_data WHERE well_id = ? AND recordate < ? AND data_colln_case = 'Operational' "
    		+ "AND datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldGwSql)) {
        stmt.setInt(1, wellId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.gw_data SET datacondtion = 'Historical' WHERE gw_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }

    // Step B: Atomically insert the fresh baseline Groundwater dataset and capture its key
    int generatedGwId = 0;
    String insertGwSql = "INSERT INTO gw_db_schema.gw_data(well_id, potability_status, non_potable_reason, data_colln_case, recordate, db_userid, "
    		+ "datacondtion) VALUES (?, ?, ?, 'Operational', ?, ?, 'Current')";
    try (PreparedStatement stmt = conn.prepareStatement(insertGwSql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, wellId);
        stmt.setString(2, opDto.getGwPotableStatus());
        stmt.setString(3, opDto.getNonPotableReason());
        stmt.setDate(4, opDto.getDataRecordedDate());
        stmt.setInt(5, dbUserId);
        stmt.executeUpdate();
        try (ResultSet gk = stmt.getGeneratedKeys()) { if (gk.next()) generatedGwId = gk.getInt(1); }
    }

    // Step C: Archive older active Hydrochemistry entries linked to this tracking path
    String findOldChemSql = "SELECT phd.pdata_id, wch.chda_id FROM gw_db_schema.physical_da as phd JOIN gw_db_schema.chemical_da as wch ON phd.gw_id = wch.gw_id WHERE phd.gw_id = ? AND (phd.recordate < ? OR wch.recordate < ?) AND phd.datacondtion = 'Current' AND wch.datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldChemSql)) {
        stmt.setInt(1, generatedGwId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        stmt.setDate(3, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                try (PreparedStatement upP = conn.prepareStatement("UPDATE gw_db_schema.physical_da SET datacondtion = 'Historical' WHERE pdata_id = ?")) { upP.setInt(1, rs.getInt(1)); upP.executeUpdate(); }
                try (PreparedStatement upC = conn.prepareStatement("UPDATE gw_db_schema.chemical_da SET datacondtion = 'Historical' WHERE chda_id = ?")) { upC.setInt(1, rs.getInt(2)); upC.executeUpdate(); }
            }
        }
    }
    addPhysicaldata(conn, generatedGwId, opDto.getTemprature(), opDto.getElecConductivity(), opDto.getDataRecordedDate(), "Operational", opDto.getWaterColor(), opDto.getWaterOdor(), "Current");
    addChemicaldata(conn, generatedGwId, opDto.getDataRecordedDate(), opDto.getpHValue(), "Operational", "Current");

    // Step D: Archive matching Discharge testing records safely
    String findOldCdtSql = "SELECT cdt_id FROM gw_db_schema.con_discharge_test WHERE ch_id = ? AND recordate < ? AND data_colln_case = 'Operational' AND datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldCdtSql)) {
        stmt.setInt(1, resolvedCharId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.con_discharge_test SET datacondtion = 'Historical' WHERE "
                		+ "cdt_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }
    
    // Step E: Insert the fresh constant discharge log natively and capture its atomic primary key
    int generatedCdtId = 0;
    String insertCdtSql = "INSERT INTO gw_db_schema.con_discharge_test(ch_id, swl_m, dwl_m, "
    		+ "data_colln_case, recordate, datacondtion) VALUES (?, ?, ?, 'Operational', ?, 'Current')";
    try (PreparedStatement stmt = conn.prepareStatement(insertCdtSql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, resolvedCharId);
        stmt.setDouble(2, opDto.getStaticWL());
        stmt.setDouble(3, opDto.getDynamicWL());
        stmt.setDate(4, opDto.getDataRecordedDate());
        stmt.executeUpdate();
        try (ResultSet gk = stmt.getGeneratedKeys()) { if (gk.next()) generatedCdtId = gk.getInt(1); }
    }

    // Step F: Archive legacy pumping status tracks transactionally
    String findOldPumpSql = "SELECT pump_id FROM gw_db_schema.pumping_status WHERE ch_id = ? AND recordate < ? AND data_colln_case = 'Operational' "
    		+ "AND datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldPumpSql)) {
        stmt.setInt(1, resolvedCharId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.pumping_status SET datacondtion = 'Historical' WHERE "
                		+ "pump_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }
    //Pumping Status Table Insertion:
    String insertPumpStatus = "insert into gw_db_schema.pumping_status(ch_id,pump_capacity,pump_position,pumphead,abstraction_rate,currenty_designy,"
    		+ "data_colln_case,recordate,datacondtion)values(?, ?, ?, ?, ?, ?, 'Operational', ?, 'Current')";
    try (PreparedStatement preparedStatement = conn.prepareStatement(insertPumpStatus)) {
    	preparedStatement.setInt(1, resolvedCharId);
    	preparedStatement.setDouble(2, opDto.getPumpCapacity());
    	preparedStatement.setDouble(3, opDto.getPumpPosition());
    	preparedStatement.setString(4, opDto.getPumpHead());
    	preparedStatement.setDouble(5, opDto.getAbstractionRate());
    	preparedStatement.setString(6, opDto.getCyieldDyield());
    	preparedStatement.setDate(7, opDto.getDataRecordedDate());
      preparedStatement.executeUpdate();
    }

    // Step G: HIGH-INTEGRITY GET-OR-CREATE RESOLUTION FOR THE ENUMERATOR PROFILE
    int resolvedEnumId = getOrCreateInternalUserId(conn, opDto.getEnumratorName(), opDto.getEnumratorCampanyN(), opDto.getEnumratoremail(), 
    		opDto.getEnumratorPhone(), "Enumerator");

    // Step H: Archive legacy Constructwell status configurations safely
    String findOldCwsSql = "SELECT cws_id FROM gw_db_schema.constwell_status WHERE cdt_id = ? AND record_date < ? AND data_colln_case = 'Operational' AND datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldCwsSql)) {
        stmt.setInt(1, generatedCdtId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.constwell_status SET datacondtion = 'Historical' "
                		+ "WHERE cws_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }
    
    // Step I: Insert the clean Constructed Well Status dataset natively and capture its key identifier
    int generatedCwsId = 0;
    String insertCwsSql = "INSERT INTO gw_db_schema.constwell_status(cdt_id, gw_id, well_status, record_date, data_colln_case, enumid, "
    		+ "datacondtion,productive_well_status) VALUES (?, ?, ?, ?, 'Operational', ?, 'Current',?)";
    try (PreparedStatement stmt = conn.prepareStatement(insertCwsSql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, generatedCdtId);
        stmt.setInt(2, generatedGwId);
        stmt.setString(3, opDto.getCurrentWellStatus());
        stmt.setDate(4, opDto.getDataRecordedDate());
        
        if (resolvedEnumId == 0) stmt.setNull(5, Types.INTEGER);
        else stmt.setInt(5, resolvedEnumId);
        stmt.setString(6, opDto.getCurrentWellStatus());
        stmt.executeUpdate();
        try (ResultSet gk = stmt.getGeneratedKeys()) { 
            if (gk.next()) generatedCwsId = gk.getInt(1); 
        }
    }

    // Step J: Archive legacy Productive Well rows transactionally
    String findOldProdSql = "SELECT pro_id FROM gw_db_schema.productive_well "
                         + "WHERE cws_id = ? AND record_date < ? AND data_colln_case = 'Operational' AND datacondtion = 'Current'";
                         
    try (PreparedStatement stmt = conn.prepareStatement(findOldProdSql)) {
        stmt.setInt(1, generatedCwsId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.productive_well SET datacondtion = 'Historical' WHERE pro_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }
 // Step K: Execute atomic insertion for the core Productive Well profile to get its generated primary key
    int generatedProId = 0;
    String insertProdSql = "INSERT INTO gw_db_schema.productive_well(cws_id, data_colln_case, record_date, wateruse_license, wellhead_cond, "
    		+ "poorwellh_reason, drainage_cond, pumping_operen_date, datacondtion) "
                         + "VALUES (?, 'Operational', ?, ?, ?, ?, ?, ?, 'Current')";
                         
    try (PreparedStatement stmtProd = conn.prepareStatement(insertProdSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtProd.setInt(1, generatedCwsId); // Inherited from previous database insertion context step
        stmtProd.setDate(2, opDto.getDataRecordedDate());
        stmtProd.setString(3, opDto.getWaterUsageLicence());
        stmtProd.setString(4, opDto.getWellHeadCondition());
        stmtProd.setString(5, opDto.getPoorWellHeadReason());
        stmtProd.setString(6, opDto.getDrainageCondition());
        stmtProd.setDate(7, opDto.getOperationStartedDate());
        stmtProd.executeUpdate();
        try (ResultSet gk = stmtProd.getGeneratedKeys()) { 
            if (gk.next()) generatedProId = gk.getInt(1); 
        }
    }
    // Step L: Archive legacy SCADA entries associated with this specific productive well path
    String findOldScadaSql = "SELECT sca_id FROM gw_db_schema.scada_systemc WHERE pro_id = ? AND recordate < ? AND data_colln_case = 'Operational' "
    		+ "AND datacondtion = 'Current'";
    try (PreparedStatement stmt = conn.prepareStatement(findOldScadaSql)) {
        stmt.setInt(1, generatedProId);
        stmt.setDate(2, opDto.getDataRecordedDate());
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                try (PreparedStatement up = conn.prepareStatement("UPDATE gw_db_schema.scada_systemc SET datacondtion = 'Historical' "
                		+ "WHERE sca_id = ?")) {
                    up.setInt(1, rs.getInt(1));
                    up.executeUpdate();
                }
            }
        }
    }

    // Step M: Insert fresh SCADA configurations natively and capture its key atomically
    int generatedScaId = 0;
    // Re-routing call map parameters cleanly to match database signature configurations
    java.sql.Array sqlSensorTypes = conn.createArrayOf("text", opDto.getSensorTypes() != null ? opDto.getSensorTypes() : new String[]{""});
    String insertScadaSql = "INSERT INTO gw_db_schema.scada_systemc(pro_id, conn_avialable, scada_status, sensor_type, data_colln_case, recordate, datacondtion) VALUES (?, ?, ?, ?, 'Operational', ?, 'Current')";
    
    try (PreparedStatement stmtScada = conn.prepareStatement(insertScadaSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtScada.setInt(1, generatedProId);
        stmtScada.setString(2, opDto.getSCADAConnection());
        stmtScada.setString(3, opDto.getSCADAstatus());
        stmtScada.setArray(4, sqlSensorTypes);
        stmtScada.setDate(5, opDto.getDataRecordedDate());
        stmtScada.executeUpdate();
        try (ResultSet gk = stmtScada.getGeneratedKeys()) { 
            if (gk.next()) generatedScaId = gk.getInt(1); 
        }
    } finally {
        if (sqlSensorTypes != null) {
            try { sqlSensorTypes.free(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Step N: Insert Power System Configuration details natively and capture its key atomically
    int generatedPowId = 0;
    String insertPowerSql = "INSERT INTO gw_db_schema.power_systemc(pro_id, power_source, st_gen_avialable, gen_status, gen_power) "
    		+ "VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmtPower = conn.prepareStatement(insertPowerSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtPower.setInt(1, generatedProId);
        stmtPower.setString(2, opDto.getPowerSource());
        stmtPower.setString(3, opDto.getStandByGeneratorAvialable());
        stmtPower.setString(4, opDto.getGeneratorStatus());
        stmtPower.setDouble(5, opDto.getGeneratorPower());
        stmtPower.executeUpdate();
        try (ResultSet gk = stmtPower.getGeneratedKeys()) { 
            if (gk.next()) generatedPowId = gk.getInt(1); 
        }
    }

    // Step O: Insert Observation Pip Configuration details natively and capture its key atomically
    int generatedPipId = 0;
    String insertPipSql = "INSERT INTO gw_db_schema.obsrvation_pipc(pro_id, pip_avialable, pip_status) VALUES (?, ?, ?)";
    try (PreparedStatement stmtPip = conn.prepareStatement(insertPipSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtPip.setInt(1, generatedProId);
        stmtPip.setString(2, opDto.getObservationPiPAvialable());
        stmtPip.setString(3, opDto.getStatusOfObservationPIP());
        stmtPip.executeUpdate();
        try (ResultSet gk = stmtPip.getGeneratedKeys()) { 
            if (gk.next()) generatedPipId = gk.getInt(1); 
        }
    }

    // Step P: Enforce structural duplication guard verification rules before logging final composite status row
    int generatedPwsId = 0;
    String checkDupStatusSql = "select count(*) from gw_db_schema.productive_well_status where prws_id=(select MAX(prws_id)"
    		+ "from gw_db_schema.productive_well_status as pws,gw_db_schema.productive_well as pw,gw_db_schema.constwell_status as cws,"
    		+ "gw_db_schema.con_discharge_test as cd,gw_db_schema.well_characteristics as ch,gw_db_schema.water_well as Ww where "
    		+ "pws.pro_id=pw.pro_id and pw.cws_id=cws.cws_id and cws.cdt_id=cd.cdt_id and cd.ch_id=ch.ch_id and ch.well_id=Ww.well_id and "
    		+ "pw.data_colln_case='Operational' and pw.record_date=? and pws.pro_id=?)limit 1";
    boolean isStatusDuplicate = false;
    try (PreparedStatement stmtCheck = conn.prepareStatement(checkDupStatusSql)) {
        stmtCheck.setInt(2, generatedProId);
        stmtCheck.setDate(1, opDto.getDataRecordedDate());
        try (ResultSet rs = stmtCheck.executeQuery()) {
            if (rs.next()) isStatusDuplicate = rs.getInt(1) > 0;
        }
    }
    
    if (isStatusDuplicate) {
        System.out.println("⚠️ Warning: Duplicated operational composite layout record matching Productive Well ID: " + generatedProId + " skipped.");
    } else {
        // Step Q: Finalize transaction mapping natively by linking generated keys into the status log
    	
        String insertFinalStatusSql = "INSERT INTO gw_db_schema.productive_well_status(pow_id, sca_id, pip_id, pro_id, wellfunstatus, "
        		+ "func_condition, reason_nonfun, reason_inactive, fun_stoped_date, datacondtion) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Current')";
                                    
        try (PreparedStatement stmtFinal = conn.prepareStatement(insertFinalStatusSql, Statement.RETURN_GENERATED_KEYS)) {
            if (generatedPowId == 0) stmtFinal.setNull(1, Types.INTEGER); else stmtFinal.setInt(1, generatedPowId);
            if (generatedScaId == 0) stmtFinal.setNull(2, Types.INTEGER); else stmtFinal.setInt(2, generatedScaId);
            if (generatedPipId == 0) stmtFinal.setNull(3, Types.INTEGER); else stmtFinal.setInt(3, generatedPipId);
            stmtFinal.setInt(4, generatedProId);
            
            stmtFinal.setString(5, opDto.getCurrentWellStatus());
            stmtFinal.setString(6, opDto.getFunctioningwellCondn());
            stmtFinal.setString(7, opDto.getInActiveReason());
            stmtFinal.setString(8, opDto.getReasonNonFunctioning());
            stmtFinal.setDate(9, opDto.getDateOfFunctionStoped());

            stmtFinal.executeUpdate();
            try (ResultSet gk = stmtFinal.getGeneratedKeys()) { if (gk.next()) generatedPwsId = gk.getInt(1); }
        }
    }

    // Step R: ATOMIC TREND LOGGING - Replaces legacy MAX() subqueries completely
    int generatedWqtId = 0;
    String insertWqtSql = "INSERT INTO gw_db_schema.waterqualitytrend(pws_id, salinity, temprature, extrnalpolun, goodwtrquality, "
    		+ "poorwaterquality, poorwaterqualityrsn) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING wqt_id";
    try (PreparedStatement stmtWqt = conn.prepareStatement(insertWqtSql, Statement.RETURN_GENERATED_KEYS)) {
    	double salinity=0;
        stmtWqt.setInt(1, generatedPwsId);
        stmtWqt.setDouble(2, salinity);
        stmtWqt.setDouble(3, opDto.getTemprature());
        stmtWqt.setString(4, opDto.getExtrnalpolun());
        stmtWqt.setString(5, opDto.getGoodwtrquality());
        stmtWqt.setString(6, opDto.getPoorwaterQuality_mineral());
        stmtWqt.setString(7, "Not Defined");
        stmtWqt.executeUpdate();
        try (ResultSet gk = stmtWqt.getGeneratedKeys()) { if (gk.next()) generatedWqtId = gk.getInt(1); }
    }

    int generatedWctId = 0;
    String insertWctSql = "INSERT INTO gw_db_schema.wellcoditiontrend(pws_id, highdrawdown, yielddeclining, rapidyielddecline, goodcondition) "
                        + "VALUES (?, ?, ?, ?, ?) RETURNING wellco_id";
    try (PreparedStatement stmtWct = conn.prepareStatement(insertWctSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtWct.setInt(1, generatedPwsId);
        stmtWct.setString(2, opDto.getHighdrawdown());
        stmtWct.setString(3, opDto.getYielddeclining());
        stmtWct.setString(4, opDto.getRapidyielddecline());
        stmtWct.setString(5, opDto.getGoodcondition());
        stmtWct.executeUpdate();
        try (ResultSet gk = stmtWct.getGeneratedKeys()) { if (gk.next()) generatedWctId = gk.getInt(1); }
    }

    // Step S: ATOMIC RECOMMENDATIONS & REHABILITATION RECORD LOGGING
    int generatedRecomId = 0;
    String insertRecomSql = "INSERT INTO gw_db_schema.recomendation(wellco_id, wqt_id, changepumpcapacity, loyerpumpposition, wellcleaning, comprehensive_reha, qualitytest_disinf, mantaindrainage, headmantainreconst, abandonmentseal, abandonmentreason) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmtRec = conn.prepareStatement(insertRecomSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtRec.setInt(1, generatedWctId);
        stmtRec.setInt(2, generatedWqtId);
        stmtRec.setString(3, opDto.getChangepumpcapacity());
        stmtRec.setString(4, opDto.getLoyerpumpposition());
        stmtRec.setString(5, opDto.getWellcleaning());
        stmtRec.setString(6, opDto.getComprehensive_reha());
        stmtRec.setString(7, opDto.getQualitytest_disinf());
        stmtRec.setString(8, opDto.getMantaindrainage());
        stmtRec.setString(9, opDto.getHeadmantainreconst());
        stmtRec.setString(10, opDto.getAbandonmentseal());
        stmtRec.setString(11, opDto.getAbandonmentreason());
        stmtRec.executeUpdate();
        try (ResultSet gk = stmtRec.getGeneratedKeys()) { if (gk.next()) generatedRecomId = gk.getInt(1); }
    }

    int generatedRcId = 0;
    String insertRcSql = "INSERT INTO gw_db_schema.rehabconducted(recom_id, conducted, redate, rehab_cycle) VALUES (?, ?, ?, 0)";
    try (PreparedStatement stmtRc = conn.prepareStatement(insertRcSql, Statement.RETURN_GENERATED_KEYS)) {
        stmtRc.setInt(1, generatedRecomId);
        stmtRc.setString(2, opDto.getRehabconducted());
        stmtRc.setDate(3, opDto.getRehabdate());
        stmtRc.executeUpdate();
        try (ResultSet gk = stmtRc.getGeneratedKeys()) { if (gk.next()) generatedRcId = gk.getInt(1); }
    }

    // Step T: FINAL REHAB PROGRAM CASCADE RECORD ENTRIES
    String insertRehabProgSql = "INSERT INTO gw_db_schema.rehabprogram(contruct_id, swl_before, swl_after, dwl_before, dwl_after, pump_pos, "
    		+ "pump_power, waterquality, qltyproblem, yield, spfc_capacity, effciency, hconductivity, transimitivity, storativity, rc_id, remark) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmtProg = conn.prepareStatement(insertRehabProgSql)) {
        stmtProg.setInt(1, 0); // Substituting loose context tracker 'contruct_id' cleanly
        stmtProg.setDouble(2, opDto.getSwl_before());
        stmtProg.setDouble(3, opDto.getSwl_after());
        stmtProg.setDouble(4, opDto.getDwl_before());
        stmtProg.setDouble(5, opDto.getDwl_after());
        stmtProg.setDouble(6, opDto.getPump_pos());
        stmtProg.setDouble(7, opDto.getPump_power());
        stmtProg.setString(8, opDto.getWaterquality());
        stmtProg.setString(9, opDto.getQltyproblem());
        stmtProg.setDouble(10, opDto.getYield());
        stmtProg.setDouble(11, opDto.getSpfc_capacity());
        stmtProg.setDouble(12, opDto.getEffciency());
        stmtProg.setDouble(13, opDto.getHconductivity());
        stmtProg.setDouble(14, opDto.getTransimitivity());
        stmtProg.setDouble(15, opDto.getStorativity());
        stmtProg.setInt(16, generatedRcId);
        stmtProg.setString(17, opDto.getRemark());
        stmtProg.executeUpdate();
    }
}

//save other doc
/**
 * Complete, high-integrity implementation of the Detailed Operations File Import pipeline.
 * Integrates row matrix mapping, Case 1 / Case 2 version controls, and final hash report logging.
 */
public List<File> saveotherofficefile(List<MultipartFile> operationalDocuments, int db_userID) throws SQLException {
    List<File> reportLogs = new ArrayList<>();
    List<String> wellIndexList = new ArrayList<>();

    // 1. Establish a single database connection using a try-with-resources container block
    try (Connection conn = ConnectionUtility.dbconnection()) {
        conn.setAutoCommit(false); // Enable manual transactions to guarantee atomic safety

        try {
            for (MultipartFile file : operationalDocuments) {
                if (file == null || file.isEmpty()) continue;

                String operationalFilename = file.getOriginalFilename();
                String operationalFileType = file.getContentType();
                int operationalfileSize    = (int) file.getSize();

                // 2. Delegate file parsing entirely to the Excel Parsing Service
                List<String[]> operationalRowsMatrix = excelParsingService.parseDetailedOperationsExcel(file);
                reportLogs.add(new File(1, "Successfully extracted " + operationalRowsMatrix.size() + " records from " + operationalFilename, "Info"));

                java.sql.Date dataRecordedDate = null;

                // 3. ROW Pass Loop: Parse data matrix and execute version controls dynamically
                for (String[] rowData : operationalRowsMatrix) {
                    if (rowData == null || rowData.length < 113) continue;

                    OperationalImportDTO opDto = new OperationalImportDTO();

                    // Extract core loop fields
                    opDto.setWellCellCode(rowData[0]);
                    opDto.setWellCellIndex(rowData[1]);
                    opDto.setWaterUsageLicence(rowData[23]);
                    opDto.setDataRecordedDate(parseExcelDate(rowData[24]));
                    dataRecordedDate = opDto.getDataRecordedDate(); // Isolate date context tracking index
                    opDto.setEnumratorCampanyN(rowData[25]);
                    opDto.setEnumratorName(rowData[26]);
                    opDto.setEnumratoremail(rowData[27]);
                    opDto.setEnumratorPhone(rowData[28]);
                    opDto.setCurrentWellStatus(rowData[29]);
                    opDto.setDateOfFunctionStoped(parseExcelDate(rowData[30])); //Date
                    opDto.setReasonNonFunctioning(rowData[31]);
                    opDto.setFunctioningwellCondn(rowData[32]);
                    opDto.setInActiveReason(rowData[33]);
                    opDto.setWellHeadCondition(rowData[34]);
                    opDto.setPoorWellHeadReason(rowData[35]);
                    opDto.setDrainageCondition(rowData[36]);
                    opDto.setPowerSource(rowData[37]);
                    opDto.setStandByGeneratorAvialable(rowData[38]);
                    opDto.setGeneratorStatus(rowData[39]);
                    opDto.setGeneratorPower(Double.parseDouble(rowData[40]));
                    opDto.setObservationPiPAvialable(rowData[41]);
                    opDto.setStatusOfObservationPIP(rowData[42]);
                    opDto.setSCADAConnection(rowData[43]);
                    opDto.setSCADAstatus(rowData[44]);
                    opDto.setMonitoringSencorIns(rowData[45]);
                    opDto.setSensorTypes(rowData[46].split("[,\\s]+")); //SCADA Sensor type
                    opDto.setStaticWL(Double.parseDouble(rowData[47]));
                    opDto.setDynamicWL(Double.parseDouble(rowData[48]));
                    opDto.setAbstractionRate(Double.parseDouble(rowData[49]));
                    opDto.setElecConductivity(Double.parseDouble(rowData[50]));
                    opDto.setpHValue(Double.parseDouble(rowData[51]));
                    opDto.setTemprature(Double.parseDouble(rowData[52]));
                    opDto.setWaterColor(rowData[53]);
                    opDto.setWaterOdor(rowData[54]);
                    opDto.setOperationStartedDate(parseExcelDate(rowData[55]));
                    opDto.setPumpPosition(Double.parseDouble(rowData[56]));
                    opDto.setPumpCapacity(Double.parseDouble(rowData[60]));
                    opDto.setPumpHead(rowData[61]); //BJ
                    opDto.setDesignedGeneratoCapacity(rowData[62]);
                    opDto.setCyieldDyield(rowData[63]);
                    opDto.setGwPotableStatus(rowData[64]);
                    opDto.setNonPotableReason(rowData[65]);
                    opDto.setWellProductivity(rowData[66]); //column BO
					opDto.setGoodcondition(rowData[67]); 
					opDto.setGoodwtrquality(rowData[68]); 
					opDto.setHighdrawdown(rowData[69]); 
					opDto.setYielddeclining(rowData[70]);
					opDto.setRapidyielddecline(rowData[71]); 
					opDto.setPoorwaterQuality_mineral(rowData[72]); 
					opDto.setExtrnalpolun(rowData[73]); 
					opDto.setChangepumpcapacity(rowData[74]); 
					opDto.setLoyerpumpposition(rowData[75]); 
					opDto.setWellcleaning(rowData[76]); 
					opDto.setComprehensive_reha(rowData[77]); 
					opDto.setQualitytest_disinf(rowData[78]); 
					opDto.setMantaindrainage(rowData[79]); 
					opDto.setHeadmantainreconst(rowData[80]); 
					opDto.setAbandonmentseal(rowData[81]); 
					opDto.setAbandonmentreason(rowData[82]); //CE
					opDto.setRehabconducted(rowData[92]); //CO
					opDto.setRehabdate(parseExcelDate(rowData[93])); 
					opDto.setRehabCompanyName(rowData[94]); 
					opDto.setRehabEmail(rowData[95]); 
					opDto.setRehabPhone(rowData[96]); 
					opDto.setSwl_before(Double.parseDouble(rowData[97])); 
					opDto.setSwl_after(Double.parseDouble(rowData[98])); 
					opDto.setDwl_before(Double.parseDouble(rowData[99])); 
					opDto.setDwl_after(Double.parseDouble(rowData[100])); 
					opDto.setPump_pos(Double.parseDouble(rowData[101])); 
					opDto.setPump_power(Double.parseDouble(rowData[102])); 
					opDto.setYield(Double.parseDouble(rowData[103])); 
					opDto.setSpfc_capacity(Double.parseDouble(rowData[104])); 
					opDto.setEffciency(Double.parseDouble(rowData[105])); 
					opDto.setHconductivity(Double.parseDouble(rowData[106])); 
					opDto.setTransimitivity(Double.parseDouble(rowData[107])); 
					opDto.setStorativity(Double.parseDouble(rowData[108])); 
					opDto.setWaterquality(rowData[109]);
					opDto.setQltyproblem(rowData[110]);
                    opDto.setRemark(rowData[112]);

                    // Resolve matching database entity identifier safely
                    int wellId = findWellIdByIndex(conn, opDto.getWellCellIndex());
                    if (wellId == 0) continue; // Skip orphaned spreadsheet data rows

                    wellIndexList.add(opDto.getWellCellIndex().trim());

                    // Fire the version archiving state machine transactionally row-by-row
                    executeOperationalVersioningPipeline(conn, wellId, opDto, db_userID);
                }

                // 🫵 PLACE OF SELECTED SECTION: Added right here at file completion closure!
                // ─── TRANSACTION COMPONENT AT FILE LEVEL ENTRY CLOSURE ───
                byte[] fileContent = fileProcessingService.convertStreamToByteArray(file.getInputStream());
                String hashvalue = fileHashingService.calculateSHA256(fileContent);
                int operationrepo_id = 0;

                String checkReportSql = "SELECT repo_id FROM gw_db_schema.wellreport WHERE hash_value = ? LIMIT 1";
                try (PreparedStatement stmtReportCheck = conn.prepareStatement(checkReportSql)) {
                    stmtReportCheck.setString(1, hashvalue);
                    try (ResultSet rs = stmtReportCheck.executeQuery()) {
                        if (rs.next()) {
                            operationrepo_id = rs.getInt(1);
                        }
                    }
                }

                if (operationrepo_id == 0 && !wellIndexList.isEmpty()) {
                    // Standardize category labels cleanly since cell extraction validation completed successfully
                    String dataCategory = "Detail Operation Well Inventory Data";
                    
                    String[] listallWellIndex = wellIndexList.toArray(new String[0]);
                    java.sql.Array sqlArray_listallwellIndex = conn.createArrayOf("text", listallWellIndex);
                    
                    String insertReportSql = "INSERT INTO gw_db_schema.wellreport(wellindexlist, recordate, filename, filesize, filetype, reportdoc, hash_value, datacategory) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement prepstatewellReport = conn.prepareStatement(insertReportSql, Statement.RETURN_GENERATED_KEYS)) {
                        prepstatewellReport.setArray(1, sqlArray_listallwellIndex);
                        prepstatewellReport.setDate(2, dataRecordedDate);
                        prepstatewellReport.setString(3, operationalFilename);
                        prepstatewellReport.setInt(4, operationalfileSize);
                        prepstatewellReport.setString(5, operationalFileType);
                        prepstatewellReport.setBytes(6, fileContent);
                        prepstatewellReport.setString(7, hashvalue);
                        prepstatewellReport.setString(8, dataCategory);
                        prepstatewellReport.executeUpdate();
                        
                        try (ResultSet gk = prepstatewellReport.getGeneratedKeys()) {
                            if (gk.next()) operationrepo_id = gk.getInt(1);
                        }
                    } finally {
                        if (sqlArray_listallwellIndex != null) {
                            try { sqlArray_listallwellIndex.free(); } catch (SQLException e) { e.printStackTrace(); }
                        }
                    }
                    
                    // Execute final downstream repository updates safely
                    udateWellStatusRepoId(conn, operationrepo_id);
                    reportLogs.add(new File(1, "Operational Data log package successfully finalized for file: " + operationalFilename, "Success"));
                }
            }
            
            conn.commit(); // Push structural updates atomically to production database disk storage
            
        } catch (Exception e) {
            conn.rollback(); // Rollback everything instantly if an excel row parsing exception hits midway
            e.printStackTrace();
            throw new SQLException("Operational processing pipeline aborted transaction safely due to data format parsing anomalies: " + e.getMessage(), e);
        }
    }
    return reportLogs;
}

//Insert to Physical Table
private static void addPhysicaldata(Connection connection,int gwCell_id,double temprature,double electricConductivity,java.sql.Date recorDate,
		String dataCollectedCase,String waterColor,String waterOdor,String dataCondition)
		throws Exception {
String query = "insert into gw_db_schema.physical_da(gw_id,temprature,elec_condu,recordate,data_colln_case,water_color,water_odor,datacondtion)"
		+ "values(?,?,?,?,?,?,?,?)";
try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
		preparedStatement.setInt(1, gwCell_id);
		preparedStatement.setDouble(2, temprature);
		preparedStatement.setDouble(3, electricConductivity);
		preparedStatement.setDate(4, recorDate);
		preparedStatement.setString(5, dataCollectedCase);
		preparedStatement.setString(6, waterColor);
		preparedStatement.setString(7, waterOdor);
		preparedStatement.setString(8, dataCondition);
		preparedStatement.executeUpdate();
}
}
//Insert into Chemical Table
private static void addChemicaldata(Connection connection,int gwCell_id,java.sql.Date recorDate,double pH,String dataCollectedCase,
		String dataCondition)
		throws Exception {
String query = "insert into gw_db_schema.chemical_da(gw_id,recordate,ph,data_colln_case,datacondtion)values(?,?,?,?,?)";
try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	preparedStatement.setInt(1, gwCell_id);
	preparedStatement.setDate(2, recorDate);
	preparedStatement.setDouble(3, pH);
	preparedStatement.setString(4, dataCollectedCase);
	preparedStatement.setString(5, dataCondition);
    preparedStatement.executeUpdate();
}
}
//Productive Update WCS
public List<File> updateWCRData(int user_id,int well_id,int geolocID,String wellCode,double easting,double northing,double longT,
		double lat,double elevation,java.sql.Date constYear,String dirillingL,double wellDepth,double wellYield,double SWL,double DWL,
		double specificCapacity,String wellOwnerName,String wellOwnerEmail,String wellOwnerHpone,int dbUserId,
		String CRSOwnerCatName,double CRlargeCasingName,double CRtelCasingName,String CRCasingMaterName,String mainAquifer,String CRwellTypeName,
		String CRabanRWellName,String CRsealedYNName,java.sql.Date CRSealedDateName,String CRpumpstatusName,
		java.sql.Date CRpumpInstalledDateName,double CRpumpCapacityName,String CRpumpHeadName,double CRpumpPosiName,
		double CRdischargeRateName,String CRgeneratorStaName,double CRcapacityCapaName,String SCADAName,String SCADACStatusName)
		throws Exception {
	int chID = 0,constDischargeId = 0,casingId=0,pumpID=0,SCADAsystemID=0;
	double longtCheck=0,latCheck=0,eastingCheck=0,northingCheck=0,l_elevation=0,well_depthCheck=0,largecasingCheck=0,telescopedcasingCheck=0,
			pump_capacityCheck=0,pump_positionCheck=0,abstraction_rateCheck=0,yield_lpsCheck=0,swl_mCheck=0,dwl_mCheck=0,specific_capacityCheck=0;
	String wellcodeCheck="",well_typeCheck="",main_aquiferCheck="",drilling_licenseCheck="",casing_mtrlCheck="",wellownercatgoryCheck="",
			pump_statusCheck="",pumpheadCheck="",conn_avialableCheck="",scada_statusCheck="",emailCheck="",orgnameCheck="",phoneCheck="",cntn_yearCheck=null;
	LocalDateTime timedateNow= LocalDateTime.now(ZoneId.systemDefault());
	String wellIndex = "";
	File noValue=null;
	List<File> updateRe=new ArrayList<File>();
	conn=ConnectionUtility.dbconnection();
	String querywellCha="select cdt.cdt_id,ch.ch_id,ww.well_index,ps.pump_id from gw_db_schema.water_well as ww,gw_db_schema.pumping_status as ps,"
			+ "gw_db_schema.well_characteristics as ch,gw_db_schema.con_discharge_test as cdt where ch.well_id=ww.well_id and ps.ch_id=ch.ch_id and "
			+ "cdt.ch_id=ch.ch_id and cdt.data_colln_case='Well Completion Report' and ps.data_colln_case='Well Completion Report' and "
			+ "ww.well_id="+well_id+"";
	Statement stmntWellCH=conn.createStatement();
	ResultSet rswellCh=stmntWellCH.executeQuery(querywellCha);
	while(rswellCh.next()){
		constDischargeId=rswellCh.getInt(1);
		chID=rswellCh.getInt(2);
		wellIndex=rswellCh.getString(3);
		pumpID=rswellCh.getInt(4);
	}
	//Productive well status information Insertion Function call
	String scadaUpdate="select sca_id from gw_db_schema.scada_systemc where sca_id=(select MAX(sca_id)from gw_db_schema.scada_systemc as scad,"
			+ "gw_db_schema.productive_well as pw,gw_db_schema.constwell_status as cws,gw_db_schema.gw_data as gw,gw_db_schema.con_discharge_test as cd,"
			+ "gw_db_schema.well_characteristics as ch,gw_db_schema.water_well as Ww where scad.pro_id=pw.pro_id and pw.cws_id=cws.cws_id and "
			+ "cws.cdt_id=cd.cdt_id and cd.ch_id=ch.ch_id and cws.gw_id=gw.gw_id and ch.well_id=Ww.well_id and gw.well_id=Ww.well_id and "
			+ "Ww.well_id="+well_id+")limit 1";
	Statement statescadaUpdate=conn.createStatement();
	ResultSet rsscadaUpdate=statescadaUpdate.executeQuery(scadaUpdate);
	while(rsscadaUpdate.next()) {
		SCADAsystemID=rsscadaUpdate.getInt(1);
	}
	String querycasing="select ci.ci_id from gw_db_schema.water_well as ww,gw_db_schema.casing_info as ci "
			+ "where ci.well_id=ww.well_id and ww.well_id="+well_id+"";
	Statement stmntcasing=conn.createStatement();
	ResultSet rscasing=stmntcasing.executeQuery(querycasing);
	while(rscasing.next()){
		casingId=rscasing.getInt(1);
	}
	//Productive well status information Insertion Function call
	 List<ValuePair>list_checkPair=new ArrayList<>();
	String checkElements_ifUpdated ="select gl.longt,gl.lat,gl.easting,gl.northing,gl.l_elevation,ww.wellcode,ww.well_type,ww.well_depth,"
			+ "extract(year from ww.cntn_year),ww.main_aquifer,ww.wellownercatgory,ww.drilling_license,ci.largecasing,ci.telescopedcasing,"
			+ "ci.casing_mtrl,ps.pump_capacity,ps.pump_position,ps.pump_status,ps.pumphead,ps.abstraction_rate,cd.yield_lps,cd.swl_m,cd.dwl_m,"
			+ "ch.specific_capacity,sc.conn_avialable,sc.scada_status,dbu.email,dbu.orgname,dbu.phone from gw_db_schema.scada_systemc as sc,"
			+ "gw_db_schema.productive_well as pw,gw_db_schema.constwell_status as cws,gw_db_schema.pumping_status as ps,gw_db_schema.con_discharge_test "
			+ "as cd,gw_db_schema.db_user as dbu,gw_db_schema.well_characteristics as ch,gw_db_schema.casing_info as ci,gw_db_schema.water_well as ww,"
			+ "gw_db_schema.geo_location as gl where sc.pro_id=pw.pro_id and pw.cws_id=cws.cws_id and cws.cdt_id=cd.cdt_id and cd.ch_id=ch.ch_id and "
			+ "ps.ch_id=ch.ch_id and ch.well_id=Ww.well_id and gl.geoloc_id=ww.geoloc_id and dbu.id=ww.owned_by and ci.well_id=ww.well_id and "
			+ "cd.data_colln_case='Well Completion Report' and ps.data_colln_case='Well Completion Report' and ww.well_id="+well_id+"";
	Statement stmtcheckIfUpdated=conn.createStatement();
	ResultSet rscheckIfUpdated=stmtcheckIfUpdated.executeQuery(checkElements_ifUpdated);
	while(rscheckIfUpdated.next()) {
		longtCheck=rscheckIfUpdated.getDouble(1);
		latCheck=rscheckIfUpdated.getDouble(2);
		eastingCheck=rscheckIfUpdated.getDouble(3);
		northingCheck=rscheckIfUpdated.getDouble(4);
		l_elevation=rscheckIfUpdated.getDouble(5);
		wellcodeCheck=rscheckIfUpdated.getString(6);
		well_typeCheck=rscheckIfUpdated.getString(7);
		well_depthCheck=rscheckIfUpdated.getDouble(8);
		cntn_yearCheck=rscheckIfUpdated.getString(9);
		main_aquiferCheck=rscheckIfUpdated.getString(10);
		wellownercatgoryCheck=rscheckIfUpdated.getString(11);
		drilling_licenseCheck=rscheckIfUpdated.getString(12);
		largecasingCheck=rscheckIfUpdated.getDouble(13);
		telescopedcasingCheck=rscheckIfUpdated.getDouble(14);
		casing_mtrlCheck=rscheckIfUpdated.getString(15);
		pump_capacityCheck=rscheckIfUpdated.getDouble(16);
		pump_positionCheck=rscheckIfUpdated.getDouble(17);
		pump_statusCheck=rscheckIfUpdated.getString(18);
		pumpheadCheck=rscheckIfUpdated.getString(19);
		abstraction_rateCheck=rscheckIfUpdated.getDouble(20);
		yield_lpsCheck=rscheckIfUpdated.getDouble(21);
		swl_mCheck=rscheckIfUpdated.getDouble(22);
		dwl_mCheck=rscheckIfUpdated.getDouble(23);
		specific_capacityCheck=rscheckIfUpdated.getDouble(24);
		conn_avialableCheck=rscheckIfUpdated.getString(25);
		scada_statusCheck=rscheckIfUpdated.getString(26);
		emailCheck=rscheckIfUpdated.getString(27);
		orgnameCheck=rscheckIfUpdated.getString(28);
		phoneCheck=rscheckIfUpdated.getString(29);
		ValuePair valleuP=new ValuePair(longtCheck, latCheck, eastingCheck, northingCheck, l_elevation, well_depthCheck, largecasingCheck,
				telescopedcasingCheck, pump_capacityCheck, pump_positionCheck, abstraction_rateCheck, yield_lpsCheck, swl_mCheck, dwl_mCheck, 
				specific_capacityCheck, wellcodeCheck, well_typeCheck, main_aquiferCheck, drilling_licenseCheck, casing_mtrlCheck, wellownercatgoryCheck,
				pump_statusCheck, pumpheadCheck, conn_avialableCheck, scada_statusCheck, emailCheck, orgnameCheck, phoneCheck, cntn_yearCheck);
		list_checkPair.add(valleuP);
	}
	for(ValuePair value: list_checkPair) {
		System.out.println("What is the invoked Value of Orgnamecheck is " +value.orgnameCheck+" is = to "+ wellOwnerName);
		  // Compare user input string with the item's name
        if ((value.longtCheck!=longT) || (value.latCheck!=lat) || (value.eastingCheck!=easting) || (value.northingCheck!=northing) || 
        		(value.l_elevation!=elevation)){
        	String query ="update gw_db_schema.geo_location set easting="+easting+",northing="+northing+",longt="+longT+",lat="+lat+",l_elevation="+elevation+" "
        			+ "where geoloc_id= "+geolocID+";"
        			+"insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: geo_location (Location Coordinate) table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}
        }else if((!value.wellcodeCheck.equals(wellCode))|| (value.well_depthCheck!=wellDepth) || (!value.main_aquiferCheck.equals(mainAquifer))||
        		(!value.wellownercatgoryCheck.equals(CRSOwnerCatName))|| (!value.drilling_licenseCheck.equals(dirillingL)) || 
        		(!value.well_typeCheck.equals(CRwellTypeName))){
        	String query = "update gw_db_schema.water_well set wellcode='"+wellCode+"',well_depth="+wellDepth+",cntn_year=?,main_aquifer='"+mainAquifer+"',"
        			+ "wellownercatgory='"+CRSOwnerCatName+"',drilling_license='"+dirillingL+"',well_type='"+CRwellTypeName+"' where well_id="+well_id+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: water_well (Water Well) Data table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setDate(1, constYear);
        		preparedStatement.setObject(2,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}
        }else if((!value.casing_mtrlCheck.equals(CRCasingMaterName)) || (value.largecasingCheck!=CRlargeCasingName) || 
        		(value.telescopedcasingCheck!=CRtelCasingName)) {
        	String query = "update gw_db_schema.casing_info set largecasing="+CRlargeCasingName+",telescopedcasing="+CRtelCasingName+","
        			+ "casing_mtrl='"+CRCasingMaterName+"' where ci_id="+casingId+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: casing_info (Well Casing Information) table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}
        }else if(value.pump_capacityCheck!=CRpumpCapacityName || value.pump_positionCheck!=CRpumpPosiName || 
        		(!value.pump_statusCheck.equals(CRpumpstatusName)) || (!value.pumpheadCheck.equals(CRpumpHeadName)) || 
        		value.abstraction_rateCheck!=CRdischargeRateName) {
        	String query = "update gw_db_schema.pumping_status set pump_capacity="+CRpumpCapacityName+",pump_position="+CRpumpPosiName+","
        			+ "pump_status='"+CRpumpstatusName+"',pumphead='"+CRpumpHeadName+"',abstraction_rate="+CRdischargeRateName+" where pump_id="+pumpID+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: pumping_status(Pumping System Status) data table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}
        }else if(value.yield_lpsCheck!=wellYield || value.swl_mCheck!=SWL || value.dwl_mCheck!=DWL){
        	String query = "update gw_db_schema.con_discharge_test set yield_lps="+wellYield+",swl_m="+SWL+",dwl_m="+DWL+" where "
        			+ "cdt_id="+constDischargeId+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: con_discharge_test(Constant discharge Test) data table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}	
        }else if(value.specific_capacityCheck!=specificCapacity){
        	String query = "update gw_db_schema.well_characteristics set specific_capacity="+specificCapacity+" where ch_id="+chID+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: well_characteristics (Specific Well Charactersitics) table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}		
        }else if((!value.scada_statusCheck.equals(SCADACStatusName)) || (!value.conn_avialableCheck.equals(SCADAName))) {
        	String query = "update gw_db_schema.scada_systemc set conn_avialable='"+SCADAName+"',scada_status='"+SCADACStatusName+"' where "
        			+ "sca_id="+SCADAsystemID+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: scada_systemc(SCADA Information) Table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}	 	
        }else if((!value.orgnameCheck.equals(wellOwnerName)) || (!value.phoneCheck.equals(wellOwnerHpone))){
        	System.out.println("User ID for Update is "+user_id);
        	String query = "update gw_db_schema.db_user set email='"+wellOwnerEmail+"',orgname='"+wellOwnerName+"',phone='"+wellOwnerHpone+"'"
        			+ " where id="+user_id+";"
        			+ "insert into gw_db_schema.database_log(well_id,user_id,actiontook,datehapped,datasheetype)values("+well_id+","+dbUserId+","
        			+ "'Contents: db_user(database User Management) Table Updated',?,'Well Completion Report')";
        	try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
        		preparedStatement.setObject(1,timedateNow);
        		preparedStatement.executeUpdate();
        		File updateWCR=new File(well_id,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
        		updateRe.add(updateWCR);
        	}catch (Exception e) {
        		e.printStackTrace();
        		File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Well Completion Report Data in the Repository is Not Affected"
        				+ " by this Updation Query",noValue); 
        		updateRe.add(updateWCR);
        	}	
        }else {	
        	File updateWCR=new File(0,"<font color=#8B8000>Warning,</font>",""+wellIndex+" Non table data is Affected"
    				+ " by this Update Query",noValue); 
    		updateRe.add(updateWCR);
        }
	}

return updateRe;
}
//Productive Well Statistical Data
public List<File> updateGWOData(int userID,int wellID,String wellIndex,String waterUserLName,java.sql.Date wellOpernStartDate,
		String enumCompanyName,String enumEmailName,String enumEmailPhoneName)
		throws Exception {
	int proId = 0;
	List<File> updateRe=new ArrayList<File>();
	conn=ConnectionUtility.dbconnection();
	String querywellCha="select pro.pro_id from gw_db_schema.water_well as ww,gw_db_schema.well_characteristics as ch,"
			+ "gw_db_schema.con_discharge_test as cdt,gw_db_schema.constwell_status as cws,gw_db_schema.productive_well as pro where "
			+ "ch.well_id=ww.well_id and cdt.ch_id=ch.ch_id and cdt.cdt_id=cws.cdt_id and cws.cws_id=pro.cws_id and cws.data_colln_case="
			+ "'Operational' and ww.well_id="+wellID+"";
	Statement stmntWellCH=conn.createStatement();
	ResultSet rswellCh=stmntWellCH.executeQuery(querywellCha);
	while(rswellCh.next()){
		proId=rswellCh.getInt(1);
	}
String query = "update gw_db_schema.productive_well set wateruse_license='"+waterUserLName+"',pumping_operen_date=? where pro_id="+proId+";"
		+ "update gw_db_schema.db_user set email='"+enumEmailName+"',orgname='"+enumCompanyName+"',phone='"+enumEmailPhoneName+"' "
		+ "where id="+userID+"";
try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
	preparedStatement.setDate(1, wellOpernStartDate);
	preparedStatement.executeUpdate();
	double noValue=0.0;
	File updateWCR= new File(wellID,"<font color=#06402B>Success,</font>","Succefully Updated",noValue);
	updateRe.add(updateWCR);
}catch (Exception e) {
	e.printStackTrace();
	double noValue=0.0;
	File updateWCR=new File(0,"<font color=#8B8000>Error,</font>",""+wellIndex+" Well's Groundwater Operational Data in the Repository is Not "
			+ "Affected by this Updation Query",noValue); 
	updateRe.add(updateWCR);
}
return updateRe;
}
//ask request
public List<File>ask_request(int center,int process,int projcet_n,String Desi,String userId,String fname,String lname){
		List<File>files=new ArrayList<File>();
		try {
			int user_id=0;
			int doc_id=0;
			 byte []bb = null;
			// String doc_name="";
								conn=ConnectionUtility.dbconnection();
					String user="select Ln.l_id from hydrogeos.login11 as Ln where Ln.user1='"+userId+"' and Ln.f_name='"+fname+"'";
								Statement stmn=conn.createStatement();
								ResultSet rs1=stmn.executeQuery(user);
								while(rs1.next()){
									user_id=rs1.getInt(1);
								}
			String sql="select As1.cat_id,dc.cat_name,As1.l_id,Ln.user1 from hydrogeos.national_cor_center as Na,hydrogeos.eco_process as Pr,hydrogeos.project_data as Cl,"
					+ "hydrogeos.doc_cat as dc,hydrogeos.login11 as Ln,hydrogeos.askprivlage as As1 where Na.na_id=Pr.na_id and Pr.pro_id=dc.process_id and "
					+ "Cl.pro_def_id=dc.pro_id and dc.cat_id=As1.cat_id and Ln.l_id=As1.l_id and Na.na_id="+center+" and Pr.pro_id="+process+" and "
							+ "Cl.pro_def_id="+projcet_n+" and dc.cat_id="+Desi+" and Ln.user1='"+userId+"' and Ln.f_name='"+fname+"'";
						Statement stmt = conn.createStatement();
						 ResultSet rs = stmt.executeQuery(sql);
						  if(rs.next()) {
							  doc_id=rs.getInt(1);
							  //doc_name=rs.getString(2);
							  user_id=rs.getInt(3);
							  //String username=rs.getString(4); 
	File ffile=new File(doc_id, "<font color=#FF0000>"+"Documents Request is Already Sent !! "+ " </font>", bb); 
							  files.add(ffile);							  
		//System.out.println("Request is Already Asked with Id= "+doc_id+" and name= "+doc_name+" and UserId= "+user_id+" and Username= "+username);  
						  }
						  else{	 
							  LocalDateTime now;
							  DateTimeFormatter timeformat;
							  timeformat= DateTimeFormatter.ofPattern("yyyy/MM/dd");
							  now=LocalDateTime.now();	
String quercenter="insert into hydrogeos.askprivlage(process_id,cat_id,l_id,asked_date)values("+process+","+Desi+","+user_id+",'"+timeformat.format(now)+"')";
				 PreparedStatement statmnt = conn.prepareStatement(quercenter);
				 statmnt.executeUpdate(); 
				 File ffile=new File(doc_id, "Your request is Succefully sent!!", bb);
				  files.add(ffile);	
							  }
		        
		} catch (Exception e) {
		e.printStackTrace();
		}
		return files;
	}

}

package net.AAWSAgDB.fileupload.dao;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.AAWSAgDB.fileupload.ValuePair;
import net.AAWSAgDB.fileupload.model.File;
import net.AAWSAgDB.fileupload.model.UserSessionProfile;
@Repository // Tells Spring to manage this class instance safely
public class LoginDao {
	public static long sizecheck=0;
	private int userId;
    private int userLo;

    // Getters and setters for tracking session state variables safely
    public int getUserId() { return userId; }
    public int getUserLo() { return userLo; }
    public void setUserLo(int userLo) { this.userLo = userLo; }
	
 // validate Elite User
 // 1. Refined Elite User Validation Method
    public boolean validateElite(String name, String pass) {
        boolean status = false;
        String sql = "SELECT id FROM gw_db_schema.db_user WHERE username = ? AND password = ?";

        // Try-with-resources guarantees the connection instantly drops back to the pool
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, name);
            pst.setString(2, pass);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    this.userId = rs.getInt("id");
                    status = true;
                }
            }
        } catch (Exception ee) {
            System.err.println("Error validating elite user: " + ee.getMessage());
            ee.printStackTrace();
        }
        return status;
    }
	//validate corporate User
    // 2. Refined Corporate User Validation Method
    public int validateCor(String name, String userName) {
        String sql = "SELECT id FROM gw_db_schema.db_user WHERE fullname = ? AND username = ?";
        int fetchedId = 0;

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, name);
            pst.setString(2, userName);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    fetchedId = rs.getInt("id");
                    this.userLo = fetchedId; // Sets your tracking marker safely
                }
            }
        } catch (Exception ee) {
            System.err.println("Error verifying corporate details: " + ee.getMessage());
            ee.printStackTrace();
        }
        return fetchedId;
    }
	//validate corporate User
 // 3. Refined Single User Detail Extraction Method
    public List<File> userDetails(String userName) {
        List<File> userDetailsList = new ArrayList<>();
        String sql = "SELECT fullname, usertype FROM gw_db_schema.db_user WHERE username = ?";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, userName);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(1);
                    String userType = rs.getString(2);
                    
                    // Create clean file models without unclosed stream associations
                    File fileModel = new File(name, 0, userType, null);
                    userDetailsList.add(fileModel);
                }
            }
        } catch (Exception ee) {
            System.err.println("Error extracting user properties: " + ee.getMessage());
            ee.printStackTrace();
        }
        return userDetailsList;
    }
	
    /**
     * Resolves active security privilege contexts and session profiles for landing dashboards.
     * Protected via try-with-resources to completely avoid open connections hanging.
     */
    public UserSessionProfile getUserLandingProfile(String username) {
        UserSessionProfile profile = new UserSessionProfile();
        
        String userSql = "SELECT fullname,id FROM gw_db_schema.db_user WHERE username = ?";
        String accessSql = "SELECT us.vern_code, at.authority FROM gw_db_schema.db_user as us "
                         + "JOIN gw_db_schema.authorities as at ON us.id = at.user_id "
                         + "WHERE at.username = ?";

        try (Connection conn = ConnectionUtility.dbconnection()) {
            
            // 1. Resolve Full Name safely
            try (PreparedStatement stmtUser = conn.prepareStatement(userSql)) {
                stmtUser.setString(1, username);
                try (ResultSet rs = stmtUser.executeQuery()) {
                    if (rs.next()) {
                        profile.setFullName(rs.getString(1));
                        profile.setUserId(rs.getInt(2));
                    }
                }
            }

            // 2. Resolve Privilege Access Keys
            try (PreparedStatement stmtAccess = conn.prepareStatement(accessSql)) {
                stmtAccess.setString(1, username);
                try (ResultSet rs = stmtAccess.executeQuery()) {
                    if (rs.next()) {
                        profile.setPrivilegeCode(rs.getString(1));
                        profile.setAuthority(rs.getString(2));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error extracting active dashboard authorizations.");
            e.printStackTrace();
        }
        return profile;
    }
    

	public static List<File> data_investigated(int cat_name){
		List<File> files=new ArrayList<File>();
		String query="select id,parameterp from gw_db_schema.addtionalparm where contentp=?";
		try (Connection conn= ConnectionUtility.dbconnection();
				PreparedStatement statmnt=conn.prepareStatement(query)){
			statmnt.setString(1, "Well Field");

			try(ResultSet rs = statmnt.executeQuery(query)){
				while(rs.next()){
					int basin_id=rs.getInt(1);
					String basin_name=rs.getString(2);
					File ff=new File(basin_id,basin_name,0);
					files.add(ff);
				}	
			}
			
		}catch(Exception ee){
		ee.printStackTrace();	
		}
		return files;
	}
	//access sample category
    /**
     * Retrieves all system users managed under core administrative roles.
     * Protected via strict try-with-resources blocks to completely eliminate connection leaks.
     */
    public List<ValuePair> userAccountManagement(int adminID) throws SQLException {
        List<ValuePair> files = new ArrayList<>();
        
        // FIXED: Enforce parenthesis structure on boolean query parameters to ensure id matching runs correctly
        String query = "SELECT id, fullname, username, usertype, enabled FROM gw_db_schema.db_user "
                     + "WHERE (usertype = 'Common User' OR usertype = 'Database User' OR usertype = 'Administrator') "
                     + "AND id != ? ORDER BY id ASC";

        try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = conn.prepareStatement(query)) {
            
            statmnt.setInt(1, adminID);
            
            try (java.sql.ResultSet rs = statmnt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt(1);
                    String fullName = rs.getString(2);
                    String userName = rs.getString(3);
                    String userType = rs.getString(4);
                    boolean enabled = rs.getBoolean(5);
                    
                    String boolValue = enabled ? "Active" : "Not Active";
                    String InterfaceUserType= userType.equals("Database User") ? "Database Operator"
                    		:userType.equals("Common User") ? "Portal User"
                    		:"Administrator";
                    ValuePair ff = new ValuePair(userId, fullName, userName, InterfaceUserType, boolValue);
                    files.add(ff);
                }
            }
        }
        return files;
    }

    /**
     * Updates an individual user's privilege code and account activation status transactionally.
     * Automatically returns a refreshed list of active profiles within a single, secure connection wrapper.
     */
    public List<ValuePair> updateUserAccount(int userId, int privilege, String accountStatus, int adminID) throws SQLException {
        String userType = "";
        if (privilege == 110) userType = "Administrator";
        else if (privilege == 120) userType = "Database User";
        else if (privilege == 123) userType = "Common User";

        boolean isUndefinedStatus = "undefined".equalsIgnoreCase(accountStatus);
        Boolean accountSt = !isUndefinedStatus ? Boolean.parseBoolean(accountStatus) : null;

        // Dynamic, fully parameterized query building strategies
        String updateSql;
        if (privilege != 4444 && !isUndefinedStatus) {
            updateSql = "UPDATE gw_db_schema.db_user SET vern_code = ?, usertype = ?, enabled = ? WHERE id = ?";
        } else if (privilege != 4444 && isUndefinedStatus) {
            updateSql = "UPDATE gw_db_schema.db_user SET vern_code = ?, usertype = ? WHERE id = ?";
        } else if (!isUndefinedStatus && privilege == 4444) {
            updateSql = "UPDATE gw_db_schema.db_user SET enabled = ? WHERE id = ?";
        } else {
            // No changes necessary, skip to list compilation
            return userAccountManagement(adminID);
        }

        try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection()) {
            conn.setAutoCommit(false); // Enable manual transaction block checks
            
            try {
                try (java.sql.PreparedStatement stmtUpdate = conn.prepareStatement(updateSql)) {
                    if (privilege != 4444 && !isUndefinedStatus) {
                        stmtUpdate.setInt(1, privilege);
                        stmtUpdate.setString(2, userType);
                        stmtUpdate.setBoolean(3, accountSt);
                        stmtUpdate.setInt(4, userId);
                    } else if (privilege != 4444 && isUndefinedStatus) {
                        stmtUpdate.setInt(1, privilege);
                        stmtUpdate.setString(2, userType);
                        stmtUpdate.setInt(3, userId);
                    } else {
                        stmtUpdate.setBoolean(1, accountSt);
                        stmtUpdate.setInt(2, userId);
                    }
                    stmtUpdate.executeUpdate();
                }
                conn.commit(); // Push modifications safely
            } catch (SQLException ex) {
                conn.rollback(); // Rollback transaction changes cleanly if validation steps break down
                throw ex;
            }
        }

        // Return a fresh, safely extracted list using our clean reference method above
        return userAccountManagement(adminID);
    }

    /**
     * Retrieves region city metadata records safely from the database repository.
     * Rewritten as an instance method using a try-with-resources structural lifecycle 
     * to safely avoid connection pool exhaustion under simultaneous use.
     */
    public List<File> region_city() {
        List<File> files = new ArrayList<>();
        String quercenter = "SELECT * FROM gw_db_schema.region_city";

        // Connection, Statement, and ResultSet are nested and auto-closed implicitly
        try (java.sql.Connection connectionInstance = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = connectionInstance.prepareStatement(quercenter);
             java.sql.ResultSet rs = statmnt.executeQuery()) {

            while (rs.next()) {
                int na_id = rs.getInt(1);
                String na_name = rs.getString(2);
                Integer jj = 0;

                // Bind attributes to your matching model instance parameters
                File ff = new File(na_id, na_name, jj);
                files.add(ff);
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error processing region city list parameters.");
            e.printStackTrace();
        }
        
        return files;
    }
		
static void readWrit(InputStream ios, BufferedOutputStream bos, long numBytes) throws IOException{
	byte[] buff=new byte[(int) numBytes];
	int val=ios.read(buff);
	if(val!=-1){
		bos.write(buff);
	}	
}
	 public List<File> access_groupingBy_proType() {
	        List<File> list_region = new ArrayList<>();
	        String report_query = "SELECT wfield, COUNT(count_row) FROM ("
	                            + "  SELECT ww.well_index as count_row, wf.wf_name as wfield "
	                            + "  FROM gw_db_schema.water_well as ww "
	                            + "  JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
	                            + "  GROUP BY ww.well_index, wf.wf_name"
	                            + ") pr_c "
	                            + "GROUP BY pr_c.wfield "
	                            + "ORDER BY COUNT(count_row) DESC";

	        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
	             java.sql.PreparedStatement repo_stm = conn.prepareStatement(report_query);
	             java.sql.ResultSet report_rs = repo_stm.executeQuery()) {

	            while (report_rs.next()) {
	                String file_gr = report_rs.getString(1);
	                int count_p = report_rs.getInt(2);
	                float lat_long = 0f;

	                File repo_file = new File(file_gr, lat_long, count_p);
	                list_region.add(repo_file);
	            }
	        } catch (java.sql.SQLException e) {
	            System.err.println("Database error encountered during legend breakdown aggregation processing.");
	            e.printStackTrace();
	        }
	        return list_region;
	    }
}
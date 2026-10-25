package net.AAWSAgDB.fileupload.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.AAWSAgDB.fileupload.model.CoordinateUpdate;
import net.AAWSAgDB.fileupload.model.File;
import net.AAWSAgDB.fileupload.model.UserRegistrationDTO;

@Repository // <-- ADD THIS ANNOTATION HERE
public class UserDao {

    public boolean checkUserExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM gw_db_schema.db_user WHERE username = ?";
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void registerEliteUser(UserRegistrationDTO dto, String encryptedPassword) throws SQLException {
        String insertUserSql = "INSERT INTO gw_db_schema.db_user(fullname, email, phone, username, password, orgname, usertype, vern_code, enabled) "
                             + "VALUES (?, ?, ?, ?, ?, 'AAWSA', ?, 0, 'true')";
        String selectIdSql = "SELECT id FROM gw_db_schema.db_user WHERE username = ?";
        String insertAuthSql = "INSERT INTO gw_db_schema.authorities(user_id, authority, username) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionUtility.dbconnection()) {
            conn.setAutoCommit(false); // Enable transactional safety

            int authorizeUser = 0;

            // 1. Insert User Profile
            try (PreparedStatement prepUser = conn.prepareStatement(insertUserSql)) {
                prepUser.setString(1, dto.getFullName());
                
                String email = dto.getUserEmail();
                if (email == null || email.isEmpty() || email.equalsIgnoreCase("0") || email.equalsIgnoreCase("none")) {
                    prepUser.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    prepUser.setString(2, email);
                }
                
                prepUser.setString(3, dto.getPhone());
                prepUser.setString(4, dto.getUserName());
                prepUser.setString(5, encryptedPassword);
                prepUser.setString(6, dto.getUserType());
                prepUser.executeUpdate();
            }
            

            // 2. Fetch Generation Key ID
            try (PreparedStatement prepId = conn.prepareStatement(selectIdSql)) {
                prepId.setString(1, dto.getUserName());
                try (ResultSet rs = prepId.executeQuery()) {
                    if (rs.next()) {
                        authorizeUser = rs.getInt(1);
                    }
                }
            }

            // 3. Insert Authority Group Mapping
            try (PreparedStatement prepAuth = conn.prepareStatement(insertAuthSql)) {
                String authorization = "Administrator".equalsIgnoreCase(dto.getUserType()) ? "ADMIN" : "USER";
                prepAuth.setInt(1, authorizeUser);
                prepAuth.setString(2, authorization);
                prepAuth.setString(3, dto.getUserName());
                prepAuth.executeUpdate();
            }

            conn.commit(); // Safely save modifications to PostgreSQL
        } catch (SQLException e) {
            throw e; // Triggers parent rollback contexts if necessary
        }
    }
    
    /**
     * Registers a new contractor profile securely in the database.
     * Managed by connection lifecycle resource hooks to prevent pools exhausting.
     */
    public void addContractorProfile(String orgName, String email, String phoneN, String userType, String wellCode) throws Exception {
        String query = "INSERT INTO gw_db_schema.db_user(orgname, email, phone, usertype, wellindex) VALUES (?, ?, ?, ?, ?)";
        
        // FIXED: Using full package path to prevent "undefined for type Connection" error
        try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection();
             java.sql.PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            
            preparedStatement.setString(1, orgName);
            
            // Reusable logic for validating empty or fallback strings
            if (email == null || email.equals("0") || email.equalsIgnoreCase("non") || 
                email.equalsIgnoreCase("None") || email.equalsIgnoreCase("none") || 
                email.trim().isEmpty() || email.equalsIgnoreCase("NA")) {
                preparedStatement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(2, email);    
            }
            
            preparedStatement.setString(3, phoneN);
            preparedStatement.setString(4, userType);
            preparedStatement.setString(5, wellCode);
            
            preparedStatement.executeUpdate();
            System.out.println("Contractor user registered safely: " + orgName);
        }
    }
    //Registration of Consultant
    public void addConsultantProfile(String OrgName,String email,String phoneN,String userType,String wellCode)throws Exception {
        String query = "INSERT INTO gw_db_schema.db_user(orgname,email,phone,usertype,wellindex)VALUES (?,?,?,?,?)";
        try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection();
                java.sql.PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, OrgName);
            if(email.equals("0") || email.equals("non")|| email.equals("None")||email.equals("none")||email.equals("")|| email.equals("NA")) {
            	preparedStatement.setNull(2,java.sql.Types.VARCHAR);
			}else {
				preparedStatement.setString(2, email);	
			}
            preparedStatement.setString(3, phoneN);
            preparedStatement.setString(4, userType);
            preparedStatement.setString(5, wellCode);
            preparedStatement.executeUpdate();
            System.out.println("User registered: " + email);
        }
    }
    /**
     * Executes batch location coordinate modifications safely.
     * Prevents database connection pool leaks and guarantees transactional consistency.
     */
    public void checkLatLongInDB(List<CoordinateUpdate> updates) {
        if (updates == null || updates.isEmpty()) {
            return; // Quick exit if no coordinate updates exist
        }

        String updateSql = "UPDATE gw_db_schema.geo_location SET easting = ?, northing = ? WHERE lat = ? AND longt = ?";
        
        // 1. Establish the connection cleanly using try-with-resources.
        // Explicit package naming bypasses Java's class collision bugs.
        try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection()) {
            
            conn.setAutoCommit(false); // Enable manual transaction controls

            // 2. Wrap the PreparedStatement in another try-with-resources block
            try (java.sql.PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                
                for (CoordinateUpdate item : updates) {
                    updatePstmt.setDouble(1, item.getUpdateEastingValue());
                    updatePstmt.setDouble(2, item.getUpdateNorthingValue());
                    updatePstmt.setDouble(3, item.getLatitude());
                    updatePstmt.setDouble(4, item.getLongitude());
                    updatePstmt.addBatch();
                }

                // Execute the batch processing parameters
                int[] updateCounts = updatePstmt.executeBatch();
                
                // 3. Commit the transaction ONLY if the entire batch completes with zero errors
                conn.commit();
                System.out.println("Successfully executed batch update for " + updateCounts.length + " coordinates.");
                
            } catch (java.sql.SQLException e) {
                // 4. CRUCIAL: Rollback updates safely on execution failures
                System.err.println("Database batch error detected! Rolling back coordinate execution transactions...");
                try {
                    conn.rollback();
                } catch (java.sql.SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
            }
            
        } catch (java.sql.SQLException e) {
            // Catches any connection allocation errors cleanly
            e.printStackTrace();
        }
        // At this closing bracket, the connection is GUARANTEED to close and return directly to the pool automatically.
    }
    
  //check email if exists
  	public boolean contractructorEmailExists(String orgName) throws Exception {
          String queryemailCkeck = "SELECT COUNT(*) FROM gw_db_schema.db_user WHERE SIMILARITY(orgname,'"+orgName+"')>0.75";
          try (Connection conn = ConnectionUtility.dbconnection()) {
        	  PreparedStatement preparedStatement = conn.prepareStatement(queryemailCkeck);
              ResultSet resultSet = preparedStatement.executeQuery();
              if (resultSet.next()) {
                  return resultSet.getInt(1) > 0;
              }
          }
          return false;
      }
    //check Consultant email if exists
    	public boolean consultantEmailExists(String orgName) throws Exception {
            String queryemailCkeck = "SELECT COUNT(*) FROM gw_db_schema.db_user WHERE SIMILARITY(orgname,'"+orgName+"')>0.75";
            try (Connection conn = ConnectionUtility.dbconnection()) {
            	PreparedStatement preparedStatement = conn.prepareStatement(queryemailCkeck);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            return false;
        }
        /**
         * Updates an existing user's password and sets their verification code based on their role.
         * Guarantees pool connection return via try-with-resources.
         */
        public boolean changeUserPassword(int userId, String encryptedPassword) throws SQLException {
            String selectSql = "SELECT usertype FROM gw_db_schema.db_user WHERE id = ?";
            String updateSql = "UPDATE gw_db_schema.db_user SET vern_code = ?, password = ? WHERE id = ?";

            try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection()) {
                String userType = "";
                
                try (java.sql.PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, userId);
                    try (java.sql.ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            userType = rs.getString("usertype");
                        } else {
                            return false; // User not found
                        }
                    }
                }

                int vCode = 0;
                if ("Administrator".equalsIgnoreCase(userType)) {
                    vCode = 110;
                } else if ("Database User".equalsIgnoreCase(userType)) {
                    vCode = 120;
                } else if ("Common User".equalsIgnoreCase(userType)) {
                    vCode = 123;
                }

                try (java.sql.PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, vCode);
                    updateStmt.setString(2, encryptedPassword);
                    updateStmt.setInt(3, userId);
                    updateStmt.executeUpdate();
                    return true;
                }
            }
        }

        /**
         * Verifies profile information match using a parameterized similarity check,
         * then resets the forgotten password securely.
         */
        public boolean resetForgottenPassword(String fullName, String userName, String email, String phone, String encryptedPassword) throws SQLException {
            // FIXED: Fully parameterized to stop severe SQL injection vulnerabilities
            String searchSql = "SELECT id FROM gw_db_schema.db_user WHERE SIMILARITY(fullname, ?) > 0.90 "
                             + "AND SIMILARITY(username, ?) > 0.90 AND SIMILARITY(email, ?) > 0.90 AND phone = ?";
            String updateSql = "UPDATE gw_db_schema.db_user SET password = ? WHERE id = ?";

            try (java.sql.Connection conn = net.AAWSAgDB.fileupload.dao.ConnectionUtility.dbconnection()) {
                int userId = 0;

                try (java.sql.PreparedStatement searchStmt = conn.prepareStatement(searchSql)) {
                    searchStmt.setString(1, fullName);
                    searchStmt.setString(2, userName);
                    searchStmt.setString(3, email);
                    searchStmt.setString(4, phone);

                    try (java.sql.ResultSet rs = searchStmt.executeQuery()) {
                        if (rs.next()) {
                            userId = rs.getInt(1);
                        } else {
                            return false; // Profile parameters did not match criteria
                        }
                    }
                }

                try (java.sql.PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, encryptedPassword);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();
                    return true;
                }
            }
        }
      //Access Center
        public List<File> usertype(){
        	List<File>files=new ArrayList<File>();
        	String pushdata="select usertype from gw_db_schema.db_user group by usertype";
        	try(Connection conn=ConnectionUtility.dbconnection();
        	    Statement stmnt=conn.createStatement();){
        
        try {
        	ResultSet rs=stmnt.executeQuery(pushdata);
            while(rs.next()){
            	//int user_id=rs.getInt(1);
            String c_name=rs.getString(1);	
            File ff=new File(0, c_name,"",0);
            files.add(ff);
            }
        }finally {}
        		
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        	
        	return files;
        }
}
package net.AAWSAgDB.fileupload.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import net.AAWSAgDB.fileupload.ValuePair;
import net.AAWSAgDB.fileupload.model.File;

@Service
public class StoreParameter {


    public List<File> gwDBParam(String AAWSA_Dname) throws SQLException {
        List<File> files = new ArrayList<>();

        String q1 = "SELECT gwdata_id, gwdata_name FROM gw_db_schema.aawsa_gdb WHERE gwdata_name = ?";
        String q2 = "SELECT reg_id, reg_name FROM gw_db_schema.region_city";
        String q3 = "SELECT wf_id, wf_name FROM gw_db_schema.well_field";
        String q4 = "SELECT id, parameterp FROM gw_db_schema.addtionalparm WHERE contentp = 'Well Field'";
        String q5 = "SELECT id, parameterp FROM gw_db_schema.addtionalparm WHERE contentp = 'Well Owner'";
        String q6 = "SELECT id, parameterp FROM gw_db_schema.addtionalparm WHERE contentp = 'Casing Material'";
        String q7 = "SELECT id, parameterp FROM gw_db_schema.addtionalparm WHERE contentp = 'Well Type'";
        String q8 = "SELECT id, parameterp FROM gw_db_schema.addtionalparm WHERE contentp = 'Abandoned Reason'";

        // Unified root connection handles the lifetime of all queries
        try (Connection conn = ConnectionUtility.dbconnection()) {

            // Query 1: AAWSA GDB (Fully Parameterized to prevent SQL Injection)
            try (PreparedStatement stmt = conn.prepareStatement(q1)) {
                stmt.setString(1, AAWSA_Dname);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        files.add(new File("", 0, rs.getInt(1), rs.getString(2), ""));
                    }
                }
            }

            // Query 2: Region City
            try (PreparedStatement stmt = conn.prepareStatement(q2);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new File(rs.getInt("reg_id"), rs.getString("reg_name"), "", 0));
                }
            }

            // Query 3: Well Field
            try (PreparedStatement stmt = conn.prepareStatement(q3);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new File(rs.getString(2), rs.getInt(1)));
                }
            }

            // Query 4: Additional Parm - Well Field
            try (PreparedStatement stmt = conn.prepareStatement(q4);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new File(rs.getInt(1), rs.getString(2), 0));
                }
            }

            // Query 5: Additional Parm - Well Owner
            try (PreparedStatement stmt = conn.prepareStatement(q5);
                 ResultSet rs = stmt.executeQuery()) {
                Object obj = null;
                while (rs.next()) {
                    files.add(new File(rs.getInt(1), rs.getString(2), 0, obj));
                }
            }

            // Query 6: Additional Parm - Casing Material
            try (PreparedStatement stmt = conn.prepareStatement(q6);
                 ResultSet rs = stmt.executeQuery()) {
                Object obj = null;
                while (rs.next()) {
                    files.add(new File(rs.getInt(1), rs.getString(2), obj, 0));
                }
            }

            // Query 7: Additional Parm - Well Type
            try (PreparedStatement stmt = conn.prepareStatement(q7);
                 ResultSet rs = stmt.executeQuery()) {
                Object obj = null;
                while (rs.next()) {
                    files.add(new File(rs.getInt(1), rs.getString(2), obj, 0, 0));
                }
            }

            // Query 8: Additional Parm - Abandoned Reason
            try (PreparedStatement stmt = conn.prepareStatement(q8);
                 ResultSet rs = stmt.executeQuery()) {
                Object obj = null;
                while (rs.next()) {
                    files.add(new File(rs.getInt(1), rs.getString(2), 0, 0, obj));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error populating system structural parameters.");
            e.printStackTrace();
            throw e;
        }
        return files;
    }

    /**
     * Extracts regional class lists securely matching specific region identifier parameters.
     */
    public List<File> region_class(int regionCityId) {
        List<File> files = new ArrayList<>();
        String ptOquery = "SELECT rgC.cl_id, rgC.cl_name FROM gw_db_schema.region_class as rgC "
                        + "JOIN gw_db_schema.region_city as rc ON rc.reg_id = rgC.reg_id "
                        + "WHERE rc.reg_id = ?";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement ptOstatmnt = conn.prepareStatement(ptOquery)) {
            
            ptOstatmnt.setInt(1, regionCityId);
            try (ResultSet ptOrs = ptOstatmnt.executeQuery()) {
                while (ptOrs.next()) {
                    List<?> kk = null;
                    files.add(new File(ptOrs.getString(2), ptOrs.getInt(1), kk));
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return files;
    }

    /**
     * Resolves sub-city zones matching unique layout classification indexes.
     */
    public List<File> subCityZone(int type_id) {
        List<File> files = new ArrayList<>();
        String ptOquery = "SELECT scz.scz_id, scz.sub_city_zone_name FROM gw_db_schema.region_city as rc "
                        + "JOIN gw_db_schema.region_class as rgC ON rc.reg_id = rgC.reg_id "
                        + "JOIN gw_db_schema.sub_city_zone as scz ON scz.cl_id = rgC.cl_id "
                        + "WHERE rgC.cl_id = ?";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement ptOstatmnt = conn.prepareStatement(ptOquery)) {
            
            ptOstatmnt.setInt(1, type_id);
            try (ResultSet ptOrs = ptOstatmnt.executeQuery()) {
                while (ptOrs.next()) {
                    List<?> kk = null;
                    List<?> km = null;
                    files.add(new File(ptOrs.getString(2), ptOrs.getInt(1), kk, km));
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return files;
    }
    /**
     * Retrieves filtered regional city records for header configuration layers.
     * Rewritten as a clean instance method using try-with-resources to prevent pool locks.
     */
    public List<File> headerfile() {
        List<File> files = new ArrayList<>();
        
        // Optimized to only fetch the exact matching dataset required
        String query = "SELECT reg_id, reg_name FROM gw_db_schema.region_city WHERE reg_id != 8";

        try (java.sql.Connection connectionInstance = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = connectionInstance.prepareStatement(query);
             java.sql.ResultSet rs = statmnt.executeQuery()) {

            while (rs.next()) {
                int basin_id = rs.getInt(1);
                String basin_name = rs.getString(2);
                
                File ff = new File(basin_id, basin_name, 0);
                files.add(ff);
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("Database execution failure inside headerfile compilation.");
            e.printStackTrace();
        }
        
        return files;
    }
    /**
     * Fetches administrative database tracking log lines filtered by specific target dates.
     */
    public List<File> accessCRUDOperationLogByDate(String bydate) throws SQLException {
        List<File> file = new ArrayList<>();
        String querybyfol = "SELECT CASE WHEN dl.well_id IS NOT NULL THEN ww.well_index ELSE dl.well_index END AS well_Index, "
                          + "dl.dblogid, du.fullname, dl.actiontook, TO_CHAR(dl.datehapped, 'YYYY-MM-DD HH24:MI'), dl.datasheetype "
                          + "FROM gw_db_schema.database_log as dl "
                          + "LEFT JOIN gw_db_schema.water_well as ww ON dl.well_id = ww.well_id "
                          + "LEFT JOIN gw_db_schema.db_user as du ON dl.user_id = du.id "
                          + "WHERE TO_CHAR(dl.datehapped, 'DD') = ? ORDER BY dl.datehapped DESC";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = conn.prepareStatement(querybyfol)) {
            
            statmnt.setString(1, bydate);
            try (java.sql.ResultSet rs = statmnt.executeQuery()) {
                while (rs.next()) {
                    file.add(new File(rs.getInt(2), rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), 0));
                }
            }
        }
        return file;
    }

    /**
     * Extracts complete application transaction tracking log streams for infrastructure metrics dashboards.
     */
    public List<File> accessCRUDOperationLog() throws SQLException {
        List<File> file = new ArrayList<>();
        String querybyfol = "SELECT CASE WHEN dl.well_id IS NOT NULL THEN ww.well_index ELSE dl.well_index END AS well_Index, "
                          + "dl.dblogid, du.fullname, dl.actiontook, TO_CHAR(dl.datehapped, 'YYYY-MM-DD HH24:MI'), dl.datasheetype "
                          + "FROM gw_db_schema.database_log as dl "
                          + "LEFT JOIN gw_db_schema.water_well as ww ON dl.well_id = ww.well_id "
                          + "LEFT JOIN gw_db_schema.db_user as du ON dl.user_id = du.id "
                          + "ORDER BY dl.datehapped DESC";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = conn.prepareStatement(querybyfol);
             java.sql.ResultSet rs = statmnt.executeQuery()) {

            while (rs.next()) {
                file.add(new File(rs.getInt(2), rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        }
        return file;
    }
}

package net.AAWSAgDB.fileupload.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.AAWSAgDB.fileupload.ValuePair;
import net.AAWSAgDB.fileupload.model.File;

@Repository
public class DataAccessDao {
    /**
     * Queries spatial groundwater well layers matching specific operational and conditioning functional attributes.
     */
    public List<File> accessByWellFunctionStatus(String wellStatus, String fuctionStatus) throws SQLException {
        List<File> file = new ArrayList<>();
        String querybyfol = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name "
                          + "FROM gw_db_schema.geo_location as gl, gw_db_schema.well_field as wf, "
                          + "gw_db_schema.water_well as ww, gw_db_schema.well_characteristics as ch, "
                          + "gw_db_schema.con_discharge_test as cdt, gw_db_schema.constwell_status as cws, "
                          + "gw_db_schema.productive_well as pro, gw_db_schema.productive_well_status as pws "
                          + "WHERE gl.geoloc_id = ww.geoloc_id AND wf.wf_id = ww.wf_id AND ch.well_id = ww.well_id "
                          + "AND cdt.ch_id = ch.ch_id AND cdt.cdt_id = cws.cdt_id AND cws.cws_id = pro.cws_id "
                          + "AND pro.pro_id = pws.pro_id AND cws.data_colln_case = 'Operational' "
                          + "AND SIMILARITY(pws.wellfunstatus, ?) > 0.85 AND SIMILARITY(pws.func_condition, ?) > 0.85";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = conn.prepareStatement(querybyfol)) {
            
            statmnt.setString(1, wellStatus);
            statmnt.setString(2, fuctionStatus);
            
            try (java.sql.ResultSet rs = statmnt.executeQuery()) {
                while (rs.next()) {
                    file.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), ""));
                }
            }
        }
        return file;
    }

    /**
     * Filters geospatial well arrays matching target administrative organizational categorization indexes.
     */
    public List<File> accessByOwnerCategpryname(String ownerCategoryName) throws SQLException {
        List<File> file = new ArrayList<>();
        String querybyfol = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name "
                          + "FROM gw_db_schema.geo_location as gl "
                          + "JOIN gw_db_schema.water_well as ww ON gl.geoloc_id = ww.geoloc_id "
                          + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                          + "WHERE ww.wellownercatgory = ?";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement statmnt = conn.prepareStatement(querybyfol)) {
            
            statmnt.setString(1, ownerCategoryName);
            try (ResultSet rs = statmnt.executeQuery()) {
                while (rs.next()) {
                    file.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", 0));
                }
            }
        }
        return file;
    }
    /**
     * Filters map layers based on customized borehole depth interval classifications.
     */
    public List<File> accessByWellDepthByRange(int depthRange) throws SQLException {
        double rangeScaller = 0;
        boolean isDeepRange = depthRange > 500;

        if (depthRange <= 100) {
            rangeScaller = 0.1;
        } else if (depthRange <= 350) {
            rangeScaller = 101;
        } else if (depthRange <= 500) {
            rangeScaller = 351;
        } else {
            rangeScaller = 1500;
        }

        String sql;
        if (isDeepRange) {
            sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.geo_location as gl "
                + "JOIN gw_db_schema.water_well as ww ON gl.geoloc_id = ww.geoloc_id "
                + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                + "WHERE ? < ww.well_depth AND ww.well_depth < ?";
        } else {
            sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.geo_location as gl "
                + "JOIN gw_db_schema.water_well as ww ON gl.geoloc_id = ww.geoloc_id "
                + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                + "WHERE ? <= ww.well_depth AND ww.well_depth <= ?";
        }

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (isDeepRange) {
                stmt.setDouble(1, depthRange);
                stmt.setDouble(2, rangeScaller);
            } else {
                stmt.setDouble(1, rangeScaller);
                stmt.setDouble(2, depthRange);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", ""));
                }
            }
        }
        return fileList;
    }

    /**
     * Filters dashboard records by active operational abstraction discharge volume scales.
     */
    public List<File> accessByCurrentDischargeRange(double dischargeRange) throws SQLException {
        double rangeScaller = 0;
        if (dischargeRange <= 15) {
            rangeScaller = 1;
        } else if (dischargeRange <= 40) {
            rangeScaller = 15;
        } else if (dischargeRange <= 70) {
            rangeScaller = 40;
        } else {
            rangeScaller = 70;
        }

        String sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                   + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                   + "JOIN gw_db_schema.pumping_status as ps ON ch.ch_id = ps.ch_id "
                   + "WHERE ps.data_colln_case = 'Operational' AND ? < ps.abstraction_rate AND ps.abstraction_rate <= ?";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, dischargeRange);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", "", ""));
                }
            }
        }
        return fileList;
    }

    /**
     * Locates well configurations matching custom static water level ranges.
     */
    public List<File> accessByCurrentSWLRange(double SWLRange) throws SQLException {
        double rangeScaller = 0;
        if (SWLRange <= 0.1) {
            rangeScaller = 0.00001;
        } else if (SWLRange <= 50) {
            rangeScaller = 0.9999;
        } else if (SWLRange <= 100) {
            rangeScaller = 50;
        } else {
            rangeScaller = 100;
        }

        String sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                   + "JOIN gw_db_schema.con_discharge_test as cdt ON ch.ch_id = cdt.ch_id "
                   + "WHERE cdt.data_colln_case = 'Operational' AND ? <= cdt.swl_m AND cdt.swl_m <= ?";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, SWLRange);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", "", "", ""));
                }
            }
        }
        return fileList;
    }
   
    /**
     * Locates spatial vectors matching custom dynamic water level scales.
     */
    public List<File> accessByCurrentDWLRange(double DWLRange) throws SQLException {
        double rangeScaller = 0;
        if (DWLRange <= 50) {
            rangeScaller = 0.0001;
        } else if (DWLRange <= 100) {
            rangeScaller = 50;
        } else if (DWLRange <= 150) {
            rangeScaller = 100;
        } else {
            rangeScaller = 150;
        }

        String sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                   + "JOIN gw_db_schema.con_discharge_test as cdt ON ch.ch_id = cdt.ch_id "
                   + "WHERE cdt.data_colln_case = 'Operational' AND ? < cdt.dwl_m AND cdt.dwl_m <= ?";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, DWLRange);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", "", "", "", ""));
                }
            }
        }
        return fileList;
    }

    /**
     * Filters historical borehole layouts based on transmissivity property intervals.
     */
    public List<File> accessByCurrentTransimissivityRange(double transimissivityRange) throws SQLException {
        double rangeScaller = 0;
        if (transimissivityRange <= 5) {
            rangeScaller = 0.000001;
        } else if (transimissivityRange <= 50) {
            rangeScaller = 5.000001;
        } else if (transimissivityRange <= 500) {
            rangeScaller = 50.00001;
        } else {
            rangeScaller = 500.000001;
        }

        String sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name FROM gw_db_schema.well_field as wf "
        		   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                   + "WHERE ? < ch.transmissivity AND ch.transmissivity <= ?";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, transimissivityRange);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), "", "", "", "", "", "", ""));
                }
            }
        }
        return fileList;
    }

    /**
     * Filters geospatial borehole entities using high-similarity string matching metrics.
     */
    public List<File> accessByWellStatus(String wellStatus) throws SQLException {
        String sql = "SELECT ww.well_id, gl.lat, gl.longt, ww.well_index, wf.wf_name,EXTRACT(YEAR FROM ww.cntn_year) FROM "
        		   + "gw_db_schema.geo_location as gl "
                   + "JOIN gw_db_schema.water_well as ww ON gl.geoloc_id = ww.geoloc_id "
                   + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON ch.well_id = ww.well_id "
                   + "JOIN gw_db_schema.con_discharge_test as cdt ON cdt.ch_id = ch.ch_id "
                   + "JOIN gw_db_schema.constwell_status as cws ON cws.cdt_id = cdt.cdt_id "
                   + "JOIN gw_db_schema.productive_well as pro ON pro.cws_id = cws.cws_id "
                   + "JOIN gw_db_schema.productive_well_status as pws ON pws.pro_id = pro.pro_id "
                   + "WHERE cws.data_colln_case = 'Operational' AND SIMILARITY(pws.wellfunstatus, ?) > 0.85";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, wellStatus);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fileList.add(new File(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5),rs.getString(6),0));
                }
            }
        }
        return fileList;
    }

    /**
     * Resolves metadata credentials for a water well based on its primary key identification parameter.
     */
    public List<File> accessGWODbyWellId(int wellId) throws SQLException {
        String sql = "SELECT ww.well_id, ww.well_index, dbu.orgname, dbu.email, dbu.phone, pro.wateruse_license, pro.pumping_operen_date, dbu.id "
                   + "FROM gw_db_schema.db_user as dbu "
                   + "JOIN gw_db_schema.constwell_status as cws ON cws.enumid = dbu.id "
                   + "JOIN gw_db_schema.productive_well as pro ON pro.cws_id = cws.cws_id "
                   + "JOIN gw_db_schema.con_discharge_test as cdt ON cws.cdt_id = cdt.cdt_id "
                   + "JOIN gw_db_schema.well_characteristics as ch ON cdt.ch_id = ch.ch_id "
                   + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
                   + "WHERE cws.data_colln_case = 'Operational' AND dbu.usertype = 'Enumerator' AND ww.well_id = ?";

        List<File> fileList = new ArrayList<>();
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, wellId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String wellIndex = rs.getString(2);
                    String orgName = rs.getString(3);
                    String orgEmail = rs.getString(4);
                    String orgPhone = rs.getString(5);
                    String license = rs.getString(6);
                    String startDate = rs.getString(7);
                    int userId = rs.getInt(8);

                    String checkEmail = "0".equals(orgEmail) ? "NR" : orgEmail;
                    String checkPhone = "0".equals(orgPhone) ? "NR" : orgPhone;
                    String checkDate = "1970-01-01".equals(startDate) ? "NR" : startDate;

                    fileList.add(new File(id, wellIndex, orgName, checkEmail, checkPhone, license, checkDate, userId));
                }
            }
        }
        return fileList;
    }
    /**
     * Resolves a hyper-detailed well data structure breakdown by primary key index identifier.
     * Wrapped in try-with-resources to guarantee that connections drop back to the pool instantly on exceptions.
     */
    public List<File> selectbyWellId(int wellId) throws SQLException {
    	//System.out.println("Well Id Is ... "+wellId);
    	
        List<File> fileList = new ArrayList<>();
        String baseQuery = "SELECT ww.well_id, ww.wellcode, geo.easting, geo.northing, geo.longt, geo.lat, geo.l_elevation, "
                          + "ww.cntn_year, ww.well_depth, cdt.yield_lps, cdt.swl_m, cdt.dwl_m, ch.specific_capacity, dbu.orgname, "
                          + "dbu.email, dbu.phone, ww.well_index, ww.drilling_license, geo.geoloc_id, dbu.id, ww.wellownercatgory, "
                          + "CI.largecasing, CI.telescopedcasing, CI.casing_mtrl, ww.main_aquifer, ww.well_type,abw.reason,abw.sealed_yn,"
                          + "abw.date_sealed,pts.pump_status, pts.pumpinstalldate, pts.pump_capacity, pts.pumphead, pts.pump_position, "
                          + "pts.abstraction_rate, ps.gen_power, ps.gen_status "
                          + "FROM gw_db_schema.water_well AS ww "
                          + "JOIN gw_db_schema.well_characteristics AS ch ON ch.well_id = ww.well_id "
                          + "JOIN gw_db_schema.con_discharge_test AS cdt ON cdt.ch_id = ch.ch_id "
                          + "JOIN gw_db_schema.constwell_status AS cws ON cws.cdt_id = cdt.cdt_id "
                          + "JOIN gw_db_schema.casing_info AS CI ON CI.well_id = ww.well_id "
                          + "JOIN gw_db_schema.pumping_status AS pts ON pts.ch_id = ch.ch_id "
                          + "JOIN gw_db_schema.geo_location AS geo ON ww.geoloc_id = geo.geoloc_id "
                          + "JOIN gw_db_schema.db_user AS dbu ON ww.owned_by = dbu.id "
                          //Conditional branches (Using LEFT JOIN because they are mutually exclusive)
                          + "LEFT JOIN gw_db_schema.productive_well AS pro ON pro.cws_id = cws.cws_id "
                          + "LEFT JOIN gw_db_schema.power_systemc AS ps ON ps.pro_id = pro.pro_id "
                          + "LEFT JOIN gw_db_schema.abandoned_well AS abw ON abw.cws_id = cws.cws_id "
                          + "WHERE pts.data_colln_case = 'Well Completion Report' "
                          + "AND cdt.data_colln_case = 'Well Completion Report' and ww.well_id = ?";

		try (Connection conn = ConnectionUtility.dbconnection();
		PreparedStatement stmt = conn.prepareStatement(baseQuery)) {
            stmt.setInt(1, wellId);
            
            try (ResultSet rsWCR = stmt.executeQuery()) {
                while (rsWCR.next()) {
                	//System.out.println("Data From DB is "+ rsWCR.getString(17));
                    File ff = new File(
                        rsWCR.getInt(1), rsWCR.getString(2), rsWCR.getDouble(3), rsWCR.getDouble(4),
                        rsWCR.getDouble(5), rsWCR.getDouble(6), rsWCR.getDouble(7), rsWCR.getString(8),
                        rsWCR.getDouble(9), rsWCR.getDouble(10), rsWCR.getDouble(11), rsWCR.getDouble(12),
                        rsWCR.getDouble(13), rsWCR.getString(14), rsWCR.getString(15), rsWCR.getString(16),
                        rsWCR.getString(17), rsWCR.getString(18), rsWCR.getInt(19), rsWCR.getInt(20),
                        rsWCR.getString(21), rsWCR.getDouble(22), rsWCR.getDouble(23), rsWCR.getString(24),
                        rsWCR.getString(25), rsWCR.getString(26), rsWCR.getString(27), rsWCR.getString(28),
                        rsWCR.getString(29),rsWCR.getString(30), rsWCR.getString(31), rsWCR.getDouble(32), 
                        rsWCR.getString(33),rsWCR.getDouble(34), rsWCR.getDouble(35), rsWCR.getDouble(36), rsWCR.getString(37)
                    );
                    fileList.add(ff);
                }
            }
        }
        return fileList;
    }

    /**
     * Resolves historical drilling report datasets based on unique well indicators and user contexts.
     * FIXED: Explicitly parameterized to completely mitigate SQL injection bugs.
     */
    public List<ValuePair> accesswellIndex4DPTHData(int userID, String wellIndex, int userType) throws SQLException {
        List<ValuePair> files = new ArrayList<>();
        boolean hasIndex = wellIndex != null && !wellIndex.trim().isEmpty();
        boolean userTypeIndex = userType ==120;

        String baseQuery = "SELECT ww.well_id, wf.wf_name, rg.reg_name, ww.wellownercatgory, du.orgname, cd.yield_lps, "
                         + "ww.well_depth, EXTRACT(YEAR FROM ww.cntn_year), ww.well_index, ww.wellcode, cws.well_status, cws.data_colln_case "
                         + "FROM gw_db_schema.constwell_status as cws "
                         + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
                         + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
                         + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
                         + "JOIN gw_db_schema.well_field as wf ON ww.wf_id = wf.wf_id "
                         + "JOIN gw_db_schema.local_name as lnm ON ww.local_id = lnm.local_id "
                         + "JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
                         + "JOIN gw_db_schema.sub_city_zone as scz ON wr.scz_id = scz.scz_id "
                         + "JOIN gw_db_schema.region_class as rc ON scz.cl_id = rc.cl_id "
                         + "JOIN gw_db_schema.region_city as rg ON rc.reg_id = rg.reg_id "
                         + "JOIN gw_db_schema.db_user as du ON du.id = ww.owned_by "
                         + "WHERE cws.data_colln_case = 'Well Completion Report'";
        
        String sql = userTypeIndex && hasIndex ? baseQuery+  " AND ww.db_userid = ? AND LOWER(ww.well_index) LIKE LOWER(?) ORDER BY ww.well_id DESC" 
        		              :hasIndex ? baseQuery+ " AND LOWER(ww.well_index) LIKE LOWER(?) ORDER BY ww.well_id DESC"
                              :baseQuery + " ORDER BY ww.well_id DESC";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if(userTypeIndex && hasIndex) {
            	stmt.setInt(1, userID);
            	stmt.setString(2, wellIndex + "%");
            }
            else if (hasIndex) {
                stmt.setString(1, wellIndex + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new ValuePair(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)
                    ));
                }
            }
        }
        return files;
    }

    /**
     * Extracts completion reports associated with the creator profile handle.
     * Refactored to complete your truncated legacy code segment.
     */
    public List<ValuePair> access_modify_delete(int UserID , int UserType) throws SQLException {
        List<ValuePair> files = new ArrayList<>();
        boolean hasIndex = UserType==120;
        StringBuilder queryBuilder = new StringBuilder( "SELECT ww.well_id, wf.wf_name, rg.reg_name, ww.wellownercatgory, du.orgname, cd.yield_lps,"
        		+ "ww.well_depth, EXTRACT(YEAR FROM ww.cntn_year), ww.well_index, ww.wellcode, cws.well_status, cws.data_colln_case "
        		+ "FROM gw_db_schema.constwell_status as cws "
        		+ "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
        		+ "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
        		+ "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
        		+ "JOIN gw_db_schema.well_field as wf ON ww.wf_id = wf.wf_id "
        		+ "JOIN gw_db_schema.local_name as lnm ON ww.local_id = lnm.local_id "
        		+ "JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
        		+ "JOIN gw_db_schema.sub_city_zone as scz ON wr.scz_id = scz.scz_id "
        		+ "JOIN gw_db_schema.region_class as rc ON scz.cl_id = rc.cl_id "
        		+ "JOIN gw_db_schema.region_city as rg ON rc.reg_id = rg.reg_id "
        		+ "JOIN gw_db_schema.db_user as du ON du.id = ww.owned_by "
        		+ "WHERE cws.data_colln_case = 'Well Completion Report' ");
        if (hasIndex) {
            queryBuilder.append("AND ww.db_userid = ?");
        }
        queryBuilder.append(" order by ww.well_id desc");

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            //System.out.println("Accessing data By = "+UserID);
        	if (hasIndex) {
        		stmt.setInt(1, UserID);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new ValuePair(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),""
                    ));   
                }
            }
        }
        //System.out.println("Data from DB is = ");
        return files;
    }
    /**B
     * Unified operational groundwater data lookup service.
     * Consolidates historical duplicate queries into a single parameterized method.
     * 
     * @param userId Filter by numerical user identifier (pass 0 to skip)
     * @param fullName Filter by full string name matches (pass null or empty to skip)
     * @param wellIndex Optional search prefix parameter for interactive filter checks
     */
    public List<ValuePair> accessOperationalGWDataByWellIndex(int userId, String wellIndex,int userType) throws SQLException {
        List<ValuePair> files = new ArrayList<>();
        boolean hasIndex = wellIndex != null && !wellIndex.trim().isEmpty();
        boolean userTypeIndex = userType ==120;

        // 1. Unified Base Query String utilizing ANSI SQL Join compliance
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT ww.well_id, wf.wf_name, rg.reg_name, ww.wellownercatgory, du.orgname, cws.datacondtion, ww.well_depth, "
          + "EXTRACT(YEAR FROM cws.record_date), ww.well_index, ww.wellcode, cws.well_status, cws.data_colln_case "
          + "FROM gw_db_schema.constwell_status as cws "
          + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
          + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
          + "JOIN gw_db_schema.pumping_status as ps ON ps.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.well_field as wf ON ww.wf_id = wf.wf_id "
          + "JOIN gw_db_schema.local_name as lnm ON ww.local_id = lnm.local_id "
          + "JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
          + "JOIN gw_db_schema.sub_city_zone as scz ON wr.scz_id = scz.scz_id "
          + "JOIN gw_db_schema.region_class as rc ON scz.cl_id = rc.cl_id "
          + "JOIN gw_db_schema.region_city as rg ON rc.reg_id = rg.reg_id "
          + "JOIN gw_db_schema.db_user as du ON du.id = ww.owned_by "
          + "WHERE cws.data_colln_case = 'Operational' AND ps.data_colln_case = 'Operational'"
        );
        //System.out.println("Accessing data By = "+userId);
        // 2. Append conditional constraints dynamically and safely
        
        if (userTypeIndex && hasIndex) {
        	queryBuilder.append(" AND LOWER(ww.well_index) LIKE LOWER(?) and ww.db_userid = ?");	
        }
        else if (hasIndex) {
            queryBuilder.append(" AND LOWER(ww.well_index) LIKE LOWER(?)");
        }
        queryBuilder.append(" ORDER BY ww.well_id DESC");

        // 3. Auto-close statements and result streams to fix maximum connection pool errors
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            if(userTypeIndex && hasIndex) {
            	stmt.setString(1, wellIndex + "%");
            	stmt.setInt(2, userId);
            }  
            if (hasIndex) {
                stmt.setString(1, wellIndex + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new ValuePair(
                        rs.getInt(1),       // well_id
                        rs.getString(2),    // wellField
                        rs.getString(3),    // regionName
                        rs.getString(4),    // ownerGroup
                        rs.getString(5),    // OwnerName
                        rs.getString(6),    // datacondition
                        rs.getDouble(7),    // wellDepth
                        rs.getString(8),    // constYear / lastUpdate
                        rs.getString(9),    // DBwellIndex
                        rs.getString(10),   // wellCode
                        rs.getString(11),   // wellStatus
                        rs.getString(12)    // dataCollectCase
                    ));
                    //System.out.println("Data from DB is = "+rs.getString(9));
                }
            }
        }
        return files;
    }
    // Access Operational Groundwater Data
    public List<ValuePair> accessOperationalGWData(int userId,int userTpe) throws SQLException {
        List<ValuePair> files = new ArrayList<>();
        boolean hasType = userTpe == 120;

        // 1. Unified Base Query String utilizing ANSI SQL Join compliance
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT ww.well_id, wf.wf_name, rg.reg_name, ww.wellownercatgory, du.orgname, cws.datacondtion, ww.well_depth, "
          + "EXTRACT(YEAR FROM cws.record_date),ww.well_index,ww.wellcode,cws.well_status,cws.data_colln_case "
          + "FROM gw_db_schema.constwell_status as cws "
          + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
          + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
          + "JOIN gw_db_schema.pumping_status as ps ON ps.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.well_field as wf ON ww.wf_id = wf.wf_id "
          + "JOIN gw_db_schema.local_name as lnm ON ww.local_id = lnm.local_id "
          + "JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
          + "JOIN gw_db_schema.sub_city_zone as scz ON wr.scz_id = scz.scz_id "
          + "JOIN gw_db_schema.region_class as rc ON scz.cl_id = rc.cl_id "
          + "JOIN gw_db_schema.region_city as rg ON rc.reg_id = rg.reg_id "
          + "JOIN gw_db_schema.db_user as du ON du.id = ww.owned_by "
          + "WHERE cws.data_colln_case = 'Operational' AND ps.data_colln_case = 'Operational'"
        );
        //System.out.println("Accessing data By = "+userId);
        // 2. Append conditional constraints dynamically and safely
        if(hasType) {
        	queryBuilder.append(" and ww.db_userid = ?");
        }
        queryBuilder.append(" ORDER BY ww.well_id DESC");

        // 3. Auto-close statements and result streams to fix maximum connection pool errors
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
                
        	if(hasType) {stmt.setInt(1, userId);}

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new ValuePair(
                        rs.getInt(1),       // well_id
                        rs.getString(2),    // wellField
                        rs.getString(3),    // regionName
                        rs.getString(4),    // ownerGroup
                        rs.getString(5),    // OwnerName
                        rs.getString(6),    // datacondition
                        rs.getDouble(7),    // wellDepth
                        rs.getString(8),    // constYear / lastUpdate
                        rs.getString(9),    // DBwellIndex
                        rs.getString(10),   // wellCode
                        rs.getString(11),   // wellStatus
                        rs.getString(12),    // dataCollectCase
                        0
                    ));
                   //System.out.println("Data from DB is = "+rs.getString(12));
                }
            }
        }
        return files;
    }
    
    //Access Well to Overlay on the georeference map
    public List<File> access_well_name() {
        List<File> list_region = new ArrayList<>();
        String report_query = "SELECT ww.well_id, wf.wf_name, gl.longt, gl.lat, gl.longt, gl.lat, ww.well_index, "
                            + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type,sn.sub_city_zone_name "
                            + "FROM gw_db_schema.water_well as ww "
                            + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                            + "JOIN gw_db_schema.local_name as ln ON ln.local_id = ww.local_id "
                            + "JOIN gw_db_schema.woreda as wn ON wn.wr_id = ln.wr_id "
                            + "JOIN gw_db_schema.sub_city_zone as sn ON sn.scz_id = wn.scz_id "
                            + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                            + "WHERE gl.longt IS NOT NULL "
                            + "GROUP BY ww.well_index, ww.well_id, wf.wf_name, gl.longt, gl.lat, "
                            + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type,sn.sub_city_zone_name";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement repo_stm = conn.prepareStatement(report_query);
             java.sql.ResultSet report_rs = repo_stm.executeQuery()) {

            while (report_rs.next()) {
            	File file=new File();    
            	
                int count_file_gr = report_rs.getInt(1);
                String file_gr = report_rs.getString(2);
                double locx = report_rs.getDouble(3);
                double locy = report_rs.getDouble(4);
                double locx2 = report_rs.getDouble(5);
                double locy2 = report_rs.getDouble(6);
                String Procode = report_rs.getString(7);
                String Pyear = report_rs.getString(8);
                String project_type = report_rs.getString(9);
                String szName = report_rs.getString(10);
                file.setWellId(count_file_gr);
                file.setWellField(file_gr);
                file.setWellIndex(Procode);
                file.setPyear(Pyear);
                file.setWellType(project_type);

                File repo_file = new File(count_file_gr, file_gr, locx, locy, locx2, locy2, Procode, Pyear, project_type,szName );
                list_region.add(repo_file);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Database error encountered during well coordinate vector compilation.");
            e.printStackTrace();
        }
        return list_region;
    }
    /**
     * Finds explicit well vectors by search keys to render map layers.
     * Prevents database pool exhaustion via strict try-with-resources wrappers.
     */
    public List<File> access_specificWellName(String wellName) throws SQLException {
        List<File> list_region = new ArrayList<>();
        String report_query = "SELECT ww.well_id, wf.wf_name, gl.longt, gl.lat, gl.longt, gl.lat, ww.well_index, "
                            + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type "
                            + "FROM gw_db_schema.water_well as ww "
                            + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                            + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                            + "WHERE gl.longt IS NOT NULL AND LOWER(ww.well_index) LIKE LOWER(?) "
                            + "GROUP BY ww.well_id, wf.wf_name, gl.longt, gl.lat, ww.well_index, "
                            + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(report_query)) {
            
            stmt.setString(1, wellName + "%");
            try (ResultSet report_rs = stmt.executeQuery()) {
                while (report_rs.next()) {
                    list_region.add(new File(
                        report_rs.getInt(1), report_rs.getString(2), report_rs.getDouble(3), report_rs.getDouble(4),
                        report_rs.getDouble(5), report_rs.getDouble(6), report_rs.getString(7), report_rs.getString(8),
                        report_rs.getString(9), "", ""
                    ));
                }
            }
        }
        return list_region;
    }

    /**
     * Resolves layout overlays grouped by field legends and functional attributes.
     * Rewritten to ensure safe conditional mapping execution without string injection flaws.
     */
    public List<File> access_All_wellName_byLegend(String legeName, String wellFunction) throws SQLException {
        List<File> list_region = new ArrayList<>();
        
        // Safe string assignment comparison eliminates potential NullPointerException crashes
        boolean isFilterUndefined = wellFunction == null || "undefined".equalsIgnoreCase(wellFunction.trim());

        StringBuilder queryBuilder = new StringBuilder(
            "SELECT ww.well_id, wf.wf_name, gl.longt, gl.lat, gl.longt, gl.lat, ww.well_index, "
          + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type "
          + "FROM gw_db_schema.productive_well_status as pws "
          + "JOIN gw_db_schema.productive_well as pw ON pws.pro_id = pw.pro_id "
          + "JOIN gw_db_schema.constwell_status as cws ON pw.cws_id = cws.cws_id "
          + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
          + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
          + "JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
          + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
          + "WHERE gl.longt IS NOT NULL AND wf.wf_name = ?"
        );

        if (!isFilterUndefined) {
            queryBuilder.append(" AND ((pws.wellfunstatus = ? OR pws.func_condition = ?) "
                              + "OR pws.wellfunstatus = ? OR cws.well_status = ? OR pws.func_condition = ?)");
        }

        queryBuilder.append(" GROUP BY ww.well_id, ww.well_index, gl.longt, gl.lat, wf.wf_name, "
                          + "EXTRACT(YEAR FROM ww.cntn_year), ww.well_type");

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            
            stmt.setString(1, legeName);
            if (!isFilterUndefined) {
                stmt.setString(2, wellFunction);
                stmt.setString(3, wellFunction);
                stmt.setString(4, wellFunction);
                stmt.setString(5, wellFunction);
                stmt.setString(6, wellFunction);
            }

            try (ResultSet report_rs = stmt.executeQuery()) {
                while (report_rs.next()) {
                    list_region.add(new File(
                        report_rs.getInt(1), report_rs.getString(2), report_rs.getDouble(3), report_rs.getDouble(4),
                        report_rs.getDouble(5), report_rs.getDouble(6), report_rs.getString(7), report_rs.getString(8),
                        report_rs.getString(9)
                    ));
                }
            }
        }
        return list_region;
    }

    /**
     * Aggregates project frequency metric metrics grouped by specific sub-city zone boundaries.
     */
    public List<File> count_project(String regName) throws SQLException {
        List<File> list_region = new ArrayList<>();
        String report_query = "SELECT subcityZ, COUNT(count_row) FROM ("
                            + "  SELECT ww.well_index as count_row, sz.sub_city_zone_name as subcityZ FROM gw_db_schema.water_well as ww "
                            + "  JOIN gw_db_schema.local_name as lc ON ww.local_id = lc.local_id "
                            + "  JOIN gw_db_schema.woreda as wr ON lc.wr_id = wr.wr_id "
                            + "  JOIN gw_db_schema.sub_city_zone as sz ON wr.scz_id = sz.scz_id "
                            + "  WHERE sz.sub_city_zone_name = ? "
                            + "  GROUP BY sz.sub_city_zone_name, ww.well_index"
                            + ") pr_c GROUP BY pr_c.subcityZ";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(report_query)) {
            
            stmt.setString(1, regName);
            try (ResultSet report_rs = stmt.executeQuery()) {
                while (report_rs.next()) {
                    list_region.add(new File(0, report_rs.getString(1), report_rs.getInt(2), 0));
                }
            }
        }
        return list_region;
    }
    /**
     * Extracts a comprehensive hydrological structure report for a specific borehole ID.
     * Fully protected using try-with-resources to completely prevent connection crashes under load.
     */
    public List<File> access_ReportByWellId(int pro_ID) throws SQLException {
        List<File> list_region = new ArrayList<>();
        if (pro_ID == 0) {
            return list_region;
        }

        String basicChReport_query = "SELECT q1.a, q1.b, q1.c, q1.d, q1.e, q1.f, q1.g, q1.h, q1.i, q1.j, q1.k, q1.l, q1.m, q1.o, q1.n, "
                + "q2.aa, q2.bb, q2.cc, q2.dd, q2.ee, q2.ff, q2.gg, q2.hh, q2.ii, q2.jj, q2.kk, q2.ll, q2.mm, q2.nn, q1.l2 "
                + "FROM ( "
                + "  SELECT ww.well_index as a, gl.longt as b, gl.lat as c, ww.wellownercatgory as d, EXTRACT(YEAR FROM ww.cntn_year) as e, "
                + "  ww.well_depth as f, cd.yield_lps as g, cd.swl_m as h, cd.dwl_m as i, ch.specific_capacity as j, cws.well_status as k, "
                + "  wf.wf_name as l, scz.sub_city_zone_name as l2, ps.pump_position as m, gl.easting as o, gl.northing as n, ww.well_id as comm "
                + "  FROM gw_db_schema.well_field as wf "
                + "  JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                + "  JOIN gw_db_schema.geo_location as gl ON gl.geoloc_id = ww.geoloc_id "
                + "  JOIN gw_db_schema.local_name as lnm ON lnm.local_id = ww.local_id "
                + "  JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
                + "  JOIN gw_db_schema.sub_city_zone as scz ON scz.scz_id = wr.scz_id "
                + "  JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                + "  JOIN gw_db_schema.con_discharge_test as cd ON ch.ch_id = cd.ch_id "
                + "  JOIN gw_db_schema.pumping_status as ps ON ps.ch_id = ch.ch_id "
                + "  JOIN gw_db_schema.constwell_status as cws ON cd.cdt_id = cws.cdt_id "
                + "  WHERE cws.data_colln_case = 'Well Completion Report' AND ps.data_colln_case = 'Well Completion Report' "
                + "  AND cd.data_colln_case = 'Well Completion Report' AND ww.well_id = ?"
                + ") as q1 "
                + "INNER JOIN ( "
                + "  SELECT ps.abstraction_rate as aa, cd.swl_m as bb, cd.dwl_m as cc, pws.wellfunstatus as dd, pws.reason_nonfun as ee, "
                + "  pws.func_condition as ff, pws.reason_inactive as gg, gw.potability_status as hh, gw.non_potable_reason as ii, "
                + "  cws.datacondtion as jj, cws.record_date as kk, prw.pumping_operen_date as ll, ps.pump_position as mm, "
                + "  ps.currenty_designy as nn, ww.well_id as co "
                + "  FROM gw_db_schema.water_well as ww "
                + "  JOIN gw_db_schema.well_characteristics as ch ON ww.well_id = ch.well_id "
                + "  JOIN gw_db_schema.gw_data as gw ON ww.well_id = gw.well_id "
                + "  JOIN gw_db_schema.con_discharge_test as cd ON ch.ch_id = cd.ch_id "
                + "  JOIN gw_db_schema.pumping_status as ps ON ps.ch_id = ch.ch_id "
                + "  JOIN gw_db_schema.constwell_status as cws ON (gw.gw_id = cws.gw_id AND cd.cdt_id = cws.cdt_id) "
                + "  JOIN gw_db_schema.productive_well as prw ON cws.cws_id = prw.cws_id "
                + "  JOIN gw_db_schema.productive_well_status as pws ON prw.pro_id = pws.pro_id "
                + "  WHERE cd.data_colln_case = 'Operational' AND cws.data_colln_case = 'Operational' AND ps.data_colln_case = 'Operational' "
                + "  AND cd.datacondtion IS NOT NULL AND gw.potability_status IS NOT NULL AND ww.well_id = ?"
                + ") as q2 ON q1.comm = q2.co LIMIT 1";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(basicChReport_query)) {
            
            stmt.setInt(1, pro_ID);
            stmt.setInt(2, pro_ID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String well_index = rs.getString(1);
                    double geoLocX = rs.getDouble(2);
                    double geoLocY = rs.getDouble(3);
                    String well_Owner = rs.getString(4);
                    String Cyear = rs.getString(5);
                    double wellDepth = rs.getDouble(6);
                    double wellYield = rs.getDouble(7);
                    double wellSWL = rs.getDouble(8);
                    double wellDWL = rs.getDouble(9);
                    double speCapacity = rs.getDouble(10);
                    String wellStatus = rs.getString(11);
                    String wellFieldOverlay = rs.getString(12);
                    double pumpPosition = rs.getDouble(13);
                    double easting = rs.getDouble(14);
                    double northing = rs.getDouble(15);
                    double abstractionRate = rs.getDouble(16);
                    double wellSWLOpn = rs.getDouble(17);
                    double wellDWLOpn = rs.getDouble(18);

                    // Refactored using an optimized ternary fallback utility string mapping
                    String currentWS = formatFallbackString(rs.getString(19));
                    String reasonNonF = formatFallbackString(rs.getString(20));
                    String functionWellCondi = formatFallbackString(rs.getString(21));
                    String inActiveRe = formatFallbackString(rs.getString(22));
                    String potableS = formatFallbackString(rs.getString(23));
                    String nonPotabableR = formatFallbackString(rs.getString(24));
                    String dataCond = formatFallbackString(rs.getString(25));
                    String recordDate = formatFallbackString(rs.getString(26));
                    String operationStartDate = formatFallbackString(rs.getString(27));
                    
                    double pumpPositionOpn = rs.getDouble(28);
                    String design_current = formatFallbackString(rs.getString(29));
                    String subcityZone = rs.getString(30);

                    File repo_file = new File(well_index, geoLocX, geoLocY, well_Owner, Cyear, wellDepth, wellYield, wellSWL, wellDWL, speCapacity, wellStatus,
                            wellFieldOverlay, pumpPosition, abstractionRate, wellSWLOpn, wellDWLOpn, currentWS, reasonNonF, functionWellCondi, inActiveRe, potableS,
                            nonPotabableR, dataCond, recordDate, operationStartDate, pumpPositionOpn, design_current, subcityZone);
                    list_region.add(repo_file);
                }
            }
        }
        return list_region;
    }

    /**
     * Compiles data collection strategy group distribution totals for dashboard bar charts.
     */
    public List<File> reports_bar() throws SQLException {
        List<File> filetype = new ArrayList<>();
        String report_query = "SELECT cws.data_colln_case, COUNT(cws.data_colln_case) "
                            + "FROM gw_db_schema.constwell_status as cws "
                            + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
                            + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
                            + "JOIN gw_db_schema.gw_data as gw ON cws.gw_id = gw.gw_id "
                            + "JOIN gw_db_schema.water_well as Ww ON (ch.well_id = Ww.well_id AND gw.well_id = Ww.well_id) "
                            + "GROUP BY cws.data_colln_case";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(report_query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                filetype.add(new File(rs.getString(1), rs.getInt(2), 0f));
            }
        }
        return filetype;
    }

    private String formatFallbackString(String input) {
        if (input == null || input.trim().isEmpty() || "0".equals(input) || "0.0".equals(input)) {
            return "_";
        }
        return input;
    }
    /**
     * Natively removes borehole spatial instances transactionally, registers an 
     * audit log tracking record, and returns a refreshed data overview collection.
     * 
     * @param wellID The structural table target database index
     * @param preparedBy The tracking variable signature identifier of the active system user
     * @param dataCase Expected collection criteria filter matching operational or report cases
     */
    public List<File> executeWellDeletionPipeline(String wellID, String preparedBy, String dataCase) throws SQLException {
        List<File> files = new ArrayList<>();
        LocalDateTime timedateNow = LocalDateTime.now(java.time.ZoneId.systemDefault());
        //boolean hasIndex = userType != null && !userType.trim().isEmpty()  && userType=="DD";
        
        int geolocId = 0;
        String wellIndex = "";

        String selectGeoSql = "SELECT geoloc_id, well_index FROM gw_db_schema.water_well WHERE well_id = ?";
        String deleteGeoSql = "DELETE FROM gw_db_schema.geo_location WHERE geoloc_id = ?";
        String insertLogSql = "INSERT INTO gw_db_schema.database_log(user_id, actiontook, datehapped, datasheetype, well_index) VALUES (?, ?, ?, ?, ?)";
        
        // Base Query builder handles conditional metadata extraction dynamically
        StringBuilder selectRefreshSql = new StringBuilder(
            "SELECT ww.well_id, wf.wf_name, rg.reg_name, ww.wellownercatgory, du.orgname, "
          + ("Well Completion Report".equalsIgnoreCase(dataCase) ? "cd.yield_lps" : "cws.datacondtion") + ", "
          + "ww.well_depth, EXTRACT(YEAR FROM " + ("Well Completion Report".equalsIgnoreCase(dataCase) ? "ww.cntn_year" : "cws.record_date") + "), "
          + "ww.well_index, ww.wellcode, cws.well_status, cws.data_colln_case "
          + "FROM gw_db_schema.constwell_status as cws "
          + "JOIN gw_db_schema.con_discharge_test as cd ON cws.cdt_id = cd.cdt_id "
          + "JOIN gw_db_schema.well_characteristics as ch ON cd.ch_id = ch.ch_id "
          + "JOIN gw_db_schema.water_well as ww ON ch.well_id = ww.well_id "
          + "JOIN gw_db_schema.well_field as wf ON ww.wf_id = wf.wf_id "
          + "JOIN gw_db_schema.local_name as lnm ON ww.local_id = lnm.local_id "
          + "JOIN gw_db_schema.woreda as wr ON lnm.wr_id = wr.wr_id "
          + "JOIN gw_db_schema.sub_city_zone as scz ON wr.scz_id = scz.scz_id "
          + "JOIN gw_db_schema.region_class as rc ON scz.cl_id = rc.cl_id "
          + "JOIN gw_db_schema.region_city as rg ON rc.reg_id = rg.reg_id "
          + "JOIN gw_db_schema.db_user as du ON du.id = ww.owned_by "
          + "WHERE du.usertype = 'Well Owner' AND cws.data_colln_case = ?"
        );

        // Safe integer verification prevents character casting anomalies in data streams
        boolean isPreparedByNumeric = preparedBy != null && preparedBy.matches("\\d+");
        if (isPreparedByNumeric) {
            selectRefreshSql.append(" AND ww.db_userid = ?");
        }
        else {
            selectRefreshSql.append(" AND du.fullname = ?");
        }
        selectRefreshSql.append(" ORDER BY ww.well_id DESC");

        // Single secure connection controls cascading states atomically
        try (Connection conn = ConnectionUtility.dbconnection()) {
            conn.setAutoCommit(false); // Enable strict manual transactions
            
            try {
                // 1. Determine associated Geolocation mapping keys safely
                try (PreparedStatement stmtSel = conn.prepareStatement(selectGeoSql)) {
                    stmtSel.setInt(1, Integer.parseInt(wellID));
                    try (ResultSet rs = stmtSel.executeQuery()) {
                        if (rs.next()) {
                            geolocId = rs.getInt(1);
                            wellIndex = rs.getString(2);
                        }
                    }
                }

                if (geolocId > 0) {
                    // 2. Perform Cascading Cascade Removal (Water_well updates depend on geo cascades)
                    try (PreparedStatement stmtDel = conn.prepareStatement(deleteGeoSql)) {
                        stmtDel.setInt(1, geolocId);
                        stmtDel.executeUpdate();
                    }

                    // 3. Register System Administration Transaction Audit Logs
                    try (PreparedStatement stmtLog = conn.prepareStatement(insertLogSql)) {
                        if (isPreparedByNumeric) {
                            stmtLog.setInt(1, Integer.parseInt(preparedBy));
                        } else {
                            stmtLog.setNull(1, java.sql.Types.INTEGER); // Safely passes fallback context if string handles are evaluated
                        }
                        stmtLog.setString(2, "Well Index: " + wellIndex + " is deleted");
                        stmtLog.setObject(3, timedateNow);
                        stmtLog.setString(4, "Well Completion Report".equalsIgnoreCase(dataCase) ? "Entire Well Information" : "Entire well data");
                        stmtLog.setString(5, wellIndex);
                        stmtLog.executeUpdate();
                    }
                }

                conn.commit(); // Push structural deletions safely to production disk instances
                
            } catch (Exception ex) {
                conn.rollback(); // Undo cascade states instantly if an unexpected mapping error surfaces
                throw ex;
            }

            // 4. Extract clean, context-refreshed list representations safely
            try (PreparedStatement stmtRefresh = conn.prepareStatement(selectRefreshSql.toString())) {
                stmtRefresh.setString(1, dataCase);
                if (isPreparedByNumeric) {
                    stmtRefresh.setInt(2, Integer.parseInt(preparedBy));
                } else {
                    stmtRefresh.setString(2, preparedBy);
                }

                try (ResultSet rsRefresh = stmtRefresh.executeQuery()) {
                    while (rsRefresh.next()) {
                        files.add(new File(
                            rsRefresh.getInt(1),       // well_id
                            rsRefresh.getString(2),    // wellField
                            rsRefresh.getString(3),    // regionName
                            rsRefresh.getString(4),    // ownerGroup
                            rsRefresh.getString(5),    // OwnerName
                            rsRefresh.getString(6),    // waterYield / dataCondition string formats
                            rsRefresh.getDouble(7),    // wellDepth
                            rsRefresh.getString(8),    // constYear
                            rsRefresh.getString(9),    // wellIndex
                            rsRefresh.getString(10),   // wellCode
                            rsRefresh.getString(11),   // wellStatus
                            rsRefresh.getString(12)    // dataCollectCase
                        ));
                    }
                }
            }
        }
        return files;
    }
    /**
     * Extracts complete hydro-chemistry metadata breakdown for a specific borehole.
     * Guarded via try-with-resources to cleanly prevent data resource failures.
     */
    public List<File> accessHydroChemistryData(int wellID) throws SQLException {
        List<File> files = new ArrayList<>();
        String sql = "SELECT wqd.well_id, wf.wf_name, wqd.well_index, ww.wellownercatgory, wqd.longt, wqd.lat, wqd.ph, "
                   + "wqd.totalh_ca_mg_caco3, wqd.f_, wqd.na_, wqd.cl_, wqd.ca_, wqd.fe_, wqd.mn_, wqd.mg_, wqd.temprature, "
                   + "wqd.so4_2_, wqd.no3_, wqd.tot_disolved_solid, wqd.elec_condu, wqd.k_, wqd.hco3_, wqd.p_, wqd.water_color, "
                   + "wqd.water_odor, scz.sub_city_zone_name "
                   + "FROM gw_db_schema.operation_wquality_data as wqd "
                   + "JOIN gw_db_schema.water_well as ww ON ww.well_id = wqd.well_id "
                   + "JOIN gw_db_schema.well_field as wf ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.local_name as lnm ON lnm.local_id = ww.local_id "
                   + "JOIN gw_db_schema.woreda as wr ON wr.wr_id = lnm.wr_id "
                   + "JOIN gw_db_schema.sub_city_zone as scz ON scz.scz_id = wr.scz_id "
                   + "WHERE wqd.well_id = ?";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, wellID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(new File(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8),
                        rs.getDouble(9), rs.getDouble(10), rs.getDouble(11), rs.getDouble(12),
                        rs.getDouble(13), rs.getDouble(14), rs.getDouble(15), rs.getDouble(16),
                        rs.getDouble(17), rs.getDouble(18), rs.getDouble(19), rs.getDouble(20),
                        rs.getDouble(21), rs.getDouble(22), rs.getDouble(23), rs.getString(24),
                        rs.getString(25), rs.getString(26)
                    ));
                }
            }
        }
        return files;
    }

    /**
     * Filters coordinates of well vectors matching customized Total Dissolved Solids limits.
     */
    public List<ValuePair> accessBYTDS(double TDS) throws SQLException {
        double rangeScaller = 0;
        if (TDS <= 500) {
            rangeScaller = 0.0001;
        } else if (TDS <= 1000) {
            rangeScaller = 500.0001;
        } else {
            rangeScaller = 1000.0001;
        }

        String sql = "SELECT wqd.well_id, wqd.lat, wqd.longt, wqd.well_index, wf.wf_name "
                   + "FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.operation_wquality_data as wqd ON wqd.well_id = ww.well_id "
                   + "WHERE ? <= wqd.tot_disolved_solid AND wqd.tot_disolved_solid <= ?";

        List<ValuePair> tdsLocation = new ArrayList<>(); 
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, TDS);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tdsLocation.add(new ValuePair(
                        rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5)
                    ));
                }
            }
        }
        return tdsLocation;
    }

    /**
     * Filters spatial well data entries matching targeted Electric Conductivity thresholds.
     */
    public List<ValuePair> accessBYEC(double EC) throws SQLException {
        double rangeScaller = 0;
        if (EC <= 1500) {
            rangeScaller = 0.0001;
        } else if (EC <= 2500) {
            rangeScaller = 1500.0001;
        } else {
            rangeScaller = 2500.0001;
        }

        String sql = "SELECT wqd.well_id, wqd.lat, wqd.longt, wqd.well_index, wf.wf_name "
                   + "FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.operation_wquality_data as wqd ON wqd.well_id = ww.well_id "
                   + "WHERE ? <= wqd.elec_condu AND wqd.elec_condu <= ?";

        List<ValuePair> ecLocation = new ArrayList<>(); 
        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rangeScaller);
            stmt.setDouble(2, EC);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ecLocation.add(new ValuePair(
                        rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getString(4), rs.getString(5), ""
                    ));
                }
            }
        }
        return ecLocation;
    }
    /**
     * Filters geospatial well entities matching selected Fluoride concentration ranges.
     */
    public List<ValuePair> accessBYFlouride(double flouride) throws SQLException {
        double rangeScaller = (flouride <= 1.5) ? 0.000001 : (flouride <= 3) ? 1.500001 : 3.000001;
        
        // Target constructor takes 2 empty trailing strings for Fluoride signature signatures
        return executeWaterQualityRangeQuery("wqd.f_", rangeScaller, flouride, 2);
    }

    /**
     * Filters geospatial well entities matching selected Iron concentration ranges.
     */
    public List<ValuePair> accessBYIron(double iron) throws SQLException {
        double rangeScaller = (iron <= 0.3) ? 0.00001 : (iron <= 5) ? 1.50001 : 3.00001;
        
        // Target constructor takes 3 empty trailing strings for Iron signature signatures
        return executeWaterQualityRangeQuery("wqd.fe_", rangeScaller, iron, 3);
    }

    /**
     * Filters geospatial well entities matching selected Nitrate concentration ranges.
     */
    public List<ValuePair> accessBYNitrate(double nitrate) throws SQLException {
        double rangeScaller = (nitrate <= 45) ? 0.00001 : (nitrate <= 100) ? 45.00001 : 100.00001;
        
        // Target constructor takes 4 empty trailing strings for Nitrate signature signatures
        return executeWaterQualityRangeQuery("wqd.no3_", rangeScaller, nitrate, 4);
    }

    /**
     * Reusable private execution core. 
     * Eliminates duplicate query logic while securing resources via explicit try-with-resources.
     */
    private List<ValuePair> executeWaterQualityRangeQuery(String columnName, double lowIdx, double highIdx, int constructorPaddingCount) throws SQLException {
        List<ValuePair> resultsList = new ArrayList<>();
        
        // Column names cannot be parameterized as ?, so we carefully whitelist input parameters inside a controlled builder
        String sql = "SELECT wqd.well_id, wqd.lat, wqd.longt, wqd.well_index, wf.wf_name "
                   + "FROM gw_db_schema.well_field as wf "
                   + "JOIN gw_db_schema.water_well as ww ON wf.wf_id = ww.wf_id "
                   + "JOIN gw_db_schema.operation_wquality_data as wqd ON wqd.well_id = ww.well_id "
                   + "WHERE ? <= " + columnName + " AND " + columnName + " <= ?";

        try (Connection conn = ConnectionUtility.dbconnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, lowIdx);
            stmt.setDouble(2, highIdx);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int wellId = rs.getInt(1);
                    double lat = rs.getDouble(2);
                    double lon = rs.getDouble(3);
                    String wellIdx = rs.getString(4);
                    String fieldName = rs.getString(5);
                    ValuePair pair;
                    // Maps the precise constructor signatures your front-end scripts expect
                    switch (constructorPaddingCount) {
                        case 2:
                            pair = new ValuePair(wellId, lat, lon, wellIdx, fieldName, "", "");
                            break;
                        case 3:
                            pair = new ValuePair(wellId, lat, lon, wellIdx, fieldName, "", "", "");
                            break;
                        case 4:
                            pair = new ValuePair(wellId, lat, lon, wellIdx, fieldName, "", "", "", "");
                            break;
                        case 5:
                            pair = new ValuePair(wellId, lat, lon, wellIdx, fieldName, "", "", "", "","");
                            break;    
                        default:
                            pair = new ValuePair(wellId, lat, lon, wellIdx, fieldName);
                    }
                    resultsList.add(pair);
                }
            }
        }
        return resultsList;
    }
    /**
     * Filters geospatial well entities matching selected Temperature scales.
     * Fully parameterized via the internal query engine to prevent resource leaks and SQL injections.
     */
    public List<ValuePair> accessBYTemprature(double temprature) throws SQLException {
        double rangeScaller = (temprature <= 37) ? 0.00001 : (temprature <= 50) ? 37.00001 : 50.00001;
        
        // Pass 5 to map the 5 empty string parameters expected by your front-end scripts
        return executeWaterQualityRangeQuery("wqd.temprature", rangeScaller, temprature, 5);
    }
    
    /**
     * Queries the download_requests table to check if a user has access to a specific document.
     * FIXED: Implements Try-With-Resources to completely prevent system connection leaks.
     */
    public int checkRepositoryAccessPermission(int repoId, int userId) {
        int statusResult = 0; // Default: 0 means no record/unrequested
        String query = "SELECT request_status FROM gw_db_schema.download_requests "
                     + "WHERE repo_id = ? AND user_id = ?";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stm = conn.prepareStatement(query)) {
            
            stm.setInt(1, repoId);
            stm.setInt(2, userId);
            
            try (java.sql.ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    statusResult = rs.getInt("request_status"); // Returns 1 (Pending), 2 (Approved), 3 (Declined)
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Database Exception encountered during repository access privilege verification.");
            e.printStackTrace();
        }
        return statusResult;
    }

    /**
     * Inserts a new access request row token token into gw_db_schema.download_requests table.
     * Enforces unique constraint safety guards.
     */
    public boolean submitNewAccessRequest(int repoId, int userId) {
        boolean successFlag = false;
        
        // UPSERT LOGIC: Inserts a new pending row, or resets status to 1 (Pending) if a previous request was declined
        String query = "INSERT INTO gw_db_schema.download_requests (user_id, repo_id, request_status, request_date, action_date) "
                     + "VALUES (?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                     + "ON CONFLICT (user_id, repo_id) "
                     + "DO UPDATE SET request_status = 1, request_date = CURRENT_TIMESTAMP, action_date = CURRENT_TIMESTAMP";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stm = conn.prepareStatement(query)) {
            
            stm.setInt(1, userId);
            stm.setInt(2, repoId);
            
            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                successFlag = true;
                System.out.println("✔ Security Logged: New download privilege request registered for User ID: " + userId + " on Repo ID: " + repoId);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Database Exception: Failed to register access token into download_requests pool schema.");
            e.printStackTrace();
        }
        return successFlag;
    }
    /**
     * Extracts all active pending download requests with unified relational metadata.
     * FIXED: Implements leak-free Try-With-Resources connection tracking.
     */
    public List<ValuePair> accessPendingDownloadRequests() {
        List<ValuePair> requestList = new ArrayList<>();
        
        String sql = "SELECT dr.request_id, u.username, u.orgname, wr.filename, wr.datacategory, dr.request_status "
                   + "FROM gw_db_schema.download_requests as dr "
                   + "JOIN gw_db_schema.db_user as u ON u.id = dr.user_id "
                   + "JOIN gw_db_schema.wellreport as wr ON wr.repo_id = dr.repo_id "
                   + "WHERE dr.request_status = 1 " // 1: Filter Pending requests only
                   + "ORDER BY dr.request_date DESC";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
            	ValuePair valueF= new ValuePair(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), 0);
                
                requestList.add(valueF);
            }
           // System.out.println("✔ Request Matrix Loaded. Total pending items in queue: " + requestList.size());
        } catch (java.sql.SQLException e) {
            System.err.println("Database Exception inside accessPendingDownloadRequests().");
            e.printStackTrace();
        }
        return requestList;
    }

    /**
     * Updates an active request status token row based on operator interaction.
     * Status updates: 2 (Approved), 3 (Declined).
     */
    public boolean updateRequestAuthorizationStatus(int requestId, int targetStatus) {
        boolean successFlag = false;
        String sql = "UPDATE gw_db_schema.download_requests "
                   + "SET request_status = ?, action_date = CURRENT_TIMESTAMP "
                   + "WHERE request_id = ?";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, targetStatus);
            stmt.setInt(2, requestId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                successFlag = true;
                //System.out.println("✔ Request ID: " + requestId + " successfully updated to Status State: " + targetStatus);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Failed to update authorization code row token inside database pool.");
            e.printStackTrace();
        }
        return successFlag;
    }

    /**
     * Extracts physical pumping test binary document streams securely.
     * Enforces explicit resource safety parameters to prevent thread slot leaks.
     */
    public ValuePair downloadDPTHData(int fileID,String DataType) throws SQLException {
        ValuePair file = null;
        String sql = "SELECT filename, filetype, filesize, reportdoc FROM gw_db_schema.wellreport WHERE repo_id = ?";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement statmnt = conn.prepareStatement(sql)) {
            
            statmnt.setInt(1, fileID);
           // statmnt.setString(2, DataType);
            try (java.sql.ResultSet rs = statmnt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString(1);
                    String contentType = rs.getString(2);
                    int size = rs.getInt(3);
                    byte[] data = rs.getBytes(4);
                    file = new ValuePair(data, name, contentType, size);
                }
            }
        }
        return file;
    }

    /**
     * Generates a unique list of unique pumping test reports uploaded across historical records.
     */
    /**
     * Extracts secured binary repositories filtered by category and handles dynamic permission state checks.
     * FIXED: Merged legacy PostgreSQL filename regex parsing and windowing loops with a left join to track user access.
     */
    public List<ValuePair> accessSecuredRepositoriesByCategory(int requestUserID) throws java.sql.SQLException {
        List<ValuePair> files = new ArrayList<>();
        
        // 🫵 MASTER ENTERPRISE SQL COMBINATION: Integrates your legacy windowing & filename filters with the permission join
        String sql = "SELECT sub.repo_id, "
                   + "LEFT(sub.filename, POSITION('.' IN RIGHT(sub.filename, POSITION('/' IN REVERSE(sub.filename)) - 1))), "
                   + "regexp_matches(sub.filename, '\\.(\\w+)$'), sub.recordate, sub.dataCategory, "
                   + "COALESCE(dr.request_status, 0) as active_status "
                   + "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY filename ORDER BY filesize DESC) as rn FROM gw_db_schema.wellreport) AS sub "
                   + "LEFT JOIN gw_db_schema.download_requests as dr ON dr.repo_id = sub.repo_id AND dr.user_id = ? "
                   + "WHERE sub.rn = 1 "
                   + "ORDER BY sub.repo_id ASC";

        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Bind input parameter variables safely to prevent SQL injection leaks
            stmt.setInt(1, requestUserID);
            
            try (java.sql.ResultSet rs3 = stmt.executeQuery()) {
                while (rs3.next()) {
                    int repo_id       = rs3.getInt(1);
                    String fileName   = rs3.getString(2);
                    
                    // Handle array parsing out of PostgreSQL's native regex_matches multi-value array object block
                    java.sql.Array fileTypeArray = rs3.getArray(3);
                    String filetype = "_";
                    if (fileTypeArray != null) {
                        String[] textArray = (String[]) fileTypeArray.getArray();
                        if (textArray != null && textArray.length > 0) {
                            filetype = textArray[0]; // Extracts the clean text extension (e.g., 'xlsx', 'pdf')
                        }
                    }
                    
                    String recordate     = rs3.getString(4);
                    String dataCategory  = rs3.getString(5);
                    
                    // 🫵 OBJECTIVE MET: Pull the live permission token state out of column 6
                    int permissionState  = rs3.getInt(6); 
                    //System.out.println("✔ The permision Id is: " + permissionState);
                    
                    // Instantiates using your constructor parameters sequence rules
                    // ValuePair mapping tracking parameters: repo_id, fileName, filetype, recordate, dataCategory, requestStatusCache
                    files.add(new ValuePair(repo_id, fileName, filetype, recordate, dataCategory, permissionState));
                }
            }
            //System.out.println("✔ Unified Security Repository Hydrated. Dataset elements logged: " + files.size());
        } catch (java.sql.SQLException e) {
            System.err.println("Critical Error: Core mapping pass broke down inside accessSecuredRepositoriesByCategory().");
            e.printStackTrace();
            throw e;
        }
        return files;
    }
    
    /**
     * Compiles unified statistical counting summaries by matching relational constraints across tables.
     * FIXED: Integrated the 6-tier table join sequence and exact column specifications mapping criteria.
     */
    public java.util.Map<String, Integer> compileWellInventoryMetrics() {
        java.util.Map<String, Integer> metricsMap = new java.util.HashMap<>();
        
        // ─── MASTER ENTERPRISE AGGREGATION JOIN STRUCTURE ───
        String countQuery = "SELECT "
            + "COUNT(CASE WHEN LOWER(pws.wellfunstatus) LIKE '%functional%' AND LOWER(pws.wellfunstatus) NOT LIKE '%non%' THEN 1 END) as count_function, "
            + "COUNT(CASE WHEN LOWER(pws.func_condition) LIKE '%active%' AND LOWER(pws.func_condition) NOT LIKE '%inactive%' THEN 1 END) as count_active, "
            + "COUNT(CASE WHEN LOWER(pws.func_condition) LIKE '%inactive%' THEN 1 END) as count_inactive, "
            + "COUNT(CASE WHEN LOWER(pws.wellfunstatus) LIKE '%non functional%' THEN 1 END) as count_abandoned, " // Non Functional maps to Abandoned
            + "COUNT(CASE WHEN LOWER(ww.wellownercatgory) LIKE '%aawsa%' THEN 1 END) as count_aawsa, "
            + "COUNT(CASE WHEN LOWER(ww.wellownercatgory) LIKE '%governmental%' AND LOWER(ww.wellownercatgory) NOT LIKE '%non%' THEN 1 END) as count_government, "
            + "COUNT(CASE WHEN LOWER(ww.wellownercatgory) LIKE '%non%governmental%' THEN 1 END) as count_nongov, "
            + "COUNT(CASE WHEN LOWER(ww.wellownercatgory) LIKE '%private%' THEN 1 END) as count_private, "
            + "COUNT(CASE WHEN ww.well_depth <=100 THEN 1 END) as count_wdepth100, "
            + "COUNT(CASE WHEN ww.well_depth BETWEEN 101 AND 350 THEN 1 END) as count_wdepth350, "
            + "COUNT(CASE WHEN ww.well_depth BETWEEN 351 AND 500 THEN 1 END) as count_wdepth500, "
            + "COUNT(CASE WHEN ww.well_depth >500 THEN 1 END) as count_wdepth501 "
            + "FROM gw_db_schema.water_well as ww "
            + "INNER JOIN gw_db_schema.well_characteristics as wc ON wc.well_id = ww.well_id "
            + "INNER JOIN gw_db_schema.con_discharge_test as cdt ON cdt.ch_id = wc.ch_id "
            + "INNER JOIN gw_db_schema.constwell_status as cws ON cws.cdt_id = cdt.cdt_id "
            + "INNER JOIN gw_db_schema.productive_well as pw ON pw.cws_id = cws.cws_id "
            + "INNER JOIN gw_db_schema.productive_well_status as pws ON pws.pro_id = pw.pro_id where ww.data_colln_case='Well Completion Report'"
            + " and pw.data_colln_case='Well Completion Report'";

        // Try-With-Resources guarantees structural resource cleanup to prevent connection leaks
        try (java.sql.Connection conn = ConnectionUtility.dbconnection();
             java.sql.PreparedStatement stm = conn.prepareStatement(countQuery);
             java.sql.ResultSet rs = stm.executeQuery()) {

            if (rs.next()) {
                metricsMap.put("Function", rs.getInt("count_function"));
                metricsMap.put("Active", rs.getInt("count_active"));
                metricsMap.put("InActive", rs.getInt("count_inactive"));
                metricsMap.put("Abandoned", rs.getInt("count_abandoned"));
                metricsMap.put("AAWSA", rs.getInt("count_aawsa"));
                metricsMap.put("Government", rs.getInt("count_government"));
                metricsMap.put("NonGovernment", rs.getInt("count_nongov"));
                metricsMap.put("Private", rs.getInt("count_private"));
                
                metricsMap.put("B100", rs.getInt("count_wdepth100"));
                metricsMap.put("BWN350", rs.getInt("count_wdepth350"));
                metricsMap.put("BWN500", rs.getInt("count_wdepth500"));
                metricsMap.put("A500", rs.getInt("count_wdepth501"));
            }
            //System.out.println("✔ Relational Metrics Engine Successfully Compiled. Data packages loaded to map dashboard widget panel.");
            
        } catch (java.sql.SQLException e) {
            System.err.println("Critical Error: Core database table aggregation join query pass broke down inside compileWellInventoryMetrics().");
            e.printStackTrace();
        }
        return metricsMap;
    }
}
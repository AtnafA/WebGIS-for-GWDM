package net.AAWSAgDB.fileupload.model;

import java.util.Date;

public class WellDataRecord {
    private String wellIndex;
    private String wellCode;
    private double latitude;
    private double longitude;
    private double yield;
    private double depth;
    private Date constructionDate;
    
    // Getters and Setters for all fields...
    public String getWellIndex() { return wellIndex; }
    public void setWellIndex(String wellIndex) { this.wellIndex = wellIndex; }

    public String getWellCode() { return wellCode; }
    public void setWellCode(String wellCode) { this.wellCode = wellCode; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getYield() { return yield; }
    public void setYield(double yield) { this.yield = yield; }

    public double getDepth() { return depth; }
    public void setDepth(double depth) { this.depth = depth; }

    public Date getConstructionDate() { return constructionDate; }
    public void setConstructionDate(Date constructionDate) { this.constructionDate = constructionDate; }
}

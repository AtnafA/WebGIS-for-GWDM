package net.AAWSAgDB.fileupload.model;

import java.io.InputStream;
import java.sql.Date;

public class SpatialUploadDTO {
    private String orgName;
    private String phone;
    private String userEmail;
    private String wellIndex;
    private String enameEmail;
    private String reportedEmail;
    private String reportedCompany;
    private String reportedPhone;
    private String enameCompany;
    private String enameName;
    private String enamePhone;
    private double elevation;
    private double latitude;
    private double longitude;
    private int wellFieldId;
    private String wellcode;
    private double wellDepth;
    private String mainAcquifer;
    private String wellType;
    private Date const_date;
    private int wellOwnerID;
    private String wellOwnerCata;
    private double specificCapacity;
    private String pumpFilename;
    private String pumpFileType;
    private int pumpfileSize;
    private InputStream pumpdataStream;
    private String wellreportName;
    private String wellReportType;
    private int wellRepoSize;
    private InputStream wellRepoInputStream;
    private int SubCity_Oromia_zone;
    private Date RecorDate;
    private String WellStatus;
    
    public String getWellStatus() {
		return WellStatus;
	}
	public void setWellStatus(String wellStatus) {
		WellStatus = wellStatus;
	}
	public Date getRecorDate() {
		return RecorDate;
	}
	public void setRecorDate(Date recorDate) {
		RecorDate = recorDate;
	}
	public int getS_Oromia_zone() {
		return SubCity_Oromia_zone;
	}
	public void setS_Oromia_zone(int SubCity_Oromia_zone) {
		this.SubCity_Oromia_zone = SubCity_Oromia_zone;
	}
    public int getRepoId() {
		return repoId;
	}
	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}
	public int getEnameId() {
		return enameId;
	}
	public void setEnameId(int enameId) {
		this.enameId = enameId;
	}
    private int repoId;
    private int enameId;

    // ─── GENERATE PUBLIC GETTERS & SETTERS FOR NEW FIELDS ───
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getWellIndex() { return wellIndex; }
    public void setWellIndex(String wellIndex) { this.wellIndex = wellIndex; }

    public String getEnameEmail() { return enameEmail; }
    public void setEnameEmail(String enameEmail) { this.enameEmail = enameEmail; }

    public String getReportedEmail() { return reportedEmail; }
    public void setReportedEmail(String reportedEmail) { this.reportedEmail = reportedEmail; }

    public String getReportedCompany() { return reportedCompany; }
    public void setReportedCompany(String reportedCompany) { this.reportedCompany = reportedCompany; }

    public String getReportedPhone() { return reportedPhone; }
    public void setReportedPhone(String reportedPhone) { this.reportedPhone = reportedPhone; }

    public String getEnameCompany() { return enameCompany; }
    public void setEnameCompany(String enameCompany) { this.enameCompany = enameCompany; }

    public String getEnameName() { return enameName; }
    public void setEnameName(String enameName) { this.enameName = enameName; }

    public String getEnamePhone() { return enamePhone; }
    public void setEnamePhone(String enamePhone) { this.enamePhone = enamePhone; }

    public double getElevation() { return elevation; }
    public void setElevation(double elevation) { this.elevation = elevation; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getWellFieldId() { return wellFieldId; }
    public void setWellFieldId(int wellFieldId) { this.wellFieldId = wellFieldId; }

    public String getWellcode() { return wellcode; }
    public void setWellcode(String wellcode) { this.wellcode = wellcode; }

    public double getWellDepth() { return wellDepth; }
    public void setWellDepth(double wellDepth) { this.wellDepth = wellDepth; }

    public String getMainAcquifer() { return mainAcquifer; }
    public void setMainAcquifer(String mainAcquifer) { this.mainAcquifer = mainAcquifer; }

    public String getWellType() { return wellType; }
    public void setWellType(String wellType) { this.wellType = wellType; }

    public Date getConst_date() { return const_date; }
    public void setConst_date(Date const_date) { this.const_date = const_date; }

    public int getWellOwnerID() { return wellOwnerID; }
    public void setWellOwnerID(int wellOwnerID) { this.wellOwnerID = wellOwnerID; }

    public String getWellOwnerCata() { return wellOwnerCata; }
    public void setWellOwnerCata(String wellOwnerCata) { this.wellOwnerCata = wellOwnerCata; }

    public double getSpecificCapacity() { return specificCapacity; }
    public void setSpecificCapacity(double specificCapacity) { this.specificCapacity = specificCapacity; }

    public String getPumpFilename() { return pumpFilename; }
    public void setPumpFilename(String pumpFilename) { this.pumpFilename = pumpFilename; }

    public String getPumpFileType() { return pumpFileType; }
    public void setPumpFileType(String pumpFileType) { this.pumpFileType = pumpFileType; }

    public int getPumpfileSize() { return pumpfileSize; }
    public void setPumpfileSize(int pumpfileSize) { this.pumpfileSize = pumpfileSize; }

    public InputStream getPumpdataStream() { return pumpdataStream; }
    public void setPumpdataStream(InputStream pumpdataStream) { this.pumpdataStream = pumpdataStream; }

    public String getWellreportName() { return wellreportName; }
    public void setWellreportName(String wellreportName) { this.wellreportName = wellreportName; }

    public String getWellReportType() { return wellReportType; }
    public void setWellReportType(String wellReportType) { this.wellReportType = wellReportType; }

    public int getWellRepoSize() { return wellRepoSize; }
    public void setWellRepoSize(int wellRepoSize) { this.wellRepoSize = wellRepoSize; }

    public InputStream getWellRepoInputStream() { return wellRepoInputStream; }
    public void setWellRepoInputStream(InputStream wellRepoInputStream) { this.wellRepoInputStream = wellRepoInputStream; }

}

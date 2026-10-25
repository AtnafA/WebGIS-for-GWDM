package net.AAWSAgDB.fileupload.model;


public class UserSessionProfile {
    private String fullName;
    private String privilegeCode;
    private String authority;
    private int userId;
    public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPrivilegeCode() { return privilegeCode; }
    public void setPrivilegeCode(String privilegeCode) { this.privilegeCode = privilegeCode; }
    
    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }
    
}

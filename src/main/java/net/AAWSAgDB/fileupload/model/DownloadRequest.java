package net.AAWSAgDB.fileupload.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class DownloadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int requestId;
    private int userId;
    private int repoId;
    private int requestStatus; // 1: Pending, 2: Approved, 3: Declined
    private Timestamp requestDate;
    private Timestamp actionDate;

    // Constructors
    public DownloadRequest() {}

    public DownloadRequest(int userId, int repoId, int requestStatus) {
        this.userId = userId;
        this.repoId = repoId;
        this.requestStatus = requestStatus;
    }

    // Standard Enterprise Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRepoId() { return repoId; }
    public void setRepoId(int repoId) { this.repoId = repoId; }

    public int getRequestStatus() { return requestStatus; }
    public void setRequestStatus(int requestStatus) { this.requestStatus = requestStatus; }

    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }

    public Timestamp getActionDate() { return actionDate; }
    public void setActionDate(Timestamp actionDate) { this.actionDate = actionDate; }
}

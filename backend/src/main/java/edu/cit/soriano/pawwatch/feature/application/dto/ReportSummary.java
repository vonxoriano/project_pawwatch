package edu.cit.soriano.pawwatch.feature.application.dto;

import java.time.LocalDate;

public class ReportSummary {

    private LocalDate startDate;
    private LocalDate endDate;
    private long totalApplications;
    private long approvedCount;
    private long rejectedCount;
    private long pendingCount;

    public ReportSummary() {
    }

    public ReportSummary(LocalDate startDate, LocalDate endDate, long totalApplications,
                          long approvedCount, long rejectedCount, long pendingCount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalApplications = totalApplications;
        this.approvedCount = approvedCount;
        this.rejectedCount = rejectedCount;
        this.pendingCount = pendingCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }
}
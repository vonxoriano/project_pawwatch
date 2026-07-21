import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import useAdminReport from './useAdminReport';

function ReportPanel() {
    const {
        startDate, setStartDate,
        endDate, setEndDate,
        report, error, loading,
        handleGenerateReport, handleResetReport
    } = useAdminReport();

    return (
        <div style={{ width: '100%', maxWidth: '1400px', margin: '0 auto', display: 'flex', flexDirection: 'column' }}>
            <div className="page-header">
                <h2>Adoption Report</h2>
            </div>

            <form className="search-bar" onSubmit={handleGenerateReport} style={{ marginBottom: '20px', flexWrap: 'wrap' }}>
                <DatePicker
                    selected={startDate}
                    onChange={setStartDate}
                    selectsStart
                    startDate={startDate}
                    endDate={endDate}
                    placeholderText="Start date"
                    dateFormat="MMM d, yyyy"
                    className="report-date-input"
                />
                <DatePicker
                    selected={endDate}
                    onChange={setEndDate}
                    selectsEnd
                    startDate={startDate}
                    endDate={endDate}
                    minDate={startDate}
                    placeholderText="End date"
                    dateFormat="MMM d, yyyy"
                    className="report-date-input"
                />
                <button type="submit" className="filter-btn">Generate Report</button>
                <button type="button" className="filter-btn" onClick={handleResetReport}>Reset</button>
            </form>

            {error && <div className="error-message">⚠️ {error}</div>}

            {loading && <p style={{ color: '#999' }}>Generating report...</p>}

            {!loading && report && report.totalApplications === 0 && (
                <div className="empty-state">
                    <div className="empty-icon">📊</div>
                    <p>No applications were submitted within the selected date range.</p>
                </div>
            )}

            {!loading && report && report.totalApplications > 0 && (
                <div className="report-summary-grid">
                    <div className="report-card">
                        <span className="report-card-label">Total Applications</span>
                        <span className="report-card-value">{report.totalApplications}</span>
                    </div>
                    <div className="report-card report-card-approved">
                        <span className="report-card-label">Approved</span>
                        <span className="report-card-value">{report.approvedCount}</span>
                    </div>
                    <div className="report-card report-card-rejected">
                        <span className="report-card-label">Rejected</span>
                        <span className="report-card-value">{report.rejectedCount}</span>
                    </div>
                    <div className="report-card report-card-pending">
                        <span className="report-card-label">Pending</span>
                        <span className="report-card-value">{report.pendingCount}</span>
                    </div>
                </div>
            )}

            {!loading && !report && !error && (
                <div className="empty-state">
                    <div className="empty-icon">📊</div>
                    <p>Select a date range and click "Generate Report" to view adoption activity.</p>
                </div>
            )}
        </div>
    );
}

export default ReportPanel;
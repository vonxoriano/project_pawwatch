import React, { useState } from 'react';
import ApplicationRow from './ApplicationRow';

function ApplicationsTable({ applications, onProcess }) {
    const [remarks, setRemarks] = useState({});
    const [expanded, setExpanded] = useState({});

    const handleRemarkChange = (id, value) => {
        setRemarks(prev => ({ ...prev, [id]: value }));
    };

    const toggleExpanded = (id) => {
        setExpanded(prev => ({ ...prev, [id]: !prev[id] }));
    };

    if (applications.length === 0) {
        return (
            <div className="empty-state">
                <div className="empty-icon">📋</div>
                <p>No applications submitted yet.</p>
            </div>
        );
    }

    return (
        <table className="admin-table">
            <thead>
                <tr>
                    <th>Applicant</th>
                    <th>Animal</th>
                    <th>Species</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Remarks</th>
                    <th>Questionnaire</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {applications.map(app => (
                    <ApplicationRow
                        key={app.applicationId}
                        app={app}
                        remark={remarks[app.applicationId] || ''}
                        onRemarkChange={(value) => handleRemarkChange(app.applicationId, value)}
                        expanded={!!expanded[app.applicationId]}
                        onToggleExpanded={() => toggleExpanded(app.applicationId)}
                        onProcess={onProcess}
                    />
                ))}
            </tbody>
        </table>
    );
}

export default ApplicationsTable;
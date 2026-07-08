import React, { useState } from 'react';

function ApplicationsTable({ applications, onProcess }) {
    const [remarks, setRemarks] = useState({});

    const getStatusClass = (status) => {
        if (status === 'PENDING') return 'status-badge status-pending';
        if (status === 'APPROVED') return 'status-badge status-available';
        return 'status-badge status-adopted';
    };

    const handleRemarkChange = (id, value) => {
        setRemarks(prev => ({ ...prev, [id]: value }));
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
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {applications.map(app => (
                    <tr key={app.applicationId}>
                        <td>{app.user.fullName}</td>
                        <td>{app.animal.name}</td>
                        <td>{app.animal.species}</td>
                        <td>{new Date(app.applicationDate).toLocaleDateString()}</td>
                        <td>
                            <span className={getStatusClass(app.status)}>
                                {app.status}
                            </span>
                        </td>
                        <td>
                            {app.status === 'PENDING' ? (
                                <input
                                    type="text"
                                    placeholder="Add remarks..."
                                    value={remarks[app.applicationId] || ''}
                                    onChange={(e) => handleRemarkChange(app.applicationId, e.target.value)}
                                    style={{
                                        padding: '6px 10px',
                                        border: '1.5px solid #ebebeb',
                                        borderRadius: '8px',
                                        fontSize: '12px',
                                        width: '150px'
                                    }}
                                />
                            ) : (
                                app.remarks || '—'
                            )}
                        </td>
                        <td>
                            {app.status === 'PENDING' && (
                                <div className="table-actions">
                                    <button
                                        className="btn-edit"
                                        onClick={() => onProcess(
                                            app.applicationId,
                                            'APPROVED',
                                            remarks[app.applicationId] || ''
                                        )}>
                                        Approve
                                    </button>
                                    <button
                                        className="btn-delete"
                                        onClick={() => onProcess(
                                            app.applicationId,
                                            'REJECTED',
                                            remarks[app.applicationId] || ''
                                        )}>
                                        Reject
                                    </button>
                                </div>
                            )}
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default ApplicationsTable;
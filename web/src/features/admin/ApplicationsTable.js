import React, { useState } from 'react';

function ApplicationsTable({ applications, onProcess }) {
    const [remarks, setRemarks] = useState({});
    const [expanded, setExpanded] = useState({});

    const getStatusClass = (status) => {
        if (status === 'PENDING') return 'status-badge status-pending';
        if (status === 'APPROVED') return 'status-badge status-available';
        return 'status-badge status-adopted';
    };

    const handleRemarkChange = (id, value) => {
        setRemarks(prev => ({ ...prev, [id]: value }));
    };

    const toggleExpanded = (id) => {
        setExpanded(prev => ({ ...prev, [id]: !prev[id] }));
    };

    const yesNo = (value) => (value === true ? 'Yes' : value === false ? 'No' : '—');

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
                    <React.Fragment key={app.applicationId}>
                        <tr>
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
                                <button
                                    className="btn-secondary"
                                    style={{ padding: '6px 12px', fontSize: '12px' }}
                                    onClick={() => toggleExpanded(app.applicationId)}>
                                    {expanded[app.applicationId] ? 'Hide' : 'View'}
                                </button>
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
                        {expanded[app.applicationId] && (
                            <tr>
                                <td colSpan={8} style={{ background: '#fafafa', padding: '16px' }}>
                                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px 24px', fontSize: '13px' }}>
                                        <div><strong>Housing type:</strong> {app.housingType || '—'}</div>
                                        <div><strong>Landlord permission:</strong> {yesNo(app.hasLandlordPermission)}</div>
                                        <div><strong>Has yard:</strong> {yesNo(app.hasYard)}</div>
                                        <div><strong>Household members:</strong> {app.householdMembers ?? '—'}</div>
                                        <div><strong>Young children at home:</strong> {yesNo(app.hasYoungChildren)}</div>
                                        <div><strong>Has other pets:</strong> {yesNo(app.hasOtherPets)}</div>
                                        <div><strong>Hours away daily:</strong> {app.hoursAwayDaily ?? '—'}</div>
                                        <div><strong>Agrees to return policy:</strong> {yesNo(app.agreesToReturnPolicy)}</div>
                                        <div style={{ gridColumn: '1 / -1' }}><strong>Pet experience:</strong> {app.petExperience || '—'}</div>
                                        <div style={{ gridColumn: '1 / -1' }}><strong>Reason for adopting:</strong> {app.reasonForAdopting || '—'}</div>
                                    </div>
                                </td>
                            </tr>
                        )}
                    </React.Fragment>
                ))}
            </tbody>
        </table>
    );
}

export default ApplicationsTable;
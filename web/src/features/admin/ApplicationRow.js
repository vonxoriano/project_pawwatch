import React from 'react';

const getStatusClass = (status) => {
    if (status === 'PENDING') return 'status-badge status-pending';
    if (status === 'APPROVED') return 'status-badge status-available';
if (status === 'REJECTED') return 'status-badge status-rejected';
return 'status-badge status-adopted';
};

const yesNo = (value) => (value === true ? 'Yes' : value === false ? 'No' : '—');

function ApplicationRow({ app, remark, onRemarkChange, expanded, onToggleExpanded, onProcess }) {
    return (
        <React.Fragment>
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
                            value={remark}
                            onChange={(e) => onRemarkChange(e.target.value)}
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
                        onClick={onToggleExpanded}>
                        {expanded ? 'Hide' : 'View'}
                    </button>
                </td>
                <td>
                    {app.status === 'PENDING' && (
                        <div className="table-actions">
                            <button
                                className="btn-edit"
                                onClick={() => onProcess(app.applicationId, 'APPROVED', remark)}>
                                Approve
                            </button>
                            <button
                                className="btn-delete"
                                onClick={() => onProcess(app.applicationId, 'REJECTED', remark)}>
                                Reject
                            </button>
                        </div>
                    )}
                </td>
            </tr>
            {expanded && (
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
    );
}

export default ApplicationRow;
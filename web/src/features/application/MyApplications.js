import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import applicationService from './applicationService';

function MyApplications() {
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [error, setError] = useState('');
    const [expanded, setExpanded] = useState({});

    useEffect(() => {
        fetchApplications();
    }, []);

    const fetchApplications = async () => {
        try {
            const res = await applicationService.getMyApplications();
            setApplications(res.data);
        } catch (err) {
            setError('Failed to load applications.');
        }
    };

    const handleCancel = async (id) => {
        if (window.confirm('Are you sure you want to cancel this application?')) {
            try {
                await applicationService.cancelApplication(id);
                alert('Application cancelled.');
                fetchApplications();
            } catch (err) {
                alert(err.response?.data?.message || 'Failed to cancel application.');
            }
        }
    };

    const getStatusClass = (status) => {
        if (status === 'PENDING') return 'status-badge status-pending';
        if (status === 'APPROVED') return 'status-badge status-available';
        return 'status-badge status-adopted';
    };

    const toggleExpanded = (id) => {
        setExpanded(prev => ({ ...prev, [id]: !prev[id] }));
    };

    const yesNo = (value) => (value === true ? 'Yes' : value === false ? 'No' : '—');

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <h2>My Applications</h2>
                    <button className="btn-secondary" onClick={() => navigate('/dashboard')}>
                        ← Browse Animals
                    </button>
                </div>

                {error && <div className="error-message">{error}</div>}

                {applications.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-icon">📋</div>
                        <p>You haven't submitted any applications yet.</p>
                    </div>
                ) : (
                    <table className="admin-table">
                        <thead>
                            <tr>
                                <th>Animal</th>
                                <th>Species</th>
                                <th>Date Applied</th>
                                <th>Status</th>
                                <th>Remarks</th>
                                <th>Questionnaire</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {applications.map(app => (
                                <React.Fragment key={app.applicationId}>
                                    <tr>
                                        <td>{app.animal.name}</td>
                                        <td>{app.animal.species}</td>
                                        <td>{new Date(app.applicationDate).toLocaleDateString()}</td>
                                        <td>
                                            <span className={getStatusClass(app.status)}>
                                                {app.status}
                                            </span>
                                        </td>
                                        <td>{app.remarks || '—'}</td>
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
                                                <button
                                                    className="btn-delete"
                                                    onClick={() => handleCancel(app.applicationId)}>
                                                    Cancel
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                    {expanded[app.applicationId] && (
                                        <tr>
                                            <td colSpan={7} style={{ background: '#fafafa', padding: '16px' }}>
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
                )}
            </div>
        </div>
    );
}

export default MyApplications;
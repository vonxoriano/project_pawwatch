import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import applicationService from './applicationService';

function MyApplications() {
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [error, setError] = useState('');

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
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {applications.map(app => (
                                <tr key={app.applicationId}>
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
                                        {app.status === 'PENDING' && (
                                            <button
                                                className="btn-delete"
                                                onClick={() => handleCancel(app.applicationId)}>
                                                Cancel
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default MyApplications;
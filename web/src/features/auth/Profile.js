import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/users';

function Profile() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const [formData, setFormData] = useState({
        fullName: '',
        contactNumber: ''
    });
    const [email, setEmail] = useState('');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const res = await axios.get(`${API_URL}/me`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setFormData({
                    fullName: res.data.fullName,
                    contactNumber: res.data.contactNumber
                });
                setEmail(res.data.email);
            } catch (err) {
                setError('Failed to load profile.');
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, [token]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');
        setSuccess('');
        try {
            const res = await axios.put(`${API_URL}/me`, formData, {
                headers: { Authorization: `Bearer ${token}` }
            });
            localStorage.setItem('fullName', res.data.fullName);
            setSuccess('Profile updated successfully!');
        } catch (err) {
            setError('Failed to update profile.');
        } finally {
            setSaving(false);
        }
    };

    const handleBack = () => {
        if (role === 'ADMIN') navigate('/admin');
        else navigate('/dashboard');
    };

    if (loading) return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <p style={{ color: '#999' }}>Loading...</p>
            </div>
        </div>
    );

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div style={{ maxWidth: '480px', margin: '0 auto' }}>
                    <button className="back-btn" onClick={handleBack}>
                        ← Back
                    </button>
                    <div className="auth-card" style={{ marginTop: '16px' }}>
                        <div className="auth-logo">
                            <div className="logo-icon">👤</div>
                            <h1 style={{ fontSize: '22px' }}>My Profile</h1>
                        </div>
                        <hr className="auth-divider" />

                        {error && <div className="error-message">⚠️ {error}</div>}
                        {success && <div className="success-message">✅ {success}</div>}

                        <div className="form-group">
                            <label>Email Address</label>
                            <input
                                type="email"
                                value={email}
                                disabled
                                style={{ background: '#f5f5f5', color: '#999', cursor: 'not-allowed' }}
                            />
                        </div>

                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Full Name</label>
                                <input
                                    type="text"
                                    name="fullName"
                                    value={formData.fullName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>Contact Number</label>
                                <input
                                    type="text"
                                    name="contactNumber"
                                    value={formData.contactNumber}
                                    onChange={handleChange}
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                className="btn-primary"
                                disabled={saving}>
                                {saving ? 'Saving...' : 'Save Changes'}
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;
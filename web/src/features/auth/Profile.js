import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import FormField from '../../components/FormField';
import ChangePasswordCard from './ChangePasswordCard';
import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/users`;

function Profile() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [contactNumber, setContactNumber] = useState('');
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
                setFullName(res.data.fullName);
                setEmail(res.data.email);
                setContactNumber(res.data.contactNumber);
            } catch (err) {
                setError('Failed to load profile.');
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, [token]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');
        setSuccess('');
        try {
            const res = await axios.put(`${API_URL}/me`,
                { fullName, contactNumber },
                { headers: { Authorization: `Bearer ${token}` } }
            );
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
                <div style={{
                    width: '100%',
                    maxWidth: '640px',
                    margin: '0 auto',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '24px'
                }}>
                    <button
                        className="btn-secondary"
                        style={{ width: 'auto', alignSelf: 'flex-start' }}
                        onClick={handleBack}>
                        Back
                    </button>

                    <div className="auth-card" style={{ maxWidth: '100%' }}>
                        <div className="auth-logo">
                            <div className="logo-icon">👤</div>
                            <h1 style={{ fontSize: '22px' }}>My Profile</h1>
                        </div>
                        <hr className="auth-divider" />

                        {error && <div className="error-message">⚠️ {error}</div>}
                        {success && <div className="success-message">✅ {success}</div>}

                        <FormField
                            label="Email Address 🔒"
                            type="email"
                            value={email}
                            disabled
                        />

                        <form onSubmit={handleSubmit}>
                            <FormField
                                label="Full Name 🔒"
                                value={fullName}
                                disabled
                            />
                            <FormField
                                label="Contact Number"
                                name="contactNumber"
                                value={contactNumber}
                                onChange={(e) => setContactNumber(e.target.value)}
                                required
                            />
                            <button
                                type="submit"
                                className="btn-primary"
                                disabled={saving}>
                                {saving ? 'Saving...' : 'Save Changes'}
                            </button>
                        </form>
                    </div>

                    <ChangePasswordCard token={token} />
                </div>
            </div>
        </div>
    );
}

export default Profile;
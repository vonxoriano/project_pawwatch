import React, { useState } from 'react';
import axios from 'axios';
import FormField from '../../components/FormField';

const API_URL = `${process.env.REACT_APP_API_URL}/api/users`;

function ChangePasswordCard({ token }) {
    const [passwordData, setPasswordData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: ''
    });
    const [passwordError, setPasswordError] = useState('');
    const [passwordSuccess, setPasswordSuccess] = useState('');
    const [changingPassword, setChangingPassword] = useState(false);

    const handlePasswordChange = (e) => {
        setPasswordData({ ...passwordData, [e.target.name]: e.target.value });
    };

    const handlePasswordSubmit = async (e) => {
        e.preventDefault();
        setPasswordError('');
        setPasswordSuccess('');

        if (passwordData.newPassword !== passwordData.confirmNewPassword) {
            setPasswordError('New passwords do not match.');
            return;
        }

        setChangingPassword(true);
        try {
            const res = await axios.put(`${API_URL}/me/password`, passwordData, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setPasswordSuccess(res.data || 'Password changed successfully!');
            setPasswordData({ currentPassword: '', newPassword: '', confirmNewPassword: '' });
        } catch (err) {
            setPasswordError(err.response?.data || 'Failed to change password.');
        } finally {
            setChangingPassword(false);
        }
    };

    return (
        <div className="auth-card" style={{ maxWidth: '100%' }}>
            <div className="auth-logo">
                <div className="logo-icon">🔑</div>
                <h1 style={{ fontSize: '22px' }}>Change Password</h1>
            </div>
            <hr className="auth-divider" />

            {passwordError && <div className="error-message">⚠️ {passwordError}</div>}
            {passwordSuccess && <div className="success-message">✅ {passwordSuccess}</div>}

            <form onSubmit={handlePasswordSubmit}>
                <FormField
                    label="Current Password"
                    type="password"
                    name="currentPassword"
                    value={passwordData.currentPassword}
                    onChange={handlePasswordChange}
                    required
                />
                <FormField
                    label="New Password"
                    type="password"
                    name="newPassword"
                    value={passwordData.newPassword}
                    onChange={handlePasswordChange}
                    required
                />
                <FormField
                    label="Confirm New Password"
                    type="password"
                    name="confirmNewPassword"
                    value={passwordData.confirmNewPassword}
                    onChange={handlePasswordChange}
                    required
                />
                <button
                    type="submit"
                    className="btn-primary"
                    disabled={changingPassword}>
                    {changingPassword ? 'Changing...' : 'Change Password'}
                </button>
            </form>
        </div>
    );
}

export default ChangePasswordCard;
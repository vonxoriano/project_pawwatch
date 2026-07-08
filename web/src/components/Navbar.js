import React from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../features/auth/authService';

function Navbar() {
    const navigate = useNavigate();
    const fullName = localStorage.getItem('fullName');
    const role = localStorage.getItem('role');

    const handleLogout = () => {
        authService.logout();
        navigate('/login');
    };

    return (
        <nav className="dashboard-nav">
            <div className="nav-brand">
                <span>🐾</span>
                <h1>PawWatch</h1>
            </div>
            <div className="nav-right">
                <span className="nav-user">
                    {role === 'ADMIN' ? 'Admin: ' : ''}<strong>{fullName}</strong>
                </span>
                <button onClick={handleLogout}>Log Out</button>
            </div>
        </nav>
    );
}

export default Navbar;
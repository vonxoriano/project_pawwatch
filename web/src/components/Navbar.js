import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../features/auth/authService';
import Modal from './Modal';

function Navbar() {
    const navigate = useNavigate();
   
    const role = localStorage.getItem('role');
    const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false);

    const confirmLogout = () => {
        authService.logout();
        navigate('/login');
    };

    return (
        <nav className="dashboard-nav">
            <div className="nav-brand">
                <span>🐾</span>
                <h1>PawWatch</h1>
            </div>

            <button
                className="nav-hamburger"
                onClick={() => setMenuOpen(!menuOpen)}
                aria-label="Toggle menu">
                {menuOpen ? '✕' : '☰'}
            </button>

            <div className={`nav-right ${menuOpen ? 'open' : ''}`}>
                {role === 'ADOPTER' && (
                    <button className="nav-btn nav-btn-neutral" onClick={() => navigate('/favorites')}>
                        ❤️ Favorites
                    </button>
                )}
                {role === 'ADOPTER' && (
                    <button className="nav-btn nav-btn-neutral" onClick={() => navigate('/my-applications')}>
                        My Applications
                    </button>
                )}
                 <button className="nav-btn nav-btn-neutral" onClick={() => navigate('/profile')}>
                        My Profile
                    </button>
                <button className="nav-btn nav-btn-primary" onClick={() => setShowLogoutConfirm(true)}>
                    Log Out
                </button>
            </div>

            {showLogoutConfirm && (
                <Modal title="Log Out?">
                    <p style={{ fontSize: '14px', color: '#555', marginBottom: '20px' }}>
                        Are you sure you want to log out?
                    </p>
                    <div className="modal-footer">
                        <button className="btn-secondary" onClick={() => setShowLogoutConfirm(false)}>
                            Cancel
                        </button>
                        <button
                            className="btn-primary"
                            style={{ width: 'auto', padding: '10px 24px' }}
                            onClick={confirmLogout}>
                            Log Out
                        </button>
                    </div>
                </Modal>
            )}
        </nav>
    );
}

export default Navbar;
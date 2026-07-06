import React from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

function Dashboard() {
  const navigate = useNavigate();
  const fullName = localStorage.getItem('fullName');
  const role = localStorage.getItem('role');

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <nav className="dashboard-nav">
        <div className="nav-brand">
          <span>🐾</span>
          <h1>PawWatch</h1>
        </div>
        <div className="nav-right">
          <span className="nav-user">Logged in as <strong>{fullName}</strong></span>
          <button onClick={handleLogout}>Log Out</button>
        </div>
      </nav>
      <div className="dashboard-body">
        <div className="dashboard-welcome-card">
          <div className="welcome-icon">🐶</div>
          <h2>Welcome, {fullName}!</h2>
          <p>You're all set. The full PawWatch dashboard is coming soon — animal listings, adoption applications, and more.</p>
          <span className="role-badge">{role}</span>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
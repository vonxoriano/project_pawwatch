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
        <h1>PawWatch</h1>
        <button onClick={handleLogout}>Log Out</button>
      </nav>
      <div className="dashboard-body">
        <h2>Welcome, {fullName}!</h2>
        <p>You are logged in as <strong>{role}</strong>. Dashboard coming soon.</p>
      </div>
    </div>
  );
}

export default Dashboard;
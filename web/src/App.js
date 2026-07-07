import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Register from './pages/Register';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import AdopterDashboard from './pages/AdopterDashboard';
import AnimalDetail from './pages/AnimalDetail';
import MyApplications from './pages/MyApplications';
console.log('MyApplications import:', MyApplications);

const PrivateRoute = ({ children }) => {
    const token = localStorage.getItem('token');
    return token ? children : <Navigate to="/login" />;
};

const RoleRoute = ({ children, role }) => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('role');
    if (!token) return <Navigate to="/login" />;
    if (userRole !== role) return <Navigate to="/login" />;
    return children;
};

function App() {
    console.log('App loaded - routes version 2');
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin" element={
                    <RoleRoute role="ADMIN">
                        <AdminDashboard />
                    </RoleRoute>
                } />
                <Route path="/dashboard" element={
                    <PrivateRoute>
                        <AdopterDashboard />
                    </PrivateRoute>
                } />
                <Route path="/animals/:id" element={
                    <PrivateRoute>
                        <AnimalDetail />
                    </PrivateRoute>
                } />
                <Route path="/my-applications" element={
                    <PrivateRoute>
                        <MyApplications />
                    </PrivateRoute>
                } />
            </Routes>
        </Router>
    );
}

export default App;
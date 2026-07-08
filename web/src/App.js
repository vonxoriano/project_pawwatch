import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Register from './features/auth/Register';
import Login from './features/auth/Login';
import AdminDashboard from './features/admin/AdminDashboard';
import AdopterDashboard from './features/animal/AdopterDashboard';
import AnimalDetail from './features/animal/AnimalDetail';
import MyApplications from './features/application/MyApplications';
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
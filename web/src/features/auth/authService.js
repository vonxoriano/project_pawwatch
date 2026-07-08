import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const register = (fullName, email, contactNumber, password) => {
    return axios.post(`${API_URL}/register`, {
        fullName,
        email,
        contactNumber,
        password
    });
};

const login = (email, password) => {
    return axios.post(`${API_URL}/login`, {
        email,
        password
    });
};

const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('fullName');
};

const authService = { register, login, logout };

export default authService;
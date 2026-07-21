import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/notifications`;

const getToken = () => localStorage.getItem('token');

const headers = () => ({
    Authorization: `Bearer ${getToken()}`
});

const getMyNotifications = () => {
    return axios.get(`${API_URL}/me`, { headers: headers() });
};

const markAsRead = (notificationId) => {
    return axios.patch(`${API_URL}/${notificationId}/read`, {}, { headers: headers() });
};

const markAllAsRead = () => {
    return axios.patch(`${API_URL}/read-all`, {}, { headers: headers() });
};

const notificationService = {
    getMyNotifications,
    markAsRead,
    markAllAsRead
};

export default notificationService;
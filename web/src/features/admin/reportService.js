import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/applications`;

const getToken = () => localStorage.getItem('token');

const headers = () => ({
    Authorization: `Bearer ${getToken()}`
});

const getReport = (startDate, endDate) => {
    return axios.get(`${API_URL}/admin/report`, {
        params: { startDate, endDate },
        headers: headers()
    });
};

const reportService = {
    getReport
};

export default reportService;
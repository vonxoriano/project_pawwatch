import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/applications`;

const getToken = () => localStorage.getItem('token');

const headers = () => ({
    Authorization: `Bearer ${getToken()}`
});

const submitApplication = (applicationData) => {
    return axios.post(`${API_URL}/submit`,
        applicationData,
        { headers: headers() }
    );
};

const getMyApplications = () => {
    return axios.get(`${API_URL}/my`, { headers: headers() });
};

const cancelApplication = (id) => {
    return axios.delete(`${API_URL}/cancel/${id}`, { headers: headers() });
};

const getAllApplications = (status, keyword, dateFrom, dateTo) => {
    const params = {};
    if (status) params.status = status;
    if (keyword) params.keyword = keyword;
    if (dateFrom) params.dateFrom = dateFrom;
    if (dateTo) params.dateTo = dateTo;
    return axios.get(`${API_URL}/admin/all`, { headers: headers(), params });
};

const processApplication = (id, status, remarks) => {
    return axios.patch(`${API_URL}/admin/process/${id}`,
        { status, remarks },
        { headers: headers() }
    );
};

const applicationService = {
    submitApplication,
    getMyApplications,
    cancelApplication,
    getAllApplications,
    processApplication
};

export default applicationService;
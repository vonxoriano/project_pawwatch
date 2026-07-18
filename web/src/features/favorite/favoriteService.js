import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/favorites`;

const getToken = () => localStorage.getItem('token');

const headers = () => ({
    Authorization: `Bearer ${getToken()}`
});

const addFavorite = (animalId) => {
    return axios.post(`${API_URL}/add/${animalId}`, {}, { headers: headers() });
};

const removeFavorite = (animalId) => {
    return axios.delete(`${API_URL}/remove/${animalId}`, { headers: headers() });
};

const getMyFavorites = () => {
    return axios.get(`${API_URL}/my`, { headers: headers() });
};

const checkFavorite = async (animalId) => {
    const res = await axios.get(`${API_URL}/check/${animalId}`, { headers: headers() });
    return res.data;
};

const favoriteService = {
    addFavorite,
    removeFavorite,
    getMyFavorites,
    checkFavorite
};

export default favoriteService;
import axios from 'axios';

const API_URL = `${process.env.REACT_APP_API_URL}/api/animals`;

const getToken = () => localStorage.getItem('token');

const headers = () => ({
    Authorization: `Bearer ${getToken()}`
});

const browseAnimals = (keyword, species, gender, minAge, maxAge) => {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (species) params.species = species;
    if (gender) params.gender = gender;
    if (minAge) params.minAge = minAge;
    if (maxAge) params.maxAge = maxAge;
    return axios.get(`${API_URL}/browse`, { params });
};

const getAnimalById = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

const getAllAnimals = () => {
    return axios.get(`${API_URL}/admin/all`, { headers: headers() });
};

const addAnimal = (data) => {
    return axios.post(`${API_URL}/admin/add`, data, { headers: headers() });
};

const editAnimal = (id, data) => {
    return axios.put(`${API_URL}/admin/edit/${id}`, data, { headers: headers() });
};

const deleteAnimal = (id) => {
    return axios.delete(`${API_URL}/admin/delete/${id}`, { headers: headers() });
};

const animalService = {
    browseAnimals,
    getAnimalById,
    getAllAnimals,
    addAnimal,
    editAnimal,
    deleteAnimal
};

export default animalService;
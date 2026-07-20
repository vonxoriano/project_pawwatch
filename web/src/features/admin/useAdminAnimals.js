import { useState, useEffect } from 'react';
import animalService from '../animal/animalService';

function useAdminAnimals(setError) {
    const [animals, setAnimals] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingAnimal, setEditingAnimal] = useState(null);

    const [animalKeyword, setAnimalKeyword] = useState('');
    const [animalSpecies, setAnimalSpecies] = useState('');
    const [animalGender, setAnimalGender] = useState('');
    const [animalStatus, setAnimalStatus] = useState('');
    const [appliedAnimalFilters, setAppliedAnimalFilters] = useState({
        keyword: '', species: '', gender: '', status: ''
    });

    useEffect(() => {
        fetchAnimals();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const fetchAnimals = async () => {
        try {
            const res = await animalService.getAllAnimals();
            setAnimals(res.data);
        } catch (err) {
            setError('Failed to load animals.');
        }
    };

    const handleAnimalFilter = (e) => {
        e.preventDefault();
        setAppliedAnimalFilters({
            keyword: animalKeyword,
            species: animalSpecies,
            gender: animalGender,
            status: animalStatus
        });
    };

    const handleAnimalReset = () => {
        setAnimalKeyword('');
        setAnimalSpecies('');
        setAnimalGender('');
        setAnimalStatus('');
        setAppliedAnimalFilters({ keyword: '', species: '', gender: '', status: '' });
    };

    const filteredAnimals = animals.filter(animal => {
        const f = appliedAnimalFilters;
        if (f.keyword) {
            const kw = f.keyword.toLowerCase();
            const matches = animal.name.toLowerCase().includes(kw) || animal.breed.toLowerCase().includes(kw);
            if (!matches) return false;
        }
        if (f.species && animal.species !== f.species) return false;
        if (f.gender && animal.gender !== f.gender) return false;
        if (f.status && animal.adoptionStatus !== f.status) return false;
        return true;
    });

    const openAddModal = () => {
        setEditingAnimal(null);
        setShowModal(true);
    };

    const openEditModal = (animal) => {
        setEditingAnimal(animal);
        setShowModal(true);
    };

    const handleSubmit = async (formData) => {
        try {
            if (editingAnimal) {
                await animalService.editAnimal(editingAnimal.animalId, formData);
                alert('Animal updated successfully!');
            } else {
                await animalService.addAnimal(formData);
                alert('Animal added successfully!');
            }
            setShowModal(false);
            fetchAnimals();
        } catch (err) {
            alert('Operation failed. Please try again.');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to remove this animal?')) {
            try {
                await animalService.deleteAnimal(id);
                alert('Animal removed successfully!');
                fetchAnimals();
            } catch (err) {
                const message = err.response?.data || 'Failed to remove animal.';
                alert(message);
            }
        }
    };

    return {
        animals, filteredAnimals, fetchAnimals,
        showModal, setShowModal, editingAnimal,
        openAddModal, openEditModal, handleSubmit, handleDelete,
        animalKeyword, setAnimalKeyword,
        animalSpecies, setAnimalSpecies,
        animalGender, setAnimalGender,
        animalStatus, setAnimalStatus,
        handleAnimalFilter, handleAnimalReset
    };
}

export default useAdminAnimals;
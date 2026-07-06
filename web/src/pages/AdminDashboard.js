import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import AnimalTable from '../components/AnimalTable';
import AnimalModal from '../components/AnimalModal';
import animalService from '../services/animalService';

function AdminDashboard() {
    const [animals, setAnimals] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingAnimal, setEditingAnimal] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchAnimals();
    }, []);

    const fetchAnimals = async () => {
        try {
            const res = await animalService.getAllAnimals();
            setAnimals(res.data);
        } catch (err) {
            setError('Failed to load animals.');
        }
    };

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
                alert('Failed to remove animal.');
            }
        }
    };

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <h2>Animal Listings</h2>
                    <button className="btn-primary"
                        style={{ width: 'auto', padding: '10px 24px' }}
                        onClick={openAddModal}>
                        + Add Animal
                    </button>
                </div>
                {error && <div className="error-message">{error}</div>}
                <AnimalTable
                    animals={animals}
                    onEdit={openEditModal}
                    onDelete={handleDelete}
                />
            </div>
            {showModal && (
                <AnimalModal
                    editingAnimal={editingAnimal}
                    onSubmit={handleSubmit}
                    onClose={() => setShowModal(false)}
                />
            )}
        </div>
    );
}

export default AdminDashboard;
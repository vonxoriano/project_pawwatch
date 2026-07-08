import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import AnimalTable from '../components/AnimalTable';
import AnimalModal from '../components/AnimalModal';
import ApplicationsTable from '../components/ApplicationsTable';
import animalService from '../features/animal/animalService';
import applicationService from '../features/application/applicationService';

function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('animals');
    const [animals, setAnimals] = useState([]);
    const [applications, setApplications] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingAnimal, setEditingAnimal] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchAnimals();
        fetchApplications();
    }, []);

    const fetchAnimals = async () => {
        try {
            const res = await animalService.getAllAnimals();
            setAnimals(res.data);
        } catch (err) {
            setError('Failed to load animals.');
        }
    };

    const fetchApplications = async () => {
        try {
            const res = await applicationService.getAllApplications();
            setApplications(res.data);
        } catch (err) {
            setError('Failed to load applications.');
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

    const handleProcess = async (id, status, remarks) => {
        const action = status === 'APPROVED' ? 'approve' : 'reject';
        if (window.confirm(`Are you sure you want to ${action} this application?`)) {
            try {
                await applicationService.processApplication(id, status, remarks);
                alert(`Application ${status.toLowerCase()} successfully!`);
                fetchApplications();
                fetchAnimals();
            } catch (err) {
                alert('Failed to process application.');
            }
        }
    };

    const tabStyle = (tab) => ({
        padding: '10px 24px',
        border: 'none',
        borderBottom: activeTab === tab ? '3px solid #FF6B2C' : '3px solid transparent',
        background: 'transparent',
        color: activeTab === tab ? '#FF6B2C' : '#888',
        fontWeight: activeTab === tab ? '700' : '500',
        fontSize: '14px',
        cursor: 'pointer',
        transition: 'all 0.2s'
    });

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                {error && <div className="error-message">{error}</div>}

                <div style={{
                    display: 'flex',
                    borderBottom: '1px solid #f0f0f0',
                    marginBottom: '28px'
                }}>
                    <button style={tabStyle('animals')} onClick={() => setActiveTab('animals')}>
                        🐾 Animal Listings
                    </button>
                    <button style={tabStyle('applications')} onClick={() => setActiveTab('applications')}>
                        📋 Adoption Applications
                        {applications.filter(a => a.status === 'PENDING').length > 0 && (
                            <span style={{
                                marginLeft: '8px',
                                background: '#FF6B2C',
                                color: '#fff',
                                borderRadius: '20px',
                                padding: '2px 8px',
                                fontSize: '11px'
                            }}>
                                {applications.filter(a => a.status === 'PENDING').length}
                            </span>
                        )}
                    </button>
                </div>

                {activeTab === 'animals' && (
                    <>
                        <div className="page-header">
                            <h2>Animal Listings</h2>
                            <button className="btn-primary"
                                style={{ width: 'auto', padding: '10px 24px' }}
                                onClick={openAddModal}>
                                + Add Animal
                            </button>
                        </div>
                        <AnimalTable
                            animals={animals}
                            onEdit={openEditModal}
                            onDelete={handleDelete}
                        />
                    </>
                )}

                {activeTab === 'applications' && (
                    <>
                        <div className="page-header">
                            <h2>Adoption Applications</h2>
                        </div>
                        <ApplicationsTable
                            applications={applications}
                            onProcess={handleProcess}
                        />
                    </>
                )}
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
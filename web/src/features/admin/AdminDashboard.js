import React, { useState } from 'react';
import Navbar from '../../components/Navbar';
import FilterBar from '../../components/FilterBar';
import TabButton from '../../components/TabButton';
import AnimalTable from './AnimalTable';
import AnimalModal from './AnimalModal';
import ApplicationsTable from './ApplicationsTable';
import useAdminAnimals from './useAdminAnimals';
import useAdminApplications from './useAdminApplications';
import getAnimalFilterFields from './animalFilterConfig';
import getApplicationFilterFields from './applicationFilterConfig';
import ReportPanel from './ReportPanel';

function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('animals');
    const [error, setError] = useState('');

    const {
        filteredAnimals, fetchAnimals,
        showModal, setShowModal, editingAnimal,
        openAddModal, openEditModal, handleSubmit, handleDelete,
        animalKeyword, setAnimalKeyword,
        animalSpecies,
        animalGender,
        animalStatus,
        handleAnimalFilter, handleAnimalReset,
        handleSpeciesChange, handleGenderChange, handleStatusChange
    } = useAdminAnimals(setError);

    const {
        applications,
        appStatus,
        appKeyword, setAppKeyword,
        appDateFrom, setAppDateFrom,
        appDateTo, setAppDateTo,
        handleAppFilter, handleAppReset, handleProcess,
        handleStatusChange: handleAppStatusChange
    } = useAdminApplications(setError, fetchAnimals);

    const animalFilterFields = getAnimalFilterFields({
        keyword: animalKeyword, setKeyword: setAnimalKeyword,
        species: animalSpecies, onSpeciesChange: handleSpeciesChange,
        gender: animalGender, onGenderChange: handleGenderChange,
        status: animalStatus, onStatusChange: handleStatusChange
    });

    const applicationFilterFields = getApplicationFilterFields({
        keyword: appKeyword, setKeyword: setAppKeyword,
        status: appStatus, onStatusChange: handleAppStatusChange,
        dateFrom: appDateFrom, setDateFrom: setAppDateFrom,
        dateTo: appDateTo, setDateTo: setAppDateTo
    });

    const pendingCount = applications.filter(a => a.status === 'PENDING').length;

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
                    <TabButton active={activeTab === 'animals'} onClick={() => setActiveTab('animals')}>
                        🐾 Animal Listings
                    </TabButton>
                    <TabButton active={activeTab === 'applications'} onClick={() => setActiveTab('applications')}>
                        📋 Adoption Applications
                        {pendingCount > 0 && (
                            <span style={{
                                marginLeft: '8px',
                                background: '#FF6B2C',
                                color: '#fff',
                                borderRadius: '20px',
                                padding: '2px 8px',
                                fontSize: '11px'
                            }}>
                                {pendingCount}
                            </span>
                        )}


                    </TabButton>
                    <TabButton active={activeTab === 'reports'} onClick={() => setActiveTab('reports')}>
                        📊 Reports
                    </TabButton>
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
                        <FilterBar
                            fields={animalFilterFields}
                            onFilter={handleAnimalFilter}
                            onReset={handleAnimalReset}
                            style={{ marginBottom: '20px', flexWrap: 'wrap' }}
                        />
                        <AnimalTable
                            animals={filteredAnimals}
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
                        <FilterBar
                            fields={applicationFilterFields}
                            onFilter={handleAppFilter}
                            onReset={handleAppReset}
                            style={{ marginBottom: '20px', flexWrap: 'wrap' }}
                        />
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
            {activeTab === 'reports' && <ReportPanel />}
        </div>
    );
}

export default AdminDashboard;
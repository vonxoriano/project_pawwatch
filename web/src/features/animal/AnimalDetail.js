import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import animalService from './animalService';
import applicationService from '../application/applicationService';
import ApplicationModal from '../application/ApplicationModal';
function AnimalDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [animal, setAnimal] = useState(null);
    const [error, setError] = useState('');
    const [showApplyModal, setShowApplyModal] = useState(false);
    const role = localStorage.getItem('role');

    useEffect(() => {
        const fetchAnimal = async () => {
            try {
                const res = await animalService.getAnimalById(id);
                setAnimal(res.data);
            } catch (err) {
                setError('Animal not found.');
            }
        };
        fetchAnimal();
    }, [id]);

    const handleSubmitApplication = async (applicationData) => {
        await applicationService.submitApplication(applicationData);
        alert('Application submitted successfully! You can track it in My Applications.');
        navigate('/my-applications');
    };

    if (error) {
        return (
            <div className="dashboard-container">
                <Navbar />
                <div className="dashboard-body">
                    <div className="error-message">{error}</div>
                </div>
            </div>
        );
    }

    if (!animal) {
        return (
            <div className="dashboard-container">
                <Navbar />
                <div className="dashboard-body">
                    <p style={{ color: '#999' }}>Loading...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="detail-container">
                <button className="back-btn" onClick={() => navigate('/dashboard')}>
                    ← Back to Browse
                </button>
                <div className="detail-card">
                    <div className="detail-photo">
                        {animal.photo
                            ? <img src={animal.photo} alt={animal.name} />
                            : (animal.species === 'CAT' ? '🐱' : '🐶')
                        }
                    </div>
                    <div className="detail-body">
                        <h2>{animal.name}</h2>
                        <div className="detail-meta">
                            <span>🐾 {animal.species}</span>
                            <span>🏷️ {animal.breed}</span>
                            <span>🎂 {animal.age} yrs</span>
                            <span>⚥ {animal.gender}</span>
                            <span>❤️ {animal.healthStatus}</span>
                        </div>
                        <p className="detail-description">
                            {animal.description || 'No description available.'}
                        </p>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flexWrap: 'wrap' }}>
                            <span className={`status-badge ${
                                animal.adoptionStatus === 'AVAILABLE' ? 'status-available' :
                                animal.adoptionStatus === 'PENDING' ? 'status-pending' :
                                'status-adopted'
                            }`}>
                                {animal.adoptionStatus}
                            </span>
                            {role === 'ADOPTER' && animal.adoptionStatus === 'AVAILABLE' && (
                                <button
                                    className="btn-primary"
                                    style={{ width: 'auto', padding: '10px 28px' }}
                                    onClick={() => setShowApplyModal(true)}>
                                    🐾 Adopt Me
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>
            {showApplyModal && (
                <ApplicationModal
                    animal={animal}
                    onSubmit={handleSubmitApplication}
                    onClose={() => setShowApplyModal(false)}
                />
            )}
        </div>
    );
}

export default AnimalDetail;
import React from 'react';
import { useNavigate } from 'react-router-dom';

function AnimalCard({ animal }) {
    const navigate = useNavigate();

    return (
        <div className="animal-card" onClick={() => navigate(`/animals/${animal.animalId}`)}>
            <div className="animal-card-photo">
                {animal.photo
                    ? <img src={animal.photo} alt={animal.name} />
                    : (animal.species === 'CAT' ? '🐱' : '🐶')
                }
            </div>
            <div className="animal-card-body">
                <h3>{animal.name}</h3>
                <p>{animal.breed} · {animal.age} yrs · {animal.gender}</p>
                <div className="animal-card-footer">
                    <span className="status-badge status-available">AVAILABLE</span>
                    <span style={{ fontSize: '12px', color: '#FF6B2C', fontWeight: 600 }}>
                        View Profile →
                    </span>
                </div>
            </div>
        </div>
    );
}

export default AnimalCard;
import React from 'react';

function AnimalTable({ animals, onEdit, onDelete }) {

    const getStatusClass = (status) => {
        if (status === 'AVAILABLE') return 'status-badge status-available';
        if (status === 'PENDING') return 'status-badge status-pending';
        return 'status-badge status-adopted';
    };

    if (animals.length === 0) {
        return (
            <div className="empty-state">
                <div className="empty-icon">🐾</div>
                <p>No animals match your search/filter criteria.</p>
            </div>
        );
    }

    return (
        <div className="admin-table-wrapper">
        <table className="admin-table">
            <thead>
                <tr>
                    <th>Photo</th>
                    <th>Name</th>
                    <th>Species</th>
                    <th>Breed</th>
                    <th>Age</th>
                    <th>Gender</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {animals.map(animal => (
                    <tr key={animal.animalId}>
                        <td>
                            <div style={{
                                width: '48px',
                                height: '48px',
                                borderRadius: '10px',
                                overflow: 'hidden',
                                background: '#fff5f0',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                fontSize: '22px'
                            }}>
                                {animal.photo
                                    ? <img
                                        src={animal.photo}
                                        alt={animal.name}
                                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                    />
                                    : (animal.species === 'CAT' ? '🐱' : '🐶')
                                }
                            </div>
                        </td>
                        <td>{animal.name}</td>
                        <td>{animal.species}</td>
                        <td>{animal.breed}</td>
                        <td>{animal.age} yrs</td>
                        <td>{animal.gender}</td>
                        <td>
                            <span className={getStatusClass(animal.adoptionStatus)}>
                                {animal.adoptionStatus}
                            </span>
                        </td>
                        <td>
                            <div className="table-actions">
                                <button className="btn-edit" onClick={() => onEdit(animal)}>Edit</button>
                                <button className="btn-delete" onClick={() => onDelete(animal.animalId)}>Delete</button>
                            </div>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
        </div>
    );
}

export default AnimalTable;
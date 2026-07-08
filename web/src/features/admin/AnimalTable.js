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
                <p>No animals listed yet. Add your first one!</p>
            </div>
        );
    }

    return (
        <table className="admin-table">
            <thead>
                <tr>
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
    );
}

export default AnimalTable;
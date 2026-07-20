import React, { useState, useEffect } from 'react';
import Modal from '../../components/Modal';
import FormField from '../../components/FormField';
import PhotoUploadField from './PhotoUploadField';

function AnimalModal({ editingAnimal, onSubmit, onClose }) {
    const [formData, setFormData] = useState({
        name: '', species: 'CAT', breed: '', age: '',
        gender: 'MALE', description: '', healthStatus: '', photo: ''
    });
    const [uploading, setUploading] = useState(false);

    useEffect(() => {
        if (editingAnimal) {
            setFormData({
                name: editingAnimal.name,
                species: editingAnimal.species,
                breed: editingAnimal.breed,
                age: editingAnimal.age,
                gender: editingAnimal.gender,
                description: editingAnimal.description || '',
                healthStatus: editingAnimal.healthStatus || '',
                photo: editingAnimal.photo || ''
            });
        } else {
            setFormData({
                name: '', species: 'CAT', breed: '', age: '',
                gender: 'MALE', description: '', healthStatus: '', photo: ''
            });
        }
    }, [editingAnimal]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    const selectStyle = {
        width: '100%', padding: '11px 14px',
        border: '1.5px solid #ebebeb', borderRadius: '10px',
        fontSize: '14px', outline: 'none', background: '#fafafa'
    };

    return (
        <Modal title={editingAnimal ? 'Edit Animal' : 'Add New Animal'}>
            <form onSubmit={handleSubmit}>
                <FormField label="Name" name="name" value={formData.name} onChange={handleChange} placeholder="Animal name" required />
                <div className="form-group">
                    <label>Species</label>
                    <select name="species" value={formData.species} onChange={handleChange} style={selectStyle}>
                        <option value="CAT">Cat</option>
                        <option value="DOG">Dog</option>
                    </select>
                </div>
                <FormField label="Breed" name="breed" value={formData.breed} onChange={handleChange} placeholder="Breed" required />
                <FormField label="Age (years)" type="number" name="age" value={formData.age} onChange={handleChange} placeholder="Age" required />
                <div className="form-group">
                    <label>Gender</label>
                    <select name="gender" value={formData.gender} onChange={handleChange} style={selectStyle}>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                </div>
                <FormField label="Health Status" name="healthStatus" value={formData.healthStatus} onChange={handleChange} placeholder="e.g. Vaccinated, Healthy" />
                <FormField label="Description" name="description" value={formData.description} onChange={handleChange} placeholder="Brief description" />
                <PhotoUploadField
                    value={formData.photo}
                    onUploadComplete={(url) => setFormData((prev) => ({ ...prev, photo: url }))}
                    onUploadingChange={setUploading}
                />
                <div className="modal-footer">
                    <button type="button" className="btn-secondary" onClick={onClose}>Cancel</button>
                    <button
                        type="submit"
                        className="btn-primary"
                        style={{ width: 'auto', padding: '10px 24px' }}
                        disabled={uploading}
                    >
                        {editingAnimal ? 'Save Changes' : 'Add Animal'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}

export default AnimalModal;
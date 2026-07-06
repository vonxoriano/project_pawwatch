import React, { useState, useEffect } from 'react';
import { supabase } from '../services/supabaseClient';

function AnimalModal({ editingAnimal, onSubmit, onClose }) {
    const [formData, setFormData] = useState({
        name: '', species: 'CAT', breed: '', age: '',
        gender: 'MALE', description: '', healthStatus: '', photo: ''
    });
    const [uploading, setUploading] = useState(false);
    const [previewUrl, setPreviewUrl] = useState('');

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
            setPreviewUrl(editingAnimal.photo || '');
        } else {
            setFormData({
                name: '', species: 'CAT', breed: '', age: '',
                gender: 'MALE', description: '', healthStatus: '', photo: ''
            });
            setPreviewUrl('');
        }
    }, [editingAnimal]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);
        try {
            const fileExt = file.name.split('.').pop();
            const fileName = `${Date.now()}.${fileExt}`;

            const { error: uploadError } = await supabase.storage
                .from('animal-photos')
                .upload(fileName, file);

            if (uploadError) throw uploadError;

            const { data } = supabase.storage
                .from('animal-photos')
                .getPublicUrl(fileName);

            setFormData((prev) => ({ ...prev, photo: data.publicUrl }));
            setPreviewUrl(data.publicUrl);
        } catch (err) {
            alert('Photo upload failed: ' + err.message);
        } finally {
            setUploading(false);
        }
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
        <div className="modal-overlay">
            <div className="modal">
                <h3>{editingAnimal ? 'Edit Animal' : 'Add New Animal'}</h3>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Name</label>
                        <input name="name" value={formData.name} onChange={handleChange} placeholder="Animal name" required />
                    </div>
                    <div className="form-group">
                        <label>Species</label>
                        <select name="species" value={formData.species} onChange={handleChange} style={selectStyle}>
                            <option value="CAT">Cat</option>
                            <option value="DOG">Dog</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Breed</label>
                        <input name="breed" value={formData.breed} onChange={handleChange} placeholder="Breed" required />
                    </div>
                    <div className="form-group">
                        <label>Age (years)</label>
                        <input name="age" type="number" value={formData.age} onChange={handleChange} placeholder="Age" required />
                    </div>
                    <div className="form-group">
                        <label>Gender</label>
                        <select name="gender" value={formData.gender} onChange={handleChange} style={selectStyle}>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label>Health Status</label>
                        <input name="healthStatus" value={formData.healthStatus} onChange={handleChange} placeholder="e.g. Vaccinated, Healthy" />
                    </div>
                    <div className="form-group">
                        <label>Description</label>
                        <input name="description" value={formData.description} onChange={handleChange} placeholder="Brief description" />
                    </div>
                    <div className="form-group">
                        <label>Photo</label>
                        <input type="file" accept="image/*" onChange={handleFileChange} />
                        {uploading && <p style={{ fontSize: '13px', color: '#888' }}>Uploading...</p>}
                        {previewUrl && !uploading && (
                            <img
                                src={previewUrl}
                                alt="Preview"
                                style={{ width: '100px', height: '100px', objectFit: 'cover', borderRadius: '8px', marginTop: '8px' }}
                            />
                        )}
                    </div>
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
            </div>
        </div>
    );
}

export default AnimalModal;
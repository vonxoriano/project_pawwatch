import React, { useState } from 'react';

function ApplicationModal({ animal, onSubmit, onClose }) {
    const [formData, setFormData] = useState({
        housingType: 'OWN',
        hasLandlordPermission: '',
        hasYard: '',
        householdMembers: '',
        hasYoungChildren: '',
        hasOtherPets: '',
        petExperience: '',
        hoursAwayDaily: '',
        reasonForAdopting: '',
        agreesToReturnPolicy: false,
        remarks: ''
    });
    const [error, setError] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const validate = () => {
        if (!formData.housingType) return 'Please select your housing type.';
        if (formData.housingType === 'RENT' && formData.hasLandlordPermission === '') {
            return 'Please indicate if you have landlord permission.';
        }
        if (formData.hasYard === '') return 'Please indicate if you have a yard.';
        if (!formData.householdMembers) return 'Please enter number of household members.';
        if (formData.hasYoungChildren === '') return 'Please indicate if you have young children.';
        if (formData.hasOtherPets === '') return 'Please indicate if you have other pets.';
        if (!formData.petExperience.trim()) return 'Please describe your pet experience.';
        if (!formData.hoursAwayDaily && formData.hoursAwayDaily !== 0) return 'Please enter hours away daily.';
        if (!formData.reasonForAdopting.trim()) return 'Please provide your reason for adopting.';
        if (!formData.agreesToReturnPolicy) return 'You must agree to the return policy to proceed.';
        return '';
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        const validationError = validate();
        if (validationError) {
            setError(validationError);
            return;
        }

        setSubmitting(true);
        try {
            await onSubmit({
                animalId: animal.animalId,
                housingType: formData.housingType,
                hasLandlordPermission: formData.housingType === 'RENT' ? formData.hasLandlordPermission === 'true' : null,
                hasYard: formData.hasYard === 'true',
                householdMembers: parseInt(formData.householdMembers, 10),
                hasYoungChildren: formData.hasYoungChildren === 'true',
                hasOtherPets: formData.hasOtherPets === 'true',
                petExperience: formData.petExperience,
                hoursAwayDaily: parseInt(formData.hoursAwayDaily, 10),
                reasonForAdopting: formData.reasonForAdopting,
                agreesToReturnPolicy: formData.agreesToReturnPolicy,
                remarks: formData.remarks
            });
        } catch (err) {
            setError(err.response?.data || 'Failed to submit application.');
        } finally {
            setSubmitting(false);
        }
    };

    const selectStyle = {
        width: '100%', padding: '11px 14px',
        border: '1.5px solid #ebebeb', borderRadius: '10px',
        fontSize: '14px', outline: 'none', background: '#fafafa'
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h3>Adoption Application — {animal.name}</h3>
                <p style={{ fontSize: '13px', color: '#888', marginTop: '-8px', marginBottom: '16px' }}>
                    Please answer honestly so our shelter staff can find the best match for {animal.name}.
                </p>

                {error && <div className="error-message">⚠️ {error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Do you own or rent your home?</label>
                        <select name="housingType" value={formData.housingType} onChange={handleChange} style={selectStyle}>
                            <option value="OWN">Own</option>
                            <option value="RENT">Rent</option>
                        </select>
                    </div>

                    {formData.housingType === 'RENT' && (
                        <div className="form-group">
                            <label>Do you have your landlord's permission to keep pets?</label>
                            <select name="hasLandlordPermission" value={formData.hasLandlordPermission} onChange={handleChange} style={selectStyle}>
                                <option value="">Select an answer</option>
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </select>
                        </div>
                    )}

                    <div className="form-group">
                        <label>Do you have a yard?</label>
                        <select name="hasYard" value={formData.hasYard} onChange={handleChange} style={selectStyle}>
                            <option value="">Select an answer</option>
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>How many members are in your household?</label>
                        <input
                            name="householdMembers"
                            type="number"
                            min="1"
                            value={formData.householdMembers}
                            onChange={handleChange}
                            placeholder="e.g. 4"
                        />
                    </div>

                    <div className="form-group">
                        <label>Do you have young children at home?</label>
                        <select name="hasYoungChildren" value={formData.hasYoungChildren} onChange={handleChange} style={selectStyle}>
                            <option value="">Select an answer</option>
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Do you currently have other pets?</label>
                        <select name="hasOtherPets" value={formData.hasOtherPets} onChange={handleChange} style={selectStyle}>
                            <option value="">Select an answer</option>
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Describe your experience with pets</label>
                        <textarea
                            name="petExperience"
                            value={formData.petExperience}
                            onChange={handleChange}
                            placeholder="e.g. Grew up with dogs, currently care for a cat, first-time owner, etc."
                            rows={3}
                            style={{ width: '100%', padding: '11px 14px', border: '1.5px solid #ebebeb', borderRadius: '10px', fontSize: '14px', fontFamily: 'inherit', resize: 'vertical' }}
                        />
                    </div>

                    <div className="form-group">
                        <label>On average, how many hours per day will {animal.name} be left alone?</label>
                        <input
                            name="hoursAwayDaily"
                            type="number"
                            min="0"
                            max="24"
                            value={formData.hoursAwayDaily}
                            onChange={handleChange}
                            placeholder="e.g. 6"
                        />
                    </div>

                    <div className="form-group">
                        <label>Why do you want to adopt {animal.name}?</label>
                        <textarea
                            name="reasonForAdopting"
                            value={formData.reasonForAdopting}
                            onChange={handleChange}
                            placeholder="Tell us your reason for adopting"
                            rows={3}
                            style={{ width: '100%', padding: '11px 14px', border: '1.5px solid #ebebeb', borderRadius: '10px', fontSize: '14px', fontFamily: 'inherit', resize: 'vertical' }}
                        />
                    </div>

                    <div className="form-group">
                        <label>Additional remarks (optional)</label>
                        <input
                            name="remarks"
                            value={formData.remarks}
                            onChange={handleChange}
                            placeholder="Anything else you'd like the shelter to know"
                        />
                    </div>

                    <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <input
                            type="checkbox"
                            name="agreesToReturnPolicy"
                            checked={formData.agreesToReturnPolicy}
                            onChange={handleChange}
                            id="returnPolicyCheck"
                            style={{ width: 'auto' }}
                        />
                        <label htmlFor="returnPolicyCheck" style={{ margin: 0, fontWeight: 400 }}>
                            I agree to the shelter's return policy if the adoption doesn't work out.
                        </label>
                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn-secondary" onClick={onClose}>Cancel</button>
                        <button
                            type="submit"
                            className="btn-primary"
                            style={{ width: 'auto', padding: '10px 24px' }}
                            disabled={submitting}
                        >
                            {submitting ? 'Submitting...' : 'Submit Application'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default ApplicationModal;

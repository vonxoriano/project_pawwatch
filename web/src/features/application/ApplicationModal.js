import React from 'react';
import Modal from '../../components/Modal';
import FormField from '../../components/FormField';
import SelectField from '../../components/SelectField';
import YesNoField from './YesNoField';
import TextAreaField from './TextAreaField';
import AgreementCheckbox from './AgreementCheckbox';
import useApplicationForm from './useApplicationForm';

const selectStyle = {
    width: '100%', padding: '11px 14px',
    border: '1.5px solid #ebebeb', borderRadius: '10px',
    fontSize: '14px', outline: 'none', background: '#fafafa'
};

const HOUSING_OPTIONS = [
    { value: 'OWN', label: 'Own' },
    { value: 'RENT', label: 'Rent' }
];

function ApplicationModal({ animal, onSubmit, onClose }) {
    const { formData, error, submitting, handleChange, handleSubmit } = useApplicationForm(animal, onSubmit);

    return (
        <Modal title={`Adoption Application — ${animal.name}`}>
            <p style={{ fontSize: '13px', color: '#888', marginTop: '-8px', marginBottom: '16px' }}>
                Please answer honestly so our shelter staff can find the best match for {animal.name}.
            </p>

            {error && <div className="error-message">⚠️ {error}</div>}

            <form onSubmit={handleSubmit}>
                <SelectField label="Do you own or rent your home?" name="housingType" value={formData.housingType} onChange={handleChange} options={HOUSING_OPTIONS} style={selectStyle} />

                {formData.housingType === 'RENT' && (
                    <YesNoField label="Do you have your landlord's permission to keep pets?" name="hasLandlordPermission" value={formData.hasLandlordPermission} onChange={handleChange} style={selectStyle} />
                )}

                <YesNoField label="Do you have a yard?" name="hasYard" value={formData.hasYard} onChange={handleChange} style={selectStyle} />

                <FormField label="How many members are in your household?" type="number" name="householdMembers" value={formData.householdMembers} onChange={handleChange} placeholder="e.g. 4" />

                <YesNoField label="Do you have young children at home?" name="hasYoungChildren" value={formData.hasYoungChildren} onChange={handleChange} style={selectStyle} />

                <YesNoField label="Do you currently have other pets?" name="hasOtherPets" value={formData.hasOtherPets} onChange={handleChange} style={selectStyle} />

                <TextAreaField label="Describe your experience with pets" name="petExperience" value={formData.petExperience} onChange={handleChange} placeholder="e.g. Grew up with dogs, currently care for a cat, first-time owner, etc." />

                <FormField label={`On average, how many hours per day will ${animal.name} be left alone?`} type="number" name="hoursAwayDaily" value={formData.hoursAwayDaily} onChange={handleChange} placeholder="e.g. 6" />

                <TextAreaField label={`Why do you want to adopt ${animal.name}?`} name="reasonForAdopting" value={formData.reasonForAdopting} onChange={handleChange} placeholder="Tell us your reason for adopting" />

                <FormField label="Additional remarks (optional)" name="remarks" value={formData.remarks} onChange={handleChange} placeholder="Anything else you'd like the shelter to know" />

                <AgreementCheckbox checked={formData.agreesToReturnPolicy} onChange={handleChange} />

                <div className="modal-footer">
                    <button type="button" className="btn-secondary" onClick={onClose}>Cancel</button>
                    <button type="submit" className="btn-primary" style={{ width: 'auto', padding: '10px 24px' }} disabled={submitting}>
                        {submitting ? 'Submitting...' : 'Submit Application'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}

export default ApplicationModal;
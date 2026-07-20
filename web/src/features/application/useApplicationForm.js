import { useState } from 'react';
import validateApplication from './validateApplication';

function useApplicationForm(animal, onSubmit) {
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        const validationError = validateApplication(formData);
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

    return { formData, error, submitting, handleChange, handleSubmit };
}

export default useApplicationForm;
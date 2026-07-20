function validateApplication(formData) {
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
}

export default validateApplication;
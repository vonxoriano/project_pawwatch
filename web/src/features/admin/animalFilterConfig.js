function getAnimalFilterFields({
    keyword, setKeyword,
    species, onSpeciesChange,
    gender, onGenderChange,
    status, onStatusChange
}) {
    return [
        {
            type: 'text',
            value: keyword,
            onChange: (e) => setKeyword(e.target.value),
            placeholder: 'Search by name or breed...'
        },
        {
            type: 'select',
            value: species,
            onChange: onSpeciesChange,
            options: [
                { value: '', label: 'All Species' },
                { value: 'CAT', label: 'Cat' },
                { value: 'DOG', label: 'Dog' }
            ]
        },
        {
            type: 'select',
            value: gender,
            onChange: onGenderChange,
            options: [
                { value: '', label: 'All Genders' },
                { value: 'MALE', label: 'Male' },
                { value: 'FEMALE', label: 'Female' }
            ]
        },
        {
            type: 'select',
            value: status,
            onChange: onStatusChange,
            options: [
                { value: '', label: 'All Statuses' },
                { value: 'AVAILABLE', label: 'Available' },
                { value: 'PENDING', label: 'Pending' },
                { value: 'ADOPTED', label: 'Adopted' }
            ]
        }
    ];
}

export default getAnimalFilterFields;
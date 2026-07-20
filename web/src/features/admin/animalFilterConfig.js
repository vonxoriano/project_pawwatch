function getAnimalFilterFields({ keyword, setKeyword, species, setSpecies, gender, setGender, status, setStatus }) {
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
            onChange: (e) => setSpecies(e.target.value),
            options: [
                { value: '', label: 'All Species' },
                { value: 'CAT', label: 'Cat' },
                { value: 'DOG', label: 'Dog' }
            ]
        },
        {
            type: 'select',
            value: gender,
            onChange: (e) => setGender(e.target.value),
            options: [
                { value: '', label: 'All Genders' },
                { value: 'MALE', label: 'Male' },
                { value: 'FEMALE', label: 'Female' }
            ]
        },
        {
            type: 'select',
            value: status,
            onChange: (e) => setStatus(e.target.value),
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
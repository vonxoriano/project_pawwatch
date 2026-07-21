import React, { useState, useEffect } from 'react';
import Navbar from '../../components/Navbar';
import FilterBar from '../../components/FilterBar';
import AnimalCard from './AnimalCard';
import animalService from './animalService';

function AdopterDashboard() {
    const [animals, setAnimals] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [species, setSpecies] = useState('');
    const [gender, setGender] = useState('');
    const [minAge, setMinAge] = useState('');
    const [maxAge, setMaxAge] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        fetchAnimals();
    }, []);

    const fetchAnimals = async (kw = '', sp = '', gd = '', minA = '', maxA = '') => {
        try {
            const res = await animalService.browseAnimals(kw, sp, gd, minA, maxA);
            setAnimals(res.data);
        } catch (err) {
            setError('Failed to load animals.');
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchAnimals(keyword, species, gender, minAge, maxAge);
    };

    const handleReset = () => {
        setKeyword('');
        setSpecies('');
        setGender('');
        setMinAge('');
        setMaxAge('');
        fetchAnimals('', '', '', '', '');
    };

    // Species/Gender dropdowns apply immediately on selection (server-side
    // fetch), matching the same instant-apply behavior used on the admin
    // Animal Listings and Adoption Applications tabs.
    const handleSpeciesChange = (e) => {
        const value = e.target.value;
        setSpecies(value);
        fetchAnimals(keyword, value, gender, minAge, maxAge);
    };

    const handleGenderChange = (e) => {
        const value = e.target.value;
        setGender(value);
        fetchAnimals(keyword, species, value, minAge, maxAge);
    };

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <h2>Find Your Companion 🐾</h2>
                 </div>

                {error && <div className="error-message">{error}</div>}

                <FilterBar
                    fields={[
                        {
                            type: 'text',
                            value: keyword,
                            onChange: (e) => setKeyword(e.target.value),
                            placeholder: 'Search by name or breed...'
                        },
                        {
                            type: 'select',
                            value: species,
                            onChange: handleSpeciesChange,
                            options: [
                                { value: '', label: 'All Species' },
                                { value: 'CAT', label: 'Cat' },
                                { value: 'DOG', label: 'Dog' }
                            ]
                        },
                        {
                            type: 'select',
                            value: gender,
                            onChange: handleGenderChange,
                            options: [
                                { value: '', label: 'All Genders' },
                                { value: 'MALE', label: 'Male' },
                                { value: 'FEMALE', label: 'Female' }
                            ]
                        },
                        {
                            type: 'number',
                            min: '0',
                            value: minAge,
                            onChange: (e) => setMinAge(e.target.value),
                            placeholder: 'Min age',
                            style: { flex: '0 1 90px', minWidth: '70px' }
                        },
                        {
                            type: 'number',
                            min: '0',
                            value: maxAge,
                            onChange: (e) => setMaxAge(e.target.value),
                            placeholder: 'Max age',
                            style: { flex: '0 1 90px', minWidth: '70px' }
                        }
                    ]}
                    onFilter={handleSearch}
                    onReset={handleReset}
                    submitLabel="Search"
                />

                {animals.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-icon">🐾</div>
                        <p>No animals available right now. Check back soon!</p>
                    </div>
                ) : (
                    <div className="animals-grid">
                        {animals.map(animal => (
                            <AnimalCard key={animal.animalId} animal={animal} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default AdopterDashboard;
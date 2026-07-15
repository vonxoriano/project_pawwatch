import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import AnimalCard from './AnimalCard';
import animalService from './animalService';

function AdopterDashboard() {
    const navigate = useNavigate();

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

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <h2>Find Your Companion 🐾</h2>
                    <button className="btn-secondary" onClick={() => navigate('/my-applications')}>
                        My Applications
                    </button>
                </div>

                {error && <div className="error-message">{error}</div>}

                <form className="search-bar" onSubmit={handleSearch}>
                    <input
                        type="text"
                        placeholder="Search by name or breed..."
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                    <select value={species} onChange={(e) => setSpecies(e.target.value)}>
                        <option value="">All Species</option>
                        <option value="CAT">Cat</option>
                        <option value="DOG">Dog</option>
                    </select>
                    <select value={gender} onChange={(e) => setGender(e.target.value)}>
                        <option value="">All Genders</option>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                    <input
                        type="number"
                        min="0"
                        placeholder="Min age"
                        value={minAge}
                        onChange={(e) => setMinAge(e.target.value)}
                        style={{ width: '90px' }}
                    />
                    <input
                        type="number"
                        min="0"
                        placeholder="Max age"
                        value={maxAge}
                        onChange={(e) => setMaxAge(e.target.value)}
                        style={{ width: '90px' }}
                    />
                    <button type="submit" className="btn-primary"
                        style={{ width: 'auto', padding: '10px 24px' }}>
                        Search
                    </button>
                    <button type="button" className="btn-secondary" onClick={handleReset}>
                        Reset
                    </button>
                </form>

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
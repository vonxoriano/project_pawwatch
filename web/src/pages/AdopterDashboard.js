import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import AnimalCard from '../components/AnimalCard';
import animalService from '../services/animalService';

function AdopterDashboard() {
    const [animals, setAnimals] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [species, setSpecies] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        fetchAnimals();
    }, []);

    const fetchAnimals = async (kw = '', sp = '') => {
        try {
            const res = await animalService.browseAnimals(kw, sp);
            setAnimals(res.data);
        } catch (err) {
            setError('Failed to load animals.');
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchAnimals(keyword, species);
    };

    const handleReset = () => {
        setKeyword('');
        setSpecies('');
        fetchAnimals('', '');
    };

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <h2>Find Your Companion 🐾</h2>
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
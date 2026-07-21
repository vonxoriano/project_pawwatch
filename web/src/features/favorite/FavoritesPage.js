import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import AnimalCard from '../animal/AnimalCard';
import favoriteService from './favoriteService';

function FavoritesPage() {
    const navigate = useNavigate();
    const [favorites, setFavorites] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchFavorites();
    }, []);

    const fetchFavorites = async () => {
        try {
            const res = await favoriteService.getMyFavorites();
            setFavorites(res.data);
        } catch (err) {
            setError('Failed to load favorites.');
        } finally {
            setLoading(false);
        }
    };

    const handleFavoriteChange = (animalId, isFavorite) => {
        if (!isFavorite) {
            setFavorites(prev => prev.filter(fav => fav.animal.animalId !== animalId));
        }
    };

    return (
        <div className="dashboard-container">
            <Navbar />
            <div className="dashboard-body">
                <div className="page-header">
                    <button className="btn-secondary" onClick={() => navigate('/dashboard')}>
                        ← Browse Animals
                    </button>
                    
                </div>

                {error && <div className="error-message">{error}</div>}

                {loading ? (
                    <p style={{ color: '#999' }}>Loading...</p>
                ) : favorites.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-icon">💔</div>
                        <p>No favorites yet. Browse animals and tap the heart to save them here!</p>
                    </div>
                ) : (
                    <div className="animals-grid">
                        {favorites.map(fav => (
                            <AnimalCard
                                key={fav.animal.animalId}
                                animal={fav.animal}
                                onFavoriteChange={handleFavoriteChange}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default FavoritesPage;
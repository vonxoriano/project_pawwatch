import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import favoriteService from '../favorite/favoriteService';

function AnimalCard({ animal, onFavoriteChange }) {
    const navigate = useNavigate();
    const [isFavorite, setIsFavorite] = useState(false);
    const [loading, setLoading] = useState(false);
    const togglingRef = useRef(false);

    useEffect(() => {
        let mounted = true;
        favoriteService.checkFavorite(animal.animalId)
            .then((result) => {
                if (mounted) setIsFavorite(result);
            })
            .catch(() => {
                // Not logged in or check failed — default to unfavorited, no error shown
            });
        return () => { mounted = false; };
    }, [animal.animalId]);

    const handleToggleFavorite = async (e) => {
        e.stopPropagation(); // don't trigger the card's navigate
        if (togglingRef.current) return; // synchronous guard, immune to state batching
        togglingRef.current = true;
        setLoading(true);

        const wasFavorite = isFavorite;
        try {
            if (wasFavorite) {
                await favoriteService.removeFavorite(animal.animalId);
                setIsFavorite(false);
            } else {
                await favoriteService.addFavorite(animal.animalId);
                setIsFavorite(true);
            }
            if (onFavoriteChange) onFavoriteChange(animal.animalId, !wasFavorite);
        } catch (err) {
            console.error('Favorite toggle failed:', err);
            // Re-sync with backend truth in case of a stale/conflicting state
            try {
                const actual = await favoriteService.checkFavorite(animal.animalId);
                setIsFavorite(actual);
            } catch (syncErr) {
                console.error('Failed to re-sync favorite state:', syncErr);
            }
        } finally {
            togglingRef.current = false;
            setLoading(false);
        }
    };

    return (
        <div className="animal-card" onClick={() => navigate(`/animals/${animal.animalId}`)}>
            <div className="animal-card-photo">
                {animal.photo
                    ? <img src={animal.photo} alt={animal.name} />
                    : (animal.species === 'CAT' ? '🐱' : '🐶')
                }
                <button
                    className="favorite-toggle-btn"
                    onClick={handleToggleFavorite}
                    disabled={loading}
                    aria-label={isFavorite ? 'Remove from favorites' : 'Add to favorites'}
                >
                    {isFavorite ? '❤️' : '🤍'}
                </button>
            </div>
            <div className="animal-card-body">
                <h3>{animal.name}</h3>
                <p>{animal.breed} · {animal.age} yrs · {animal.gender}</p>
                <div className="animal-card-footer">
                    <span className="status-badge status-available">AVAILABLE</span>
                    <span style={{ fontSize: '12px', color: '#FF6B2C', fontWeight: 600 }}>
                        View Profile →
                    </span>
                </div>
            </div>
        </div>
    );
}

export default AnimalCard;
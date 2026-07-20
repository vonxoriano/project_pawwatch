import React, { useState, useEffect } from 'react';
import { supabase } from '../../services/supabaseClient';

function PhotoUploadField({ value, onUploadComplete, onUploadingChange }) {
    const [uploading, setUploading] = useState(false);
    const [previewUrl, setPreviewUrl] = useState(value || '');

    useEffect(() => {
        setPreviewUrl(value || '');
    }, [value]);

    const setUploadingState = (state) => {
        setUploading(state);
        if (onUploadingChange) onUploadingChange(state);
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploadingState(true);
        try {
            const fileExt = file.name.split('.').pop();
            const fileName = `${Date.now()}.${fileExt}`;

            const { error: uploadError } = await supabase.storage
                .from('animal-photos')
                .upload(fileName, file);

            if (uploadError) throw uploadError;

            const { data } = supabase.storage
                .from('animal-photos')
                .getPublicUrl(fileName);

            setPreviewUrl(data.publicUrl);
            onUploadComplete(data.publicUrl);
        } catch (err) {
            alert('Photo upload failed: ' + err.message);
        } finally {
            setUploadingState(false);
        }
    };

    return (
        <div className="form-group">
            <label>Photo</label>
            <input type="file" accept="image/*" onChange={handleFileChange} />
            {uploading && <p style={{ fontSize: '13px', color: '#888' }}>Uploading...</p>}
            {previewUrl && !uploading && (
                <img
                    src={previewUrl}
                    alt="Preview"
                    style={{ width: '100px', height: '100px', objectFit: 'cover', borderRadius: '8px', marginTop: '8px' }}
                />
            )}
        </div>
    );
}

export default PhotoUploadField;
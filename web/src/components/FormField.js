import React from 'react';

function FormField({ label, type = 'text', name, value, onChange, placeholder, required, disabled }) {
    return (
        <div className="form-group">
            <label>{label}</label>
            <input
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                required={required}
                disabled={disabled}
                style={disabled ? { background: '#f5f5f5', color: '#999', cursor: 'not-allowed' } : undefined}
            />
        </div>
    );
}

export default FormField;
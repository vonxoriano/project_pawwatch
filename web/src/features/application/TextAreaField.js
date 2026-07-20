import React from 'react';

const textareaStyle = {
    width: '100%', padding: '11px 14px',
    border: '1.5px solid #ebebeb', borderRadius: '10px',
    fontSize: '14px', fontFamily: 'inherit', resize: 'vertical'
};

function TextAreaField({ label, name, value, onChange, placeholder, rows = 3 }) {
    return (
        <div className="form-group">
            <label>{label}</label>
            <textarea
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                rows={rows}
                style={textareaStyle}
            />
        </div>
    );
}

export default TextAreaField;
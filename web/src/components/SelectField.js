import React from 'react';

function SelectField({ label, name, value, onChange, options, style }) {
    return (
        <div className="form-group">
            <label>{label}</label>
            <select name={name} value={value} onChange={onChange} style={style}>
                {options.map(opt => (
                    <option key={opt.value} value={opt.value}>{opt.label}</option>
                ))}
            </select>
        </div>
    );
}

export default SelectField;
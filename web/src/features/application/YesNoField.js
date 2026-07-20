import React from 'react';
import SelectField from '../../components/SelectField';

const YES_NO_OPTIONS = [
    { value: '', label: 'Select an answer' },
    { value: 'true', label: 'Yes' },
    { value: 'false', label: 'No' }
];

function YesNoField({ label, name, value, onChange, style }) {
    return (
        <SelectField
            label={label}
            name={name}
            value={value}
            onChange={onChange}
            options={YES_NO_OPTIONS}
            style={style}
        />
    );
}

export default YesNoField;
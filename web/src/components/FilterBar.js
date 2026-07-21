import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

// fields: array of { type: 'text'|'select'|'date'|'number', value, onChange,
//   placeholder?, options? (for select: [{value,label}]), style?, min? }
//
// For type 'date': value is a JS Date object (or null), and onChange is
// called with a synthetic { target: { value: date } } object so existing
// consumer handlers (written as (e) => setX(e.target.value)) work unchanged.
function FilterBar({ fields, onFilter, onReset, submitLabel = 'Filter', style }) {
    return (
        <form className="search-bar" onSubmit={onFilter} style={style}>
            {fields.map((field, idx) => {
                if (field.type === 'select') {
                    return (
                        <select key={idx} value={field.value} onChange={field.onChange} style={field.style}>
                            {field.options.map(opt => (
                                <option key={opt.value} value={opt.value}>{opt.label}</option>
                            ))}
                        </select>
                    );
                }
                if (field.type === 'date') {
                    return (
                        <DatePicker
                            key={idx}
                            selected={field.value}
                            onChange={(date) => field.onChange({ target: { value: date } })}
                            placeholderText={field.placeholder || 'Select date'}
                            dateFormat="MMM d, yyyy"
                            className="report-date-input"
                        />
                    );
                }
                return (
                    <input
                        key={idx}
                        type={field.type || 'text'}
                        placeholder={field.placeholder}
                        value={field.value}
                        onChange={field.onChange}
                        style={field.style}
                        min={field.min}
                    />
                );
            })}
            <button type="submit" className="filter-btn">{submitLabel}</button>
            <button type="button" className="filter-btn" onClick={onReset}>Reset</button>
        </form>
    );
}

export default FilterBar;
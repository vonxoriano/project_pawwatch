import React from 'react';

// fields: array of { type: 'text'|'select'|'date'|'number', value, onChange,
//   placeholder?, options? (for select: [{value,label}]), style?, min? }
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
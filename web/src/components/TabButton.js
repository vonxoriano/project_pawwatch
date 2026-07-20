import React from 'react';

function TabButton({ active, onClick, children }) {
    const style = {
        padding: '10px 24px',
        border: 'none',
        borderBottom: active ? '3px solid #FF6B2C' : '3px solid transparent',
        background: 'transparent',
        color: active ? '#FF6B2C' : '#888',
        fontWeight: active ? '700' : '500',
        fontSize: '14px',
        cursor: 'pointer',
        transition: 'all 0.2s'
    };

    return (
        <button style={style} onClick={onClick}>
            {children}
        </button>
    );
}

export default TabButton;
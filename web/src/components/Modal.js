import React from 'react';

// Overlay + card shell only. The form (including its own footer buttons)
// is passed as children, since footer buttons must stay inside the <form>
// for native submit/Enter-key behavior to keep working unchanged.
function Modal({ title, children }) {
    return (
        <div className="modal-overlay">
            <div className="modal">
                {title && <h3>{title}</h3>}
                {children}
            </div>
        </div>
    );
}

export default Modal;
import React from 'react';

function AgreementCheckbox({ checked, onChange }) {
    return (
        <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <input
                type="checkbox"
                name="agreesToReturnPolicy"
                checked={checked}
                onChange={onChange}
                id="returnPolicyCheck"
                style={{ width: 'auto' }}
            />
            <label htmlFor="returnPolicyCheck" style={{ margin: 0, fontWeight: 400 }}>
                I agree to the shelter's return policy if the adoption doesn't work out.
            </label>
        </div>
    );
}

export default AgreementCheckbox;
function getApplicationFilterFields({ keyword, setKeyword, status, onStatusChange, dateFrom, setDateFrom, dateTo, setDateTo }) {
    return [
        {
            type: 'text',
            value: keyword,
            onChange: (e) => setKeyword(e.target.value),
            placeholder: 'Search by applicant or animal name...'
        },
        {
            type: 'select',
            value: status,
            onChange: onStatusChange,
            options: [
                { value: '', label: 'All Statuses' },
                { value: 'PENDING', label: 'Pending' },
                { value: 'APPROVED', label: 'Approved' },
                { value: 'REJECTED', label: 'Rejected' }
            ]
        },
        {
            type: 'date',
            value: dateFrom,
            onChange: (e) => setDateFrom(e.target.value),
            placeholder: 'Start date'
        },
        {
            type: 'date',
            value: dateTo,
            onChange: (e) => setDateTo(e.target.value),
            placeholder: 'End date'
        }
    ];
}

export default getApplicationFilterFields;
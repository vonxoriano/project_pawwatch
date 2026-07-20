function getApplicationFilterFields({ keyword, setKeyword, status, setStatus, dateFrom, setDateFrom, dateTo, setDateTo }) {
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
            onChange: (e) => setStatus(e.target.value),
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
            style: { padding: '10px 12px' }
        },
        {
            type: 'date',
            value: dateTo,
            onChange: (e) => setDateTo(e.target.value),
            style: { padding: '10px 12px' }
        }
    ];
}

export default getApplicationFilterFields;
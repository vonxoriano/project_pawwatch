import { useState, useEffect } from 'react';
import applicationService from '../application/applicationService';

function useAdminApplications(setError, refetchAnimals) {
    const [applications, setApplications] = useState([]);
    const [appStatus, setAppStatus] = useState('');
    const [appKeyword, setAppKeyword] = useState('');
    const [appDateFrom, setAppDateFrom] = useState('');
    const [appDateTo, setAppDateTo] = useState('');

    useEffect(() => {
        fetchApplications();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const fetchApplications = async (status = '', keyword = '', dateFrom = '', dateTo = '') => {
        try {
            const res = await applicationService.getAllApplications(status, keyword, dateFrom, dateTo);
            setApplications(res.data);
        } catch (err) {
            setError('Failed to load applications.');
        }
    };

    const handleAppFilter = (e) => {
        e.preventDefault();
        fetchApplications(appStatus, appKeyword, appDateFrom, appDateTo);
    };

    const handleAppReset = () => {
        setAppStatus('');
        setAppKeyword('');
        setAppDateFrom('');
        setAppDateTo('');
        fetchApplications('', '', '', '');
    };

    const handleProcess = async (id, status, remarks) => {
        const action = status === 'APPROVED' ? 'approve' : 'reject';
        if (window.confirm(`Are you sure you want to ${action} this application?`)) {
            try {
                await applicationService.processApplication(id, status, remarks);
                alert(`Application ${status.toLowerCase()} successfully!`);
                fetchApplications(appStatus, appKeyword, appDateFrom, appDateTo);
                if (refetchAnimals) refetchAnimals();
            } catch (err) {
                alert('Failed to process application.');
            }
        }
    };

    return {
        applications,
        appStatus, setAppStatus,
        appKeyword, setAppKeyword,
        appDateFrom, setAppDateFrom,
        appDateTo, setAppDateTo,
        handleAppFilter, handleAppReset, handleProcess
    };
}

export default useAdminApplications;
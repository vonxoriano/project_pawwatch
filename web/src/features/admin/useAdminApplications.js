import { useState, useEffect } from 'react';
import applicationService from '../application/applicationService';
import { formatDate } from '../../utils/dateFormat';

function useAdminApplications(setError, refetchAnimals) {
    const [applications, setApplications] = useState([]);
    const [appStatus, setAppStatus] = useState('');
    const [appKeyword, setAppKeyword] = useState('');
    const [appDateFrom, setAppDateFrom] = useState(null);
    const [appDateTo, setAppDateTo] = useState(null);

    useEffect(() => {
        fetchApplications();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const fetchApplications = async (status = '', keyword = '', dateFrom = null, dateTo = null) => {
        try {
            const res = await applicationService.getAllApplications(
                status, keyword, formatDate(dateFrom), formatDate(dateTo)
            );
            setApplications(res.data);
        } catch (err) {
            setError('Failed to load applications.');
        }
    };

    const handleAppFilter = (e) => {
        e.preventDefault();
        fetchApplications(appStatus, appKeyword, appDateFrom, appDateTo);
    };

    // Status dropdown applies immediately on selection (server-side fetch),
    // matching the same instant-apply behavior as the Animal Listings tab.
    const handleStatusChange = (e) => {
        const value = e.target.value;
        setAppStatus(value);
        fetchApplications(value, appKeyword, appDateFrom, appDateTo);
    };

    const handleAppReset = () => {
        setAppStatus('');
        setAppKeyword('');
        setAppDateFrom(null);
        setAppDateTo(null);
        fetchApplications('', '', null, null);
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
        handleAppFilter, handleAppReset, handleProcess,
        handleStatusChange
    };
}

export default useAdminApplications;
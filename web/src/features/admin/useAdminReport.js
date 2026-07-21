import { useState } from 'react';
import reportService from './reportService';
import { formatDate } from '../../utils/dateFormat';

function useAdminReport() {
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [report, setReport] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleGenerateReport = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const res = await reportService.getReport(formatDate(startDate), formatDate(endDate));
            setReport(res.data);
        } catch (err) {
            setError(err.response?.data || 'Failed to generate report.');
            setReport(null);
        } finally {
            setLoading(false);
        }
    };

    const handleResetReport = () => {
        setStartDate(null);
        setEndDate(null);
        setReport(null);
        setError('');
    };

    return {
        startDate, setStartDate,
        endDate, setEndDate,
        report, error, loading,
        handleGenerateReport, handleResetReport
    };
}

export default useAdminReport;
// Converts a JS Date object to a 'yyyy-MM-dd' string, matching the format
// expected by the backend's @DateTimeFormat(iso = DATE) LocalDate params.
export function formatDate(date) {
    if (!date) return null;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
export function formatDate(date) {
    return (new Date(Date.parse(date))).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
}
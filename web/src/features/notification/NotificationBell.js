import React, { useState, useEffect, useRef, useCallback } from 'react';
import notificationService from './notificationService';

const POLL_INTERVAL_MS = 45000; // 45 seconds

function formatRelativeTime(dateString) {
    const date = new Date(dateString);
    const seconds = Math.floor((new Date() - date) / 1000);

    if (seconds < 60) return 'just now';
    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) return `${minutes} minute${minutes === 1 ? '' : 's'} ago`;
    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `${hours} hour${hours === 1 ? '' : 's'} ago`;
    const days = Math.floor(hours / 24);
    if (days < 7) return `${days} day${days === 1 ? '' : 's'} ago`;
    return date.toLocaleDateString();
}

function NotificationBell() {
    const [notifications, setNotifications] = useState([]);
    const [open, setOpen] = useState(false);
    const dropdownRef = useRef(null);

    const fetchNotifications = useCallback(async () => {
        try {
            const res = await notificationService.getMyNotifications();
            setNotifications(res.data);
        } catch (err) {
            // Silently fail on poll errors - not critical enough for an alert
        }
    }, []);

    useEffect(() => {
        fetchNotifications();
        const interval = setInterval(fetchNotifications, POLL_INTERVAL_MS);
        return () => clearInterval(interval);
    }, [fetchNotifications]);

    // Close dropdown when clicking outside it
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
                setOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const unreadCount = notifications.filter(n => n.status === 'UNREAD').length;

    const handleNotificationClick = async (notification) => {
        if (notification.status === 'UNREAD') {
            try {
                await notificationService.markAsRead(notification.notificationId);
                setNotifications(prev =>
                    prev.map(n =>
                        n.notificationId === notification.notificationId
                            ? { ...n, status: 'READ' }
                            : n
                    )
                );
            } catch (err) {
                // Ignore - not critical
            }
        }
    };

    return (
        <div className="notification-bell-wrapper" ref={dropdownRef}>
            <button
                className="notification-bell-btn"
                onClick={() => setOpen(!open)}
                aria-label="Notifications">
                🔔
                {unreadCount > 0 && (
                    <span className="notification-badge">{unreadCount > 9 ? '9+' : unreadCount}</span>
                )}
            </button>

            {open && (
                <div className="notification-dropdown">
                    <div className="notification-dropdown-header">
                        <span>Notifications</span>
                    </div>
                    {notifications.length === 0 ? (
                        <div className="notification-empty">No notifications yet.</div>
                    ) : (
                        <div className="notification-list">
                            {notifications.map(n => (
                                <div
                                    key={n.notificationId}
                                    className={`notification-item ${n.status === 'UNREAD' ? 'unread' : ''}`}
                                    onClick={() => handleNotificationClick(n)}>
                                    <p className="notification-message">{n.message}</p>
                                    <span className="notification-time">{formatRelativeTime(n.dateSent)}</span>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default NotificationBell;
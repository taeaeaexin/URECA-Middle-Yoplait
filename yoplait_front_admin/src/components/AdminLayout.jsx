import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const tabs = [
    { label: '대시보드', path: '/dashboard' },
    { label: '사용자 관리', path: '/users' },
    { label: '요금제 관리', path: '/plans' },
    { label: '금칙어 관리', path: '/filters' },
];

const AdminLayout = ({ children }) => {
    const location = useLocation();

    const handleLogout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/';
    };

    return (
        <div style={styles.container}>
            <aside style={styles.sidebar}>
                <h2 style={styles.logo}>Yoplait Admin</h2>
                <nav style={styles.nav}>
                    {tabs.map(tab => (
                        <Link
                            key={tab.path}
                            to={tab.path}
                            style={{
                                ...styles.tabButton,
                                ...(location.pathname === tab.path ? styles.activeTabButton : {}),
                            }}
                        >
                            {tab.label}
                        </Link>
                    ))}
                </nav>
            </aside>
            <main style={styles.main}>
                <header style={styles.header}>
                    <button onClick={handleLogout} style={styles.logoutButton}>
                        로그아웃
                    </button>
                </header>
                <section style={styles.content}>
                    {children}
                </section>
            </main>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        height: '100vh',
        backgroundColor: '#f9f9f9',
        color: '#222',
        fontFamily: 'sans-serif',
    },
    sidebar: {
        width: '220px',
        backgroundColor: '#fff',
        padding: '24px',
        boxSizing: 'border-box',
        borderRight: '1px solid #ddd',
        color: '#333',
    },
    logo: {
        marginBottom: '28px',
        fontSize: '20px',
        fontWeight: '600',
        color: '#333',
    },
    nav: {
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
    },
    tabButton: {
        backgroundColor: 'transparent',
        color: '#333',
        border: 'none',
        textAlign: 'left',
        padding: '12px 14px',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '15px',
        transition: 'background 0.2s, color 0.2s',
        textDecoration: 'none',
    },
    activeTabButton: {
        backgroundColor: '#e6f0ff',
        fontWeight: 'bold',
        color: '#007bff',
    },
    main: {
        flexGrow: 1,
        display: 'flex',
        flexDirection: 'column',
        minHeight: '0',
    },
    header: {
        height: '64px',
        backgroundColor: '#fff',
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        padding: '0 28px',
        borderBottom: '1px solid #ddd',
    },
    logoutButton: {
        backgroundColor: '#007bff',
        border: 'none',
        borderRadius: '6px',
        padding: '10px 18px',
        color: '#fff',
        fontWeight: '600',
        cursor: 'pointer',
    },
    content: {
        flex: 1,
        padding: '36px',
        fontSize: '16px',
        overflow: 'auto',
    },
};

export default AdminLayout;
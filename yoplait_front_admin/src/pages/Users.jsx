import React, { useEffect, useState, useMemo } from 'react';
import AdminLayout from '../components/AdminLayout';
import axios from 'axios';
import {
    BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer
} from 'recharts';

const UsersPage = () => {
    const [users, setUsers] = useState([]);
    const [userLines, setUserLines] = useState({});
    const [planDetails, setPlanDetails] = useState({});
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [expandedUserId, setExpandedUserId] = useState(null);
    const [aiResult, setAiResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [linesLoading, setLinesLoading] = useState({});
    const [searchKeyword, setSearchKeyword] = useState('');
    const [sortBy, setSortBy] = useState('emailAsc');

    useEffect(() => {
        fetchUsers();
    }, [searchKeyword, sortBy]);

    const fetchUsers = () => {
        axios.get('http://localhost:8081/api/admin/users', {
            params: {
                sortBy,
                keyword: searchKeyword
            }
        })
        .then(res => setUsers(res.data.data))
        .catch(err => console.error(err));
    };

    const toggleSort = (key) => {
        setSortBy(prev => {
            const asc = key + 'Asc';
            const desc = key + 'Desc';
            return prev === asc ? desc : asc;
        });
    };

    const fetchUserLines = async (userId) => {
        if (userLines[userId]) return;

        setLinesLoading(prev => ({ ...prev, [userId]: true }));
        try {
            const res = await axios.get(`http://localhost:8081/api/admin/lines/${userId}`);
            const lines = res.data;
            setUserLines(prev => ({ ...prev, [userId]: lines }));

            for (const line of lines) {
                if (!planDetails[line.planId]) {
                    fetchPlanDetails(line.planId);
                }
            }
        } catch (err) {
            console.error('회선 정보 로드 실패:', err);
            setUserLines(prev => ({ ...prev, [userId]: [] }));
        } finally {
            setLinesLoading(prev => ({ ...prev, [userId]: false }));
        }
    };

    const fetchPlanDetails = async (planId) => {
        if (planDetails[planId]) return;

        try {
            const res = await axios.get(`http://localhost:8081/api/admin/plans/${planId}`);
            setPlanDetails(prev => ({ ...prev, [planId]: res.data.data }));
        } catch (err) {
            console.error('요금제 정보 로드 실패:', err);
        }
    };

    const handleToggleLines = (userId) => {
        if (expandedUserId === userId) {
            setExpandedUserId(null);
        } else {
            setExpandedUserId(userId);
            if (!userLines[userId] && !linesLoading[userId]) {
                fetchUserLines(userId);
            }
        }
    };

    const handleViewAnalysis = async (user) => {
        if (selectedUserId === user.id) {
            setSelectedUserId(null);
            return;
        }
        setSelectedUserId(user.id);
        setAiResult(null);
        setLoading(true);
        try {
            const res = await axios.post('http://localhost:8081/api/admin/ai/users', { userId: user.id });
            setAiResult(res.data.data);
        } catch (err) {
            alert("AI 요약을 불러오지 못했습니다.");
        } finally {
            setLoading(false);
        }
    };

    const formatPrice = (price) => new Intl.NumberFormat('ko-KR').format(price) + '원';
    const formatDate = (dateString) => dateString ? new Date(dateString).toLocaleDateString('ko-KR') : '-';
    const formatDataAmount = (amount) => amount === -1 ? '무제한' : `${(amount / 1024).toFixed(1)}GB`;
    const formatCallAmount = (amount) => amount === -1 ? '무제한' : `${amount}분`;
    const formatSmsAmount = (amount) => amount === 15000 ? '기본제공' : `${amount}건`;

    const getStatusBadge = (status) => {
        const styles = {
            active: { backgroundColor: '#10b981', color: 'white' },
            canceled: { backgroundColor: '#ef4444', color: 'white' },
            suspended: { backgroundColor: '#f59e0b', color: 'white' }
        };
        const labels = {
            active: '사용중',
            canceled: '해지',
            suspended: '정지'
        };
        return <span style={{ ...badgeStyle, ...styles[status] }}>{labels[status] || status}</span>;
    };

    const renderDetailedLines = (userId) => {
        if (linesLoading[userId]) return <div style={{ fontStyle: 'italic', color: '#888' }}>회선 정보 로딩중...</div>;

        const lines = userLines[userId] || [];
        if (lines.length === 0) return <div style={{ color: '#666', padding: '16px' }}>등록된 회선이 없습니다.</div>;

        const activeLines = lines.filter(line => line.status === 'active');
        const totalActivePrice = activeLines.reduce((sum, line) => sum + line.discountedPrice, 0);

        return (
            <div style={{ padding: '16px 0' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                    <h4 style={{ margin: 0, fontSize: '16px', color: '#374151' }}>회선 상세 정보</h4>
                    {totalActivePrice > 0 && (
                        <div style={{ fontSize: '18px', fontWeight: '700', color: '#2563eb' }}>
                            월 총 이용료: {formatPrice(totalActivePrice)}
                        </div>
                    )}
                </div>
                <div style={{ display: 'grid', gap: '12px' }}>
                    {lines.map((line, idx) => {
                        const plan = planDetails[line.planId];
                        return (
                            <div key={idx} style={detailedLineItemStyle}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <strong style={{ fontSize: '16px' }}>{line.phoneNumber}</strong>
                                        {getStatusBadge(line.status)}
                                    </div>
                                    <div style={{ fontSize: '16px', fontWeight: '600', color: '#2563eb' }}>
                                        {formatPrice(line.discountedPrice)}
                                    </div>
                                </div>

                                {plan && (
                                    <div style={{ backgroundColor: '#f8fafc', padding: '12px', borderRadius: '6px', marginBottom: '8px' }}>
                                        <div style={{ fontSize: '14px', fontWeight: '500', marginBottom: '4px' }}>{plan.name}</div>
                                        <div style={{ fontSize: '12px', color: '#666', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(120px, 1fr))', gap: '4px' }}>
                                            <div>데이터: {formatDataAmount(plan.dataAmount)}</div>
                                            <div>통화: {formatCallAmount(plan.callAmount)}</div>
                                            <div>SMS: {formatSmsAmount(plan.smsAmount)}</div>
                                            <div>정가: {formatPrice(plan.price)}</div>
                                        </div>
                                        {plan.description && (
                                            <div style={{ fontSize: '12px', color: '#666', marginTop: '6px', fontStyle: 'italic' }}>
                                                {plan.description}
                                            </div>
                                        )}
                                    </div>
                                )}

                                <div style={{ fontSize: '12px', color: '#888', display: 'flex', gap: '16px' }}>
                                    <span>시작일: {formatDate(line.startDate)}</span>
                                    {line.endDate && <span>종료일: {formatDate(line.endDate)}</span>}
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        );
    };

    return (
        <AdminLayout>
            <h1 style={{ fontSize: '28px', fontWeight: 'bold', marginBottom: '24px' }}>사용자 관리</h1>
            <div style={{ marginBottom: '16px' }}>
                <input
                    type="text"
                    placeholder="이름 또는 이메일 검색"
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    style={{
                        padding: '8px 12px',
                        borderRadius: '4px',
                        border: '1px solid #ccc',
                        width: '300px',
                        fontSize: '14px'
                    }}
                />
            </div>
            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', minWidth: '800px', borderCollapse: 'separate', borderSpacing: 0, boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
                    <thead>
                    <tr style={{ backgroundColor: '#f9fafb' }}>
                        <th style={thStyle} onClick={() => toggleSort('name')}>
                            <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', minWidth: '80px' }}>
                                이름 {sortBy.includes('name') && (sortBy === 'nameAsc' ? '▲' : '▼')}
                            </span>
                        </th>
                        <th style={thStyle} onClick={() => toggleSort('email')}>
                            <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', minWidth: '80px' }}>
                                이메일 {sortBy.includes('email') && (sortBy === 'emailAsc' ? '▲' : '▼')}
                            </span>
                        </th>
                        <th style={thStyle} onClick={() => toggleSort('service')}>
                            <span style={{ display: 'inline-flex', alignItems: 'center', gap: '4px', minWidth: '120px' }}>
                                가입된 서비스 수 {sortBy.includes('service') && (sortBy === 'serviceAsc' ? '▲' : '▼')}
                            </span>
                        </th>
                        <th style={thStyle}>상세보기</th>
                        <th style={thStyle}>AI 요약</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(user => (
                        <React.Fragment key={user.id}>
                            <tr style={{ backgroundColor: '#fff', transition: 'background 0.2s' }}>
                                <td style={tdStyle}>{user.name}</td>
                                <td style={tdStyle}>{user.email}</td>
                                <td style={tdStyle}>{user.subscribedCount || 0}개</td>
                                <td style={tdStyle}>
                                    <button
                                        onClick={() => handleToggleLines(user.id)}
                                        style={expandButtonStyle}
                                    >
                                        {expandedUserId === user.id ? '접기' : '상세보기'}
                                    </button>
                                </td>
                                <td style={tdStyle}>
                                    <button onClick={() => handleViewAnalysis(user)} style={buttonStyle}>
                                        {selectedUserId === user.id ? '접기' : '요약 보기'}
                                    </button>
                                </td>
                            </tr>
                            {expandedUserId === user.id && (
                                <tr>
                                    <td colSpan={5} style={{ padding: '0 32px', backgroundColor: '#f9fafb', borderBottom: '1px solid #eee' }}>
                                        {renderDetailedLines(user.id)}
                                    </td>
                                </tr>
                            )}
                            {selectedUserId === user.id && (
                                <tr>
                                    <td colSpan={5} style={{ padding: '24px 32px', backgroundColor: '#fefefe', borderTop: '1px solid #eee' }}>
                                        {loading ? (
                                            <div style={{ fontStyle: 'italic', color: '#888' }}>AI 분석 불러오는 중...</div>
                                        ) : aiResult ? (
                                            <>
                                                <h4 style={{ margin: '0 0 16px 0', fontSize: '16px', color: '#374151' }}>AI 분석 결과</h4>
                                                <p style={{ marginBottom: '16px', fontSize: '15px', lineHeight: 1.6, color: '#333' }}>{aiResult.summary}</p>
                                                <div style={{ height: '250px', marginBottom: '24px' }}>
                                                    <ResponsiveContainer width="100%" height="100%">
                                                        <BarChart data={aiResult.frequentTopics}>
                                                            <CartesianGrid strokeDasharray="3 3" />
                                                            <XAxis dataKey="topic" />
                                                            <YAxis />
                                                            <Tooltip />
                                                            <Bar dataKey="frequency" fill="#3B82F6" radius={[4, 4, 0, 0]} />
                                                        </BarChart>
                                                    </ResponsiveContainer>
                                                </div>
                                                <div style={{ display: 'grid', gap: '12px' }}>
                                                    {aiResult.frequentTopics.map((topic, idx) => (
                                                        <div key={idx} style={{ background: '#f9fafb', border: '1px solid #e5e7eb', borderRadius: '8px', padding: '16px' }}>
                                                            <strong style={{ fontSize: '14px' }}>{topic.topic}</strong> <span style={{ fontSize: '12px', color: '#6b7280' }}>({topic.percentage}%)</span>
                                                            <ul style={{ paddingLeft: '20px', marginTop: '6px', fontSize: '13px', color: '#374151' }}>
                                                                {topic.examples.map((ex, i) => <li key={i}>{ex}</li>)}
                                                            </ul>
                                                        </div>
                                                    ))}
                                                </div>
                                            </>
                                        ) : (
                                            <div style={{ color: '#999' }}>분석 결과 없음</div>
                                        )}
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                    </tbody>
                </table>
            </div>
        </AdminLayout>
    );
};

const thStyle = {
    padding: '14px 20px',
    borderBottom: '1px solid #e5e7eb',
    textAlign: 'left',
    fontSize: '14px',
    fontWeight: '600',
    color: '#374151',
    whiteSpace: 'nowrap'
};

const tdStyle = {
    padding: '12px 20px',
    borderBottom: '1px solid #f3f4f6',
    fontSize: '14px',
    color: '#111827',
    verticalAlign: 'top'
};

const buttonStyle = {
    backgroundColor: '#2563eb',
    color: '#fff',
    border: 'none',
    borderRadius: '6px',
    padding: '8px 14px',
    cursor: 'pointer',
    fontSize: '13px',
    fontWeight: '500',
    transition: 'background 0.2s ease-in-out'
};

const badgeStyle = {
    fontSize: '11px',
    fontWeight: '500',
    padding: '2px 8px',
    borderRadius: '12px',
    whiteSpace: 'nowrap'
};

const expandButtonStyle = {
    backgroundColor: '#f3f4f6',
    color: '#374151',
    border: '1px solid #d1d5db',
    borderRadius: '4px',
    padding: '4px 8px',
    cursor: 'pointer',
    fontSize: '11px',
    fontWeight: '500',
    transition: 'all 0.2s ease-in-out'
};

const detailedLineItemStyle = {
    backgroundColor: '#ffffff',
    border: '1px solid #e5e7eb',
    borderRadius: '8px',
    padding: '16px',
    boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
};

export default UsersPage;

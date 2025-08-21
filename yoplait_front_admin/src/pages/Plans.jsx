import React, { useEffect, useState } from 'react';
import AdminLayout from '../components/AdminLayout';
import axios from 'axios';
import { Link } from 'react-router-dom';

const Plans = () => {
    const [plans, setPlans] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [sortBy, setSortBy] = useState('');
    const [sortOrder, setSortOrder] = useState('asc');
    const [selectedPlan, setSelectedPlan] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isAnalyzing, setIsAnalyzing] = useState(false);

    // 요금제 생성 버튼 클릭 시: 임시 plan 생성하여 모달 오픈
    const createEmptyPlan = () => {
        const defaultPlan = {
            id: `temp-${Date.now()}`,
            name: "새 요금제",
            price: 0,
            dataAmount: 0,
            callAmount: 0,
            smsAmount: 0,
            description: "",
            createdAt: new Date().toISOString(),
            userAmount: 0,
            analysisResult: null,
        };
        setSelectedPlan(defaultPlan);
        setIsModalOpen(true);
    };

    // 📥 요금제 목록 조회 API 호출
    const fetchPlans = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/admin/plans', {
                params: {
                    sortBy: `${sortBy}${sortOrder === 'asc' ? 'Asc' : 'Desc'}`,
                    keyword
                }
                // 정렬 기준(sortBy + 정렬 방향)과 키워드로 서버에 요청
            });

            let data = response.data.data;

            // 커스텀 정렬: 데이터/통화 정렬 시 무제한(-1)은 Infinity로 취급
            if (sortBy === 'data') {
                data = [...data].sort((a, b) => {
                    const aVal = a.dataAmount === -1 ? Infinity : a.dataAmount;
                    const bVal = b.dataAmount === -1 ? Infinity : b.dataAmount;
                    return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
                });
            } else if (sortBy === 'call') {
                data = [...data].sort((a, b) => {
                    const aVal = a.callAmount === -1 ? Infinity : a.callAmount;
                    const bVal = b.callAmount === -1 ? Infinity : b.callAmount;
                    return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
                });
            }

            setPlans(data);
        } catch (error) {
            console.error('요금제 목록 조회 실패:', error);
        }
    };

    // 📄 특정 요금제 상세정보 조회 API 호출
    const fetchPlanDetail = async (id) => {
        try {
            const response = await axios.get(`http://localhost:8081/api/admin/plans/${id}`);
            setSelectedPlan(response.data.data);
            setIsModalOpen(true);
        } catch (error) {
            console.error('상세 조회 실패:', error);
        }
    };

    // 🔁 키워드 또는 정렬 조건 변경 시 목록 재요청
    useEffect(() => {
        fetchPlans();
    }, [keyword, sortBy, sortOrder]);

    // 🔃 테이블 헤더 클릭 시 정렬 방식 변경 핸들러
    const handleSort = (field) => {
        if (sortBy === field) {
            setSortOrder(prev => prev === 'asc' ? 'desc' : 'asc');
        } else {
            setSortBy(field);
            setSortOrder('asc');
        }
    };

    // 📦 전체 컴포넌트 렌더링 영역 시작
    return (
        <AdminLayout>
            <h1>요금제 관리</h1>
            <p style={{ fontSize: '13px', color: '#888', marginBottom: '16px' }}>
                - 각 항목을 누르면 정렬됩니다<br />
                - 통화, 데이터가 -1이면 무제한입니다<br />
                - 문자량이 15000건이면 기본 제공입니다
            </p>
            <div style={{ maxHeight: '77%', minHeight: '300px', overflowY: 'auto', overflowX: 'hidden' }}>
                <div style={{ display: 'flex', alignItems: 'center', margin: '16px 0', gap: '12px' }}>
                    <input
                        type="text"
                        placeholder="요금제명 검색"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                        style={{
                            padding: '8px',
                            width: '250px',
                            borderRadius: '4px',
                            border: '1px solid #ccc',
                            fontSize: '14px',
                        }}
                    />
                    {/* ➕ 요금제 추가 버튼 */}
                    <button
                        onClick={createEmptyPlan}
                        style={{
                            padding: '8px 16px',
                            backgroundColor: '#007bff',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '6px',
                            fontSize: '14px',
                            fontWeight: 'bold',
                            cursor: 'pointer'
                        }}
                    >
                        + 요금제 추가
                    </button>
                </div>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr style={{ backgroundColor: '#f0f0f0' }}>
                            <th style={thStyle} onClick={() => handleSort('name')}>
                                <span style={{ display: 'inline-block', width: '60px' }}>
                                    요금제 {sortBy === 'name' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle} onClick={() => handleSort('price')}>
                                <span style={{ display: 'inline-block', width: '50px' }}>
                                    가격 {sortBy === 'price' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle} onClick={() => handleSort('data')}>
                                <span style={{ display: 'inline-block', width: '60px' }}>
                                    데이터 {sortBy === 'data' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle} onClick={() => handleSort('call')}>
                                <span style={{ display: 'inline-block', width: '50px' }}>
                                    통화 {sortBy === 'call' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle} onClick={() => handleSort('sms')}>
                                <span style={{ display: 'inline-block', width: '50px' }}>
                                    문자 {sortBy === 'sms' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle} onClick={() => handleSort('user')}>
                                <span style={{ display: 'inline-block', width: '70px' }}>
                                    가입자 {sortBy === 'user' ? (sortOrder === 'asc' ? '▲' : '▼') : ''}
                                </span>
                            </th>
                            <th style={thStyle}>상세</th>
                        </tr>
                    </thead>
                    <tbody>
                        {plans.map((plan) => (
                            // 📝 각 요금제 행 렌더링
                            <tr key={plan.id}>
                                <td style={tdStyle}>{plan.name}</td>
                                <td style={tdStyle}>{Number(plan.price).toLocaleString()}원</td>
                                <td style={tdStyle}>
                                  {plan.dataAmount === -1 ? '무제한' : `${(Number(plan.dataAmount) / 1024).toFixed(1)}GB`}
                                </td>
                                <td style={tdStyle}>{plan.callAmount === -1 ? '무제한' : `${Number(plan.callAmount).toLocaleString()}분`}</td>
                                <td style={tdStyle}>{plan.smsAmount === 15000 ? '기본 제공' : `${Number(plan.smsAmount).toLocaleString()}건`}</td>
                                <td style={tdStyle}>{Number(plan.userAmount).toLocaleString()}명</td>
                                <td style={tdStyle}>
                                    <button
                                      onClick={() => fetchPlanDetail(plan.id)}
                                      style={{
                                        backgroundColor: '#007bff',
                                        color: '#fff',
                                        border: 'none',
                                        borderRadius: '4px',
                                        padding: '6px 12px',
                                        cursor: 'pointer',
                                        fontSize: '13px'
                                      }}
                                    >
                                      상세
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            {isModalOpen && selectedPlan && (
                // 🪟 상세 모달 (요금제 정보 + 삭제/수정/생성)
                <div style={{
                    position: 'fixed',
                    top: 0, left: 0, right: 0, bottom: 0,
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex', justifyContent: 'center', alignItems: 'center',
                    zIndex: 1000
                }}>
                    <div style={{
                        background: '#fff',
                        padding: '24px',
                        borderRadius: '8px',
                        width: '400px',
                        maxHeight: '80vh',
                        overflowY: 'auto',
                        boxShadow: '0 2px 10px rgba(0,0,0,0.2)',
                        position: 'relative'
                    }}>
                        <button
                            onClick={() => setIsModalOpen(false)}
                            style={{
                                position: 'absolute',
                                top: '12px',
                                right: '12px',
                                backgroundColor: '#ff4d4f',
                                border: 'none',
                                borderRadius: '50%',
                                width: '24px',
                                height: '24px',
                                color: '#fff',
                                fontWeight: 'bold',
                                cursor: 'pointer'
                            }}
                        >×</button>
                        <h3>
                          <input
                            value={selectedPlan.name}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, name: e.target.value })}
                            style={{ width: '100%', fontSize: '18px', fontWeight: 'bold', marginBottom: '8px' }}
                          />
                        </h3>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                          <strong style={{ width: '60px' }}>가격:</strong>
                          <input
                            type="number"
                            value={selectedPlan.price}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, price: e.target.value })}
                            style={{ flex: 1 }}
                          />
                          <span>원</span>
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                          <strong style={{ width: '60px' }}>데이터:</strong>
                          <input
                            type="number"
                            value={selectedPlan.dataAmount}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, dataAmount: e.target.value })}
                            style={{ flex: 1 }}
                          />
                          <span>MB</span>
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                          <strong style={{ width: '60px' }}>통화:</strong>
                          <input
                            type="number"
                            value={selectedPlan.callAmount}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, callAmount: e.target.value })}
                            style={{ flex: 1 }}
                          />
                          <span>분</span>
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                          <strong style={{ width: '60px' }}>문자:</strong>
                          <input
                            type="number"
                            value={selectedPlan.smsAmount}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, smsAmount: e.target.value })}
                            style={{ flex: 1 }}
                          />
                          <span>건</span>
                        </div>
                        <p>
                          <strong>설명: </strong>
                          <textarea
                            value={selectedPlan.description}
                            onChange={(e) => setSelectedPlan({ ...selectedPlan, description: e.target.value })}
                            style={{ width: '100%', height: '80px' }}
                          />
                        </p>
                        <hr style={{ margin: '16px 0' }} />
                        {/* AI 분석 버튼/결과: 새로 생성하는 경우(임시 id)에는 표시하지 않음 */}
                        {!selectedPlan?.id?.startsWith("temp-") && (
                          <div style={{ marginBottom: '12px', width: '100%' }}>
                            {isAnalyzing ? (
                              <div style={{ padding: '10px', textAlign: 'center', fontStyle: 'italic' }}>분석 중...</div>
                            ) : selectedPlan && selectedPlan.analysisResult ? (
                              <div style={{
                                padding: '10px',
                                border: '1px solid #ccc',
                                borderRadius: '4px',
                                background: '#f9f9f9',
                                maxHeight: '200px',
                                overflowY: 'auto'
                              }}>
                                <strong>AI 분석 결과</strong>
                                <ul style={{ paddingLeft: '20px', margin: 0 }}>
                                  <li><strong>요약:</strong> {selectedPlan.analysisResult.summary}</li>
                                  <li><strong>권장:</strong> {selectedPlan.analysisResult.decision}</li>
                                  <li><strong>강점:</strong> {selectedPlan.analysisResult.strength}</li>
                                  <li><strong>약점:</strong> {selectedPlan.analysisResult.weakness}</li>
                                  <li><strong>유사:</strong> {selectedPlan.analysisResult.redundancy}</li>
                                  <li><strong>분석:</strong> {selectedPlan.analysisResult.userFeedback}</li>
                                  <li><strong>제안:</strong> {selectedPlan.analysisResult.recommendedStrategy}</li>
                                </ul>
                              </div>
                            ) : (
                              <button
                                onClick={async () => {
                                  setIsAnalyzing(true);
                                  try {
                                    const res = await axios.post(`http://localhost:8081/api/admin/ai/plans/${selectedPlan.id}`);
                                    setSelectedPlan(prev => ({
                                      ...prev,
                                      analysisResult: res.data.data,
                                    }));
                                  } catch (err) {
                                    console.error('AI 분석 실패:', err);
                                    alert('AI 분석 실패');
                                  } finally {
                                    setIsAnalyzing(false);
                                  }
                                }}
                                style={{
                                  backgroundColor: '#4caf50',
                                  color: '#fff',
                                  border: 'none',
                                  borderRadius: '4px',
                                  padding: '10px 16px',
                                  fontWeight: 'bold',
                                  cursor: 'pointer',
                                  width: '100%',
                                }}
                              >
                                AI 분석
                              </button>
                            )}
                          </div>
                        )}
                        <p><strong>생성일:</strong> {selectedPlan.createdAt}</p>
                        <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center', gap: '16px' }}>
                          {selectedPlan?.id?.startsWith("temp-") ? (
                            <>
                              <button
                                onClick={async () => {
                                  // 생성 버튼 클릭 시 POST API 호출
                                  try {
                                    const { id, analysisResult, ...planToCreate } = selectedPlan;
                                    const response = await axios.post('http://localhost:8081/api/admin/plans', planToCreate);
                                    alert("생성하였습니다.");
                                    setIsModalOpen(false);
                                    fetchPlans();
                                    fetchPlanDetail(response.data.data.id);
                                  } catch (error) {
                                    console.error("요금제 생성 실패:", error);
                                    alert("요금제 생성에 실패했습니다.");
                                  }
                                }}
                                style={{
                                  backgroundColor: '#007bff',
                                  color: '#fff',
                                  border: 'none',
                                  borderRadius: '4px',
                                  padding: '8px 12px',
                                  fontWeight: 'bold',
                                  cursor: 'pointer'
                                }}
                              >
                                생성
                              </button>
                              <button
                                onClick={() => setIsModalOpen(false)}
                                style={{
                                  backgroundColor: '#888',
                                  color: '#fff',
                                  border: 'none',
                                  borderRadius: '4px',
                                  padding: '8px 12px',
                                  fontWeight: 'bold',
                                  cursor: 'pointer'
                                }}
                              >
                                취소
                              </button>
                            </>
                          ) : (
                            <>
                              <button
                                onClick={async () => {
                                  // ✏️ 수정 버튼 클릭 시 PUT API 호출
                                  try {
                                    await axios.put(`http://localhost:8081/api/admin/plans/${selectedPlan.id}`, selectedPlan);
                                    alert("수정하였습니다.");
                                    setIsModalOpen(false);
                                    fetchPlans();
                                  } catch (error) {
                                    console.error('수정 실패:', error);
                                    alert("수정에 실패했습니다.");
                                  }
                                }}
                                style={{
                                  backgroundColor: '#ffc107',
                                  color: '#000',
                                  border: 'none',
                                  borderRadius: '4px',
                                  padding: '8px 12px',
                                  fontWeight: 'bold',
                                  cursor: 'pointer'
                                }}
                              >
                                수정
                              </button>
                              <button
                                onClick={async () => {
                                  // 🗑️ 삭제 버튼 클릭 시 확인 후 API 호출
                                  try {
                                    const confirm = window.confirm("삭제하시겠습니까?");
                                    if (confirm) {
                                      await axios.delete(`http://localhost:8081/api/admin/plans/${selectedPlan.id}`);
                                      alert("삭제하였습니다.");
                                      setIsModalOpen(false);
                                      fetchPlans();
                                    }
                                  } catch (error) {
                                    console.error('삭제 실패:', error);
                                    const errorMessage = error.response?.data?.message || "⚠ 이 요금제에 가입 중인 사용자가 있어 삭제할 수 없습니다.";
                                    alert(errorMessage);
                                  }
                                }}
                                style={{
                                  backgroundColor: '#dc3545',
                                  color: '#fff',
                                  border: 'none',
                                  borderRadius: '4px',
                                  padding: '8px 12px',
                                  fontWeight: 'bold',
                                  cursor: 'pointer'
                                }}
                              >
                                삭제
                              </button>
                            </>
                          )}
                        </div>
                    </div>
                </div>
            )}
        </AdminLayout>
    );
};

const thStyle = {
    padding: '12px',
    borderBottom: '1px solid #ccc',
    textAlign: 'left',
    cursor: 'pointer',
};

const tdStyle = {
    padding: '10px',
    borderBottom: '1px solid #eee',
};

export default Plans;

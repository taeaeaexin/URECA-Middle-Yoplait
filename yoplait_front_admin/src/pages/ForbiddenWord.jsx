import React, { useEffect, useState } from 'react';
import AdminLayout from '../components/AdminLayout';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function ForbiddenWordPage() {
    const [forbiddenWords, setForbiddenWords] = useState([]);
    const [allWords, setAllWords] = useState([]);
    const [searchKeyword, setSearchKeyword] = useState('');
    const [searchClass, setSearchClass] = useState('');
    const [selectedWord, setSelectedWord] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editWord, setEditWord] = useState('');
    const [editDesc, setEditDesc] = useState('');
    const [editClass, setEditClass] = useState('');
    const navigate = useNavigate();

    const isCreateMode = !selectedWord;

    const fetchAllWords = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/admin/forbidden');
            setAllWords(response.data);
        } catch (error) {
            console.error('금칙어 목록을 불러오지 못했습니다.', error);
        }
    };

    useEffect(() => {
        fetchAllWords();
    }, []);

    useEffect(() => {
        const filtered = allWords.filter(word => {
            const matchesKeyword = searchKeyword ? word.word.includes(searchKeyword) : true;
            const matchesClass = searchClass ? word.wordClass === searchClass : true;
            return matchesKeyword && matchesClass;
        });
        setForbiddenWords(filtered);
    }, [allWords, searchKeyword, searchClass]);

    useEffect(() => {
      if (selectedWord) {
        setEditWord(selectedWord.word);
        setEditDesc(selectedWord.wordDesc);
        setEditClass(selectedWord.wordClass);
      }
    }, [selectedWord]);

    // 삭제 및 수정 함수
    const handleDelete = async () => {
      if (window.confirm('정말 삭제하시겠습니까?')) {
        try {
          const token = localStorage.getItem('accessToken');
          await axios.delete(`http://localhost:8081/api/admin/forbidden/${selectedWord.wordId}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          alert('삭제되었습니다.');
          setIsModalOpen(false);
          fetchAllWords(); // refresh list
        } catch (error) {
          console.error('삭제 실패:', error);
        }
      }
    };

    const handleEdit = async () => {
      if (window.confirm(isCreateMode ? '등록하시겠습니까?' : '정말 수정하시겠습니까?')) {
        try {
          const url = isCreateMode

            ? 'http://localhost:8081/api/admin/forbidden'
            : `http://localhost:8081/api/admin/forbidden/${selectedWord.wordId}`;

        const method = isCreateMode ? 'post' : 'put';
          const token = localStorage.getItem('accessToken');
          await axios[method](url, {
            word: editWord,
            wordDesc: editDesc,
            wordClass: editClass,
          }, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          alert(isCreateMode ? '등록되었습니다.' : '수정되었습니다.');
          setIsModalOpen(false);
          fetchAllWords();
        } catch (error) {
          console.error(isCreateMode ? '등록 실패:' : '수정 실패:', error);
        }
      }
    };

    return (
      <AdminLayout>
        <h1>금칙어 관리</h1>
        <div style={{ maxHeight: '77%', minHeight: '300px', overflowY: 'auto', overflowX: 'hidden' }}>
          <div style={{ display: 'flex', alignItems: 'center', margin: '16px 0', gap: '12px' }}>
            <input
              type="text"
              placeholder="금칙어 검색"
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              style={{
                padding: '8px',
                width: '250px',
                borderRadius: '4px',
                border: '1px solid #ccc',
                fontSize: '14px',
              }}
            />
            <select
              value={searchClass}
              onChange={(e) => setSearchClass(e.target.value)}
              style={{
                padding: '8px',
                borderRadius: '4px',
                border: '1px solid #ccc',
                fontSize: '14px',
              }}
            >
              <option value="">전체</option>
              {Array.from(new Set(allWords.map(word => word.wordClass)))
                .filter(Boolean)
                .sort()
                .map(cls => (
                  <option key={cls} value={cls}>{cls}</option>
              ))}
            </select>
            <button onClick={() => {
              setSelectedWord(null);
              setEditWord('');
              setEditDesc('');
              setEditClass('');
              setIsModalOpen(true);
            }}
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
            >금칙어 추가</button>
          </div>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ backgroundColor: '#f0f0f0' }}>
                <th style={thStyle}>금칙어</th>
                <th style={thStyle}>분류</th>
                <th style={thStyle}>상세</th>
              </tr>
            </thead>
            <tbody>
              {forbiddenWords.map((word) => (
                <tr key={word.id}>
                  <td style={tdStyle}>{word.word}</td>
                  <td style={tdStyle}>{word.wordClass}</td>
                  <td style={tdStyle}>
                    <button style={detailButtonStyle} onClick={() => {
                      setSelectedWord(word);
                      setIsModalOpen(true);
                    }}>상세</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {isModalOpen && (
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
                {isCreateMode ? '금칙어 추가' : '금칙어 상세정보'}</h3>
              <p><strong>단어:</strong></p>
              <input
                type="text"
                value={editWord}
                onChange={(e) => setEditWord(e.target.value)}
                style={{ width: '100%', marginBottom: '8px', padding: '6px', boxSizing: 'border-box' }}
              />
              <p><strong>설명:</strong></p>
              <textarea
                value={editDesc}
                onChange={(e) => setEditDesc(e.target.value)}
                style={{ width: '100%', marginBottom: '8px', padding: '6px', boxSizing: 'border-box', resize: 'vertical' }}
                rows={3}
              />
              <p><strong>분류:</strong></p>
              <input
                type="text"
                value={editClass}
                onChange={(e) => setEditClass(e.target.value)}
                style={{ width: '100%', marginBottom: '8px', padding: '6px', boxSizing: 'border-box' }}
              />
              {!isCreateMode && <p><strong>업데이트:</strong> {selectedWord.wordUpdate}</p>}
              <div style={{ marginTop: '24px', display: 'flex', justifyContent: 'flex-end', gap: '12px' }}>
                <button onClick={handleEdit}>{isCreateMode ? '등록' : '수정'}</button>
                {!isCreateMode && <button onClick={handleDelete} style={{ backgroundColor: '#ff4d4f', color: '#fff' }}>삭제</button>}
              </div>
            </div>
          </div>
        )}
      </AdminLayout>
    );
}

const thStyle = {
    padding: '12px',
    borderBottom: '1px solid #ccc',
    textAlign: 'left',
};

const tdStyle = {
    padding: '10px',
    borderBottom: '1px solid #eee',
};

const detailButtonStyle = {
  marginRight: '8px',
  backgroundColor: '#007bff',
  color: 'white',
  border: 'none',
  padding: '6px 12px',
  borderRadius: '4px',
  cursor: 'pointer'
};

export default ForbiddenWordPage;
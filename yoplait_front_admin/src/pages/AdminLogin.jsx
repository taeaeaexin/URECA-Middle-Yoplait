import React, { useState } from 'react';
import axios from 'axios';

function AdminLogin() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();

        // 분기처리 상세화 필요
        try {
            const response = await axios.post('http://localhost:8081/api/login', {
                email,
                password,
            });

            if (response.data.result === 'SUCCESS') {
                alert('로그인 성공!');
                const { accessToken, refreshToken } = response.data.data;

                localStorage.setItem("accessToken", accessToken);
                localStorage.setItem("refreshToken", refreshToken);

                window.location.href = "/dashboard";
            } else {
                setErrorMsg('이메일 또는 비밀번호가 올바르지 않습니다.');
            }
        } catch (error) {
            console.error(error);
            setErrorMsg('로그인 중 오류가 발생했습니다.');
        }
    };

    return (
        <div style={styles.container}>
            <form style={styles.form} onSubmit={handleLogin}>
                <h2 style={styles.title}>관리자 로그인</h2>
                <input
                    type="email"
                    placeholder="이메일"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    style={styles.input}
                />
                <input
                    type="password"
                    placeholder="패스워드"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    style={styles.input}
                />
                {errorMsg && <p style={styles.error}>{errorMsg}</p>}
                <button type="submit" style={styles.button}>로그인</button>
            </form>
        </div>
    );
}

const styles = {
    container: {
        width: '100vw',
        height: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f9f9f9',
    },
    form: {
        padding: '36px 32px',
        borderRadius: '10px',
        backgroundColor: '#ffffff',
        boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
        textAlign: 'center',
        width: '100%',
        maxWidth: '400px',
        display: 'flex',
        flexDirection: 'column',
        gap: '16px',
        color: '#222',
        boxSizing: 'border-box',
    },
    title: {
        margin: 0,
        fontSize: '22px',
        fontWeight: 600,
        color: '#222',
    },
    input: {
        width: '100%',
        padding: '12px 14px',
        borderRadius: '6px',
        border: '1px solid #ccc',
        backgroundColor: '#ffffff',
        color: '#222',
        fontSize: '15px',
        outline: 'none',
        boxSizing: 'border-box',
    },
    button: {
        width: '100%',
        padding: '12px 14px',
        backgroundColor: '#007bff',
        border: 'none',
        borderRadius: '6px',
        fontWeight: '600',
        fontSize: '15px',
        color: '#fff',
        cursor: 'pointer',
        transition: 'background-color 0.3s ease',
    },
    error: {
        color: '#ff6b6b',
        fontSize: '14px',
        marginTop: '0',
    }
};

export default AdminLogin;
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AdminLogin from './pages/AdminLogin';
import Dashboard from './pages/Dashboard';
import PrivateRoute from './routes/PrivateRoute';
import ForbiddenWord from "./pages/ForbiddenWord";
import Plans from './pages/Plans';
import Users from './pages/Users';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<AdminLogin />} />
                <Route path="/dashboard" element={ <PrivateRoute> <Dashboard /></PrivateRoute>}/>
                <Route path="/filters" element={<PrivateRoute><ForbiddenWord /></PrivateRoute>}/>
                <Route path="/plans" element={<PrivateRoute><Plans /></PrivateRoute>}/>
                <Route path="/users" element={<PrivateRoute><Users /></PrivateRoute>}/>
            </Routes>
        </Router>
    );
}

export default App;
// src/App.js
import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import AdminPanel from './components/AdminPanel';
import SellerPanel from './seller_components/SellerPanel';

function RoleSelection() {
  const navigate = useNavigate();

  const handleRoleClick = (role) => {
    if (role === 'Админ') {
      navigate('/admin');
    } else if (role === 'Продавец') {
      navigate('/seller');
    } else {
      alert(`Вы выбрали роль: ${role}`);
    }
  };

  return (
    <div className="App">
      <div className="welcome-container">
        <h1 className="welcome-title">
          Добро пожаловать в <span className="pharma-pro">АптекаПРО</span>
        </h1>
        <p className="welcome-subtitle">Выберите вашу роль в системе</p>

        <div className="roles-grid">
          <div className="role-card" onClick={() => handleRoleClick('Покупатель')}>
            <div className="role-icon">👨‍⚕️</div>
            <h3>Покупатель</h3>
            <p>Просмотр товаров и оформление заказов</p>
          </div>

          <div className="role-card" onClick={() => handleRoleClick('Продавец')}>
            <div className="role-icon">💊</div>
            <h3>Продавец</h3>
            <p>Управление заказами и складом</p>
          </div>

          <div className="role-card" onClick={() => handleRoleClick('Админ')}>
            <div className="role-icon">🔧</div>
            <h3>Администратор</h3>
            <p>Управление системой и пользователями</p>
          </div>
        </div>
      </div>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<RoleSelection />} />
        <Route path="/admin" element={<AdminPanel />} />
        <Route path="/seller" element={<SellerPanel />} />
      </Routes>
    </Router>
  );
}

export default App;

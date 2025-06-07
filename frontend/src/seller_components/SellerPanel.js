// src/seller_components/SellerPanel.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SellerPanel.css';
import SellerReports from './SellerReports';

// Заглушки для будущих секций (пока выводят «В разработке»)
function InventorySection() {
  return (
    <div className="coming-soon">
      <h2>Инвентаризация</h2>
      <p>Этот раздел находится в разработке</p>
    </div>
  );
}

function OrdersSection() {
  return (
    <div className="coming-soon">
      <h2>Заказы</h2>
      <p>Этот раздел находится в разработке</p>
    </div>
  );
}

function StorageSection() {
  return (
    <div className="coming-soon">
      <h2>Склад</h2>
      <p>Этот раздел находится в разработке</p>
    </div>
  );
}

const sections = [
  { name: 'Инвентаризация', icon: '📦' },
  { name: 'Заказы', icon: '📋' },
  { name: 'Склад', icon: '🏭' },
  { name: 'Отчёты', icon: '📊' },
];

function SellerPanel() {
  const [selectedSection, setSelectedSection] = useState(sections[0].name);
  const navigate = useNavigate();

  return (
    <div className="seller-panel">
      <aside className="sidebar">
        <div className="sidebar-header">
          <h1 className="seller-title">АптекаПРО</h1>
          <p className="seller-subtitle">Панель Продавца</p>
        </div>

        <nav className="sidebar-nav">
          {sections.map((section) => (
            <button
              key={section.name}
              className={`nav-item ${
                selectedSection === section.name ? 'active' : ''
              }`}
              onClick={() => setSelectedSection(section.name)}
            >
              <span className="nav-icon">{section.icon}</span>
              <span className="nav-text" style={{ textAlign: 'left' }}>
                {section.name}
              </span>
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <button className="logout-btn" onClick={() => navigate('/')}>
            <span className="logout-icon">🚪</span>
            Выйти из системы
          </button>
        </div>
      </aside>

      <main className="main-content">
        {selectedSection === 'Инвентаризация' ? (
          <InventorySection />
        ) : selectedSection === 'Заказы' ? (
          <OrdersSection />
        ) : selectedSection === 'Склад' ? (
          <StorageSection />
        ) : selectedSection === 'Отчёты' ? (
          <SellerReports />
        ) : (
          <div className="coming-soon">
            <h2>{selectedSection}</h2>
            <p>Этот раздел находится в разработке</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default SellerPanel;

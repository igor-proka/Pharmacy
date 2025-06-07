// src/components/AdminPanel.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminPanel.css';
import WaysOfUseSection from './WaysOfUseSection';
import MedicamentTypesSection from './MedicamentTypesSection';
import MedicamentUseSection from './MedicamentUseSection';
import PatientsSection from './PatientsSection';
import DoctorsSection from './DoctorsSection';
import CustomersSection from './CustomersSection';
import SuppliersSection from './SuppliersSection';
import UnitsOfMeasurementSection from './UnitsOfMeasurementSection';
import MedicamentsSection from './MedicamentsSection';
import MedicamentPackagesSection from './MedicamentPackagesSection';
import MixturesSection from './MixturesSection';
import PillsSection from './PillsSection';
import PowdersSection from './PowdersSection';
import SolutionsSection from './SolutionsSection';
import OintmentsSection from './OintmentsSection';
import TincturesSection from './TincturesSection';
import DiseasesSection from './DiseasesSection';
import PrescriptionsSection from './PrescriptionsSection';
import PrescriptionsContentSection from './PrescriptionsContentSection';
import StorageSection from './StorageSection'
import SuppliesSection from './SuppliesSection'
import PreparationMethodsSection from './PreparationMethodsSection'
import TechnologiesSection from './TechnologiesSection'
import TechnologyComponentsSection from './TechnologyComponentsSection'
import OrdersSection from './OrdersSection'
import ProductionSection from './ProductionSection'
import WaitingMedicamentsSection from './WaitingMedicamentsSection'
import ReservedMedicamentsSection from './ReservedMedicamentsSection'

const sections = [
  { name: 'Возможные способы использования лекарств', icon: '💊' },
  { name: 'Типы лекарств', icon: '🏷️' },
  { name: 'Способы применения медикаментов', icon: '📋' },
  { name: 'Пациенты', icon: '👨‍⚕️' },
  { name: 'Доктора', icon: '👩‍⚕️' },
  { name: 'Покупатели', icon: '🛒' },
  { name: 'Поставщики', icon: '🚚' },
  { name: 'Единицы измерения', icon: '📏' },
  { name: 'Медикаменты', icon: '🧪' },
  { name: 'Пачки лекарств', icon: '📦' },
  { name: 'Микстуры', icon: '🍶' },
  { name: 'Таблетки', icon: '💊' },
  { name: 'Порошки', icon: '🧂' },
  { name: 'Растворы', icon: '🧴' },
  { name: 'Мази', icon: '🧴' },
  { name: 'Настойки', icon: '🍵' },
  { name: 'Болезни', icon: '🤒' },
  { name: 'Рецепты', icon: '📝' },
  { name: 'Содержание рецептов', icon: '📑' },
  { name: 'Склад', icon: '🏭' },
  { name: 'Поставки лекарств', icon: '📦' },
  { name: 'Методы приготовления лекарств', icon: '🔬' },
  { name: 'Справочник технологий', icon: '📚' },
  { name: 'Компоненты для приготовления', icon: '🧩' },
  { name: 'Заказы', icon: '📋' },
  { name: 'Производство лекарств', icon: '🏗️' },
  { name: 'Ожидание поставок лекарств', icon: '⏳' },
  { name: 'Зарезервированные лекарства', icon: '🔒' }
];

function AdminPanel() {
  const [selectedSection, setSelectedSection] = useState(sections[0].name);
  const navigate = useNavigate();

  const handleLogout = () => {
    // Здесь можно добавить очистку сессии или токена (если бы он был)
    navigate('/');
  };

  return (
     <div className="admin-panel">
          <aside className="sidebar">
            <div className="sidebar-header">
              <h1 className="admin-title">АптекаПРО</h1>
              <p className="admin-subtitle">Административная панель</p>
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
        {selectedSection === 'Возможные способы использования лекарств' ? (
          <WaysOfUseSection />
        ) : selectedSection === 'Типы лекарств' ? (
          <MedicamentTypesSection />
        ) : selectedSection === 'Способы применения медикаментов' ? (
          <MedicamentUseSection />
        ) : selectedSection === 'Пациенты' ? (
          <PatientsSection />
        ) : selectedSection === 'Доктора' ? (
          <DoctorsSection />
        ) : selectedSection === 'Покупатели' ? (
          <CustomersSection />
        ) : selectedSection === 'Поставщики' ? (
          <SuppliersSection />
        ) : selectedSection === 'Единицы измерения' ? (
          <UnitsOfMeasurementSection />
        ) : selectedSection === 'Медикаменты' ? (
          <MedicamentsSection />
        ) : selectedSection === 'Пачки лекарств' ? (
          <MedicamentPackagesSection />
        ) : selectedSection === 'Микстуры' ? (
          <MixturesSection />
        ) : selectedSection === 'Таблетки' ? (
          <PillsSection />
        ) : selectedSection === 'Порошки' ? (
          <PowdersSection />
        ) : selectedSection === 'Растворы' ? (
          <SolutionsSection />
        ) : selectedSection === 'Мази' ? (
          <OintmentsSection />
        ) : selectedSection === 'Настойки' ? (
          <TincturesSection />
        ) : selectedSection === 'Болезни' ? (
          <DiseasesSection />
        ) : selectedSection === 'Рецепты' ? (
          <PrescriptionsSection />
        ) : selectedSection === 'Содержание рецептов' ? (
          <PrescriptionsContentSection />
        ) : selectedSection === 'Склад' ? (
          <StorageSection />
        ) : selectedSection === 'Поставки лекарств' ? (
          <SuppliesSection />
        ) : selectedSection === 'Методы приготовления лекарств' ? (
          <PreparationMethodsSection />
        ) : selectedSection === 'Справочник технологий' ? (
          <TechnologiesSection />
        ) : selectedSection === 'Компоненты для приготовления' ? (
          <TechnologyComponentsSection />
        ) : selectedSection === 'Заказы' ? (
          <OrdersSection />
        ) : selectedSection === 'Производство лекарств' ? (
          <ProductionSection />
        ) : selectedSection === 'Ожидание поставок лекарств' ? (
          <WaitingMedicamentsSection />
        ) : selectedSection === 'Зарезервированные лекарства' ? (
          <ReservedMedicamentsSection />
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

export default AdminPanel;

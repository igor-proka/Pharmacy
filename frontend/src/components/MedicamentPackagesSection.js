// src/components/MedicamentPackagesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function MedicamentPackagesSection() {
  const [packages, setPackages] = useState([]);
  const [medicaments, setMedicaments] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentId: '',
    price: '',
    criticalAmount: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/medicament-packages').then(res => res.json()),
      fetch('/api/medicaments').then(res => res.json()),
    ])
      .then(([pkgs, meds]) => {
        setPackages(pkgs);
        setMedicaments(meds);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setIsLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      medicamentId: '',
      price: '',
      criticalAmount: '',
    });
    setEditingItem(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { medicamentId, price, criticalAmount } = form;
    if (!medicamentId || !price || !criticalAmount) {
      alert('Пожалуйста, заполните все поля');
      return;
    }

    const medicament = medicaments.find(m => m.id === Number(medicamentId));
    if (!medicament) {
      alert('Неверный медикамент');
      return;
    }

    const payload = {
      medicament,
      price: parseFloat(price),
      criticalAmount: parseInt(criticalAmount, 10),
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/medicament-packages/${editingItem.id}`
      : '/api/medicament-packages';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => res.ok ? res.json() : Promise.reject())
      .then(saved => {
        setPackages(prev => {
          if (editingItem) {
            return prev.map(p => p.id === saved.id ? saved : p);
          }
          return [...prev, saved];
        });
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = (pkg) => {
    setEditingItem(pkg);
    setForm({
      medicamentId: pkg.medicament.id.toString(),
      price: pkg.price,
      criticalAmount: pkg.criticalAmount,
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = (pkg) => {
    if (!window.confirm(`Удалить пачку медикамента "${pkg.medicament.name}"?`)) return;

    fetch(`/api/medicament-packages/${pkg.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setPackages(prev => prev.filter(p => p.id !== pkg.id));
      })
      .catch(() => alert('Не удалось удалить'));
  };

  const filteredPackages = filterMedId
    ? packages.filter(p => p.medicament.id === Number(filterMedId))
    : packages;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Пачки лекарств</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentId"
          value={form.medicamentId}
          onChange={handleChange}
        >
          <option value="">Медикамент</option>
          {medicaments.map(m => (
            <option key={m.id} value={m.id}>{m.name}</option>
          ))}
        </select>

        <input
          type="number"
          name="price"
          className="form-input"
          placeholder="Цена"
          value={form.price}
          onChange={handleChange}
          step="0.01"
        />

        <input
          type="number"
          name="criticalAmount"
          className="form-input"
          placeholder="Критическое кол-во"
          value={form.criticalAmount}
          onChange={handleChange}
        />

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editingItem ? 'Сохранить' : 'Добавить'}
          </button>
          {editingItem && (
            <button className="btn btn-cancel" onClick={resetForm}>
              Отмена
            </button>
          )}
        </div>
      </div>

      <div className="filter-container">
        <select
          className="form-input"
          value={filterMedId}
          onChange={e => setFilterMedId(e.target.value)}
        >
          <option value="">Все медикаменты</option>
          {medicaments.map(m => (
            <option key={m.id} value={m.id}>{m.name}</option>
          ))}
        </select>
      </div>

      {isLoading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Медикамент</th>
                <th>Цена</th>
                <th>Крит. кол-во</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filteredPackages.length ? filteredPackages.map(pkg => (
                <tr key={pkg.id}>
                  <td>{pkg.medicament.name}</td>
                  <td>{pkg.price}</td>
                  <td>{pkg.criticalAmount}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(pkg)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(pkg)}>Удалить</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="4" className="no-data">Нет данных</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default MedicamentPackagesSection;

// src/components/TechnologiesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function TechnologiesSection() {
  const [technologies, setTechnologies] = useState([]);
  const [packages, setPackages] = useState([]);
  const [methods, setMethods] = useState([]);
  const [filterPackageId, setFilterPackageId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    preparationTime: '',
    amount: '',
    preparationMethodId: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetch('/api/technologies').then(r => r.json()),
      fetch('/api/medicament-packages').then(r => r.json()),
      fetch('/api/preparation-methods').then(r => r.json()),
    ])
      .then(([techData, pkgData, methodData]) => {
        setTechnologies(techData);
        setPackages(pkgData);
        setMethods(methodData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      preparationTime: '',
      amount: '',
      preparationMethodId: '',
    });
    setEditingItem(null);
  };

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { medicamentPackageId, preparationTime, amount, preparationMethodId } = form;
    if (!medicamentPackageId || !preparationTime || !amount || !preparationMethodId) {
      alert('Заполните все поля');
      return;
    }
    const pkg = packages.find(p => p.id === Number(medicamentPackageId));
    const method = methods.find(m => m.id === Number(preparationMethodId));
    if (!pkg || !method) {
      alert('Выберите корректные пачку и метод');
      return;
    }
    const payload = {
      medicament: pkg,
      preparationTime: parseInt(preparationTime, 10),
      amount: parseInt(amount, 10),
      preparationMethod: method,
    };
    const methodType = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/technologies/${editingItem.id}`
      : '/api/technologies';
    fetch(url, {
      method: methodType,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/technologies').then(r => r.json()))
      .then(all => {
        setTechnologies(all);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setForm({
      medicamentPackageId: item.medicament.id.toString(),
      preparationTime: item.preparationTime.toString(),
      amount: item.amount.toString(),
      preparationMethodId: item.preparationMethod.id.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить технологию?')) return;
    fetch(`/api/technologies/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        return fetch('/api/technologies');
      })
      .then(r => r.json())
      .then(all => setTechnologies(all))
      .catch(() => alert('Ошибка при удалении'));
  };

  // Фильтр по пачке (medicamentPackage.id)
  const filtered = filterPackageId
    ? technologies.filter(
        t => t.medicament.id === Number(filterPackageId)
      )
    : technologies;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Справочник технологий</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={handleFormChange}
        >
          <option value="">Пачка (медикамент — цена)</option>
          {packages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="preparationTime"
          className="form-input"
          placeholder="Время приготовления (мин)"
          value={form.preparationTime}
          onChange={handleFormChange}
        />

        <input
          type="number"
          name="amount"
          className="form-input"
          placeholder="Количество"
          value={form.amount}
          onChange={handleFormChange}
        />

        <select
          className="form-input"
          name="preparationMethodId"
          value={form.preparationMethodId}
          onChange={handleFormChange}
        >
          <option value="">Метод приготовления</option>
          {methods.map(m => (
            <option key={m.id} value={m.id}>
              {m.name}
            </option>
          ))}
        </select>

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

      {/* Фильтр по пачке */}
      <div className="form-container">
        <select
          className="form-input"
          value={filterPackageId}
          onChange={e => setFilterPackageId(e.target.value)}
        >
          <option value="">Все пачки</option>
          {packages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Пачка (медикамент — цена)</th>
                <th>Время (мин)</th>
                <th>Количество</th>
                <th>Метод</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.id}>
                    <td>
                      {item.medicament.medicament.name} — {item.medicament.price}₽
                    </td>
                    <td>{item.preparationTime}</td>
                    <td>{item.amount}</td>
                    <td>{item.preparationMethod.name}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => handleEdit(item)}
                      >
                        Редактировать
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(item)}
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="no-data">
                    Нет данных
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default TechnologiesSection;

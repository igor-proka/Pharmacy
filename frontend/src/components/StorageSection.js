// src/components/StorageSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function StorageSection() {
  const [storageData, setStorageData] = useState([]);
  const [packages, setPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    availableAmount: '',
    originalAmount: '',
    receiptDatetime: '',
    manufactureDate: '',
    shelfLife: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка данных: записи склада + все пачки
  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetch('/api/storage').then(r => r.json()),
      fetch('/api/medicament-packages').then(r => r.json()),
    ])
      .then(([storageList, pkgList]) => {
        setStorageData(storageList);
        setPackages(pkgList);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      availableAmount: '',
      originalAmount: '',
      receiptDatetime: '',
      manufactureDate: '',
      shelfLife: '',
    });
    setEditingItem(null);
  };

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const {
      medicamentPackageId,
      availableAmount,
      originalAmount,
      receiptDatetime,
      manufactureDate,
      shelfLife,
    } = form;

    if (
      !medicamentPackageId ||
      !availableAmount ||
      !originalAmount ||
      !receiptDatetime ||
      !shelfLife
    ) {
      alert('Заполните все обязательные поля');
      return;
    }

    const pkg = packages.find(p => p.id === Number(medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку');
      return;
    }

    const payload = {
      medicamentPackage: pkg,
      availableAmount: parseInt(availableAmount, 10),
      originalAmount: parseInt(originalAmount, 10),
      receiptDatetime,
      manufactureDate: manufactureDate || null,
      shelfLife: parseInt(shelfLife, 10),
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem ? `/api/storage/${editingItem.id}` : '/api/storage';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/storage').then(r => r.json()))
      .then(all => {
        setStorageData(all);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setForm({
      medicamentPackageId: item.medicamentPackage.id.toString(),
      availableAmount: item.availableAmount.toString(),
      originalAmount: item.originalAmount.toString(),
      // проверяем, что receiptDatetime не null
      receiptDatetime: item.receiptDatetime
        ? item.receiptDatetime.replace('Z', '')
        : '',
      manufactureDate: item.manufactureDate || '',
      shelfLife: item.shelfLife != null
        ? item.shelfLife.toString()
        : '',
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить запись со склада?')) return;
    fetch(`/api/storage/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        return fetch('/api/storage');
      })
      .then(r => r.json())
      .then(all => setStorageData(all))
      .catch(() => alert('Ошибка при удалении'));
  };

  // Фильтр по medicament_id (берём id медикамента из medicamentPackage.medicament.id)
  const filtered = filterMedId
    ? storageData.filter(
        s => s.medicamentPackage.medicament.id === Number(filterMedId)
      )
    : storageData;

  // Уникальный список медикаментов (название) для фильтра
  const uniqueMedMap = {};
  packages.forEach(p => {
    const med = p.medicament;
    uniqueMedMap[med.id] = med.name;
  });
  const uniqueMedList = Object.entries(uniqueMedMap).map(([id, label]) => ({
    id: Number(id),
    label,
  }));

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Склад</h2>

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
          name="availableAmount"
          className="form-input"
          placeholder="Доступное количество"
          value={form.availableAmount}
          onChange={handleFormChange}
        />

        <input
          type="number"
          name="originalAmount"
          className="form-input"
          placeholder="Начальное количество"
          value={form.originalAmount}
          onChange={handleFormChange}
        />

        <input
          type="datetime-local"
          name="receiptDatetime"
          className="form-input"
          value={form.receiptDatetime}
          onChange={handleFormChange}
        />

        <input
          type="date"
          name="manufactureDate"
          className="form-input"
          value={form.manufactureDate}
          onChange={handleFormChange}
        />

        <input
          type="number"
          name="shelfLife"
          className="form-input"
          placeholder="Срок годности (месяцы)"
          value={form.shelfLife}
          onChange={handleFormChange}
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

      {/* Фильтр по медикаменту */}
      <div className="form-container">
        <select
          className="form-input"
          value={filterMedId}
          onChange={e => setFilterMedId(e.target.value)}
        >
          <option value="">Все медикаменты</option>
          {uniqueMedList.map(m => (
            <option key={m.id} value={m.id}>
              {m.label}
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
                <th>Медикамент (цена)</th>
                <th>Доступно</th>
                <th>Начальное</th>
                <th>Дата поступления</th>
                <th>Дата изготовления</th>
                <th>Срок годности (месяцы)</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.id}>
                    <td>
                      {item.medicamentPackage.medicament.name} —{' '}
                      {item.medicamentPackage.price}₽
                    </td>
                    <td>{item.availableAmount}</td>
                    <td>{item.originalAmount}</td>
                    <td>
                      {new Date(item.receiptDatetime).toLocaleString('sv-SE', {
                        year: 'numeric',
                        month: '2-digit',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit'
                      }).replace(' ', 'T')}
                    </td>
                    <td>{item.manufactureDate || '—'}</td>
                    <td>{item.shelfLife}</td>
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
                  <td colSpan="7" className="no-data">
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

export default StorageSection;

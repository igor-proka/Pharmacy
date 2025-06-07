// src/components/OintmentsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function OintmentsSection() {
  const [ointments, setOintments] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    activeIngredient: '',
    volumeMl: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка данных
  useEffect(() => {
    Promise.all([
      fetch('/api/ointments').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([ointmentData, pkgData]) => {
        setOintments(ointmentData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сброс формы
  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      activeIngredient: '',
      volumeMl: '',
    });
    setEditing(null);
  };

  // Обработка сабмита
  const handleSubmit = () => {
    const pkg = ointmentPackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку мази');
      return;
    }
    if (!form.activeIngredient || form.volumeMl === '') {
      alert('Заполните все поля');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      activeIngredient: form.activeIngredient,
      volumeMl: parseInt(form.volumeMl, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/ointments/${editing.medicamentId}` : '/api/ointments';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setOintments(prev => {
          if (editing) {
            return prev.map(o => (o.medicamentId === saved.medicamentId ? saved : o));
          }
          return [...prev, saved];
        });
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  // Редактировать
  const handleEdit = item => {
    setEditing(item);
    setForm({
      medicamentPackageId: item.medicamentPackage.id.toString(),
      activeIngredient: item.activeIngredient,
      volumeMl: item.volumeMl.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удалить
  const handleDelete = item => {
    if (!window.confirm('Удалить мазь?')) return;
    fetch(`/api/ointments/${item.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setOintments(prev => prev.filter(o => o.medicamentId !== item.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Фильтруем только пачки мазей
  const ointmentPackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'мазь'
  );

  // Уникальный список медикаментов из ointmentPackages
  const uniqueMedicamentsMap = {};
  ointmentPackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтрация мазей по выбранному медикаменту
  const filtered = filterMedId
    ? ointments.filter(o => o.medicamentPackage.medicament.id === Number(filterMedId))
    : ointments;

  const getMedName = item => item.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Мази</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку мази</option>
          {ointmentPackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="text"
          name="activeIngredient"
          className="form-input"
          placeholder="Активный компонент"
          value={form.activeIngredient}
          onChange={e => setForm({ ...form, activeIngredient: e.target.value })}
        />

        <input
          type="number"
          name="volumeMl"
          className="form-input"
          placeholder="Объем (мл)"
          value={form.volumeMl}
          onChange={e => setForm({ ...form, volumeMl: e.target.value })}
        />

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editing ? 'Сохранить' : 'Добавить'}
          </button>
          {editing && (
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
          <option value="">Все медикаменты (мази)</option>
          {uniqueMedicaments.map(m => (
            <option key={m.id} value={m.id}>
              {m.name}
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
                <th>Медикамент</th>
                <th>Активный компонент</th>
                <th>Объем (мл)</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.medicamentId}>
                    <td>{getMedName(item)}</td>
                    <td>{item.activeIngredient}</td>
                    <td>{item.volumeMl}</td>
                    <td className="actions-cell">
                      <button className="btn btn-edit" onClick={() => handleEdit(item)}>
                        Редактировать
                      </button>
                      <button className="btn btn-delete" onClick={() => handleDelete(item)}>
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="no-data">
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

export default OintmentsSection;

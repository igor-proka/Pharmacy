// src/components/PowdersSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function PowdersSection() {
  const [powders, setPowders] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    isCompound: false,
    packageWeight: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка списка порошков и всех пачек
  useEffect(() => {
    Promise.all([
      fetch('/api/powders').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([powdersData, pkgData]) => {
        setPowders(powdersData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сброс формы
  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      isCompound: false,
      packageWeight: '',
    });
    setEditing(null);
  };

  // Обработчик отправки формы
  const handleSubmit = () => {
    const pkg = powdersPackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку порошка');
      return;
    }
    if (form.packageWeight === '') {
      alert('Заполните вес упаковки');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      isCompound: form.isCompound,
      packageWeight: parseInt(form.packageWeight, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/powders/${editing.medicamentId}` : '/api/powders';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setPowders(prev => {
          if (editing) {
            return prev.map(p => (p.medicamentId === saved.medicamentId ? saved : p));
          }
          return [...prev, saved];
        });
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  // Редактирование записи
  const handleEdit = item => {
    setEditing(item);
    setForm({
      medicamentPackageId: item.medicamentPackage.id.toString(),
      isCompound: item.isCompound,
      packageWeight: item.packageWeight.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удаление записи
  const handleDelete = item => {
    if (!window.confirm('Удалить порошок?')) return;
    fetch(`/api/powders/${item.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setPowders(prev => prev.filter(p => p.medicamentId !== item.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Отбираем только пачки, где тип медикамента "порошок"
  const powdersPackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'порошок'
  );

  // Составляем уникальный список медикаментов (id + name) из powdersPackages
  const uniqueMedicamentsMap = {};
  powdersPackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтрация порошков по выбранному медикаменту
  const filtered = filterMedId
    ? powders.filter(p => p.medicamentPackage.medicament.id === Number(filterMedId))
    : powders;

  const getMedName = item => item.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Порошки</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку порошка</option>
          {powdersPackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <label className="checkbox-label">
          <input
            type="checkbox"
            name="isCompound"
            checked={form.isCompound}
            onChange={e => setForm({ ...form, isCompound: e.target.checked })}
          />
          Составной порошок
        </label>

        <input
          type="number"
          name="packageWeight"
          className="form-input"
          placeholder="Вес упаковки (г)"
          value={form.packageWeight}
          onChange={e => setForm({ ...form, packageWeight: e.target.value })}
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
          <option value="">Все медикаменты (порошок)</option>
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
                <th>Составной</th>
                <th>Вес упаковки (г)</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.medicamentId}>
                    <td>{getMedName(item)}</td>
                    <td>{item.isCompound ? 'Да' : 'Нет'}</td>
                    <td>{item.packageWeight}</td>
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

export default PowdersSection;

// src/components/SolutionsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function SolutionsSection() {
  const [solutions, setSolutions] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    concentration: '',
    phLevel: '',
    volumeMl: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка растворов и всех пачек
  useEffect(() => {
    Promise.all([
      fetch('/api/solutions').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([solData, pkgData]) => {
        setSolutions(solData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сброс формы
  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      concentration: '',
      phLevel: '',
      volumeMl: '',
    });
    setEditing(null);
  };

  // Отправка формы
  const handleSubmit = () => {
    const pkg = solutionPackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку раствора');
      return;
    }
    if (!form.concentration || !form.phLevel || !form.volumeMl) {
      alert('Заполните все поля');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      concentration: form.concentration,  // строка в формате "0.00"
      phLevel: form.phLevel,              // строка в формате "0.00"
      volumeMl: parseInt(form.volumeMl, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/solutions/${editing.medicamentId}` : '/api/solutions';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setSolutions(prev => {
          if (editing) {
            return prev.map(s => (s.medicamentId === saved.medicamentId ? saved : s));
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
      concentration: item.concentration.toString(),
      phLevel: item.phLevel.toString(),
      volumeMl: item.volumeMl.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удаление
  const handleDelete = item => {
    if (!window.confirm('Удалить раствор?')) return;
    fetch(`/api/solutions/${item.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setSolutions(prev => prev.filter(s => s.medicamentId !== item.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Отбираем только пачки, где тип медикамента "раствор"
  const solutionPackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'раствор'
  );

  // Уникальный список медикаментов из solutionPackages
  const uniqueMedicamentsMap = {};
  solutionPackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтрация растворов по выбранному медикаменту
  const filtered = filterMedId
    ? solutions.filter(s => s.medicamentPackage.medicament.id === Number(filterMedId))
    : solutions;

  const getMedName = item => item.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Растворы</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку раствора</option>
          {solutionPackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="concentration"
          className="form-input"
          step="0.01"
          placeholder="Концентрация (0.00)"
          value={form.concentration}
          onChange={e => setForm({ ...form, concentration: e.target.value })}
        />

        <input
          type="number"
          name="phLevel"
          className="form-input"
          step="0.01"
          placeholder="pH уровень (0.00)"
          value={form.phLevel}
          onChange={e => setForm({ ...form, phLevel: e.target.value })}
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
          <option value="">Все медикаменты (растворы)</option>
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
                <th>Концентрация</th>
                <th>pH уровень</th>
                <th>Объем (мл)</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.medicamentId}>
                    <td>{getMedName(item)}</td>
                    <td>{item.concentration}</td>
                    <td>{item.phLevel}</td>
                    <td>{item.volumeMl}</td>
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

export default SolutionsSection;

// src/components/PillsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function PillsSection() {
  const [pills, setPills] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    massPerPill: '',
    pillsPerPack: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка данных
  useEffect(() => {
    Promise.all([
      fetch('/api/pills').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([pillsData, pkgData]) => {
        setPills(pillsData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сбрасываем форму
  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      massPerPill: '',
      pillsPerPack: '',
    });
    setEditing(null);
  };

  // Сабмит формы
  const handleSubmit = () => {
    const pkg = pillsPackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку таблеток');
      return;
    }
    if (!form.massPerPill || !form.pillsPerPack) {
      alert('Заполните все поля');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      massPerPill: parseInt(form.massPerPill, 10),
      pillsPerPack: parseInt(form.pillsPerPack, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/pills/${editing.medicamentId}` : '/api/pills';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setPills(prev => {
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
  const handleEdit = pill => {
    setEditing(pill);
    setForm({
      medicamentPackageId: pill.medicamentPackage.id.toString(),
      massPerPill: pill.massPerPill.toString(),
      pillsPerPack: pill.pillsPerPack.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удаление записи
  const handleDelete = pill => {
    if (!window.confirm('Удалить таблетки?')) return;
    fetch(`/api/pills/${pill.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setPills(prev => prev.filter(p => p.medicamentId !== pill.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Отбираем только те пачки, у которых тип медикамента "таблетка"
  const pillsPackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'таблетка'
  );

  // Собираем уникальные медикаменты (id + name) из pillsPackages
  const uniqueMedicamentsMap = {};
  pillsPackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтруем список таблеток по выбранному медикаменту
  const filtered = filterMedId
    ? pills.filter(p => p.medicamentPackage.medicament.id === Number(filterMedId))
    : pills;

  const getMedName = pill => pill.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Таблетки</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку таблеток</option>
          {pillsPackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="massPerPill"
          className="form-input"
          placeholder="Масса на таблетку (мг)"
          value={form.massPerPill}
          onChange={e => setForm({ ...form, massPerPill: e.target.value })}
        />

        <input
          type="number"
          name="pillsPerPack"
          className="form-input"
          placeholder="Таблеток в упаковке"
          value={form.pillsPerPack}
          onChange={e => setForm({ ...form, pillsPerPack: e.target.value })}
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
          <option value="">Все медикаменты (таблетки)</option>
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
                <th>Масса на таблетку (мг)</th>
                <th>Таблеток в упаковке</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(pill => (
                  <tr key={pill.medicamentId}>
                    <td>{getMedName(pill)}</td>
                    <td>{pill.massPerPill}</td>
                    <td>{pill.pillsPerPack}</td>
                    <td className="actions-cell">
                      <button className="btn btn-edit" onClick={() => handleEdit(pill)}>
                        Редактировать
                      </button>
                      <button className="btn btn-delete" onClick={() => handleDelete(pill)}>
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

export default PillsSection;

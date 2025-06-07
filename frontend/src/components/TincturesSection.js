// src/components/TincturesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function TincturesSection() {
  const [tinctures, setTinctures] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    alcoholPercentage: '',
    material: '',
    volumeMl: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/tinctures').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([tinctData, pkgData]) => {
        setTinctures(tinctData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      alcoholPercentage: '',
      material: '',
      volumeMl: '',
    });
    setEditing(null);
  };

  const handleSubmit = () => {
    const pkg = tincturePackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку настойки');
      return;
    }
    if (!form.alcoholPercentage || !form.material || form.volumeMl === '') {
      alert('Заполните все поля');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      alcoholPercentage: form.alcoholPercentage,
      material: form.material,
      volumeMl: parseInt(form.volumeMl, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/tinctures/${editing.medicamentId}` : '/api/tinctures';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setTinctures(prev => {
          if (editing) {
            return prev.map(t => (t.medicamentId === saved.medicamentId ? saved : t));
          }
          return [...prev, saved];
        });
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditing(item);
    setForm({
      medicamentPackageId: item.medicamentPackage.id.toString(),
      alcoholPercentage: item.alcoholPercentage.toString(),
      material: item.material,
      volumeMl: item.volumeMl.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить настойку?')) return;
    fetch(`/api/tinctures/${item.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setTinctures(prev => prev.filter(t => t.medicamentId !== item.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Берём только пачки, где тип медикамента "настойка"
  const tincturePackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'настойка'
  );

  // Уникальный список медикаментов из tincturePackages
  const uniqueMedicamentsMap = {};
  tincturePackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтрация
  const filtered = filterMedId
    ? tinctures.filter(t => t.medicamentPackage.medicament.id === Number(filterMedId))
    : tinctures;

  const getMedName = item => item.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Настойки</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку настойки</option>
          {tincturePackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="alcoholPercentage"
          className="form-input"
          step="0.01"
          placeholder="Алкоголь, % (0.00)"
          value={form.alcoholPercentage}
          onChange={e => setForm({ ...form, alcoholPercentage: e.target.value })}
        />

        <input
          type="text"
          name="material"
          className="form-input"
          placeholder="Материал"
          value={form.material}
          onChange={e => setForm({ ...form, material: e.target.value })}
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
          <option value="">Все медикаменты (настойки)</option>
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
                <th>Алкоголь, %</th>
                <th>Материал</th>
                <th>Объем (мл)</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.medicamentId}>
                    <td>{getMedName(item)}</td>
                    <td>{item.alcoholPercentage}</td>
                    <td>{item.material}</td>
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

export default TincturesSection;
